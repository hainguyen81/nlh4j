/*
 * @(#)ChangePasswordServiceImpl.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.changepwd.service;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.pagination.PaginationSearchDto;
import org.nlh4j.core.service.AbstractService;
import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.web.base.changepwd.dto.UserDto;
import org.nlh4j.web.base.changepwd.dto.UserUniqueDto;
import org.nlh4j.web.core.service.UserService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * The implement of {@link ChangePasswordService} interface
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Service
@Transactional
@Singleton
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ChangePasswordServiceImpl extends AbstractService implements ChangePasswordService {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * {@link UserService}
     */
    @Inject
    private UserService userService;

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#findEntities(org.nlh4j.core.pagination.PaginationSearchDto)
     */
    @Override
    @Transactional
    public PaginationSearchDto<UserDto, AbstractDto> findEntities(
            PaginationSearchDto<UserDto, AbstractDto> search) {
        throw new UnsupportedOperationException();
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#findEntity(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    @Transactional
    public UserDto findEntity(UserUniqueDto unique) {
        UserDto udto = null;
        org.nlh4j.web.core.dto.UserDto sessUdto =
                AuthenticationUtils.getCurrentProfile(org.nlh4j.web.core.dto.UserDto.class);
        if (sessUdto != null
                && StringUtils.hasText(sessUdto.getUsername())
                && sessUdto.getUsername().equals(unique.getUsername())) {
            try {
                udto = BeanUtils.copyBean(sessUdto, UserDto.class);
            }
            catch (Exception e) {
                udto = null;
            }
        }
        return udto;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#isUniqueConstraint(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    @Transactional
    public boolean isUniqueConstraint(UserUniqueDto unique) {
        throw new UnsupportedOperationException();
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#saveOrUpdate(org.nlh4j.core.dto.AbstractDto, boolean)
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    public int saveOrUpdate(UserDto entity, boolean create) throws Exception {
        // only valid for updating
        if (create) return -1;
        // change password
        org.nlh4j.web.core.dto.UserDto udto =
                this.userService.changePassword(entity, entity.getNewPassword());
        // update session if necessary
        if (udto != null) AuthenticationUtils.invalidateAuthentication(udto);
        return (udto != null ? 1 : -1);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#deleteEntity(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    public int deleteEntity(UserUniqueDto unique) {
        throw new UnsupportedOperationException();
    }

}
