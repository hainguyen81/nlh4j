/*
 * @(#)AbstractLoginController.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.nlh4j.core.controller.AbstractController;
import org.nlh4j.core.dto.ApiLogin;
import org.nlh4j.core.dto.ApiToken;
import org.nlh4j.core.dto.UserDetails;
import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.util.LogUtils;
import org.nlh4j.util.SessionUtils;

/**
 * Login controller
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
@RequestMapping(value = "/login")
public abstract class AbstractApiLoginController extends AbstractController {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

    /**
     * Login API via REST
     *
     * @param <T> parameter class type
     * @param apiLogin login information
     * @param result parameters binding result
     *
     * @return API token
     */
    @RequestMapping(value = "/api", method = { RequestMethod.POST })
    public final <T extends ApiLogin> ResponseEntity<ApiToken> apiLogin(
            @RequestBody(required = true) T apiLogin, BindingResult result) {
    	// check by bean validation
		this.checkBinding(result);

        SecurityContext sc = null;
        Authentication token = null;
        AuthenticationProvider authenticationProvider = null;
        try {
            // Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
            token = new UsernamePasswordAuthenticationToken(apiLogin.getUsername(), apiLogin.getPassword());
            ((UsernamePasswordAuthenticationToken) token).setDetails(
                    new WebAuthenticationDetails(super.getRequest()));

            // authenticate
            authenticationProvider = this.getApiAuthenticationProvider();
            authenticationProvider = (authenticationProvider == null
                    ? super.findBean(AuthenticationProvider.class) : authenticationProvider);
            token = authenticationProvider.authenticate(token);

            // apply security context authentication
            AuthenticationUtils.invalidateAuthentication(token);

            // save context
            SessionUtils.setSessionValue(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);

            // parse authentication detail
            UserDetails user = AuthenticationUtils.getProfile(token);

            // transform login detail to token string
            String jwtToken = (user != null ? this.transformToken(user) : null);

            // response successful token
            ApiToken apiToken = new ApiToken();
            apiToken.setToken(jwtToken);
            return new ResponseEntity<ApiToken>(apiToken, HttpStatus.OK);
        } catch (Exception e) {
            LogUtils.logError(this.getClass(), e.getMessage());
            return new ResponseEntity<ApiToken>(HttpStatus.UNAUTHORIZED);
        }
    }
    /**
     * Transform authentication {@link UserDetails} information to token string.<br>
     * Default is not transform, and return NULL token.<br>
     * TODO Children classes maybe override this method to transforming to token (such as JWT token)
     *
     * @param <T> the authentication principal type
     * @param ud authentication detail
     *
     * @return token string
     */
    protected <T extends UserDetails> String transformToken(T ud) {
        return null;
    }

    /**
     * Get the authentication provider for authenticating from API.<br>
     * NULL/not found for default searching {@link AuthenticationProvider} from context.<br>
     * TODO Children classes maybe override this method for custom {@link AuthenticationProvider}
     *
     * @return the authentication provider class
     */
    protected AuthenticationProvider getApiAuthenticationProvider() {
        return null;
    }
}