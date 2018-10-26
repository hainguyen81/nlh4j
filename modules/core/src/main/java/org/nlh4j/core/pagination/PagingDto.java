/*
 * @(#)PagingDto.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.pagination;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.core.dto.AbstractDto;

/**
 * The paging data information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PagingDto<T> extends AbstractDto {

	/** */
	private static final long serialVersionUID = 1L;

	/** the pagination information **/
	private Pagination pagination;
	/** the page data **/
	private List<T> data;

	/**
	 * Initialize an instance of {@link PagingDto}
	 */
	protected PagingDto() {
		Pagination pagination = new Pagination();
		pagination.setTotalPages(0);
		pagination.setLimit(15);
		pagination.setPageNumber(1);
		this.setData(new LinkedList<T>());
		this.setPagination(pagination);
	}
	protected PagingDto(Pagination pagination) {
		this.setPagination(pagination);
		this.setData(new LinkedList<T>());
	}
}
