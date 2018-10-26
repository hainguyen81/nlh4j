/*
 * @(#)SecurityController.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.nlh4j.core.controller.AbstractController;
import org.nlh4j.core.util.AuthenticationUtils;

/**
 * Security controller
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
public class SecurityController extends AbstractController {

	/** */
	private static final long serialVersionUID = 1L;
	private static final String CSRF_TOKEN_HEADER_NAME = "JCSRFTOKEN";

	/**
	 * Initialize a new instance of {@link SecurityController}
	 */
	public SecurityController() {}

	/**
	 * Get the concurrent authentication name
	 *
	 * @return the concurrent authentication name
	 */
	@RequestMapping(value = "/username", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String currentUserNameSimple() {
		Authentication auth = AuthenticationUtils.getCurrentPrincipal();
		return (auth == null ? null : auth.getName());
	}

	/**
	 * Set the CSRF token to response header
	 *
	 * @param request HTTP request
	 * @param response HTTP response
	 * @param principal authentication
	 */
	@RequestMapping(value = "/csrftoken", method = { RequestMethod.HEAD })
	public void requireCsrfToken(HttpServletRequest request, HttpServletResponse response, Principal principal) {
		if (response != null && principal != null) {
			response.setHeader(CSRF_TOKEN_HEADER_NAME,
					AuthenticationUtils.generateCsrfToken(request, response, principal, true, false));
		}
	}
}
