/*
 * @(#)AbstractSocketController.java 1.0 Mar 6, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import lombok.Getter;
import reactor.util.Assert;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.dto.AbstractSocketDto;
import org.nlh4j.core.dto.UserDetails;
import org.nlh4j.util.BeanUtils;

/**
 * Abstract controller for socket
 *
 * @param <K> the real socket response data type
 * @param <T> the outbound type of socket response data
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Controller
public abstract class AbstractSocketController<K extends AbstractDto, T extends AbstractSocketDto<K>>
		extends AbstractController {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    /** the outbound class of the socket response data */
    @Getter
    private Class<T> socketDtoClass;
    /** the outbound socket response DTO history */
    protected final Set<T> socketDtoSet = Collections.synchronizedSet(new LinkedHashSet<T>());

    /**
     * Initialize a new instance of {@link AbstractSocketController}
     *
     * @param socketDtoClass the outbound class of the socket response data
     */
    protected AbstractSocketController(Class<T> socketDtoClass) {
    	Assert.notNull(socketDtoClass, "socketDtoClass");
    }

    /**
     * Create the outbound socket DTO to response/send to client
     *
     * @param message the received message payload
	 * @param messageHeaders the received message headers
	 * @param principal the sender that had been sent this notification
	 *
     * @return the outbound socket DTO to response/send to client
     */
    protected T createSocketDto(
    		@Payload K message
    		, @Headers MessageHeaders messageHeaders
    		, Principal principal) {
    	Authentication authentication = BeanUtils.safeType(principal, Authentication.class);
    	UserDetails user = (authentication == null ? null
    			: BeanUtils.safeType(authentication.getPrincipal(), UserDetails.class));
    	Assert.notNull(user, "authentication");
    	T socketDto = BeanUtils.safeNewInstance(
    			this.getSocketDtoClass(),
    			new Object[] { user, messageHeaders, message });
    	Assert.notNull(socketDto, "The socket DTO class ["
    			+ this.socketDtoClass.getName() + "] has constructor with parameters: "
				+ "authentication and original message!");
    	socketDtoSet.add(socketDto);
    	return socketDto;
    }
}
