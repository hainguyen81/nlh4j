/*
 * @(#)AbstractSearchConditionsDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.common.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.web.base.common.AppConst;
import org.nlh4j.web.base.util.SessionUtils;

/**
 * The base search conditions DTO information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractSearchConditionsDto extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Get current logged-in user name as search conditions
     *
     * @return the current logged-in user name
     */
    @JsonIgnore()
    public final String getUsername() {
        return SessionUtils.getUsername();
    }

    /** LIKE operator */

    /**
     * Get a boolean value indicating that LIKE operator whether is '%abc'
     * @return true for prefix; else false
     */
    @JsonIgnore()
    public final boolean isPrefix() {
        return AppConst.findLikeOperator().isPrefix();
    }
    /**
     * Get a boolean value indicating that LIKE operator whether is 'abc%'
     * @return true for suffix; else false
     */
    @JsonIgnore()
    public final boolean isSuffix() {
        return AppConst.findLikeOperator().isSuffix();
    }
    /**
     * Get a boolean value indicating that LIKE operator whether is '%abc%'
     * @return true for contain; else false
     */
    @JsonIgnore()
    public final boolean isContain() {
        return AppConst.findLikeOperator().isContain();
    }
}
