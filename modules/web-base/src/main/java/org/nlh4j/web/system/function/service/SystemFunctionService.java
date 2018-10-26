/*
 * @(#)SystemFunctionService.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.function.service;

import java.util.List;

import org.nlh4j.core.service.MasterService;
import org.nlh4j.web.core.dto.FunctionDto;
import org.nlh4j.web.system.function.dto.FunctionSearchConditions;
import org.nlh4j.web.system.function.dto.FunctionUniqueDto;

/**
 * The service interface of {@link FunctionDto}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public interface SystemFunctionService extends MasterService<FunctionDto, FunctionSearchConditions, FunctionUniqueDto> {
    /**
     * Find the {@link FunctionDto} list that excluding the specified module codes
     *
     * @param codes the excluded function codes
     *
     * @return the {@link FunctionDto} list or null
     */
    public List<FunctionDto> findFunctionsExcluded(String...codes);
}
