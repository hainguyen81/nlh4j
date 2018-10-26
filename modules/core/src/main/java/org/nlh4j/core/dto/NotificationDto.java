/*
 * @(#)NotificationDto.java 1.0 Aug 28, 2015 Copyright 2015 by GNU Lesser General Public License (LGPL).
 * All rights reserved.
 */
package org.nlh4j.core.dto;

import java.sql.Timestamp;

import org.nlh4j.util.DateUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Notification PUSH information
 *
 * @param <T> notification data type
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NotificationDto<T> extends AbstractDto {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    private Timestamp time;
    private T data;

    /** Notification type */
    public enum NotificationType {
        None, Success, Info, Warning, Error, Wait,
    }

    /** notification type */
    @JsonIgnore
    private NotificationType messageType = NotificationType.None;

    /**
     * Get the notification type ordinal
     * @return the notification type ordinal
     */
    public int getType() {
        int type = NotificationType.None.ordinal();
        if (this.getMessageType() != null) {
            type = this.getMessageType().ordinal();
        }
        return type;
    }

    /**
     * Initialize a new instance of {@link NotificationDto}
     *
     * @param data notification data
     */
    public NotificationDto(T data) {
        this(NotificationType.None, data);
    }

    /**
     * Initialize a new instance of {@link NotificationDto}
     *
     * @param msgType notification type
     * @param data notification message
     */
    public NotificationDto(NotificationType msgType, T data) {
        this.setMessageType(msgType == null ? NotificationType.None : msgType);
        this.setData(data);
        this.setTime(DateUtils.currentTimestamp());
    }
}
