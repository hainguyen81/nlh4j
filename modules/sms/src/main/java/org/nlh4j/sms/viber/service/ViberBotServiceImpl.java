/*
 * @(#)ViberBotServiceImpl.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.sms.viber.service;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Singleton;

import com.viber.bot.ViberSignatureValidator;
import com.viber.bot.api.ViberBot;
import com.viber.bot.message.TextMessage;
import com.viber.bot.profile.BotProfile;
import com.viber.bot.profile.UserProfile;

import org.nlh4j.core.service.AbstractService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import lombok.Getter;
import lombok.Setter;

/**
 * The implement of {@link ViberBotService} interface
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Service
@Singleton
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ViberBotServiceImpl extends AbstractService implements ViberBotService {

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link ViberBot} authentication token
	 */
	@Getter
	@Setter
	private String authenticationToken;
	/**
	 * {@link ViberBot} profile name
	 */
	@Getter
	@Setter
	private String profileName;
	/**
	 * {@link ViberBot} profile avatar
	 */
	@Getter
	@Setter
	private String profileAvatar;

	/**
	 * {@link ViberSignatureValidator}
	 */
	private transient ViberSignatureValidator signatureValidator;
	/**
	 * Get {@link ViberSignatureValidator}
	 *
	 * @return {@link ViberSignatureValidator}
	 */
	protected final ViberSignatureValidator getSignatureValidator() {
		if (this.signatureValidator == null) {
			try {
				this.signatureValidator = new ViberSignatureValidator(this.getAuthenticationToken());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				this.signatureValidator = null;
			}
		}
		if (this.signatureValidator == null) return null;
		synchronized (this.signatureValidator) {
			return this.signatureValidator;
		}
	}

	/**
	 * {@link ViberBot}
	 */
	private transient ViberBot viberBot;
	/**
	 * Get {@link ViberBot}
	 *
	 * @return {@link ViberBot}
	 */
	protected final ViberBot getBot() {
		if (this.viberBot == null) {
			try {
				this.viberBot = new ViberBot(
						new BotProfile(this.getProfileName(), this.getProfileAvatar()),
						this.getAuthenticationToken());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				this.viberBot = null;
			}
		}
		if (this.viberBot == null) return null;
		synchronized (this.viberBot) {
			return this.viberBot;
		}
	}
	/* (Non-Javadoc)
	 * @see org.nlh4j.sms.viber.service.ViberBotService#sendMessage(com.viber.bot.profile.UserProfile, java.lang.String[])
	 */
	@Override
	public void sendMessage(UserProfile profile, String... messages) {
		Assert.notNull(profile, "Viber Profile");
		Assert.notEmpty(messages, "messages");
		Assert.notNull(this.getBot(), "Viber Bot");
		List<TextMessage> msgLst = new LinkedList<TextMessage>();
		for(String message : messages) {
			msgLst.add(new TextMessage(message));
		}
		this.getBot().sendMessage(profile, msgLst.toArray(new TextMessage[msgLst.size()]));
	}
	/* (Non-Javadoc)
	 * @see org.nlh4j.sms.viber.service.ViberBotService#sendMessage(java.util.List, java.lang.String[])
	 */
	@Override
	public void sendMessage(List<UserProfile> profiles, String... messages) {
		Assert.notEmpty(profiles, "Viber Profiles");
		Assert.notEmpty(messages, "messages");
		Assert.notNull(this.getBot(), "Viber Bot");
		for(UserProfile profile : profiles) {
			this.sendMessage(profile, messages);
		}
	}

	/* (Non-Javadoc)
	 * @see org.nlh4j.sms.viber.service.ViberBotService#sendMessage(com.viber.bot.profile.UserProfile, java.lang.String, java.lang.Object[])
	 */
	@Override
	public void sendMessage(UserProfile profile, String messageKey, Object... args) {
		Assert.notNull(profile, "Viber Profile");
		Assert.hasText(messageKey, "message");
		this.sendMessage(profile, super.messageService.getMessage(messageKey, args));
	}
}
