/*
 * @(#)UserEx.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.user.domain.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.entity.NamingType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.nlh4j.web.core.domain.entity.RoleGroup;

/**
 * An extended class of {@link org.nlh4j.web.core.domain.entity.UserEx}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@EqualsAndHashCode(callSuper = false)
@Entity(naming = NamingType.SNAKE_UPPER_CASE)
public class UserEx extends org.nlh4j.web.core.domain.entity.UserEx {

    /** */
    private static final long serialVersionUID = 1L;
    /** {@link RoleGroup} name */
    @Getter
    @Setter
    private String groupName;
}
