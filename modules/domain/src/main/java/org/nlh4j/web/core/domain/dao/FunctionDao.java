/*
 * @(#)FunctionDao.java 1.0 Jun 1, 2015
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

import org.nlh4j.core.annotation.InjectRepository;
import org.nlh4j.web.core.domain.entity.Function;

/**
 * {@link Function} repository
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version $Revision:  $  $Date:  $
 */
@Dao
@InjectRepository
public interface FunctionDao {

    /**
     * Select all {@link Function}
     * @return all {@link Function} entities list
     */
    @Select
    List<Function> selectAll();

    /**
     * Select {@link Function} by identity
     *
     * @param id identity
     *
     * @return {@link Function} by identity
     */
    @Select
    Function selectById(Long id);

    /**
     * Insert the specified {@link Function}
     *
     * @param entity to insert
     *
     * @return effected records
     */
    @Insert
    int insert(Function entity);

    /**
     * Update the specified {@link Function}
     *
     * @param entity to update
     *
     * @return effected records
     */
    @Update
    int update(Function entity);

    /**
     * Delete the specified {@link Function}
     *
     * @param entity to delete
     *
     * @return effected records
     */
    @Delete
    int delete(Function entity);

    /**
     * Insert multiple {@link Function}
     *
     * @param roles {@link Function} list to insert
     *
     * @return effected records
     */
    @BatchInsert
    int[] batchInsert(List<Function> roles);
}