/*
 * @(#)AbstractJWTFilter.java 1.0 Apr 7, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;

import lombok.Getter;
import lombok.Setter;
import org.nlh4j.core.dto.AbstractJWTToken;
import org.nlh4j.core.dto.UserDetails;
import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.util.RequestUtils;
import org.nlh4j.util.StringUtils;

/**
 * Abstract JWT (Json Web Token) filter
 *
 * @param <T> the authentication principal type
 * @param <A> the authentication granted authority type
 * @param <J> the JWT authentication token
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public abstract class AbstractJWTFilter<T extends UserDetails, A extends GrantedAuthority, J extends AbstractJWTToken<T, A>>
		extends AbstractOncePerRequestFilter {

	/** */
    private static final long serialVersionUID = 1L;

	/** {@link AuthenticationEntryPoint} */
    @Getter
	private AuthenticationEntryPoint entryPoint;
	/** {@link AuthenticationManager} */
    @Getter
    private AuthenticationManager authenticationManager;
    /** Default token header parameter name */
    public static final String DEFAULT_TOKEN_HEADER = "X-Token";
    /** Default token header parameter name */
    public static final String DEFAULT_TOKEN_PARAMETER = "t";
    /** Token header parameter name */
    @Getter
    @Setter
    private String authorizationSchema;
    /** Token header parameter name */
    @Getter
    @Setter
    private String tokenHeader;
    /** Token POST/GET parameter name */
    @Getter
    @Setter
    private String tokenParameter;

    /**
     * Initialize a new instance of {@link AbstractJWTFilter}
     *
     * @param authenticationManager {@link AuthenticationManager}
     * @param entryPoint {@link AuthenticationEntryPoint}
     */
    protected AbstractJWTFilter(
    		AuthenticationManager authenticationManager,
    		AuthenticationEntryPoint entryPoint) {
        this.authenticationManager = authenticationManager;
        this.entryPoint = entryPoint;
    }

    /* (Non-Javadoc)
     * @see org.springframework.web.filter.GenericFilterBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        Assert.notNull(this.getAuthenticationManager(), "authenticationManager");
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.authentication.AbstractOncePerRequestFilter#doBeforeFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected boolean doBeforeFilter(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException {
    	// parse token
    	String tokenHeader = (StringUtils.hasText(this.getTokenHeader())
    			? this.getTokenHeader() : DEFAULT_TOKEN_HEADER);
		String token = RequestUtils.getHeader(request, tokenHeader);
		if (!StringUtils.hasText(token)) {
		    String tokenParam = (StringUtils.hasText(this.getTokenParameter())
	                ? this.getTokenParameter() : DEFAULT_TOKEN_PARAMETER);
		    token = RequestUtils.getParameter(request, tokenParam);
		}

		// if invalid token
		if (StringUtils.hasText(token)) {
    		// parse schema
    		String authorizationSchema = this.getAuthorizationSchema();

    		// parse authentication
        	try {
                // remove schema from token
                if (StringUtils.hasText(authorizationSchema)
                        && token.indexOf(authorizationSchema) < 0) {
                    throw new InsufficientAuthenticationException(
                    		"Authorization schema [" + authorizationSchema + "] not found");
                }

                // apply token
                token = ((StringUtils.hasText(authorizationSchema) && token.indexOf(authorizationSchema) >= 0)
                        ? token.substring(authorizationSchema.length()).trim() : token.trim());
                JWT jwt = null;
    			try {
    				jwt = JWTParser.parse(token);
    			} catch (ParseException e) {
    				throw new BadCredentialsException("Invalid JWT token [" + token + "]", e);
    			}
                // transform token
                AbstractJWTToken<T, A> jwtToken = this.transformToken(jwt);
                // authenticate token
                Authentication auth = this.getAuthenticationManager().authenticate(jwtToken);
                AuthenticationUtils.invalidateAuthentication(auth);
                // handle authentication successful
                this.authenticateSuccessHandler(auth);
        	}
        	catch (AuthenticationException e) {
                SecurityContextHolder.clearContext();
                if (this.getEntryPoint() != null) {
                	this.getEntryPoint().commence(request, response, e);
                } else {
                    throw e;
                }
            }
		}
    	return false;
    }

    /**
     * Transform the authentication JWT token to authentication principal
     *
     * @param token to parse
     *
     * @return the authentication principal
     */
    protected abstract J transformToken(JWT token);
    /**
     * Handle authentication successful
     *
     * @param auth authentication token
     */
    protected void authenticateSuccessHandler(Authentication auth) {}
}
