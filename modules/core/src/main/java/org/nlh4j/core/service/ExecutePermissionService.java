/*
 * @(#)ExecutePermissionService.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.service;

import org.nlh4j.core.dto.UserDetails;

/**
 * Module service interface
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public interface ExecutePermissionService {
	/**
	 * Check the specified user code whether has permission to access the specified module code, request URI
	 * (such as module link)
	 *
	 * @param user the user
	 * @param moduleCds the module codes
	 * @param requestURI such as module link (may be using regular expression base on PostgreSQL)
     * @param functions specify this module require function permissions
	 *
	 * @return true for having permission; else otherwise
	 */
	boolean hasPermissionOnRequest(UserDetails user, String[] moduleCds, String requestURI, String[] functions);
	/**
	 * Check the specified module codes whether existed or enabled all
	 *
	 * @param moduleCds the module codes to check
	 * @param requestURI the request URI to check
	 *
	 * @return true for existed and enabled; otherwise false
	 */
	boolean isEnabledOnRequest(String[] moduleCds, String requestURI);
}
