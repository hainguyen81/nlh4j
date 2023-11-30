/*
 * @(#)TextWebSocketHandler.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.socket;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import org.nlh4j.core.context.profiles.SpringProfiles;
import org.nlh4j.core.service.ChatService;

/**
 * Spring SOCKET text handler
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Profile(value = { SpringProfiles.PROFILE_SOCKET, SpringProfiles.PROFILE_FULL })
public class TextWebSocketHandler extends org.springframework.web.socket.handler.TextWebSocketHandler {

	/** logger */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @see ChatService
     */
    @Inject
    private ChatService chatService;

    /*
     * (Non-Javadoc)
     * @see org.springframework.web.socket.handler.AbstractWebSocketHandler#afterConnectionEstablished(org.springframework.web.socket.WebSocketSession)
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	logger.debug("afterConnectionEstablished");
    	chatService.registerOpenConnection(session);
    	super.afterConnectionEstablished(session);
    }

    /*
     * (Non-Javadoc)
     * @see org.springframework.web.socket.handler.AbstractWebSocketHandler#afterConnectionClosed(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.CloseStatus)
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	logger.debug("afterConnectionClosed");
    	chatService.registerCloseConnection(session);
    	super.afterConnectionClosed(session, status);
    }

    /*
     * (Non-Javadoc)
     * @see org.springframework.web.socket.handler.TextWebSocketHandler#handleBinaryMessage(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.BinaryMessage)
     */
    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
    	logger.debug("handleBinaryMessage");
    	super.handleBinaryMessage(session, message);
    }

    /*
     * (Non-Javadoc)
     * @see org.springframework.web.socket.handler.AbstractWebSocketHandler#handleMessage(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.WebSocketMessage)
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
    	logger.debug("handleMessage");
    	super.handleMessage(session, message);
    }

    /*
     * (Non-Javadoc)
     * @see org.springframework.web.socket.handler.AbstractWebSocketHandler#handlePongMessage(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.PongMessage)
     */
    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
    	logger.debug("handlePongMessage");
    	super.handlePongMessage(session, message);
    }

    /*
     * (Non-Javadoc)
     * @see org.springframework.web.socket.handler.AbstractWebSocketHandler#handleTextMessage(org.springframework.web.socket.WebSocketSession, org.springframework.web.socket.TextMessage)
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	logger.debug("handleTextMessage");
    	chatService.processMessage(session, message.getPayload());
    	super.handleTextMessage(session, message);
    }

    /*
     * (Non-Javadoc)
     * @see org.springframework.web.socket.handler.AbstractWebSocketHandler#handleTransportError(org.springframework.web.socket.WebSocketSession, java.lang.Throwable)
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    	logger.debug("handleTransportError");
    	chatService.registerCloseConnection(session);
    	super.handleTransportError(session, exception);
    }
}
