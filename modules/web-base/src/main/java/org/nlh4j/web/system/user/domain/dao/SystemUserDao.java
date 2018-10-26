/*
 * @(#)SystemUserDao.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.user.domain.dao;

import java.util.List;

import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.SelectOptions;

import org.nlh4j.core.annotation.InjectRepository;
import org.nlh4j.web.base.changepwd.dto.UserUniqueDto;
import org.nlh4j.web.system.user.controller.UserController;
import org.nlh4j.web.system.user.domain.entity.UserEx;
import org.nlh4j.web.system.user.dto.UserDto;
import org.nlh4j.web.system.user.dto.UserSearchConditions;

/**
 * The repository of {@link UserController}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Dao
@InjectRepository
public interface SystemUserDao {
    /**
     * Find the {@link UserDto} data list by the specified conditions
     *
     * @param conditions the search conditions
     * @param options the LIMIT options
     * @param orderBy the ORDER BY
     *
     * @return the {@link UserDto} data list or null
     */
    @Select
    List<UserEx> findUsers(UserSearchConditions conditions, SelectOptions options, String orderBy);
    /**
     * Find the {@link UserDto} data by the specified unique key
     *
     * @param unique the {@link UserDto} data unique key
     *
     * @return the {@link UserDto} data or null
     */
    @Select
    UserDto findUser(UserUniqueDto unique);
    /**
     * Check the specified {@link UserDto} data whether is unique constraint
     *
     * @param unique the {@link UserDto} data unique key
     *
     * @return true for unique; else false
     */
    @Select
    boolean isUniqueConstraint(UserUniqueDto unique);
    /**
     * Delete the specified user physically
     * 
     * @param uid user identity
     * 
     * @return effected records
     */
    @Select
    int deleteUser(Long uid);
}
