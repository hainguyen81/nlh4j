/*
 * @(#)AudioEncoderType.java 1.0 Nov 13, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.enums;

import android.media.MediaRecorder.AudioEncoder;
import android.provider.MediaStore.Audio;

/**
 * {@link Audio} encoder type
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public enum AudioEncoderType {
    /** Unknown */
    UNKNOWN(-1),
    /** {@link AudioEncoder#DEFAULT} */
    DEFAULT(0),
    /** {@link AudioEncoder#AMR_NB} */
    AMR_NB(1),
    /** {@link AudioEncoder#AMR_WB} */
    AMR_WB(2),
    /** {@link AudioEncoder#AAC} */
    AAC(3),
    /** {@link AudioEncoder#HE_AAC} */
    HE_AAC(4),
    /** {@link AudioEncoder#AAC_ELD} */
    AAC_ELD(5);

    private final int value;
    private AudioEncoderType(int value) {
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
    public static AudioEncoderType valueOf(int value) {
        for(AudioEncoderType st : AudioEncoderType.values()) {
            if (st.value == value) {
                return st;
            }
        }
        return AudioEncoderType.UNKNOWN;
    }
}
