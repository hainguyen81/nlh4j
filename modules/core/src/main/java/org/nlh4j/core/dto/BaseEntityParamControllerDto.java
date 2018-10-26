/*
 * @(#)BaseEntityPkDto.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The bound class of entity primary/unique key
 *
 * @param <T> the entity unique key type
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseEntityParamControllerDto<T> extends AbstractDto {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /** entity primary/unique key */
    private T pk;
    public BaseEntityParamControllerDto() {}
    public BaseEntityParamControllerDto(T pk) {
        this.pk = pk;
    }
}
