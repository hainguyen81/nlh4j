/*
 * @(#)SystemUserServiceImpl.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.user.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import org.nlh4j.core.pagination.PaginationSearchDto;
import org.nlh4j.core.service.AbstractService;
import org.nlh4j.core.service.UserService;
import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.DateUtils;
import org.nlh4j.util.EncryptUtils;
import org.nlh4j.web.base.changepwd.dto.UserUniqueDto;
import org.nlh4j.web.core.domain.dao.UserDao;
import org.nlh4j.web.core.domain.entity.User;
import org.nlh4j.web.system.role.dto.RoleGroupDto;
import org.nlh4j.web.system.role.dto.RoleGroupSearch;
import org.nlh4j.web.system.role.service.SystemRoleService;
import org.nlh4j.web.system.user.domain.dao.SystemUserDao;
import org.nlh4j.web.system.user.domain.entity.UserEx;
import org.nlh4j.web.system.user.dto.UserDto;
import org.nlh4j.web.system.user.dto.UserSearchConditions;

/**
 * The implement of {@link SystemUserService} interface
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Service
public class SystemUserServiceImpl extends AbstractService implements SystemUserService {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * {@link UserDao}
     */
    @Inject
    private UserDao userDao;
    /**
     * {@link UserService}
     */
    @Inject
    private UserService userService;
    /**
     * {@link SystemUserDao}
     */
    @Inject
    private SystemUserDao systemUserDao;
    /**
     * {@link SystemRoleService}
     */
    @Inject
    private SystemRoleService systemRoleService;

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#findEntities(org.nlh4j.core.pagination.PaginationSearchDto)
     */
    @Override
    @Transactional
    public PaginationSearchDto<UserDto, UserSearchConditions> findEntities(
            PaginationSearchDto<UserDto, UserSearchConditions> search) {
        // check system administrator condition
        org.nlh4j.web.core.dto.UserDto udto =
                AuthenticationUtils.getCurrentProfile(
                        org.nlh4j.web.core.dto.UserDto.class);
        if (udto == null || !udto.isSysadmin()) {
            // not search system administrators
            search.getSearchConditions().setSysAdmin(Boolean.FALSE);
        }
        // search users
        List<UserEx> users = this.systemUserDao.findUsers(
                search.getSearchConditions(), search.getOptions(), search.getOrderBy());
        // save search result
        try {
            search.getData().setData(BeanUtils.copyBeansList(users, UserDto.class));
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            if (!CollectionUtils.isEmpty(search.getData().getData())) {
                search.getData().getData().clear();
            }
        }
        return search;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#findEntity(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    @Transactional
    public UserDto findEntity(UserUniqueDto unique) {
        return this.systemUserDao.findUser(unique);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#isUniqueConstraint(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    @Transactional
    public boolean isUniqueConstraint(UserUniqueDto unique) {
        return this.systemUserDao.isUniqueConstraint(unique);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#saveOrUpdate(org.nlh4j.core.dto.AbstractDto, boolean)
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    public int saveOrUpdate(UserDto entity, boolean create) throws Exception {
        // if inserting
        int effected = 0;
        org.nlh4j.web.core.dto.UserDto udto =
                AuthenticationUtils.getCurrentProfile(
                        org.nlh4j.web.core.dto.UserDto.class);
        User user = null;
        if (create) {
            user = BeanUtils.copyBean(entity, User.class);
            user.setUserName(entity.getUsername());
            user.setPassword(EncryptUtils.md5(user.getPassword()));
            user.setCreatedAt(DateUtils.currentTimestamp());
            user.setCreatedUser(udto.getId());
            effected = this.userDao.insert(user);
        }
        else {
            user = this.userDao.selectById(entity.getId());
            if (!entity.isChangepwd()) entity.setPassword(user.getPassword());
            BeanUtils.copyProperties(user, entity, new String[] { "createdAt", "createdUser" });
            user.setUserName(entity.getUsername());
            if (entity.isChangepwd()) user.setPassword(EncryptUtils.md5(user.getPassword()));
            user.setUpdatedAt(DateUtils.currentTimestamp());
            user.setUpdatedUser(udto.getId());
            effected = this.userDao.update(user);
        }
        // update session
        if (udto.getId().equals(user.getId())) {
        	AuthenticationUtils.invalidateAuthentication(
        			this.userService.findProfile(udto.getId()));
        }
        return effected;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#deleteEntity(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    public int deleteEntity(UserUniqueDto unique) {
        int effected = 0;
        UserDto udto = this.findEntity(unique);
        if (udto != null) {
            // FIXME Delete physical users, and do nothing with relation data
            //		    User user = this.userDao.selectById(udto.getId());
            //		    user.setEnabled(false);
            //		    effected = this.userDao.update(user);
            User user = this.userDao.selectById(udto.getId());
            effected = this.systemUserDao.deleteUser(user.getId());
        }
        return effected;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.web.system.user.service.SystemUserService#searchRoles(java.lang.Long)
     */
    @Override
    @Transactional
    public List<RoleGroupDto> searchRoles() {
        // prepare search
        RoleGroupSearch search = new RoleGroupSearch();
        search.setUnlimit();
        // searching
        search = BeanUtils.safeType(
        		this.systemRoleService.findEntities(search),
        		RoleGroupSearch.class);
        return (search == null ? null : search.getData().getData());
    }
}
