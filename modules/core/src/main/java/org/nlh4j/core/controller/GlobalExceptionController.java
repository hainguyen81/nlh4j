/*
 * @(#)GlobalExceptionController.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.nlh4j.exceptions.ApplicationRuntimeException;

/**
 * GlobalExceptionController.javaのクラス。
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version 0.6
 *
 */
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

	/**
	 * slf4j
	 */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Process exception
	 *
	 * @param httpRequest HTTP request
	 * @param httpResponse HTTP response
	 * @param e global exception
	 *
	 * @return response entity that has been attached error
	 */
	@ExceptionHandler
	public ResponseEntity<ApplicationRuntimeException> handleCustomException(
			HttpServletRequest httpRequest, HttpServletResponse httpResponse, Throwable e) {
		ApplicationRuntimeException ex = null;
		// Error message
		String errMsg = e.getMessage();
		Throwable throwable = e.getCause();
		// Response type
		if (throwable != null) {
			ex = new ApplicationRuntimeException(errMsg, e);
		}
		else {
			ex = new ApplicationRuntimeException(errMsg, e);
		}
		return new ResponseEntity<ApplicationRuntimeException>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
