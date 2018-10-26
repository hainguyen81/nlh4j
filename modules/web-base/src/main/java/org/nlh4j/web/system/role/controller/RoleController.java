/*
 * @(#)RoleController.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.role.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import org.nlh4j.core.annotation.ExecutePermission;
import org.nlh4j.core.controller.MasterPageType;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.web.base.common.Module;
import org.nlh4j.web.base.common.ModuleConst;
import org.nlh4j.web.base.common.controller.AbstractMasterController;
import org.nlh4j.web.core.domain.entity.Role;
import org.nlh4j.web.core.domain.entity.RoleGroup;
import org.nlh4j.web.core.dto.FunctionDto;
import org.nlh4j.web.core.dto.RoleDto;
import org.nlh4j.web.system.role.dto.RoleGroupDto;
import org.nlh4j.web.system.role.dto.RoleGroupEntityParamControllerDto;
import org.nlh4j.web.system.role.dto.RoleGroupSearchConditions;
import org.nlh4j.web.system.role.dto.RoleGroupSearchParamControllerDto;
import org.nlh4j.web.system.role.dto.RoleGroupUniqueDto;
import org.nlh4j.web.system.role.service.SystemRoleService;

/**
 * {@link RoleGroup}, {@link Role} controller
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
@RequestMapping("/system/role")
@ExecutePermission(value = { ModuleConst.SYSTEM_ROLE })
public class RoleController
    extends AbstractMasterController
        <RoleGroupDto, RoleGroupSearchConditions, RoleGroupUniqueDto,
        RoleGroupSearchParamControllerDto, RoleGroupEntityParamControllerDto,
        SystemRoleService, AbstractDto> {

	/** */
	private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of {@link RoleController}
     */
    public RoleController() {
        super(SystemRoleService.class, Module.SYSTEM_ROLE);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.controller.MasterController#getEntityPk(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    protected RoleGroupUniqueDto getEntityPk(RoleGroupDto entity) {
        RoleGroupUniqueDto unique = null;
        if (entity != null) {
            unique = new RoleGroupUniqueDto();
            unique.setId(entity.getId());
            unique.setCode(entity.getCode());
        }
        return unique;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#attachModelAndView(org.springframework.web.servlet.ModelAndView, org.nlh4j.core.controller.MasterPageType)
     */
    @Override
    protected ModelAndView attachModelAndView(ModelAndView mav, MasterPageType mode) {
    	// attach master functions for detail page
    	if (!MasterPageType.HOME.equals(mode) && mav != null) {
    		mav.addObject("masterFunctions", this.getFunctions());
    	}
    	return super.attachModelAndView(mav, mode);
    }

    /**
     * Gets the roles list by the specified role group code
     *
     * @param id the role group identity
     * @param code the role group code to filter
     * @param assigned specify whether getting all roles that had been assigned into group
     *
     * @return the roles list or null
     */
    @RequestMapping(value = "/childs", method = RequestMethod.POST)
    public List<RoleDto> searchRolesList(
    		@RequestParam(value="gid", required = false) Long id,
            @RequestParam(value="gcd", required = false) String code,
            @RequestParam(value="assigned", required = false) Boolean assigned) {
        // create unique key
        RoleGroupUniqueDto unique = new RoleGroupUniqueDto();
        unique.setId(id);
        unique.setCode(code);
        // if need assigned roles list
        if (Boolean.TRUE.equals(assigned)) {
            return super.getMasterService().searchRolesBy(unique);
        }
        // else need un-assigned roles list
        else {
            return super.getMasterService().searchUnRolesBy(unique);
        }
    }

    /**
     * Get all functions list
     *
     * @return functions list or null
     */
    @RequestMapping(value = { "/functions" }, method = { RequestMethod.POST })
    public List<FunctionDto> getFunctions() {
        return super.getMasterService().searchFunctions(Boolean.TRUE);
    }
}
