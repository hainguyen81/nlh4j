/*
 * @(#)SystemUtils.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.NumberUtils;
import org.nlh4j.util.StringUtils;
import org.springframework.util.Assert;

import android.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

/**
 * System utilities
 *
 * @author Hai Nguyen
 *
 */
public final class SystemUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    private static final String PREFS_FILE = "multidex.version";

    /**
     * Get the system service from the specified {@link Context}
     *
     * @param <T> the service type
     * @param context {@link Context}
     * @param name service name
     * @param serviceClass service class to cast safety
     *
     * @return the system service
     */
    public static <T> T getSystemService(Context context, String name, Class<T> serviceClass) {
        Assert.notNull(context, "context");
        Assert.hasText(name, "service name");
        Assert.notNull(serviceClass, "service class");
        T service = BeanUtils.safeType(context.getSystemService(name), serviceClass);
        if (service == null && context.getApplicationContext() != null) {
            service = getSystemService(context.getApplicationContext(), name, serviceClass);
        }
        return service;
    }

    /**
     * Get the {@link Build#MODEL} name.
     * @return the {@link Build#MODEL} name.
     */
    public static String getModelName() {
        return Build.MODEL.toString();
    }

    /**
     * Get {@link SharedPreferences} information from {@link Context}
     *
     * @param context to parse
     *
     * @return {@link SharedPreferences} information
     */
    public static SharedPreferences getMultiDexPreferences(Context context) {
        Assert.notNull(context, "context");
        return context.getSharedPreferences(PREFS_FILE,
                Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB
                        ? Context.MODE_PRIVATE
                        : Context.MODE_PRIVATE | Context.MODE_MULTI_PROCESS);
    }

    /**
     * Get the version of the application. For e.g. 1.9.3
     *
     * @param context {@link Context}
     *
     * @return the version of the application. or NULL if failed
     */
    public static String getAppVersion(Context context) {
        String version = null;
        try {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
                if (pi != null) version = pi.versionName;
            }
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
        }
        return version;
    }

    /**
     * Get the version code of the application. For e.g. 1.9.3
     *
     * @param context {@link Context}
     *
     * @return the version code of the application. or -1 if failed
     */
    public static int getAppVersionCode(Context context) {
        int version = -1;
        try {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
                if (pi != null) version = pi.versionCode;
            }
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            version = -1;
        }
        return version;
    }

    /**
     * Get application information from {@link Context}
     *
     * @param context to parse
     * @param packName package name. NULL for default
     * @param flags information flags
     *
     * @return the application information
     */
    public static ApplicationInfo getAppInfo(Context context, String packName, int flags) {
        Assert.notNull(context, "context");
        ApplicationInfo ai = null;
        if (StringUtils.hasText(packName)) {
            PackageManager pm = context.getPackageManager();
            Assert.notNull(pm, "PackageManager");
            try {
                ai = pm.getApplicationInfo(packName, flags);
            }
            catch (Exception e) {
                LogUtils.w(e.getMessage());
                ai = null;
            }
        }
        return (ai == null ? context.getApplicationInfo() : ai);
    }
    /**
     * Get application information from {@link Context} base on {@link PackageManager#GET_META_DATA} flag
     *
     * @param context to parse
     * @param packName package name. NULL for default
     *
     * @return the application information
     */
    public static ApplicationInfo getAppInfo(Context context, String packName) {
        return getAppInfo(context, packName, PackageManager.GET_META_DATA);
    }
    /**
     * Get application information from {@link Context} base on {@link PackageManager#GET_META_DATA} flag
     *
     * @param context to parse
     *
     * @return the application information
     */
    public static ApplicationInfo getAppInfo(Context context) {
        return getAppInfo(context, null, PackageManager.GET_META_DATA);
    }

    /**
     * Get application source directory path from {@link Context}
     *
     * @param context to parse
     * @param packName package name. NULL for default
     *
     * @return the application source directory path
     */
    public static String getAppSourceDir(Context context, String packName) {
        Assert.notNull(context, "context");
        ApplicationInfo ai = getAppInfo(context, packName);
        Assert.notNull(ai, "ApplicationInfo");
        return ai.sourceDir;
    }
    /**
     * Get application source directory path from {@link Context}
     *
     * @param context to parse
     *
     * @return the application source directory path
     */
    public static String getAppSourceDir(Context context) {
        return getAppSourceDir(context, null);
    }

    /**
     * Get application UID from {@link Context}
     *
     * @param context to parse
     * @param packName package name. NULL for default
     *
     * @return the application UID
     */
    public static int getAppUid(Context context, String packName) {
        Assert.notNull(context, "context");
        ApplicationInfo ai = getAppInfo(context, packName);
        Assert.notNull(ai, "ApplicationInfo");
        return ai.uid;
    }
    /**
     * Get application UID from {@link Context}
     *
     * @param context to parse
     *
     * @return the application UID
     */
    public static int getAppUid(Context context) {
        return getAppUid(context, null);
    }

    /**
     * Get application data directory path from {@link Context}
     *
     * @param context to parse
     * @param packName package name. NULL for default
     *
     * @return the application data directory path
     */
    public static String getAppDataDir(Context context, String packName) {
        Assert.notNull(context, "context");
        ApplicationInfo ai = getAppInfo(context, packName);
        Assert.notNull(ai, "ApplicationInfo");
        return ai.dataDir;
    }
    /**
     * Get application data directory path from {@link Context}
     *
     * @param context to parse
     *
     * @return the application data directory path
     */
    public static String getAppDataDir(Context context) {
        return getAppDataDir(context, null);
    }

    /**
     * Get application icon identity from {@link Context}
     *
     * @param context to parse
     * @param packName package name. NULL for default
     *
     * @return the application icon identity
     */
    public static int getAppIcon(Context context, String packName) {
        Assert.notNull(context, "context");
        ApplicationInfo ai = getAppInfo(context, packName);
        Assert.notNull(ai, "ApplicationInfo");
        return ai.icon;
    }
    /**
     * Get application icon identity from {@link Context}
     *
     * @param context to parse
     *
     * @return the application icon identity
     */
    public static int getAppIcon(Context context) {
        return getAppIcon(context, null);
    }

    /**
     * Get application name from {@link Context}
     *
     * @param context to parse
     * @param packName package name. NULL for default
     *
     * @return the application name
     */
    public static String getAppName(Context context, String packName) {
        Assert.notNull(context, "context");
        ApplicationInfo ai = getAppInfo(context, packName);
        Assert.notNull(ai, "ApplicationInfo");
        return ai.name;
    }
    /**
     * Get application name from {@link Context}
     *
     * @param context to parse
     *
     * @return the application name
     */
    public static String getAppName(Context context) {
        return getAppName(context, null);
    }

    /**
     * Get the application icon size
     *
     * @param context to detect
     * @param defVal default value if failed
     *
     * @return the application icon size or default value if failed
     */
    public static int getAppIconSize(Context context, int defVal) {
        return (int) ResourceUtils.getResourceDimension(context, R.dimen.app_icon_size, defVal);
    }

    /**
     * Check if the application is visible on foreground to make sure it is not in sleep or termination state.
     * This method useful after come from long thread and want to check if the user still using the application now
     *
     * @param context {@link Context}
     *
     * @return true if the application is foreground, false otherwise
     */
    public static boolean isAppOnForeground(Context context) {
        boolean onForeground = false;
        ActivityManager actMan = ServiceUtils.getActivityManager(context);
        // detect via running processes
        List<RunningAppProcessInfo> appProcs = (actMan == null ? null : actMan.getRunningAppProcesses());
        if (!CollectionUtils.isEmpty(appProcs)) {
            int uid = getAppUid(context);
            String packageName = context.getPackageName();
            for (RunningAppProcessInfo appProcess : appProcs) {
                onForeground = ((appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                        && (appProcess.uid == uid
                                || appProcess.processName.equals(packageName)
                                || CollectionUtils.indexOf(appProcess.pkgList, packageName) >= 0));
                if (onForeground) break;
            }
        }
        // try to detect
        if (!onForeground && actMan != null) {
            List<RunningTaskInfo> appTasks = actMan.getRunningTasks(1);
            if (!CollectionUtils.isEmpty(appTasks)) {
                ComponentName compNm = appTasks.get(0).topActivity;
                String pkgNm = (compNm == null ? "" : compNm.getPackageName());
                onForeground = (pkgNm.indexOf(context.getPackageName()) > -1);
            }
        }
        return onForeground;
    }

    /**
     * Check if the application is visible on background to make sure it is not in sleep or termination state.
     * This method useful after come from long thread and want to check if the user still using the application now
     *
     * @param context {@link Context}
     *
     * @return true if the application is background, false otherwise
     */
    public static boolean isAppOnBackground(Context context) {
        boolean onBackground = false;
        ActivityManager actMan = ServiceUtils.getActivityManager(context);
        // detect via running processes
        List<RunningAppProcessInfo> appProcs = (actMan == null ? null : actMan.getRunningAppProcesses());
        if (!CollectionUtils.isEmpty(appProcs)) {
            int uid = getAppUid(context);
            String packageName = context.getPackageName();
            for (RunningAppProcessInfo appProcess : appProcs) {
                onBackground = (
                        (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                        && (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_VISIBLE)
                        && (appProcess.uid == uid
                                || appProcess.processName.equals(packageName)
                                || CollectionUtils.indexOf(appProcess.pkgList, packageName) >= 0));
                onBackground = (onBackground || KeyguardUtils.inKeyguardRestrictedInputMode(context));
                if (onBackground) break;
            }
        }
        return onBackground;
    }

    /**
     * Checks if application is alive.
     *
     * @param context {@link Context}
     *
     * @return true, if application is alive
     */
    public static boolean isAppAlive(Context context) {
        boolean alive = false;
        ActivityManager actMan = ServiceUtils.getActivityManager(context);
        // detect via running processes
        List<RunningAppProcessInfo> appProcs = (actMan == null ? null : actMan.getRunningAppProcesses());
        if (!CollectionUtils.isEmpty(appProcs)) {
            int uid = getAppUid(context);
            String packageName = context.getPackageName();
            for (RunningAppProcessInfo appProcess : appProcs) {
                alive = (appProcess.uid == uid
                        || appProcess.processName.equals(packageName)
                        || CollectionUtils.indexOf(appProcess.pkgList, packageName) >= 0);
                if (alive) break;
            }
        }
        // try to detect
        if (!alive && actMan != null) {
            List<RunningTaskInfo> appTasks = actMan.getRunningTasks(100);
            if (!CollectionUtils.isEmpty(appTasks)) {
                ComponentName compNm = appTasks.get(0).topActivity;
                String pkgNm = (compNm == null ? "" : compNm.getPackageName());
                alive = (pkgNm.indexOf(context.getPackageName()) > -1);
            }
        }
        return alive;
    }

    /**
     * Checks the specified {@link Activity} whether is top
     *
     * @param context {@link Context}
     * @param activity {@link Activity} class to check
     *
     * @return true, if is top activity
     */
    public static boolean isTopActivity(Context context, Class<? extends Activity> activity) {
        ActivityManager actMan = ServiceUtils.getActivityManager(context);
        List<RunningTaskInfo> appTasks = (actMan == null ? null : actMan.getRunningTasks(1));
        ComponentName compNm = (!CollectionUtils.isEmpty(appTasks) ? appTasks.get(0).topActivity : null);
        return (compNm != null && activity != null && compNm.getClassName().equals(activity.getName()));
    }

    /**
     * Checks the specified {@link Activity} whether is base
     *
     * @param context {@link Context}
     * @param activity {@link Activity} class to check
     *
     * @return true, if is base activity
     */
    public static boolean isBaseActivity(Context context, Class<? extends Activity> activity) {
        ActivityManager actMan = ServiceUtils.getActivityManager(context);
        List<RunningTaskInfo> appTasks = (actMan == null ? null : actMan.getRunningTasks(1));
        ComponentName compNm = (!CollectionUtils.isEmpty(appTasks) ? appTasks.get(0).baseActivity : null);
        return (compNm != null && activity != null && compNm.getClassName().equals(activity.getName()));
    }

    /**
     * Checks if is GPS support.
     *
     * @param context {@link Context}
     *
     * @return true, if is GPS support
     */
    public static boolean isGpsSupported(Context context) {
        PackageManager pm = (context == null ? null : context.getPackageManager());
        return (pm != null && pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS));
    }

    /**
     * Gets the battery level.
     *
     * @param context {@link Context}
     *
     * @return the battery level. -1 if failed
     */
    public static float getBatteryLevel(Context context) {
        float batLevel = -1;
        if (context != null) {
            Intent batIntent = context.registerReceiver(
                    null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int level = batIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            // Error checking that probably isn't needed but I added just in case.
            batLevel = ((level == -1 || scale == -1) ? 50.0f
                    : (((float) level / (float) scale) * 100.0f));
        }
        return batLevel;
    }

    /**
     * Gets the available internal memory size.
     *
     * @return the available internal memory size
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = (path == null ? null : new StatFs(path.getPath()));
        long blockSize = (stat == null ? 0 : stat.getBlockSize());
        long availableBlocks = (stat == null ? 0 : stat.getAvailableBlocks());
        return (availableBlocks * blockSize);
    }

    /**
     * Gets the total external memory size.
     *
     * @return the total external memory size
     */
    public static long getTotalExternalMemorySize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = (path == null ? null : new StatFs(path.getPath()));
        long blockSize = (stat == null ? 0 : stat.getBlockSize());
        long totalBlocks = (stat == null ? 0 : stat.getBlockCount());
        return (totalBlocks * blockSize);
    }

    /**
     * Gets the available external memory size.
     *
     * @return the available external memory size
     */
    public static long getAvailableExternalMemorySize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = (path == null ? null : new StatFs(path.getPath()));
        long blockSize = (stat == null ? 0 : stat.getBlockSize());
        long availableBlocks = (stat == null ? 0 : stat.getAvailableBlocks());
        return (availableBlocks * blockSize);
    }

    /**
     * Get the first occurred activity (not paused from current activity thread) by reflection
     *
     * @return the first occurred activity (not paused from current activity thread) by reflection or NULL if failed
     */
    public static Activity getActivity(){
        Class<?> activityThreadClass = null;
        Object activityThread = null;
        Method currentActivityMed = null;
        Field activitiesField = null;
        Map<Object, Object> activities = null;
        Activity activity = null;
        try {
            // get thread class
            activityThreadClass = Class.forName("android.app.ActivityThread");
            // get current activity method
            currentActivityMed = BeanUtils.getMethod(
                    activityThreadClass, "currentActivityThread", (Class<?>[]) null);
            // get thread instance
            activityThread = BeanUtils.invokeMethod(
                    null, currentActivityMed, (Object[]) null);
            // require activities map
            activitiesField = BeanUtils.getField(activityThreadClass, "mActivities");
            activities = CollectionUtils.safeMap(
                    BeanUtils.getFieldValue(activityThread, activitiesField),
                    Object.class, Object.class);
            // search activity
            if (!CollectionUtils.isEmpty(activities)) {
                for(final Iterator<Object> it = activities.keySet().iterator(); it.hasNext();) {
                    Object key = it.next();
                    Object value = activities.get(key);
                    Object valueFldVal = BeanUtils.getFieldValue(value, "paused");
                    if (!NumberUtils.toBool(valueFldVal, Boolean.FALSE)) {
                        activity = BeanUtils.safeType(value, Activity.class);
                        break;
                    }
                }
            }
        } catch(Exception e) {
            LogUtils.w(e.getMessage());
            activity = null;
        }
        return activity;
    }
}
