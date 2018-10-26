/*
 * @(#)PaginationUtils.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.pagination;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * The pagination utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class PaginationUtils implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 検索系SQLを実行する際のオプションです。
	 *
	 * @see SelectOptions
	 */
	private SelectOptions options;
	private Pagination pagination;
	private int pageSize;
	private int offset;

	/**
	 * Initialize an instance of class {@link PaginationUtils}
	 *
	 * @param pagination
	 *            the pagination information
	 */
	public PaginationUtils(Pagination pagination) {
		Assert.notNull(pagination, "pagination");
		Assert.isTrue(pagination.getLimit() >= -1,
				"the page limit must be greater or equals than -1. -1 for unlimitted!!!");
		this.pageSize = pagination.getLimit();
		Assert.isTrue(pagination.getPageNumber() > 0, "the page number must be greater than 0!!!");
		this.offset = ((pagination.getPageNumber() - 1) * this.pageSize);
		this.pagination = pagination;
	}

	/**
	 * Get the DOMA select options
	 *
	 * @return the DOMA select options
	 */
	public SelectOptions getOptions() {
		if (this.options == null) {
			if (this.pageSize < 0) {
				this.options = SelectOptions.get();
			} else {
				this.options = SelectOptions.get().offset(this.offset).limit(this.pageSize).count();
			}
		}
		return this.options;
	}

	/**
	 * Get paging information
	 *
	 * @return Paging information
	 */
	public Pagination getPagination() {
	    if (this.pagination.isAutoRecalculate()) {
    		long totalRows = this.getOptions().getCount();
    		if (this.pagination.getTotalRows() > 0) {
    			totalRows = this.pagination.getTotalRows();
    		}
    		if (this.pageSize <= 0) {
    			this.pagination.setTotalRows(totalRows);
    			if (this.pagination.getTotalPages() > 0)
    				this.pagination.setTotalPages(this.pagination.getTotalPages());
    			else
    				this.pagination.setTotalPages(totalRows > 0 ? 1 : 0);
    		} else {
    			this.pagination.setTotalRows(totalRows);
    			this.pagination.setTotalPages(
    			        (int) (totalRows / this.pageSize)
    			        + ((totalRows % this.pageSize) > 0 ? 1 : 0));
    		}
	    }
		return this.pagination;
	}

	/**
	 * Get the DOMA order by SQL
	 *
	 * @return the DOMA order by SQL
	 */
	public String getOrderBy() {
		String orderby = "";
		if (!CollectionUtils.isEmpty(this.pagination.getSortColumns())) {
			List<SortColumn> columns = this.pagination.getSortColumns();
			Collections.sort(columns, new Comparator<SortColumn>() {
				@Override
				public int compare(SortColumn o1, SortColumn o2) {
					Sort sort1 = o1.getSort();
					Sort sort2 = o2.getSort();
					return (sort1 == null ? -1
							: sort2 == null ? 1
									: sort1.getPriority() < sort2.getPriority() ? -1
											: sort1.getPriority() > sort2.getPriority() ? 1 : 0);
				}
			});
			orderby = String.format("ORDER BY %s",
					StringUtils.arrayToCommaDelimitedString(columns.toArray(new SortColumn[columns.size()])));
		}
		return orderby;
	}
}
