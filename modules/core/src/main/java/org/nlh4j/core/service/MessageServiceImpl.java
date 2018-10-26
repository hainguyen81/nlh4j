/*
 * @(#)MessageServiceImpl.java 1.0 Jun 1, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.service;

import java.text.MessageFormat;
import java.util.Locale;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import org.nlh4j.core.context.profiles.SpringProfiles;
import org.nlh4j.core.context.profiles.SpringProfilesEnum;
import org.nlh4j.core.dto.NotificationDto;
import org.nlh4j.core.dto.NotificationDto.NotificationType;
import org.nlh4j.core.servlet.SpringContextHelper;

/**
 * MessageServiceImpl.javaのクラス。
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Service
public class MessageServiceImpl implements MessageService {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/** Logger. **/
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * {@link MessageSource}
	 */
	@Resource
	private MessageSource messageSource;

	/**
	 * {@link Environment}
	 */
	@Inject
	protected Environment environment;
	/**
	 * {@link SpringContextHelper}
	 */
	@Inject
	protected SpringContextHelper contextHelper;

	/**
	 * {@link SimpMessagingTemplate}
	 */
	private transient SimpMessagingTemplate messagingTemplate;

	/**
     * Get the messaging template for sending message
     *
     * @return the messaging template for sending message
     */
	@Profile(value = { SpringProfiles.PROFILE_SOCKET, SpringProfiles.PROFILE_FULL })
    protected final SimpMessagingTemplate getMessagingTemplate() {
        if (this.messagingTemplate == null && this.environment != null && this.contextHelper != null
                && SpringProfilesEnum.isProfile(SpringProfilesEnum.SOCKET, this.environment)) {
            this.messagingTemplate = this.contextHelper.searchBean(SimpMessagingTemplate.class);
        }
        return this.messagingTemplate;
    }

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.service.MessageService#getMessage(java.lang.String, java.lang.Object[])
	 */
	@Override
	public String getMessage(String key, Object... args) {
		return getMessage(key, args, key);
	}
	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.service.MessageService#getMessage(java.lang.String, java.lang.Object[], java.lang.String)
	 */
	@Override
	public String getMessage(String messageKey, Object[] args, String defmessage) {
		Locale locale = null;
		try {
			locale = LocaleContextHolder.getLocale();
			if (locale == null) {
				ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				if (sra != null) {
					locale = RequestContextUtils.getLocale(sra.getRequest());
				}
			}
		}
		catch (Throwable e) {
			logger.error(e.getMessage(), e);
			locale = null;
		}
		return this.getMessage(messageKey, args, defmessage, locale);
	}
	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.service.MessageService#getMessage(java.lang.String, java.lang.Object[], java.lang.String, java.util.Locale)
	 */
	@Override
	public String getMessage(String messageKey, Object[] args, String defmessage, Locale locale) {
		// checks parameters
		if (StringUtils.hasText(messageKey)) {
			try {
				locale = (locale == null ? Locale.getDefault() : locale);
				if (logger.isTraceEnabled()) {
    				logger.debug(MessageFormat.format(
    						"Get message from bundle with key [{0}] and locale [{1}]",
    						(messageKey == null ? "NULL" : messageKey),
    						(locale == null ? "NULL" : locale.getDisplayLanguage())));
				}
				return messageSource.getMessage(messageKey, args, locale);
			}
			catch (Throwable e) {
				logger.error(e.getMessage(), e);
			}
		}
		return defmessage;
	}

	/*
     * (Non-Javadoc)
     * @see org.nlh4j.common.service.MessageService#sendMessage(java.lang.String, java.lang.Object)
     */
    @Override
    public void sendMessage(String broker, Object data) {
        Assert.notNull(this.getMessagingTemplate(), "MessagingTemplate");
        Assert.hasText(broker, "NotificationBrokerDestination");
        Assert.notNull(data, "NotificationData");
        this.getMessagingTemplate().convertAndSend(broker, data);
    }
    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MessageService#sendNotification(java.lang.String, org.nlh4j.core.dto.NotificationDto.NotificationType, java.lang.String)
     */
    @Override
    public void sendNotification(String broker, NotificationType type, String message) {
        this.sendMessage(broker, new NotificationDto<String>(type, message));
    }
    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MessageService#sendInfo(java.lang.String, java.lang.String)
     */
    @Override
    public void sendInfo(String broker, String message) {
        this.sendNotification(broker, NotificationType.Info, message);
    }
    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MessageService#sendError(java.lang.String, java.lang.String)
     */
    @Override
    public void sendError(String broker, String message) {
        this.sendNotification(broker, NotificationType.Error, message);
    }
    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.MessageService#sendWarning(java.lang.String, java.lang.String)
     */
    @Override
    public void sendWarning(String broker, String message) {
        this.sendNotification(broker, NotificationType.Warning, message);
    }
}
