/*
 * @(#)LogUtils.java 1.0 Oct 8, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;

import android.util.Log;
import org.nlh4j.util.StringUtils;

/**
 * {@link Log} utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class LogUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    public static final String LOG_START = "[START] {0}.{1}";
    public static final String LOG_END_TIME = "[END] {0}.{1} [TIME] {2,number} ms";
    public static final String LOG_END = "[END] {0}.{1}";

    /**
     * Get a boolean value indicating that the class name whether is valid with package name
     *
     * @param pkgName package name
     * @param clazz class name
     *
     * @return true for valid; else false
     */
    private static boolean validClass(String pkgName, String clazz) {
        boolean valid = false;
        if (pkgName != null && clazz != null) {
            if (!pkgName.endsWith(".")) pkgName += ".";
            try {
                valid = (Class.forName(pkgName + clazz) != null);
            } catch (Exception e) {
                valid = false;
            }
        }
        return valid;
    }
    /**
     * Check the specified class name whether is in package by recursive
     *
     * @param pkgName package name
     * @param clazz class name
     *
     * @return true for in package; else false
     */
    private static boolean inPackage(String pkgName, String clazz) {
        boolean ok = false;
        if (pkgName != null && clazz != null) {
            try {
                Package[] packages = Package.getPackages();
                if (packages != null && packages.length > 0) {
                    for(Package pkg : packages) {
                        if (!pkg.getName().contains(pkgName)) continue;
                        ok = validClass(pkg.getName(), clazz);
                        if (ok) break;
                    }
                }
            } catch (Exception e) {
                ok = false;
            }
        }
        return ok;
    }
    /**
     * Get the main package name
     *
     * @return the main package name
     */
    private static String getPackageMain() {
        String pkgName = LogUtils.class.getCanonicalName();
        String[] pkgNames = pkgName.split("\\.");
        if (pkgNames.length > 3) {
            pkgNames = Arrays.copyOf(pkgNames, pkgNames.length - 3);
        }
        return StringUtils.join(pkgNames, ".");
    }
    /**
     * Get a boolean value indicating that the specified TAG whethe is loggable
     *
     * @param sTag to check
     * @param level log level
     *
     * @return true for loggable; else false
     */
    private static boolean isLoggable(String sTag, int level) {
        boolean enabled = false;
        String pkgMain = getPackageMain();
        if (inPackage(pkgMain, sTag)) {
            enabled = Log.isLoggable(pkgMain, level);
        } else if (sTag != null) {
            enabled = Log.isLoggable(sTag, level);
        }
        return enabled;
    }
    /**
     * Detect the caller TAG
     *
     * @param clazz TAG
     *
     * @return the caller TAG or current if fail
     */
    private static String getCallerTag(Class<?> clazz) {
        String sTag = LogUtils.class.getSimpleName();
        boolean requiredClass = (clazz == null);
        if (!requiredClass) sTag = clazz.getSimpleName();
        StackTraceElement[] arrSte = Thread.currentThread().getStackTrace();
        StackTraceElement ste = null;
        if (arrSte != null && arrSte.length > 0) {
            boolean found = false;
            String pkgMain = getPackageMain();
            for(int i = arrSte.length - 1; i >= 0; i--) {
                ste = arrSte[i];
                if (requiredClass) {
                    try { clazz = Class.forName(ste.getClassName()); }
                    catch (ClassNotFoundException e) { clazz = null; }
                    if (clazz == null || clazz.equals(LogUtils.class)
                            || clazz.getPackage() == null
                            || !clazz.getPackage().getName().contains(pkgMain)) continue;
                    break;
                } else {
                    try { found = clazz.equals(Class.forName(ste.getClassName())); }
                    catch (ClassNotFoundException e) { found = false; }
                    if (found) break;
                }
            }
            // try last time by current class
            if (!requiredClass && (!found || clazz == null)) clazz = LogUtils.class;
        }
        if (ste != null && clazz != null) {
            sTag = MessageFormat.format("{0}.{1}", clazz.getSimpleName(), ste.getMethodName());
            if (sTag.length() > 23) sTag = clazz.getSimpleName();
            if (sTag.length() > 23) sTag = sTag.substring(0, 23);
        }
        return sTag;
    }

    /**
     * Log DEBUG message
     *
     * @param message message to log
     */
    public static void d(String message) {
        d((Class<?>) null, message, null, (Object[]) null);
    }
    /**
     * Log DEBUG message
     *
     * @param clazz TAG
     * @param message message to log
     */
    public static void d(Class<?> clazz, String message) {
        d(clazz, message, null, (Object[]) null);
    }
    /**
     * Log DEBUG message
     *
     * @param inst TAG
     * @param message message to log
     */
    public static void d(Object inst, String message) {
        d((Class<?>) (inst == null ? null : inst.getClass()), message, null, (Object[]) null);
    }
    /**
     * Log DEBUG message
     *
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void d(String message, Object...args) {
        d((Class<?>) null, message, null, args);
    }
    /**
     * Log DEBUG message
     *
     * @param clazz TAG
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void d(Class<?> clazz, String message, Object...args) {
        d(clazz, message, null, args);
    }
    /**
     * Log DEBUG message
     *
     * @param inst TAG
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void d(Object inst, String message, Object...args) {
        d((Class<?>) (inst == null ? null : inst.getClass()), message, null, args);
    }
    /**
     * Log DEBUG message
     *
     * @param message message to log
     * @param e exception need to log
     */
    public static void d(String message, Throwable e) {
        d((Class<?>) null, message, e, (Object[]) null);
    }
    /**
     * Log DEBUG message
     *
     * @param clazz TAG
     * @param message message to log
     * @param e exception need to log
     */
    public static void d(Class<?> clazz, String message, Throwable e) {
        d(clazz, message, e, (Object[]) null);
    }
    /**
     * Log DEBUG message
     *
     * @param inst TAG
     * @param message message to log
     * @param e exception need to log
     */
    public static void d(Object inst, String message, Throwable e) {
        d((Class<?>) (inst == null ? null : inst.getClass()), message, e, (Object[]) null);
    }
    /**
     * Log DEBUG message
     *
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void d(String message, Throwable e, Object...args) {
        d((Class<?>) null, message, e, args);
    }
    /**
     * Log DEBUG message
     *
     * @param inst TAG
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void d(Object inst, String message, Throwable e, Object...args) {
        d((Class<?>) (inst == null ? null : inst.getClass()), message, e, args);
    }
    /**
     * Log DEBUG message
     *
     * @param clazz TAG
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void d(Class<?> clazz, String message, Throwable e, Object...args) {
        String sTag = getCallerTag(clazz);
        if (isLoggable(sTag, Log.DEBUG)) {
            if (e != null) {
                if (args == null || args.length <= 0) {
                    Log.d(sTag, message, e);
                } else {
                    Log.d(sTag, MessageFormat.format(message, args), e);
                }
            } else {
                if (args == null || args.length <= 0) {
                    Log.d(sTag, message);
                } else {
                    Log.d(sTag, MessageFormat.format(message, args));
                }
            }
        }
    }
    /**
     * Log DEBUG with pattern <b>[START] &lt;clazz&gt;.&lt;method&gt;</b>
     *
     * @param clazz class name
     * @param method method name
     */
    public static void debugStart(String clazz, String method) {
        d(LOG_START, new Object[] { clazz, method });
    }
    /**
     * Log DEBUG with pattern <b>[END] &lt;clazz&gt;.&lt;method&gt;</b>
     *
     * @param clazz class name
     * @param method method name
     */
    public static void debugEnd(String clazz, String method) {
        d(LOG_END, new Object[] { clazz, method });
    }
    /**
     * Log DEBUG with pattern <b>[END] &lt;clazz&gt;.&lt;method&gt; [TIME] &lt;time&gt; ms</b>
     *
     * @param clazz class name
     * @param method method name
     * @param time time in milliseconds
     */
    public static void debugEnd(String clazz, String method, long time) {
        d(LOG_END_TIME, new Object[] { clazz, method, time });
    }

    /**
     * Log INFO message
     *
     * @param message message to log
     */
    public static void i(String message) {
        i((Class<?>) null, message, null, (Object[]) null);
    }
    /**
     * Log INFO message
     *
     * @param clazz TAG
     * @param message message to log
     */
    public static void i(Class<?> clazz, String message) {
        i(clazz, message, null, (Object[]) null);
    }
    /**
     * Log INFO message
     *
     * @param inst TAG
     * @param message message to log
     */
    public static void i(Object inst, String message) {
        i((Class<?>) (inst == null ? null : inst.getClass()), message, null, (Object[]) null);
    }
    /**
     * Log INFO message
     *
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void i(String message, Object...args) {
        i((Class<?>) null, message, null, args);
    }
    /**
     * Log INFO message
     *
     * @param clazz TAG
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void i(Class<?> clazz, String message, Object...args) {
        i(clazz, message, null, args);
    }
    /**
     * Log INFO message
     *
     * @param inst TAG
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void i(Object inst, String message, Object...args) {
        i((Class<?>) (inst == null ? null : inst.getClass()), message, null, args);
    }
    /**
     * Log INFO message
     *
     * @param message message to log
     * @param e exception need to log
     */
    public static void i(String message, Throwable e) {
        i((Class<?>) null, message, e, (Object[]) null);
    }
    /**
     * Log INFO message
     *
     * @param clazz TAG
     * @param message message to log
     * @param e exception need to log
     */
    public static void i(Class<?> clazz, String message, Throwable e) {
        i(clazz, message, e, (Object[]) null);
    }
    /**
     * Log INFO message
     *
     * @param inst TAG
     * @param message message to log
     * @param e exception need to log
     */
    public static void i(Object inst, String message, Throwable e) {
        i((Class<?>) (inst == null ? null : inst.getClass()), message, e, (Object[]) null);
    }
    /**
     * Log INFO message
     *
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void i(String message, Throwable e, Object...args) {
        i((Class<?>) null, message, e, args);
    }
    /**
     * Log INFO message
     *
     * @param inst TAG
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void i(Object inst, String message, Throwable e, Object...args) {
        i((Class<?>) (inst == null ? null : inst.getClass()), message, e, args);
    }
    /**
     * Log INFO message
     *
     * @param clazz TAG
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void i(Class<?> clazz, String message, Throwable e, Object...args) {
        String sTag = getCallerTag(clazz);
        if (isLoggable(sTag, Log.INFO)) {
            if (e != null) {
                if (args == null || args.length <= 0) {
                    Log.i(sTag, message, e);
                } else {
                    Log.i(sTag, MessageFormat.format(message, args), e);
                }
            } else {
                if (args == null || args.length <= 0) {
                    Log.i(sTag, message);
                } else {
                    Log.i(sTag, MessageFormat.format(message, args));
                }
            }
        }
    }
    /**
     * Log INFO with pattern <b>[START] &lt;clazz&gt;.&lt;method&gt;</b>
     *
     * @param clazz class name
     * @param method method name
     */
    public static void infoStart(String clazz, String method) {
        i(LOG_START, new Object[] { clazz, method });
    }
    /**
     * Log INFO with pattern <b>[END] &lt;clazz&gt;.&lt;method&gt;</b>
     *
     * @param clazz class name
     * @param method method name
     */
    public static void infoEnd(String clazz, String method) {
        i(LOG_END, new Object[] { clazz, method });
    }
    /**
     * Log INFO with pattern <b>[END] &lt;clazz&gt;.&lt;method&gt; [TIME] &lt;time&gt; ms</b>
     *
     * @param clazz class name
     * @param method method name
     * @param time time in milliseconds
     */
    public static void infoEnd(String clazz, String method, long time) {
        i(LOG_END_TIME, new Object[] { clazz, method, time });
    }

    /**
     * Log ERROR message
     *
     * @param message message to log
     */
    public static void e(String message) {
        e((Class<?>) null, message, null, (Object[]) null);
    }
    /**
     * Log ERROR message
     *
     * @param clazz TAG
     * @param message message to log
     */
    public static void e(Class<?> clazz, String message) {
        e(clazz, message, null, (Object[]) null);
    }
    /**
     * Log ERROR message
     *
     * @param inst TAG
     * @param message message to log
     */
    public static void e(Object inst, String message) {
        e((Class<?>) (inst == null ? null : inst.getClass()), message, null, (Object[]) null);
    }
    /**
     * Log ERROR message
     *
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void e(String message, Object...args) {
        e((Class<?>) null, message, null, args);
    }
    /**
     * Log ERROR message
     *
     * @param clazz TAG
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void e(Class<?> clazz, String message, Object...args) {
        e(clazz, message, null, args);
    }
    /**
     * Log ERROR message
     *
     * @param inst TAG
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void e(Object inst, String message, Object...args) {
        e((Class<?>) (inst == null ? null : inst.getClass()), message, null, args);
    }
    /**
     * Log ERROR message
     *
     * @param message message to log
     * @param e exception need to log
     */
    public static void e(String message, Throwable e) {
        e((Class<?>) null, message, e, (Object[]) null);
    }
    /**
     * Log ERROR message
     *
     * @param clazz TAG
     * @param message message to log
     * @param e exception need to log
     */
    public static void e(Class<?> clazz, String message, Throwable e) {
        e(clazz, message, e, (Object[]) null);
    }
    /**
     * Log ERROR message
     *
     * @param inst TAG
     * @param message message to log
     * @param e exception need to log
     */
    public static void e(Object inst, String message, Throwable e) {
        e((Class<?>) (inst == null ? null : inst.getClass()), message, e, (Object[]) null);
    }
    /**
     * Log ERROR message
     *
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void e(String message, Throwable e, Object...args) {
        e((Class<?>) null, message, e, args);
    }
    /**
     * Log ERROR message
     *
     * @param inst TAG
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void e(Object inst, String message, Throwable e, Object...args) {
        e((Class<?>) (inst == null ? null : inst.getClass()), message, e, args);
    }
    /**
     * Log ERROR message
     *
     * @param clazz TAG
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void e(Class<?> clazz, String message, Throwable e, Object...args) {
        String sTag = getCallerTag(clazz);
        if (isLoggable(sTag, Log.ERROR)) {
            if (e != null) {
                if (args == null || args.length <= 0) {
                    Log.e(sTag, message, e);
                } else {
                    Log.e(sTag, MessageFormat.format(message, args), e);
                }
            } else {
                if (args == null || args.length <= 0) {
                    Log.e(sTag, message);
                } else {
                    Log.e(sTag, MessageFormat.format(message, args));
                }
            }
        }
    }

    /**
     * Log VERBOSE message
     *
     * @param message message to log
     */
    public static void v(String message) {
        v((Class<?>) null, message, null, (Object[]) null);
    }
    /**
     * Log VERBOSE message
     *
     * @param clazz TAG
     * @param message message to log
     */
    public static void v(Class<?> clazz, String message) {
        v(clazz, message, null, (Object[]) null);
    }
    /**
     * Log VERBOSE message
     *
     * @param inst TAG
     * @param message message to log
     */
    public static void v(Object inst, String message) {
        v((Class<?>) (inst == null ? null : inst.getClass()), message, null, (Object[]) null);
    }
    /**
     * Log VERBOSE message
     *
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void v(String message, Object...args) {
        v((Class<?>) null, message, null, args);
    }
    /**
     * Log VERBOSE message
     *
     * @param clazz TAG
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void v(Class<?> clazz, String message, Object...args) {
        v(clazz, message, null, args);
    }
    /**
     * Log VERBOSE message
     *
     * @param inst TAG
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void v(Object inst, String message, Object...args) {
        v((Class<?>) (inst == null ? null : inst.getClass()), message, null, args);
    }
    /**
     * Log VERBOSE message
     *
     * @param message message to log
     * @param e exception need to log
     */
    public static void v(String message, Throwable e) {
        v((Class<?>) null, message, e, (Object[]) null);
    }
    /**
     * Log VERBOSE message
     *
     * @param clazz TAG
     * @param message message to log
     * @param e exception need to log
     */
    public static void v(Class<?> clazz, String message, Throwable e) {
        v(clazz, message, e, (Object[]) null);
    }
    /**
     * Log VERBOSE message
     *
     * @param inst TAG
     * @param message message to log
     * @param e exception need to log
     */
    public static void v(Object inst, String message, Throwable e) {
        v((Class<?>) (inst == null ? null : inst.getClass()), message, e, (Object[]) null);
    }
    /**
     * Log VERBOSE message
     *
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void v(String message, Throwable e, Object...args) {
        v((Class<?>) null, message, e, args);
    }
    /**
     * Log VERBOSE message
     *
     * @param inst TAG
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void v(Object inst, String message, Throwable e, Object...args) {
        v((Class<?>) (inst == null ? null : inst.getClass()), message, e, args);
    }
    /**
     * Log VERBOSE message
     *
     * @param clazz TAG
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void v(Class<?> clazz, String message, Throwable e, Object...args) {
        String sTag = getCallerTag(clazz);
        if (isLoggable(sTag, Log.VERBOSE)) {
            if (e != null) {
                if (args == null || args.length <= 0) {
                    Log.v(sTag, message, e);
                } else {
                    Log.v(sTag, MessageFormat.format(message, args), e);
                }
            } else {
                if (args == null || args.length <= 0) {
                    Log.v(sTag, message);
                } else {
                    Log.v(sTag, MessageFormat.format(message, args));
                }
            }
        }
    }

    /**
     * Log WARN message
     *
     * @param message message to log
     */
    public static void w(String message) {
        w((Class<?>) null, message, null, (Object[]) null);
    }
    /**
     * Log WARN message
     *
     * @param clazz TAG
     * @param message message to log
     */
    public static void w(Class<?> clazz, String message) {
        w(clazz, message, null, (Object[]) null);
    }
    /**
     * Log WARN message
     *
     * @param inst TAG
     * @param message message to log
     */
    public static void w(Object inst, String message) {
        w((Class<?>) (inst == null ? null : inst.getClass()), message, null, (Object[]) null);
    }
    /**
     * Log WARN message
     *
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void w(String message, Object...args) {
        w((Class<?>) null, message, null, args);
    }
    /**
     * Log WARN message
     *
     * @param clazz TAG
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void w(Class<?> clazz, String message, Object...args) {
        w(clazz, message, null, args);
    }
    /**
     * Log WARN message
     *
     * @param inst TAG
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void w(Object inst, String message, Object...args) {
        w((Class<?>) (inst == null ? null : inst.getClass()), message, null, args);
    }
    /**
     * Log WARN message
     *
     * @param message message to log
     * @param e exception need to log
     */
    public static void w(String message, Throwable e) {
        w((Class<?>) null, message, e, (Object[]) null);
    }
    /**
     * Log WARN message
     *
     * @param clazz TAG
     * @param message message to log
     * @param e exception need to log
     */
    public static void w(Class<?> clazz, String message, Throwable e) {
        w(clazz, message, e, (Object[]) null);
    }
    /**
     * Log WARN message
     *
     * @param inst TAG
     * @param message message to log
     * @param e exception need to log
     */
    public static void w(Object inst, String message, Throwable e) {
        w((Class<?>) (inst == null ? null : inst.getClass()), message, e, (Object[]) null);
    }
    /**
     * Log WARN message
     *
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void w(String message, Throwable e, Object...args) {
        w((Class<?>) null, message, e, args);
    }
    /**
     * Log WARN message
     *
     * @param inst TAG
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void w(Object inst, String message, Throwable e, Object...args) {
        w((Class<?>) (inst == null ? null : inst.getClass()), message, e, args);
    }
    /**
     * Log WARN message
     *
     * @param clazz TAG
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void w(Class<?> clazz, String message, Throwable e, Object...args) {
        String sTag = getCallerTag(clazz);
        if (isLoggable(sTag, Log.WARN)) {
            if (e != null) {
                if (args == null || args.length <= 0) {
                    Log.w(sTag, message, e);
                } else {
                    Log.w(sTag, MessageFormat.format(message, args), e);
                }
            } else {
                if (args == null || args.length <= 0) {
                    Log.w(sTag, message);
                } else {
                    Log.w(sTag, MessageFormat.format(message, args));
                }
            }
        }
    }
    /**
     * Log WARN message
     *
     * @param e exception need to log
     */
    public static void w(Throwable e) {
        w((Class<?>) null, e);
    }
    /**
     * Log WARN message
     *
     * @param clazz TAG
     * @param e exception need to log
     */
    public static void w(Class<?> clazz, Throwable e) {
        String sTag = getCallerTag(clazz);
        if (isLoggable(sTag, Log.WARN)) {
            Log.w(sTag, e);
        }
    }
    /**
     * Log WARN message
     *
     * @param inst TAG
     * @param e exception need to log
     */
    public static void w(Object inst, Throwable e) {
        w((Class<?>) (inst == null ? null : inst.getClass()), e);
    }

    /**
     * Log WTF message
     *
     * @param message message to log
     */
    public static void wtf(String message) {
        wtf((Class<?>) null, message, null, (Object[]) null);
    }
    /**
     * Log WTF message
     *
     * @param clazz TAG
     * @param message message to log
     */
    public static void wtf(Class<?> clazz, String message) {
        wtf(clazz, message, null, (Object[]) null);
    }
    /**
     * Log WTF message
     *
     * @param inst TAG
     * @param message message to log
     */
    public static void wtf(Object inst, String message) {
        wtf((Class<?>) (inst == null ? null : inst.getClass()), message, null, (Object[]) null);
    }
    /**
     * Log WTF message
     *
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void wtf(String message, Object...args) {
        wtf((Class<?>) null, message, null, args);
    }
    /**
     * Log WTF message
     *
     * @param clazz TAG
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void wtf(Class<?> clazz, String message, Object...args) {
        wtf(clazz, message, null, args);
    }
    /**
     * Log WTF message
     *
     * @param inst TAG
     * @param message message to log
     * @param args message pattern arguments
     */
    public static void wtf(Object inst, String message, Object...args) {
        wtf((Class<?>) (inst == null ? null : inst.getClass()), message, null, args);
    }
    /**
     * Log WTF message
     *
     * @param message message to log
     * @param e exception need to log
     */
    public static void wtf(String message, Throwable e) {
        wtf((Class<?>) null, message, e, (Object[]) null);
    }
    /**
     * Log WTF message
     *
     * @param clazz TAG
     * @param message message to log
     * @param e exception need to log
     */
    public static void wtf(Class<?> clazz, String message, Throwable e) {
        wtf(clazz, message, e, (Object[]) null);
    }
    /**
     * Log WTF message
     *
     * @param inst TAG
     * @param message message to log
     * @param e exception need to log
     */
    public static void wtf(Object inst, String message, Throwable e) {
        wtf((Class<?>) (inst == null ? null : inst.getClass()), message, e, (Object[]) null);
    }
    /**
     * Log WTF message
     *
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void wtf(String message, Throwable e, Object...args) {
        wtf((Class<?>) null, message, e, args);
    }
    /**
     * Log WTF message
     *
     * @param inst TAG
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void wtf(Object inst, String message, Throwable e, Object...args) {
        wtf((Class<?>) (inst == null ? null : inst.getClass()), message, e, args);
    }
    /**
     * Log WTF message
     *
     * @param clazz TAG
     * @param message message to log
     * @param e exception need to log
     * @param args message pattern arguments
     */
    public static void wtf(Class<?> clazz, String message, Throwable e, Object...args) {
        String sTag = getCallerTag(clazz);
        if (isLoggable(sTag, Log.ASSERT)) {
            if (e != null) {
                if (args == null || args.length <= 0) {
                    Log.wtf(sTag, message, e);
                } else {
                    Log.wtf(sTag, MessageFormat.format(message, args), e);
                }
            } else {
                if (args == null || args.length <= 0) {
                    Log.wtf(sTag, message);
                } else {
                    Log.wtf(sTag, MessageFormat.format(message, args));
                }
            }
        }
    }
    /**
     * Log WTF message
     *
     * @param e exception need to log
     */
    public static void wtf(Throwable e) {
        wtf((Class<?>) null, e);
    }
    /**
     * Log WTF message
     *
     * @param clazz TAG
     * @param e exception need to log
     */
    public static void wtf(Class<?> clazz, Throwable e) {
        String sTag = getCallerTag(clazz);
        if (isLoggable(sTag, Log.ASSERT)) {
            Log.wtf(sTag, e);
        }
    }
    /**
     * Log WTF message
     *
     * @param inst TAG
     * @param e exception need to log
     */
    public static void wtf(Object inst, Throwable e) {
        wtf((Class<?>) (inst == null ? null : inst.getClass()), e);
    }

    public static void main(String[] args) {
        System.out.println(getPackageMain());
    }
}
