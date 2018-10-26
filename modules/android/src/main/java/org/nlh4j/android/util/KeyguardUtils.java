/*
 * @(#)KeyguardUtils.java 1.0 Nov 9, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;

import android.app.KeyguardManager;
import android.content.Context;

/**
 * Keyguard utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class KeyguardUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Get a boolean value indicating that keyguard has been locked
     *
     * @param context {@link Context}
     *
     * @return true for locked; else false
     */
    public static boolean isKeyguardLocked(Context context) {
        KeyguardManager km = ServiceUtils.getKeyguardManager(context);
        return (km != null && km.isKeyguardLocked() && km.isKeyguardSecure());
    }

    /**
     * Get a boolean value indicating that keyguard has been restricted input mode
     *
     * @param context {@link Context}
     *
     * @return true for restricted; else false
     */
    public static boolean inKeyguardRestrictedInputMode(Context context) {
        KeyguardManager km = ServiceUtils.getKeyguardManager(context);
        return (km != null && km.inKeyguardRestrictedInputMode());
    }
}
