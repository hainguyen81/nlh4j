/*
 * @(#)SortColumn.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.pagination;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.core.dto.AbstractDto;

/**
 * The sort column information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SortColumn extends AbstractDto {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/** sort column **/
	private String name;
	/** sort information **/
	private Sort sort;

	public SortColumn() {}
	public SortColumn(String name, Sort sort) {
		this.setName(name);
		this.setSort(sort);
	}
	public SortColumn(String name, Sort.Direction direction) {
        this.setName(name);
        this.setSort(new Sort(direction));
    }

	/*
	 * (Non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s %s", this.getName(), this.getSort());
	}
}
