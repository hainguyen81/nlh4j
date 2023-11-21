/*
 * @(#)SystemFunctionDao.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.function.domain.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.SelectOptions;

import org.nlh4j.core.annotation.InjectRepository;
import org.nlh4j.web.core.dto.FunctionDto;
import org.nlh4j.web.system.function.controller.FunctionController;
import org.nlh4j.web.system.function.dto.FunctionSearchConditions;
import org.nlh4j.web.system.function.dto.FunctionUniqueDto;

/**
 * The repository of {@link FunctionController}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Dao
@InjectRepository
public interface SystemFunctionDao {
    /**
     * Find the {@link FunctionDto} list by the specified conditions
     *
     * @param conditions search conditions
     * @param options select options
     * @param orderBy order by
     *
     * @return the {@link FunctionDto} list or null
     */
    @Select
    public List<FunctionDto> findFunctions(FunctionSearchConditions conditions, SelectOptions options, String orderBy);
    /**
     * Find the {@link FunctionDto} by the specified unique key
     *
     * @param unique the role group unique key
     *
     * @return the {@link FunctionDto} or null
     */
    @Select
    public FunctionDto findFunction(FunctionUniqueDto unique);
    /**
     * Check the specified unique key whether is unique
     *
     * @param unique the function unique key
     *
     * @return true for unique; else false
     */
    @Select
    public boolean isUniqueConstraint(FunctionUniqueDto unique);

    /**
     * Find the {@link FunctionDto} list that excluding the specified function codes
     *
     * @param codes the excluded function codes
     *
     * @return the {@link FunctionDto} list or null
     */
    @Select
    public List<FunctionDto> findFunctionsExcluded(List<String> codes);

    /**
     * Delete function by specified function code
     *
     * @param code function code
     *
     * @return effected records
     */
    @Delete(sqlFile = true)
    int deleteFunctionBy(String code);
}
