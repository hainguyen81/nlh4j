/*
 * @(#)ModuleServiceImpl.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import org.nlh4j.core.dto.UserDetails;
import org.nlh4j.core.service.AbstractService;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.web.core.domain.dao.ModuleDao;
import org.nlh4j.web.core.dto.ModuleDto;
import org.nlh4j.web.core.dto.UserDto;

/**
 * The implement of {@link ModuleService} interface
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Service(value = "moduleService")
public class ModuleServiceImpl extends AbstractService implements ModuleService {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link ModuleDao}
	 */
	@Inject
	private ModuleDao moduleDao;

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.service.ModuleService#getTopUIModulesList(java.lang.String)
	 */
	@Override
	@Transactional
    public List<ModuleDto> getTopUIModulesList(String userName) {
		return this.getUIModulesList(userName, 0L, null);
	}
	/* (Non-Javadoc)
	 * @see org.nlh4j.web.core.service.ModuleService#getUIModulesList(java.lang.String, java.lang.Long, java.lang.Boolean)
	 */
	@Override
	@Transactional
    public List<ModuleDto> getUIModulesList(String userName, Long pid, Boolean leaf) {
	    return moduleDao.findModules(userName, pid, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, leaf);
	}

	/* (Non-Javadoc)
	 * @see org.nlh4j.web.core.service.ModuleService#hasPermission(java.lang.String, java.lang.String[], java.lang.String[])
	 */
	@Override
	@Transactional
    public boolean hasPermission(String userName, String[] moduleCds, String[] functions) {
	    List<String> modLst = new ArrayList<String>();
	    List<String> funcLst = new ArrayList<String>();
        if (!CollectionUtils.isEmpty(moduleCds)) modLst.addAll(Arrays.asList(moduleCds));
        if (!CollectionUtils.isEmpty(functions)) funcLst.addAll(Arrays.asList(functions));
        return moduleDao.hasPermission(userName, modLst, funcLst);
	}
	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.service.ModuleService#getModuleCss(java.util.List)
	 */
	@Override
	@Transactional
    public List<String> getModuleStylesheets(List<String> moduleCds) {
		return moduleDao.findModuleStylesheets(moduleCds);
	}
	/* (Non-Javadoc)
	 * @see org.nlh4j.core.service.ExecutePermissionService#hasPermissionOnRequest(org.nlh4j.core.dto.UserDetails, java.lang.String[], java.lang.String, java.lang.String[])
	 */
	@Override
	@Transactional
    public boolean hasPermissionOnRequest(UserDetails user, String[] moduleCds, String requestURI, String[] functions) {
	    if (user == null || !StringUtils.hasText(user.getUsername())) {
	        logger.error("Invalid user to detect permission!");
	        return Boolean.FALSE;
	    }
        List<String> modLst = new ArrayList<String>();
        List<String> funcLst = new ArrayList<String>();
        if (!CollectionUtils.isEmpty(moduleCds)) modLst.addAll(Arrays.asList(moduleCds));
        if (!CollectionUtils.isEmpty(functions)) funcLst.addAll(Arrays.asList(functions));
        if (!BeanUtils.isInstanceOf(user, UserDto.class)) {
            logger.error("Invalid user instance '" + UserDto.class.getName() + "' to detect permission!");
            return Boolean.FALSE;
        }
        UserDto udto = (UserDto) user;
        return moduleDao.hasPermissionOnRequest(udto.getUsername(), modLst, requestURI, funcLst);
	}
	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.service.ModuleService#isEnabled(java.lang.String[])
	 */
	@Override
	@Transactional
    public boolean isEnabled(String[] moduleCds) {
		List<String> modules = new ArrayList<String>();
		if (!CollectionUtils.isEmpty(moduleCds)) modules.addAll(Arrays.asList(moduleCds));
		return moduleDao.isEnabled(modules);
	}
	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.service.ModuleService#isEnabledOnRequest(java.lang.String[], java.lang.String)
	 */
	@Override
	@Transactional
    public boolean isEnabledOnRequest(String[] moduleCds, String requestURI) {
		List<String> modules = new ArrayList<String>();
		if (!CollectionUtils.isEmpty(moduleCds)) modules.addAll(Arrays.asList(moduleCds));
		return moduleDao.isEnabledOnRequest(modules, requestURI);
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.web.core.service.ModuleService#selectParentModuleOf(java.lang.String, java.lang.String, java.lang.Boolean, java.lang.Boolean)
	 */
	@Override
	@Transactional
    public ModuleDto selectParentModuleOf(String userName, String requestURI, Boolean visibled, Boolean ui) {
		return moduleDao.findParentModuleOf(userName, requestURI, visibled, ui);
	}

    /* (Non-Javadoc)
     * @see org.nlh4j.web.core.service.ModuleService#getModuleFunctions(java.util.List)
     */
    @Override
    @Transactional
    public Map<String, List<String>> getModuleFunctions(String userName, List<String> moduleCds) {
        Map<String, List<String>> functions = new LinkedHashMap<String, List<String>>();
        List<ModuleDto> modules = this.moduleDao.findModulesByCode(
                userName, moduleCds, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        if (!CollectionUtils.isEmpty(modules)) {
            for(ModuleDto module : modules) {
                functions.put(module.getCode(),
                        CollectionUtils.toList(StringUtils.delimitedListToStringArray(
                                module.getFunctions(), "|")));
            }
        }
        return functions;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.web.core.service.ModuleService#getModuleForwardUrls(java.lang.String, java.lang.String[])
     */
    @Override
    @Transactional
    public List<String> getModuleForwardUrls(String userName, String[] moduleCds) {
    	List<String> modLst = new ArrayList<String>();
    	if (!CollectionUtils.isEmpty(moduleCds)) modLst.addAll(Arrays.asList(moduleCds));
    	return this.moduleDao.findModuleForwardUrls(userName, modLst);
    }
}
