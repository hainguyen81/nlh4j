/*
 * @(#)Email.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto.mail;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nlh4j.core.dto.AbstractDto;

/**
 * The email information for sending out-going email
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Email extends AbstractDto {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Defines the email's attachments
	 */
	@Data
	public static class EmailAttachment implements Serializable {

		/**
		 * Default serial UID
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * The MIME type of this attachment
		 */
		private String mimeType;

		/**
		 * The name of this attachment.
		 */
	    private String name;

		/**
		 * The {@link File} attachment
		 */
		private File attachFile;
		/**
		 * The binary attachment
		 */
		private byte[] attachBinary;
		/**
		 * The mail body part content identity of this attachment
		 */
		private String contentId;

		/**
		 * The description of this attachment.
		 */
	    private String description;

	    /**
	     * The disposition.
	     */
	    private String disposition;

		/**
		 * Initializes a new instance of the {@link Email.EmailAttachment} class.
		 *
		 */
		public EmailAttachment() {}

		/**
		 * Initializes a new instance of the {@link EmailAttachment} class.
		 *
		 * @param file
		 * 		the {@link File} attachment
		 */
		public EmailAttachment(File file) {
			this.setAttachFile(file);
		}

		/**
		 * Initializes a new instance of the {@link EmailAttachment} class.
		 *
		 * @param attachBinary
		 * 		the binary attachment
		 */
		public EmailAttachment(byte[] attachBinary) {
			this.setAttachBinary(attachBinary);
		}

		/**
		 * Initializes a new instance of the {@link EmailAttachment} class.
		 *
		 * @param ea
		 * 		the {@link EmailAttachment} for cloning
		 */
		public EmailAttachment(EmailAttachment ea) {
			if (ea != null) {
				this.setMimeType(ea.getMimeType());
				this.setName(ea.getName());
				this.setAttachFile(ea.getAttachFile());
				this.setAttachBinary(ea.getAttachBinary());
				this.setContentId(ea.getContentId());
				this.setDescription(ea.getDescription());
				this.setDisposition(ea.getDisposition());
			}
		}

		/**
		 * Sets the {@link File} attachment.
		 * (This function will apply attachment name, MIME type automatically if them has not been set)
		 *
		 * @param attachFile
		 *			the {@link File} attachment
		 */
		public void setAttachFile(File attachFile) {
			this.attachFile = attachFile;
			if (attachFile != null) {
				// sets attachment name
				if (!StringUtils.hasLength(this.getName()))
					this.setName(attachFile.getName());
				// sets attachment MIME type
				if (!StringUtils.hasLength(this.getMimeType()))
					this.setMimeType(this.getMimeType(attachFile));
			}
		}

		/**
		 * Helper function to get MIME type of the specified {@link File}
		 *
		 * @param f
		 * 		the {@link File} to get MIME type
		 *
		 * @return the MIME type or null
		 */
		private String getMimeType(File f) {
			MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
			return mimeTypesMap.getContentType(f);
		}

		/**
		 * (non-Javadoc)
		 * @see java.lang.Object#clone()
		 */
		@Override
		protected Object clone() throws CloneNotSupportedException {
			return new EmailAttachment(this);
		}
	}

	// mail from
	private String sender;
	private String senderName;

	// mail list
	private String mailingList;
	public void setMailingList(String mailingList) {
		Assert.hasText(mailingList, "mailingList");
		this.mailingList = mailingList;
	}
	public void setMailingList(String[] emails) {
		Assert.notEmpty(emails, "emails");
		String mailingList = "";
		for(String email : emails) {
			if (StringUtils.hasText(email)) {
				if (StringUtils.hasText(mailingList)) mailingList += ";";
				mailingList += email;
			}
		}
		this.setMailingList(mailingList);
	}

	private String ccList;

	private String bccList;

	// mail header
	private String header;

	// subject
	private String subject;

	// content
	private StringBuffer content;

	// mail footer
	private String footer;

	// mail attachment
	private List<EmailAttachment> attachments;

	/**
	 * Initializes a new instance of the {@link Email} class.
	 *
	 */
	public Email() {}

	/**
	 * Initializes a new instance of the {@link Email} class.
	 *
	 * @param ei another {@link Email} instance to clone
	 */
	public Email(Email ei) {
		if (ei != null) {
			this.setSender(ei.getSender());
			this.setSenderName(ei.getSenderName());
			this.setMailingList(ei.getMailingList());
			this.setCcList(ei.getCcList());
			this.setBccList(ei.getBccList());
			this.setHeader(ei.getHeader());
			this.setSubject(ei.getSubject());
			this.setContent(ei.getContent());
			this.setFooter(ei.getFooter());
			if (ei.getAttachments() != null && ei.getAttachments().size() > 0) {
				for(EmailAttachment ea : ei.getAttachments()) {
					if (ea == null) continue;
					this.getAttachments().add(new EmailAttachment(ea));
				}
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Email clone() throws CloneNotSupportedException {
		return new Email(this);
	}
}
