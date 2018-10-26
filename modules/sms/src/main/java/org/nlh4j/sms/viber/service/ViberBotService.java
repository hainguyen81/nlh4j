/*
 * @(#)ViberBotService.java 1.0 Feb 4, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.sms.viber.service;

import java.util.List;

import com.viber.bot.api.ViberBot;
import com.viber.bot.profile.UserProfile;

/**
 * {@link ViberBot} service
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public interface ViberBotService {
	/**
	 * Send message
	 * @param profiles receivers
	 * @param messages to send
	 */
	void sendMessage(List<UserProfile> profiles, String...messages);
	/**
	 * Send message
	 * @param profile receiver
	 * @param messages to send
	 */
	void sendMessage(UserProfile profile, String...messages);
	/**
	 * Send message
	 * @param profile receiver
	 * @param messageKey message key from resource to send
	 * @param args message arguments
	 */
	void sendMessage(UserProfile profile, String messageKey, Object...args);
}
