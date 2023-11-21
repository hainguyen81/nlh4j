/*
 * @(#)SystemModuleDao.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.module.domain.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.SelectOptions;

import org.nlh4j.core.annotation.InjectRepository;
import org.nlh4j.web.core.dto.ModuleDto;
import org.nlh4j.web.system.module.controller.ModuleController;
import org.nlh4j.web.system.module.dto.ModuleSearchConditions;
import org.nlh4j.web.system.module.dto.ModuleUniqueDto;

/**
 * The repository of {@link ModuleController}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Dao
@InjectRepository
public interface SystemModuleDao {
    /**
     * Find the {@link ModuleDto} list by the specified conditions
     *
     * @param conditions search conditions
     * @param options select options
     * @param orderBy order by
     *
     * @return the {@link ModuleDto} list or null
     */
    @Select
    public List<ModuleDto> findModules(ModuleSearchConditions conditions, SelectOptions options, String orderBy);
    /**
     * Find the {@link ModuleDto} by the specified unique key
     *
     * @param unique the module unique key
     *
     * @return the {@link ModuleDto} or null
     */
    @Select
    public ModuleDto findModule(ModuleUniqueDto unique);
    /**
     * Check the specified unique key whether is unique
     *
     * @param unique the role group unique key
     *
     * @return true for unique; else false
     */
    @Select
    public boolean isUniqueConstraint(ModuleUniqueDto unique);

    /**
     * Find the {@link ModuleDto} list that excluding the specified module codes
     *
     * @param codes the excluded module codes
     *
     * @return the {@link ModuleDto} list or null
     */
    @Select
    public List<ModuleDto> findModulesExcluded(List<String> codes);
    /**
     * Check the specified module code and url whether is unique
     *
     * @param id the module identity
     * @param code the module code
     * @param url the module url
     *
     * @return true for unique; else false
     */
    @Select
    public boolean isUrlUniqueConstraint(Long id, String code, String url);

    /**
     * Remove all children modules out of the specified module code
     *
     * @param code module code
     *
     * @return effected records
     */
    @Update(sqlFile = true)
    int removeOutOf(String code);
    /**
     * Delete module by specified module code
     *
     * @param code module code
     *
     * @return effected records
     */
    @Delete(sqlFile = true)
    int deleteModuleBy(String code);
}
