/*
 * @(#)ConcurrentSessionFilter.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.IOException;
import java.io.Serializable;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.nlh4j.util.BeanUtils;

/**
 * An extended class of {@link org.springframework.security.web.session.ConcurrentSessionFilter}
 * to clear remember authentication while session has been expired
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class ConcurrentSessionFilter
		extends org.springframework.security.web.session.ConcurrentSessionFilter
		implements Serializable {

	/** */
    private static final long serialVersionUID = 1L;

    /** {@link RememberMeServices} */
    @Inject
    private RememberMeServices rememberMeServices;
    private SessionRegistry sessionRegistry;
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

	/**
	 * Initialize a new instance of {@link ConcurrentSessionFilter}
	 *
	 * @param sessionRegistry {@link SessionRegistry}
	 */
	public ConcurrentSessionFilter(SessionRegistry sessionRegistry) {
        super(sessionRegistry);
        this.sessionRegistry = sessionRegistry;
    }
	/**
	 * Initialize a new instance of {@link ConcurrentSessionFilter}
	 *
	 * @param sessionRegistry {@link SessionRegistry}
	 * @param expiredUrl expired URL
	 */
    @SuppressWarnings("deprecation")
	public ConcurrentSessionFilter(SessionRegistry sessionRegistry, String expiredUrl) {
        super(sessionRegistry, expiredUrl);
        this.sessionRegistry = sessionRegistry;
    }
	/**
	 * Initialize a new instance of {@link ConcurrentSessionFilter}
	 *
	 * @param sessionRegistry {@link SessionRegistry}
	 * @param expiredUrl expired URL
	 */
    public ConcurrentSessionFilter(SessionRegistry sessionRegistry, SessionInformationExpiredStrategy sessionInformationExpiredStrategy) {
        super(sessionRegistry, sessionInformationExpiredStrategy);
        this.sessionRegistry = sessionRegistry;
        this.sessionInformationExpiredStrategy = sessionInformationExpiredStrategy;
    }

    /**
	 * Get the sessionRegistry
	 *
	 * @return the sessionRegistry
	 */
	protected final SessionRegistry getSessionRegistry() {
		return this.sessionRegistry;
	}

    /**
	 * Get the sessionInformationExpiredStrategy
	 *
	 * @return the sessionInformationExpiredStrategy
	 */
	protected final SessionInformationExpiredStrategy getSessionInformationExpiredStrategy() {
		return this.sessionInformationExpiredStrategy;
	}

	/**
	 * Get the rememberMeServices
	 *
	 * @return the rememberMeServices
	 */
	protected final RememberMeServices getRememberMeServices() {
		return rememberMeServices;
	}

    /* (Non-Javadoc)
     * @see org.springframework.security.web.session.ConcurrentSessionFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    		throws IOException, ServletException {

    	// remove remember if necessary while expired
    	if (BeanUtils.isInstanceOf(req, HttpServletRequest.class)
    			&& BeanUtils.isInstanceOf(res, HttpServletResponse.class)
    			&& this.getRememberMeServices() != null) {
	    	HttpServletRequest request = BeanUtils.safeType(req, HttpServletRequest.class);
	    	HttpServletResponse response = BeanUtils.safeType(res, HttpServletResponse.class);
	        HttpSession session = request.getSession(false);
	        if (session != null) {
	            SessionInformation info = this.getSessionRegistry().getSessionInformation(session.getId());
	            if (info != null && info.isExpired()) {
	            	this.getRememberMeServices().loginFail(request, response);
	            }
	        }
    	}

    	// expired by super parent class
    	super.doFilter(req, res, chain);
    }
}
