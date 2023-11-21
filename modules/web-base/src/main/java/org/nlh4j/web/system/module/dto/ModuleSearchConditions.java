/*
 * @(#)ModuleSearchConditions.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.module.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.web.base.common.controller.AbstractSearchConditionsDto;
import org.nlh4j.web.core.domain.entity.Module;

/**
 * {@link Module} search conditions
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ModuleSearchConditions extends AbstractSearchConditionsDto {

    /** */
    private static final long serialVersionUID = 1L;
    private String keyword;
    private Boolean enabled;
    private Boolean common;
    private Boolean service;
    private Boolean visibled;
}
