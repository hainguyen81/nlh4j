/*
 * @(#)AudioOutputFormat.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.enums;

import android.media.MediaRecorder.OutputFormat;
import android.provider.MediaStore.Audio;

/**
 * {@link Audio} output format type
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public enum AudioOutputFormat {
    /** Unknown */
    UNKNOWN(-1),
    /** {@link OutputFormat#DEFAULT} */
    DEFAULT(0),
    /** {@link OutputFormat#THREE_GPP} */
    THREE_GPP(1),
    /** {@link OutputFormat#MPEG_4} */
    MPEG_4(2),
    /** {@link OutputFormat#RAW_AMR} */
    @Deprecated
    RAW_AMR(3),
    /** {@link OutputFormat#AMR_NB} */
    AMR_NB(3),
    /** {@link OutputFormat#AMR_WB} */
    AMR_WB(4),
    /** {@link OutputFormat#AAC_ADTS} */
    AAC_ADTS(6);

    private final int value;
    private AudioOutputFormat(int value) {
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
    public static AudioOutputFormat valueOf(int value) {
        for(AudioOutputFormat st : AudioOutputFormat.values()) {
            if (st.value == value) {
                return st;
            }
        }
        return AudioOutputFormat.UNKNOWN;
    }
}
