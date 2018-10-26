/*
 * @(#)IncomingEmailJob.java 1.0 Sep 27, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.scheduler.interval.mail;

import java.text.MessageFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.IllegalWriteException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.search.SearchTerm;

import org.apache.commons.lang.ArrayUtils;
import org.quartz.JobExecutionException;
import org.springframework.util.Assert;

import lombok.Getter;
import org.nlh4j.core.dto.mail.Account;
import org.nlh4j.core.scheduler.BaseIntervalJob;

/**
 * Abstract base job for processing data via email message.
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public abstract class AbstractIncomingEmailJob<T> extends BaseIntervalJob {

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;
	private static final String FOLDER_INBOX = "inbox";

	/**
	 * The executor for calling the {@link AbstractIncomingEmailCallable} threads
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
	 * The number of completed threads
	 */
	@Getter
	private int numOfDoneThread = 0;
	@Getter
	private final Account account;

	/**
     * Initialize a new instance of {@link AbstractIncomingEmailJob}
     *
     * @param account the account settings to log-in
     */
    protected AbstractIncomingEmailJob(Account account) {
        Assert.notNull(account, "account");
        this.account = account;
    }

    /**
     * Open mail folder in mode
     *
     * @param mailbox such as "inbox"
     * @param mode the opened mode
     *
     * @return the mail folder
     */
    protected final Folder openFolder(String mailbox, int mode) throws NoSuchProviderException, MessagingException {
        logger.debug("Requires mail session.....");
        Session session = Session.getInstance(this.getAccount().toProperties());

        logger.debug("Requires URL account for getting mail.....");
        URLName urlName = new URLName(
                this.getAccount().getProtocol(),
                this.getAccount().getHost(),
                this.getAccount().getPort(),
                "",
                this.getAccount().getUser(),
                this.getAccount().getPassword());

        logger.debug("Connecting to email acount.....");
        Store store = session.getStore(urlName);
        store.connect();

        logger.debug("Requires email account inbox.....");
        Folder folder = store.getFolder(mailbox);
        folder.open(mode);

        return folder;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.scheduler.BaseIntervalJob#doJob()
     */
    @Override
    protected void doJob() throws JobExecutionException {
        logger.debug("----- Starting to scan new email in inbox -----");

        Folder inbox = null;
        Message[] messages = null;
        int unread = 0;
        try {
            // gets the mail box
            inbox = this.openFolder(FOLDER_INBOX, Folder.READ_WRITE);

            // gets the number of the un-read email messages
            unread = inbox.getUnreadMessageCount();
            logger.debug(MessageFormat.format("The number of un-read emails {0}", String.valueOf(unread)));

            // if not found any un-read email
            if (unread <= 0) logger.debug("No email found.");
            // else if found un-read emails; then processing the found emails
            else {
                // searches all un-read emails
                messages = inbox.search(
                        new SearchTerm() {

                            /**
                             * serialVersionUID
                             */
                            private static final long serialVersionUID = 1L;

                            /**
                             * (non-Javadoc)
                             * @see javax.mail.search.SearchTerm#match(javax.mail.Message)
                             */
                            @Override
                            public boolean match(Message message) {
                                try {
                                    return (message != null && !message.isSet(Flag.SEEN));
                                }
                                catch (MessagingException e) {
                                    logger.error(e.getMessage(), e);
                                    e.printStackTrace();
                                    return false;
                                }
                            }
                        }
                );
                // processes un-read emails
                if (!ArrayUtils.isEmpty(messages)) {
                    processMessages(messages);
                }
            }

            // re-checks after 5 seconds
            try {
                Thread.sleep(5000);
            }
            catch(Exception e) {}
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        finally {
            if ((inbox != null) && inbox.isOpen()) {
                // marks processed emails as read
                if (!ArrayUtils.isEmpty(messages)) {
                    try {
                        inbox.setFlags(messages, new Flags(Flags.Flag.SEEN), Boolean.TRUE);
                    }
                    catch (IllegalStateException e) {
                        logger.error(e.getMessage(), e);
                        e.printStackTrace();
                    }
                    catch (MessagingException e) {
                        logger.error(e.getMessage(), e);
                        e.printStackTrace();
                    }
                }

                // closes box mail
                try {
                    inbox.close(true);
                }
                catch (IllegalStateException e) {
                    logger.error(e.getMessage(), e);
                    e.printStackTrace();
                }
                catch (MessagingException e) {
                    logger.error(e.getMessage(), e);
                    e.printStackTrace();
                }
            }
        }

        logger.info("----- End job -----");
    }

	/**
	 * Process and create a new document for each mail message received
	 *
	 * @param messages
	 *
	 * @throws MessagingException
	 */
	private void processMessages(Message[] messages) throws MessagingException {
		// Checks parameter
		Assert.isTrue(!ArrayUtils.isEmpty(messages), "Emails");
		// Processes e-mails
		logger.info("Processing " + messages.length + " emails...");
		for (Message message : messages) {
			// only process email that has not been read
			if ((message != null) && !message.isSet(Flag.SEEN)) {
				// applies this message as read (like processed)
				message.setFlag(Flag.SEEN, Boolean.TRUE);
				// TODO For non POP3 protocol, such as IMAP
				if (!this.getAccount().isPop3()) {
					try {
						message.saveChanges();
					}
					catch (IllegalStateException e) {
						logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
					catch (IllegalWriteException e) {
						logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
					catch (MessagingException e) {
						logger.error(e.getMessage(), e);
						e.printStackTrace();
					}
				}

				// creates incoming call-able by the specified email
				AbstractIncomingEmailCallable<T> callable = this.createCallable(message);
				if (callable != null) {
					// creates thread for executing the just created call-able
					FutureTask<T> task = new FutureTask<T>(callable);
					this.executor.submit(task);

					// executes call-able
					try {
						task.get();
						numOfDoneThread++;
					}
					catch (Exception e) {
						logger.error(e.getMessage(), e);
						e.printStackTrace();
						numOfDoneThread++;
					}

					// debug
					logger.info("Number of done tasks: " + numOfDoneThread);
				}
			}
		}
	}

	/**
	 * Gets the incoming call-able for performing action on the specified message.<br/>
	 * TODO Overrides this method for creating the incoming call-able.
	 *
	 * @param message the current message that need to process
	 *
	 * @return the incoming call-able for performing action on the specified message
	 */
	protected abstract AbstractIncomingEmailCallable<T> createCallable(Message message);
}
