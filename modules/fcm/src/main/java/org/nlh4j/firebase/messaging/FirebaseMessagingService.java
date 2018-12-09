/*
 * @(#)FirebaseMessagingService.java 1.0 Aug 5, 2018
 * Copyright 2018 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.firebase.messaging;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.validator.routines.UrlValidator;
import org.json.JSONObject;
import org.nlh4j.firebase.FirebaseService;
import org.nlh4j.util.HttpUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Message.Builder;
import com.google.firebase.messaging.TopicManagementResponse;

/**
 * Firebase Messaging Service
 *
 * @author Hai Nguyen
 *
 */
public class FirebaseMessagingService extends FirebaseService implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of {@link FirebaseMessagingService}
     *
     * @param adminSdkAccJson the path to adminSDK account JSON file
     * @param database adminSDK database name
     * @param serverKey firebase server key
     *
     * @throws IOException thrown if could not read adminSQK service account stream
     */
    public FirebaseMessagingService(String adminSdkAccJson, String database, String serverKey) throws IOException {
    	super(adminSdkAccJson, database, serverKey);
    }

    /**
     * Send entity notification by {@link Message}
     * @param entity to send
     * @param messageBuilder to build {@link Message}
     * @param dryRun notification data will be appended "validate_only" attribute
     * @return Firebase Response
     */
	public <T> String send(IMessageBuilder<T> messageBuilder, T entity, boolean dryRun) {
    	Assert.notNull(messageBuilder, "MessageBuilder");
    	try {
    		// build message
    		Builder message = messageBuilder.build(entity);
    		return this.getMessaging().send(message.build(), dryRun);
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		throw new RuntimeException(e);
    	}
    }
	/**
     * Send entity notification by {@link Message}
     * @param entity to send
     * @param messageBuilder to build {@link Message}
     * @return Firebase Response
     */
	public <T> String send(IMessageBuilder<T> messageBuilder, T entity) {
    	return this.send(messageBuilder, entity, Boolean.FALSE);
    }
    /**
     * Send entity notification by {@link Message}
     * @param entity to send
     * @param messageBuilder to build {@link Message}
     * @param dryRun notification data will be appended "validate_only" attribute
     * @return {@link CompletableFuture}
     */
    @Async
	public <T> CompletableFuture<?> apiSend(IMessageBuilder<T> messageBuilder, T entity, boolean dryRun) {
		return CompletableFuture.completedFuture(this.send(messageBuilder, entity, dryRun));
    }
    /**
     * Send entity notification by {@link Message}
     * @param entity to send
     * @param messageBuilder to build {@link Message}
     * @return {@link CompletableFuture}
     */
    @Async
	public <T> CompletableFuture<?> apiSend(IMessageBuilder<T> messageBuilder, T entity) {
    	return this.apiSend(messageBuilder, entity, Boolean.FALSE);
    }

    /**
     * Send entity notification by {@link Message}
     * @param entity to send
     * @param messageBuilder to build {@link Message}
     * @param dryRun notification data will be appended "validate_only" attribute
     * @return {@link ApiFuture}
     */
	public <T> ApiFuture<String> sendAsync(IMessageBuilder<T> messageBuilder, T entity, boolean dryRun) {
    	Assert.notNull(messageBuilder, "MessageBuilder");
    	try {
    		// build message
    		Builder message = messageBuilder.build(entity);
    		return this.getMessaging().sendAsync(message.build(), dryRun);
    	} catch (Exception e) {
    		logger.error(e.getMessage(), e);
    		throw new RuntimeException(e);
    	}
    }
	/**
     * Send entity notification by {@link Message}
     * @param entity to send
     * @param messageBuilder to build {@link Message}
     * @return {@link ApiFuture}
     */
	public <T> ApiFuture<String> sendAsync(IMessageBuilder<T> messageBuilder, T entity) {
    	return this.sendAsync(messageBuilder, entity, Boolean.FALSE);
    }
    /**
     * Send entity notification by {@link Message}
     * @param entity to send
     * @param messageBuilder to build {@link Message}
     * @param dryRun notification data will be appended "validate_only" attribute
     * @return {@link CompletableFuture}
     * @throws ExecutionException thrown if failed
     * @throws InterruptedException thrown if failed
     */
    @Async
	public <T> CompletableFuture<?> apiSendAsync(IMessageBuilder<T> messageBuilder, T entity, boolean dryRun)
			throws InterruptedException, ExecutionException {
		return CompletableFuture.completedFuture(this.sendAsync(messageBuilder, entity, dryRun).get());
    }
    /**
     * Send entity notification by {@link Message}
     * @param entity to send
     * @param messageBuilder to build {@link Message}
     * @return {@link CompletableFuture}
     * @throws ExecutionException thrown if failed
     * @throws InterruptedException thrown if failed
     */
    @Async
	public <T> CompletableFuture<?> apiSendAsync(IMessageBuilder<T> messageBuilder, T entity)
			throws InterruptedException, ExecutionException {
    	return this.apiSendAsync(messageBuilder, entity, Boolean.FALSE);
    }

    /**
     * Subscribe the specified topic by the specified registration tokens
     * @param tokens to subscribe
     * @param topic to subscrible
     * @return {@link TopicManagementResponse}
     */
	public TopicManagementResponse subscribeToTopic(List<String> tokens, String topic) {
    	Assert.notEmpty(tokens, "tokens");
    	Assert.hasText(topic, "topic");
    	try {
			return this.getMessaging().subscribeToTopic(tokens, topic);
		} catch (FirebaseMessagingException e) {
			logger.error(e.getMessage(), e);
    		throw new RuntimeException(e);
		}
    }
    /**
     * Subscribe the specified topic by the specified registration tokens
     * @param tokens to subscribe
     * @param topic to subscrible
     * @return {@link TopicManagementResponse}
     */
	@Async
	public CompletableFuture<TopicManagementResponse> apiSubscribeToTopic(List<String> tokens, String topic) {
		return CompletableFuture.completedFuture(this.subscribeToTopic(tokens, topic));
    }

    /**
     * Subscribe the specified topic by the specified registration tokens
     * @param tokens to subscribe
     * @param topic to subscrible
     * @return {@link ApiFuture}
     */
	public ApiFuture<TopicManagementResponse> subscribeToTopicAsync(List<String> tokens, String topic) {
    	Assert.notEmpty(tokens, "tokens");
    	Assert.hasText(topic, "topic");
		return this.getMessaging().subscribeToTopicAsync(tokens, topic);
    }
    /**
     * Subscribe the specified topic by the specified registration tokens
     * @param tokens to subscribe
     * @param topic to subscrible
     * @return {@link CompletableFuture}
     * @throws ExecutionException thrown if failed
     * @throws InterruptedException thrown if failed
     */
	@Async
	public CompletableFuture<TopicManagementResponse> apiSubscribeToTopicAsync(List<String> tokens, String topic)
			throws InterruptedException, ExecutionException {
		return CompletableFuture.completedFuture(this.subscribeToTopicAsync(tokens, topic).get());
    }

    /**
     * Un-subscribe the specified topic by the specified registration tokens
     * @param tokens to subscribe
     * @param topic to subscrible
     * @return {@link TopicManagementResponse}
     */
	public TopicManagementResponse unsubscribeFromTopic(List<String> tokens, String topic) {
    	Assert.notEmpty(tokens, "tokens");
    	Assert.hasText(topic, "topic");
    	try {
			return this.getMessaging().unsubscribeFromTopic(tokens, topic);
		} catch (FirebaseMessagingException e) {
			logger.error(e.getMessage(), e);
    		throw new RuntimeException(e);
		}
    }
	/**
     * Subscribe the specified topic by the specified registration tokens
     * @param tokens to subscribe
     * @param topic to subscrible
     * @return {@link CompletableFuture}
     */
	@Async
	public CompletableFuture<TopicManagementResponse> apiUnsubscribeFromTopic(List<String> tokens, String topic) {
		return CompletableFuture.completedFuture(this.unsubscribeFromTopic(tokens, topic));
    }

	/**
     * Un-subscribe the specified topic by the specified registration tokens
     * @param tokens to subscribe
     * @param topic to subscrible
     * @return {@link ApiFuture}
     */
	public ApiFuture<TopicManagementResponse> unsubscribeFromTopicAsync(List<String> tokens, String topic) {
    	Assert.notEmpty(tokens, "tokens");
    	Assert.hasText(topic, "topic");
		return this.getMessaging().unsubscribeFromTopicAsync(tokens, topic);
    }
	/**
     * Subscribe the specified topic by the specified registration tokens
     * @param tokens to subscribe
     * @param topic to subscrible
     * @return {@link CompletableFuture}
     * @throws ExecutionException thrown if failed
     * @throws InterruptedException thrown if failed
     */
	@Async
	public CompletableFuture<TopicManagementResponse> apiUnsubscribeFromTopicAsync(List<String> tokens, String topic)
			throws InterruptedException, ExecutionException {
		return CompletableFuture.completedFuture(this.unsubscribeFromTopicAsync(tokens, topic).get());
    }

	/**
	 * Create devices group for receiving notification by the specified notification key name
	 * @param tokens device token to resgiter into group
	 * @param notificationKeyName to create
	 * @return FCM notification key
	 * @throws Exception thrown if failed
	 */
	public String createDevicesGroup(List<String> tokens, String notificationKeyName) throws Exception {
		// check parameters
		Assert.hasText(notificationKeyName, "notificationKeyName");
		Assert.notEmpty(tokens, "tokens");
		String serverKey = this.getAdminServerKey();
		Assert.hasText(serverKey, "Not implement API key! Please implement via getter/setter adminServerKey!");
		String senderId = this.getSenderId();
		Assert.hasText(senderId, "Not implement senderId! Please implement via getter/setter senderId!");
		String url = this.getNotificationURL();
		Assert.hasText(url, "Not implement notification URL! Please implement via getter/setter notificationURL!");
		UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
		Assert.isTrue(validator.isValid(url), "Invalid notification URL {" + url + "}");
		// create HTTP Headers
		Map<String, String> headers = new LinkedHashMap<String, String>();
		headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.put(HttpHeaders.AUTHORIZATION, MessageFormat.format("key={0}", serverKey));
		headers.put("project_id", senderId);
		// request body
		Map<String, Object> reqBody = new LinkedHashMap<String, Object>();
		reqBody.put("operation", "create");
		reqBody.put("notification_key_name", notificationKeyName);
		reqBody.put("registration_ids", tokens);
		// send request to create device group
		logger.debug("CREATE_DEVICES_GROUP {0}", url);
		String response = HttpUtils.restPost(url, headers, reqBody, null, String.class);
		JSONObject jsonResp = new JSONObject(response);
		return (jsonResp.length() <= 0 || jsonResp.has("error") || !jsonResp.has("notification_key")
				? null : jsonResp.getString("notification_key"));
	}
	/**
	 * Create devices group for receiving notification by the specified notification key name
	 * @param tokens device token to resgiter into group
	 * @param notificationKeyName to create
	 * @return FCM notification key
	 * @throws Exception thrown if failed
	 */
	@Async
	public CompletableFuture<String> asyncCreateDevicesGroup(final List<String> tokens, final String notificationKeyName) throws Exception {
		return CompletableFuture.completedFuture(this.createDevicesGroup(tokens, notificationKeyName));
	}

	/**
	 * Add tokens to devices group for receiving notification by the specified notification key name
	 * @param tokens device token to resgiter into group
	 * @param notificationKeyName to add
	 * @param notificationKey unique notification key that has been created
	 * @return FCM notification key
	 * @throws Exception thrown if failed
	 */
	public String addToDevicesGroup(List<String> tokens, String notificationKeyName, String notificationKey) throws Exception {
		// check parameters
		Assert.hasText(notificationKeyName, "notificationKeyName");
		Assert.hasText(notificationKey, "notificationKey");
		Assert.notEmpty(tokens, "tokens");
		String serverKey = this.getAdminServerKey();
		Assert.hasText(serverKey, "Not implement API key! Please implement via getter/setter adminServerKey!");
		String senderId = this.getSenderId();
		Assert.hasText(senderId, "Not implement senderId! Please implement via getter/setter senderId!");
		String url = this.getNotificationURL();
		Assert.hasText(url, "Not implement notification URL! Please implement via getter/setter notificationURL!");
		UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
		Assert.isTrue(validator.isValid(url), "Invalid notification URL {" + url + "}");
		// create HTTP Headers
		Map<String, String> headers = new LinkedHashMap<String, String>();
		headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.put(HttpHeaders.AUTHORIZATION, MessageFormat.format("key={0}", serverKey));
		headers.put("project_id", senderId);
		// request body
		Map<String, Object> reqBody = new LinkedHashMap<String, Object>();
		reqBody.put("operation", "add");
		reqBody.put("notification_key_name", notificationKeyName);
		reqBody.put("notification_key", notificationKey);
		reqBody.put("registration_ids", tokens);
		// send request to create device group
		logger.debug("ADD_TO_DEVICES_GROUP {0}", url);
		String response = HttpUtils.restPost(url, headers, reqBody, null, String.class);
		JSONObject jsonResp = new JSONObject(response);
		return (jsonResp.length() <= 0 || jsonResp.has("error") || !jsonResp.has("notification_key")
				? null : jsonResp.getString("notification_key"));
	}
	/**
	 * Add tokens to devices group for receiving notification by the specified notification key name
	 * @param tokens device token to resgiter into group
	 * @param notificationKeyName to add
	 * @param notificationKey unique notification key that has been created
	 * @return FCM notification key
	 * @throws Exception thrown if failed
	 */
	@Async
	public CompletableFuture<String> asyncAddToDevicesGroup(final List<String> tokens, final String notificationKeyName, final String notificationKey) throws Exception {
		return CompletableFuture.completedFuture(this.addToDevicesGroup(tokens, notificationKeyName, notificationKey));
	}

	/**
	 * Remove tokens out of devices group by the specified notification key name
	 * @param tokens device token to remove out of group
	 * @param notificationKeyName to remove
	 * @param notificationKey unique notification key that has been created
	 * @return FCM notification key
	 * @throws Exception thrown if failed
	 */
	public String removeOutDevicesGroup(List<String> tokens, String notificationKeyName, String notificationKey) throws Exception {
		// check parameters
		Assert.hasText(notificationKeyName, "notificationKeyName");
		Assert.hasText(notificationKey, "notificationKey");
		Assert.notEmpty(tokens, "tokens");
		String serverKey = this.getAdminServerKey();
		Assert.hasText(serverKey, "Not implement API key! Please implement via getter/setter adminServerKey!");
		String senderId = this.getSenderId();
		Assert.hasText(senderId, "Not implement senderId! Please implement via getter/setter senderId!");
		String url = this.getNotificationURL();
		Assert.hasText(url, "Not implement notification URL! Please implement via getter/setter notificationURL!");
		UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
		Assert.isTrue(validator.isValid(url), "Invalid notification URL {" + url + "}");
		// create HTTP Headers
		Map<String, String> headers = new LinkedHashMap<String, String>();
		headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.put(HttpHeaders.AUTHORIZATION, MessageFormat.format("key={0}", serverKey));
		headers.put("project_id", senderId);
		// request body
		Map<String, Object> reqBody = new LinkedHashMap<String, Object>();
		reqBody.put("operation", "remove");
		reqBody.put("notification_key_name", notificationKeyName);
		reqBody.put("notification_key", notificationKey);
		reqBody.put("registration_ids", tokens);
		// send request to create device group
		String response = HttpUtils.restPost(url, headers, reqBody, null, String.class);
		JSONObject jsonResp = new JSONObject(response);
		return (jsonResp.length() <= 0 || jsonResp.has("error") || !jsonResp.has("notification_key")
				? null : jsonResp.getString("notification_key"));
	}
	/**
	 * Remove tokens out of devices group by the specified notification key name
	 * @param tokens device token to remove out of group
	 * @param notificationKeyName to remove
	 * @param notificationKey unique notification key that has been created
	 * @return FCM notification key
	 * @throws Exception thrown if failed
	 */
	@Async
	public CompletableFuture<String> asyncRemoveOutDevicesGroup(final List<String> tokens, final String notificationKeyName, final String notificationKey) throws Exception {
		return CompletableFuture.completedFuture(this.removeOutDevicesGroup(tokens, notificationKeyName, notificationKey));
	}

	/**
	 * Check devices group whether has been registered
	 * @param notificationKeyName to add
	 * @return FCM notification key
	 * @throws Exception thrown if failed
	 */
	public String requireNotificationKey(String notificationKeyName) throws Exception {
		// check parameters
		Assert.hasText(notificationKeyName, "notificationKeyName");
		String serverKey = this.getAdminServerKey();
		Assert.hasText(serverKey, "Not implement API key! Please implement via getter/setter adminServerKey!");
		String senderId = this.getSenderId();
		Assert.hasText(senderId, "Not implement senderId! Please implement via getter/setter senderId!");
		String url = this.getNotificationURL();
		Assert.hasText(url, "Not implement notification URL! Please implement via getter/setter notificationURL!");
		url += (url.indexOf("?") < 0 ? "?" : "&");
		url += ("notification_key_name=" + notificationKeyName);
		UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
		Assert.isTrue(validator.isValid(url), "Invalid notification URL {" + url + "}");
		// create HTTP Headers
		Map<String, String> headers = new LinkedHashMap<String, String>();
		headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.put(HttpHeaders.AUTHORIZATION, MessageFormat.format("key={0}", serverKey));
		headers.put("project_id", senderId);
		// send request to create device group
		String response = HttpUtils.restGet(url, headers, null, null, String.class);
		JSONObject jsonResp = new JSONObject(response);
		return (jsonResp.length() <= 0 || jsonResp.has("error") || !jsonResp.has("notification_key")
				? null : jsonResp.getString("notification_key"));
	}
	/**
	 * Check devices group whether has been registered
	 * @param notificationKeyName to add
	 * @return FCM notification key
	 * @throws Exception thrown if failed
	 */
	@Async
	public CompletableFuture<String> asyncRequireNotificationKey(final String notificationKeyName) throws Exception {
		return CompletableFuture.completedFuture(this.requireNotificationKey(notificationKeyName));
	}

	/**
	 * Send notification data to devices group by the specified notification key name
	 * @param tokens device token to resgiter into group
	 * @param notificationKeyName to add
	 * @param data notification data
	 * @return HTTP Json response such as: { success: 2, failure: 0 }
	 * @throws Exception thrown if failed
	 */
	public JSONObject sendToDevicesGroup(String notificationKeyName, Map<?, ?> data) throws Exception {
		// check parameters
		Assert.hasText(notificationKeyName, "notificationKeyName");
		String serverKey = this.getAdminServerKey();
		Assert.hasText(serverKey, "Not implement API key! Please implement via getter/setter adminServerKey!");
		String url = this.getNotificationSendURL();
		Assert.hasText(url, "Not implement notification sending URL! Please implement via getter/setter notificationSendURL!");
		UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
		Assert.isTrue(validator.isValid(url), "Invalid notification URL {" + url + "}");
		// create HTTP Headers
		Map<String, String> headers = new LinkedHashMap<String, String>();
		headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		headers.put(HttpHeaders.AUTHORIZATION, MessageFormat.format("key={0}", serverKey));
		// request body
		Map<String, Object> reqBody = new LinkedHashMap<String, Object>();
		reqBody.put("to", notificationKeyName);
		reqBody.put("data", data);
		// send request to create device group
		String response = HttpUtils.restPost(url, headers, reqBody, null, String.class);
		return (new JSONObject(response));
	}
	/**
	 * Send notification data to devices group by the specified notification key name
	 * @param tokens device token to resgiter into group
	 * @param notificationKeyName to add
	 * @param data notification data
	 * @return HTTP Json response such as: { success: 2, failure: 0 }
	 * @throws Exception thrown if failed
	 */
	@Async
	public CompletableFuture<JSONObject> asyncSendToDevicesGroup(final String notificationKeyName, final Map<?, ?> data) throws Exception {
		return CompletableFuture.completedFuture(this.sendToDevicesGroup(notificationKeyName, data));
	}
}
