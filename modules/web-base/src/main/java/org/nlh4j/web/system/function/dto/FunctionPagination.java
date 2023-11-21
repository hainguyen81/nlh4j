/*
 * @(#)FunctionPagination.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.function.dto;

import org.nlh4j.core.pagination.AbstractPagingDto;
import org.nlh4j.web.base.common.AppConst;
import org.nlh4j.web.core.dto.FunctionDto;

/**
 * The {@link FunctionDto} paging information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class FunctionPagination extends AbstractPagingDto<FunctionDto> {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Initializes a new instance of {@link FunctionPagination}
     */
    protected FunctionPagination() {
        super(AppConst.findRecordsPerPage());
    }
}
