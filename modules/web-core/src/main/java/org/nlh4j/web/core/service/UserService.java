/*
 * @(#)UserService.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.service;

import java.util.Locale;

import org.nlh4j.web.core.domain.entity.User;
import org.nlh4j.web.core.dto.UserDto;

/**
 * The service interface of {@link User}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public interface UserService extends org.nlh4j.core.service.UserService {
    /**
     * Notification socket
     */
    void broadcastOnlineUsers();
    /**
     * Change password of the specified user
     *
     * @param user user to update
     * @param newPassword new password
     *
     * @return new user DTO information
     * @throws ReflectiveOperationException if fail
     */
    UserDto changePassword(UserDto user, String newPassword) throws Exception;
    /**
     * Update {@link Locale} for the specified {@link User} identity
     *
     * @param uid {@link User} identity to update
     * @param locale {@link Locale}
     * @param updater the updater
     *
     * @return true for updating successful; else false
     */
    boolean updateLocale(Long uid, Locale locale, Long updater);
}
