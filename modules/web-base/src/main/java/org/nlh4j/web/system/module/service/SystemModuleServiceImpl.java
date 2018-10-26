/*
 * @(#)SystemModuleServiceImpl.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.module.service;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.nlh4j.core.pagination.PaginationSearchDto;
import org.nlh4j.core.service.AbstractService;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.web.core.domain.dao.ModuleDao;
import org.nlh4j.web.core.domain.entity.Module;
import org.nlh4j.web.core.dto.ModuleDto;
import org.nlh4j.web.system.module.domain.dao.SystemModuleDao;
import org.nlh4j.web.system.module.dto.ModuleSearchConditions;
import org.nlh4j.web.system.module.dto.ModuleUniqueDto;

/**
 * The implement of {@link SystemModuleService} interface
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Service
public class SystemModuleServiceImpl extends AbstractService implements SystemModuleService {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * {@link ModuleDao}
     */
    @Inject
    private ModuleDao moduleDao;
    /**
     * {@link SystemModuleDao}
     */
    @Inject
    private SystemModuleDao systemModuleDao;

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#findEntities(org.nlh4j.core.pagination.PaginationSearchDto)
     */
    @Override
    @Transactional
    public PaginationSearchDto<ModuleDto, ModuleSearchConditions> findEntities(
            PaginationSearchDto<ModuleDto, ModuleSearchConditions> search) {
        try {
            // no pagination because this is a tree
            search.getData().getPagination().setLimit(-1);
            // search all modules
            search.getData().setData(
                    this.systemModuleDao.findModules(
                            search.getSearchConditions(),
                            search.getOptions(), search.getOrderBy()));
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
    public ModuleDto findEntity(ModuleUniqueDto unique) {
        return this.systemModuleDao.findModule(unique);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#isUniqueConstraint(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    @Transactional
    public boolean isUniqueConstraint(ModuleUniqueDto unique) {
        return this.systemModuleDao.isUniqueConstraint(unique);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#saveOrUpdate(org.nlh4j.core.dto.AbstractDto, boolean)
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    public int saveOrUpdate(ModuleDto entity, boolean create) throws Exception {
        int effected = 0;
        // current user
        Module mod = null;
        // if inserting
        if (create) {
            // clone information
            mod = BeanUtils.copyBean(entity, Module.class);
            effected = this.moduleDao.insert(mod);
        }
        else {
            // search from database for creating information
            mod = this.moduleDao.selectById(entity.getId());
            BeanUtils.copyProperties(mod, entity);
            effected = this.moduleDao.update(mod);
        }
        return effected;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#deleteEntity(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    public int deleteEntity(ModuleUniqueDto unique) {
        int effected = 0;
        ModuleDto module = this.systemModuleDao.findModule(unique);
        if (module != null) {
            // remove child modules
            this.systemModuleDao.removeOutOf(module.getCode());
            // delete module
            effected = this.systemModuleDao.deleteModuleBy(module.getCode());
        }
        return effected;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.web.system.module.service.SystemModuleService#findModulesExcluded(java.lang.String[])
     */
    @Override
    @Transactional
    public List<ModuleDto> findModulesExcluded(String... codes) {
        List<String> codesLst = new LinkedList<String>();
        if (!ArrayUtils.isEmpty(codes)) {
            for(String code : codes) {
                if (!StringUtils.hasText(code)) continue;
                codesLst.add(code.trim());
            }
        }
        return this.systemModuleDao.findModulesExcluded(codesLst);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.web.system.module.service.SystemModuleService#isUrlUniqueConstraint(java.lang.Long, java.lang.String, java.lang.String)
     */
    @Override
    @Transactional
    public boolean isUrlUniqueConstraint(Long id, String code, String url) {
        return this.systemModuleDao.isUrlUniqueConstraint(id, code, url);
    }
}
