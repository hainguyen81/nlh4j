/*
 * @(#)RoleService.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.role.service;

import java.util.List;

import org.nlh4j.core.service.MasterService;
import org.nlh4j.web.core.dto.FunctionDto;
import org.nlh4j.web.core.dto.RoleDto;
import org.nlh4j.web.system.role.dto.RoleGroupDto;
import org.nlh4j.web.system.role.dto.RoleGroupSearchConditions;
import org.nlh4j.web.system.role.dto.RoleGroupUniqueDto;

/**
 * The service interface of {@link RoleDto}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public interface SystemRoleService extends MasterService<RoleGroupDto, RoleGroupSearchConditions, RoleGroupUniqueDto> {
	/**
	 * Get the roles list that had been belonged to the specified group unique key
	 *
	 * @param unique the role group unique key
	 *
	 * @return the roles list or null
	 */
	List<RoleDto> searchRolesBy(RoleGroupUniqueDto unique);
	/**
	 * Get the roles list that has not been belonged to the specified group unique key yet
	 *
	 * @param unique the role group unique key
	 *
	 * @return the roles list or null
	 */
	List<RoleDto> searchUnRolesBy(RoleGroupUniqueDto unique);

    /**
     * Get the functions list
     *
     * @param enabled true for filtering enabled functions
     *
     * @return functions list
     */
    public List<FunctionDto> searchFunctions(Boolean enabled);
}
