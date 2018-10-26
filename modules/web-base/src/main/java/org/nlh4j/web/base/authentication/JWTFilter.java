/*
 * @(#)JWTFilter.java 1.0 Apr 10, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.authentication;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.nimbusds.jwt.JWT;

import org.nlh4j.core.authentication.AbstractJWTFilter;
import org.nlh4j.web.core.dto.RoleDto;
import org.nlh4j.web.core.dto.UserDto;

/**
 * JWT (Json Web Token) filter
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public class JWTFilter extends AbstractJWTFilter<UserDto, RoleDto, JWTToken> {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of {@link JWTFilter}
     *
     * @param authenticationManager {@link AuthenticationManager}
     * @param entryPoint {@link AuthenticationEntryPoint}
     */
    public JWTFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint entryPoint) {
        super(authenticationManager, entryPoint);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.authentication.AbstractJWTFilter#transformToken(com.nimbusds.jwt.JWT)
     */
    @Override
    protected JWTToken transformToken(JWT token) {
        return new JWTToken(token);
    }
}
