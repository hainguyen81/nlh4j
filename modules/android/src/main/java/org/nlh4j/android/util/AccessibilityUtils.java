/*
 * @(#)AccessibilityUtils.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;

import org.springframework.util.Assert;

import android.content.Context;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

/**
 * Accessibility utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class AccessibilityUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Get a boolean value indicating that the accessibility service or touch exploration are enabled.
     *
     * @param context {@link Context}
     *
     * @return true if the accessibility service or touch exploration are enabled; else false
     */
    public static boolean isTouchAccessiblityEnabled(Context context) {
        AccessibilityManager am = ServiceUtils.getAccessibilityManager(context);
        boolean isAccessibilityEnabled = (am == null ? false : am.isEnabled());
        boolean isTouchExplorationEnabled = (am == null ? false : am.isTouchExplorationEnabled());
        return (isAccessibilityEnabled || isTouchExplorationEnabled);
    }

    /**
     * Announce text through the {@link AccessibilityManager} for a view.
     *
     * @param text to announce
     * @param view to announce
     *
     * @return true for successful; else false
     */
    public static boolean announceText(View view, CharSequence text) {
        Assert.notNull(view, "view");
        // if JDK >= 16
        if (CompatibilityUtils.isJellyBean()) {
            try {
                view.announceForAccessibility(text);
                return true;
            } catch(Exception e) {
                LogUtils.w(e.getMessage());
            }
        } else {
            // Only announce text if the accessibility service is enabled
            AccessibilityManager am = ServiceUtils.getAccessibilityManager(view.getContext());
            if (am == null || !am.isEnabled()) return false;
            try {
                // announce
                AccessibilityEvent ae = AccessibilityEvent.obtain(
                        AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUSED);
                ae.getText().add(text);
                ae.setEnabled(true);
                // Tie the event to the view
                ae.setClassName(view.getClass().getName());
                ae.setPackageName(view.getContext().getPackageName());
                AccessibilityEventCompat.asRecord(ae).setSource(view);

                // Send the announcement
                am.sendAccessibilityEvent(ae);
                return true;
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
            }
        }
        return false;
    }
}
