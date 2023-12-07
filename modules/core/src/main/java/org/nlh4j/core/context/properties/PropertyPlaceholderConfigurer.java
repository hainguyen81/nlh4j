/*
 * @(#)PropertyPlaceholderConfigurer.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.context.properties;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.NumberUtils;

/**
 * An extended class of {@link org.springframework.beans.factory.config.PropertyPlaceholderConfigurer}
 * to expose properties from context
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class PropertyPlaceholderConfigurer
		extends org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {

	/** properties container to expose */
	private final Map<Object, Object> exposedProperties = new LinkedHashMap<Object, Object>();

	/* (Non-Javadoc)
	 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#processProperties(org.springframework.beans.factory.config.ConfigurableListableBeanFactory, java.util.Properties)
	 */
	@Override
	protected void processProperties(
			ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		// process properties as super class
		super.processProperties(beanFactoryToProcess, props);
		// cache proeprties to expose
		if (props != null && !props.isEmpty()) {
			for(final Iterator<Object> it = props.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				this.exposedProperties.put(key, props.get(key));
			}
		}
	}

	/**
	 * Get property value
	 * @param key property key
	 * @return property value
	 */
	public Object getProperty(Object key) {
		Object propVal = null;
    	if (!CollectionUtils.isEmpty(this.exposedProperties)
    			&& this.exposedProperties.containsKey(key)) {
			propVal = this.exposedProperties.get(key);
    	}
    	return propVal;
	}

	/**
	 * Get property value under string type
	 * @param key property key
	 * @return property value under string type
	 */
	public String getPropertyString(String key) {
		return this.getPropertyString(key, "");
	}
	/**
     * Get property value under string type
     * @param key property key
     * @param defaultvalue default value in fail
     * @return property value under string type
     */
    public String getPropertyString(String key, String defaultvalue) {
    	Object propVal = this.getProperty(key);
    	return (propVal == null ? defaultvalue : propVal.toString());
	}

    /**
     * Get property value under integer type
     * @param key property key
     * @return property value under integer type
     */
    public int getPropertyInt(String key) {
		return this.getPropertyInt(key, 0);
	}
    /**
     * Get property value under integer type
     * @param key property key
     * @param defaultvalue default value in fail
     * @return property value under integer type
     */
    public int getPropertyInt(String key, int defaultvalue) {
    	Object propVal = this.getProperty(key);
    	return (propVal == null ? defaultvalue : NumberUtils.toInt(propVal, defaultvalue));
	}

    /**
     * Get property value under boolean type
     * @param key property key
     * @return property value under boolean type
     */
    public boolean getPropertyBool(String key) {
		return this.getPropertyBool(key, false);
	}
    /**
     * Get property value under boolean type
     * @param key property key
     * @param defaultvalue default value in fail
     * @return property value under boolean type
     */
    public boolean getPropertyBool(String key, boolean defaultvalue) {
    	Object propVal = this.getProperty(key);
    	return (propVal == null ? defaultvalue : NumberUtils.toBool(propVal, defaultvalue));
	}

    /**
     * Get property values array
     * @param key property key
     * @return property values array or empty
     */
    public String[] getPropertyStringArray(String key) {
    	Object propVal = this.getProperty(key);
    	return (propVal == null ? new String[] {}
    			: CollectionUtils.isArrayOf(propVal, String.class)
    			? (String[]) propVal : new String[] { propVal.toString() });
	}
}
