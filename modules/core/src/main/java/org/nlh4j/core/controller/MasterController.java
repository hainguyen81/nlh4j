/*
 * @(#)MasterController.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.controller;

import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.dto.BaseEntityParamControllerDto;
import org.nlh4j.core.dto.BaseSearchParamControllerDto;
import org.nlh4j.core.pagination.PaginationSearchDto;
import org.nlh4j.core.service.MasterService;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.seasar.doma.internal.util.AssertionUtil;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Abstract controller for master screens
 *
 * @param <T> the main entity type
 * @param <C> the entity search condition type
 * @param <PK> the entity primary/unique key type
 * @param <S> the bound class of searching conditions
 * @param <U> the bound class of entity primary/unique key
 * @param <Srv> the {@link MasterService} class
 * @param <M> the attached upload data type
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
public abstract class MasterController
    <T extends AbstractDto, C extends AbstractDto, PK extends AbstractDto,
    S extends BaseSearchParamControllerDto<C>, U extends BaseEntityParamControllerDto<PK>,
    Srv extends MasterService<T, C, PK>,
    M extends AbstractDto>
    extends AbstractMasterController<T, C, PK, S, U, M> {

	/** */
	private static final long serialVersionUID = 1L;
	@Getter(value = AccessLevel.PROTECTED)
	private Class<Srv> masterServiceClass;
	private Srv masterService = null;

	/**
	 * Initialize a new instance of {@link MasterController}
	 */
	protected MasterController(Class<Srv> masterServiceClass) {
	    AssertionUtil.assertNotNull(masterServiceClass);
	    this.masterServiceClass = masterServiceClass;
	}

	/**
	 * Get the {@link MasterService} instance
	 *
	 * @return the {@link MasterService} instance
	 */
	protected final Srv getMasterService() {
	    if (this.masterService == null) {
	        this.masterService = super.findBean(this.masterServiceClass);
	    }
	    AssertionUtil.assertNotNull(this.masterService);
	    return this.masterService;
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.controller.AbstractMasterController#searchEntities(org.nlh4j.core.pagination.PaginationSearchDto)
	 */
	@Override
	protected PaginationSearchDto<T, C> searchEntities(PaginationSearchDto<T, C> search) {
		return this.getMasterService().findEntities(search);
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.controller.AbstractMasterController#findEntityBy(org.nlh4j.core.dto.AbstractDto)
	 */
	@Override
    protected final T findEntityBy(PK entitypk) {
	    return (entitypk == null ? null : this.getMasterService().findEntity(entitypk));
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.controller.AbstractMasterController#isUnique(org.nlh4j.core.dto.AbstractDto)
	 */
	@Override
	protected final boolean isUnique(PK entitypk) {
	    return (entitypk == null ? false : this.getMasterService().isUniqueConstraint(entitypk));
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.controller.AbstractMasterController#doSaveOrUpdate(org.nlh4j.core.dto.AbstractDto, boolean)
	 */
	@Override
	protected final int doSaveOrUpdate(T entity, boolean create) {
	    AssertionUtil.assertNotNull(entity);
	    try {
	        return this.getMasterService().saveOrUpdate(entity, create);
	    }
	    catch(Exception e) {
	        throw new ApplicationRuntimeException(-1, e.getMessage(), e);
	    }
	}
	/**
	 * Get the entity unique key from the specified entity
	 *
	 * @param entity the entity to parse
	 *
	 * @return the entity unique key
	 */
	protected abstract PK getEntityPk(T entity);
	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.controller.AbstractMasterController#existEntity(org.nlh4j.core.dto.AbstractDto)
	 */
	@Override
	protected final boolean existEntity(T entity) {
	    PK entityPk = (entity == null ? null : this.getEntityPk(entity));
	    return (entityPk == null ? false : this.findEntityBy(entityPk) != null);
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.controller.AbstractMasterController#doDelete(org.nlh4j.core.dto.AbstractDto)
	 */
	@Override
	protected final boolean doDelete(PK entitypk) {
	    AssertionUtil.assertNotNull(entitypk);
        try {
            return (this.getMasterService().deleteEntity(entitypk) >= 0);
        }
        catch(Exception e) {
            throw new ApplicationRuntimeException(-1, e.getMessage(), e);
        }
	}
}
