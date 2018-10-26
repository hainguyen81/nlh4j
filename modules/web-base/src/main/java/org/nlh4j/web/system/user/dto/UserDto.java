/*
 * @(#)UserDto.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.user.dto;

import java.util.Locale;

import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.entity.NamingType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.nlh4j.util.LocaleUtils;
import org.nlh4j.web.base.util.SessionUtils;
import org.nlh4j.web.core.domain.entity.RoleGroup;

/**
 * An extended class of {@link org.nlh4j.web.core.dto.UserDto}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@EqualsAndHashCode(callSuper = false)
@Entity(naming = NamingType.SNAKE_UPPER_CASE)
public class UserDto extends org.nlh4j.web.core.domain.entity.UserEx {

    /** */
    private static final long serialVersionUID = 1L;
    /** {@link RoleGroup} name */
    @Getter
    @Setter
    private String groupName;
    /** Specify whether changing password */
    @Getter
    @Setter
    private boolean changepwd = false;

    /* (Non-Javadoc)
     * @see org.nlh4j.core.dto.UserDetails#getLocale()
     */
    @JsonIgnore
    public Locale getLocale() {
        Locale locale = null;
        try { locale = LocaleUtils.toLocale(super.getLanguage()); }
        catch(Exception e) { locale = null; }
        return locale;
    }
    public String getDisplayLanguage() {
        Locale locale = this.getLocale();
        return (locale == null ? "" : locale.getDisplayLanguage());
    }

    /**
     * Get a boolean value indicating that this information whether is current logged-in user
     *
     * @return true for current user; else false
     */
    public boolean isCurrentUser() {
        return SessionUtils.getUid().equals(super.getId());
    }
}
