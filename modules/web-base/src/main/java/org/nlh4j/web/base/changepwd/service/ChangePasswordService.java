/*
 * @(#)ChangePasswordService.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.changepwd.service;

import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.service.MasterService;
import org.nlh4j.web.base.changepwd.dto.UserDto;
import org.nlh4j.web.base.changepwd.dto.UserUniqueDto;

/**
 * The service interface of {@link UserDto}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public interface ChangePasswordService
    extends MasterService<UserDto, AbstractDto, UserUniqueDto> {}
