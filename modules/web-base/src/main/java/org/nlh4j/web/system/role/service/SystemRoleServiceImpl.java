/*
 * @(#)RoleServiceImpl.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.role.service;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.nlh4j.core.annotation.InjectTransactionalService;
import org.nlh4j.core.pagination.PaginationSearchDto;
import org.nlh4j.core.service.AbstractService;
import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.DateUtils;
import org.nlh4j.web.core.domain.dao.RoleDao;
import org.nlh4j.web.core.domain.dao.RoleGroupDao;
import org.nlh4j.web.core.domain.entity.Role;
import org.nlh4j.web.core.dto.FunctionDto;
import org.nlh4j.web.core.dto.RoleDto;
import org.nlh4j.web.core.dto.UserDto;
import org.nlh4j.web.system.function.domain.dao.SystemFunctionDao;
import org.nlh4j.web.system.function.dto.FunctionSearchConditions;
import org.nlh4j.web.system.role.domain.dao.SystemRoleDao;
import org.nlh4j.web.system.role.domain.entity.RoleGroup;
import org.nlh4j.web.system.role.dto.RoleGroupDto;
import org.nlh4j.web.system.role.dto.RoleGroupSearchConditions;
import org.nlh4j.web.system.role.dto.RoleGroupUniqueDto;
import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.transaction.annotation.Transactional;

/**
 * The implement of {@link SystemRoleService} interface
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@InjectTransactionalService
public class SystemRoleServiceImpl extends AbstractService implements SystemRoleService {

	/** serial uid **/
	private static final long serialVersionUID = 1L;

    /**
     * {@link RoleDao}
     */
    @Inject
    private RoleDao roleDao;
    /**
     * {@link RoleGroupDao}
     */
    @Inject
    private RoleGroupDao roleGroupDao;
	/**
	 * {@link SystemRoleDao}
	 */
	@Inject
	private SystemRoleDao systemRoleDao;
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
    public PaginationSearchDto<RoleGroupDto, RoleGroupSearchConditions> findEntities(
            PaginationSearchDto<RoleGroupDto, RoleGroupSearchConditions> search) {
        // search role groups
        List<RoleGroup> roles = systemRoleDao.findGroups(
                search.getSearchConditions(), search.getOptions(), search.getOrderBy());
        // save search result
        try {
            search.getData().setData(BeanUtils.copyBeansList(roles, RoleGroupDto.class));
        } catch (Exception e) {
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
    public RoleGroupDto findEntity(RoleGroupUniqueDto unique) {
        try {
            return BeanUtils.copyBean(
                    this.systemRoleDao.findGroup(unique),
                    RoleGroupDto.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.web.system.role.service.RoleService#isUniqueConstraint(org.nlh4j.web.system.role.dto.RoleGroupUniqueDto)
     */
    @Override
    @Transactional
    public boolean isUniqueConstraint(RoleGroupUniqueDto unique) {
        return this.systemRoleDao.isUniqueConstraint(unique);
    }

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.web.system.role.service.SystemRoleService#searchRolesBy(org.nlh4j.web.system.role.dto.RoleGroupUniqueDto)
	 */
	@Override
	@Transactional
    public List<RoleDto> searchRolesBy(RoleGroupUniqueDto unique) {
		return this.systemRoleDao.findRolesBy(unique, Boolean.TRUE, SelectOptions.get(), null);
	}
	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.web.system.role.service.SystemRoleService#searchUnRolesBy(org.nlh4j.web.system.role.dto.RoleGroupUniqueDto)
	 */
	@Override
	@Transactional
    public List<RoleDto> searchUnRolesBy(RoleGroupUniqueDto unique) {
		return this.systemRoleDao.findUnRolesBy(unique, Boolean.TRUE, SelectOptions.get(), null);
	}

    /* (Non-Javadoc)
     * @see org.nlh4j.web.system.role.service.RoleService#saveOrUpdate(org.nlh4j.web.system.role.dto.RoleGroupDto, boolean)
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    public int saveOrUpdate(RoleGroupDto roleGroup, boolean create) throws Exception {
        int effected = 0;
        // current user
        org.nlh4j.web.core.domain.entity.RoleGroup rg = null;
        UserDto udto = AuthenticationUtils.getCurrentProfile(UserDto.class);
        // if inserting
        if (create) {
            // clone information
            rg = BeanUtils.copyBean(roleGroup, org.nlh4j.web.core.domain.entity.RoleGroup.class);
            rg.setCreatedUser(udto.getId());
            rg.setCreatedAt(DateUtils.currentTimestamp());
            effected = this.roleGroupDao.insert(rg);
        }
        else {
            // search from database for creating information
            rg = this.roleGroupDao.selectById(roleGroup.getId());
            BeanUtils.copyProperties(rg, roleGroup);
            rg.setUpdatedAt(DateUtils.currentTimestamp());
            rg.setUpdatedUser(udto.getId());
            effected = this.roleGroupDao.update(rg);
        }
        // deletes old children roles if necessary
        if (effected > 0 && !create) {
            this.systemRoleDao.deleteRolesBy(rg.getId());
        }
        // insert all new children roles
        if (effected > 0) {
            // effect session if necessary
            boolean session = rg.getId().equals(udto.getGid());
            if (session && !CollectionUtils.isEmpty(udto.getRoles())) {
                udto.getRoles().clear();
            }
            List<Role> roles = new LinkedList<Role>();
            for(RoleDto role : roleGroup.getRoles()) {
                Role r = BeanUtils.copyBean(role, Role.class);
                r.setGid(rg.getId());
                r.setCreatedAt(DateUtils.currentTimestamp());
                r.setCreatedUser(udto.getId());
                roles.add(r);
                // effect session if necessary
                if (session) udto.getRoles().add(role);
            }
            this.roleDao.batchInsert(roles);
            // effect session if necessary
            if (session) AuthenticationUtils.invalidateAuthentication(udto);
        }
        return effected;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MasterService#deleteEntity(org.nlh4j.core.dto.AbstractDto)
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    public int deleteEntity(RoleGroupUniqueDto unique) {
        int effected = 0;
        RoleGroup group = this.systemRoleDao.findGroup(unique);
        if (group != null) {
            // remove users out of group
            this.systemRoleDao.removeUserRoles(group.getId());
            // deletes old children roles if necessary
            this.systemRoleDao.deleteRolesBy(group.getId());
            // delete role group
            effected = this.systemRoleDao.deleteRoleGroupBy(group.getCode());
        }
        return effected;
    }

	/* (Non-Javadoc)
	 * @see org.nlh4j.web.system.role.service.SystemRoleService#searchFunctions(java.lang.Boolean)
	 */
	@Override
	@Transactional
    public List<FunctionDto> searchFunctions(Boolean enabled) {
		FunctionSearchConditions cond = new FunctionSearchConditions();
		cond.setEnabled(Boolean.TRUE);
		return systemFunctionDao.findFunctions(
				cond, SelectOptions.get(), null);
	}
}
