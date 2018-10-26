/*
 * @(#)Function.java 1.0 Jun 1, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.domain.entity;

import java.sql.Timestamp;

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
 * FUNCTION PERMISSIONS
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version $Revision:  $  $Date:  $
 */
@Entity(listener = FunctionListener.class)
@Table(name = "\\\"function\\\"")
public class Function extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;

    /** Function identity */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long id;

    /** Function code */
    @Column(name = "code")
    @Getter
    @Setter
    private String code;

    /** Function name */
    @Column(name = "name")
    @Getter
    @Setter
    private String name;

    /** FUNCTION name key from language resource file */
    @Column(name = "lang_key")
    @Getter
    @Setter
    private String langKey;

    /** Specify this module whether is enabled */
    @Column(name = "enabled")
    @Getter
    @Setter
    private Boolean enabled;

    /** FUNCTION display order */
    @Column(name = "func_order")
    @Getter
    @Setter
    private Integer funcOrder;

    /** */
    @Column(name = "created_at")
    @Getter
    @Setter
    private Timestamp createdAt;

    /** */
    @Column(name = "updated_at")
    @Getter
    @Setter
    private Timestamp updatedAt;

    /** */
    @Column(name = "created_user")
    @Getter
    @Setter
    private Long createdUser;

    /** */
    @Column(name = "updated_user")
    @Getter
    @Setter
    private Long updatedUser;
}