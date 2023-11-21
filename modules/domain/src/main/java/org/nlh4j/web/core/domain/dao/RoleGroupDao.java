/*
 * @(#)RoleGroupGroupDao.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.domain.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;

import org.nlh4j.core.annotation.InjectRepository;
import org.nlh4j.web.core.domain.entity.RoleGroup;

/**
 * {@link RoleGroup} repository
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version $Revision:  $  $Date:  $
 */
@Dao
@InjectRepository
public interface RoleGroupDao {

    /**
     * Select all {@link RoleGroup}
     * @return all {@link RoleGroup} entities list
     */
    @Select
    List<RoleGroup> selectAll();

    /**
     * Select {@link RoleGroup} by identity
     *
     * @param id identity
     *
     * @return {@link RoleGroup} by identity
     */
    @Select
    RoleGroup selectById(Long id);

    /**
     * Insert the specified {@link RoleGroup}
     *
     * @param entity to insert
     *
     * @return effected records
     */
    @Insert
    int insert(RoleGroup entity);

    /**
     * Update the specified {@link RoleGroup}
     *
     * @param entity to update
     *
     * @return effected records
     */
    @Update
    int update(RoleGroup entity);

    /**
     * Delete the specified {@link RoleGroup}
     *
     * @param entity to delete
     *
     * @return effected records
     */
    @Delete
    int delete(RoleGroup entity);
}