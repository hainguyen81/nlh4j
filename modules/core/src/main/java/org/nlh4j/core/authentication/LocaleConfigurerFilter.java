/*
 * @(#)CsrfTokenGeneratorFilter.java 1.0 Jun 1, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import org.nlh4j.util.LocaleUtils;

/**
 * To prevent locale attack
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version 1.0 Jun 1, 2015
 */
@Component(value = "localeConfigurerFilter")
public class LocaleConfigurerFilter extends AbstractOncePerRequestFilter {

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
		// セッションのタイムアウト判定を行う。
		Locale locale = LocaleUtils.resolveLocale(this.localeResolver, request, response);
		if (locale != null) LocaleContextHolder.setLocale(locale);
		return false;
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.authentication.AbstractOncePerRequestFilter#doAfterFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doAfterFilter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// apply locale to cookie
	    LocaleUtils.applyCookieLocale(request, response, LocaleContextHolder.getLocale(), "lang");
	    // reset locale holder
        LocaleContextHolder.resetLocaleContext();
	}
}
