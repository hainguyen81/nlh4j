/*
 * @(#)RoleGroupPagination.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.role.dto;

import org.nlh4j.core.pagination.AbstractPagingDto;
import org.nlh4j.web.base.common.AppConst;

/**
 * The {@link RoleGroupDto} paging information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class RoleGroupPagination extends AbstractPagingDto<RoleGroupDto> {

    /** */
	private static final long serialVersionUID = 1L;

    /**
     * Initializes a new instance of {@link RoleGroupPagination}
     */
    public RoleGroupPagination() {
        this(AppConst.findRecordsPerPage());
    }
    public RoleGroupPagination(int limit) {
        super(limit);
    }
}
