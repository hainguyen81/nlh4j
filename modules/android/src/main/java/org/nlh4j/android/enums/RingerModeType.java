/*
 * @(#)RingerModeType.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.enums;

import android.provider.MediaStore.Audio;

/**
 * {@link Audio} ringer mode type
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public enum RingerModeType {
    UNKNOWN(-1),
    SILENT(0),
    VIBRATE(1),
    NORMAL(2);

    private final int value;
    private RingerModeType(int value) {
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
    public static RingerModeType valueOf(int value) {
        for(RingerModeType st : RingerModeType.values()) {
            if (st.value == value) {
                return st;
            }
        }
        return RingerModeType.UNKNOWN;
    }
}
