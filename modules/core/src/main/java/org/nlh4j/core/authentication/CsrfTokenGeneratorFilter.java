/*
 * @(#)CsrfTokenGeneratorFilter.java 1.0 Jun 1, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.util.LocaleUtils;

/**
 * To prevent Cross-Site Request Forgery attack, I enable csrf in my spring security context.
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version 1.0 Jun 1, 2015
 */
public class CsrfTokenGeneratorFilter extends AbstractOncePerRequestFilter {

	/** */
    private static final long serialVersionUID = 1L;

    /**
     * {@link LocaleResolver}
     */
    @Inject
    protected LocaleResolver localeResolver;

    /*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.authentication.AbstractOncePerRequestFilter#doBeforeFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected boolean doBeforeFilter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // filter CSRF key
		boolean filter = !StringUtils.hasText(AuthenticationUtils.generateCsrfToken(request, response, false, false));
		// resolve locale while filtering failed
		if (filter) LocaleUtils.resolveLocale(localeResolver, request, response);
		return filter;
	}
}
