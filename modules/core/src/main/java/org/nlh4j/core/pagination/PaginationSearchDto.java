/*
 * @(#)PaginationSearchDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.pagination;

import java.util.Objects;
import java.util.Optional;

import com.machinezoo.noexception.Exceptions;

import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.support.IGenericTypeSupport;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.ExceptionUtils;
import org.seasar.doma.jdbc.SelectOptions;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * The pagination data search information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 * @param <T> the data entity type
 * @param <C> the condition type
 */
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class PaginationSearchDto<T, C> extends AbstractDto implements IGenericTypeSupport {

	private static final long serialVersionUID = 1L;

	/** search conditions */
	@Setter
	private C searchConditions;
	/**
	 * Get the searchConditions
	 *
	 * @return the searchConditions
	 */
	@SuppressWarnings("unchecked")
	public C getSearchConditions() {
		// detect for creating default condition automatically
		if (this.searchConditions == null) {
			this.searchConditions = (C) BeanUtils.newInstance(getSearchConditionsType());
		}
		// detect for unknown conditions
		return Objects.requireNonNull(this.searchConditions, "search conditions could not be null/empty!");
	}

	/** pagination */
	@Setter
	private PagingDto<T> data;
	/**
	 * Get the data pagination (will be applied pagination if searching data)
	 *
	 * @return the data pagination (will be applied pagination if searching data)
	 */
	public PagingDto<T> getData() {
		if (this.pagination != null && this.data != null) {
			this.data.setPagination(this.pagination.getPagination());
		}
		return this.data;
	}

	/**
	 * Initialize an instance of {@link PaginationSearchDto}
	 */
	public PaginationSearchDto() {
		this.setData(new PagingDto<T>());
	}
	/**
	 * Initialize a new instance of {@link PaginationSearchDto}
	 *
	 * @param pagingDto pagination information
	 */
	public PaginationSearchDto(PagingDto<T> pagingDto) {
		this.setData(Objects.requireNonNull(pagingDto, "pagingDto"));
	}

	/**
	 * The pagination utils instance base on the paging data DTO information
	 */
	private PaginationUtils pagination;
	/**
	 * Get the pagination utils instance base on the paging data DTO information
	 * @return the pagination utils instance base on the paging data DTO information
	 */
	protected final PaginationUtils getPagination() {
		if (this.pagination == null) {
			PagingDto<T> data = this.getData();
			if (data != null && data.getPagination() != null) {
				this.pagination = new PaginationUtils(data.getPagination());
			}
		}
		return this.pagination;
	}

	/**
	 * Get the DOMA select options base on the paging data DTO information
	 *
	 * @return the DOMA select options
	 */
	public SelectOptions getOptions() {
		PaginationUtils pagination = this.getPagination();
		if (pagination == null) return SelectOptions.get();
		return pagination.getOptions();
	}
	/**
	 * Get the DOMA SQL order by base on the paging data DTO information
	 *
	 * @return the DOMA SQL order by
	 */
	public String getOrderBy() {
		PaginationUtils pagination = this.getPagination();
		if (pagination == null) return "";
		return pagination.getOrderBy();
	}
	
	/**
	 * Get the data entity type class
	 * 
	 * @return the data entity type class
	 */
	@SuppressWarnings("unchecked")
	protected Class<T> getSearchEntityType() {
		return Optional.ofNullable(getClassGeneraicTypeByIndex(0))
				.map(ExceptionUtils.wrap(log).function(Exceptions.wrap().function(t -> (Class<T>) t)))
				.filter(Optional::isPresent).map(Optional::get).orElse(null);
	}
	
	/**
	 * Get the condition type class
	 * 
	 * @return the condition type class
	 */
	@SuppressWarnings("unchecked")
	protected Class<C> getSearchConditionsType() {
		return Optional.ofNullable(getClassGeneraicTypeByIndex(1))
				.map(ExceptionUtils.wrap(log).function(Exceptions.wrap().function(t -> (Class<C>) t)))
				.filter(Optional::isPresent).map(Optional::get).orElse(null);
	}
}
