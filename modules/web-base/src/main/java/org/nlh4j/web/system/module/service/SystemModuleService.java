/*
 * @(#)SystemModuleService.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.module.service;

import java.util.List;

import org.nlh4j.core.service.MasterService;
import org.nlh4j.web.core.dto.ModuleDto;
import org.nlh4j.web.system.module.dto.ModuleSearchConditions;
import org.nlh4j.web.system.module.dto.ModuleUniqueDto;

/**
 * The service interface of {@link ModuleDto}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public interface SystemModuleService extends MasterService<ModuleDto, ModuleSearchConditions, ModuleUniqueDto> {
    /**
     * Find the {@link ModuleDto} list that excluding the specified module codes
     *
     * @param codes the excluded module codes
     *
     * @return the {@link ModuleDto} list or null
     */
    public List<ModuleDto> findModulesExcluded(String...codes);
    /**
     * Check the specified module code and url whether is unique
     *
     * @param id the module identity
     * @param code the module code
     * @param url the module url
     *
     * @return true for unique; else false
     */
    public boolean isUrlUniqueConstraint(Long id, String code, String url);
}
