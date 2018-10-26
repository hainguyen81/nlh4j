/*
 * @(#)FunctionEntityParamControllerDto.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.function.dto;

import org.nlh4j.core.dto.BaseEntityParamControllerDto;

/**
 * See {@link BaseEntityParamControllerDto} and {@link FunctionUniqueDto}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class FunctionEntityParamControllerDto extends BaseEntityParamControllerDto<FunctionUniqueDto> {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * Initialize a new instance of {@link FunctionEntityParamControllerDto}
     *
     */
    public FunctionEntityParamControllerDto() {}
    /**
     * Initialize a new instance of {@link FunctionEntityParamControllerDto}
     *
     * @param pk unique primary
     */
    public FunctionEntityParamControllerDto(FunctionUniqueDto pk) { super(pk); }
}
