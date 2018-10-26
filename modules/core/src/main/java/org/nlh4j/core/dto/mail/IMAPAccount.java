/*
 * @(#)IMAPAccount.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto.mail;

/**
 * The IMAP email information for logging-in email account
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public class IMAPAccount extends Account {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Initialize a new instance of {@link IMAPAccount}
	 *
	 * @param protocol the server mail protocol
     * @param secure specify server mail whether is secured
     * @param port the server mail port
	 */
	protected IMAPAccount(String protocol, boolean secure, int port) {
		super(protocol, secure, port);
	}
}