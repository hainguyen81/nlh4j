/*
 * @(#)ThumbnailerRuntimeException.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL).
 * All rights reserved.
 */
package org.nlh4j.exceptions;

/**
 * Thumbnail Runtime Exception
 *
 * @author Hai Nguyen
 */
public class ThumbnailerRuntimeException extends ApplicationRuntimeException {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of {@link ThumbnailerRuntimeException}
     */
    public ThumbnailerRuntimeException() {}
    /**
     * Initialize a new instance of {@link ThumbnailerRuntimeException}
     *
     * @param message exception message
     */
    public ThumbnailerRuntimeException(String message) { super(message); }
    /**
     * Initialize a new instance of {@link ThumbnailerRuntimeException}
     *
     * @param cause inner exception
     */
    public ThumbnailerRuntimeException(Throwable cause) { super(cause); }
    /**
     * Initialize a new instance of {@link ThumbnailerRuntimeException}
     *
     * @param message exception message
     * @param cause inner exception
     */
    public ThumbnailerRuntimeException(String message, Throwable cause) { super(message, cause); }
    /**
     * Initialize a new instance of {@link ThumbnailerRuntimeException}
     *
     * @param message exception message
     * @param cause inner exception
     * @param enableSuppression specify suppression
     * @param writableStackTrace specify writting stack trace
     */
    public ThumbnailerRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Initialize a new instance of {@link ThumbnailerRuntimeException}
     *
     * @param errorCode managed error code
     */
    public ThumbnailerRuntimeException(long errorCode) {
        super(errorCode);
    }
    /**
     * Initialize a new instance of {@link ThumbnailerRuntimeException}
     *
     * @param message exception message
     * @param errorCode managed error code
     */
    public ThumbnailerRuntimeException(long errorCode, String message) {
        super(errorCode, message);
    }
    /**
     * Initialize a new instance of {@link ThumbnailerRuntimeException}
     *
     * @param cause inner exception
     * @param errorCode managed error code
     */
    public ThumbnailerRuntimeException(long errorCode, Throwable cause) {
        super(errorCode, cause);
    }
    /**
     * Initialize a new instance of {@link ThumbnailerRuntimeException}
     *
     * @param message exception message
     * @param cause inner exception
     * @param errorCode managed error code
     */
    public ThumbnailerRuntimeException(long errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    /**
     * Initialize a new instance of {@link ThumbnailerRuntimeException}
     *
     * @param message exception message
     * @param cause inner exception
     * @param enableSuppression specify suppression
     * @param writableStackTrace specify writting stack trace
     * @param errorCode managed error code
     */
    public ThumbnailerRuntimeException(long errorCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(errorCode, message, cause, enableSuppression, writableStackTrace);
    }
}
