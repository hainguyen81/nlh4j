/*
 * @(#)ModuleDao.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.domain.dao;

import java.util.List;

import org.nlh4j.core.annotation.InjectRepository;
import org.nlh4j.web.core.domain.entity.Module;
import org.nlh4j.web.core.dto.ModuleDto;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import jp.doma.dao.BaseDomaDao;

/**
 * {@link Module} repository
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version $Revision:  $  $Date:  $
 */
@Dao
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@InjectRepository
public interface ModuleDao extends BaseDomaDao<Module> {

    /**
     * Select all {@link Module}
     * @return all {@link Module} entities list
     */
    @Select
    List<Module> selectAll();

    /**
     * Select {@link Module} by identity
     *
     * @param id identity
     *
     * @return {@link Module} by identity
     */
    @Select
    Module selectById(Long id);

    /**
     * Insert the specified {@link Module}
     *
     * @param entity to insert
     *
     * @return effected records
     */
    @Insert
    int insert(Module entity);

    /**
     * Update the specified {@link Module}
     *
     * @param entity to update
     *
     * @return effected records
     */
    @Update
    int update(Module entity);

    /**
     * Delete the specified {@link Module}
     *
     * @param entity to delete
     *
     * @return effected records
     */
    @Delete
    int delete(Module entity);

    /**
     * Select all modules by conditions
     *
     * @param userName the user name for checking role
     * @param pid the parent module identity (null for not filtering, 0 for root)
     * @param visibled the module visibled flag
     * @param ui specifies the modules whether is UI module or service module
     * @param enabled specifies the enabled/disabled modules
     * @param leaf specify the leaf modules have been filtered
     *
     * @return the modules list or null
     */
    @Select
    List<ModuleDto> findModules(String userName, Long pid, Boolean enabled, Boolean visibled, Boolean ui, Boolean leaf);

    /**
     * Select all modules by conditions
     *
     * @param userName the user name for checking role
     * @param moduleCds module codes
     * @param visibled the module visibled flag
     * @param ui specifies the modules whether is UI module or service module
     * @param enabled specifies the enabled/disabled modules
     * @param leaf specify the leaf modules have been filtered
     *
     * @return the modules list or null
     */
    @Select
    List<ModuleDto> findModulesByCode(String userName, List<String> moduleCds, Boolean enabled, Boolean visibled, Boolean ui, Boolean leaf);

    /**
     * Select all forward URL(s) by conditions
     *
     * @param userName the user name for checking role
     * @param moduleCds module codes
     *
     * @return the forward URL(s) list or null
     */
    @Select
    List<String> findModuleForwardUrls(String userName, List<String> moduleCds);

    /**
     * Get the module CSS data list
     *
     * @param moduleCds module codes
     *
     * @return the module CSS data list
     */
    @Select
    List<String> findModuleStylesheets(List<String> moduleCds);

    /**
     * Check the specified user code whether has permission to access the specified module code
     *
     * @param userName the user name
     * @param moduleCds the module codes
     * @param functions specify this module require function permissions
     *
     * @return true for having permission; else otherwise
     */
    @Select
    boolean hasPermission(String userName, List<String> moduleCds, List<String> functions);
    /**
     * Check the specified user code whether has permission to access the specified module code, request URI
     * (such as module link)
     *
     * @param userName the user name
     * @param moduleCds the module codes
     * @param requestURI such as module link (may be using regular expression base on PostgreSQL)
     * @param functions specify this module require function permissions
     *
     * @return true for having permission; else otherwise
     */
    @Select
    boolean hasPermissionOnRequest(String userName, List<String> moduleCds, String requestURI, List<String> functions);

    /**
     * Check the specified module codes whether existed or enabled all
     *
     * @param moduleCds the module codes to check
     *
     * @return true for existed and enabled; otherwise false
     */
    @Select
    boolean isEnabled(List<String> moduleCds);
    /**
     * Check the specified module codes whether existed or enabled all
     *
     * @param moduleCds the module codes to check
     * @param requestURI such as module link (may be using regular expression base on PostgreSQL)
     *
     * @return true for existed and enabled; otherwise false
     */
    @Select
    boolean isEnabledOnRequest(List<String> moduleCds, String requestURI);
    /**
     * Detect parent module of the specified request URI
     *
     * @param userName the user name for checking role
     * @param requestURI current request URI
     * @param visibled the module visibled flag
     * @param ui specifies the modules whether is UI module or service module
     *
     * @return module information or NULL
     */
    @Select
    ModuleDto findParentModuleOf(String userName, String requestURI, Boolean visibled, Boolean ui);
}