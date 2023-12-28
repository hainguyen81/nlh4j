/*
 * @(#)DomaBaseDao.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package jp.doma.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.machinezoo.noexception.Exceptions;

import org.nlh4j.core.annotation.InjectRepository;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.support.IGenericTypeSupport;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.ExceptionUtils;
import org.nlh4j.util.LogUtils;
import org.seasar.doma.FetchType;
import org.seasar.doma.Select;
import org.seasar.doma.Table;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.builder.SelectBuilder;
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
	 * Get the number of records
	 * 
	 * @return the number of records
	 */
	default long countAll() {
		return Optional.ofNullable(getEntityClass())
			.map(clz -> BeanUtils.getClassAnnotation(clz, Table.class))
			.map(Table::name).map(tblName -> {
				Config config = Config.get(this);
		        SelectBuilder builder = SelectBuilder.newInstance(config);
		        builder.sql(String.format("SELECT COUNT(1) FROM %s", tblName));
		        return builder.getScalarSingleResult(long.class);
			}).orElse(0L);
    }
	
	/**
	 * Get the number of records
	 * 
	 * @return the number of records
	 */
	@Select
	default List<E> selectAll() {
		final Config config = Config.get(this);
		config.getJdbcLogger().logDaoMethodEntering(getClass().getName(), "selectAll");
		return Optional.ofNullable(getEntityClass())
			.map(clz -> BeanUtils.getClassAnnotation(clz, Table.class))
			.map(Table::name).map(tblName -> {
		        SelectBuilder builder = SelectBuilder.newInstance(config);
		        builder.sql(String.format("SELECT * FROM %s", tblName));
		        builder.ensureResult(false);
		        builder.ensureResultMapping(false);
		        builder.fetch(FetchType.LAZY);
		        builder.queryTimeout(-1);
		        builder.maxRows(-1);
		        builder.fetchSize(-1);
		        builder.sqlLogType(SqlLogType.FORMATTED);
		        return builder.getEntityResultList(getEntityClass());
			}).orElseGet(LinkedList::new);
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
