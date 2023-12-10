/*
 * @(#)UserUniqueDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.changepwd.dto;

import org.nlh4j.core.dto.AbstractDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * {@link UserDto} unique constraint DTO information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class UserUniqueDto extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
}
