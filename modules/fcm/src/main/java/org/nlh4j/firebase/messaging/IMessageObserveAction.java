/*
 * @(#)IMessageObserveAction.java Aug 15, 2018
 * Copyright 2018 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.firebase.messaging;

import java.util.Map;

import org.nlh4j.mongo.observation.IObserveAction;

/**
 * Interface to perform sending notification via Firebase
 *
 * @author Hai Nguyen
 *
 */
public interface IMessageObserveAction extends IObserveAction {

	/**
	 * Get the notification message builder
	 * @return the notification message builder
	 */
	IMessageBuilder<Map<String, ?>> getMessageBuilder();
}
