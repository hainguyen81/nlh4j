/*
 * @(#)Sort.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.pagination;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.core.dto.AbstractDto;

/**
 * The order information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Sort extends AbstractDto {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Sort direction ASC/DESC
	 */
	public static enum Direction {
		Asc,
		Desc
	};

	/** sort direction **/
	private Direction direction;
	/** priority **/
	private int priority;

	public Sort() {}
	public Sort(Direction direction) {
		this.setDirection(direction);
	}

	/*
	 * (Non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (Direction.Asc.equals(this.getDirection()) ? "asc" : "desc");
	}
}
