/*
 * @(#)ViewUtils.java 1.0 Nov 13, 2016
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;

import android.content.Context;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

/**
 * {@link View} utilities
 *
 * @author Hai Nguyen
 *
 */
public final class ViewUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Sets the passed-in {@link Drawable} parameter as a background to the
     * passed in target parameter in an SDK independent way. This
     * is the recommended way of setting background rather
     * than using native background setters provided by {@link View}
     * class. This method should NOT be used for setting background of an {@link ImageView}
     *
     * @param target to set background to.
     * @param drawable background image
     *
     * @return true for successful; else false
     */
    @SuppressWarnings("deprecation")
    public static boolean setBackground(View target, Drawable drawable) {
        boolean ok = false;
        if (target != null && drawable != null) {
            try {
                if (!CompatibilityUtils.isJellyBean()) {
                    target.setBackgroundDrawable(drawable);
                } else {
                    target.setBackground(drawable);
                }
                ok = true;
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
                ok = false;
            }
        }
        return ok;
    }

    /**
     * Tiles the background of the for a view with background resource identity.
     *
     * @param context {@link Context}
     * @param view {@link View} to apply background
     * @param bgResId background resource identity
     * @param x horizontal tile mode
     * @param y vertical tile mode
     *
     * @return true for successful; else false
     */
    public static boolean tileBackground(Context context, View view, int bgResId, TileMode x, TileMode y) {
        boolean ok = false;
        if (view != null) {
            BitmapDrawable drawable = null;
            try {
                // Tiling the background.
                x = (x == null ? TileMode.REPEAT : x);
                y = (y == null ? TileMode.REPEAT : y);
                drawable = ImageUtils.toDrawable(context, bgResId);
                if (drawable != null) drawable.setTileModeXY(x, y);

                // apply background
                ok = setBackground(view, drawable);
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
                ok = false;
            }
        }
        return ok;
    }
    /**
     * Tiles the background of the for a view with background resource identity.
     *
     * @param context {@link Context}
     * @param view {@link View} to apply background
     * @param bgResId background resource identity
     *
     * @return true for successful; else false
     */
    public static boolean tileBackground(Context context, View view, int bgResId) {
        return tileBackground(context, view, bgResId, TileMode.REPEAT, TileMode.REPEAT);
    }
}
