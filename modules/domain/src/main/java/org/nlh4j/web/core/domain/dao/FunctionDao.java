/*
 * @(#)FunctionDao.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.domain.dao;

import java.util.List;

import org.nlh4j.core.annotation.InjectRepository;
import org.nlh4j.web.core.domain.entity.Function;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import jp.doma.dao.BaseDomaDao;

/**
 * {@link Function} repository
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version $Revision:  $  $Date:  $
 */
@Dao
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@InjectRepository
public interface FunctionDao extends BaseDomaDao<Function> {

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