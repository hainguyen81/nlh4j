/*
 * @(#)ModuleController.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.module.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.util.Assert;
import org.nlh4j.core.annotation.ExecutePermission;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.web.base.common.Module;
import org.nlh4j.web.base.common.ModuleConst;
import org.nlh4j.web.base.common.controller.AbstractMasterController;
import org.nlh4j.web.core.dto.ModuleDto;
import org.nlh4j.web.system.module.dto.ModuleEntityParamControllerDto;
import org.nlh4j.web.system.module.dto.ModuleSearchConditions;
import org.nlh4j.web.system.module.dto.ModuleSearchParamControllerDto;
import org.nlh4j.web.system.module.dto.ModuleUniqueDto;
import org.nlh4j.web.system.module.dto.ModuleUrlUniqueDto;
import org.nlh4j.web.system.module.service.SystemModuleService;

/**
 * {@link org.nlh4j.web.core.domain.entity.Module} controller
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
@RequestMapping("/system/module")
@ExecutePermission(value = { ModuleConst.SYSTEM_MODULE })
public class ModuleController extends
        AbstractMasterController
        <ModuleDto, ModuleSearchConditions, ModuleUniqueDto,
        ModuleSearchParamControllerDto,
        ModuleEntityParamControllerDto,
        SystemModuleService, AbstractDto> {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of {@link ModuleController}
     */
    public ModuleController() {
        super(SystemModuleService.class, Module.SYSTEM_MODULE);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.controller.MasterController#getEntityPk(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    protected ModuleUniqueDto getEntityPk(ModuleDto entity) {
        ModuleUniqueDto unique = null;
        if (entity != null) {
            unique = new ModuleUniqueDto();
            unique.setId(entity.getId());
            unique.setCode(entity.getCode());
        }
        return unique;
    }

    /**
     * Get all modules list excluding the specified module code
     *
     * @param code the excluded module code
     *
     * @return modules list or null
     */
    @RequestMapping(value = { "/list" }, method = { RequestMethod.POST })
    public List<ModuleDto> getModules(@RequestParam(value = "code", required = false) String code) {
        return super.getMasterService().findModulesExcluded(code);
    }
    /**
     * Check unique URL
     *
     * @param urlUniq the module URL unique
     *
     * @return true if unique; else false
     */
    @RequestMapping(value = "/checkUniqueUrl", method = RequestMethod.POST)
    public ResponseEntity<String> checkUrlUnique(@RequestBody ModuleUrlUniqueDto urlUniq) {
        Assert.notNull(urlUniq, "unique");
        Assert.hasText(urlUniq.getUrl(), "unique URL");
        return (super.getMasterService().isUrlUniqueConstraint(
        		urlUniq.getId(), urlUniq.getCode(), urlUniq.getUrl())
                ? new ResponseEntity<String>((String) null, HttpStatus.CONFLICT)
                        : new ResponseEntity<String>((String) null, HttpStatus.OK));
    }
}
