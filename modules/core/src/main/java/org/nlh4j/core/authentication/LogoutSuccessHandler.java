/*
 * @(#)LogoutSuccessHandler.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.IOException;
import java.io.Serializable;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nlh4j.core.dto.UserDetails;
import org.nlh4j.core.service.UserService;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * After logout successful
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Component(value = "logoutSuccessHandler")
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler
	implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * SLF4J
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * {@link UserService}
	 */
    @Inject
	protected UserService userService;

	/*
	 * (Non-Javadoc)
	 * @see org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler#onLogoutSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
    @Override
    public final void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    		throws IOException, ServletException {
    	// log-out
    	this.doLogout(authentication);
    	// by super class
        this.justOnLogoutSuccess(request, response, authentication);
        // clear session if necessary
        SessionUtils.clearSessionValues();
    }

    /**
     * Perform actions while logging out if necessary.<br>
     * Default is clear online information.<br>
     * TODO Children classes maybe override this method for doing something if necessary
     *
     * @param authentication authentication information
     */
    protected void doLogout(Authentication authentication) {
    	// log-out
    	UserDetails user = BeanUtils.safeType(authentication.getPrincipal(), UserDetails.class);
    	this.userService.logout(user);
    }

    /**
     * Just call super class {@link SimpleUrlLogoutSuccessHandler#onLogoutSuccess(HttpServletRequest, HttpServletResponse, Authentication)}
     * for children extended classes
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param authentication {@link Authentication}
     * @throws IOException if failed
     * @throws ServletException if failed
     */
    protected void justOnLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        super.onLogoutSuccess(request, response, authentication);
    }
}
