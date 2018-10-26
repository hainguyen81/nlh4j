/*
 * @(#)LoginController.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.login.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RestController;

import org.nlh4j.AbstractLoginController;
import org.nlh4j.core.annotation.ExecutePermission;
import org.nlh4j.util.StringUtils;
import org.nlh4j.web.base.common.Module;
import org.nlh4j.web.base.common.ModuleConst;

/**
 * Login controller
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController(value = "baseLoginController")
@ExecutePermission(value = { ModuleConst.CMN_LOGIN })
public class LoginController extends AbstractLoginController {

	/** */
	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * @see org.nlh4j.AbstractLoginController#getIndexPage()
	 */
	@Override
	protected String getIndexPage() {
		return Module.CMN_LOGIN.getIndexPage();
	}

	/* (Non-Javadoc)
	 * @see org.nlh4j.AbstractLoginController#dispatchAuthenticationException(org.springframework.security.core.AuthenticationException)
	 */
	@Override
	protected String dispatchAuthenticationException(AuthenticationException ex) {
		// parse message by exception class name
		String message = null;
		if (ex != null) {
			String msgKey = ex.getClass().getName();
			message = super.messageService.getMessage(msgKey, (Object[]) null, null);
			message = (StringUtils.hasText(message) ? message
					: super.messageService.getMessage(String.valueOf(HttpStatus.UNAUTHORIZED.value())));
		}
		return message;
	}
}
