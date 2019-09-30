/*
 * @(#)SystemUtils.java 1.0 Feb 19, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.SessionTrackingMode;

/**
 * System utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class SystemUtils implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    /** Logger */
    private static final String CLASS_NAME_LOG4J_FACTORY = "org.slf4j.LoggerFactory";
    private static final String METHOD_NAME_LOG4J_FACTORY = "getILoggerFactory";
    private static final String METHOD_NAME_LOG4J_FACTORY_STOP = "stop";
    /** Thread */
    private static final String CLASS_NAME_THREAD_LOCALS_MAP = "java.lang.ThreadLocal$ThreadLocalMap";
    private static final String FIELD_NAME_THREAD_LOCALS = "threadLocals";
    private static final String FIELD_NAME_THREAD_LOCALS_MAP_TABLE = "table";
    private static final String FIELD_NAME_REFERENCE_REFERENT = "referent";

    /**
     * Tracking session
     *
     * @param sc {@link ServletContext} to track
     * @param eModes tracking modes
     */
    public static void trackingSession(ServletContext sc, SessionTrackingMode...eModes) {
        if (sc != null && !CollectionUtils.isEmpty(eModes)) {
            try {
                Set<SessionTrackingMode> modes = new LinkedHashSet<SessionTrackingMode>();
                for(SessionTrackingMode mode : eModes) {
                    modes.addAll(EnumSet.of(mode));
                }
                sc.setSessionTrackingModes(modes);
            } catch (Exception e) {
                LogUtils.logError(SystemUtils.class, e.getMessage());
            }
        }
    }

    /**
	 * Un-regist database connection drivers for memory leak
	 */
	public static void unregistDrivers() {
	    try {
	    	LogUtils.logInfo(SystemUtils.class, "Unregistering database drivers");
    	    Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                try { DriverManager.deregisterDriver(driver); }
                catch (Exception e) {
                	LogUtils.logWarn(SystemUtils.class, e.getMessage());
            	}
            }
	    } catch (Exception e) {
	    	LogUtils.logError(SystemUtils.class, e.getMessage());
	    }
	}

	/**
     * Turn off logger for memory leak
     */
    public static void turnOffLogger() {
        try {
        	LogUtils.logInfo(SystemUtils.class, "Turning logger off");
        	// load logger factory class
    		Class<?> loggerFactory = null;
    		try { loggerFactory = Class.forName(CLASS_NAME_LOG4J_FACTORY); }
    		catch (Exception e1) {
    			ClassLoader loader = ClassLoader.getSystemClassLoader();
    			while(loader != null) {
	    			try { loggerFactory = Class.forName(CLASS_NAME_LOG4J_FACTORY, Boolean.FALSE, loader); }
	    			catch (Exception e2) { loggerFactory = null; }
	    			finally {
	    				if (loggerFactory != null) break;
	    				else loader = loader.getParent();
	    			}
    			}
			}
    		// if found logger factory
    		if (loggerFactory != null) {
    			Method medGetFactory = BeanUtils.getMethod(loggerFactory, METHOD_NAME_LOG4J_FACTORY, (Class<?>[]) null);
    			if (medGetFactory != null) {
    				Object ctx = BeanUtils.invokeMethod(null, medGetFactory, (Object[]) null);
    				if (ctx != null) {
    					Method medStop = BeanUtils.getMethod(ctx.getClass(), METHOD_NAME_LOG4J_FACTORY_STOP, (Class<?>[]) null);
    					BeanUtils.invokeMethod(ctx, medStop, (Object[]) null);
    				}
    			}
    		}
        } catch (Exception e) {
        	LogUtils.logError(SystemUtils.class, e.getMessage());
        }
    }

    /**
     * Clean up {@link ThreadLocal} for memory leak
     */
    public static void cleanThreadLocals() {
    	Thread thread = null;
    	Field threadLocalsField = null;
    	Object threadLocalTable = null;
    	Class<?> threadLocalMapClass = null;
    	Field tableField = null;
    	Object table = null;
    	Field referentField = null;
    	ThreadLocal<?> threadLocal = null;
    	boolean accessible = false;
        try {
        	LogUtils.logInfo(SystemUtils.class, "Clearing ThreadLocals");
            // Get a reference to the thread locals table of the current thread
            thread = Thread.currentThread();
            threadLocalsField = BeanUtils.getField(Thread.class, FIELD_NAME_THREAD_LOCALS);
            if (threadLocalsField != null) {
            	accessible = threadLocalsField.isAccessible();
            	threadLocalsField.setAccessible(true);
            	threadLocalTable = threadLocalsField.get(thread);
            	threadLocalsField.setAccessible(accessible);
            }

            // Get a reference to the array holding the thread local variables inside the
            // ThreadLocalMap of the current thread
            if (threadLocalTable != null) {
            	threadLocalMapClass = Class.forName(CLASS_NAME_THREAD_LOCALS_MAP);
            	tableField = BeanUtils.getField(threadLocalMapClass, FIELD_NAME_THREAD_LOCALS_MAP_TABLE);
            	if (tableField != null) {
            		accessible = tableField.isAccessible();
            		tableField.setAccessible(true);
            		table = tableField.get(threadLocalTable);
            		tableField.setAccessible(accessible);
            	}
            }

            // The key to the ThreadLocalMap is a WeakReference object. The referent field of this object
            // is a reference to the actual ThreadLocal variable
            if (table != null) {
	            referentField = BeanUtils.getField(Reference.class, FIELD_NAME_REFERENCE_REFERENT);
	            int size = CollectionUtils.getSize(table);
	            if (referentField != null && size > 0) {
	            	accessible = referentField.isAccessible();
		            referentField.setAccessible(true);
		            for (int i = 0; i < size; i++) {
		                // Each entry in the table array of ThreadLocalMap is an Entry object
		                // representing the thread local reference and its value
		                Object entry = CollectionUtils.get(table, i);
		                if (entry != null) {
		                    // Get a reference to the thread local object and remove it from the table
		                    threadLocal = BeanUtils.safeType(
		                    		referentField.get(entry), ThreadLocal.class);
		                    if (threadLocal != null) threadLocal.remove();
		                }
		            }
		            referentField.setAccessible(accessible);
	            }
            }
        } catch(Exception e) {
        	LogUtils.logError(SystemUtils.class, e.getMessage());
        }
    }
}
