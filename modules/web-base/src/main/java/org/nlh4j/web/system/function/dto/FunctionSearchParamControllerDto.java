/*
 * @(#)FunctionSearchParamControllerDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.function.dto;

import org.nlh4j.core.dto.BaseSearchParamControllerDto;

/**
 * See {@link BaseSearchParamControllerDto} and {@link FunctionSearchConditions}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class FunctionSearchParamControllerDto extends BaseSearchParamControllerDto<FunctionSearchConditions> {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * Initialize a new instance of {@link FunctionSearchParamControllerDto}
     *
     */
    public FunctionSearchParamControllerDto() {}
    /**
     * Initialize a new instance of {@link FunctionSearchParamControllerDto}
     *
     * @param c conditions
     * @param p page number
     */
    public FunctionSearchParamControllerDto(FunctionSearchConditions c, Integer p) { super(c, p); }
}
