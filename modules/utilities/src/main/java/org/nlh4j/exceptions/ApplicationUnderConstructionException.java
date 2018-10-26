/*
 * @(#)ApplicationUnderConstructionException.java 1.0 Oct 28, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.exceptions;

/**
 * Under-construction runtime exception
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class ApplicationUnderConstructionException extends ApplicationRuntimeException {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of {@link ApplicationUnderConstructionException}
     */
    public ApplicationUnderConstructionException() {}
    /**
     * Initialize a new instance of {@link ApplicationUnderConstructionException}
     *
     * @param message exception message
     */
    public ApplicationUnderConstructionException(String message) { super(message); }
    /**
     * Initialize a new instance of {@link ApplicationUnderConstructionException}
     *
     * @param cause inner exception
     */
    public ApplicationUnderConstructionException(Throwable cause) { super(cause); }
    /**
     * Initialize a new instance of {@link ApplicationUnderConstructionException}
     *
     * @param message exception message
     * @param cause inner exception
     */
    public ApplicationUnderConstructionException(String message, Throwable cause) { super(message, cause); }
    /**
     * Initialize a new instance of {@link ApplicationUnderConstructionException}
     *
     * @param message exception message
     * @param cause inner exception
     * @param enableSuppression specify suppression
     * @param writableStackTrace specify writting stack trace
     */
    public ApplicationUnderConstructionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Initialize a new instance of {@link ApplicationUnderConstructionException}
     *
     * @param errorCode managed error code
     */
    public ApplicationUnderConstructionException(long errorCode) {
        super(errorCode);
    }
    /**
     * Initialize a new instance of {@link ApplicationUnderConstructionException}
     *
     * @param message exception message
     * @param errorCode managed error code
     */
    public ApplicationUnderConstructionException(long errorCode, String message) {
        super(errorCode, message);
    }
    /**
     * Initialize a new instance of {@link ApplicationUnderConstructionException}
     *
     * @param cause inner exception
     * @param errorCode managed error code
     */
    public ApplicationUnderConstructionException(long errorCode, Throwable cause) {
        super(errorCode, cause);
    }
    /**
     * Initialize a new instance of {@link ApplicationUnderConstructionException}
     *
     * @param message exception message
     * @param cause inner exception
     * @param errorCode managed error code
     */
    public ApplicationUnderConstructionException(long errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    /**
     * Initialize a new instance of {@link ApplicationUnderConstructionException}
     *
     * @param message exception message
     * @param cause inner exception
     * @param enableSuppression specify suppression
     * @param writableStackTrace specify writting stack trace
     * @param errorCode managed error code
     */
    public ApplicationUnderConstructionException(long errorCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(errorCode, message, cause, enableSuppression, writableStackTrace);
    }
}
