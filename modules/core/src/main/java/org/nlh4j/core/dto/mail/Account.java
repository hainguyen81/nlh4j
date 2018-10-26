/*
 * @(#)Account.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto.mail;

import java.util.Properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.util.StringUtils;

/**
 * The email information for logging-in email account
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@EqualsAndHashCode(callSuper = false)
public abstract class Account extends AbstractDto {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/** IMAP protocol */
	public static final String PROTOCOL_IMAP = "imap";
	/** POP3 protocol */
	public static final String PROTOCOL_POP = "pop3";
	/** SMTP protocol */
	public static final String PROTOCOL_SMTP = "smtp";
	/** IMAP protocol port */
	public static final int PORT_IMAP_DEFAULT = 143;
	/** IMAP(s) protocol port */
	public static final int PORT_IMAPS_DEFAULT = 993;
	/** POP3 protocol port */
	public static final int PORT_POP_DEFAULT = 110;
	/** POP3(s) protocol port */
	public static final int PORT_POPS_DEFAULT = 995;
	/** SMTP protocol port */
	public static final int PORT_SMTP_DEFAULT = 25;
	/** SMTP(s) protocol port */
	public static final int PORT_SMTPS_DEFAULT = 465;

	/** account server mail host address */
	@Getter
	private String host;
	/** account mail password */
	@Getter
	@Setter
    private String password;
    /** account server mail port */
	@Getter
    private int port;
    /** account server mail protocol */
	@Getter
    private String protocol;
    /** specify account server mail whether is secured */
	@Getter
    private boolean secure = false;
    /** account mail login */
	@Getter
	@Setter
    private String user;

    /**
     * Get a boolean value indicating this account whether has been applied user/password
     *
     * @return true for applied; else false
     */
    public boolean isRequiresAuthentication() {
        return (StringUtils.hasText(this.getUser())
                && StringUtils.hasText(this.getPassword()));
    }

    /**
     * Get account settings by the specified protocol
     *
     * @param protocol the mail server protocol
     *
     * @return the account settings
     */
	public static Account getInstance(String protocol) {
		return getInstance(protocol, 0);
	}
	/**
     * Get account settings by the specified protocol and port
     *
     * @param protocol the mail server protocol
     * @param port the mail server port. default is equals or less than 0 for default port
     *
     * @return the account settings
     */
    public static Account getInstance(String protocol, int port) {
		Account account = null;
		if (StringUtils.hasText(protocol)) {
		    protocol = protocol.toLowerCase();
    		/** IMAP */
		    boolean secure = protocol.startsWith(PROTOCOL_IMAP + "s");
    		if (protocol.startsWith(PROTOCOL_IMAP)) {
    			port = port <= 0 ? secure ? PORT_IMAPS_DEFAULT : PORT_IMAP_DEFAULT : port;
    			account = new IMAPAccount((PROTOCOL_IMAP + (secure ? "s" : "")), secure, port);
    		}
    		/** POP3 */
    		else if (protocol.startsWith(PROTOCOL_POP)) {
    		    port = port <= 0 ? secure ? PORT_POPS_DEFAULT : PORT_POP_DEFAULT : port;
    			account = new POPAccount((PROTOCOL_POP + (secure ? "s" : "")), secure, port);
    		}
    		/** SMTP */
    		else {
    		    port = port <= 0 ? secure ? PORT_SMTPS_DEFAULT : PORT_SMTP_DEFAULT : port;
    			account = new SMTPAccount((PROTOCOL_SMTP + (secure ? "s" : "")), secure, port);
    		}
		}
		return account;
	}

    /**
     * Initialize a new instance of {@link Account}
     *
     * @param protocol the server mail protocol
     * @param secure specify server mail whether is secured
     * @param port the server mail port
     */
	protected Account(String protocol, boolean secure, int port) {
	    this.protocol = protocol;
	    this.secure = secure;
	    this.port = port;
	}

	/**
	 * Specify this account protocol whether is SMTP
	 * @return true for SMTP; else false
	 */
	public final boolean isSmtp() {
	    return (PROTOCOL_SMTP.equalsIgnoreCase(this.getProtocol())
	            || (PROTOCOL_SMTP + "s").equalsIgnoreCase(this.getProtocol()));
	}
	/**
     * Specify this account protocol whether is IMAP
     * @return true for IMAP; else false
     */
    public final boolean isImap() {
        return (PROTOCOL_IMAP.equalsIgnoreCase(this.getProtocol())
                || (PROTOCOL_IMAP + "s").equalsIgnoreCase(this.getProtocol()));
    }
    /**
     * Specify this account protocol whether is POP3
     * @return true for POP3; else false
     */
    public final boolean isPop3() {
        return (PROTOCOL_POP.equalsIgnoreCase(this.getProtocol())
                || (PROTOCOL_POP + "s").equalsIgnoreCase(this.getProtocol()));
    }

	/**
     * Convert account to javax mail {@link Properties}
     *
     * @return javax mail {@link Properties}
     */
    public final Properties toProperties() {
        Properties properties = new Properties();

        String protocol = this.getProtocol();
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail." + protocol + ".host", this.getHost());
        properties.setProperty("mail." + protocol + ".port", String.valueOf(this.getPort()));
        /** requires authentication */
        if (this.isRequiresAuthentication()) {
            properties.setProperty("mail." + protocol + ".auth", "true");
            properties.setProperty("mail." + protocol + ".user", this.getUser());
            properties.setProperty("mail." + protocol + ".password", this.getPassword());
        }
        /** secure */
        if (this.isSecure()) {
            properties.setProperty(
                    "mail." + protocol + ".socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail." + protocol + ".socketFactory.fallback", "false");
            properties.setProperty(
                "mail." + protocol + ".socketFactory.port",
                String.valueOf(this.getPort()));
        }

        return properties;
    }
}
