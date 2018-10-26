/*
 * @(#)AbstractPaginationSearchDto.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.pagination;

/**
 * The pagination data search information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 * @param <T> the data entity type
 * @param <C> the condition type
 */
public class AbstractPaginationSearchDto<T, C> extends PaginationSearchDto<T, C> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Initialize an instance of {@link AbstractPaginationSearchDto}
	 *
	 * @param limit the records limit number
	 */
	public AbstractPaginationSearchDto(int limit) {
		super();
		this.getData().getPagination().setPageNumber(1);
		this.getData().getPagination().setLimit(limit);
	}
	/**
	 * Initialize a new instance of {@link AbstractPaginationSearchDto}
	 *
	 * @param paging the pagination information
	 */
	public AbstractPaginationSearchDto(PagingDto<T> paging) {
		super(paging);
	}

	/**
	 * Apply searching unlimitted records
	 */
	public void setUnlimit() {
		this.getData().getPagination().setPageNumber(1);
		this.getData().getPagination().setLimit(-1);
	}
}
