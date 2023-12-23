/*
 * @(#)DashboardServiceImpl.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.dashboard.service;

import java.util.List;

import javax.inject.Inject;

import org.nlh4j.core.annotation.InjectTransactionalService;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.dto.UserDetails;
import org.nlh4j.core.pagination.PaginationSearchDto;
import org.nlh4j.core.service.AbstractService;
import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.web.base.dashboard.dto.ModulePagination;
import org.nlh4j.web.base.dashboard.dto.ModuleSearchConditions;
import org.nlh4j.web.core.dto.ModuleDto;
import org.nlh4j.web.core.service.ModuleService;
import org.springframework.transaction.annotation.Transactional;

/**
 * The implement of {@link DashboardService} interface
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@InjectTransactionalService
public class DashboardServiceImpl extends AbstractService implements DashboardService {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * {@link ModuleService}
     */
    @Inject
    private ModuleService moduleService;

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#findEntities(org.nlh4j.core.pagination.PaginationSearchDto)
     */
    @Override
    @Transactional
    public PaginationSearchDto<ModuleDto, ModuleSearchConditions> findEntities(
            PaginationSearchDto<ModuleDto, ModuleSearchConditions> search) {
        // detect current login user
        String userName = search.getSearchConditions().getUsername();
        // detect module search condition
        Long pid = search.getSearchConditions().getMid();
        pid = (pid == null ? 0L : pid.longValue());
        // search modules list
        ModulePagination data = new ModulePagination();
        data.setData(this.moduleService.getUIModulesList(userName, pid, null));
        search.setData(data);
        return search;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#findEntity(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    @Deprecated
    @Transactional
    public ModuleDto findEntity(AbstractDto unique) {
        throw new UnsupportedOperationException();
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#isUniqueConstraint(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    @Deprecated
    @Transactional
    public boolean isUniqueConstraint(AbstractDto unique) {
        throw new UnsupportedOperationException();
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#saveOrUpdate(org.nlh4j.core.dto.AbstractDto, boolean)
     */
    @Override
    @Deprecated
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    public int saveOrUpdate(ModuleDto entity, boolean create) throws Exception {
        throw new UnsupportedOperationException();
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#deleteEntity(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    @Deprecated
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    public int deleteEntity(AbstractDto unique) {
        throw new UnsupportedOperationException();
    }

    /*
     * (Non-Javadoc)
     * @see org.nlh4j.web.base.dashboard.service.DashboardService#selectParentModuleOf(java.lang.String, java.lang.Boolean, java.lang.Boolean)
     */
    @Override
    @Transactional
    public ModuleDto selectParentModuleOf(String requestURI, Boolean visibled, Boolean ui) {
        // detect current login user
        UserDetails user = AuthenticationUtils.getCurrentProfile();
        String userName = user.getUsername();
        return this.moduleService.selectParentModuleOf(userName, requestURI, visibled, ui);
    }

    /*
     * (Non-Javadoc)
     * @see org.nlh4j.web.base.dashboard.service.DashboardService#searchUIModulesList(java.lang.Long)
     */
    @Override
    @Transactional
    public List<ModuleDto> searchUIModulesList(Long pid) {
        // detect current login user
        UserDetails user = AuthenticationUtils.getCurrentProfile();
        String userName = user.getUsername();
        return this.moduleService.getUIModulesList(userName, pid, null);
    }
}
