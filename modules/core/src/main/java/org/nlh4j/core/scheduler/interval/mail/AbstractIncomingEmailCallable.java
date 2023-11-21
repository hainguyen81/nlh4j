/*
 * @(#)IncomingEmailCallable.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.scheduler.interval.mail;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import freemarker.template.Configuration;
import org.nlh4j.core.context.profiles.SpringProfiles;
import org.nlh4j.core.service.AbstractService;
import org.nlh4j.core.service.MessageService;
import org.nlh4j.core.service.TemplateService;
import org.nlh4j.core.service.TemplateServiceImpl;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.core.util.JobSchedulerUtils;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.StreamUtils;

/**
 * Abstract scheduler call-able job for parsing email from mail account
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Profile(value = { SpringProfiles.PROFILE_QUARTZ, SpringProfiles.PROFILE_FULL })
public abstract class AbstractIncomingEmailCallable<T> implements Callable<T>, Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private static final String UTF8_ENCODING = "UTF-8";

	/**
	 * log4j
	 */
	protected transient final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * The email message
	 */
	private final Message message;

	/**
	 * The sender emails
	 */
	private List<Address> senders;
	/**
	 * The {@link InternetAddress} senders
	 */
	private List<InternetAddress> internetSenders;
	/**
	 * The body parts of the message
	 */
	private List<Part> bodyParts;
	/**
	 * The content of body parts, that are not attachment/in-line part, in the mail message
	 */
	private List<String> bodyContents;
	/**
	 * The message attachments data of body parts, that are attachment/in-line part, in the mail message
	 */
	private transient Map<String, byte[]> attachments;
	/** context helper */
	@Inject
	private SpringContextHelper contextHelper;

	/**
	 * Initializes a new instance of the {@link AbstractIncomingEmailCallable} class.
	 *
	 * @param message the email message need to process
	 *
	 * @throws Exception thrown if could not process message
	 */
	public AbstractIncomingEmailCallable(Message message) throws Exception {
		// backups processed message
		this.message = message;
		// handles message
		this.parseMessage(message);
	}

	/**
	 * Gets the <b>message</b> value
	 *
	 * @return the <b>message</b> value
	 */
	public final Message getMessage() {
		return this.message;
	}

	/**
	 * Gets the email senders.
	 *
	 * @return the email senders.
	 */
	protected final List<Address> getSenders() {
		if (this.senders == null) {
			this.senders = new LinkedList<Address>();
		}
		return this.senders;
	}
	/**
	 * Gets the {@link InternetAddress} senders.
	 *
	 * @return the {@link InternetAddress} senders.
	 */
	protected final List<InternetAddress> getInternetSenders() {
		if (this.internetSenders == null) {
			this.internetSenders = new LinkedList<InternetAddress>();
		}
		return this.internetSenders;
	}
	/**
	 * Gets the body parts of the message.
	 *
	 * @return the body parts of the message.
	 */
	protected final List<Part> getBodyParts() {
		if (this.bodyParts == null) {
			this.bodyParts = new LinkedList<Part>();
		}
		return this.bodyParts;
	}
	/**
	 * Gets the content of body parts, that are not attachment/in-line part, in the mail message
	 *
	 * @return the content of body parts, that are not attachment/in-line part, in the mail message
	 */
	protected final List<String> getBodyContents() {
		if (this.bodyContents == null) {
			this.bodyContents = new LinkedList<String>();
		}
		return this.bodyContents;
	}
	/**
	 * Gets the message attachments data of body parts, that are attachment/in-line part, in the mail message
	 *
	 * @return the message attachments data of body parts, that are attachment/in-line part, in the mail message
	 */
	protected final Map<String, byte[]> getAttachments() {
		if (this.attachments == null) {
			this.attachments = new LinkedHashMap<String, byte[]>();
		}
		return this.attachments;
	}

	/*
	 * (Non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public final T call() throws Exception {
		try {
			return doCall();
		}
		catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * TODO Overrides this method for performing action
	 * @return result of call-able thread
	 */
	protected abstract T doCall();

	/**
	 * Parses the specified {@link Message}
	 *
	 * @param message the {@link Message} need to parse
	 *
	 * @throws Exception could not process message
	 */
	private void parseMessage(Message message) throws Exception {
		// checks parameter
		Assert.notNull(message, "Email");
		logger.debug(MessageFormat.format("Processing email [{0}]", message.getSubject()));

		// -------------------------------------------------
		// parses senders
		// -------------------------------------------------
		if (!ArrayUtils.isEmpty(message.getFrom())) {
			for(Address address : message.getFrom()) {
				if (address == null) continue;
				this.getSenders().add(address);
				if (BeanUtils.isInstanceOf(address, InternetAddress.class))
					this.getInternetSenders().add((InternetAddress) address);
				else {
					InternetAddress[] addresses = InternetAddress.parse(address.toString());
					if (!ArrayUtils.isEmpty(addresses)) {
						for(InternetAddress iaddress : addresses)
							this.getInternetSenders().add(iaddress);
					}
				}
			}
		}

		// -------------------------------------------------
		// parses email content
		// -------------------------------------------------
		Object content = message.getContent();
        if (BeanUtils.isInstanceOf(content, Multipart.class)) {
        	// handles mail content by multi-part
        	this.handleMultipart((Multipart) content);
        }
        else {
        	// handles simple mail content
        	this.handlePart(message);
        }
	}

	/**
	 * Handles email message in multi-part case.
	 *
	 * @param multipart the email content multi-part need to handle
	 *
	 * @throws Exception could not process message
	 */
	private void handleMultipart(Multipart multipart)  throws Exception {
		Assert.notNull(multipart, "multipart");
		for(int i = 0, n = multipart.getCount(); i < n; i++) {
			handlePart(multipart.getBodyPart(i));
		}
	}
	/**
	 * Handles email message in simple case
	 *
	 * @param part the email message part need to handle
	 *
	 * @throws Exception could not process message
	 */
	private void handlePart(Part part) throws Exception {
		Assert.notNull(part, "part");
		// caches the message part
		this.getBodyParts().add(part);
		// parses message attachments and contents
		String disposition = part.getDisposition();
		// if this part is just message body
	    if (!StringUtils.hasLength(disposition)) { // When just body
	    	// creates stream for reading message body
	    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	// writes part content to output stream
	    	part.writeTo(baos);
	    	// caches the part content by UTF-8 encode
	    	this.getBodyContents().add(
	    	        baos.toString(Charset.forName(UTF8_ENCODING).name()));
	    }
	    // else if the specified part is attachment/in-line
	    else if (disposition.equalsIgnoreCase(Part.ATTACHMENT)
	    		|| disposition.equalsIgnoreCase(Part.INLINE)) {
	    	// creates stream for writing out attachments data
	    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	// creates stream for reading attachments data
	    	BufferedInputStream bis = new BufferedInputStream(part.getInputStream());
	    	// starts reading attachments data
	    	StreamUtils.copyStream(bis, baos);
	    	// caches attachments data
	    	this.getAttachments().put(part.getFileName(), baos.toByteArray());
	    	// releases
	    	StreamUtils.closeQuitely(bis);
	    	StreamUtils.closeQuitely(baos);
	    }
	}

	/**
	 * Get context helper
	 * @return context helper
	 */
	protected final SpringContextHelper getContextHelper() {
		if (this.contextHelper == null) {
			this.contextHelper = new SpringContextHelper(JobSchedulerUtils.getSpringContext());
		}
		if (this.contextHelper == null) return null;
		synchronized (this.contextHelper) {
			return this.contextHelper;
		}
	}
	/**
	 * Get service bean from context
	 * @param serviceClass service bean class
	 * @return service bean or null
	 */
	protected final <B> B findBean(Class<B> beanClass) {
		SpringContextHelper helper = this.getContextHelper();
		return (helper == null ? null : helper.searchBean(beanClass));
	}
	/**
	 * Gets the service, that extended from the {@link BaseService}, with the specified service class
	 *
	 * @param servClass
	 * 		the service interface class
	 *
	 * @exception NullPointerException
	 * 		thrown if the specified <b>servClass</b>, or <b>servAutowiseName</b> is null or empty
	 * @param IllegalArgumentException
	 * 		thrown if the bean service with the specified auto-wised name is not an instance of the specified <b>servClass</b> class
	 */
	protected final <K extends AbstractService> K findService(Class<K> servClass) {
		return this.findBean(servClass);
	}
	/**
	 * Gets the message service {@link MessageService}
	 *
	 * @return the message service {@link MessageService}
	 */
	protected final MessageService getMessageService() {
		return this.findBean(MessageService.class);
	}

    /**
     * {@link TemplateService}
     */
    private TemplateService templateService;
    /**
     * Get the template service {@link TemplateService}
     *
     * @return the template service {@link TemplateService}
     */
    protected final TemplateService getTemplateService() {
        if (this.templateService == null) {
            this.templateService = this.findBean(TemplateService.class);
            if (this.templateService == null) {
                Configuration fmCfg = this.findBean(Configuration.class);
                if (fmCfg != null) {
                    this.templateService = new TemplateServiceImpl(fmCfg);
                }
            }
        }
        if (this.templateService == null) return null;
        synchronized (this.templateService) {
            return this.templateService;
        }
    }
}
