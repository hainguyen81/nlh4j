/*
 * @(#)AbstractLoginController.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j;

import org.apache.commons.lang3.BooleanUtils;
import org.nlh4j.core.controller.AbstractController;
import org.nlh4j.core.service.MessageService;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.RequestUtils;
import org.nlh4j.util.SessionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Login controller
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
@RequestMapping(value = "/login")
public abstract class AbstractLoginController extends AbstractController {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * authentication entry point for rendering as login-form POST action<br>
	 * default is `j_spring_security_check` for spring security 4 or below
	 */
	@Value("${sec.auth.entry.login.url:j_spring_security_check}")
	@Getter(value = AccessLevel.PROTECTED)
	private String authenticationEntryPointUrl;
	/**
	 * the user parameter for the authentication entry point for rendering as login-form user<br>
	 * default is `j_username` for spring security 4 or below
	 */
	@Value("${sec.auth.login.param.user:j_username}")
	@Getter(value = AccessLevel.PROTECTED)
	private String authenticationEntryUserName;
	/**
	 * the password parameter for the authentication entry point for rendering as login-form password<br>
	 * default is `j_password` for spring security 4 or below
	 */
	@Value("${sec.auth.login.param.password:j_password}")
	@Getter(value = AccessLevel.PROTECTED)
	private String authenticationEntryPassword;
	/**
	 * the remember parameter for the authentication entry point for rendering as login-form password<br>
	 * default is `j_remember` for spring security 4 or below
	 */
	@Value("${sec.auth.login.param.remember:j_remember}")
	@Getter(value = AccessLevel.PROTECTED)
	private String authenticationEntryRemember;

	/**
	 * Login page
	 * @return login page
	 */
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public final ModelAndView index() {
		ModelAndView mv = new ModelAndView(this.getIndexPage());
	    AuthenticationException ae = null;
	    // check for expired or used by another
	    boolean expired = RequestUtils.getBooleanParameter("expired");
	    if (BooleanUtils.isNotTrue(expired)) {
		    if (super.getRequest() != null) {
			    ae = RequestUtils.getRequestAttribute(
			    		super.getRequest(), WebAttributes.AUTHENTICATION_EXCEPTION, AuthenticationException.class);
		    }
		    if (super.getSession() != null && ae == null) {
		        ae = BeanUtils.safeType(
		                SessionUtils.getSessionValue(WebAttributes.AUTHENTICATION_EXCEPTION),
		                AuthenticationException.class);
		    }

		    // as expired or used by another
	    } else {
	    	String msgKey = null;
	    	// catch exception while context has not been initialized
	    	try {
		    	msgKey = SessionAuthenticationException.class.getName();
		    	MessageService msgSrv = super.messageService;
		    	msgSrv = (msgSrv == null ? super.findBean(MessageService.class) : msgSrv);
		    	msgKey = (msgSrv == null ? msgKey : msgSrv.getMessage(msgKey));
	    	} catch (Exception e) {
	    		if (logger != null) logger.warn(e.getMessage());
	    		else e.printStackTrace();
	    		msgKey = e.getMessage();
	    	} finally {
	    		ae = new SessionAuthenticationException(msgKey);
	    		RequestUtils.setRequestAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, ae);
	    	}
	    }
	    if (ae != null) {
	        mv.addObject("error", this.dispatchAuthenticationException(ae));
	    }

	    // TODO Important: need there values for rendering login-form due to the changes from spring-security
	    // add default render values
	    mv.addObject("loginAction", getAuthenticationEntryPointUrl());
	    mv.addObject("loginUser", getAuthenticationEntryUserName());
	    mv.addObject("loginPwd", getAuthenticationEntryPassword());
	    mv.addObject("loginRemember", getAuthenticationEntryRemember());
		return mv;
	}
	/**
     * Get the index page path
     * @return the index page path
     */
    protected abstract String getIndexPage();
    /**
     * Dispatch {@link AuthenticationException} to simple message
     * @TODO Children classes maybe override this method for custom authentication failed message
     *
     * @param ex {@link AuthenticationException} to dispatch
     *
     * @return the authentication failed message or null if not using
     */
    protected String dispatchAuthenticationException(AuthenticationException ex) {
        return (ex != null ? ex.getMessage() : null);
    }
}