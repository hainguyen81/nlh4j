/*
 * @(#)UserDto.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.changepwd.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User DTO information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserDto extends org.nlh4j.web.core.dto.UserDto {

    /** */
    private static final long serialVersionUID = 1L;
    private String newPassword;
    private String retypePassword;
    private String currentPassword;
    /* (Non-Javadoc)
     * @see org.nlh4j.web.core.dto.UserDto#getPassword()
     */
    @Override
    @JsonIgnore
    public String getPassword() {
        return super.getPassword();
    }
}
