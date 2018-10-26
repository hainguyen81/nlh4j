/*
 * @(#)AudioSourceType.java 1.0 Nov 12, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.enums;

import android.media.MediaRecorder.AudioSource;

/**
 * {@link Audio} source type
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public enum AudioSourceType {
    /** Unknown */
    UNKNOWN(-1),
    /** {@link AudioSource#DEFAULT} */
    DEFAULT(0),
    /** {@link AudioSource#MIC} */
    MIC(1),
    /** {@link AudioSource#VOICE_UPLINK} */
    VOICE_UPLINK(2),
    /** {@link AudioSource#VOICE_DOWNLINK} */
    VOICE_DOWNLINK(3),
    /** {@link AudioSource#VOICE_CALL} */
    VOICE_CALL(4),
    /** {@link AudioSource#CAMCORDER} */
    CAMCORDER(5),
    /** {@link AudioSource#VOICE_RECOGNITION} */
    VOICE_RECOGNITION(6),
    /** {@link AudioSource#VOICE_COMMUNICATION} */
    VOICE_COMMUNICATION(7);

    private final int value;
    private AudioSourceType(int value) {
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
    public static AudioSourceType valueOf(int value) {
        for(AudioSourceType st : AudioSourceType.values()) {
            if (st.value == value) {
                return st;
            }
        }
        return AudioSourceType.UNKNOWN;
    }
}
