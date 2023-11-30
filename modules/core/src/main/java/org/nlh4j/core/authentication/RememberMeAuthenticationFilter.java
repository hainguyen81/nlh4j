/*
 * @(#)RememberMeAuthenticationFilter.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.RememberMeServices;

/**
 * An alias of {@link org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class RememberMeAuthenticationFilter
        extends org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter
        implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * SLF4J
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Initialize a new instance of {@link RememberMeAuthenticationFilter}
     *
     * @param authenticationManager {@link AuthenticationManager} for filtering
     * @param rememberMeServices {@link RememberMeServices} for checking remembered users information
     */
    public RememberMeAuthenticationFilter(
            AuthenticationManager authenticationManager,
            RememberMeServices rememberMeServices) {
        super(authenticationManager, rememberMeServices);
    }
}
