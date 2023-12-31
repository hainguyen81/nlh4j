/*
 * @(#)ControllerInterceptorAdapterService.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.intercepter;

import java.io.Serializable;

/**
 * Service interface for AspectJ point-cut controller
 */
public interface ControllerInterceptorAdapterService extends Serializable {

	/**
	 * Check conditions to allow invoking method
	 * TODO Override for checking or default is true
	 *
	 * @param targetClass the target class invocation
	 * @param method the method name
	 * @param medArgs the method arguments
	 *
	 * @return true for allowing
	 */
	default boolean checkInvoke(Class<?> targetClass, String method, Object[] medArgs) {
		return true;
	}
	
	/**
	 * Require forward URL
	 * TODO Override for requiring forward URL
	 *
	 * @param targetClass the target class invocation
	 * @param method the method name
	 * @param medArgs the method arguments
	 *
	 * @return valid forward URL or NULL/empty to continue controller handling method
	 */
	default String checkForward(Class<?> targetClass, String method, Object[] medArgs) {
		return null;
	}

	/**
	 * Do something after invoking method
	 * TODO Override for doing something
	 *
	 * @param targetClass the target class invocation
	 * @param method the method name
	 * @param medArgs the method arguments
	 * @param value the result of method invoking
	 */
	default void afterInvoke(Class<?> targetClass, String method, Object[] medArgs, Object value) {
		// do nothing
	}
}
