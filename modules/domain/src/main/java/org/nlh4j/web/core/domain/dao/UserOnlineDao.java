/*
 * @(#)UserOnlineDao.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.domain.dao;

import java.util.List;

import org.nlh4j.core.annotation.InjectRepository;
import org.nlh4j.core.context.profiles.SpringProfiles;
import org.nlh4j.web.core.domain.entity.UserOnline;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import jp.doma.dao.BaseDomaDao;

/**
 * {@link UserOnline} repository
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version $Revision:  $  $Date:  $
 */
@Dao
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@InjectRepository
@Profile(value = { SpringProfiles.PROFILE_SOCKET, SpringProfiles.PROFILE_SOCKET_ONLINE, SpringProfiles.PROFILE_FULL })
public interface UserOnlineDao extends BaseDomaDao<UserOnline> {

    /**
     * Select all {@link UserOnline}
     * @return all {@link UserOnline} entities list
     */
    @Select
    List<UserOnline> selectAll();

    /**
     * Select {@link UserOnline} by identity
     *
     * @param id identity
     *
     * @return {@link UserOnline} by identity
     */
    @Select
    UserOnline selectById(Long id);

    /**
     * Insert the specified {@link UserOnline}
     *
     * @param entity to insert
     *
     * @return effected records
     */
    @Insert
    int insert(UserOnline entity);

    /**
     * Update the specified {@link UserOnline}
     *
     * @param entity to update
     *
     * @return effected records
     */
    @Update
    int update(UserOnline entity);

    /**
     * Delete the specified {@link UserOnline}
     *
     * @param entity to delete
     *
     * @return effected records
     */
    @Delete
    int delete(UserOnline entity);

    /**
     * Select user that has been logged in
     *
     * @param uid the user identity
     *
     * @return the logged-in user or null
     */
    @Select
    UserOnline findOnlineUser(Long uid);
    /**
     * Select user that has been logged in
     *
     * @param userName the user name
     *
     * @return the logged-in user or null
     */
    @Select
    UserOnline findOnlineUserByUserName(String userName);

    /**
     * Log-out the specified user identities list
     *
     * @param uids the user identities list
     *
     * @return effected records
     */
    @Update(sqlFile = true)
    int logout(List<Long> uids);
    /**
     * Log-out the specified user names list
     *
     * @param userNames the user names list
     *
     * @return effected records
     */
    @Update(sqlFile = true)
    int logoutByUserNames(List<String> userNames);
}