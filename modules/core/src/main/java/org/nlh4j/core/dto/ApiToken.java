/*
 * @(#)ApiToken.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API Response JWT Token
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ApiToken extends AbstractDto {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    /** API login successful token */
    private String token;
}
