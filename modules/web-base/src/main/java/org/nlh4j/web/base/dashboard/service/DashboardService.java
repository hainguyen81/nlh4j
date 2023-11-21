/*
 * @(#)DashboardService.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.dashboard.service;

import java.util.List;

import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.service.MasterService;
import org.nlh4j.web.base.dashboard.controller.DashboardController;
import org.nlh4j.web.base.dashboard.dto.ModuleSearchConditions;
import org.nlh4j.web.core.dto.ModuleDto;

/**
 * The service interface of {@link DashboardController}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public interface DashboardService extends MasterService<ModuleDto, ModuleSearchConditions, AbstractDto> {
    /**
     * Detect parent module of the specified request URI
     *
     * @param requestURI current request URI
     * @param visibled the module visibled flag
     * @param ui specifies the modules whether is UI module or service module
     *
     * @return module information or NULL
     */
    ModuleDto selectParentModuleOf(String requestURI, Boolean visibled, Boolean ui);
    /**
     * Gets all UI modules list of the current logged-in user
     *
     * @param pid the parent module identity
     *
     * @return the root UI modules list or null
     */
    List<ModuleDto> searchUIModulesList(Long pid);
}
