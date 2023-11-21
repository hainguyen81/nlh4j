/*
 * @(#)PaginationSearchDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.pagination;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

import org.seasar.doma.jdbc.SelectOptions;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;

/**
 * The pagination data search information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 * @param <T> the data entity type
 * @param <C> the condition type
 */
@EqualsAndHashCode(callSuper = false)
public class PaginationSearchDto<T, C> extends AbstractDto {

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
			Type genSuperClass = this.getClass().getGenericSuperclass();
			ParameterizedType paramType = BeanUtils.safeType(genSuperClass, ParameterizedType.class);
			if (paramType != null) {
				Type[] actualTypeArgs = paramType.getActualTypeArguments();
				if (CollectionUtils.isElementsNumber(actualTypeArgs, 2)) {
					Class<C> condClass = (Class<C>) actualTypeArgs[1];
					this.searchConditions = (C) BeanUtils.newInstance(condClass);
				}
			}
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
}
