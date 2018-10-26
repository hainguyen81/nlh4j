/*
 * @(#)UserSearch.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.user.dto;

import org.nlh4j.core.pagination.AbstractPaginationSearchDto;

/**
 * See {@link AbstractPaginationSearchDto}, {@link UserDto} and {@link UserSearchConditions}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class UserSearch extends AbstractPaginationSearchDto<UserDto, UserSearchConditions> {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize an instance of {@link UserSearch}
     */
    public UserSearch() { super(new UserPagination()); }
}
