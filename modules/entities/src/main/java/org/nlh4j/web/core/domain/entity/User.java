/*
 * @(#)User.java
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
 * Contain system user information
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version $Revision:  $  $Date:  $
 */
@Entity(listener = UserListener.class)
@Table(name = "\\\"user\\\"")
public class User extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;

    /** USER identity */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long id;

    /** USER ROLES GROUP identity */
    @Column(name = "role_group_id")
    @Getter
    @Setter
    private Long gid;

    /** EMPLOYEE identity */
    @Column(name = "employee_id")
    @Getter
    @Setter
    private Long eid;

    /** Login user name */
    @Column(name = "user_name")
    @Getter
    @Setter
    private String userName;

    /** Login user MD5 password */
    @Column(name = "password")
    @Getter
    @Setter
    private String password;

    /** Login user email */
    @Column(name = "email")
    @Getter
    @Setter
    private String email;

    /** Specify this user whether can login */
    @Column(name = "enabled")
    @Getter
    @Setter
    private Boolean enabled;

    /** Specify this user whether is system administrator */
    @Column(name = "sysadmin")
    @Getter
    @Setter
    private Boolean sysadmin;

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