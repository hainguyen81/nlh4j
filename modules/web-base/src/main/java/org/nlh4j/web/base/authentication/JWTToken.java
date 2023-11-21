/*
 * @(#)JWTToken.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.authentication;

import java.util.List;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;

import org.nlh4j.core.dto.AbstractJWTToken;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.web.core.dto.RoleDto;
import org.nlh4j.web.core.dto.UserDto;

/**
 * JWT Token
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public class JWTToken extends AbstractJWTToken<UserDto, RoleDto> {

    /** */
    private static final long serialVersionUID = 1L;
    public static final String DEFAULT_AUTHORITIES_CLAIM = "roles";

    /**
     * Initialize a new instance of {@link JWTToken}
     *
     * @param jwt token
     */
    public JWTToken(JWT jwt) {
        this(jwt, DEFAULT_AUTHORITIES_CLAIM);
    }
    /**
     * Initialize a new instance of {@link JWTToken}
     *
     * @param jwt token
     * @param claim claim to parse authorities
     */
    protected JWTToken(JWT jwt, String claim) {
        super(jwt, claim, UserDto.class, RoleDto.class);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.dto.AbstractJWTToken#parsePrincipal(com.nimbusds.jwt.JWTClaimsSet)
     */
    @Override
    protected UserDto parsePrincipal(JWTClaimsSet claims) {
        UserDto user = new UserDto();
        if (claims != null) {
            user.setUserName(claims.getSubject());
            List<String> audiences = claims.getAudience();
            user.setPassword(!CollectionUtils.isEmpty(audiences) ? audiences.get(0) : null);
        }
        return user;
    }
}
