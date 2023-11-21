/*
 * @(#)RoleGroupDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.role.domain.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.entity.NamingType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.nlh4j.core.dto.AbstractDto;

/**
 * The role group information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@EqualsAndHashCode(callSuper = false)
@Entity(naming = NamingType.SNAKE_UPPER_CASE)
public class RoleGroup extends AbstractDto {

    /** */
	private static final long serialVersionUID = 1L;
	/** the group identity */
	@Getter
    @Setter
    private Long id;
    /** the group code */
	@Getter
    @Setter
    private String code;
    /** the group name */
	@Getter
    @Setter
    private String name;
    /** the group description */
	@Getter
    @Setter
    private String description;
    /** the role childs count **/
	@Getter
    @Setter
    private Long rolesCount;
}