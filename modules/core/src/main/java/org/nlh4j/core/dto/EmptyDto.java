/*
 * @(#)EmptyDto.java 1.0 Feb 28, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto;

import lombok.EqualsAndHashCode;

/**
 * Just empty DTO
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@EqualsAndHashCode(callSuper = false)
public final class EmptyDto extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;
}
