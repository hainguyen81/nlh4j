/*
 * @(#)ResourceUtils.java 1.0 Oct 25, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.StreamUtils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * {@link Resources} utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class ResourceUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Get resource boolean value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     * @param defVal default value if failed
     *
     * @return resource boolean value or default value
     */
    public static boolean getResourceBoolean(Context context, int resId, boolean defVal) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getBoolean(resId) : defVal);
        } catch(Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? defVal
                    : getResourceBoolean(context.getApplicationContext(), resId, defVal));
        }
    }
    /**
     * Get resource boolean value
     *
     * @param resId resource identity
     * @param defVal default value if failed
     *
     * @return resource boolean value or default value
     */
    public static boolean getResourceBoolean(int resId, boolean defVal) {
        return getResourceBoolean(null, resId, defVal);
    }
    /**
     * Get resource boolean value
     *
     * @param resId resource identity
     *
     * @return resource boolean value or false if failed
     */
    public static boolean getResourceBoolean(int resId) {
        return getResourceBoolean(null, resId, Boolean.FALSE);
    }

    /**
     * Get resource color value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     * @param defVal default value if failed
     *
     * @return resource color value or default value
     */
    public static int getResourceColor(Context context, int resId, int defVal) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getColor(resId) : defVal);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? defVal
                    : getResourceColor(context.getApplicationContext(), resId, defVal));
        }
    }
    /**
     * Get resource color value
     *
     * @param resId resource identity
     * @param defVal default value if failed
     *
     * @return resource color value or default value
     */
    public static int getResourceColor(int resId, int defVal) {
        return getResourceColor(null, resId, defVal);
    }
    /**
     * Get resource color value
     *
     * @param resId resource identity
     *
     * @return resource color value or 0 if failed
     */
    public static int getResourceColor(int resId) {
        return getResourceColor(null, resId, 0);
    }

    /**
     * Get resource color state list
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     *
     * @return resource color state list or NULL if failed
     */
    public static ColorStateList getResourceColorStateList(Context context, int resId) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getColorStateList(resId) : null);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? null
                    : getResourceColorStateList(context.getApplicationContext(), resId));
        }
    }
    /**
     * Get resource color state list
     *
     * @param resId resource identity
     *
     * @return resource color state list or NULL if failed
     */
    public static ColorStateList getResourceColorStateList(int resId) {
        return getResourceColorStateList(null, resId);
    }

    /**
     * Get resource dimension value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     * @param defVal default value if failed
     *
     * @return resource dimension value or default value
     */
    public static float getResourceDimension(Context context, int resId, float defVal) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getDimension(resId) : defVal);
        } catch(Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? defVal
                    : getResourceDimension(context.getApplicationContext(), resId, defVal));
        }
    }
    /**
     * Get resource dimension value
     *
     * @param resId resource identity
     * @param defVal default value if failed
     *
     * @return resource dimension value or default value
     */
    public static float getResourceDimension(int resId, float defVal) {
        return getResourceDimension(null, resId, defVal);
    }
    /**
     * Get resource dimension value
     *
     * @param resId resource identity
     *
     * @return resource dimension value or 0 if failed
     */
    public static float getResourceDimension(int resId) {
        return getResourceDimension(null, resId, 0f);
    }

    /**
     * Get resource dimension pixel offset value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     * @param defVal default value if failed
     *
     * @return resource dimension pixel offset value or default value
     */
    public static int getResourceDimensionPixelOffset(Context context, int resId, int defVal) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getDimensionPixelOffset(resId) : defVal);
        } catch(Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? defVal
                    : getResourceDimensionPixelOffset(context.getApplicationContext(), resId, defVal));
        }
    }
    /**
     * Get resource dimension pixel offset value
     *
     * @param resId resource identity
     * @param defVal default value if failed
     *
     * @return resource dimension pixel offset value or default value
     */
    public static int getResourceDimensionPixelOffset(int resId, int defVal) {
        return getResourceDimensionPixelOffset(null, resId, defVal);
    }
    /**
     * Get resource dimension pixel offset value
     *
     * @param resId resource identity
     *
     * @return resource dimension pixel offset value or 0 if failed
     */
    public static int getResourceDimensionPixelOffset(int resId) {
        return getResourceDimensionPixelOffset(null, resId, 0);
    }

    /**
     * Get resource dimension pixel size value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     * @param defVal default value if failed
     *
     * @return resource dimension pixel size value or default value
     */
    public static int getResourceDimensionPixelSize(Context context, int resId, int defVal) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getDimensionPixelSize(resId) : defVal);
        } catch(Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? defVal
                    : getResourceDimensionPixelSize(context.getApplicationContext(), resId, defVal));
        }
    }
    /**
     * Get resource dimension pixel size value
     *
     * @param resId resource identity
     * @param defVal default value if failed
     *
     * @return resource dimension pixel size value or default value
     */
    public static int getResourceDimensionPixelSize(int resId, int defVal) {
        return getResourceDimensionPixelSize(null, resId, defVal);
    }
    /**
     * Get resource dimension pixel size value
     *
     * @param resId resource identity
     *
     * @return resource dimension pixel size value or 0 if failed
     */
    public static int getResourceDimensionPixelSize(int resId) {
        return getResourceDimensionPixelSize(null, resId, 0);
    }

    /**
     * Get resource drawable value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     *
     * @return resource drawable value or NULL if failed
     */
    public static Drawable getResourceDrawable(Context context, int resId) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getDrawable(resId) : null);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? null
                    : getResourceDrawable(context.getApplicationContext(), resId));
        }
    }
    /**
     * Get resource drawable value
     *
     * @param resId resource identity
     *
     * @return resource drawable value or NULL if failed
     */
    public static Drawable getResourceDrawable(int resId) {
        return getResourceDrawable(null, resId);
    }

    /**
     * Get resource integer array value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     *
     * @return resource integer array value or empty array if failed
     */
    public static int[] getResourceIntArray(Context context, int resId) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getIntArray(resId) : new int[] {});
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? new int[] {}
                    : getResourceIntArray(context.getApplicationContext(), resId));
        }
    }
    /**
     * Get resource integer array value
     *
     * @param resId resource identity
     *
     * @return resource integer array value or empty array if failed
     */
    public static int[] getResourceIntArray(int resId) {
        return getResourceIntArray(null, resId);
    }

    /**
     * Get resource integer value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     * @param defVal default value if failed
     *
     * @return resource integer value or default value
     */
    public static int getResourceInteger(Context context, int resId, int defVal) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getInteger(resId) : defVal);
        } catch(Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? defVal
                    : getResourceInteger(context.getApplicationContext(), resId, defVal));
        }
    }
    /**
     * Get resource integer value
     *
     * @param resId resource identity
     * @param defVal default value if failed
     *
     * @return resource integer value or default value
     */
    public static int getResourceInteger(int resId, int defVal) {
        return getResourceInteger(null, resId, defVal);
    }
    /**
     * Get resource integer value
     *
     * @param resId resource identity
     *
     * @return resource integer value or 0 if failed
     */
    public static int getResourceInteger(int resId) {
        return getResourceInteger(null, resId, 0);
    }

    /**
     * Get resource movie value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     * @param quantity the resource quantity
     *
     * @return resource movie value or NULL if failed
     */
    public static Movie getResourceMovie(Context context, int resId) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getMovie(resId) : null);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? null
                    : getResourceMovie(context.getApplicationContext(), resId));
        }
    }
    /**
     * Get resource movie value
     *
     * @param resId resource identity
     * @param quantity the resource quantity
     *
     * @return resource movie value or NULL if failed
     */
    public static Movie getResourceMovie(int resId) {
        return getResourceMovie(null, resId);
    }

    /**
     * Get resource quantity string value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     * @param quantity the resource quantity
     * @param args resource string format arguments
     *
     * @return resource quantity string value or NULL if failed
     */
    public static String getResourceQuantityString(Context context, int resId, int quantity, Object...args) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getQuantityString(resId, quantity, args) : null);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? null
                    : getResourceQuantityString(context.getApplicationContext(), resId, quantity, args));
        }
    }
    /**
     * Get resource quantity string value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     * @param quantity the resource quantity
     *
     * @return resource quantity string value or NULL if failed
     */
    public static String getResourceQuantityString(Context context, int resId, int quantity) {
        return getResourceQuantityString(context, resId, quantity, (Object[]) null);
    }
    /**
     * Get resource quantity string value
     *
     * @param resId resource identity
     * @param quantity the resource quantity
     * @param args resource string format arguments
     *
     * @return resource quantity string value or NULL if failed
     */
    public static String getResourceQuantityString(int resId, int quantity, Object...args) {
        return getResourceQuantityString(null, resId, quantity, args);
    }
    /**
     * Get resource quantity string value
     *
     * @param resId resource identity
     * @param quantity the resource quantity
     *
     * @return resource quantity string value or NULL if failed
     */
    public static String getResourceQuantityString(int resId, int quantity) {
        return getResourceQuantityString(null, resId, quantity, (Object[]) null);
    }

    /**
     * Get resource quantity text value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     * @param quantity the resource quantity
     *
     * @return resource quantity text value or NULL if failed
     */
    public static CharSequence getResourceQuantityText(Context context, int resId, int quantity) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getQuantityText(resId, quantity) : null);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? null
                    : getResourceQuantityText(context.getApplicationContext(), resId, quantity));
        }
    }
    /**
     * Get resource quantity text value
     *
     * @param resId resource identity
     * @param quantity the resource quantity
     *
     * @return resource quantity text value or NULL if failed
     */
    public static CharSequence getResourceQuantityText(int resId, int quantity) {
        return getResourceQuantityText(null, resId, quantity);
    }

    /**
     * Get resource entry name value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     *
     * @return resource entry name value or NULL if failed
     */
    public static String getResourceEntryName(Context context, int resId) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getResourceEntryName(resId) : null);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? null
                    : getResourceEntryName(context.getApplicationContext(), resId));
        }
    }
    /**
     * Get resource entry name value
     *
     * @param resId resource identity
     *
     * @return resource entry name value or NULL if failed
     */
    public static String getResourceEntryName(int resId) {
        return getResourceEntryName(null, resId);
    }

    /**
     * Get resource name value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     *
     * @return resource name value or NULL if failed
     */
    public static String getResourceName(Context context, int resId) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getResourceName(resId) : null);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? null
                    : getResourceName(context.getApplicationContext(), resId));
        }
    }
    /**
     * Get resource name value
     *
     * @param resId resource identity
     *
     * @return resource name value or NULL if failed
     */
    public static String getResourceName(int resId) {
        return getResourceName(null, resId);
    }

    /**
     * Get resource package name value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     *
     * @return resource package name value or NULL if failed
     */
    public static String getResourcePackageName(Context context, int resId) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getResourcePackageName(resId) : null);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? null
                    : getResourcePackageName(context.getApplicationContext(), resId));
        }
    }
    /**
     * Get resource package name value
     *
     * @param resId resource identity
     *
     * @return resource package name value or NULL if failed
     */
    public static String getResourcePackageName(int resId) {
        return getResourcePackageName(null, resId);
    }

    /**
     * Get resource type name value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     *
     * @return resource type name value or NULL if failed
     */
    public static String getResourceTypeName(Context context, int resId) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getResourceTypeName(resId) : null);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? null
                    : getResourceTypeName(context.getApplicationContext(), resId));
        }
    }
    /**
     * Get resource type name value
     *
     * @param resId resource identity
     *
     * @return resource type name value or NULL if failed
     */
    public static String getResourceTypeName(int resId) {
        return getResourceTypeName(null, resId);
    }

    /**
     * Get resource string value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     * @param args resource string format arguments
     *
     * @return resource string value
     */
    public static String getResourceString(Context context, int resId, Object...args) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getString(resId, args) : null);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? null
                    : getResourceString(context.getApplicationContext(), resId, args));
        }
    }
    /**
     * Get resource string value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     *
     * @return resource string value
     */
    public static String getResourceString(Context context, int resId) {
        return getResourceString(context, resId, (Object[]) null);
    }
    /**
     * Get resource string value
     *
     * @param resId resource identity
     * @param args resource string format arguments
     *
     * @return resource string value
     */
    public static String getResourceString(int resId, Object...args) {
        return getResourceString(null, resId, args);
    }
    /**
     * Get resource string value
     *
     * @param resId resource identity
     *
     * @return resource string value
     */
    public static String getResourceString(int resId) {
        return getResourceString(null, resId, (Object[]) null);
    }
    /**
     * Get resource string value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     * @param argsFromRes resource identity format arguments
     *
     * @return resource string value
     */
    public static String getResourceString(Context context, int resId, Integer...argsFromRes) {
        List<Object> args = new LinkedList<Object>();
        if (!CollectionUtils.isEmpty(argsFromRes)) {
            for(Integer argsFromRe : argsFromRes) {
                if (argsFromRe != null && argsFromRe.intValue() > 0) {
                    args.add(getResourceString(context, argsFromRe.intValue(), (Object[]) null));
                }
            }
        }
        return getResourceString(context, resId,
                CollectionUtils.isEmpty(args) ? (Object[]) null : args.toArray(new Object[args.size()]));
    }
    /**
     * Get resource string value
     *
     * @param resId resource identity
     * @param argsFromRes resource identity format arguments
     *
     * @return resource string value
     */
    public static String getResourceString(int resId, Integer...argsFromRes) {
        return getResourceString(null, resId, argsFromRes);
    }

    /**
     * Get resource string array value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     *
     * @return resource string array value or empty array if failed
     */
    public static String[] getResourceStringArray(Context context, int resId) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getStringArray(resId) : new String[] {});
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? new String[] {}
                    : getResourceStringArray(context.getApplicationContext(), resId));
        }
    }
    /**
     * Get resource string array value
     *
     * @param resId resource identity
     *
     * @return resource string array value or empty array if failed
     */
    public static String[] getResourceStringArray(int resId) {
        return getResourceStringArray(null, resId);
    }

    /**
     * Get resource text value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     *
     * @return resource text value or NULL if failed
     */
    public static CharSequence getResourceText(Context context, int resId) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getText(resId) : null);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? null
                    : getResourceText(context.getApplicationContext(), resId));
        }
    }
    /**
     * Get resource text value
     *
     * @param resId resource identity
     *
     * @return resource text value or NULL if failed
     */
    public static CharSequence getResourceText(int resId) {
        return getResourceText(null, resId);
    }

    /**
     * Get resource text array value
     *
     * @param context to get resource. NULL for system resource
     * @param resId resource identity
     *
     * @return resource text array value or empty array if failed
     */
    public static CharSequence[] getResourceTextArray(Context context, int resId) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? res.getTextArray(resId) : null);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? new CharSequence[] {}
                    : getResourceTextArray(context.getApplicationContext(), resId));
        }
    }
    /**
     * Get resource text array value
     *
     * @param resId resource identity
     *
     * @return resource text array value or empty array if failed
     */
    public static CharSequence[] getResourceTextArray(int resId) {
        return getResourceTextArray(null, resId);
    }

    /**
     * Check the specified resource identity whether has been existed
     *
     * @param context to check. NULL for system resource
     * @param resId resource identity
     *
     * @return true for existed; else false
     */
    public static boolean isExistedResource(Context context, int resId) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        InputStream is = null;
        try {
            is = res.openRawResource(resId);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            StreamUtils.closeQuitely(is);
        }
    }
    /**
     * Check the specified resource identity whether has been existed
     *
     * @param resId resource identity
     *
     * @return true for existed; else false
     */
    public static boolean isExistedResource(int resId) {
        return isExistedResource(null, resId);
    }

    /**
     * Get the resource display metrics
     *
     * @param context to check. NULL for system resource
     *
     * @return the resource display metrics or NULL if failed
     */
    public static DisplayMetrics getResourceDisplayMetrics(Context context) {
        // get display metrics via context resources
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        DisplayMetrics dm = null;
        try {
            dm = (res == null ? null : res.getDisplayMetrics());
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            dm = (context == null ? null
                    : getResourceDisplayMetrics(context.getApplicationContext()));
        }
        // try accessing via window service
        if (dm == null) {
            WindowManager wm = ServiceUtils.getWindowManager(context);
            Display dp = (wm == null ? null : wm.getDefaultDisplay());
            if (dp != null) {
                dm = new DisplayMetrics();
                dp.getMetrics(dm);
            }
        }
        return dm;
    }

    /**
     * Get the resource configuration
     *
     * @param context to check. NULL for system resource
     *
     * @return the resource configuration or NULL if failed
     */
    public static Configuration getResourceConfiguration(Context context) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res == null ? null : res.getConfiguration());
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? null
                    : getResourceConfiguration(context.getApplicationContext()));
        }
    }
}
