/*
 * @(#)ModuleSearchParamControllerDto.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.module.dto;

import org.nlh4j.core.dto.BaseSearchParamControllerDto;

/**
 * See {@link BaseSearchParamControllerDto} and {@link ModuleSearchConditions}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class ModuleSearchParamControllerDto extends BaseSearchParamControllerDto<ModuleSearchConditions> {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * Initialize a new instance of {@link ModuleSearchParamControllerDto}
     *
     */
    public ModuleSearchParamControllerDto() {}
    /**
     * Initialize a new instance of {@link ModuleSearchParamControllerDto}
     *
     * @param c conditions
     * @param p page number
     */
    public ModuleSearchParamControllerDto(ModuleSearchConditions c, Integer p) { super(c, p); }
}
