/*
 * @(#)Nlh4jAuthenticationProvider.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;

import org.apache.commons.lang3.StringUtils;
import org.nlh4j.core.dto.AbstractJWTToken;
import org.nlh4j.core.dto.UserDetails;
import org.nlh4j.core.service.MessageService;
import org.nlh4j.core.service.UserService;
import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.DateUtils;
import org.nlh4j.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Authentication Provider
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Component(value = "nh4jAuthenticationProvider")
public class Nlh4jAuthenticationProvider implements AuthenticationProvider, Serializable {

	/** */
    private static final long serialVersionUID = 1L;
    /**
     * SLF4J
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * {@link HttpServletRequest}
     */
	@Inject
    private HttpServletRequest request;

	/**
	 * Get the request
	 *
	 * @return the request
	 */
	protected final HttpServletRequest getRequest() {
		if (this.request == null) {
			this.request = RequestUtils.getHttpRequest();
		}
		return this.request;
	}

    /**
	 * The remember key for checking authentication in remember
	 */
	@Getter
	@Setter
	private String rememberKey = null;

    /**
     * {@link MessageService}
     */
	@Inject
    protected MessageService messageService;
    /**
     * {@link UserService}
     */
	@Inject
	protected UserService userService;

	/**
     * Perform action before authenticating (such as checking product license).<br>
     * TODO Children classes mayber override this method for doing anything while logging if necessary
     */
    protected void beforeAuthenticate() {}
	/*
	 * (Non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Authentication token = null;
		// perform before authenticating
		logger.debug("1. Before authentication [{}]", authentication);
		this.beforeAuthenticate();
		// normal token
		if (BeanUtils.isInstanceOf(authentication, UsernamePasswordAuthenticationToken.class)) {
		    // parse authentication token
			logger.debug("2. Authenticate by {} [{}]", UsernamePasswordAuthenticationToken.class.getName(), authentication);
			token = this.internalAuthenticate(
					BeanUtils.safeType(authentication, UsernamePasswordAuthenticationToken.class));

		}
		// remember token
		else if (BeanUtils.isInstanceOf(authentication, RememberMeAuthenticationToken.class)) {
			logger.debug("3. Authenticate by {} [{}]", RememberMeAuthenticationToken.class.getName(), authentication);
			token = this.internalRememberAuthenticate(
					BeanUtils.safeType(authentication, RememberMeAuthenticationToken.class));
		}
		// JWT token
		else if (BeanUtils.isInstanceOf(authentication, AbstractJWTToken.class)) {
			logger.debug("4. Authenticate by {} [{}]", AbstractJWTToken.class.getName(), authentication);
			token = this.internalJWTAuthenticate(
					BeanUtils.safeType(authentication, AbstractJWTToken.class));
		}
		// perform action after authenticating.
		if (token != null) {
			logger.debug("5. After authentication [{}], Token [{}]", authentication, token);
			this.afterAuthenticate(token);
		} else {
			logger.warn("5. After authentication [{}], Token NULL", authentication);
		}
		// return authentication token
		return token;
	}
	/**
     * Perform action after authenticating (only authenticated; such as loading session information).<br>
     * TODO Children classes mayber override this method for doing anything while loging if necessary
     *
     * @param authentication {@link Authentication} (authenticated)
     *
     * @return {@link Authentication}
     */
    protected void afterAuthenticate(Authentication authentication) {}

	/**
	 * Authenticate with user name and password
	 *
	 * @param username user name
	 * @param cridenticals cridenticals
	 * @param encrypted specify the password from credentials whether has been encrypted
	 *
	 * @return authentication
	 *
	 * @exception AuthenticationException thrown if authenticating has been failed
	 */
	private Authentication internalLogin(String username, Object cridenticals, boolean encrypted)
			throws AuthenticationException {
		if (StringUtils.isBlank(username)) {
			throw new IllegalArgumentException("username must be not blank/null");
		}
		cridenticals = Objects.requireNonNull(cridenticals, "cridenticals");

		// login
		UserDetails user = userService.login(username, cridenticals, encrypted);
		if (user == null) {
			logger.error("Not found UserDetails by credenticals [{}]", cridenticals);
			throw new BadCredentialsException(this.getUnAuthorizedReason());
		}

		// Return an authenticated token, containing user data and authorities
		return new UsernamePasswordAuthenticationToken(
				user, user.getCredentials(), user.getAuthorities());
	}
	/**
	 * Authenticate {@link UsernamePasswordAuthenticationToken}
	 *
	 * @param auth to authenticate
	 *
	 * @return authentication
	 *
	 * @exception AuthenticationException thrown if authenticating has been failed
	 */
	private Authentication internalAuthenticate(UsernamePasswordAuthenticationToken auth)
			throws AuthenticationException {
		return this.internalLogin(String.valueOf(Objects.requireNonNull(auth, "auth").getPrincipal()), auth.getCredentials(), false);
	}
	/**
	 * Authenticate {@link RememberMeAuthenticationToken}
	 *
	 * @param auth to authenticate
	 *
	 * @return authentication
	 *
	 * @exception AuthenticationException thrown if authenticating has been failed
	 */
	private Authentication internalRememberAuthenticate(RememberMeAuthenticationToken auth) throws AuthenticationException {
		auth = Objects.requireNonNull(auth, "authentication");

		// check remember key
		if (StringUtils.isBlank(this.getRememberKey())
				|| (this.getRememberKey().hashCode() != auth.getKeyHash())) {
            throw new BadCredentialsException(this.getUnAuthorizedReason());
        }

		// Return an authenticated token, containing user data and authorities
		UserDetails user = AuthenticationUtils.getProfile(auth.getPrincipal());
		if (user == null) {
			logger.error("Not found UserDetails by credenticals [{}]", auth.getPrincipal());
			throw new BadCredentialsException(this.getUnAuthorizedReason());
		}

		// Return an authenticated token, containing user data and authorities
		return this.internalLogin(user.getUsername(), user.getCredentials(), false);
	}
	/**
	 * Authenticate {@link RememberMeAuthenticationToken}
	 *
	 * @param auth to authenticate
	 *
	 * @return authentication
	 *
	 * @exception AuthenticationException thrown if authenticating has been failed
	 */
	private <T extends UserDetails, A extends GrantedAuthority>
			Authentication internalJWTAuthenticate(AbstractJWTToken<T, A> auth)
					throws AuthenticationException {
		auth = Objects.requireNonNull(auth, "authentication");

		// require token
		JWT jwt = auth.getJwt();

		// check valid token
		this.handleJWTToken(jwt);

		// check token expiration
        Date referenceTime = DateUtils.currentTimestamp();
        JWTClaimsSet claims = auth.getClaims();
        Date expirationTime = (claims == null ? null : claims.getExpirationTime());
        if (expirationTime == null || expirationTime.before(referenceTime)) {
			logger.error("Token has been EXPIRED! Expiration Time [{}]", expirationTime);
            throw new BadCredentialsException("The token has expired!!!");
        }

        // check token whether is before server date/time
        Date notBeforeTime = (claims == null ? null : claims.getNotBeforeTime());
        if (notBeforeTime == null || notBeforeTime.after(referenceTime)) {
			logger.error("Token has been EXPIRED! NOT_BEFORE [{}]", notBeforeTime);
            throw new BadCredentialsException("Not before is after system date!!!");
        }

        // parse principal user name
 		UserDetails user = auth.getPrincipal();

 		// login
 		return this.internalLogin(user.getUsername(), user.getCredentials(), true);
	}
	/**
	 * Handle JWT token.<br>
	 * Default is throwing {@link BadCredentialsException} as invalid token.<br>
	 * TODO Children class maybe override this method for handling plain text JWT token
	 *
	 * @param jwt to handle
	 */
	protected void handleJWTToken(JWT jwt) {
        throw new BadCredentialsException("JWT Tokens are not supported!!!");
    }

    /**
     * The {@link HttpStatus}.UNAUTHORIZED reason phase key from resource bundle
     */
    @Getter
    @Setter
    private String unAuthorizedReasonKey;

    /**
     * The {@link HttpStatus}.UNAUTHORIZED reason phase.
     * TODO children classes maybe override this SET method for customizing reason phase
     */
    @Setter
    private String unAuthorizedReason;
    /**
     * Get the {@link HttpStatus}.UNAUTHORIZED reason phase
     * @return the {@link HttpStatus}.UNAUTHORIZED reason phase
     */
    public String getUnAuthorizedReason() {
        if (StringUtils.isBlank(this.unAuthorizedReason)
                && StringUtils.isNotBlank(this.getUnAuthorizedReasonKey())) {
            this.unAuthorizedReason = this.messageService.getMessage(
                    this.getUnAuthorizedReasonKey());
        }
        if (StringUtils.isBlank(this.unAuthorizedReason)) {
            this.unAuthorizedReason = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        }
        return this.unAuthorizedReason;
    }

	/*
	 * (Non-Javadoc)
	 * @see org.springframework.security.authentication.AuthenticationProvider#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> aClass) {
		return (BeanUtils.isInstanceOf(aClass, UsernamePasswordAuthenticationToken.class)
				|| BeanUtils.isInstanceOf(aClass, RememberMeAuthenticationToken.class)
				|| BeanUtils.isInstanceOf(aClass, AbstractJWTToken.class));
	}
}
