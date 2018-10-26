/*
 * @(#)RoleGroupSearchConditions.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.role.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.web.base.common.controller.AbstractSearchConditionsDto;
import org.nlh4j.web.core.domain.entity.RoleGroup;

/**
 * {@link RoleGroup} search conditions
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleGroupSearchConditions extends AbstractSearchConditionsDto {

    /** */
    private static final long serialVersionUID = 1L;
    /** group code/name keyword */
    private String keyword;
}
