/*
 * @(#)ChatService.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.service;

import org.springframework.web.socket.WebSocketSession;

/**
 * CHAT socket service
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public interface ChatService {
	/**
	 * Regist opened connection socket
	 *
	 * @param session socket session
	 */
	void registerOpenConnection(WebSocketSession session);
	/**
	 * Regist closed connection socket
	 *
	 * @param session socket session
	 */
	void registerCloseConnection(WebSocketSession session);
	/**
	 * Process message
	 *
	 * @param session socket session
	 * @param message message
	 */
	void processMessage(WebSocketSession session, String message);
}
