/*
 * @(#)MailServiceImpl.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.service.mail;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.inject.Singleton;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang3.ArrayUtils;
import org.nlh4j.core.context.profiles.SpringProfiles;
import org.nlh4j.core.dto.mail.Email;
import org.nlh4j.core.dto.mail.Email.EmailAttachment;
import org.nlh4j.core.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * The implement of the {@link MailService} interface.<br>
 * FIXME Create instance using dynamic by disabling/enabling mail module
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Service
@Transactional
@Singleton
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Profile(value = { SpringProfiles.PROFILE_MAIL, SpringProfiles.PROFILE_FULL })
public class MailServiceImpl extends AbstractService implements MailService {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	public static final String UTF8_ENCODE = "UTF-8";

	/**
	 * @see JavaMailSender
	 */
	@Getter
	@Setter
	private transient JavaMailSender mailSender;

	/**
	 * FIXME Demo for maximum Integer number of the excutor services for sending email
	 *
	 * @see {@link ExecutorService}
	 * @see {@link ThreadPoolExecutor}
	 */
	private transient ExecutorService executor =
		new ThreadPoolExecutor(
				0, Integer.MAX_VALUE,
				0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(),
				new ThreadFactory() {

					/**
					 * (non-Javadoc)
					 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
					 */
					@Override
					public Thread newThread(Runnable r) {
						return new Thread(r);
					}
				}
		);

    /**
     * Initializes a new instance of the {@link MailServiceImpl} class.
     *
     * @param mailSender mail sender
     */
	@Autowired(required = true)
    public MailServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
    }

	/**
	 * Internal sending email method
	 *
	 * @param email to send
	 */
	private void sendMailInternal(final Email email) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			/**
			 * (non-Javadoc)
			 * @see org.springframework.mail.javamail.MimeMessagePreparator#prepare(javax.mail.internet.MimeMessage)
			 */
			@Override
		    public void prepare(MimeMessage mimeMessage) throws Exception {
				// starts
		        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, UTF8_ENCODE) {

		        	/**
		        	 * (non-Javadoc)
		        	 * @see org.springframework.mail.javamail.MimeMessageHelper#setTo(java.lang.String[])
		        	 */
		        	@Override
		        	public void setTo(String[] to) throws MessagingException {
		        		List<String> addresses = new LinkedList<String>();
		        		for (int i = 0; i < to.length; i++) {
		        			if (!StringUtils.hasText(to[i])) continue;
		        			addresses.add(to[i]);
		        		}
		        		super.setTo(addresses.toArray(new String[addresses.size()]));
		        	}

		        	/**
		        	 * (non-Javadoc)
		        	 * @see org.springframework.mail.javamail.MimeMessageHelper#setBcc(java.lang.String[])
		        	 */
		        	@Override
		        	public void setBcc(String[] bcc) throws MessagingException {
		        		List<String> addresses = new LinkedList<String>();
		        		for (int i = 0; i < bcc.length; i++) {
		        			if (!StringUtils.hasText(bcc[i])) continue;
		        			addresses.add(bcc[i]);
		        		}
		        		super.setBcc(addresses.toArray(new String[addresses.size()]));
		        	}

		        	/**
		        	 * (non-Javadoc)
		        	 * @see org.springframework.mail.javamail.MimeMessageHelper#setCc(java.lang.String[])
		        	 */
		        	@Override
		        	public void setCc(String[] cc) throws MessagingException {
		        		List<String> addresses = new LinkedList<String>();
		        		for (int i = 0; i < cc.length; i++) {
		        			if (!StringUtils.hasText(cc[i])) continue;
		        			addresses.add(cc[i]);
		        		}
		        		super.setCc(addresses.toArray(new String[addresses.size()]));
		        	}
		        };

				// set sender info
		        if (StringUtils.hasText(email.getSenderName())) {
		        	message.setFrom(email.getSender(), email.getSenderName());
		        }
		        else {
		        	message.setFrom(email.getSender());
		        }

				// set receives info
				message.setTo(StringUtils.delimitedListToStringArray(email.getMailingList(), ";"));
				// set receives cc
				if (StringUtils.hasText(email.getCcList())) {
					message.setCc(StringUtils.delimitedListToStringArray(email.getCcList(), ";"));
				}
				// set receives bcc
				if (StringUtils.hasText(email.getBccList())) {
					message.setBcc(StringUtils.delimitedListToStringArray(email.getBccList(), ";"));
				}

				// set subject
				message.setSubject(email.getSubject());

				// debug
				logger.debug(
						MessageFormat.format(
								"------- from [{0}] to [{1}] with subject [{2}] -------",
								email.getSender(), email.getMailingList(), email.getSubject()
						)
				);

				// if not using attachment
				if (CollectionUtils.isEmpty(email.getAttachments())) {
					// set body
					message.setText(email.getContent().toString(), true);
				}
				// if using attachment
				else {
					// -------------------------------------------------
			        // This HTML mail have to 2 part, the BODY and the embedded image
			        // -------------------------------------------------
			        MimeMultipart multipart = new MimeMultipart("related");

			        // -------------------------------------------------
			        // first part (the html) [MAIL BODY MESSAGE]
			        // -------------------------------------------------
			        BodyPart messageBodyPart = new MimeBodyPart();
			        messageBodyPart.setContent(email.getContent().toString(), "text/html; charset=" + UTF8_ENCODE);
			        // add it
			        multipart.addBodyPart(messageBodyPart);

			        // -------------------------------------------------
			        // second part (attachments) [MAIL BODY MESSAGE]
			        // -------------------------------------------------
			        for(final EmailAttachment ea : email.getAttachments()) {
			        	// if file attachment
			        	if ((ea.getAttachFile() != null)
			        			&& ea.getAttachFile().exists()) {
			        		// handles part's attachment
			        		DataSource ds = new FileDataSource(ea.getAttachFile());
			        		BodyPart bp = createMailBodyPart(ds, ea.getContentId(), ea.getName(), ea.getDisposition());
			        		if (bp != null)
			        			multipart.addBodyPart(bp);
			        	}
			        	// if binary attachment
			        	if (!ArrayUtils.isEmpty(ea.getAttachBinary())) {
			        		// handles part's attachment
				        	DataSource ds = new ByteArrayDataSource(ea.getAttachBinary(), ea.getMimeType());
				        	BodyPart bp = createMailBodyPart(ds, ea.getContentId(), ea.getName(), ea.getDisposition());
			        		if (bp != null) multipart.addBodyPart(bp);
			        	}
			        }

			        // -------------------------------------------------
			        // attachment all parts
			        // -------------------------------------------------
			        // put everything together
			        mimeMessage.setContent(multipart);
				}
		    };
		};
		// send mail
		executor.submit(new SendThreadMail(preparator));
	}

	/**
	 * Handle send mail by safe thread
	 *
	 * @author Hai Nguyen (hainguyenjc@gmail.com)
	 */
	class SendThreadMail extends Thread {

		/**
		 * The mail mime message preparator
		 */
		MimeMessagePreparator preparator;

		/**
		 * Initializes a new instance of the {@link SendThreadMail} class.
		 *
		 * @param preparator
		 * 		The mail mime message preparator
		 */
		SendThreadMail(MimeMessagePreparator preparator) {
			super(MailService.class.getName());
	        this.preparator = preparator;
	    }

		/**
		 * (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
	    public void run() {
			try {
				mailSender.send(this.preparator);
			}
			catch (MailException me) {
				logger.error(me.getMessage(), me);
			}
	    }
	}

	/**
	 * Creates mail body part
	 *
	 * @param ds the body part attachment data source
	 * @param contentId the body part content identity
	 * @param attachName the body part attachment identity
	 * @param disposition the body part disposition
	 *
	 * @return the new body part or null
	 *
	 * @throws MessagingException thrown if fail
	 */
	private BodyPart createMailBodyPart(DataSource ds, String contentId, String attachName, String disposition) throws MessagingException {
		if (ds == null) return null;
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setDataHandler(new DataHandler(ds));
		if (contentId != null)
			messageBodyPart.setHeader("Content-ID",
					MessageFormat.format("<{0}>",
							contentId.replace("<>".subSequence(0, 2), "_")));
		if (attachName != null)
			messageBodyPart.setFileName(attachName);
		if (disposition != null)
			messageBodyPart.setDisposition(disposition);
		return messageBodyPart;
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.service.mail.MailService#sendMail(org.nlh4j.common.service.mail.Email)
	 */
	@Override
	@Transactional
	public void sendMail(Email email) {
		if (null != email && StringUtils.hasText(email.getMailingList())) {
			sendMailInternal(email);
		}
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.service.mail.MailService#sendMail(java.util.List)
	 */
	@Override
	@Transactional
	public void sendMail(List<Email> emails) {
		if(CollectionUtils.isEmpty(emails)) return;
		for (Email email : emails) {
			this.sendMail(email);
		}
	}
}