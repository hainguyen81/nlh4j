/*
 * @(#)UnsecureClassLoader.java 1.0 Mar 5, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.servlet;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.util.BeanUtils;

/**
 * An extended class of {@link ClassLoader} for SecurityException by
 * maven jarsigner and Spring AOP:<br>
 * FIXME This class could not apply final because jarsigner and Spring CGLIB is conflict
 * by error (this is a error of Spring AOP and maven jarsigner plugin):
 * Could not generate CGLIB subclass... Common causes of this problem include
 * using a final class or a non-visible class; nested exception is java.lang.IllegalArgumentException:
 * Cannot subclass final class...<br>
 * See http://dimafeng.com/2015/08/16/cglib/
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class UnsecureClassLoader extends ClassLoader implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;
	protected static final String DECLARE_FIELD_PACKAGE2CERTS = "package2certs";

	/** SLF4J */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Initialize a new instance of {@link UnsecureClassLoader}
	 */
	public UnsecureClassLoader() {
		this(UnsecureClassLoader.class.getClassLoader());
	}

	/**
	 * Initialize a new instance of {@link UnsecureClassLoader}
	 *
	 * @param classLoader {@link ClassLoader}
	 */
	public UnsecureClassLoader(ClassLoader classLoader) {
        super(classLoader);

        try {
            /**
             * Changing map with certificates from real one to map without elements.
             * All system's attempts to add certificates to the map do nothing.
             */
        	Field package2certs = BeanUtils.getField(
        			ClassLoader.class, DECLARE_FIELD_PACKAGE2CERTS);
        	if(package2certs != null) {
        		boolean accessible = package2certs.isAccessible();
	            package2certs.setAccessible(true);
	            package2certs.set(this,
	            		new AbstractMap<Object, Object>() {

							/* (Non-Javadoc)
							 * @see java.util.AbstractMap#entrySet()
							 */
							@Override
							public Set<Entry<Object, Object>> entrySet() {
							    return Collections.emptySet();
							}

							/* (Non-Javadoc)
							 * @see java.util.AbstractMap#put(java.lang.Object, java.lang.Object)
							 */
							@Override
							public Object put(Object key, Object value) {
								if (logger.isDebugEnabled()) {
									logger.debug(
											"Attach [" + (value == null ? "NULL" : value.toString())
											+ "] to check certificate by key ["
											+ (key == null ? "NULL" : key.toString()) + "]");
								}
							    return null;
							}
			            });
	            package2certs.setAccessible(accessible);
        	}
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
            throw new ApplicationRuntimeException(e);
        }
    }
}
