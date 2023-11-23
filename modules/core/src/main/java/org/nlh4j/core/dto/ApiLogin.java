/*
 * @(#)ApiLogin.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API Login receiver
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ApiLogin extends AbstractDto {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    /** user name */
    @NotNull
    private String username;
    /** password */
    @NotNull
    private String password;
}
