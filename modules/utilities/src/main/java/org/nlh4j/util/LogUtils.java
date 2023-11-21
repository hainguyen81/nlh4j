/*
 * @(#)LogUtils.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.PrintStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;

/**
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class LogUtils implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    private static final String PACKAGE_NAME =
            SystemUtils.class.getPackage().getName().substring(
                    0, SystemUtils.class.getPackage().getName().lastIndexOf("."));
    private static final String CLASS_NAME = SystemUtils.class.getName();

    /** Log level */
    public static enum LOG_LEVEL {
    	DEBUG,
    	INFO,
    	WARN,
    	ERROR
    }

    /**
     * Detect the caller TAG
     *
     * @param clazz TAG
     *
     * @return the caller TAG or current if fail
     */
    private static String getCallerTag(Class<?> clazz) {
        String sTag = CLASS_NAME;
        boolean requiredClass = (clazz == null);
        if (!requiredClass) sTag = clazz.getSimpleName();
        StackTraceElement[] arrSte = Thread.currentThread().getStackTrace();
        StackTraceElement ste = null;
        if (arrSte != null && arrSte.length > 0) {
            boolean found = false;
            String pkgMain = PACKAGE_NAME;
            for(int i = arrSte.length - 1; i >= 0; i--) {
                ste = arrSte[i];
                if (requiredClass) {
                    try { clazz = Class.forName(ste.getClassName()); }
                    catch (ClassNotFoundException e) { clazz = null; }
                    if (clazz == null || clazz.equals(SystemUtils.class)
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
            if (!requiredClass && (!found || clazz == null)) {
            	clazz = SystemUtils.class;
            }
        }
        if (ste != null && clazz != null) {
            sTag = MessageFormat.format("{0}[{1}]", clazz.getSimpleName(), ste.getMethodName());
        }
        return sTag;
    }
    /**
     * {@link System#err}
     *
     * @param clazz caller class
     * @param msg to log
     * @param e exception
     * @param level 0 - debug; 1 - info; 2 - warn; other - error
     */
    public static void log(Class<?> clazz, String msg, Throwable e, LOG_LEVEL level) {
    	String sTag = getCallerTag(clazz);
    	Date dt = new Date();
    	// parse stream
    	level = (level == null ? LOG_LEVEL.DEBUG : level);
    	PrintStream ps = (!LOG_LEVEL.ERROR.equals(level) ? System.out : System.err);
    	String mode = (LOG_LEVEL.DEBUG.equals(level) ? "[DEBUG ]"
    			: LOG_LEVEL.INFO.equals(level) ? "[INFO ]"
    					: LOG_LEVEL.WARN.equals(level) ? "[WARN ]" : "[ERROR]");
    	// log message
    	msg = (!StringUtils.hasText(msg) && e != null ? e.getMessage() : msg);
    	if (StringUtils.hasText(msg)) {
    		ps.println(MessageFormat.format(
    				"{0} {1} {2}: {3}",
    				mode, dt.toString(), sTag, msg));
    	}
    	// log error
    	if (e != null) e.printStackTrace(ps);
    }
    /**
     * {@link System#err}
     *
     * @param clazz caller class
     * @param msg to log
     * @param e exception
     */
    public static void logError(Class<?> clazz, String msg, Throwable e) {
    	log(clazz, msg, e, LOG_LEVEL.ERROR);
    }
    /**
     * {@link System#err}
     *
     * @param msg to log
     * @param e exception
     */
    public static void logError(String msg, Throwable e) {
    	log((Class<?>) null, msg, e, LOG_LEVEL.ERROR);
    }
    /**
     * {@link System#err}
     *
     * @param clazz caller class
     * @param msg to log
     */
    public static void logError(Class<?> clazz, String msg) {
    	log(clazz, msg, (Throwable) null, LOG_LEVEL.ERROR);
    }
    /**
     * {@link System#err}
     *
     * @param msg to log
     */
    public static void logError(String msg) {
    	log((Class<?>) null, msg, (Throwable) null, LOG_LEVEL.ERROR);
    }
    /**
     * {@link System#err}
     *
     * @param clazz caller class
     * @param e exception
     */
    public static void logError(Class<?> clazz, Throwable e) {
    	log(clazz, (String) null, e, LOG_LEVEL.ERROR);
    }
    /**
     * {@link System#err}
     *
     * @param e exception
     */
    public static void logError(Throwable e) {
    	log((Class<?>) null, (String) null, e, LOG_LEVEL.ERROR);
    }
    /**
     * {@link System#out}
     *
     * @param clazz caller class
     * @param msg to log
     * @param e exception
     */
    public static void logWarn(Class<?> clazz, String msg, Throwable e) {
    	log(clazz, msg, e, LOG_LEVEL.WARN);
    }
    /**
     * {@link System#out}
     *
     * @param msg to log
     * @param e exception
     */
    public static void logWarn(String msg, Throwable e) {
    	log((Class<?>) null, msg, e, LOG_LEVEL.WARN);
    }
    /**
     * {@link System#out}
     *
     * @param clazz caller class
     * @param e exception
     */
    public static void logWarn(Class<?> clazz, Throwable e) {
    	log(clazz, (String) null, e, LOG_LEVEL.WARN);
    }
    /**
     * {@link System#out}
     *
     * @param e exception
     */
    public static void logWarn(Throwable e) {
    	log((Class<?>) null, (String) null, e, LOG_LEVEL.WARN);
    }
    /**
     * {@link System#out}
     *
     * @param clazz caller class
     * @param msg to log
     */
    public static void logWarn(Class<?> clazz, String msg) {
    	log(clazz, msg, (Throwable) null, LOG_LEVEL.WARN);
    }
    /**
     * {@link System#out}
     *
     * @param msg to log
     */
    public static void logWarn(String msg) {
    	log((Class<?>) null, msg, (Throwable) null, LOG_LEVEL.WARN);
    }
    /**
     * {@link System#out}
     *
     * @param clazz caller class
     * @param msg to log
     * @param e exception
     */
    public static void logInfo(Class<?> clazz, String msg, Throwable e) {
    	log(clazz, msg, e, LOG_LEVEL.INFO);
    }
    /**
     * {@link System#out}
     *
     * @param msg to log
     * @param e exception
     */
    public static void logInfo(String msg, Throwable e) {
    	log((Class<?>) null, msg, e, LOG_LEVEL.INFO);
    }
    /**
     * {@link System#out}
     *
     * @param clazz caller class
     * @param e exception
     */
    public static void logInfo(Class<?> clazz, Throwable e) {
    	log(clazz, (String) null, e, LOG_LEVEL.INFO);
    }
    /**
     * {@link System#out}
     *
     * @param e exception
     */
    public static void logInfo(Throwable e) {
    	log((Class<?>) null, (String) null, e, LOG_LEVEL.INFO);
    }
    /**
     * {@link System#out}
     *
     * @param clazz caller class
     * @param msg to log
     */
    public static void logInfo(Class<?> clazz, String msg) {
    	log(clazz, msg, (Throwable) null, LOG_LEVEL.INFO);
    }
    /**
     * {@link System#out}
     *
     * @param msg to log
     */
    public static void logInfo(String msg) {
    	log((Class<?>) null, msg, (Throwable) null, LOG_LEVEL.INFO);
    }
    /**
     * {@link System#out}
     *
     * @param clazz caller class
     * @param msg to log
     * @param e exception
     */
    public static void logDebug(Class<?> clazz, String msg, Throwable e) {
    	log(clazz, msg, e, LOG_LEVEL.DEBUG);
    }
    /**
     * {@link System#out}
     *
     * @param msg to log
     * @param e exception
     */
    public static void logDebug(String msg, Throwable e) {
    	log((Class<?>) null, msg, e, LOG_LEVEL.DEBUG);
    }
    /**
     * {@link System#out}
     *
     * @param clazz caller class
     * @param e exception
     */
    public static void logDebug(Class<?> clazz, Throwable e) {
    	log(clazz, (String) null, e, LOG_LEVEL.DEBUG);
    }
    /**
     * {@link System#out}
     *
     * @param e exception
     */
    public static void logDebug(Throwable e) {
    	log((Class<?>) null, (String) null, e, LOG_LEVEL.DEBUG);
    }
    /**
     * {@link System#out}
     *
     * @param clazz caller class
     * @param msg to log
     */
    public static void logDebug(Class<?> clazz, String msg) {
    	log(clazz, msg, (Throwable) null, LOG_LEVEL.DEBUG);
    }
    /**
     * {@link System#out}
     *
     * @param msg to log
     */
    public static void logDebug(String msg) {
    	log((Class<?>) null, msg, (Throwable) null, LOG_LEVEL.DEBUG);
    }
}
