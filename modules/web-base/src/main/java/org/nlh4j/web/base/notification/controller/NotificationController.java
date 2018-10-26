/*
 * @(#)NotificationController.java 1.0 Mar 6, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.notification.controller;

import java.security.Principal;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.nlh4j.core.controller.AbstractSocketController;
import org.nlh4j.core.dto.NotificationDto;
import org.nlh4j.web.base.notification.dto.NotificationSocketDto;

/**
 * Notification controller by socket
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Controller
@RequestMapping(value = "/socket")
public class NotificationController extends AbstractSocketController<NotificationDto<String>, NotificationSocketDto> {

	/**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
	 * Initialize a new instance of {@link NotificationController}
	 */
	public NotificationController() {
		super(NotificationSocketDto.class);
	}

	/**
	 * Send notification from broacast to all
	 *
	 * @param notification the received message payload
	 * @param messageHeaders the received message headers
	 * @param principal the sender that had been sent this notification
	 *
	 * @return the notification outbound DTO
	 */
	@MessageMapping(value = { "/notify" })
    @SendTo("/topic/notify")
    public NotificationSocketDto notify(
    		@Payload NotificationDto<String> notification
    		, @Headers MessageHeaders messageHeaders
    		, Principal principal) {
    	return super.createSocketDto(notification, messageHeaders, principal);
    }
}
