/*
 * @(#)ModuleEntityParamControllerDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.module.dto;

import org.nlh4j.core.dto.BaseEntityParamControllerDto;

/**
 * See {@link BaseEntityParamControllerDto} and {@link ModuleUniqueDto}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class ModuleEntityParamControllerDto extends BaseEntityParamControllerDto<ModuleUniqueDto> {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * Initialize a new instance of {@link ModuleEntityParamControllerDto}
     *
     */
    public ModuleEntityParamControllerDto() {}
    /**
     * Initialize a new instance of {@link ModuleEntityParamControllerDto}
     *
     * @param pk unique primary
     */
    public ModuleEntityParamControllerDto(ModuleUniqueDto pk) { super(pk); }
}
