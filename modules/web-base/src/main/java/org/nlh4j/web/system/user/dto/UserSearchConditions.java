/*
 * @(#)UserSearchConditions.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.system.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.web.base.common.controller.AbstractSearchConditionsDto;
import org.nlh4j.web.core.domain.entity.User;

/**
 * {@link User} search conditions
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserSearchConditions extends AbstractSearchConditionsDto {

    /** */
    private static final long serialVersionUID = 1L;
    private String keyword;
    private Boolean enabled;
    private Boolean expired;
    private Boolean sysAdmin;
}
