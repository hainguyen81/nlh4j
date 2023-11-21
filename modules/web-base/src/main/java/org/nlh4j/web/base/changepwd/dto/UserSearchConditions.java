/*
 * @(#)UserSearchConditions.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.changepwd.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.core.dto.AbstractDto;

/**
 * {@link UserDto} search conditions
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserSearchConditions extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;
    private String username;
}
