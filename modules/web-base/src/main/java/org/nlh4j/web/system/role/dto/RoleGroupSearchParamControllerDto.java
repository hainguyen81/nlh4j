/*
 * @(#)RoleGroupSearchDto.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.role.dto;

import org.nlh4j.core.dto.BaseSearchParamControllerDto;

/**
 * See {@link BaseSearchParamControllerDto}, {@link RoleGroupSearchConditions}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class RoleGroupSearchParamControllerDto extends BaseSearchParamControllerDto<RoleGroupSearchConditions> {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * Initialize a new instance of {@link RoleGroupSearchParamControllerDto}
     *
     */
    public RoleGroupSearchParamControllerDto() {}
    /**
     * Initialize a new instance of {@link RoleGroupSearchParamControllerDto}
     *
     * @param c conditions
     * @param p page number
     */
    public RoleGroupSearchParamControllerDto(RoleGroupSearchConditions c, Integer p) { super(c, p); }
}
