/*
 * @(#)MailService.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.service.mail;

import java.io.Serializable;
import java.util.List;

import org.nlh4j.core.dto.mail.Email;

/**
 * Service interface for sending email
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public interface MailService extends Serializable {
	/**
	 * Sends email using common email template
	 *
	 * @param email the {@link Email} to send
	 */
	void sendMail(Email email);

	/**
	 * Send multiple emails
	 * @param emails to send
	 */
	void sendMail(List<Email> emails);
}
