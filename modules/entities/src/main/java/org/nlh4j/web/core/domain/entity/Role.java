/*
 * @(#)Role.java 1.0 Jun 1, 2015
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
 * MODULE ROLE PERMISSIONS
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version $Revision:  $  $Date:  $
 */
@Entity(listener = RoleListener.class)
@Table(name = "\\\"right\\\"")
public class Role extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;

    /** Role identity */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long id;

    /** MODULE identity */
    @Column(name = "module_id")
    @Getter
    @Setter
    private Long mid;

    /** ROLES GROUP identity */
    @Column(name = "role_id")
    @Getter
    @Setter
    private Long gid;

    /** insertable permission */
    @Column(name = "functions")
    @Getter
    @Setter
    private String functions;

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