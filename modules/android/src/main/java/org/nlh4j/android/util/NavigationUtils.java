/*
 * @(#)NavigationUtils.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;

import org.springframework.util.Assert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

/**
 * Navigation utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class NavigationUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Open the specified activity from the present activity
     *
     * @param <T> the opened activity type
     * @param activity the present activity
     * @param activityClass activity class to open
     * @param flags the intent flags
     * @param options the bundle data options
     * @param finished specify finishing the present activity
     */
    public static <T extends Activity> void openActivity(
            Activity activity, Class<T> activityClass, int flags, Bundle options, boolean finished) {
        Assert.notNull(activity, "activity");
        Assert.notNull(activityClass, "activityClass");
        Intent intent = new Intent(activity.getApplicationContext(), activityClass);
        intent.setFlags(flags);
        openActivity(activity, intent, options, finished);
    }
    /**
     * Open the specified activity from the present activity
     *
     * @param <T> the opened activity type
     * @param activity the present activity
     * @param activityClass activity class to open
     * @param flags the intent flags
     * @param finished specify finishing the present activity
     */
    public static <T extends Activity> void openActivity(
            Activity activity, Class<T> activityClass, int flags, boolean finished) {
        Assert.notNull(activity, "activity");
        Assert.notNull(activityClass, "activityClass");
        Intent intent = new Intent(activity.getApplicationContext(), activityClass);
        intent.setFlags(flags);
        openActivity(activity, intent, finished);
    }
    /**
     * Open the specified activity from the present activity
     *
     * @param <T> the opened activity type
     * @param activity the present activity
     * @param activityClass activity class to open
     * @param finished specify finishing the present activity
     */
    public static <T extends Activity> void openActivity(
            Activity activity, Class<T> activityClass, boolean finished) {
        Assert.notNull(activity, "activity");
        Assert.notNull(activityClass, "activityClass");
        openActivity(activity, new Intent(activity.getApplicationContext(), activityClass), finished);
    }
    /**
     * Open the specified activity from the present activity
     *
     * @param <T> the opened activity type
     * @param activity the present activity
     * @param activityClass activity class to open
     */
    public static <T extends Activity> void openActivity(Activity activity, Class<T> activityClass) {
        Assert.notNull(activity, "activity");
        Assert.notNull(activityClass, "activityClass");
        openActivity(activity, new Intent(activity.getApplicationContext(), activityClass), Boolean.TRUE);
    }

    /**
     * Open the specified activity from the present context
     *
     * @param <T> the opened activity type
     * @param context the present context
     * @param activityClass activity class to open
     * @param flags the intent flags
     * @param options the bundle data options
     */
    public static <T extends Activity> void openActivity(
            Context context, Class<T> activityClass, int flags, Bundle options) {
        Assert.notNull(context, "activity");
        Assert.notNull(activityClass, "activityClass");
        Intent intent = new Intent(context, activityClass);
        intent.setFlags(flags);
        openActivity(context, intent, options);
    }
    /**
     * Open the specified activity from the present context
     *
     * @param <T> the opened activity type
     * @param context the present context
     * @param activityClass activity class to open
     * @param flags the intent flags
     */
    public static <T extends Activity> void openActivity(
            Context context, Class<T> activityClass, int flags) {
        Assert.notNull(context, "context");
        Assert.notNull(activityClass, "activityClass");
        Intent intent = new Intent(context, activityClass);
        intent.setFlags(flags);
        openActivity(context, intent);
    }
    /**
     * Open the specified activity from the present context
     *
     * @param <T> the opened activity type
     * @param context the present context
     * @param activityClass activity class to open
     */
    public static <T extends Activity> void openActivity(
            Context context, Class<T> activityClass) {
        Assert.notNull(context, "context");
        Assert.notNull(activityClass, "activityClass");
        openActivity(context, new Intent(context, activityClass));
    }

    /**
     * Open the specified activity from the present activity
     *
     * @param <T> the opened activity type
     * @param activity the present activity
     * @param intent to send
     * @param options the bundle data options
     * @param finished specify finishing the present activity
     */
    public static <T extends Activity> void openActivity(
            Activity activity, Intent intent, Bundle options, boolean finished) {
        Assert.notNull(activity, "activity");
        Assert.notNull(intent, "Intent");
        activity.startActivity(intent, options);
        if (finished) activity.finish();
    }
    /**
     * Open the specified activity from the present activity
     *
     * @param <T> the opened activity type
     * @param activity the present activity
     * @param intent to send
     * @param finished specify finishing the present activity
     */
    public static <T extends Activity> void openActivity(
            Activity activity, Intent intent, boolean finished) {
        Assert.notNull(activity, "activity");
        Assert.notNull(intent, "Intent");
        activity.startActivity(intent);
        if (finished) activity.finish();
    }
    /**
     * Open the specified activity from the present context
     *
     * @param context the present context
     * @param intent to send
     * @param options the bundle data options
     */
    public static void openActivity(
            Context context, Intent intent, Bundle options) {
        Assert.notNull(context, "context");
        Assert.notNull(intent, "Intent");
        context.startActivity(intent, options);
    }
    /**
     * Open the specified activity from the present context
     *
     * @param context the present context
     * @param intent to send
     */
    public static void openActivity(
            Context context, Intent intent) {
        Assert.notNull(context, "context");
        Assert.notNull(intent, "Intent");
        context.startActivity(intent);
    }

    /**
     * Open the {@link Settings#ACTION_LOCATION_SOURCE_SETTINGS} from the present context
     *
     * @param context the present context
     */
    public static void openLocationSourceSettings(Context context) {
        openActivity(context, new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }
    /**
     * Open the {@link Settings#ACTION_NETWORK_OPERATOR_SETTINGS} from the present context
     *
     * @param context the present context
     */
    public static void openNetworkOperatorSettings(Context context) {
        openActivity(context, new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS));
    }
    /**
     * Open the WIFI settings (see {@link Settings#ACTION_WIRELESS_SETTINGS}) from the present context
     *
     * @param context the present context
     */
    public static void openWirelessSettings(Context context) {
        openActivity(context, new Intent(Settings.ACTION_WIRELESS_SETTINGS));
    }
}
