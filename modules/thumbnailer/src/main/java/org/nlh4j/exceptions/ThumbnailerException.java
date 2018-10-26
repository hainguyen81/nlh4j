/*
 * @(#)ThumbnailerException.java 1.0 May 29, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL).
 * All rights reserved.
 */
package org.nlh4j.exceptions;

/**
 * Thumbnail Exception
 *
 * @author Hai Nguyen
 */
public class ThumbnailerException extends ApplicationException {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of {@link ThumbnailerException}
     */
    public ThumbnailerException() {}
    /**
     * Initialize a new instance of {@link ThumbnailerException}
     *
     * @param message exception message
     */
    public ThumbnailerException(String message) { super(message); }
    /**
     * Initialize a new instance of {@link ThumbnailerException}
     *
     * @param cause inner exception
     */
    public ThumbnailerException(Throwable cause) { super(cause); }
    /**
     * Initialize a new instance of {@link ThumbnailerException}
     *
     * @param message exception message
     * @param cause inner exception
     */
    public ThumbnailerException(String message, Throwable cause) { super(message, cause); }
    /**
     * Initialize a new instance of {@link ThumbnailerException}
     *
     * @param message exception message
     * @param cause inner exception
     * @param enableSuppression specify suppression
     * @param writableStackTrace specify writting stack trace
     */
    public ThumbnailerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Initialize a new instance of {@link ThumbnailerException}
     *
     * @param errorCode managed error code
     */
    public ThumbnailerException(long errorCode) {
        super(errorCode);
    }
    /**
     * Initialize a new instance of {@link ThumbnailerException}
     *
     * @param message exception message
     * @param errorCode managed error code
     */
    public ThumbnailerException(long errorCode, String message) {
        super(errorCode, message);
    }
    /**
     * Initialize a new instance of {@link ThumbnailerException}
     *
     * @param cause inner exception
     * @param errorCode managed error code
     */
    public ThumbnailerException(long errorCode, Throwable cause) {
        super(errorCode, cause);
    }
    /**
     * Initialize a new instance of {@link ThumbnailerException}
     *
     * @param message exception message
     * @param cause inner exception
     * @param errorCode managed error code
     */
    public ThumbnailerException(long errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    /**
     * Initialize a new instance of {@link ThumbnailerException}
     *
     * @param message exception message
     * @param cause inner exception
     * @param enableSuppression specify suppression
     * @param writableStackTrace specify writting stack trace
     * @param errorCode managed error code
     */
    public ThumbnailerException(long errorCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(errorCode, message, cause, enableSuppression, writableStackTrace);
    }
}
