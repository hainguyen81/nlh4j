/*
 * @(#)SensorType.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.enums;

import android.hardware.Sensor;

/**
 * {@link Sensor} enumeration type
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public enum SensorType {
    /** {@link Sensor#TYPE_ALL} */
    ALL(-1),
    /** Unknown type */
    UNKNOWN(0),
    /** {@link Sensor#TYPE_ACCELEROMETER} */
    ACCELEROMETER(1),
    /** {@link Sensor#TYPE_MAGNETIC_FIELD} */
    MAGNETIC_FIELD(2),
    /** {@link Sensor#TYPE_ORIENTATION} */
    @Deprecated()
    ORIENTATION(3),
    /** {@link Sensor#TYPE_GYROSCOPE} */
    GYROSCOPE(4),
    /** {@link Sensor#TYPE_LIGHT} */
    LIGHT(5),
    /** {@link Sensor#TYPE_PRESSURE} */
    PRESSURE(6),
    /** {@link Sensor#TYPE_TEMPERATURE} */
    @Deprecated()
    TEMPERATURE(7),
    /** {@link Sensor#TYPE_PROXIMITY} */
    PROXIMITY(8),
    /** {@link Sensor#TYPE_GRAVITY} */
    GRAVITY(9),
    /** {@link Sensor#TYPE_LINEAR_ACCELERATION} */
    LINEAR_ACCELERATION(10),
    /** {@link Sensor#TYPE_ROTATION_VECTOR} */
    ROTATION_VECTOR(11),
    /** {@link Sensor#TYPE_RELATIVE_HUMIDITY} */
    RELATIVE_HUMIDITY(12),
    /** {@link Sensor#TYPE_AMBIENT_TEMPERATURE} */
    AMBIENT_TEMPERATURE(13);

    private final int value;
    private SensorType(int value) {
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
    public static SensorType valueOf(int value) {
        for(SensorType st : SensorType.values()) {
            if (st.value == value) {
                return st;
            }
        }
        return SensorType.UNKNOWN;
    }
}
