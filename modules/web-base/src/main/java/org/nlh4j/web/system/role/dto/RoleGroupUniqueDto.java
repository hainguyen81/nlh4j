/*
 * @(#)RoleGroupUniqueDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.role.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.web.core.domain.entity.RoleGroup;

/**
 * The unique information of {@link RoleGroup}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleGroupUniqueDto extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String code;
}
