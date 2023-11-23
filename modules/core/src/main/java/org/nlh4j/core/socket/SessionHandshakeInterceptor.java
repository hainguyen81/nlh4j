/*
 * @(#)SessionHandshakeInterceptor.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.socket;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import org.nlh4j.core.context.profiles.SpringProfiles;

/**
 * Spring SOCKET session handshake interceptor
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Profile(value = { SpringProfiles.PROFILE_SOCKET, SpringProfiles.PROFILE_FULL })
public class SessionHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

	/** logger */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /*
     * (Non-Javadoc)
     * @see org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor#beforeHandshake(org.springframework.http.server.ServerHttpRequest, org.springframework.http.server.ServerHttpResponse, org.springframework.web.socket.WebSocketHandler, java.util.Map)
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
    	// TODO Auto-generated Method Stub
    	logger.debug("beforeHandshake");
    	return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    /*
     * (Non-Javadoc)
     * @see org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor#afterHandshake(org.springframework.http.server.ServerHttpRequest, org.springframework.http.server.ServerHttpResponse, org.springframework.web.socket.WebSocketHandler, java.lang.Exception)
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
    	// TODO Auto-generated Method Stub
    	logger.debug("afterHandshake");
    	super.afterHandshake(request, response, wsHandler, ex);
    }
}
