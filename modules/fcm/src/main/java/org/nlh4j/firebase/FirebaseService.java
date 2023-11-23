/*
 * @(#)FirebaseService.java
 * Copyright 2018 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.firebase;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;

import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Firebase Service
 *
 * @author Hai Nguyen
 *
 */
@Component
@Service
@Singleton
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class FirebaseService implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    protected static final String FIREBASE_DATABASE_URI_PATTERN = "https://{0}.firebaseio.com/";

    /** slf4j */
    protected final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Firebase Information
     */
    @Getter(value = AccessLevel.PROTECTED)
    private transient FirebaseApp app;
    @Getter(value = AccessLevel.PROTECTED)
    private transient FirebaseOptions options;
    @Getter(value = AccessLevel.PROTECTED)
    private ServiceAccountCredentials serviceAccountCredentials;
    @Getter(value = AccessLevel.PROTECTED)
    private transient FirebaseAuth auth;
    @Getter(value = AccessLevel.PROTECTED)
    private transient FirebaseDatabase database;
    @Getter(value = AccessLevel.PROTECTED)
    private transient FirebaseInstanceId instanceId;
    @Getter(value = AccessLevel.PROTECTED)
    private transient FirebaseMessaging messaging;
    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PRIVATE)
    private String adminSdkResource;
    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PRIVATE)
    private String adminSdkDatabase;
    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PRIVATE)
    private String adminServerKey;
    /** https://fcm.googleapis.com/fcm/notification */
    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PROTECTED)
    private String notificationURL;
    /** https://fcm.googleapis.com/fcm/send */
    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PROTECTED)
    private String notificationSendURL;
    /** Sender ID: 47046415657 */
    @Getter(value = AccessLevel.PROTECTED)
    @Setter(value = AccessLevel.PROTECTED)
    private String senderId;

    /**
     * Initialize a new instance of {@link FirebaseService}
     *
     * @param adminSdkAccJson the path to adminSDK account JSON file
     * @param database adminSDK database name
     * @param serverKey firebase server key
     *
     * @throws IOException thrown if could not read adminSQK service account stream
     */
    protected FirebaseService(String adminSdkAccJson, String database, String serverKey) throws IOException {
        // check configuration
    	this.setAdminSdkResource(adminSdkAccJson);
    	this.setAdminSdkDatabase(database);
    	this.setAdminServerKey(serverKey);
    	if (StringUtils.isBlank(this.getAdminSdkResource())) {
    		throw new IllegalArgumentException("AdminSDK Account JSON Resource");
    	}
    	if (StringUtils.isBlank(this.getAdminSdkDatabase())) {
    		throw new IllegalArgumentException("AdminSDK Database Name");
    	}
    	if (StringUtils.isBlank(this.getAdminServerKey())) {
    		throw new IllegalArgumentException("Firebase Server Key");
    	}

    	// require input stream from adminSDK account
        InputStream is = SpringContextHelper.findFirstResourceAsStream(this.getAdminSdkResource());
        if (is == null) {
        	throw new IllegalArgumentException(
        			"Not found adminSDK account JSON resource [" + this.getAdminSdkResource() + "]");
        }
        // initialize adminSDK
        FirebaseOptions fbOpts = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(is))
                .setDatabaseUrl(MessageFormat.format(FIREBASE_DATABASE_URI_PATTERN, this.getAdminSdkDatabase()))
                .build();
        // release resource stream
        try { is.close(); }
        catch (Exception e) {}

        // requires all application
        List<FirebaseApp> apps = FirebaseApp.getApps();
        if (CollectionUtils.isEmpty(apps)) {
            this.app = FirebaseApp.initializeApp(fbOpts);
        } else {
        	logger.debug("+ FIREBASE APPS: ");
            int i = 1;
            for(FirebaseApp app : apps) {
            	logger.debug(MessageFormat.format("{0}. {1}", String.valueOf(i), app.getName()));
            }
            logger.debug("++++++++++++++++++ ");
            this.app = FirebaseApp.getInstance();
        }
        if (this.app == null) {
        	throw new IllegalArgumentException("Could not found or initialize FirebaseApp!");
        }
        this.options = this.getApp().getOptions();
        this.serviceAccountCredentials = (this.options == null ? null
        		: BeanUtils.safeType(BeanUtils.getFieldValue(this.options, "credentials"),
        				ServiceAccountCredentials.class));
        this.auth = FirebaseAuth.getInstance(this.getApp());
        this.database = FirebaseDatabase.getInstance(this.getApp());
        this.instanceId = FirebaseInstanceId.getInstance(this.getApp());
        this.messaging = FirebaseMessaging.getInstance(this.getApp());
    }

    /**
     * Retrieve {@link DatabaseReference}
     * @param path path to database reference
     * @return {@link DatabaseReference}
     */
    protected final DatabaseReference getDbRef(String path) {
        return (this.getDatabase() != null && !StringUtils.isBlank(path)
                ? this.getDatabase().getReference(path) : this.getDatabase() != null
                ? this.getDatabase().getReference() : null);
    }

    /**
     * Get firebase project identity from {@link FirebaseApp}
     * @return firebase project identity from {@link FirebaseApp}
     */
    protected final String getProjectId() {
    	if (this.getOptions() == null) return null;
    	String projectId = this.getOptions().getProjectId();
    	if (StringUtils.isBlank(projectId) && this.getServiceAccountCredentials() != null) {
    		projectId = this.getServiceAccountCredentials().getProjectId();
    	}
    	return projectId;
    }
    /**
     * Get firebase client identity from {@link FirebaseApp}
     * @return firebase client identity from {@link FirebaseApp}
     */
    protected final String getClientId() {
    	return (this.getServiceAccountCredentials() == null ? null
    			: this.getServiceAccountCredentials().getClientId());
    }
}
