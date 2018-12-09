/*
 * @(#)AbstractMessageObserveAction.java Aug 15, 2018
 * Copyright 2018 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.firebase.messaging;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.inject.Singleton;

import org.bson.Document;
import org.nlh4j.util.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mongodb.client.model.changestream.OperationType;
import com.mongodb.client.model.changestream.UpdateDescription;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Abstract Firebase Messaging Observe Action
 *
 * @author Hai Nguyen
 *
 */
@Component
@Singleton
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public abstract class AbstractMessageObserveAction implements IMessageObserveAction, Serializable {

    /** */
    private static final long serialVersionUID = 1L;

    /** slf4j */
    protected final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    /** {@link FirebaseMessagingService} */
    @Getter(value = AccessLevel.PROTECTED)
	private FirebaseMessagingService firebaseService;

	/**
	 * Initialize a new instance of {@link AbstractMessageObserveAction} class
	 *
	 * @param firebaseService {@link FirebaseMessagingService} to send notification
	 */
	protected AbstractMessageObserveAction(FirebaseMessagingService firebaseService) {
		if (firebaseService == null) {
			throw new IllegalArgumentException("Invalid messaging service to send notification!");
		}
		this.firebaseService = firebaseService;
	}

	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.observation.IObserveAction#perform(com.mongodb.client.model.changestream.OperationType, org.bson.Document, com.mongodb.client.model.changestream.UpdateDescription)
	 */
	@Override
	public void perform(OperationType op, Document document, UpdateDescription description) {
		IMessageBuilder<Map<String, ?>> builder = this.getMessageBuilder();
		if (builder == null) {
			throw new IllegalArgumentException("Please implement message builder to send notification!");
		}
		try {
			logger.info("Sending notification with document [" + document.toJson() + "]");
			Object response = this.getFirebaseService().apiSendAsync(builder, document).get();
			logger.info("--> Result {" + BeanUtils.toString(response) + "}");
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage(), e);
		}
	}

	/* (非 Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {}
}
