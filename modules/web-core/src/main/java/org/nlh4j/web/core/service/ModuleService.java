/*
 * @(#)ModuleService.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.service;

import java.util.List;
import java.util.Map;

import org.nlh4j.core.service.ExecutePermissionService;
import org.nlh4j.web.core.domain.entity.Module;
import org.nlh4j.web.core.dto.ModuleDto;

/**
 * The service interface of {@link Module}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public interface ModuleService extends ExecutePermissionService {
	/**
	 * Gets all root UI modules list
	 *
	 * @param userName the user name. NULL for current logged-in user
	 *
     * @return the root UI modules list or null
	 */
	List<ModuleDto> getTopUIModulesList(String userName);
	/**
	 * Gets all UI modules list of the specified parent
	 *
	 * @param userName the user name. NULL for current logged-in user
     * @param pid the parent module identity
     * @param leaf specify the leaf modules have been filtered
	 *
	 * @return the root UI modules list or null
	 */
	List<ModuleDto> getUIModulesList(String userName, Long pid, Boolean leaf);
	/**
	 * Check the specified user code whether has permission to access the specified module code
	 *
	 * @param userName the user name. NULL for current logged-in user
	 * @param moduleCds the module codes
     * @param functions specify this module require function permissions
	 *
	 * @return true for having permission; else otherwise
	 */
	boolean hasPermission(String userName, String[] moduleCds, String[] functions);
	/**
	 * Get the module CSS data list
	 *
	 * @param moduleCds module codes
	 *
	 * @return the module CSS data list
	 */
	List<String> getModuleStylesheets(List<String> moduleCds);
	/**
	 * Get the module forward URL(s)
	 *
	 * @param userName the user name. NULL for current logged-in user
	 * @param moduleCds the module codes
	 *
	 * @return the module forward URL(s)
	 */
	List<String> getModuleForwardUrls(String userName, String[] moduleCds);
    /**
     * Get the module functions data list
     *
     * @param moduleCds module codes
     *
     * @return the module functions data list
     */
    Map<String, List<String>> getModuleFunctions(String userName, List<String> moduleCds);

	/**
	 * Check the specified module codes whether existed or enabled all
	 *
	 * @param moduleCds the module codes to check
	 *
	 * @return true for existed and enabled; otherwise false
	 */
	boolean isEnabled(String[] moduleCds);

	/**
	 * Detect parent module of the specified request URI
	 *
	 * @param userName the user name for checking role. NULL for current logged-in user
	 * @param requestURI current request URI
     * @param visibled the module visibled flag
     * @param ui specifies the modules whether is UI module or service module
     *
	 * @return module information or NULL
	 */
	ModuleDto selectParentModuleOf(String userName, String requestURI, Boolean visibled, Boolean ui);
}
