/*
 * @(#)ShakeDetectorService.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.services;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import org.nlh4j.android.util.LogUtils;
import org.nlh4j.android.util.ServiceUtils;

/**
 * A detector of shakes using the embedded accelerometer of an Android device.
 * It works as a service broadcasting a shake event when a shake is detected.
 *
 * @author Hai Nguyen
 *
 */
public abstract class ShakeDetectorService extends AbstractService implements SensorEventListener {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    public static final long DELTA_TIME = 200000000; // in nanoseconds
    public static final double SHAKE_THRESHOLD = 5.0; // in m.s^-3
    // Minimal duration of the shake
    public static final long SHAKE_DURATION = 500000000; // in nanoseconds

    private long lastMeasureDate = -1L;
    private float[] lastValues = new float[3];
    private int shakePeriods = 0;
    private boolean enabled = false;

    /**
     * Initialize a new instance of {@link ShakeDetectorService}
     *
     * @param context {@link Context}
     */
    public ShakeDetectorService(Context context) {
        super(context);
    }

    /**
     * Get the delta time while waiting shaking in nanoseconds
     * TODO Children classes maybe override this property to customizing delta time.
     *
     * @return the delta time
     */
    public long getDeltaTime() {
        return DELTA_TIME;
    }
    /**
     * Get the shaking threshold time while shaking in m.s^-3
     * TODO Children classes maybe override this property to customizing shaking threshold time.
     *
     * @return the shaking threshold time
     */
    public double getThresholdTime() {
        return SHAKE_THRESHOLD;
    }
    /**
     * Get the shaking duration time while shaking in nanoseconds
     * TODO Children classes maybe override this property to customizing shaking duration time.
     *
     * @return the shaking duration time
     */
    public long getDurationTime() {
        return SHAKE_DURATION;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.android.services.AbstractService#doStartCommand(android.content.Intent, int, int)
     */
    @Override
    protected int doStartCommand(Intent intent, int flags, int startId) {
        if (!this.enabled) {
            LogUtils.d("Shake detector starting...");
            SensorManager sm = ServiceUtils.getSensorManager(super.getContext());
            if (sm != null) {
                sm.registerListener(this,
                        sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_GAME);
                this.enabled = true;
            }
        }
        return START_REDELIVER_INTENT;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.android.services.AbstractService#doDestroy()
     */
    @Override
    protected void doDestroy() {
        LogUtils.d("Destroying shake detector service...");
        SensorManager sm = ServiceUtils.getSensorManager(super.getContext());
        if (sm != null) sm.unregisterListener(this);
    }

    /*
     * (Non-Javadoc)
     * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
     */
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // by class
        this.doAccuracyChanged(sensor, accuracy);
    }
    /**
     * Called when the accuracy of the registered sensor has changed.<br>
     * Unlike {@link ShakeDetectorService#onSensorChanged(SensorEvent)},
     * this is only called when this accuracy value changes.<br>
     * TODO Children class maybe override this method for perform some actions
     *
     * @param sensor {@link Sensor}
     * @param accuracy the new accuracy of this sensor, one of {@link SensorManager}.SENSOR_STATUS_*
     */
    protected void doAccuracyChanged(Sensor sensor, int accuracy) {}

    /**
     * Calculate distance while shaking
     *
     * @param values1 values array 1
     * @param values2 values array 2
     *
     * @return distance
     */
    private static double calcDistance(float[] values1, float[] values2) {
        double v = 0.0;
        for (int i = 0; i < values1.length; i++) {
            v += (values1[i] - values2[i]) * (values1[i] - values2[i]);
        }
        return Math.sqrt(v);
    }

    /*
     * (Non-Javadoc)
     * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
     */
    @Override
    public final void onSensorChanged(SensorEvent event) {
        long d = System.nanoTime();
        long deltaTime = Math.max(this.getDeltaTime(), 0);
        long duration = Math.max(this.getDurationTime(), 0);
        double threshold = Math.max(this.getThresholdTime(), 0);
        if ((this.lastMeasureDate < 0) || ((d - lastMeasureDate) > deltaTime)) {
            double shakeAmplitude = (this.lastMeasureDate < 0 ? 0.0 : calcDistance(this.lastValues, event.values));
            LogUtils.d("Shake amplitude: {0}", new Object[] { String.valueOf(shakeAmplitude) });
            this.lastMeasureDate = d;
            System.arraycopy(event.values, 0, lastValues, 0, event.values.length);
            if (shakeAmplitude > threshold) {
                this.shakePeriods++;
            } else {
                this.shakePeriods = 0;
            }
        }
        // need to do actions
        if ((this.shakePeriods * deltaTime) >= duration) {
            // A shake has been detected
            this.onShake();
            // and we reset the shakePeriods
            this.shakePeriods = 0;
        }
    }
    /**
     * Perform actions on shaking
     * TODO Children classes must override this method for performing some actions while shaking device
     */
    protected abstract void onShake();
}
