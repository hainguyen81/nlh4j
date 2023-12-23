/*
 * @(#)ChatServiceImpl.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.service;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.nlh4j.core.annotation.InjectTransactionalService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * {@link ChatService} implement
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@InjectTransactionalService
public class ChatServiceImpl extends AbstractService implements ChatService {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private Set<WebSocketSession> conns = Collections.synchronizedSet(new LinkedHashSet<WebSocketSession>());
	private Map<WebSocketSession, String> nickNames = new ConcurrentHashMap<WebSocketSession, String>();

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.web.chat.service.ChatService#registerOpenConnection(org.springframework.web.socket.WebSocketSession)
	 */
	@Override
	@Transactional
	public void registerOpenConnection(WebSocketSession session) {
		logger.debug("registerOpenConnection");
		conns.add(session);
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.web.chat.service.ChatService#registerCloseConnection(org.springframework.web.socket.WebSocketSession)
	 */
	@Override
	@Transactional
	public void registerCloseConnection(WebSocketSession session) {
		logger.debug("registerCloseConnection");
		String nick = nickNames.get(session);
	    conns.remove(session);
	    nickNames.remove(session);
	    if (nick!= null) {
	    	String messageToSend = "{\"removeUser\":\"" + nick + "\"}";
	    	for (WebSocketSession sock : conns) {
	    		try {
	    			sock.sendMessage(new TextMessage(messageToSend));
	    		} catch (IOException e) {
	    			logger.error(e.getMessage(), e);
	    		}
	    	}
	    }
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.web.chat.service.ChatService#processMessage(org.springframework.web.socket.WebSocketSession, java.lang.String)
	 */
	@Override
	@Transactional
	public void processMessage(WebSocketSession session, String message) {
		logger.debug("processMessage");
		if (!nickNames.containsKey(session)) {
			//No nickname has been assigned by now
			//the first message is the nickname
			//escape the " character first
			message = message.replace("\"", "\\\"");

			//broadcast all the nicknames to him
			for (String nick : nickNames.values()) {
				try {
					session.sendMessage(new TextMessage("{\"addUser\":\"" + nick + "\"}"));
		        } catch (IOException e) {
		        	logger.error(e.getMessage(), e);
		        }
			}

			//Register the nickname with the
			nickNames.put(session, message);

			//broadcast him to everyone now
			String messageToSend = "{\"addUser\":\"" + message + "\"}";
			for (WebSocketSession sock : conns) {
				try {
					sock.sendMessage(new TextMessage(messageToSend));
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		else {
			//Broadcast the message
			String messageToSend = "{\"nickname\":\"" + nickNames.get(session)
					+ "\", \"message\":\"" + message.replace("\"", "\\\"") +"\"}";
			for (WebSocketSession sock : conns) {
				try {
					sock.sendMessage(new TextMessage(messageToSend));
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}

}
