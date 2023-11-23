/*
 * @(#)RoleGroupEntityPkDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.role.dto;

import org.nlh4j.core.dto.BaseEntityParamControllerDto;

/**
 * See {@link BaseEntityParamControllerDto}, {@link RoleGroupUniqueDto}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class RoleGroupEntityParamControllerDto extends BaseEntityParamControllerDto<RoleGroupUniqueDto> {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * Initialize a new instance of {@link RoleGroupEntityParamControllerDto}
     *
     */
    public RoleGroupEntityParamControllerDto() {}
    /**
     * Initialize a new instance of {@link RoleGroupEntityParamControllerDto}
     *
     * @param pk unique primary key
     */
    public RoleGroupEntityParamControllerDto(RoleGroupUniqueDto pk) { super(pk); }
}
