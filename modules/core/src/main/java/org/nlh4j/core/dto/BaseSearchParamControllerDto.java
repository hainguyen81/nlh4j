/*
 * @(#)BaseSearchDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.util.NumberUtils;

/**
 * The bound class of searching entities conditions
 *
 * @param <T> the entity search conditions type
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseSearchParamControllerDto<T> extends AbstractDto {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /** search condition */
    private T c;
    /** current page number */
    private Integer p;
	/** the records number limit (such as records per page) **/
	private Integer limit;
    public BaseSearchParamControllerDto() {}
    public BaseSearchParamControllerDto(T c, Integer p) {
        this(c, p, 15);
    }
    public BaseSearchParamControllerDto(T c, Integer p, Integer limit) {
        this.c = c;
        this.p = p;
        this.limit = NumberUtils.max(new Integer[] { limit == null ? -1 : limit, -1 });
    }
}
