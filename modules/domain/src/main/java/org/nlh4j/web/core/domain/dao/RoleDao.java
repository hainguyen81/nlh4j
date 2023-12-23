/*
 * @(#)RoleDao.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.domain.dao;

import java.util.List;

import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import jp.doma.dao.BaseDomaDao;

import org.nlh4j.core.annotation.InjectRepository;
import org.nlh4j.web.core.domain.entity.Role;
import org.nlh4j.web.core.dto.RoleDto;

/**
 * {@link Role} repository
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version $Revision:  $  $Date:  $
 */
@Dao
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@InjectRepository
public interface RoleDao extends BaseDomaDao<Role> {

    /**
     * Select all {@link Role}
     * @return all {@link Role} entities list
     */
    @Select
    List<Role> selectAll();

    /**
     * Select {@link Role} by identity
     *
     * @param id identity
     *
     * @return {@link Role} by identity
     */
    @Select
    Role selectById(Long id);

    /**
     * Insert the specified {@link Role}
     *
     * @param entity to insert
     *
     * @return effected records
     */
    @Insert
    int insert(Role entity);

    /**
     * Update the specified {@link Role}
     *
     * @param entity to update
     *
     * @return effected records
     */
    @Update
    int update(Role entity);

    /**
     * Delete the specified {@link Role}
     *
     * @param entity to delete
     *
     * @return effected records
     */
    @Delete
    int delete(Role entity);

    /**
     * Get {@link RoleDto} by the specified user and module link
     *
     * @param userName the user name
     *
     * @return the {@link RoleDto} by the specified user and module link
     */
    @Select
    List<RoleDto> findRoles(String userName);

    /**
     * Insert multiple {@link Role}
     *
     * @param roles {@link Role} list to insert
     *
     * @return effected records
     */
    @BatchInsert
    int[] batchInsert(List<Role> roles);
}