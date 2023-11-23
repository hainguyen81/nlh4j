/*
 * @(#)IMessageBuilder.java
 * Copyright 2018 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.firebase.messaging;

import java.io.Serializable;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Message.Builder;

/**
 * Interface to build Firebase {@link Message} to send notification
 * @param <T> data type to build {@link Message}
 * @see
 * <u>Notification Structure:</u><br>
 * <code>
 * {<br>
 * 		<span style="padding-left: 15px">topic: 'topics1',</span><br>
 * 		<span style="padding-left: 15px">condition: '"a" in topics',</span><br>
 * 		<span style="padding-left: 15px">notification: {</span><br>
 * 		<span style="padding-left: 30px">title: '$GOOG up 1.43% on the day',</span><br>
 * 		<span style="padding-left: 30px">body: '$GOOG gained 11.80 points to close at 835.67, up 1.43% on the day.',</span><br>
 * 		<span style="padding-left: 15px">},</span><br>
 * 		<span style="padding-left: 15px">data: {</span><br>
 * 		<span style="padding-left: 30px">score: '850',</span><br>
 * 		<span style="padding-left: 30px">time: '2:45',</span><br>
 * 		<span style="padding-left: 30px">image: 'http:'...</span><br>
 * 		<span style="padding-left: 15px">},</span><br>
 * 		<span style="padding-left: 15px">android: {</span><br>
 * 		<span style="padding-left: 30px">ttl: 3600 * 1000,</span><br>
 * 		<span style="padding-left: 30px">priority: 'normal',</span><br>
 * 		<span style="padding-left: 30px">notification: {</span><br>
 * 		<span style="padding-left: 45px">title: '$GOOG up 1.43% on the day',</span><br>
 * 		<span style="padding-left: 45px">body: '$GOOG gained 11.80 points to close at 835.67, up 1.43% on the day.',</span><br>
 * 		<span style="padding-left: 45px">icon: 'stock_ticker_update',</span><br>
 * 		<span style="padding-left: 45px">color: '#f45342'</span><br>
 * 		<span style="padding-left: 30px">}</span><br>
 * 		<span style="padding-left: 15px">},</span><br>
 * 		<span style="padding-left: 15px">apns: {</span><br>
 * 		<span style="padding-left: 30px">header: {</span><br>
 * 		<span style="padding-left: 45px">'apns-priority': '10'</span><br>
 * 		<span style="padding-left: 30px">},</span><br>
 * 		<span style="padding-left: 30px">payload: {</span><br>
 * 		<span style="padding-left: 45px">aps: {</span><br>
 * 		<span style="padding-left: 60px">alert: {</span><br>
 * 		<span style="padding-left: 75px">title: '$GOOG up 1.43% on the day',</span><br>
 * 		<span style="padding-left: 75px">body: '$GOOG gained 11.80 points to close at 835.67, up 1.43% on the day.',</span><br>
 * 		<span style="padding-left: 60px">},</span><br>
 * 		<span style="padding-left: 60px">badge: 42,</span><br>
 * 		<span style="padding-left: 45px">}</span><br>
 * 		<span style="padding-left: 30px">}</span><br>
 * 		<span style="padding-left: 15px">},</span><br>
 * 		<span style="padding-left: 15px">webpush: {</span><br>
 * 		<span style="padding-left: 30px">notification: {</span><br>
 * 		<span style="padding-left: 45px">title: '$GOOG up 1.43% on the day',</span><br>
 * 		<span style="padding-left: 45px">body: '$GOOG gained 11.80 points to close at 835.67, up 1.43% on the day.',</span><br>
 * 		<span style="padding-left: 45px">icon: 'https://my-server/icon.png'</span><br>
 * 		<span style="padding-left: 30px">}</span><br>
 * 		<span style="padding-left: 15px">},</span><br>
 * 		<span style="padding-left: 15px">token: "token",</span><br>
 * }
 * </code>
 */
public interface IMessageBuilder<T> extends Serializable {
	/**
	 * Implement to build {@link Message} from the specified notification data
	 * @param data to build
	 * @return {@link Message}
	 */
	Builder build(T data);
}
