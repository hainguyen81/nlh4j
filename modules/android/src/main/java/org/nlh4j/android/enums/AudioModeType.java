/*
 * @(#)AudioModeType.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.enums;

/**
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public enum AudioModeType {
    UNKNOWN(-3),
    INVALID(-2),
    CURRENT(-1),
    NORMAL(0),
    RINGTONE(1),
    IN_CALL(2),
    IN_COMMUNICATION(3);

    private final int value;
    private AudioModeType(int value) {
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
    public static AudioModeType valueOf(int value) {
        for(AudioModeType st : AudioModeType.values()) {
            if (st.value == value) {
                return st;
            }
        }
        return AudioModeType.UNKNOWN;
    }
}
