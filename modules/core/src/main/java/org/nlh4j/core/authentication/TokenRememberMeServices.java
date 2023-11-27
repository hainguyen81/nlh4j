/*
 * @(#)TokenRememberMeServices.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.StringUtils;

import org.nlh4j.core.service.UserService;
import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.util.EncryptUtils;

/**
 * Token remember services
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class TokenRememberMeServices extends TokenBasedRememberMeServices implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * SLF4J
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * {@link UserService}
     */
    @Inject
	protected UserService userService;
    /** This empty class just for bypass super class constructor */
    protected static class EmptyUserDetailsService implements UserDetailsService {
        /* (Non-Javadoc)
         * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
         */
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return null;
        }
    }

    /**
     * Initialize a new instance of {@link TokenRememberMeServices}
     *
     * @param key token key
     */
	public TokenRememberMeServices(String key) {
        super(key, new EmptyUserDetailsService());
    }
	/**
	 * Initialize a new instance of {@link TokenRememberMeServices}
	 */
	public TokenRememberMeServices() {
        this(TokenRememberMeServices.class.getName());
    }

	/*
	 * (Non-Javadoc)
	 * @see org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices#getUserDetailsService()
	 */
	@Override
	protected final UserDetailsService getUserDetailsService() {
		return userService;
	}

	/* (Non-Javadoc)
	 * @see org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {}

	/* (Non-Javadoc)
	 * @see org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices#processAutoLoginCookie(java.lang.String[], javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected UserDetails processAutoLoginCookie(
	        String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {

	    if (cookieTokens.length < 3) {
            throw new InvalidCookieException(
                    "Cookie token did not contain at least 3" +
                    " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }

	    // parse token expired time
        long tokenExpiryTime;
        try {
            tokenExpiryTime = NumberUtils.toLong(cookieTokens[1], 0L);
        }
        catch (NumberFormatException nfe) {
            throw new InvalidCookieException(
                    "Cookie token[1] did not contain a valid number (contained '" + cookieTokens[1] + "')");
        }

        // check expired token
        if (isTokenExpired(tokenExpiryTime)) {
            throw new InvalidCookieException(
                    "Cookie token[1] has expired (expired on '"
                            + new Date(tokenExpiryTime) + "'; current time is '" + new Date() + "')");
        }

        // Check the user exists.
        // Defer lookup until after expiration time checked, to possibly avoid expensive database call.
        List<String> tokens = new LinkedList<String>();
        String[] arrTokens = null;
        if (cookieTokens.length >= 3) {
            for(int i = 3; i < cookieTokens.length; i++) {
                String token = (!StringUtils.hasText(cookieTokens[i]) ? "" : cookieTokens[i]);
                if (StringUtils.hasText(token)) {
                    token = EncryptUtils.base64decode(token);
                }
                tokens.add(token);
            }
            arrTokens = tokens.toArray(new String[tokens.size()]);
        }

        // parse user information by tokens
        UserDetails userDetails = this.userService.loadUserByTokens(cookieTokens[0], arrTokens);
        // check whether user is valid
        if (!userDetails.isEnabled() || !userDetails.isAccountNonExpired()
                || !userDetails.isAccountNonLocked() || !userDetails.isCredentialsNonExpired()) {
            throw new InvalidCookieException(
                    "Cookie token[2] contained signature '" + cookieTokens[2]
                            + "' but user has been disabled, or account has been locked/expired");
        }

        // Check signature of token matches remaining details.
        // Must do this after user lookup, as we need the DAO-derived password.
        // If efficiency was a major issue, just add in a UserCache implementation,
        // but recall that this method is usually only called once per HttpSession - if the token is valid,
        // it will cause SecurityContextHolder population, whilst if invalid, will cause the cookie to be cancelled.
        String expectedTokenSignature = this.makeMoreTokenSignature(
                tokenExpiryTime, userDetails.getUsername(), userDetails.getPassword(), arrTokens);

        if (!equals(expectedTokenSignature,cookieTokens[2])) {
            throw new InvalidCookieException(
                    "Cookie token[2] contained signature '" + cookieTokens[2]
                            + "' but expected '" + expectedTokenSignature + "'");
        }
        return userDetails;
	}

    /**
     * This method had been deprecated. Instead of using {@link makeMoreTokenSignature}
     */
	@Deprecated
    protected final String makeTokenSignature(long tokenExpiryTime, String username, String password) {
        throw new UnsupportedOperationException();
    }
	/**
     * Calculates the digital signature to be put in the cookie. Default value is
     * MD5 ("username:tokenExpiryTime:password:key")
     */
	protected final String makeMoreTokenSignature(long tokenExpiryTime, String username, String password, String...tokens) {
	    // parse cookie tokens
        String data = username + ":" + tokenExpiryTime + ":" + password;
        if (!ArrayUtils.isEmpty(tokens)) {
            for(String token : tokens) {
                data += ":" + (!StringUtils.hasLength(token) ? "" : token);
            }
        }
        data += ":" + getKey();

        // encode token data
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        return new String(Hex.encode(digest.digest(data.getBytes())));
    }

	/* (Non-Javadoc)
	 * @see org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices#onLoginSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onLoginSuccess(
	        HttpServletRequest request, HttpServletResponse response,
	        Authentication successfulAuthentication) {
	    org.nlh4j.core.dto.UserDetails user = AuthenticationUtils.getProfile(successfulAuthentication);
	    String username = user.getUsername();
        String password = user.getPassword();

        // If unable to find a username and password, just abort as TokenBasedRememberMeServices is
        // unable to construct a valid token in this case.
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            logger.debug("Unable to retrieve username or password");
            return;
        }

        int tokenLifetime = calculateLoginLifetime(request, successfulAuthentication);
        long expiryTime = System.currentTimeMillis();
        // SEC-949
        expiryTime += 1000L* (tokenLifetime < 0 ? TWO_WEEKS_S : tokenLifetime);

        // create cookie signature
        String signatureValue = this.makeMoreTokenSignature(
                expiryTime, username, password, user.getRememberTokenSignatures());

        // apply cookie
        List<String> cookieVals = new LinkedList<String>();
        cookieVals.add(username);
        cookieVals.add(Long.toString(expiryTime));
        cookieVals.add(signatureValue);
        if (!ArrayUtils.isEmpty(user.getRememberTokenSignatures())) {
            for(String token : user.getRememberTokenSignatures()) {
                cookieVals.add(!StringUtils.hasText(token) ? "" : EncryptUtils.base64encode(token));
            }
        }
        setCookie(cookieVals.toArray(new String[cookieVals.size()]), tokenLifetime, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("Added remember-me cookie for user '" + username + "', expiry: '"
                    + new Date(expiryTime) + "'");
        }
	}

    /**
     * Constant time comparison to prevent against timing attacks.
     */
    protected static boolean equals(String expected, String actual) {
        byte[] expectedBytes = bytesUtf8(expected);
        byte[] actualBytes = bytesUtf8(actual);
        if (expectedBytes.length != actualBytes.length) return false;

        int result = 0;
        for (int i = 0; i < expectedBytes.length; i++) {
            result |= expectedBytes[i] ^ actualBytes[i];
        }
        return result == 0;
    }
    protected static byte[] bytesUtf8(String s) {
        if (s == null) return null;
        return Utf8.encode(s);
    }
}
