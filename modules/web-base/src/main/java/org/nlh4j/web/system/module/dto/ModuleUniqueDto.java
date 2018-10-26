/*
 * @(#)ModuleUniqueDto.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.module.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.web.core.domain.entity.Module;

/**
 * {@link Module} unique constraint DTO information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ModuleUniqueDto extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String code;
}
