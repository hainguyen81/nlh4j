/*
 * @(#)AbstractPagingDto.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.pagination;

/**
 * The pagination data information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 * @param <T> the data entity type
 */
public abstract class AbstractPagingDto<T> extends PagingDto<T> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Initialize an instance of {@link AbstractPagingDto}
	 */
	protected AbstractPagingDto(int limit) {
		super();
		super.getPagination().setPageNumber(1);
		super.getPagination().setLimit(limit);
	}
}
