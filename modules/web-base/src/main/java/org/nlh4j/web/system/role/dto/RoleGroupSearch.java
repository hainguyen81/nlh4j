/*
 * @(#)RoleGroupSearch.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.role.dto;

import org.nlh4j.core.pagination.AbstractPaginationSearchDto;
import org.nlh4j.core.pagination.PaginationSearchDto;

/**
 * The role group search information.
 * See also {@link PaginationSearchDto}.<br>
 * + the {@link RoleGroupDto} the search result DTO<br>
 * + the search condition: role group name
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class RoleGroupSearch extends AbstractPaginationSearchDto<RoleGroupDto, RoleGroupSearchConditions> {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Initialize an instance of {@link RoleGroupSearch}
	 */
	public RoleGroupSearch() { super(new RoleGroupPagination()); }
}
