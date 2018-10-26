/*
 * @(#)FunctionSearch.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.function.dto;

import org.nlh4j.core.pagination.AbstractPaginationSearchDto;
import org.nlh4j.web.core.dto.FunctionDto;

/**
 * See {@link AbstractPaginationSearchDto}, {@link FunctionDto} and {@link FunctionSearchConditions}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class FunctionSearch extends AbstractPaginationSearchDto<FunctionDto, FunctionSearchConditions> {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize an instance of {@link FunctionSearch}
     */
    public FunctionSearch() { super(new FunctionPagination()); }
}
