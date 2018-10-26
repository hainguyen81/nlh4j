/*
 * @(#)WifiState.java 1.0 Nov 14, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.enums;

/**
 * Wifi state
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public enum WifiState {
    UNKNOWN(4),
    DISABLING(0),
    DISABLED(1),
    ENABLING(2),
    ENABLED(3);

    private final int value;
    private WifiState(int value) {
        this.value = value;
    }

    /**
     * Get the value
     *
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Get enumeration from integer value
     *
     * @param value to check
     *
     * @return enumeration type
     */
    public static WifiState valueOf(int value) {
        for(WifiState st : WifiState.values()) {
            if (st.value == value) {
                return st;
            }
        }
        return WifiState.UNKNOWN;
    }
}
