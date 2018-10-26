/*
 * @(#)ThreadLocalHolder.java 1.0 Aug 3, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.util.Objects;

import javax.inject.Singleton;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Object/Value/Context holder
 *
 * @author Hai Nguyen
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Singleton
public final class ThreadLocalHolder implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;
	private static final transient ThreadLocal<Object> holder = new ThreadLocal<Object>();

	/**
	 * Clear holder
	 */
	public static void clearHolder() {
	    holder.remove();
	}

	/**
	 * Get value from holder
	 * @return
	 */
	public static <T> T getValue(Class<T> valueClass) {
		return BeanUtils.safeType(holder.get(), valueClass);
	}

	/**
	 * Store value into holder
	 * @param value to store
	 * @param requiredNotNull specify whether checking value is not NULL
	 */
	public static <T> void setValue(T value, boolean requiredNotNull) {
		if (requiredNotNull) value = Objects.requireNonNull(value);
		holder.set((Object) value);
	}
	/**
     * Store value into holder
     * @param value to store
     */
    public static <T> void setValue(T value) {
        setValue(value, Boolean.FALSE);
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		clearHolder();
	}
}
