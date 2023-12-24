/*
 * @(#)MasterSearchController.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.controller;

import org.seasar.doma.internal.util.AssertionUtil;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.Optional;

import com.machinezoo.noexception.Exceptions;

import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.dto.BaseSearchParamControllerDto;
import org.nlh4j.core.pagination.PaginationSearchDto;
import org.nlh4j.core.service.MasterService;
import org.nlh4j.util.ExceptionUtils;

/**
 * Abstract controller for searching on master screens
 *
 * @param <T> the main entity type
 * @param <C> the entity search condition type
 * @param <S> the bound class of searching conditions
 * @param <Srv> the {@link MasterService} class
 * @param <M> the attached upload data type
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
public abstract class MasterSearchController
    <T extends AbstractDto, C extends AbstractDto,
    S extends BaseSearchParamControllerDto<C>,
    Srv extends MasterService<T, C, AbstractDto>,
    M extends AbstractDto>
    extends AbstractSearchController<T, C, S, M> {

    /** */
    private static final long serialVersionUID = 1L;
    @Getter(value = AccessLevel.PROTECTED)
    private Class<Srv> masterServiceClass;
    private Srv masterService = null;

    /**
     * Initialize a new instance of {@link MasterSearchController}
     */
    protected MasterSearchController(Class<Srv> masterServiceClass) {
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
        Assert.notNull(this.masterService, this.masterServiceClass.getName());
        synchronized (this.masterService) {
            return this.masterService;
        }
    }

    /*
     * (Non-Javadoc)
     * @see org.nlh4j.core.controller.AbstractMasterController#searchEntities(org.nlh4j.core.pagination.PaginationSearchDto)
     */
    @Override
    protected final PaginationSearchDto<T, C> searchEntities(PaginationSearchDto<T, C> search) {
        return this.getMasterService().findEntities(search);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    protected Class<M> getAttachedUploadDataType() {
    	return Optional.ofNullable(getClassGeneraicTypeByIndex(4))
				.map(ExceptionUtils.wrap(logger).function(Exceptions.wrap().function(t -> (Class<M>) t)))
				.filter(Optional::isPresent).map(Optional::get).orElse(null);
    }
}
