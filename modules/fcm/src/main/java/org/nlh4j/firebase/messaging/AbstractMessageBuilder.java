/*
 * @(#)IMessageBuilder.java
 * Copyright 2018 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.firebase.messaging;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDateTime;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Message.Builder;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;

import lombok.Getter;
import lombok.Setter;

/**
 * Abstract implement of {@link IMessageBuilder}
 *
 * @param <T> notification body data type
 */
@Component
@Singleton
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AbstractMessageBuilder<T> implements IMessageBuilder<T> {

	/** */
    private static final long serialVersionUID = 1L;

    /** slf4j */
    protected final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    /** Notification title */
    @Getter
    @Setter
    private String title;
    /** Notification body */
    @Getter
    @Setter
    private String body;

    /**
     * Initialize a new instance of {@link AbstractMessageBuilder}
     *
     */
    protected AbstractMessageBuilder() {
    	this(null, null);
    }
    /**
     * Initialize a new instance of {@link AbstractMessageBuilder}
     *
     * @param title notification title
     */
    protected AbstractMessageBuilder(String title) {
    	this(title, null);
    }
    /**
     * Initialize a new instance of {@link AbstractMessageBuilder}
     *
     * @param title notification title
     */
    protected AbstractMessageBuilder(String title, String body) {
    	title = (!StringUtils.isBlank(title) ? this.getClass().getSimpleName() : title);
    	this.setTitle(title);
    	this.setBody(body);
    }

    /**
     * Build {@link AndroidConfig} notification configuration<br>
     * TODO Children class maybe override to custom notification configuration for Android device(s)
     * @return {@link AndroidConfig} notification configuration
     */
    protected AndroidConfig buildAndroidConfig() {
    	return AndroidConfig.builder()
				.setNotification(AndroidNotification.builder().build())
				.build();
    }
    /**
     * Build {@link ApnsConfig} notification configuration<br>
     * TODO Children class maybe override to custom notification configuration for Apple device(s)
     * @return {@link ApnsConfig} notification configuration
     */
    protected ApnsConfig buildIosConfig() {
    	return ApnsConfig.builder()
				.setAps(Aps.builder().build())
				.build();
    }
    /**
     * Build {@link WebpushConfig} notification configuration<br>
     * TODO Children class maybe override to custom notification configuration for Web-push browser
     * @return {@link WebpushConfig} notification configuration
     */
    protected WebpushConfig buildWebPushConfig() {
    	return WebpushConfig.builder().build();
    }
    /**
     * Internal support for building data map recursively from the speicifed data
     * @param retDataMap returned data {@link Map}
     * @param rootKey parent key map
     * @param data to build
     */
    @SuppressWarnings("unchecked")
	protected final <K> void buildDataMap(Map<String, String> retDataMap, String rootKey, K data) {
    	retDataMap = (retDataMap == null ? new LinkedHashMap<String, String>() : retDataMap);
    	// if data is a map
    	if (CollectionUtils.isMap(data)) {
    		Map<?, ?> dataMap = BeanUtils.safeType(data, Map.class);
			if (!CollectionUtils.isEmpty(dataMap)) {
				for(final Iterator<?> it = dataMap.keySet().iterator(); it.hasNext();) {
					Object key = it.next();
					Object value = dataMap.get(key);
					if (key == null || StringUtils.isBlank(String.valueOf(key))) continue;
					String sKey = rootKey;
					sKey = (StringUtils.isBlank(sKey) ? "" : (sKey + "."));
					sKey += String.valueOf(key);
					this.buildDataMap(retDataMap, sKey, value);
				}
			}

			// if data is a collection
    	} else if (CollectionUtils.isCollection(data) || CollectionUtils.isArray(data)) {
    		String json = null;
    		try { json = JsonUtils.manualSerialize(data); }
    		catch (Exception e) { json = null; }
    		if (!StringUtils.isBlank(json)) {
    			String sKey = rootKey;
				sKey = (StringUtils.isBlank(sKey) ? "value" : sKey);
				retDataMap.put(sKey, json);
    		} else {
	    		Collection<?> dataCol = BeanUtils.safeType(data, Collection.class);
				if (!CollectionUtils.isEmpty(dataCol)) {
					for(final Iterator<?> it = dataCol.iterator(); it.hasNext();) {
						Object value = it.next();
						this.buildDataMap(retDataMap, rootKey, value);
					}
				}
    		}

			// if data is not a primitive data
    	} else if (!BeanUtils.isPrimitive(data.getClass())) {
    		// check if data is a instance of class and has properties
    		List<String> fields = (BeanUtils.isInstanceOf(data, String.class)
    				|| BeanUtils.isInstanceOf(data, Number.class)
    				|| BeanUtils.isInstanceOf(data, Date.class)
    				? null : BeanUtils.getFieldNames(data.getClass(), Boolean.FALSE, Boolean.TRUE, null));
    		if (CollectionUtils.isEmpty(fields)) {
    			// parse notification data as JSON
    			String sVal = String.valueOf(data);
    			if (BeanUtils.isInstanceOf(data, Date.class)) {
    				BsonDateTime bson = new BsonDateTime(BeanUtils.safeType(data, Date.class).getTime());
    				sVal = String.valueOf(bson.getValue());
    			}
    			String sKey = rootKey;
				sKey = (StringUtils.isBlank(sKey) ? "value" : sKey);
				retDataMap.put(sKey, sVal);

				// check by value object properties
			} else {
				List<K> dataLst = new LinkedList<K>();
				dataLst.add(data);
				List<Map<String, Object>> dataMapLst = BeanUtils.mapPropertyValuesMap(
						(Class<K>) data.getClass(), Boolean.TRUE, dataLst, (String[]) null);
				if (!CollectionUtils.isEmpty(dataMapLst)) {
					Map<String, Object> dataObjMap = dataMapLst.get(0);
					for(final Iterator<String> it = dataObjMap.keySet().iterator(); it.hasNext();) {
						String key = it.next();
						Object keyData = dataObjMap.get(key);
						if (StringUtils.isBlank(key)) continue;
						String sKey = rootKey;
						sKey = (StringUtils.isBlank(sKey) ? "" : (sKey + "."));
						sKey += key;
						this.buildDataMap(retDataMap, sKey, keyData);
					}
				}
			}

    		// primitive type
    	} else if (data != null) {
    		String sKey = rootKey;
			sKey = (StringUtils.isBlank(sKey) ? "value" : sKey);
    		retDataMap.put(sKey, String.valueOf(data));
    	}
    }
    /**
     * Build notification data from the specified entity data<br>
     * TODO Children class maybe override to custom building notification data
     * @param data to build
     * @return notification data
     */
	protected Map<String, String> buildNotificationData(T data) {
    	Map<String, String> dataMap = new LinkedHashMap<String, String>();
    	if (data != null) this.buildDataMap(dataMap, null, data);
    	return dataMap;
    }

    /**
     * Apply receivers for message builder
     * TODO Children classes maybe override this method for applying message receivers
     * @param builder message builder after applying settings, title, body, data, etc.
     * @param data data that has used to build message
     */
    protected void applyReceiver(Builder builder, T data) {
    	// Do nothing
    }

	/* (Non-Javadoc)
	 * @see com.api.mongo.model.FirebaseService.IMessageBuilder#build(java.lang.Object)
	 */
	@Override
	public final Builder build(T data) {
		Builder builder = Message.builder();
		// TODO Fix for applying notification configuration on devices
		// build notification configuration
		//	builder = builder.setNotification(new Notification(this.getTitle(), this.getBody()))
		//			.setAndroidConfig(this.buildAndroidConfig())
		//			.setApnsConfig(this.buildIosConfig())
		//			.setWebpushConfig(this.buildWebPushConfig());
		// build notification data
		Map<String, String> notifData = this.buildNotificationData(data);
		if (!CollectionUtils.isEmpty(notifData)) builder = builder.putAllData(notifData);
		// apply receiver
		this.applyReceiver(builder, data);
		// apply notification title/body
		if (!StringUtils.isBlank(this.getTitle()) || !StringUtils.isBlank(this.getBody())) {
			builder = builder.setNotification(new Notification(this.getTitle(), this.getBody()));
		}
		return builder;
	}
}
