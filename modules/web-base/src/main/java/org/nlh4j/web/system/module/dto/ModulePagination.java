/*
 * @(#)ModulePagination.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.module.dto;

import org.nlh4j.core.pagination.AbstractPagingDto;
import org.nlh4j.web.base.common.AppConst;
import org.nlh4j.web.core.dto.ModuleDto;

/**
 * The {@link ModuleDto} paging information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class ModulePagination extends AbstractPagingDto<ModuleDto> {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Initializes a new instance of {@link ModulePagination}
     */
    protected ModulePagination() {
        super(AppConst.findRecordsPerPage());
    }
}
