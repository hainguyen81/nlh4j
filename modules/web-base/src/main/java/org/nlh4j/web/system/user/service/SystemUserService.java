/*
 * @(#)SystemUserService.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.user.service;

import java.util.List;

import org.nlh4j.core.service.MasterService;
import org.nlh4j.web.base.changepwd.dto.UserUniqueDto;
import org.nlh4j.web.system.role.domain.entity.RoleGroup;
import org.nlh4j.web.system.role.dto.RoleGroupDto;
import org.nlh4j.web.system.user.dto.UserDto;
import org.nlh4j.web.system.user.dto.UserSearchConditions;

/**
 * The service interface of {@link UserDto}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public interface SystemUserService extends MasterService<UserDto, UserSearchConditions, UserUniqueDto> {
    /**
     * Get {@link RoleGroup} list
     *
     * @return the {@link RoleGroup} list or null
     */
    public List<RoleGroupDto> searchRoles();
}
