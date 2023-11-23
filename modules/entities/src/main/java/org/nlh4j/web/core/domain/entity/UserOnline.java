/*
 * @(#)UserOnline.java
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
 * Contain USER ONLINE information
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version $Revision:  $  $Date:  $
 */
@Entity(listener = UserOnlineListener.class)
@Table(name = "user_online")
public class UserOnline extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;

    /** USER ONLINE identity */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long id;

    /** USER identity */
    @Column(name = "uid")
    @Getter
    @Setter
    private Long uid;

    /** USER login date */
    @Column(name = "login_at")
    @Getter
    @Setter
    private Timestamp loginAt;

    /** USER logout date */
    @Column(name = "logout_at")
    @Getter
    @Setter
    private Timestamp logoutAt;
}