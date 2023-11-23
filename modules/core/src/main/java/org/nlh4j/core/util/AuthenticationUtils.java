/*
 * @(#)AuthenticationUtils.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.util;

import java.io.Serializable;
import java.security.Principal;
import java.security.PrivateKey;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nlh4j.core.dto.UserDetails;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.DateUtils;
import org.nlh4j.util.JWTUtils;
import org.nlh4j.util.RequestUtils;
import org.nlh4j.util.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.util.WebUtils;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.MACSigner;

import lombok.extern.slf4j.Slf4j;

/**
 * Authentication utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@SuppressWarnings("unchecked")
@Slf4j
public final class AuthenticationUtils implements Serializable {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * CSRF-XSRF-TOKEN
	 */
	public static final String HEADER_CSRF_TOKEN = "CSRF-TOKEN";
	public static final String COOKIE_CSRF_TOKEN = "CSRF-TOKEN-COOKIE";

    /**
     * Get the safe-type profile from the specified {@link Principal}
     *
     * @param <T> profile type
     * @param profileClass the profile type to cast
     * @param principal {@link Principal}
     *
     * @return the safe-type profile
     */
    public static <T> T getProfile(Principal principal, Class<T> profileClass) {
        return getProfile(BeanUtils.safeType(principal, Authentication.class), profileClass);
    }
    /**
     * Get the safe-type profile from the specified {@link Authentication}
     *
     * @param <T> profile type
     * @param profileClass the profile type to cast
     * @param authentication {@link Authentication}
     *
     * @return the safe-type profile
     */
    public static <T> T getProfile(Authentication authentication, Class<T> profileClass) {
        return (authentication != null
                ? BeanUtils.safeType(authentication.getPrincipal(), profileClass) : null);
    }
    /**
     * Get the safe-type profile from the specified authentication principal
     *
     * @param <T> profile type
     * @param profileClass the profile type to cast
     * @param principal the authentication principal
     *
     * @return the safe-type profile
     */
    public static <T> T getProfile(Object principal, Class<T> profileClass) {
        return (BeanUtils.isInstanceOf(principal, Principal.class)
                ? getProfile((Principal) principal, profileClass)
                        : BeanUtils.isInstanceOf(principal, Authentication.class)
                        ? getProfile((Authentication) principal, profileClass)
                                : BeanUtils.isInstanceOf(principal, profileClass)
                                ? (T) principal : null);
    }
    /**
     * Get the safe-type {@link UserDetails} from the specified {@link Principal}
     *
     * @param principal {@link Principal}
     *
     * @return the safe-type {@link UserDetails}
     */
    public static UserDetails getProfile(Principal principal) {
    	return getProfile(principal, UserDetails.class);
    }
    /**
     * Get the safe-type {@link UserDetails} from the specified {@link Authentication}
     *
     * @param authentication {@link Authentication}
     *
     * @return the safe-type {@link UserDetails}
     */
    public static UserDetails getProfile(Authentication authentication) {
    	return getProfile(authentication, UserDetails.class);
    }
    /**
     * Get the safe-type {@link UserDetails} from the specified authentication principal
     *
     * @param principal the authentication principal
     *
     * @return the safe-type {@link UserDetails}
     */
    public static UserDetails getProfile(Object principal) {
    	return getProfile(principal, UserDetails.class);
    }

    /**
     * Get the current {@link Authentication} principal from context
     * @return the current {@link Authentication} principal from context
     */
    public static Authentication getCurrentPrincipal() {
    	SecurityContext sc = SecurityContextHolder.getContext();
    	return (sc == null ? null : sc.getAuthentication());
    }

    /**
     * Get the current safe-type {@link UserDetails} from context
     * @return the current safe-type {@link UserDetails} from context
     */
    public static UserDetails getCurrentProfile() {
    	return getProfile(getCurrentPrincipal());
    }
    /**
     * Get the current safe-type profile from context
     *
     * @param <T> profile type
     * @param profileClass profile class to cast
     *
     * @return the current safe-type profile from context
     */
    public static <T> T getCurrentProfile(Class<T> profileClass) {
        return getProfile(getCurrentPrincipal(), profileClass);
    }

    /**
     * Generate CSRF token
     *
     * @param request the current request
     * @param response the current response
     * @param principal the current authentication
     * @param checkauth specify whether need to check authentication before generating
     * @param applycookie specify whether applying token to cookie
     *
     * @return the CSRF token string
     */
    public static String generateCsrfToken(
    		HttpServletRequest request, HttpServletResponse response,
    		Principal principal, boolean checkauth, boolean applycookie) {
    	// check parameters
    	String token = null;
    	if (request == null || response == null || (checkauth && principal == null)) {
    		log.warn("Invalid request|response|principal to generate CSRF token!!!");
    		return token;
    	}
    	// need to check authentication
    	if (checkauth) {
    		UserDetails user = getProfile(principal);
	    	if (user == null) {
	    		log.warn("Invalid authentication to generate CSRF token!!!");
	    		return token;
	    	}
    	}

    	// generate token
    	CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    	token = csrf.getToken();
		if (csrf != null && applycookie) {
			Cookie cookie = WebUtils.getCookie(request, COOKIE_CSRF_TOKEN);
			if (cookie == null || (token != null && !token.equals(cookie.getValue()))) {
				cookie = new Cookie(COOKIE_CSRF_TOKEN, token);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		log.warn(">>> GENERATED TOKEN: [" + token + "]");
		return token;
    }
    /**
     * Generate CSRF token
     *
     * @param request the current request
     * @param response the current response
     * @param checkauth specify whether need to check authentication before generating
     * @param applycookie specify whether applying token to cookie
     *
     * @return the CSRF token string
     */
    public static String generateCsrfToken(
    		HttpServletRequest request, HttpServletResponse response,
    		boolean checkauth, boolean applycookie) {
    	return generateCsrfToken(request, response, getCurrentPrincipal(), checkauth, applycookie);
    }

    /**
     * Invalidate the specified {@link UserDetails} from session
     *
     * @param <T> parameter class type
     * @param ud the user to invalidate
     */
    public static <T extends UserDetails> void invalidateAuthentication(T ud) {
        if (ud == null) return;
        invalidateAuthentication(
        		new UsernamePasswordAuthenticationToken(
        				ud, ud.getPassword(), ud.getAuthorities()));
    }
    /**
     * Invalidate the specified {@link UserDetails} from session
     *
     * @param <T> parameter class type
     * @param authentication the user to invalidate
     */
    public static <T extends Authentication> void invalidateAuthentication(T authentication) {
        if (authentication == null || authentication.getPrincipal() == null) return;
        SecurityContext sc = SecurityContextHolder.getContext();
        sc = (sc == null ? SecurityContextHolder.createEmptyContext() : sc);
        sc.setAuthentication(authentication);
        SecurityContextHolder.setContext(sc);
    }

    /**
     * Generate the specified authentication to token string.<br>
     * Base on user name, password and login date/time
     *
     * @param <T> the authentication principal type
     * @param algorithm {@link JWSAlgorithm}
     * @param privateKey {@link PrivateKey} to sign as secret key
     * @param authorizationSchema authorization schema
     * @param username user name
     * @param password password
     * @param loginAt logged-in datetime
     * @param timeout the token timeout, in seconds
     *
     * @return token string
     */
    public static <T extends UserDetails> String generateToken(
            JWSAlgorithm algorithm, PrivateKey privateKey, String authorizationSchema,
            String username, String password, Date loginAt, int timeout) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) return null;
        Date dt = (loginAt == null ? DateUtils.currentDate() : loginAt);
        Date expiration = DateUtils.addDate(dt.getTime(), Calendar.SECOND, timeout);
        return JWTUtils.createSignedJWT(
                algorithm, privateKey, authorizationSchema,
                RequestUtils.getFullContextPath(),
                username, password,
                expiration, dt, dt, username);
    }
    /**
     * Generate the specified authentication to token string.<br>
     * Base on user name, password and login date/time
     *
     * @param <T> the authentication principal type
     * @param algorithm {@link JWSAlgorithm}
     * @param secretKey {@link MACSigner} secret key to sign (un-encoded base64 key)
     * @param authorizationSchema authorization schema
     * @param username user name
     * @param password user password
     * @param loginAt logged-in date time
     * @param timeout the token timeout, in seconds
     *
     * @return token string
     */
    public static <T extends UserDetails> String generateToken(
            JWSAlgorithm algorithm, String secretKey, String authorizationSchema,
            String username, String password, Date loginAt, int timeout) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) return null;
        Date dt = (loginAt == null ? DateUtils.currentDate() : loginAt);
        Date expiration = DateUtils.addDate(dt.getTime(), Calendar.SECOND, timeout);
        return JWTUtils.createSignedJWT(
                algorithm, secretKey, authorizationSchema,
                RequestUtils.getFullContextPath(),
                username, password,
                expiration, dt, dt, username);
    }
    /**
     * Generate the specified authentication to token string.<br>
     * Base on user name, password and login date/time
     *
     * @param <T> the authentication principal type
     * @param algorithm {@link JWSAlgorithm}
     * @param privateKey {@link PrivateKey} to sign as secret key
     * @param authorizationSchema authorization schema
     * @param ud authentication to generate
     * @param timeout the token timeout, in seconds
     *
     * @return token string
     */
    public static <T extends UserDetails> String generateToken(
            JWSAlgorithm algorithm, PrivateKey privateKey, String authorizationSchema, T ud, int timeout) {
        if (ud == null) return null;
        List<UserDetails> users = new LinkedList<UserDetails>();
        users.add(ud);
        List<Map<String, Object>> dataLst = BeanUtils.mapPropertyValuesMap(
                UserDetails.class, users, new String[] { "username", "password", "loginAt" });
        if (CollectionUtils.isEmpty(dataLst)) return null;
        Map<String, Object> data = dataLst.get(0);
        String username = (data.get("username") == null ? null : String.valueOf(data.get("username")));
        String password = (data.get("password") == null ? null : String.valueOf(data.get("password")));
        Date dt = BeanUtils.safeType(data.get("loginAt"), Date.class);
        return generateToken(
                algorithm, privateKey, authorizationSchema,
                username, password, dt, timeout);
    }
    /**
     * Generate the specified authentication to token string.<br>
     * Base on user name, password and login date/time
     *
     * @param <T> the authentication principal type
     * @param algorithm {@link JWSAlgorithm}
     * @param secretKey {@link MACSigner} secret key to sign (un-encoded base64 key)
     * @param authorizationSchema authorization schema
     * @param ud authentication to generate
     * @param timeout the token timeout, in seconds
     *
     * @return token string
     */
    public static <T extends UserDetails> String generateToken(
            JWSAlgorithm algorithm, String secretKey, String authorizationSchema, T ud, int timeout) {
        if (ud == null) return null;
        List<UserDetails> users = new LinkedList<UserDetails>();
        users.add(ud);
        List<Map<String, Object>> dataLst = BeanUtils.mapPropertyValuesMap(
                UserDetails.class, users, new String[] { "username", "password", "loginAt" });
        if (CollectionUtils.isEmpty(dataLst)) return null;
        Map<String, Object> data = dataLst.get(0);
        String username = (data.get("username") == null ? null : String.valueOf(data.get("username")));
        String password = (data.get("password") == null ? null : String.valueOf(data.get("password")));
        Date dt = BeanUtils.safeType(data.get("loginAt"), Date.class);
        return generateToken(
                algorithm, secretKey, authorizationSchema,
                username, password, dt, timeout);
    }
    /**
     * Generate the specified authentication to token string.<br>
     * Base on user name, password and login date/time
     *
     * @param <T> the authentication principal type
     * @param privateKey {@link PrivateKey} to sign as secret key
     * @param authorizationSchema authorization schema
     * @param ud authentication to generate
     * @param timeout the token timeout, in seconds
     *
     * @return token string
     */
    public static <T extends UserDetails> String generateHs512Token(
    		PrivateKey privateKey, String authorizationSchema, T ud, int timeout) {
    	return generateToken(JWSAlgorithm.HS512, privateKey, authorizationSchema, ud, timeout);
    }
    /**
     * Generate the specified authentication to token string.<br>
     * Base on user name, password and login date/time
     *
     * @param <T> the authentication principal type
     * @param secretKey {@link MACSigner} secret key to sign (un-encoded base64 key)
     * @param authorizationSchema authorization schema
     * @param ud authentication to generate
     * @param timeout the token timeout, in seconds
     *
     * @return token string
     */
    public static <T extends UserDetails> String generateHs512Token(
            String secretKey, String authorizationSchema, T ud, int timeout) {
        return generateToken(JWSAlgorithm.HS512, secretKey, authorizationSchema, ud, timeout);
    }
}
