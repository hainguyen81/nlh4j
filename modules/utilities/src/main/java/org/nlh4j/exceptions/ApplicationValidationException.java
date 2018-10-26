/*
 * @(#)ApplicationInvalidRequestException.java 1.0 Feb 15, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.exceptions;

import org.springframework.validation.Errors;

import lombok.Getter;

/**
 * Application validation exception
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class ApplicationValidationException extends ApplicationRuntimeException {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    /** application validation errors */
    @Getter
    private transient Errors errors;

    /**
     * Initialize a new instance of {@link ApplicationValidationException}
     *
     * @param errors application validation errors
     */
    public ApplicationValidationException(Errors errors) {
    	super();
    	this.errors = errors;
	}

    /**
     * Initialize a new instance of {@link ApplicationValidationException}
     *
     * @param errorCode managed error code
     * @param errors application validation errors
     */
    public ApplicationValidationException(long errorCode, Errors errors) {
        super(errorCode);
        this.errors = errors;
    }
}
