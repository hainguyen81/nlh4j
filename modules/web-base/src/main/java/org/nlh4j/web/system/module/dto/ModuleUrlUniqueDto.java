/*
 * @(#)ModuleUrlUniqueDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.module.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.web.core.domain.entity.Module;

/**
 * {@link Module} URL unique constraint DTO information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ModuleUrlUniqueDto extends ModuleUniqueDto {

    /** */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String url;
}
