/*
 * @(#)RoleDto.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.dto;

import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.entity.NamingType;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.util.StringUtils;

/**
 * Module ROLE DTO information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@EqualsAndHashCode(callSuper = false)
@Entity(naming = NamingType.SNAKE_UPPER_CASE)
public class RoleDto extends AbstractDto implements GrantedAuthority {

    /** */
    private static final long serialVersionUID = 1L;
    /** Role identity */
    @Getter
    @Setter
    private Long id;
    /** function permission */
    @Setter
    @Getter
    private String functions;
    @JsonIgnore
    public String[] getFunctionPermissions() {
        if (StringUtils.hasText(this.functions)) {
            return org.springframework.util.StringUtils.delimitedListToStringArray(this.functions, "|");
        }
        return new String[] {};
    }
    /** MODULE identity */
    @Getter
    @Setter
    private Long mid;
    /** MODULE code */
    @Getter
    @Setter
    private String moduleCd;
    /** MODULE name */
    @Getter
    @Setter
    private String moduleName;
    /** MODULE language resource key */
    @Getter
    @Setter
    private String moduleLangKey;
    /** MODULE URL */
    @Getter
    @Setter
    private String moduleLink;
    /** MODULE Description */
    @Getter
    @Setter
    private String moduleDescription;
    /** MODULE ROLES GROUP identity */
    @Getter
    @Setter
    private Long gid;
    /** MODULE ROLES GROUP code */
    @Getter
    @Setter
    private String groupCd;
    /** MODULE ROLES GROUP name */
    @Getter
    @Setter
    private String groupName;

    /*
     * (Non-Javadoc)
     * @see org.springframework.security.core.GrantedAuthority#getAuthority()
     */
    @Override
    @JsonIgnore
    public String getAuthority() {
        return this.getModuleCd();
    }
}
