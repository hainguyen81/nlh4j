/*
 * @(#)UserEntityParamControllerDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.changepwd.dto;

import org.nlh4j.core.dto.BaseEntityParamControllerDto;

/**
 * See {@link BaseEntityParamControllerDto} and {@link UserUniqueDto}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class UserEntityParamControllerDto extends BaseEntityParamControllerDto<UserUniqueDto> {

    /** */
    private static final long serialVersionUID = 1L;
    /** */
    public UserEntityParamControllerDto() {}
    /**
     * Initialize a new instance of {@link UserEntityParamControllerDto}
     *
     * @param pk unique primary key
     */
    public UserEntityParamControllerDto(UserUniqueDto pk) { super(pk); }
}
