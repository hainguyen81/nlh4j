/*
 * @(#)AbstractLoginController.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import org.nlh4j.core.controller.AbstractController;
import org.nlh4j.core.service.MessageService;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.RequestUtils;
import org.nlh4j.util.SessionUtils;

/**
 * Login controller
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
@RequestMapping(value = "/login")
public abstract class AbstractLoginController extends AbstractController {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

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
	    if (!Boolean.TRUE.equals(expired)) {
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