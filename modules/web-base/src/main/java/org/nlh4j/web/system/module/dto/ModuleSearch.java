/*
 * @(#)ModuleSearch.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.module.dto;

import org.nlh4j.core.pagination.AbstractPaginationSearchDto;
import org.nlh4j.web.core.dto.ModuleDto;

/**
 * See {@link AbstractPaginationSearchDto}, {@link ModuleDto} and {@link ModuleSearchConditions}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class ModuleSearch extends AbstractPaginationSearchDto<ModuleDto, ModuleSearchConditions> {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize an instance of {@link ModuleSearch}
     */
    public ModuleSearch() { super(new ModulePagination()); }
}
