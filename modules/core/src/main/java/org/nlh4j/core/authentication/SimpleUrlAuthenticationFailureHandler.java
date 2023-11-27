/*
 * @(#)SimpleUrlAuthenticationFailureHandler.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.ExceptionMappingAuthenticationFailureHandler;

/**
 * An alias of {@link ExceptionMappingAuthenticationFailureHandler}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class SimpleUrlAuthenticationFailureHandler
        extends ExceptionMappingAuthenticationFailureHandler
        implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * SLF4J
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Initialize a new instance of {@link SimpleUrlAuthenticationFailureHandler}
     *
     */
    public SimpleUrlAuthenticationFailureHandler() {}
    /**
     * Initialize a new instance of {@link SimpleUrlAuthenticationFailureHandler}
     *
     * @param defaultFailureUrl default failure URL
     */
    public SimpleUrlAuthenticationFailureHandler(String defaultFailureUrl) {
        super.setDefaultFailureUrl(defaultFailureUrl);
    }
}
