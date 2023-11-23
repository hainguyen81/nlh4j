/*
 * @(#)FunctionUniqueDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.function.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.web.core.domain.entity.Function;

/**
 * The unique information of {@link Function}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FunctionUniqueDto extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String code;
}
