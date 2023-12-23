/*
 * @(#)UserDao.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.domain.dao;

import java.util.List;

import org.nlh4j.core.annotation.InjectRepository;
import org.nlh4j.web.core.domain.entity.User;
import org.nlh4j.web.core.domain.entity.UserEx;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import jp.doma.dao.BaseDomaDao;

/**
 * {@link User} repository
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version $Revision:  $  $Date:  $
 */
@Dao
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@InjectRepository
public interface UserDao extends BaseDomaDao<User> {

    /**
     * Select all {@link User}
     * @return all {@link User} entities list
     */
    @Select
    List<User> selectAll();

    /**
     * Select {@link User} by identity (not filter by enabled profile)
     *
     * @param id identity
     *
     * @return {@link User} by identity
     */
    @Select
    User selectById(Long id);

    /**
     * Select {@link User} by identity (filter by enabled profile)
     *
     * @param id identity
     *
     * @return {@link User} by identity
     */
    @Select
    UserEx selectByEnabledId(Long id);

    /**
     * Insert the specified {@link User}
     *
     * @param entity to insert
     *
     * @return effected records
     */
    @Insert
    int insert(User entity);

    /**
     * Update the specified {@link User}
     *
     * @param entity to update
     *
     * @return effected records
     */
    @Update
    int update(User entity);

    /**
     * Delete the specified {@link User}
     *
     * @param entity to delete
     *
     * @return effected records
     */
    @Delete
    int delete(User entity);

    /**
     * Get {@link UserEx} by user name screen login permission
     *
     * @param userName user name
     *
     * @return {@link UserEx}
     */
    @Select
    UserEx findByUserName(String userName);

    /**
     * Get the onlined users list
     *
     * @return the onlined users list or null
     */
    @Select
    List<User> findOnlineUsers();

    /**
     * Update language for the specified {@link User} identity
     *
     * @param uid {@link User} identity to update
     * @param language language to update
     * @param updater the updater
     *
     * @return true for updating successful; else false
     */
    @Update(sqlFile = true)
    int updateLanguage(Long uid, String language, Long updater);
}