/*
 * @(#)SimpleUrlAuthenticationSuccessHandler.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An alias of {@link org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class SimpleUrlAuthenticationSuccessHandler
        extends org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
        implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * SLF4J
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Initialize a new instance of {@link SimpleUrlAuthenticationSuccessHandler}
     *
     */
    public SimpleUrlAuthenticationSuccessHandler() {}
    /**
     * Initialize a new instance of {@link SimpleUrlAuthenticationSuccessHandler}
     *
     * @param defaultTargetUrl default target URL
     */
    public SimpleUrlAuthenticationSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }
}
