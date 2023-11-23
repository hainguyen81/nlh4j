/**
 * @(#)FunctionAuthenticationFilter.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;
import org.nlh4j.core.dto.UserDetails;
import org.nlh4j.core.service.ExecutePermissionService;
import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.RequestUtils;

/**
 * Function authentication fillter.
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version 1.0 Jun 2, 2015
 */
public class FunctionAuthenticationFilter extends AbstractOncePerRequestFilter {

    /** */
    private static final long serialVersionUID = 1L;

	/**
	 * The redirection URL
	 */
	@Getter
	@Setter
	private String redirectURI;

	/**
	 * {@link ExecutePermissionService}
	 */
	@Inject
	private ExecutePermissionService executePermissionService;

    /**
     * The {@link HttpStatus#FORBIDDEN} reason phase key from resource bundle
     */
    @Getter
    @Setter
    private String forbiddenReasonKey;

    /**
     * The {@link HttpStatus#FORBIDDEN} reason phase.
     * TODO children classes maybe override this method for customizing reason phase
     */
    @Setter
    private String forbiddenReason;
    /**
     * Get the {@link HttpStatus#FORBIDDEN} reason phase
     * @return the {@link HttpStatus#FORBIDDEN} reason phase
     */
    public String getForbiddenReason() {
        if (!StringUtils.hasText(this.forbiddenReason)
                && StringUtils.hasText(this.getForbiddenReasonKey())) {
            this.forbiddenReason = super.messageService.getMessage(
                    this.getForbiddenReasonKey());
        }
        if (!StringUtils.hasText(this.forbiddenReason)) {
            this.forbiddenReason = HttpStatus.FORBIDDEN.getReasonPhrase();
        }
        return this.forbiddenReason;
    }

    /**
     * The {@link HttpStatus#UNAUTHORIZED} reason phase key from resource bundle
     */
    @Getter
    @Setter
    private String unAuthenticationReasonKey;

    /**
     * The {@link HttpStatus#UNAUTHORIZED} reason phase.
     * TODO children classes maybe override this method for customizing reason phase
     */
    @Setter
    private String unAuthenticationReason;
    /**
     * Get the {@link HttpStatus#UNAUTHORIZED} reason phase
     * @return the {@link HttpStatus#UNAUTHORIZED} reason phase
     */
    public String getUnAuthenticationReason() {
        if (!StringUtils.hasText(this.unAuthenticationReason)
                && StringUtils.hasText(this.getForbiddenReasonKey())) {
            this.unAuthenticationReason = super.messageService.getMessage(
                    this.getForbiddenReasonKey());
        }
        if (!StringUtils.hasText(this.unAuthenticationReason)) {
            this.unAuthenticationReason = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        }
        return this.unAuthenticationReason;
    }

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.authentication.AbstractOncePerRequestFilter#doBeforeFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected boolean doBeforeFilter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Retrieve user details
		Authentication authentication = AuthenticationUtils.getCurrentPrincipal();
		if (authentication != null) {
			if (BeanUtils.isInstanceOf(authentication, AnonymousAuthenticationToken.class)) {
				logger.debug("ANONYMOUS!!!");
				// throw new BadCredentialsException(this.getUnAuthenticationReason());
			}
			else {
				// Check access permission
				logger.debug(">>>>>>> Checking access permission: " + authentication.getName());
				if (!this.isAccessPermission(request, authentication)) {
					logger.debug("ACCESS DENIED on [" + request.getRequestURI() + "]");
					// Force the user to the main page
					int status = HttpStatus.FORBIDDEN.value();
					String msg = this.getForbiddenReason();
					if (RequestUtils.isAjaxRequest(request)) {
						logger.warn(status + " - " + msg);
						response.setStatus(status);
						response.setCharacterEncoding("utf-8");
						PrintWriter out = response.getWriter();
						out.println(msg);
						out.flush();
						return true;
					}
					throw new AccessDeniedException(msg);
				}
				else {
					logger.debug("ALLOW access [" + request.getRequestURI() + "]!!!");
				}
			}
		}
		else {
			logger.debug("UNAUTHENTICATION. Not allow access [" + request.getRequestURI() + "]!!!");
			// throw new BadCredentialsException(this.getUnAuthenticationReason());
		}
		return false;
	}

	/**
	 * Check if user is access permission
	 *
	 * @param request
	 * @param authentication
	 * @return boolean
	 */
	private boolean isAccessPermission(HttpServletRequest request, Authentication authentication) {
		UserDetails profile = AuthenticationUtils.getProfile(authentication);
		boolean accepted = (
				profile != null
				&& profile.isAccountNonExpired()
				&& profile.isAccountNonLocked()
				&& profile.isCredentialsNonExpired()
				&& profile.isEnabled()
				&& profile.isSystemAdmin()
		);
		// if not root user
		if (!accepted) {
			try {
				accepted = (profile.isAccountNonExpired()
						&& profile.isAccountNonLocked()
						&& profile.isCredentialsNonExpired()
						&& profile.isEnabled()
				);
				if (accepted) {
					// check access permission base on module and request
					// just checking on access permission - no need writable permission
					String requestURI = request.getRequestURI();
					String ctxPath = request.getContextPath();
					if (requestURI.startsWith(ctxPath)) {
						requestURI = requestURI.substring(ctxPath.length());
					}
					// check if request URI is home
					requestURI = ("".equals(requestURI) || "/".equals(requestURI) ? "/index" : requestURI);
					accepted = executePermissionService.hasPermissionOnRequest(profile, null, requestURI, null);
					if (!accepted) {
						logger.debug(">>> Module/User may be disabled or User has not enough permission!!!");
					}
				}
				else {
					if (!profile.isAccountNonExpired()) {
						logger.debug(">>> User may be EXPIRED!!!");
					}
					if (!profile.isAccountNonLocked()) {
						logger.debug(">>> User may be LOCKED!!!");
					}
					if (!profile.isCredentialsNonExpired()) {
						logger.debug(">>> User' CREDENTIALS may be EXPIRED!!!");
					}
					if (!profile.isEnabled()) {
						logger.debug(">>> User may be DISABLED!!!");
					}
				}
			}
			catch(Exception e) {
				logger.error(e.getMessage(), e);
				accepted = false;
			}
		}
		// if root user; just checking module has been enabled
		else {
			// check access permission base on module and request
			// just checking on access permission - no need writable permission
			String requestURI = request.getRequestURI();
			String ctxPath = request.getContextPath();
			if (requestURI.startsWith(ctxPath)) {
				requestURI = requestURI.substring(ctxPath.length());
			}
			// check if request URI is home
			requestURI = ("".equals(requestURI) || "/".equals(requestURI) ? "/index" : requestURI);
			accepted = executePermissionService.isEnabledOnRequest(null, requestURI);
			if (!accepted) {
				logger.debug("This request module not found or had been disabled: " + request.getRequestURI());
				logger.debug(">>> Module/User may be disabled or request link has not found!!!");
			}
		}
		return accepted;
	}
}
