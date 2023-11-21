/*
 * @(#)JsonHttpAsyncTask.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.tasks;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import org.nlh4j.android.util.LogUtils;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.util.HttpUtils;
import org.nlh4j.util.HttpUtils.JsonResult;
import org.nlh4j.util.JsonUtils;

/**
 * HTTP JSON request a-synchronized task
 *
 * @author Hai Nguyen
 *
 * @param <Params> the task parameter type
 * @param <Progress> the task progress to update
 * @param <T> the result entity class to de-serialize JSON response
 */
@SuppressWarnings("unchecked")
public abstract class JsonHttpAsyncTask<Params, Progress, T>
    extends AbstractAsyncTask<Params, Progress, T> {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    protected final static String HEADER_ACCEPT = "Accept";
    protected final static String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    protected final static String HEADER_ACCEPT_ENCODING_VALUE = "gzip, deflate, br";
    protected final static String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
    protected final static String HEADER_ACCEPT_LANGUAGE_VALUE = "vi,ja;q=0.8,en-US;q=0.6,en;q=0.4";
    protected final static String HEADER_CONTENT_TYPE = "Content-Type";

    /** result entity class */
    private Class<T> entityClass;
    /**
     * Get the result entity class
     *
     * @return the result entity class
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }

    /** the JSON response after de-serializing */
    private JsonResult<T> jsonResult;
    /**
     * Get the JSON response after de-serializing
     *
     * @return the JSON response after de-serializing
     */
    public JsonResult<T> getJsonResult() {
        return jsonResult;
    }

    /**
     * Get the HTTP request URL
     *
     * @return the HTTP request URL
     */
    protected abstract String getUrl();

    /**
     * Get the HTTP method
     *
     * @return the HTTP method
     */
    protected abstract HttpMethod getMethod();

    /** the HTTP response status code */
    private int status = HttpStatus.SC_OK;
    /**
     * Get the HTTP response status code
     *
     * @return the HTTP response status code
     */
    public final int getHttpStatus() {
        return this.status;
    }

    /**
     * Get the request headers
     * TODO children classes maybe override this method for inserting/updating/deleting request headers
     *
     * @return the request headers
     */
    protected Map<String, String> getHeaders() {
        // TODO children classes maybe override this method for inserting/updating/deleting request headers
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put(HEADER_ACCEPT, MediaType.APPLICATION_JSON);
        headers.put(HEADER_ACCEPT_ENCODING, HEADER_ACCEPT_ENCODING_VALUE);
        headers.put(HEADER_ACCEPT_LANGUAGE, HEADER_ACCEPT_LANGUAGE_VALUE);
        headers.put(HEADER_CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    /**
     * Get the request parameters
     *
     * @return the request parameters
     */
    protected abstract Map<String, String> getParameters();

    /**
     * Get the HTTP proxy if necessary
     * TODO children classes maybe override this method for proxy
     *
     * @return the HTTP proxy if necessary
     */
    protected HttpHost getProxy() {
        // TODO children classes maybe override this method for proxy
        return null;
    }

    /**
     * Initialize a new instance of {@link JsonHttpAsyncTask}
     *
     * @param entityClass the entity class to de-serialize response JSON
     */
    public JsonHttpAsyncTask(Class<T> entityClass) {
        this(entityClass, RETRY_MAX_ATTEMPTS, RETRY_INIT_BACKOFF, null);
    }
    /**
     * Initialize a new instance of {@link JsonHttpAsyncTask}
     *
     * @param entityClass entityClass the entity class to de-serialize response JSON
     * @param listener the posting execution listener
     */
    public JsonHttpAsyncTask(Class<T> entityClass, AsyncTaskListener<Params, Progress, T> listener) {
        this(entityClass, RETRY_MAX_ATTEMPTS, RETRY_INIT_BACKOFF, listener);
    }
    /**
     * Initialize a new instance of {@link JsonHttpAsyncTask}
     *
     * @param entityClass the entity class to de-serialize JSON response
     * @param maxAttempts the maximum attempts number
     * @param sleepTime the sleep time (milliseconds) every attempting time
     * @param listener the posting execution listener
     */
    public JsonHttpAsyncTask(
            Class<T> entityClass,
            int maxAttempts, long sleepTime,
            AsyncTaskListener<Params, Progress, T> listener) {
        Assert.notNull(entityClass, "entityClass");
        this.entityClass = entityClass;
        this.setMaxAttempts(maxAttempts);
        this.setSleepTime(sleepTime);
        this.setListener(listener);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.android.tasks.AbstractAsyncTask#doTask(java.lang.Object[])
     */
    @Override
    protected T doTask(Params... params) throws ApplicationRuntimeException {
        Assert.notNull(this.getMethod(), "HTTP Method");

        // send request
        T result = null;
        StringBuffer respBuf = new StringBuffer();
        try {
            this.status = HttpUtils.sendStatus(
                    this.getUrl(), this.getMethod(),
                    this.getHeaders(),
                    this.getParameters(),
                    this.getProxy(),
                    respBuf);
        }
        catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            throw new ApplicationRuntimeException(e);
        }

        // status okie
        if (HttpStatus.SC_OK <= this.status
                && this.status < HttpStatus.SC_MULTIPLE_CHOICES
                && respBuf.length() > 0) {
            // de-serialize JSON response
            result = JsonUtils.deserialize(respBuf.toString(), this.getEntityClass());
        }

        // result
        return result;
    }
}
