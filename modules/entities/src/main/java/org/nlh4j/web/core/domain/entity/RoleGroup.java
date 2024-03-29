/*
 * @(#)RoleGroup.java
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
 * Contain MODULE ROLES GROUP information
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version $Revision:  $  $Date:  $
 */
@Entity(listener = RoleGroupListener.class)
@Table(name = "\\\"role\\\"")
public class RoleGroup extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;

    /** Roles group identity */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long id;

    /** Roles group code */
    @Column(name = "role_code")
    @Getter
    @Setter
    private String code;

    /** Roles group name */
    @Column(name = "role_name")
    @Getter
    @Setter
    private String name;

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