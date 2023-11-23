/*
 * @(#)FunctionSearchConditions.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.function.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.web.base.common.controller.AbstractSearchConditionsDto;
import org.nlh4j.web.core.domain.entity.Function;

/**
 * {@link Function} search conditions
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FunctionSearchConditions extends AbstractSearchConditionsDto {

    /** */
    private static final long serialVersionUID = 1L;
    /** group code/name keyword */
    private String keyword;
    private Boolean enabled;
}
