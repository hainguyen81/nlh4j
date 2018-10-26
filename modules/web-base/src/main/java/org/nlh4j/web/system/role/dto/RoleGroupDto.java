/*
 * @(#)RoleGroupDto.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.role.dto;

import java.text.MessageFormat;
import java.util.List;

import org.springframework.util.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.web.core.dto.RoleDto;
import org.nlh4j.web.system.role.domain.entity.RoleGroup;

/**
 * The role group information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleGroupDto extends RoleGroup {

    /** */
	private static final long serialVersionUID = 1L;
	public String getCodeNameDisplay() {
        if (!StringUtils.hasText(this.getCode())) return null;
        return MessageFormat.format("{0} - {1}", this.getCode(), this.getName());
    }
    /** the role childs for inserting/updating **/
    private List<RoleDto> roles;
}