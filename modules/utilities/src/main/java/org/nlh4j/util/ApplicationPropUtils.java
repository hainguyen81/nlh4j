/*
 * @(#)ApplicationPropUtils.java 1.0 May 29, 2015 Copyright 2015 by GNU Lesser General Public License (LGPL). All
 * rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * System built-in property utilities.<br>
 * <p><u>NOTE:</u> Children classes maybe extend this class for custom more property utilities</p>
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public class ApplicationPropUtils implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
    /** Application properties file */
    public static final String APPLICATION_PROPERTIES_FILE = "app.properties";

	/**
	 * The static properties instances map
	 */
	private static transient Map<String, PropertyUtils> internalPropInstances = null;
	public enum BUILTIN_PROPERTY {
		APP,
	}

	/**
	 * Gets the <b>Instance</b> value
	 *
	 * @param classLoader class loader
	 * @param propertiesfile property file
	 * @param password specify property file whether has been encrypted
	 *
	 * @return the <b>Instance</b> value
	 */
	public static final synchronized PropertyUtils getInstance(
	        ClassLoader classLoader, String propertiesfile, String password) {
		// singleton utilities
	    if (internalPropInstances == null) {
	        internalPropInstances = new LinkedHashMap<String, PropertyUtils>();
	    }
		synchronized (internalPropInstances) {
		    // checks for initializing properties utilities
	        if (!internalPropInstances.containsKey(propertiesfile)) {
	            classLoader = (classLoader == null ? ApplicationPropUtils.class.getClassLoader() : classLoader);
	            internalPropInstances.put(propertiesfile, new PropertyUtils(classLoader, propertiesfile, password));
	        }
			return internalPropInstances.get(propertiesfile);
		}
	}
	/**
	 * Gets the system properties instance
	 *
	 * @return the system properties instance
	 */
	public static final PropertyUtils getAppInstance() {
		return getAppInstance(null);
	}
	/**
     * Gets the application properties instance (app.properties)
     *
     * @param classLoader class loader
     *
     * @return the application properties instance (app.properties)
     */
    public static final PropertyUtils getAppInstance(ClassLoader classLoader) {
        return getInstance(classLoader, APPLICATION_PROPERTIES_FILE, null);
    }

	/**
	 * Get property string value
	 * @param property property type
	 * @param key property key
	 * @return the property string value
	 */
	public static final String getString(BUILTIN_PROPERTY property, String key) {
		return getString(property, key, "");
	}
	/**
     * Get property string value
     * @param property property type
     * @param key property key
     * @param defaultvalue default value
     * @return the property string value
     */
    public static String getString(BUILTIN_PROPERTY property, String key, String defaultvalue) {
    	if (property != null) {
			switch(property) {
				// application
				case APP:
					return getAppInstance().getPropertyString(key, defaultvalue);
				default:
					return defaultvalue;
			}
    	} else {
    		return defaultvalue;
    	}
	}
    /**
     * Get property string value
     * @param propertiesFile property file ass access key
     * @param key property key
     * @param defaultvalue default value
     * @return the property string value
     */
    public static String getString(String propertiesFile, String key, String defaultvalue) {
        PropertyUtils instance = getInstance(null, propertiesFile, null);
        return (instance == null ? defaultvalue : instance.getPropertyString(key, defaultvalue));
    }

    /**
     * Get property integer value
     * @param property property type
     * @param key property key
     * @return the property integer value
     */
    public static final int getInt(BUILTIN_PROPERTY property, String key) {
		return getInt(property, key, 0);
	}
    /**
     * Get property integer value
     * @param property property type
     * @param key property key
     * @param defaultvalue default value
     * @return the property integer value
     */
    public static int getInt(BUILTIN_PROPERTY property, String key, int defaultvalue) {
    	if (property != null) {
			switch(property) {
			    // application
	            case APP:
					return getAppInstance().getPropertyInt(key, defaultvalue);
				default:
					return defaultvalue;
			}
    	} else {
    		return defaultvalue;
    	}
	}
    /**
     * Get property integer value
     * @param propertiesFile property file ass access key
     * @param key property key
     * @param defaultvalue default value
     * @return the property integer value
     */
    public static int getInt(String propertiesFile, String key, int defaultvalue) {
        PropertyUtils instance = getInstance(null, propertiesFile, null);
        return (instance == null ? defaultvalue : instance.getPropertyInt(key, defaultvalue));
    }

    /**
     * Get property boolean value
     * @param property property type
     * @param key property key
     * @return the property boolean value
     */
    public static final boolean getBool(BUILTIN_PROPERTY property, String key) {
		return getBool(property, key, false);
	}
    /**
     * Get property boolean value
     * @param property property type
     * @param key property key
     * @param defaultvalue default value
     * @return the property boolean value
     */
    public static boolean getBool(BUILTIN_PROPERTY property, String key, boolean defaultvalue) {
    	if (property != null) {
			switch(property) {
				// application
	            case APP:
	                return getAppInstance().getPropertyBool(key, defaultvalue);
				default:
					return defaultvalue;
			}
    	} else {
    		return defaultvalue;
    	}
	}
    /**
     * Get property boolean value
     * @param propertiesFile property file ass access key
     * @param key property key
     * @param defaultvalue default value
     * @return the property boolean value
     */
    public static boolean getBool(String propertiesFile, String key, boolean defaultvalue) {
        PropertyUtils instance = getInstance(null, propertiesFile, null);
        return (instance == null ? defaultvalue : instance.getPropertyBool(key, defaultvalue));
    }

    /**
     * Get property string values array
     * @param property property type
     * @param key property key
     * @return the property string values array
     */
    public static final String[] getStringArray(BUILTIN_PROPERTY property, String key) {
		switch(property) {
			// application
            case APP:
                return getAppInstance().getPropertyStringArray(key);
			default:
				return new String[] {};
		}
	}
    /**
     * Get property string values array
     * @param propertiesFile property file ass access key
     * @param key property key
     * @return the property string values array
     */
    public static final String[] getStringArray(String propertiesFile, String key) {
        PropertyUtils instance = getInstance(null, propertiesFile, null);
        return (instance == null ? new String[] {} : instance.getPropertyStringArray(key));
    }
}
