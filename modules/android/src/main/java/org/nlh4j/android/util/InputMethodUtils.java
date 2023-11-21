/*
 * @(#)InputMethodUtils.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;

/**
 * {@link InputMethod} utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class InputMethodUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Hide the soft keyboard
     *
     * @param activity to check
     *
     * @return true for successful; else false
     */
    public static boolean hideSoftKeyboard(Activity activity) {
        boolean ok = true;
        if (activity != null) {
            View focused = activity.getCurrentFocus();
            if (focused != null) {
                InputMethodManager imm = ServiceUtils.getInputMethodManager(activity);
                if (imm != null) {
                    try {
                        imm.hideSoftInputFromWindow(focused.getWindowToken(), 0);
                        ok = true;
                    } catch (Exception e) {
                        LogUtils.w(e.getMessage());
                        ok = false;
                    }
                }
            }
        }
        return ok;
    }

    /**
     * Show the soft keyboard
     *
     * @param view to show
     *
     * @return true for successful; else false
     */
    public static boolean showSoftKeyboard(View view) {
        boolean ok = true;
        if (view != null && view.isFocusable()) {
            view.requestFocus();
            InputMethodManager imm = ServiceUtils.getInputMethodManager(view.getContext());
            if (imm != null) {
                try {
                    imm.showSoftInput(view, 0);
                    ok = true;
                } catch (Exception e) {
                    LogUtils.w(e.getMessage());
                    ok = false;
                }
            }
        }
        return ok;
    }
}
