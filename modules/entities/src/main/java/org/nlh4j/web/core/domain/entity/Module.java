/*
 * @(#)Module.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.domain.entity;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;
import org.seasar.doma.Table;

import lombok.Getter;
import lombok.Setter;
import org.nlh4j.core.dto.AbstractDto;


/**
 * Contain MODULE information
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version $Revision:  $  $Date:  $
 */
@Entity(listener = ModuleListener.class)
@Table(name = "module")
public class Module extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;

    /** MODULE identity */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long id;

    /** PARENT MODULE identity */
    @Column(name = "parent_id")
    @Getter
    @Setter
    private Long pid;

    /** MODULE code */
    @Column(name = "code")
    @Getter
    @Setter
    private String code;

    /** MODULE name */
    @Column(name = "name")
    @Getter
    @Setter
    private String name;

    /** MODULE name key from language resource file */
    @Column(name = "lang_key")
    @Getter
    @Setter
    private String langKey;

    /** MODULE display order */
    @Column(name = "mod_order")
    @Getter
    @Setter
    private Integer modOrder;

    /** Specify this module whether is enabled */
    @Column(name = "enabled")
    @Getter
    @Setter
    private Boolean enabled;

    /** Specify this module whether is visibled, only when this is not a serviced module */
    @Column(name = "visibled")
    @Getter
    @Setter
    private Boolean visibled;

    /** Specify this module whether is a serviced module */
    @Column(name = "service")
    @Getter
    @Setter
    private Boolean service;

    /** Specify this module whether is common module. This's mean force permissions for all users */
    @Column(name = "common")
    @Getter
    @Setter
    private Boolean common;

    /** MODULE URL regular expression */
    @Column(name = "link_regex")
    @Getter
    @Setter
    private String urlRegex;

    /** MODULE URL */
    @Column(name = "main_link")
    @Getter
    @Setter
    private String mainUrl;

    /** MODULE stylesheet */
    @Column(name = "css")
    @Getter
    @Setter
    private String css;

    /** MODULE description */
    @Column(name = "description")
    @Getter
    @Setter
    private String description;

    /** MODULE JAR library */
    @Column(name = "library")
    @Getter
    @Setter
    private String library;

    /** URL to forward */
    @Column(name = "forward_url")
    @Getter
    @Setter
    private String forwardUrl;

    /** Client HTML template file */
    @Column(name = "template_url")
    @Getter
    @Setter
    private String templateUrl;

    /** Angular client controller name */
    @Column(name = "ngcontroller")
    @Getter
    @Setter
    private String ngcontroller;
}