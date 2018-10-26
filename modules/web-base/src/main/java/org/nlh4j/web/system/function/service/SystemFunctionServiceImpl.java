/*
 * @(#)SystemFunctionServiceImpl.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.function.service;

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
import org.nlh4j.web.core.domain.dao.FunctionDao;
import org.nlh4j.web.core.domain.entity.Function;
import org.nlh4j.web.core.dto.FunctionDto;
import org.nlh4j.web.system.function.domain.dao.SystemFunctionDao;
import org.nlh4j.web.system.function.dto.FunctionSearchConditions;
import org.nlh4j.web.system.function.dto.FunctionUniqueDto;

/**
 * The implement of {@link SystemFunctionService} interface
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Service
public class SystemFunctionServiceImpl extends AbstractService implements SystemFunctionService {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * {@link FunctionDao}
     */
    @Inject
    private FunctionDao moduleDao;
    /**
     * {@link SystemFunctionDao}
     */
    @Inject
    private SystemFunctionDao systemFunctionDao;

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#findEntities(org.nlh4j.core.pagination.PaginationSearchDto)
     */
    @Override
    @Transactional
    public PaginationSearchDto<FunctionDto, FunctionSearchConditions> findEntities(
            PaginationSearchDto<FunctionDto, FunctionSearchConditions> search) {
        try {
            // no pagination because this is a tree
            search.getData().getPagination().setLimit(-1);
            // search all modules
            search.getData().setData(
                    this.systemFunctionDao.findFunctions(
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
    public FunctionDto findEntity(FunctionUniqueDto unique) {
        return this.systemFunctionDao.findFunction(unique);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#isUniqueConstraint(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    @Transactional
    public boolean isUniqueConstraint(FunctionUniqueDto unique) {
        return this.systemFunctionDao.isUniqueConstraint(unique);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#saveOrUpdate(org.nlh4j.core.dto.AbstractDto, boolean)
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    public int saveOrUpdate(FunctionDto entity, boolean create) throws Exception {
        int effected = 0;
        // current user
        Function mod = null;
        // if inserting
        if (create) {
            // clone information
            mod = BeanUtils.copyBean(entity, Function.class);
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
    public int deleteEntity(FunctionUniqueDto unique) {
        int effected = 0;
        FunctionDto module = this.systemFunctionDao.findFunction(unique);
        if (module != null) {
            // delete module
            effected = this.systemFunctionDao.deleteFunctionBy(module.getCode());
        }
        return effected;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.web.system.module.service.SystemFunctionService#findFunctionsExcluded(java.lang.String[])
     */
    @Override
    @Transactional
    public List<FunctionDto> findFunctionsExcluded(String... codes) {
        List<String> codesLst = new LinkedList<String>();
        if (!ArrayUtils.isEmpty(codes)) {
            for(String code : codes) {
                if (!StringUtils.hasText(code)) continue;
                codesLst.add(code.trim());
            }
        }
        return this.systemFunctionDao.findFunctionsExcluded(codesLst);
    }
}
