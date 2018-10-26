/*
 * @(#)UserPagination.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.user.dto;

import org.nlh4j.core.pagination.AbstractPagingDto;
import org.nlh4j.web.base.common.AppConst;

/**
 * The {@link UserDto} paging information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class UserPagination extends AbstractPagingDto<UserDto> {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Initializes a new instance of {@link UserPagination}
     */
    protected UserPagination() {
        super(AppConst.findRecordsPerPage());
    }
}
