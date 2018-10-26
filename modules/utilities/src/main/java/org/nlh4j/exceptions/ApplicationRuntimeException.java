/*
 * @(#)ApplicationRuntimeException.java 1.0 Oct 28, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.exceptions;

/**
 * Application runtime exception
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class ApplicationRuntimeException extends RuntimeException {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    private long errorCode = -99999;

    /**
     * Initialize a new instance of {@link ApplicationRuntimeException}
     */
    public ApplicationRuntimeException() {}
    /**
     * Initialize a new instance of {@link ApplicationRuntimeException}
     *
     * @param message exception message
     */
    public ApplicationRuntimeException(String message) { super(message); }
    /**
     * Initialize a new instance of {@link ApplicationRuntimeException}
     *
     * @param cause inner exception
     */
    public ApplicationRuntimeException(Throwable cause) { super(cause); }
    /**
     * Initialize a new instance of {@link ApplicationRuntimeException}
     *
     * @param message exception message
     * @param cause inner exception
     */
    public ApplicationRuntimeException(String message, Throwable cause) { super(message, cause); }
    /**
     * Initialize a new instance of {@link ApplicationRuntimeException}
     *
     * @param message exception message
     * @param cause inner exception
     * @param enableSuppression specify suppression
     * @param writableStackTrace specify writting stack trace
     */
    public ApplicationRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Initialize a new instance of {@link ApplicationRuntimeException}
     *
     * @param errorCode managed error code
     */
    public ApplicationRuntimeException(long errorCode) {
        this.errorCode = errorCode;
    }
    /**
     * Initialize a new instance of {@link ApplicationRuntimeException}
     *
     * @param message exception message
     * @param errorCode managed error code
     */
    public ApplicationRuntimeException(long errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    /**
     * Initialize a new instance of {@link ApplicationRuntimeException}
     *
     * @param cause inner exception
     * @param errorCode managed error code
     */
    public ApplicationRuntimeException(long errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }
    /**
     * Initialize a new instance of {@link ApplicationRuntimeException}
     *
     * @param message exception message
     * @param cause inner exception
     * @param errorCode managed error code
     */
    public ApplicationRuntimeException(long errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    /**
     * Initialize a new instance of {@link ApplicationRuntimeException}
     *
     * @param message exception message
     * @param cause inner exception
     * @param enableSuppression specify suppression
     * @param writableStackTrace specify writting stack trace
     * @param errorCode managed error code
     */
    public ApplicationRuntimeException(long errorCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }

    /**
     * Get the managed error code
     *
     * @return the managed error code
     */
    public final long getErrorCode() {
        return errorCode;
    }
}
