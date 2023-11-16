/*
 * @(#)ToastUtils.java 1.0 Nov 7, 2016
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;

import org.springframework.util.Assert;

import android.content.Context;
import android.widget.Toast;

/**
 * {@link Toast} utilities
 *
 * @author Hai Nguyen
 *
 */
public final class ToastUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Show toast message box
     *
     * @param context {@link Context}
     * @param msg message to show
     */
    public static void toast(Context context, String msg) {
        toast(context, msg, Toast.LENGTH_SHORT);
    }
    /**
     * Show toast message box
     *
     * @param context {@link Context}
     * @param msg message to show
     * @param duration duration time
     */
    public static void toast(Context context, String msg, int duration) {
        Assert.notNull(context, "context");
        Toast.makeText(context, msg, duration).show();
    }
    /**
     * Show toast message box
     *
     * @param context {@link Context}
     * @param resId message resource identity to show
     */
    public static void toast(Context context, int resId) {
        toast(context, resId, Toast.LENGTH_SHORT);
    }
    /**
     * Show toast message box
     *
     * @param context {@link Context}
     * @param resId message resource identity to show
     * @param duration duration time
     */
    public static void toast(Context context, int resId, int duration) {
        Assert.notNull(context, "context");
        Toast.makeText(context, resId, duration).show();
    }
    /**
     * Show toast message box
     *
     * @param context {@link Context}
     * @param resId message resource identity to show
     * @param argIds message argument resource identities
     */
    public static void toast(Context context, int resId, Integer...argIds) {
        toast(context, resId, Toast.LENGTH_SHORT, argIds);
    }
    /**
     * Show toast message box
     *
     * @param context {@link Context}
     * @param resId message resource identity to show
     * @param duration duration time
     * @param argIds message argument resource identities
     */
    public static void toast(Context context, int resId, int duration, Integer...argIds) {
        Assert.notNull(context, "context");
        Toast.makeText(context, ResourceUtils.getResourceString(context, resId, argIds), duration).show();
    }
}
