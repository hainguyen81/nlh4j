/*
 * @(#)MasterService.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.service;

import java.io.Serializable;

import org.nlh4j.core.controller.AbstractMasterController;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.pagination.PaginationSearchDto;

/**
 * The service interface for {@link AbstractMasterController}
 *
 * @param <T> the entity class. it must be inherited from {@link AbstractDto}
 * @param <C> the entity search conditions class
 * @param <PK> the entity unique key class. it must be inherited from {@link AbstractDto}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public interface MasterService
    <T extends AbstractDto, C extends AbstractDto, PK extends AbstractDto>
    extends Serializable {
    /**
     * Find the pagination entities list by specified conditions
     *
     * @param search the search conditions/pagination information
     *
     * @return the pagination entities list or null
     */
    PaginationSearchDto<T, C> findEntities(PaginationSearchDto<T, C> search);

    /**
     * Find the entity by the data unique identity
     *
     * @param unique the data unique key
     *
     * @return the entity or null
     */
    T findEntity(PK unique);
    /**
     * Check the entity whether constraint with specified unique key
     *
     * @param unique the data unique key
     *
     * @return true for unique constraint; else false
     */
    public boolean isUniqueConstraint(PK unique);

    /**
     * Save/Update the specified entity
     *
     * @param entity entity to save/update
     * @param create specify inserting/updating
     *
     * @return effected records
     * @throws Exception thrown if fail
     */
    int saveOrUpdate(T entity, boolean create) throws Exception;
    /**
     * Delete the entity by the specified unique key
     *
     * @param unique the entity unique key
     *
     * @return effected records
     */
    int deleteEntity(PK unique);
}
