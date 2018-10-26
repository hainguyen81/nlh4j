/*
 * @(#)AccessDeniedHandler.java 1.0 Mar 23, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.handlers;

import java.io.IOException;
import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import org.nlh4j.exceptions.ApplicationRuntimeException;

/**
 * Extended {@link org.springframework.security.web.access.AccessDeniedHandler} for handling CSRF token missing
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Component
@Singleton
public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler, Serializable {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * slf4j
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** {@link GlobalExceptionResolver} */
    @Inject
    protected GlobalExceptionResolver exceptionResolver;

    /* (Non-Javadoc)
     * @see org.springframework.security.web.access.AccessDeniedHandler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.access.AccessDeniedException)
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // if autowired exception resolver; just using for resolving
        if (this.exceptionResolver != null) {
            this.exceptionResolver.handleException(
                    new ServletWebRequest(request, response), request, response,
                    accessDeniedException, null);

        }
        // just throwing exception
        else {
            throw new ApplicationRuntimeException(accessDeniedException);
        }
    }

}
