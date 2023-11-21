/*
 * @(#)LocaleConfigurerFilter.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.authentication;

import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.web.core.dto.UserDto;
import org.nlh4j.web.core.service.UserService;

/**
 * An extended class of {@link org.nlh4j.core.authentication.LocaleConfigurerFilter}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Component(value = "baseLocaleConfigurerFilter")
public class LocaleConfigurerFilter
    extends org.nlh4j.core.authentication.LocaleConfigurerFilter {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * {@link UserService}
     */
    @Inject
    protected UserService userService;

    /* (Non-Javadoc)
     * @see org.nlh4j.core.authentication.LocaleConfigurerFilter#doAfterFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected final void doAfterFilter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // apply session user language
        Locale locale = LocaleContextHolder.getLocale();
        UserDto user = AuthenticationUtils.getCurrentProfile(UserDto.class);
        boolean changeLocale = (locale == null && (user != null && user.getLocale() != null));
        changeLocale = (changeLocale || (user != null && locale != null && user.getLocale() == null));
        changeLocale = (changeLocale || (user != null && locale != null && user.getLocale() != null
                && !locale.getLanguage().equalsIgnoreCase(user.getLocale().getLanguage())));
        if (changeLocale && user != null) {
            // update session user locale
            user.setLanguage(locale.getLanguage());
            AuthenticationUtils.invalidateAuthentication(user);
            // update database if necessary
            this.updateUserLocaleDb(user.getId(), locale);
        }
        // apply locale to response, cookie
        super.doAfterFilter(request, response);
    }
    /**
     * Update {@link UserDto} language into database.<br>
     * TODO Children classes maybe override and not using this method if not necessary
     *
     * @param uid user identity
     * @param locale locale language
     */
    protected void updateUserLocaleDb(Long uid, Locale locale) {
        this.userService.updateLocale(uid, locale, uid);
    }
}
