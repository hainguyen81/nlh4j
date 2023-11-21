/*
 * @(#)CompatibilityUtil.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;

import android.content.Context;
import android.os.Build;

/**
 * Detect compatibility utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class CompatibilityUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Get the current Android API level.
     * @return the current Android API level.
     */
    public static int getSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * Determine if the device is running API level 8 or higher.
     * @return true for running API level 8 or higher.
     */
    public static boolean isFroyo() {
        return (getSdkVersion() >= Build.VERSION_CODES.FROYO);
    }

    /**
     * Determine if the device is running API level 9 or higher.
     * @return true for running API level 9 or higher.
     */
    public static boolean isGingerBread() {
        return (getSdkVersion() >= Build.VERSION_CODES.GINGERBREAD);
    }

    /**
     * Determine if the device is running API level 10 or higher.
     * @return true for running API level 10 or higher.
     */
    public static boolean isGingerBreadMr1() {
        return (getSdkVersion() >= Build.VERSION_CODES.GINGERBREAD_MR1);
    }

    /**
     * Determine if the device is running API level 11 or higher.
     * @return true for running API level 11 or higher.
     */
    public static boolean isHoneycomb() {
        return (getSdkVersion() >= Build.VERSION_CODES.HONEYCOMB);
    }

    /**
     * Determine if the device is running API level 12 or higher.
     * @return true for running API level 12 or higher.
     */
    public static boolean isHoneycombMr1() {
        return (getSdkVersion() >= Build.VERSION_CODES.HONEYCOMB_MR1);
    }

    /**
     * Determine if the device is running API level 13 or higher.
     * @return true for running API level 13 or higher.
     */
    public static boolean isHoneycombMr2() {
        return (getSdkVersion() >= Build.VERSION_CODES.HONEYCOMB_MR2);
    }

    /**
     * Determine if the device is running API level 14 or higher.
     * @return true for running API level 14 or higher.
     */
    public static boolean isIceCreamSandwich() {
        return (getSdkVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH);
    }

    /**
     * Determine if the device is running API level 15 or higher.
     * @return true for running API level 15 or higher.
     */
    public static boolean isIceCreamSandwichMr1() {
        return (getSdkVersion() >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1);
    }

    /**
     * Determine if the device is running API level 16 or higher.
     * @return true for running API level 16 or higher.
     */
    public static boolean isJellyBean() {
        return (getSdkVersion() >= Build.VERSION_CODES.JELLY_BEAN);
    }

    /**
     * Determine if the device is a HoneyComb tablet.
     *
     * @param context The calling context.
     *
     * @return true for Honeycomb Tablet; else false
     */
    public static boolean isHoneycombTablet(Context context) {
        return isHoneycomb() && MetricUtils.isTablet(context);
    }
}
