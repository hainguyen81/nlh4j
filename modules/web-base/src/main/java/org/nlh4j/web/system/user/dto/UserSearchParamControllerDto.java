/*
 * @(#)UserSearchParamControllerDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.user.dto;

import org.nlh4j.core.dto.BaseSearchParamControllerDto;

/**
 * See {@link BaseSearchParamControllerDto}, {@link UserSearchConditions}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class UserSearchParamControllerDto extends BaseSearchParamControllerDto<UserSearchConditions> {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * Initialize a new instance of {@link UserSearchParamControllerDto}
     *
     */
    public UserSearchParamControllerDto() {}
    /**
     * Initialize a new instance of {@link UserSearchParamControllerDto}
     *
     * @param c conditions
     * @param p page number
     */
    public UserSearchParamControllerDto(UserSearchConditions c, Integer p) { super(c, p); }
}
