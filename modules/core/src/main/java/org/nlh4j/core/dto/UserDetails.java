/*
 * @(#)UserDetails.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto;

import java.sql.Timestamp;
import java.util.Locale;

/**
 * An extended class of {@link org.springframework.security.core.userdetails.UserDetails}
 * for more credentials/token signatures
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public interface UserDetails extends org.springframework.security.core.userdetails.UserDetails {
    /**
     * Indicates whether the user is fulle-permission (system administrator).
     *
     * @return <code>true</code> if the user is system administrator, <code>false</code> otherwise
     */
    boolean isSystemAdmin();
    /**
     * Get the USER {@link Locale} settings
     *
     * @return the USER {@link Locale} settings
     */
    Locale getLocale();
    /**
     * Attach more information into remember token signature
     *
     * @return the data list to attach into remember token signature
     */
    String[] getRememberTokenSignatures();
    /**
     * Get the user credentials
     *
     * @return the user credentials
     */
    Object getCredentials();
    /**
     * Get the USER logged-in date/time
     *
     * @return the USER logged-in date/time
     */
    Timestamp getLoginAt();
}
