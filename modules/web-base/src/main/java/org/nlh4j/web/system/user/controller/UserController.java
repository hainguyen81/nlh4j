/*
 * @(#)UserController.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.user.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.nlh4j.core.annotation.ExecutePermission;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.web.base.changepwd.dto.UserUniqueDto;
import org.nlh4j.web.base.common.Module;
import org.nlh4j.web.base.common.ModuleConst;
import org.nlh4j.web.base.common.controller.AbstractMasterController;
import org.nlh4j.web.core.domain.entity.User;
import org.nlh4j.web.system.role.dto.RoleGroupDto;
import org.nlh4j.web.system.user.dto.UserDto;
import org.nlh4j.web.system.user.dto.UserEntityParamControllerDto;
import org.nlh4j.web.system.user.dto.UserSearchConditions;
import org.nlh4j.web.system.user.dto.UserSearchParamControllerDto;
import org.nlh4j.web.system.user.service.SystemUserService;

/**
 * {@link User} controller
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
@RequestMapping("/system/user")
@ExecutePermission(value = { ModuleConst.SYSTEM_USER })
public class UserController
    extends AbstractMasterController
    <UserDto, UserSearchConditions, UserUniqueDto,
    UserSearchParamControllerDto, UserEntityParamControllerDto,
    SystemUserService, AbstractDto> {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of {@link UserController}
     */
    public UserController() {
        super(SystemUserService.class, Module.SYSTEM_USER);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.controller.MasterController#getEntityPk(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    protected UserUniqueDto getEntityPk(UserDto entity) {
        UserUniqueDto unique = null;
        if (entity != null) {
            unique = new UserUniqueDto();
            unique.setId(entity.getId());
            unique.setUsername(entity.getUsername());
        }
        return unique;
    }

    /**
     * Get all roles for assigning
     *
     * @return all roles list
     */
    @RequestMapping(value = "/roles", method = RequestMethod.POST)
    public List<RoleGroupDto> searchRoles() {
        return super.getMasterService().searchRoles();
    }
}
