/*
 * @(#)FunctionController.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.function.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.nlh4j.core.annotation.ExecutePermission;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.web.base.common.Module;
import org.nlh4j.web.base.common.ModuleConst;
import org.nlh4j.web.base.common.controller.AbstractMasterController;
import org.nlh4j.web.core.dto.FunctionDto;
import org.nlh4j.web.system.function.dto.FunctionEntityParamControllerDto;
import org.nlh4j.web.system.function.dto.FunctionSearchConditions;
import org.nlh4j.web.system.function.dto.FunctionSearchParamControllerDto;
import org.nlh4j.web.system.function.dto.FunctionUniqueDto;
import org.nlh4j.web.system.function.service.SystemFunctionService;

/**
 * {@link org.nlh4j.web.core.domain.entity.Function} controller
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
@RequestMapping("/system/function")
@ExecutePermission(value = { ModuleConst.SYSTEM_FUNCTION })
public class FunctionController extends
        AbstractMasterController
        <FunctionDto, FunctionSearchConditions, FunctionUniqueDto,
        FunctionSearchParamControllerDto,
        FunctionEntityParamControllerDto,
        SystemFunctionService, AbstractDto> {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of {@link FunctionController}
     */
    public FunctionController() {
        super(SystemFunctionService.class, Module.SYSTEM_FUNCTION);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.controller.MasterController#getEntityPk(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    protected FunctionUniqueDto getEntityPk(FunctionDto entity) {
        FunctionUniqueDto unique = null;
        if (entity != null) {
            unique = new FunctionUniqueDto();
            unique.setId(entity.getId());
            unique.setCode(entity.getCode());
        }
        return unique;
    }

    /**
     * Get all functions list excluding the specified function code
     *
     * @param code the excluded function code
     *
     * @return functions list or null
     */
    @RequestMapping(value = { "/list" }, method = { RequestMethod.POST })
    public List<FunctionDto> getFunctions(@RequestParam(value = "code", required = false) String code) {
        return super.getMasterService().findFunctionsExcluded(code);
    }
}
