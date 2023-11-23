/*
 * @(#)ApplicationUnderConstructionException.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.exceptions;

/**
 * Under-construction runtime exception
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class ApplicationLicenseException extends ApplicationRuntimeException {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of {@link ApplicationLicenseException}
     */
    public ApplicationLicenseException() {}
    /**
     * Initialize a new instance of {@link ApplicationLicenseException}
     *
     * @param message exception message
     */
    public ApplicationLicenseException(String message) { super(message); }
    /**
     * Initialize a new instance of {@link ApplicationLicenseException}
     *
     * @param cause inner exception
     */
    public ApplicationLicenseException(Throwable cause) { super(cause); }
    /**
     * Initialize a new instance of {@link ApplicationLicenseException}
     *
     * @param message exception message
     * @param cause inner exception
     */
    public ApplicationLicenseException(String message, Throwable cause) { super(message, cause); }
    /**
     * Initialize a new instance of {@link ApplicationLicenseException}
     *
     * @param message exception message
     * @param cause inner exception
     * @param enableSuppression specify suppression
     * @param writableStackTrace specify writting stack trace
     */
    public ApplicationLicenseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Initialize a new instance of {@link ApplicationLicenseException}
     *
     * @param errorCode managed error code
     */
    public ApplicationLicenseException(long errorCode) {
        super(errorCode);
    }
    /**
     * Initialize a new instance of {@link ApplicationLicenseException}
     *
     * @param message exception message
     * @param errorCode managed error code
     */
    public ApplicationLicenseException(long errorCode, String message) {
        super(errorCode, message);
    }
    /**
     * Initialize a new instance of {@link ApplicationLicenseException}
     *
     * @param cause inner exception
     * @param errorCode managed error code
     */
    public ApplicationLicenseException(long errorCode, Throwable cause) {
        super(errorCode, cause);
    }
    /**
     * Initialize a new instance of {@link ApplicationLicenseException}
     *
     * @param message exception message
     * @param cause inner exception
     * @param errorCode managed error code
     */
    public ApplicationLicenseException(long errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    /**
     * Initialize a new instance of {@link ApplicationLicenseException}
     *
     * @param message exception message
     * @param cause inner exception
     * @param enableSuppression specify suppression
     * @param writableStackTrace specify writting stack trace
     * @param errorCode managed error code
     */
    public ApplicationLicenseException(long errorCode, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(errorCode, message, cause, enableSuppression, writableStackTrace);
    }
}
