/*
 * @(#)MetricUtils.java 1.0 Oct 28, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Metric utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class MetricUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Get the status bar height
     *
     * @param activity activity to detect
     *
     * @return the status bar height or 0
     */
    public static int getStatusBarHeight(Activity activity) {
        int h = 0;
        if (activity != null) {
            Window w = activity.getWindow();
            if (w != null) {
                View v = w.getDecorView();
                if (v != null) {
                    Rect r = new Rect();
                    v.getWindowVisibleDisplayFrame(r);
                    h = r.top;
                }
            }
        }
        return h;
    }

    /**
     * Get the title bar height
     *
     * @param activity activity to detect
     *
     * @return the title bar height
     */
    public static int getTitleBarHeight(Activity activity) {
        int h = 0;
        if (activity != null) {
            Window w = activity.getWindow();
            if (w != null) {
                View v = w.findViewById(Window.ID_ANDROID_CONTENT);
                if (v != null) {
                    h = v.getTop();
                    h -= getStatusBarHeight(activity);
                }
            }
        }
        return h;
    }

    /**
     * Get window display metrics
     *
     * @param activity activity to detect
     *
     * @return window display metrics or NULL if failed
     */
    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics dm = null;
        if (activity != null) {
            WindowManager wm = activity.getWindowManager();
            if (wm != null) {
                Display dp = wm.getDefaultDisplay();
                if (dp != null) {
                    dm = new DisplayMetrics();
                    dp.getMetrics(dm);
                }

            }
        }
        return dm;
    }
    /**
     * Get window width display in pixels
     *
     * @param context to detect
     *
     * @return width in pixels. or -1 if failed
     */
    public static int getDisplayWidthPixels(Context context) {
        DisplayMetrics dm = ResourceUtils.getResourceDisplayMetrics(context);
        return (dm == null ? -1 : dm.widthPixels);
    }
    /**
     * Get window width display in pixels
     *
     * @param activity activity to detect
     *
     * @return width in pixels. or -1 if failed
     */
    public static int getDisplayWidthPixels(Activity activity) {
        DisplayMetrics dm = getDisplayMetrics(activity);
        return (dm == null ? -1 : dm.widthPixels);
    }
    /**
     * Get window height display in pixels
     *
     * @param context to detect
     *
     * @return height in pixels. or -1 if failed
     */
    public static int getDisplayHeightPixels(Context context) {
        DisplayMetrics dm = ResourceUtils.getResourceDisplayMetrics(context);
        return (dm == null ? -1 : dm.heightPixels);
    }
    /**
     * Get window height display in pixels
     *
     * @param activity activity to detect
     *
     * @return height in pixels. or -1 if failed
     */
    public static int getDisplayHeightPixels(Activity activity) {
        DisplayMetrics dm = getDisplayMetrics(activity);
        return (dm == null ? -1 : dm.heightPixels);
    }
    /**
     * Convert ratio to width in pixels
     *
     * @param context to detect
     * @param ratio to convert
     *
     * @return width in pixels. or -1 if failed
     */
    public static double ratioToWidthPixels(Context context, float ratio) {
        double w = -1;
        if (0 <= ratio && ratio < 1) {
            w = getDisplayWidthPixels(context);
            w = (w >= 0 ? (w * ratio) : w);
        }
        return w;
    }
    /**
     * Convert ratio to width in pixels
     *
     * @param activity activity to detect
     * @param ratio to convert
     *
     * @return width in pixels. or -1 if failed
     */
    public static double ratioToWidthPixels(Activity activity, float ratio) {
        double w = -1;
        if (0 <= ratio && ratio < 1) {
            w = getDisplayWidthPixels(activity);
            w = (w >= 0 ? (w * ratio) : w);
        }
        return w;
    }
    /**
     * Convert ratio to height in pixels
     *
     * @param context to detect
     * @param ratio to convert
     *
     * @return height in pixels. or -1 if failed
     */
    public static double ratioToHeightPixels(Context context, float ratio) {
        double h = -1;
        if (0 <= ratio && ratio < 1) {
            h = getDisplayWidthPixels(context);
            h = (h >= 0 ? (h * ratio) : h);
        }
        return h;
    }
    /**
     * Convert ratio to height in pixels
     *
     * @param activity activity to detect
     * @param ratio to convert
     *
     * @return height in pixels. or -1 if failed
     */
    public static double ratioToHeightPixels(Activity activity, float ratio) {
        double h = -1;
        if (0 <= ratio && ratio < 1) {
            h = getDisplayWidthPixels(activity);
            h = (h >= 0 ? (h * ratio) : h);
        }
        return h;
    }

    /**
     * Converts the given device independent pixels (DIP) value into the corresponding pixels
     * value for the current screen.
     *
     * @param context {@link Context}
     * @param dip to convert
     *
     * @return The pixels value for the current screen of the given DIP value.
     */
    public static float dipToPixels(Context context, float dip) {
        DisplayMetrics dm = ResourceUtils.getResourceDisplayMetrics(context);
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, dm);
    }

    /**
     * Converts the given pixels value into the corresponding device independent pixels (DIP)
     * value for the current screen.
     *
     * @param context {@link Context}
     * @param pixels to convert
     *
     * @return The DIP value for the current screen of the given pixels value.
     */
    public static float pixelsToDip(Context context, int pixels) {
        DisplayMetrics dm = ResourceUtils.getResourceDisplayMetrics(context);
        return (pixels / (dm.densityDpi / 160f));
    }

    /**
     * Returns the current screen dimensions in device independent pixels (DIP) as a {@link Point} object where
     * {@link Point#x} is the screen width and {@link Point#y} is the screen height.
     *
     * @param context {@link Context}
     *
     * @return The current screen dimensions in DIP.
     */
    public static Point getScreenDimensionsInDip(Context context) {
        Point pt = null;
        if (CompatibilityUtils.isHoneycombMr2()) {
            Configuration cfg = ResourceUtils.getResourceConfiguration(context);
            pt = (cfg != null ? new Point(cfg.screenWidthDp, cfg.screenHeightDp) : null);

        } else {
            // APIs prior to v13 gave the screen dimensions in pixels. We convert them to DIPs before returning them.
            DisplayMetrics dm = ResourceUtils.getResourceDisplayMetrics(context);
            if (dm != null) {
                float widthInDip = pixelsToDip(context, dm.widthPixels);
                float heightInDip = pixelsToDip(context, dm.heightPixels);
                pt = new Point((int) widthInDip, (int) heightInDip);
            }
        }
        return pt;
    }

    /**
     * Get a boolean value indicating that device is in landscape mode.
     *
     * @param context {@link Context}
     *
     * @return [true] if the device is in landscape orientation, [false] otherwise.
     */
    public static boolean isLandscape(Context context) {
        Configuration cfg = ResourceUtils.getResourceConfiguration(context);
        return (cfg.orientation == Configuration.ORIENTATION_LANDSCAPE);
    }
    /**
     * Get a boolean value indicating that device is in portrait mode.
     *
     * @param context {@link Context}
     *
     * @return [true] if the device is in portrait orientation, [false] otherwise.
     */
    public static boolean isPortrait(Context context) {
        Configuration cfg = ResourceUtils.getResourceConfiguration(context);
        return (cfg.orientation == Configuration.ORIENTATION_PORTRAIT);
    }
    /**
     * Get a boolean value indicating that device is in square mode.
     *
     * @param context {@link Context}
     *
     * @return [true] if the device is in square orientation, [false] otherwise.
     */
    @Deprecated
    public static boolean isSquare(Context context) {
        Configuration cfg = ResourceUtils.getResourceConfiguration(context);
        return (cfg.orientation == Configuration.ORIENTATION_SQUARE);
    }

    /**
     * The size of the screen, one of 4 possible values:
     *
     * <ul>
     *     <li>http://developer.android.com/reference/android/content/res/Configuration.html#SCREENLAYOUT_SIZE_SMALL</li>
     *     <li>http://developer.android.com/reference/android/content/res/Configuration.html#SCREENLAYOUT_SIZE_NORMAL</li>
     *     <li>http://developer.android.com/reference/android/content/res/Configuration.html#SCREENLAYOUT_SIZE_LARGE</li>
     *     <li>http://developer.android.com/reference/android/content/res/Configuration.html#SCREENLAYOUT_SIZE_XLARGE</li>
     * </ul>
     *
     * See http://developer.android.com/reference/android/content/res/Configuration.html#screenLayout for more details.
     *
     * @param context {@link Context}
     *
     * @return The size of the screen
     */
    public static int getScreenSize(Context context) {
        Configuration cfg = ResourceUtils.getResourceConfiguration(context);
        return (cfg == null ? Configuration.SCREENLAYOUT_SIZE_UNDEFINED
                : (cfg.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK));
    }

    /**
     * Get a boolean value indicating that screen size is small
     *
     * @param context {@link Context}
     *
     * @return [true] if the device has a small screen, [false] otherwise.
     */
    public static boolean hasSmallScreen(Context context) {
        return (getScreenSize(context) == Configuration.SCREENLAYOUT_SIZE_SMALL);
    }

    /**
     * Get a boolean value indicating that screen size is normal
     *
     * @param context {@link Context}
     *
     * @return [true] if the device has a normal screen, [false] otherwise.
     */
    public static boolean hasNormalScreen(Context context) {
        return (getScreenSize(context) == Configuration.SCREENLAYOUT_SIZE_NORMAL);
    }

    /**
     * Get a boolean value indicating that screen size is large
     *
     * @param context {@link Context}
     *
     * @return [true] if the device has a large screen, [false] otherwise.
     */
    public static boolean hasLargeScreen(Context context) {
        return (getScreenSize(context) == Configuration.SCREENLAYOUT_SIZE_LARGE);
    }

    /**
     * Get a boolean value indicating that screen size is x-large
     *
     * @param context {@link Context}
     *
     * @return [true] if the device has an extra large screen, [false] otherwise.
     */
    public static boolean hasXLargeScreen(Context context) {
        return (getScreenSize(context) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
    }

    /**
     * Determine if the device is a tablet (i.e. it has a large screen).
     *
     * @param context The calling context.
     *
     * @return true for tablet; else false
     */
    public static boolean isTablet(Context context) {
        return (MetricUtils.getScreenSize(context) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
    }
}
