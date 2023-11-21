/*
 * @(#)ModuleDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.dto;

import java.text.MessageFormat;

import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.entity.NamingType;
import org.springframework.util.StringUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.nlh4j.core.dto.AbstractDto;

/**
 * Module DTO information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@EqualsAndHashCode(callSuper = false)
@Entity(naming = NamingType.SNAKE_UPPER_CASE)
public class ModuleDto extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;
    /** MODULE identity */
    @Getter
    @Setter
    private Long id;
    /** PARENT MODULE identity */
    @Getter
    @Setter
    private Long pid;
    /** MODULE code */
    @Getter
    @Setter
    private String code;
    /** MODULE name */
    @Getter
    @Setter
    private String name;
    /** MODULE name key from language resource file */
    @Getter
    @Setter
    private String langKey;
    /** MODULE display order */
    @Getter
    @Setter
    private Integer modOrder;
    /** Specify this module whether is enabled */
    @Getter
    @Setter
    private Boolean enabled;
    /** Specify this module whether is visibled, only when this is not a serviced module */
    @Getter
    @Setter
    private Boolean visibled;
    /** Specify this module whether is a serviced module */
    @Getter
    @Setter
    private Boolean service;
    /** Specify this module whether is common module. This's mean force permissions for all users */
    @Getter
    @Setter
    private Boolean common;
    /** MODULE URL regular expression */
    @Getter
    @Setter
    private String urlRegex;
    /** MODULE URL */
    @Getter
    @Setter
    private String mainUrl;
    /** MODULE stylesheet */
    @Getter
    @Setter
    private String css;
    /** MODULE description */
    @Getter
    @Setter
    private String description;
    /** MODULE JAR library */
    @Getter
    @Setter
    private String library;
    /** MODULE CHILDS count */
    @Getter
    @Setter
    private Long childs;
    /** MODULE depth level */
    @Getter
    @Setter
    private Long depth;
    /** MODULE JAR library */
    @Getter
    @Setter
    private boolean leaf = true;
    /** function permissions */
    @Getter
    @Setter
    private String functions;
    /** MODULE FORWARD URL */
    @Getter
    @Setter
    private String forwardUrl;
    /** Client HTML template file */
    @Getter
    @Setter
    private String templateUrl;
    /** Angular client controller name */
    @Getter
    @Setter
    private String ngcontroller;

    public String getDisplay() {
        if (!StringUtils.hasText(this.getLangKey())) return this.getName();
        return super.getMessageService().getMessage(this.getLangKey());
    }
    public String getCodeNameDisplay() {
        if (!StringUtils.hasText(this.getCode())) return null;
        String name = this.getName();
        if (StringUtils.hasText(this.getLangKey())) {
            name = super.getMessageService().getMessage(this.getLangKey());
        }
        return MessageFormat.format("{0} - {1}", this.getCode(), name);
    }
    public String getLink() {
        if (!StringUtils.hasText(this.getMainUrl())) return "javascript:void(0);";
        return this.getMainUrl();
    }
}
