/*
 * @(#)NotificationSocketDto.java 1.0 Mar 6, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.notification.dto;

import org.springframework.messaging.MessageHeaders;

import org.nlh4j.core.dto.AbstractSocketDto;
import org.nlh4j.core.dto.NotificationDto;
import org.nlh4j.core.dto.UserDetails;

/**
 * Socket notification DTO
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class NotificationSocketDto extends AbstractSocketDto<NotificationDto<String>> {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

	/**
	 * Initialize a new instance of {@link NotificationSocketDto}
	 *
	 * @param sender the authentication of the socket sender
	 * @param headers the received message headers
	 * @param original the received socket data
	 */
	public NotificationSocketDto(UserDetails sender, MessageHeaders headers, NotificationDto<String> original) {
		super(sender, headers, original);
	}
}
