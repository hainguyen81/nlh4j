/*
 * @(#)LoginAuthenticationFilter.java 1.0 Jun 5, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.LocaleResolver;

import org.nlh4j.core.dto.UserDetails;
import org.nlh4j.core.util.AuthenticationUtils;

/**
 * After login success, notify message to client
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version 1.0 Jun 5, 2015
 */
public class LoginAuthenticationFilter
    extends UsernamePasswordAuthenticationFilter
    implements Serializable {

	/** */
    private static final long serialVersionUID = 1L;
    /**
	 * SLF4J
	 */
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * {@link LocaleResolver}
     */
    @Inject
    protected LocaleResolver localeResolver;

	/*
	 * (Non-Javadoc)
	 * @see org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter#successfulAuthentication(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain, org.springframework.security.core.Authentication)
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

	    // resolve locale
        Locale locale = localeResolver.resolveLocale(request);
        UserDetails user = AuthenticationUtils.getProfile(authResult);
        boolean changeLocale = (locale == null && (user != null && user.getLocale() != null));
        changeLocale = (changeLocale || (user != null && locale != null && user.getLocale() != null
                && !locale.getLanguage().equalsIgnoreCase(user.getLocale().getLanguage())));
        // if current locale is different with user locale settings;
        // then applying user locale settings
        if (changeLocale) {
            localeResolver.setLocale(request, response, user.getLocale());
        }

        // success authentication
		super.successfulAuthentication(request, response, chain, authResult);
		logger.debug("Notify message");
	}
}
