/*
 * @(#)Pagination.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.pagination;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.core.dto.AbstractDto;

/**
 * The pagination information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Pagination extends AbstractDto {

	/** */
	private static final long serialVersionUID = 1L;
	/** the page number **/
	private int pageNumber = 1;
	/** total pages number **/
	private int totalPages = 0;
	private long totalRows = 0;
	/** the records number limit (such as records per page) **/
	private int limit = 15;
    /** specify total rows/pages will be re-calculate automatically */
    private boolean autoRecalculate = true;
	/** sort information **/
	private List<SortColumn> sortColumns;
	public void setSortColumns(SortColumn...columns) {
		if (this.sortColumns != null) this.sortColumns.clear();
		else this.sortColumns = new LinkedList<SortColumn>();
		if (!ArrayUtils.isEmpty(columns)) {
			for(SortColumn column : columns) {
				this.sortColumns.add(column);
			}
		}
	}
}