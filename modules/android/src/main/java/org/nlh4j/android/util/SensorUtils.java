/*
 * @(#)SensorUtils.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import org.nlh4j.android.enums.SensorType;

/**
 * {@link Sensor} utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class SensorUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Get the sensors list from {@link Context}
     *
     * @param context {@link Context}
     * @param st {@link SensorType}
     *
     * @return the sensors list or empty if failed
     */
    public static List<Sensor> getSensors(Context context, SensorType st) {
        SensorManager sm = ServiceUtils.getSensorManager(context);
        return (sm == null ? new LinkedList<Sensor>()
                : sm.getSensorList(st == null ? SensorType.ALL.getValue() : st.getValue()));
    }

    /**
     * Get the default sensor from {@link Context}
     *
     * @param context {@link Context}
     * @param st {@link SensorType}
     *
     * @return the default sensor or NULL if failed
     */
    public static Sensor getDefaultSensor(Context context, SensorType st) {
        SensorManager sm = ServiceUtils.getSensorManager(context);
        return (sm == null ? null
                : sm.getDefaultSensor(st == null ? SensorType.ALL.getValue() : st.getValue()));
    }
}
