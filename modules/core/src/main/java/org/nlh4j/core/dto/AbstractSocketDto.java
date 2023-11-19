/*
 * @(#)AbstractSocketDto.java 1.0 Mar 6, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto;

import java.sql.Timestamp;
import java.util.Objects;

import org.nlh4j.util.DateUtils;
import org.springframework.messaging.MessageHeaders;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract socket message DTO
 *
 * @param <T> the socket response data type
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractSocketDto<T extends AbstractDto> extends AbstractDto {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    /** the received socket data */
    @Getter
    @Setter
	private T original;
    /** the authentication of the socket sender */
    @Getter
    @Setter
	private UserDetails sender;
    /** the received socket message headers */
    @Getter
    @Setter
    private MessageHeaders headers;
    /** the time when socket data DTO has been created */
    private Timestamp time;

	/**
	 * Initialize a new instance of {@link AbstractSocketDto}
	 *
	 * @param sender the authentication of the socket sender
	 * @param original the received socket data
	 */
	protected AbstractSocketDto(UserDetails sender, MessageHeaders headers, T original) {
		this.setOriginal(original);
		this.setSender(Objects.requireNonNull(sender, "sender"));
		this.setHeaders(headers);
	}

	/**
	 * Get the time when socket data DTO has been created
	 * @return the time when socket data DTO has been created
	 */
	public Timestamp getTime() {
		if (this.time == null) {
			this.time = DateUtils.currentTimestamp();
		}
		return this.time;
	}
}
