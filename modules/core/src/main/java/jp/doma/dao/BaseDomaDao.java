/*
 * @(#)DomaBaseDao.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package jp.doma.dao;

import java.util.List;
import java.util.Optional;

import com.machinezoo.noexception.Exceptions;

import org.nlh4j.core.annotation.InjectRepository;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.exceptions.ApplicationUnderConstructionException;
import org.nlh4j.support.IGenericTypeSupport;
import org.nlh4j.util.ExceptionUtils;
import org.nlh4j.util.LogUtils;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

/**
 * FIXME Due to [DOMA4440] The methods in the parent interface is not default method.<br>
 * When the parent interface is not annotated with @Dao, the all methods in the interface must be default methods.<br>
 * <br>
 * DOMA Base DAO interface
 * 
 * @param <E> {@link AbstractDto}
 */
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@InjectRepository
public interface BaseDomaDao<E extends AbstractDto> extends IGenericTypeSupport {

	/**
     * Select all {@link AbstractDto}
     * 
     * @return all {@link AbstractDto} entities list
     */
    @Select
    default List<E> selectAll() {
    	throw new ApplicationUnderConstructionException("Not implemented yet!");
    }

    /**
     * Select {@link AbstractDto} by identity
     *
     * @param id identity
     *
     * @return {@link AbstractDto} by identity
     */
    @Select
    default E selectById(Long id) {
    	throw new ApplicationUnderConstructionException("Not implemented yet!");
    }

    /**
     * Insert the specified {@link AbstractDto}
     *
     * @param entity to insert
     *
     * @return effected records
     */
    @Insert
    default int insert(E entity) {
    	throw new ApplicationUnderConstructionException("Not implemented yet!");
    }

    /**
     * Update the specified {@link AbstractDto}
     *
     * @param entity to update
     *
     * @return effected records
     */
    @Update
    default int update(E entity) {
    	throw new ApplicationUnderConstructionException("Not implemented yet!");
    }

    /**
     * Delete the specified {@link AbstractDto}
     *
     * @param entity to delete
     *
     * @return effected records
     */
    @Delete
    default int delete(E entity) {
    	throw new ApplicationUnderConstructionException("Not implemented yet!");
    }

    /**
     * Insert multiple {@link AbstractDto}
     *
     * @param roles {@link AbstractDto} list to insert
     *
     * @return effected records
     */
    @BatchInsert
    default int[] batchInsert(List<E> roles) {
    	throw new ApplicationUnderConstructionException("Not implemented yet!");
    }

    /**
     * Get the generic entity class
     * 
     * @return the generic entity class
     */
    @SuppressWarnings("unchecked")
	default Class<E> getEntityClass() {
    	return Optional.ofNullable(getClassGeneraicTypeByIndex(0))
				.map(ExceptionUtils.wrap(e -> {
					LogUtils.logError(getClass(), e.getMessage(), e);
					return Boolean.TRUE;
				}).function(Exceptions.wrap().function(t -> (Class<E>) t)))
				.filter(Optional::isPresent).map(Optional::get).orElse(null);
	}
}
