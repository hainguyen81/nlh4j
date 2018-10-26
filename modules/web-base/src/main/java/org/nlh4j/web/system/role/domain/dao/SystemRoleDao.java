/*
 * @(#)BaseExRoleDao.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.role.domain.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.jdbc.SelectOptions;

import org.nlh4j.core.annotation.InjectRepository;
import org.nlh4j.web.core.domain.entity.Role;
import org.nlh4j.web.core.dto.RoleDto;
import org.nlh4j.web.system.role.domain.entity.RoleGroup;
import org.nlh4j.web.system.role.dto.RoleGroupSearchConditions;
import org.nlh4j.web.system.role.dto.RoleGroupUniqueDto;

/**
 * The repository of {@link Role}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Dao
@InjectRepository
public interface SystemRoleDao {
    /**
     * Find the {@link RoleGroup} list by the specified conditions
     *
     * @param conditions search conditions
     * @param options select options
     * @param orderBy order by
     *
     * @return the {@link RoleGroup} list or null
     */
    @Select
    public List<RoleGroup> findGroups(RoleGroupSearchConditions conditions, SelectOptions options, String orderBy);
    /**
     * Find the {@link RoleGroup} by the specified unique key
     *
     * @param unique the role group unique key
     *
     * @return the {@link RoleGroup} or null
     */
    @Select
    public RoleGroup findGroup(RoleGroupUniqueDto unique);
    /**
     * Find the {@link RoleGroup} by the specified unique key
     *
     * @param unique the role group unique key
     *
     * @return the {@link RoleGroup} or null
     */
    @Select
    public boolean isUniqueConstraint(RoleGroupUniqueDto unique);

    /**
     * Get the roles list that has belong to the specified group unique key
     *
     * @param unique the role group unique key
     * @param enabled specify whether getting roles that its modules have been disabled/enabled. NULL or not filtering
     * @param options SELECT options
     * @param orderBy order by
     *
     * @return the roles list or null
     */
    @Select
    List<RoleDto> findRolesBy(RoleGroupUniqueDto unique, Boolean enabled, SelectOptions options, String orderBy);
    /**
     * Get the roles list that has not belonged to the specified group unique key
     *
     * @param unique the role group unique key
     * @param enabled specify whether getting roles that its modules have been disabled/enabled. NULL or not filtering
     * @param options SELECT options
     * @param orderBy order by
     *
     * @return the roles list or null
     */
    @Select
    List<RoleDto> findUnRolesBy(RoleGroupUniqueDto unique, Boolean enabled, SelectOptions options, String orderBy);

    /**
     * Deletes all children roles by specified group identity
     *
     * @param gid group identity
     *
     * @return effected records
     */
    @Delete(sqlFile = true)
    int deleteRolesBy(Long gid);
    /**
     * Deletes roles group by specified group code
     *
     * @param code group code
     *
     * @return effected records
     */
    @Delete(sqlFile = true)
    int deleteRoleGroupBy(String code);
    /**
     * Remove all users, that had been assigned into group, out of group
     *
     * @param gid the group identity
     *
     * @return effected records
     */
    @Update(sqlFile = true)
    int removeUserRoles(Long gid);
}
