/*
 * @(#)MessageService.java 1.0 Jun 1, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.service;

import java.io.Serializable;
import java.util.Locale;

import org.nlh4j.core.dto.NotificationDto.NotificationType;


/**
 * MessageService.javaのクラス。
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public interface MessageService extends Serializable {
	/**
	 * Get message content from resource based on system's locale
	 *
	 * @param key the message key
	 * @param args the message arguments
	 *
	 * @return the message from resource
	 */
	String getMessage(String key, Object... args);
	/**
	 * Get message content from resource based on system's locale
	 *
	 * @param key the message key
	 * @param args the message arguments
	 * @param defmessage default message in not found case
	 *
	 * @return the message from resource
	 */
	String getMessage(String key, Object[] args, String defmessage);
	/**
	 * Get message content from resource based on system's locale
	 *
	 * @param key the message key
	 * @param args the message arguments
	 * @param defmessage default message in not found case
	 * @param locale message locale
	 *
	 * @return the message from resource
	 */
	String getMessage(String key, Object[] args, String defmessage, Locale locale);

	/**
     * Send notification data to client via web socket
     *
     * @param broker the socket subscription destination
     * @param data the data to send
     */
    void sendMessage(String broker, Object data);
    /**
     * Send notification to client via web socket
     *
     * @param broker the socket subscription destination
     * @param type the notification type
     * @param message the notification message
     */
    void sendNotification(String broker, NotificationType type, String message);
    /**
     * Send info notification to client via web socket
     *
     * @param broker the socket subscription destination
     * @param message the notification message
     */
    void sendInfo(String broker, String message);
    /**
     * Send error notification to client via web socket
     *
     * @param broker the socket subscription destination
     * @param message the notification message
     */
    void sendError(String broker, String message);
    /**
     * Send warning notification to client via web socket
     *
     * @param broker the socket subscription destination
     * @param message the notification message
     */
    void sendWarning(String broker, String message);
}
