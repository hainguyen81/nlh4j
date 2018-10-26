/*
 * @(#)FieldErrorResource.java 1.0 Feb 15, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.validation.errors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.core.dto.AbstractDto;

/**
 * Validation field error resource information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FieldErrorResource extends AbstractDto {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    private String resource;
    private String field;
    private String code;
    private String message;
    private Object rejectedValue;
    private boolean bindingFailure;
}
