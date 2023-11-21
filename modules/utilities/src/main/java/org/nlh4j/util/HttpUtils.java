/*
 * @(#)HttpUtils.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 * Utilities for sending/posting request to an URL and receive response from that URL
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class HttpUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    private static final String ENCODING_UTF8 = "UTF-8";

    /**
     * The entity for receiving/responding the JSON serialization/deserialization result
     *
     * @author Hai Nguyen (hainguyenjc@gmail.com)
     */
    @JsonCachable(value = true)
    @JsonSerialize(include = Inclusion.NON_NULL)
    public static class JsonResult<T> implements Serializable {

        /**
         * serialVersionUID
         */
        private static final long serialVersionUID = 1L;

        /**
         * Specifies the JSON serialization/deserialization whether is success
         */
        private boolean success = false;

        /**
         * The errors in un-success case
         */
        private List<String> errors = new LinkedList<String>();

        /**
         * The attached JSON elements
         */
        private transient List<T> elements = new LinkedList<T>();

        /**
         * Initializes a new instance of the {@link JsonResult} class.
         *
         */
        public JsonResult() {}

        /**
         * Initializes a new instance of the {@link JsonResult} class.
         *
         * @param success specify processing whether is success
         * @param errors the errors list
         * @param elements to serialize/de-serialize
         */
        public JsonResult(
                @JsonProperty(value = "success") boolean success,
                @JsonProperty(value = "errors") List<String> errors,
                @JsonProperty(value = "elements") List<T> elements) {
            this.setSuccess(success);
            if (!CollectionUtils.isEmpty(errors)) this.getErrors().addAll(errors);
            if (!CollectionUtils.isEmpty(elements)) this.getElements().addAll(elements);
        }

        /**
         * Gets the <b>success</b> value
         *
         * @return the <b>success</b> value
         */
        @JsonProperty(value = "success")
        public boolean isSuccess() {
            return this.success;
        }
        /**
         * Sets the <b>success</b> value
         *
         * @param success
         *          the <b>success</b> value to set
         */
        public void setSuccess(boolean success) {
            this.success = success;
        }

        /**
         * Gets the <b>errors</b> value
         *
         * @return the <b>errors</b> value
         */
        @JsonProperty(value = "errors")
        public List<String> getErrors() {
            return this.errors;
        }

        /**
         * Gets the <b>elements</b> value
         *
         * @return the <b>elements</b> value
         */
        @JsonProperty(value = "elements")
        public List<T> getElements() {
            return this.elements;
        }
    }

    /**
     * Multiple parameters
     *
     * @author Hai Nguyen (hainguyenjc@gmail.com)
     *
     */
     @SuppressWarnings("unchecked")
     public static class MultivaluedParameter<T, K> extends LinkedHashMap<T, List<K>>
            implements MultivaluedMap<T, K>, Serializable {

        /**
         * serialVersionUID
         */
        private static final long serialVersionUID = 1L;

        /**
         * Initializes a new instance of the {@link MultivaluedParameter} class.
         */
        public MultivaluedParameter() {}
        /**
         * Initializes a new instance of the {@link MultivaluedParameter} class.
         *
         * @param initialCapacity the initial capacity
         * @param loadFactor the load factor
         */
        public MultivaluedParameter(int initialCapacity, float loadFactor) {
            super(initialCapacity, loadFactor);
        }
        /**
         * Initializes a new instance of the {@link MultivaluedParameter} class.
         *
         * @param initialCapacity the initial capacity
         */
        public MultivaluedParameter(int initialCapacity) {
            super(initialCapacity);
        }
        /**
         * Initializes a new instance of the {@link MultivaluedParameter} class.
         *
         * @param m the map whose mappings are to be placed in this map
         */
        public MultivaluedParameter(Map<? extends T, ? extends List<K>> m) {
            super(m);
        }

        /*
         * (Non-Javadoc)
         * @see java.util.HashMap#size()
         */
        @Override
        public int size() {
            return super.size();
        }

        /*
         * (Non-Javadoc)
         * @see java.util.HashMap#isEmpty()
         */
        @Override
        public boolean isEmpty() {
            return super.isEmpty();
        }

        /*
         * (Non-Javadoc)
         * @see java.util.HashMap#containsKey(java.lang.Object)
         */
        @Override
        public boolean containsKey(Object key) {
            return super.containsKey(key);
        }

        /*
         * (Non-Javadoc)
         * @see java.util.LinkedHashMap#containsValue(java.lang.Object)
         */
        @Override
        public boolean containsValue(Object value) {
            return super.containsValue(value);
        }

        /*
         * (Non-Javadoc)
         * @see java.util.LinkedHashMap#get(java.lang.Object)
         */
        @Override
        public List<K> get(Object key) {
            return super.get(key);
        }

        /*
         * (Non-Javadoc)
         * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
         */
        @Override
        public List<K> put(T key, List<K> value) {
            return super.put(key, value);
        }

        /*
         * (Non-Javadoc)
         * @see java.util.HashMap#remove(java.lang.Object)
         */
        @Override
        public List<K> remove(Object key) {
            return super.remove(key);
        }

        /*
         * (Non-Javadoc)
         * @see java.util.HashMap#putAll(java.util.Map)
         */
        @Override
        public void putAll(Map<? extends T, ? extends List<K>> m) {
            super.putAll(m);
        }

        /*
         * (Non-Javadoc)
         * @see java.util.LinkedHashMap#clear()
         */
        @Override
        public void clear() {
            super.clear();
        }

        /*
         * (Non-Javadoc)
         * @see java.util.HashMap#keySet()
         */
        @Override
        public Set<T> keySet() {
            return super.keySet();
        }

        /*
         * (Non-Javadoc)
         * @see java.util.HashMap#values()
         */
        @Override
        public Collection<List<K>> values() {
            return super.values();
        }

        /*
         * (Non-Javadoc)
         * @see java.util.HashMap#entrySet()
         */
        @Override
        public Set<java.util.Map.Entry<T, List<K>>> entrySet() {
            return super.entrySet();
        }

        /*
         * (Non-Javadoc)
         * @see javax.ws.rs.core.MultivaluedMap#putSingle(java.lang.Object, java.lang.Object)
         */
        @Override
        public void putSingle(T key, K value) {
            List<K> values = super.get(key);
            if (values == null) values = new LinkedList<K>();
            values.add(value);
            super.put(key, values);
        }

        /*
         * (Non-Javadoc)
         * @see javax.ws.rs.core.MultivaluedMap#add(java.lang.Object, java.lang.Object)
         */
        @Override
        public void add(T key, K value) {
            this.putSingle(key, value);
        }

        /*
         * (Non-Javadoc)
         * @see javax.ws.rs.core.MultivaluedMap#getFirst(java.lang.Object)
         */
        @Override
        public K getFirst(T key) {
            List<K> values = super.get(key);
            return (CollectionUtils.isEmpty(values) ? null : values.get(0));
        }

        /*
         * (Non-Javadoc)
         * @see javax.ws.rs.core.MultivaluedMap#addAll(java.lang.Object, java.lang.Object[])
         */
        public void addAll(T key, K... newValues) {
            if (!CollectionUtils.isEmpty(newValues)) {
                if (!super.containsKey(key)) {
                    super.put(key, new LinkedList<K>());
                }
                for(K newValue : newValues) {
                    super.get(key).add(newValue);
                }
            }
        }

        /* (Non-Javadoc)
         * @see javax.ws.rs.core.MultivaluedMap#addAll(java.lang.Object, java.util.List)
         */
        public void addAll(T key, List<K> valueList) {
            if (!CollectionUtils.isEmpty(valueList)) {
                if (!super.containsKey(key)) {
                    super.put(key, new LinkedList<K>());
                }
                for(K value : valueList) {
                    super.get(key).add(value);
                }
            }
        }

        /* (Non-Javadoc)
         * @see javax.ws.rs.core.MultivaluedMap#addFirst(java.lang.Object, java.lang.Object)
         */
        public void addFirst(T key, K value) {
            if (!super.containsKey(key)) {
                super.put(key, new LinkedList<K>());
            }
            super.get(key).add(0, value);
        }

        /* (Non-Javadoc)
         * @see javax.ws.rs.core.MultivaluedMap#equalsIgnoreValueOrder(javax.ws.rs.core.MultivaluedMap)
         */
        public boolean equalsIgnoreValueOrder(MultivaluedMap<T, K> otherMap) {
            if (CollectionUtils.isEmpty(this) && !CollectionUtils.isEmpty(otherMap)) return false;
            if (!CollectionUtils.isEmpty(this) && CollectionUtils.isEmpty(otherMap)) return false;
            for(final Iterator<T> it1 = this.keySet().iterator(); it1.hasNext();) {
                T key1 = it1.next();
                if (!otherMap.containsKey(key1)) return false;
                List<K> values1 = this.get(key1);
                List<K> values2 = otherMap.get(key1);
                if (CollectionUtils.isEmpty(values1) && !CollectionUtils.isEmpty(values2)) return false;
                if (!CollectionUtils.isEmpty(values1) && CollectionUtils.isEmpty(values2)) return false;
                for(K value1 : values1) {
                    if (!values2.contains(value1)) return false;
                }
            }
            return true;
        }
    }

    // -------------------------------------------------
    // SPRING: REST TEMPLATE
    // -------------------------------------------------

    /**
     * And extended class of {@link HttpComponentsClientHttpRequestFactory}
     * to build request with the specified header parameters
     */
    public static final class RequestHeadersFactory
            extends HttpComponentsClientHttpRequestFactory
            implements Serializable {

        /**
         * serialVersionUID
         */
        private static final long serialVersionUID = 1L;
        private final Map<String, String> headers;

        /**
         * Initialize a new instance of {@link RequestHeadersFactory}
         *
         * @param headers request headers
         */
        public RequestHeadersFactory(Map<String, String> headers) {
            this.headers = headers;
        }

        /* (Non-Javadoc)
         * @see org.springframework.http.client.HttpComponentsClientHttpRequestFactory#postProcessHttpRequest(org.apache.http.client.methods.HttpUriRequest)
         */
        @Override
        protected void postProcessHttpRequest(HttpUriRequest request) {
            if (!CollectionUtils.isEmpty(this.headers)) {
                for(final Iterator<String> it = this.headers.keySet().iterator(); it.hasNext();) {
                    String key = it.next();
                    if (!StringUtils.hasText(key)) continue;
                    request.addHeader(key, this.headers.get(key));
                }
            }
        }
    }

    /**
     * Send REST request to the specified URL using {@link RestTemplate}
     *
     * @param <T> the result type
     * @param <E> the request body entity type
     * @param url to send
     * @param method request method method
     * @param headers request headers
     * @param requestBody request body parameters
     * @param uriVariables the variables to expand in the template
     * @param entityClass the result class
     *
     * @return result by entity class or NULL if fail or status code &gt;= 300 or status code &lt; 200
     * @throws RestClientException if fail
     */
    public static <T, E> T restSend(
            String url, HttpMethod method,
            Map<String, String> headers,
            E requestBody,
            Map<String, String> uriVariables,
            Class<T> entityClass)
            throws RestClientException {
        Assert.hasText(url, "URL");
        Assert.notNull(method, "HTTP Method");
        Assert.notNull(entityClass, "entityClass");

        // initialize request
        RestTemplate rest = new RestTemplate(new RequestHeadersFactory(headers));

        // create request body attach headers
        HttpHeaders httpHeaders = null;
        if (requestBody != null) {
            httpHeaders = new HttpHeaders();
            if (!CollectionUtils.isEmpty(headers)) {
                for(final Iterator<String> it = headers.keySet().iterator(); it.hasNext();) {
                    String headerName = it.next();
                    httpHeaders.add(headerName, headers.get(headerName));
                }
            }
        }

        // perform request
        ResponseEntity<T> respEntity = rest.exchange(
        		url, method,
        		(requestBody == null ? null : new HttpEntity<E>(requestBody, httpHeaders)),
        		entityClass,
        		(uriVariables == null ? new LinkedHashMap<String, String>() : uriVariables));

        // by status
        if (respEntity != null
                && respEntity.getStatusCode() != null
                && respEntity.getStatusCode().is2xxSuccessful()) {
            return respEntity.getBody();
        } else {
        	System.out.println("URL [" + url + "] responsed: " + BeanUtils.toString(respEntity));
        }
        return null;
    }
    /**
     * Send REST DELETE request to the specified URL using {@link RestTemplate}
     *
     * @param <T> the result type
     * @param <E> the request body entity type
     * @param url to send
     * @param headers request headers
     * @param requestBody request body parameters
     * @param uriVariables the variables to expand in the template
     * @param entityClass the result class
     *
     * @return result by entity class or NULL if fail or status code &gt;= 300 or status code &lt; 200
     * @throws RestClientException if fail
     */
    public static <T, E> T restDelete(
            String url,
            Map<String, String> headers,
            E requestBody,
            Map<String, String> uriVariables,
            Class<T> entityClass)
            throws RestClientException {
        return restSend(url, HttpMethod.DELETE, headers, requestBody, uriVariables, entityClass);
    }
    /**
     * Send REST GET request to the specified URL using {@link RestTemplate}
     *
     * @param <T> the result type
     * @param <E> the request body entity type
     * @param url to send
     * @param headers request headers
     * @param requestBody request body parameters
     * @param uriVariables the variables to expand in the template
     * @param entityClass the result class
     *
     * @return result by entity class or NULL if fail or status code &gt;= 300 or status code &lt; 200
     * @throws RestClientException if fail
     */
    public static <T, E> T restGet(
            String url,
            Map<String, String> headers,
            E requestBody,
            Map<String, String> uriVariables,
            Class<T> entityClass)
            throws RestClientException {
        return restSend(url, HttpMethod.GET, headers, requestBody, uriVariables, entityClass);
    }
    /**
     * Send REST HEAD request to the specified URL using {@link RestTemplate}
     *
     * @param <T> the result type
     * @param <E> the request body entity type
     * @param url to send
     * @param headers request headers
     * @param requestBody request body parameters
     * @param uriVariables the variables to expand in the template
     * @param entityClass the result class
     *
     * @return result by entity class or NULL if fail or status code &gt;= 300 or status code &lt; 200
     * @throws RestClientException if fail
     */
    public static <T, E> T restHead(
            String url,
            Map<String, String> headers,
            E requestBody,
            Map<String, String> uriVariables,
            Class<T> entityClass)
            throws RestClientException {
        return restSend(url, HttpMethod.HEAD, headers, requestBody, uriVariables, entityClass);
    }
    /**
     * Send REST OPTIONS request to the specified URL using {@link RestTemplate}
     *
     * @param <T> the result type
     * @param <E> the request body entity type
     * @param url to send
     * @param headers request headers
     * @param requestBody request body parameters
     * @param uriVariables the variables to expand in the template
     * @param entityClass the result class
     *
     * @return result by entity class or NULL if fail or status code &gt;= 300 or status code &lt; 200
     * @throws RestClientException if fail
     */
    public static <T, E> T restOptions(
            String url,
            Map<String, String> headers,
            E requestBody,
            Map<String, String> uriVariables,
            Class<T> entityClass)
            throws RestClientException {
        return restSend(url, HttpMethod.OPTIONS, headers, requestBody, uriVariables, entityClass);
    }
    /**
     * Send REST PATCH request to the specified URL using {@link RestTemplate}
     *
     * @param <T> the result type
     * @param <E> the request body entity type
     * @param url to send
     * @param headers request headers
     * @param requestBody request body parameters
     * @param uriVariables the variables to expand in the template
     * @param entityClass the result class
     *
     * @return result by entity class or NULL if fail or status code &gt;= 300 or status code &lt; 200
     * @throws RestClientException if fail
     */
    public static <T, E> T restPatch(
            String url,
            Map<String, String> headers,
            E requestBody,
            Map<String, String> uriVariables,
            Class<T> entityClass)
            throws RestClientException {
        return restSend(url, HttpMethod.PATCH, headers, requestBody, uriVariables, entityClass);
    }
    /**
     * Send REST POST request to the specified URL using {@link RestTemplate}
     *
     * @param <T> the result type
     * @param <E> the request body entity type
     * @param url to send
     * @param headers request headers
     * @param requestBody request body parameters
     * @param uriVariables the variables to expand in the template
     * @param entityClass the result class
     *
     * @return result by entity class or NULL if fail or status code &gt;= 300 or status code &lt; 200
     * @throws RestClientException if fail
     */
    public static <T, E> T restPost(
            String url,
            Map<String, String> headers,
            E requestBody,
            Map<String, String> uriVariables,
            Class<T> entityClass)
            throws RestClientException {
        return restSend(url, HttpMethod.POST, headers, requestBody, uriVariables, entityClass);
    }
    /**
     * Send REST PUT request to the specified URL using {@link RestTemplate}
     *
     * @param <T> the result type
     * @param <E> the request body entity type
     * @param url to send
     * @param headers request headers
     * @param requestBody request body parameters
     * @param uriVariables the variables to expand in the template
     * @param entityClass the result class
     *
     * @return result by entity class or NULL if fail or status code &gt;= 300 or status code &lt; 200
     * @throws RestClientException if fail
     */
    public static <T, E> T restPut(
            String url,
            Map<String, String> headers,
            E requestBody,
            Map<String, String> uriVariables,
            Class<T> entityClass)
            throws RestClientException {
        return restSend(url, HttpMethod.PUT, headers, requestBody, uriVariables, entityClass);
    }
    /**
     * Send REST TRACE request to the specified URL using {@link RestTemplate}
     *
     * @param <T> the result type
     * @param <E> the request body entity type
     * @param url to send
     * @param headers request headers
     * @param requestBody request body parameters
     * @param uriVariables the variables to expand in the template
     * @param entityClass the result class
     *
     * @return result by entity class or NULL if fail or status code &gt;= 300 or status code &lt; 200
     * @throws RestClientException if fail
     */
    public static <T, E> T restTrace(
            String url,
            Map<String, String> headers,
            E requestBody,
            Map<String, String> uriVariables,
            Class<T> entityClass)
            throws RestClientException {
        return restSend(url, HttpMethod.TRACE, headers, requestBody, uriVariables, entityClass);
    }

    // -------------------------------------------------
    // APACHE: HTTP Components
    // -------------------------------------------------

    /**
     * Send request to the specified URL
     *
     * @param <T> the returned data type
     * @param url the request URL
     * @param method the request method
     * @param charset the request character-set
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respHandler {@link ResponseHandler} for handling response from server
     * @return HTTP response by the specified type belongs to the specified {@link ResponseHandler}
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    protected static <T> T send(
            final String url, final HttpMethod method, String charset,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            ResponseHandler<? extends T> respHandler)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        Assert.hasText(url, "URL");
        Assert.notNull(method, "HTTP Method");
        Assert.notNull(respHandler, "HTTP Response Handler");

        /** request parameters */
        List<NameValuePair> lstParams = new LinkedList<NameValuePair>();
        if (!CollectionUtils.isEmpty(params)) {
            for(final Iterator<String> it = headers.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                String value = headers.get(key);
                if (!StringUtils.hasText(key)) continue;
                lstParams.add(new BasicNameValuePair(key, value));
            }
        }

        // create request configuration
        Builder reqCfgBuilder = RequestConfig.custom()
                .setAuthenticationEnabled(true)
                .setCircularRedirectsAllowed(true)
                .setContentCompressionEnabled(true)
                .setConnectionRequestTimeout(-1)
                .setConnectTimeout(-1);
        if (proxy != null) {
            reqCfgBuilder = reqCfgBuilder.setProxy(proxy);
        }

        // create HTTP request
        CloseableHttpClient http = HttpClients.createDefault();
        HttpRequestBase request = null;
        charset = (!StringUtils.hasText(charset) ? ENCODING_UTF8 : charset);
        if (HttpMethod.DELETE.equals(method)) {
            request = new HttpDelete(UrlUtils.buildUri(url, lstParams));
        } else if (HttpMethod.GET.equals(method)) {
            request = new HttpGet(UrlUtils.buildUri(url, lstParams));
        } else if (HttpMethod.HEAD.equals(method)) {
            request = new HttpHead(UrlUtils.buildUri(url, lstParams));
        } else if (HttpMethod.OPTIONS.equals(method)) {
            request = new HttpOptions(UrlUtils.buildUri(url, lstParams));
        } else if (HttpMethod.PATCH.equals(method)) {
            request = new HttpPatch(UrlUtils.buildUri(url, null));
            ((HttpPatch) request).setEntity(
                    new UrlEncodedFormEntity(lstParams, charset));
        } else if (HttpMethod.POST.equals(method)) {
            request = new HttpPost(UrlUtils.buildUri(url, null));
            ((HttpPost) request).setEntity(
                    new UrlEncodedFormEntity(lstParams, charset));
        } else if (HttpMethod.PUT.equals(method)) {
            request = new HttpPut(UrlUtils.buildUri(url, null));
            ((HttpPut) request).setEntity(
                    new UrlEncodedFormEntity(lstParams, charset));
        } else if (HttpMethod.TRACE.equals(method)) {
            request = new HttpTrace(UrlUtils.buildUri(url, lstParams));
        } else {
            throw new IllegalArgumentException("Invaid HTTP method!");
        }
        // FIXME Use "if" instead of "switch" for obfuscating code,
        // because while obfuscating code, it'll generate _clinit@12345677() method for referencing.
        // this method will be failed on android (not support) or referencing from another class
        //        switch(method) {
        //            case DELETE:
        //                request = new HttpDelete(UrlUtils.buildUri(url, lstParams));
        //                break;
        //            case GET:
        //                request = new HttpGet(UrlUtils.buildUri(url, lstParams));
        //                break;
        //            case HEAD:
        //                request = new HttpHead(UrlUtils.buildUri(url, lstParams));
        //                break;
        //            case OPTIONS:
        //                request = new HttpOptions(UrlUtils.buildUri(url, lstParams));
        //                break;
        //            case PATCH:
        //                request = new HttpPatch(UrlUtils.buildUri(url, null));
        //                ((HttpPatch) request).setEntity(
        //                        new UrlEncodedFormEntity(lstParams, charset));
        //                break;
        //            case POST:
        //                request = new HttpPost(UrlUtils.buildUri(url, null));
        //                ((HttpPost) request).setEntity(
        //                        new UrlEncodedFormEntity(lstParams, charset));
        //                break;
        //            case PUT:
        //                request = new HttpPut(UrlUtils.buildUri(url, null));
        //                ((HttpPut) request).setEntity(
        //                        new UrlEncodedFormEntity(lstParams, charset));
        //                break;
        //            case TRACE:
        //                request = new HttpTrace(UrlUtils.buildUri(url, lstParams));
        //                break;
        //        }

        /** request header parameters */
        if (!CollectionUtils.isEmpty(headers)) {
            for(final Iterator<String> it = headers.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                String value = headers.get(key);
                if (!StringUtils.hasText(key)) continue;
                request.addHeader(key, value);
            }
        }

        /** send request */
        try {
            /* Execute URL and attach after execution response handler */
            return http.execute(request, respHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Send request to the specified URL
     *
     * @param url the request URL
     * @param method the request method
     * @param charset the request character-set
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respBuf the response string data from server
     * @return HTTP response status
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static Integer sendStatus(
            final String url, final HttpMethod method, String charset,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            final StringBuffer respBuf)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return send(url, method, charset, headers, params, proxy,
                new ResponseHandler<Integer>() {

                    /*
                     * (Non-Javadoc)
                     * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
                     */
                    @Override
                    public Integer handleResponse(HttpResponse response)
                            throws ClientProtocolException, IOException {
                        int status = response.getStatusLine().getStatusCode();
                        if (response.getEntity() != null) {
                            respBuf.append(EntityUtils.toString(response.getEntity()));
                        }
                        return status;
                    }
                });
    }
    /**
     * Send request to the specified URL
     *
     * @param url the request URL
     * @param method the request method
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respBuf the response string data from server
     * @return HTTP response status
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static Integer sendStatus(
            final String url, final HttpMethod method,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            final StringBuffer respBuf)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return sendStatus(url, method, ENCODING_UTF8, headers, params, proxy, respBuf);
    }

    /**
     * Send DELETE request to the specified URL
     *
     * @param <T> the returned data type
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respHandler {@link ResponseHandler} for handling response from server
     * @return HTTP response by the specified type belongs to the specified {@link ResponseHandler}
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static <T> T delete(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            ResponseHandler<? extends T> respHandler)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return send(url, HttpMethod.DELETE, ENCODING_UTF8, headers, params, proxy, respHandler);
    }
    /**
     * Send DELETE request to the specified URL
     *
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respBuf the response string data from server
     * @return HTTP response status
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static Integer deleteStatus(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            final StringBuffer respBuf)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return sendStatus(url, HttpMethod.DELETE, ENCODING_UTF8, headers, params, proxy, respBuf);
    }

    /**
     * Send GET request to the specified URL
     *
     * @param <T> the returned data type
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respHandler {@link ResponseHandler} for handling response from server
     * @return HTTP response by the specified type belongs to the specified {@link ResponseHandler}
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static <T> T get(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            ResponseHandler<? extends T> respHandler)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return send(url, HttpMethod.GET, ENCODING_UTF8, headers, params, proxy, respHandler);
    }
    /**
     * Send GET request to the specified URL
     *
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respBuf the response string data from server
     * @return HTTP response status
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static Integer getStatus(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            final StringBuffer respBuf)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return sendStatus(url, HttpMethod.GET, ENCODING_UTF8, headers, params, proxy, respBuf);
    }

    /**
     * Send HEAD request to the specified URL
     *
     * @param <T> the returned data type
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respHandler {@link ResponseHandler} for handling response from server
     * @return HTTP response by the specified type belongs to the specified {@link ResponseHandler}
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static <T> T head(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            ResponseHandler<? extends T> respHandler)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return send(url, HttpMethod.HEAD, ENCODING_UTF8, headers, params, proxy, respHandler);
    }
    /**
     * Send HEAD request to the specified URL
     *
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respBuf the response string data from server
     * @return HTTP response status
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static Integer headStatus(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            final StringBuffer respBuf)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return sendStatus(url, HttpMethod.HEAD, ENCODING_UTF8, headers, params, proxy, respBuf);
    }

    /**
     * Send OPTIONS request to the specified URL
     *
     * @param <T> the returned data type
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respHandler {@link ResponseHandler} for handling response from server
     * @return HTTP response by the specified type belongs to the specified {@link ResponseHandler}
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static <T> T options(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            ResponseHandler<? extends T> respHandler)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return send(url, HttpMethod.OPTIONS, ENCODING_UTF8, headers, params, proxy, respHandler);
    }
    /**
     * Send OPTIONS request to the specified URL
     *
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respBuf the response string data from server
     * @return HTTP response status
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static Integer optionsStatus(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            final StringBuffer respBuf)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return sendStatus(url, HttpMethod.OPTIONS, ENCODING_UTF8, headers, params, proxy, respBuf);
    }

    /**
     * Send PATCH request to the specified URL
     *
     * @param <T> the returned data type
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respHandler {@link ResponseHandler} for handling response from server
     * @return HTTP response by the specified type belongs to the specified {@link ResponseHandler}
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static <T> T patch(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            ResponseHandler<? extends T> respHandler)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return send(url, HttpMethod.PATCH, ENCODING_UTF8, headers, params, proxy, respHandler);
    }
    /**
     * Send PATCH request to the specified URL
     *
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respBuf the response string data from server
     * @return HTTP response status
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static Integer patchStatus(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            final StringBuffer respBuf)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return sendStatus(url, HttpMethod.PATCH, ENCODING_UTF8, headers, params, proxy, respBuf);
    }

    /**
     * Send POST request to the specified URL
     *
     * @param <T> the returned data type
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respHandler {@link ResponseHandler} for handling response from server
     * @return HTTP response by the specified type belongs to the specified {@link ResponseHandler}
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static <T> T post(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            ResponseHandler<? extends T> respHandler)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return send(url, HttpMethod.POST, ENCODING_UTF8, headers, params, proxy, respHandler);
    }
    /**
     * Send POST request to the specified URL
     *
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respBuf the response string data from server
     * @return HTTP response status
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static Integer postStatus(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            final StringBuffer respBuf)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return sendStatus(url, HttpMethod.POST, ENCODING_UTF8, headers, params, proxy, respBuf);
    }

    /**
     * Send PUT request to the specified URL
     *
     * @param <T> the returned data type
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respHandler {@link ResponseHandler} for handling response from server
     * @return HTTP response by the specified type belongs to the specified {@link ResponseHandler}
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static <T> T put(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            ResponseHandler<? extends T> respHandler)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return send(url, HttpMethod.PUT, ENCODING_UTF8, headers, params, proxy, respHandler);
    }
    /**
     * Send PUT request to the specified URL
     *
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respBuf the response string data from server
     * @return HTTP response status
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static Integer putStatus(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            final StringBuffer respBuf)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return sendStatus(url, HttpMethod.PUT, ENCODING_UTF8, headers, params, proxy, respBuf);
    }

    /**
     * Send TRACE request to the specified URL
     *
     * @param <T> the returned data type
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respHandler {@link ResponseHandler} for handling response from server
     * @return HTTP response by the specified type belongs to the specified {@link ResponseHandler}
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static <T> T trace(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            ResponseHandler<? extends T> respHandler)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return send(url, HttpMethod.TRACE, ENCODING_UTF8, headers, params, proxy, respHandler);
    }
    /**
     * Send TRACE request to the specified URL
     *
     * @param url the request URL
     * @param headers the request header parameters
     * @param params the request parameters
     * @param proxy the PROXY if necessary
     * @param respBuf the response string data from server
     * @return HTTP response status
     * @throws MalformedURLException thrown if invalid URL
     * @throws URISyntaxException thrown if invalid URI
     * @throws UnsupportedEncodingException thrown if invalid encoding
     */
    public static Integer traceStatus(
            final String url,
            Map<String, String> headers,
            Map<String, String> params,
            HttpHost proxy,
            final StringBuffer respBuf)
            throws MalformedURLException, URISyntaxException, UnsupportedEncodingException {
        return sendStatus(url, HttpMethod.TRACE, ENCODING_UTF8, headers, params, proxy, respBuf);
    }

    // -------------------------------------------------
    // JERSEY: Web resource
    // -------------------------------------------------

    /**
     * Gets the response result of the specified URL
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param method
     *      the request method
     * @param type
     *      the media type of {@link WebResource}
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     * @param accepts
     *      the accept media types
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL; or HTTP method is empty/NULL/HEAD/OPTIONS/TRACE
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse send(
            String url, HttpMethod method, MediaType type,
            Map<String, Object> headers,
            Map<T, K> parameters,
            MediaType...accepts)
            throws UniformInterfaceException, ClientHandlerException {
        Assert.hasText(url, "URL");
        Assert.notNull(method, "HTTP Method");
        Assert.isTrue(
                !HttpMethod.HEAD.equals(method)
                && !HttpMethod.PATCH.equals(method)
                && !HttpMethod.TRACE.equals(method),
                "Not support for HEAD, PATCH, TRACE methods!");
        // creates client
        Client client = Client.create();
        // create web resource
        WebResource webResource = client.resource(url);
        // sets parameter to post
        MultivaluedMap<T, K> formdata = new MultivaluedParameter<T, K>();
        if (!CollectionUtils.isEmpty(parameters)) {
            for(final Iterator<T> it = parameters.keySet().iterator(); it.hasNext();) {
                T key = it.next();
                K value = parameters.get(key);
                formdata.add(key, value);
            }
        }

        // post to jersey service and get response data
        com.sun.jersey.api.client.WebResource.Builder requestBuilder = null;
        if (type != null) {
            requestBuilder = webResource.entity(formdata, type);
        } else {
            requestBuilder = webResource.entity(formdata);
        }
        if (!CollectionUtils.isEmpty(accepts)) {
            requestBuilder = requestBuilder.accept(accepts);
        }

        /** request header parameters */
        if (!CollectionUtils.isEmpty(headers)) {
            for(final Iterator<String> it = headers.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                Object value = headers.get(key);
                if (!StringUtils.hasText(key)) continue;
                requestBuilder = requestBuilder.header(key, value);
            }
        }

        // send request
        if (HttpMethod.DELETE.equals(method)) {
            return requestBuilder.delete(ClientResponse.class);
        } else if (HttpMethod.GET.equals(method)) {
            return requestBuilder.get(ClientResponse.class);
        } else if (HttpMethod.OPTIONS.equals(method)) {
            return requestBuilder.options(ClientResponse.class);
        } else if (HttpMethod.POST.equals(method)) {
            return requestBuilder.post(ClientResponse.class);
        } else if (HttpMethod.PUT.equals(method)) {
            return requestBuilder.put(ClientResponse.class);
        } else {
            throw new IllegalArgumentException("Not support for HEAD, PATCH, TRACE methods!");
        }
        // FIXME Use "if" instead of "switch" for obfuscating code,
        // because while obfuscating code, it'll generate _clinit@12345677() method for referencing.
        // this method will be failed on android (not support) or referencing from another class
        //        switch(method) {
        //            case DELETE:
        //                return requestBuilder.delete(ClientResponse.class);
        //            case GET:
        //                return requestBuilder.get(ClientResponse.class);
        //            case OPTIONS:
        //                return requestBuilder.options(ClientResponse.class);
        //            case POST:
        //                return requestBuilder.post(ClientResponse.class);
        //            case PUT:
        //                return requestBuilder.put(ClientResponse.class);
        //            default:
        //                throw new UnsupportedOperationException("Not support for HEAD, OPTIONS, TRACE");
        //        }
    }
    /**
     * Gets the response result of the specified URL
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param method
     *      the request method
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     * @param accepts
     *      the accept media types
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL; or HTTP method is empty/NULL/HEAD/OPTIONS/TRACE
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse send(
            String url, HttpMethod method,
            Map<String, Object> headers,
            Map<T, K> parameters,
            MediaType...accepts)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, method, null, headers, parameters, accepts);
    }
    /**
     * Gets the response result of the specified URL
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param method
     *      the request method
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL; or HTTP method is empty/NULL/HEAD/OPTIONS/TRACE
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse send(
            String url, HttpMethod method,
            Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, method, null, parameters, (MediaType[]) null);
    }
    /**
     * Gets the response result of the specified URL
     *
     * @param <R>
     *      the JSON result instance type
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param method
     *      the request method
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     * @param typeref
     *      the response entity type reference
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL; or HTTP method is empty/NULL/HEAD/OPTIONS/TRACE
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     * @throws WebApplicationException thrown if could not de-serialize response to the specified type reference
     */
    public static <R, T, K> JsonResult<R> jsonSend(
            String url, HttpMethod method,
            Map<String, Object> headers,
            Map<T, K> parameters,
            TypeReference<JsonResult<R>> typeref)
            throws UniformInterfaceException, ClientHandlerException, WebApplicationException {
        JsonResult<R> result = null;
        // posts parameters
        ClientResponse clientresp = send(
                url, method, MediaType.APPLICATION_FORM_URLENCODED_TYPE,
                headers, parameters);
        if (clientresp != null) {
            String json = clientresp.getEntity(String.class);
            result = JsonUtils.deserialize(json, typeref);
        }
        return result;
    }
    /**
     * Gets the response result of the specified URL
     *
     * @param <R>
     *      the JSON result instance type
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param method
     *      the request method
     * @param parameters
     *      the parameters to send
     * @param typeref
     *      the response entity type reference
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL; or HTTP method is empty/NULL/HEAD/OPTIONS/TRACE
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     * @throws WebApplicationException thrown if could not de-serialize response to the specified type reference
     */
    public static <R, T, K> JsonResult<R> jsonSend(String url, HttpMethod method, Map<T, K> parameters, TypeReference<JsonResult<R>> typeref)
            throws UniformInterfaceException, ClientHandlerException, WebApplicationException {
        return jsonSend(url, method, null, parameters, typeref);
    }

    /**
     * Gets the response result of the specified URL (HTTP DELETE METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param type
     *      the media type of {@link WebResource}
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse delete(String url, MediaType type, Map<String, Object> headers, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.DELETE, type, headers, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP DELETE METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param type
     *      the media type of {@link WebResource}
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse delete(String url, MediaType type, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.DELETE, type, null, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP DELETE METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse delete(String url, Map<String, Object> headers, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.DELETE, null, headers, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP DELETE METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse delete(String url, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.DELETE, null, null, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP DELETE METHOD)
     *
     * @param <R>
     *      the JSON result instance type
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param parameters
     *      the parameters to send
     * @param headers
     *      the request header parameters
     * @param typeref
     *      the response entity type reference
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     * @throws WebApplicationException thrown if could not de-serialize response to the specified type reference
     */
    public static <R, T, K> JsonResult<R> jsonDelete(String url, Map<String, Object> headers, Map<T, K> parameters, TypeReference<JsonResult<R>> typeref)
            throws UniformInterfaceException, ClientHandlerException, WebApplicationException {
        return jsonSend(url, HttpMethod.DELETE, headers, parameters, typeref);
    }
    /**
     * Gets the response result of the specified URL (HTTP DELETE METHOD)
     *
     * @param <R>
     *      the JSON result instance type
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param parameters
     *      the parameters to send
     * @param typeref
     *      the response entity type reference
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     * @throws WebApplicationException thrown if could not de-serialize response to the specified type reference
     */
    public static <R, T, K> JsonResult<R> jsonDelete(String url, Map<T, K> parameters, TypeReference<JsonResult<R>> typeref)
            throws UniformInterfaceException, ClientHandlerException, WebApplicationException {
        return jsonSend(url, HttpMethod.DELETE, null, parameters, typeref);
    }

    /**
     * Gets the response result of the specified URL (HTTP GET METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param type
     *      the media type of {@link WebResource}
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse get(String url, MediaType type, Map<String, Object> headers, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.GET, type, headers, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP GET METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param type
     *      the media type of {@link WebResource}
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse get(String url, MediaType type, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.GET, type, null, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP GET METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse get(String url, Map<String, Object> headers, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.GET, null, headers, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP GET METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse get(String url, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.GET, null, null, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP GET METHOD)
     *
     * @param <R>
     *      the JSON result instance type
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     * @param typeref
     *      the response entity type reference
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     * @throws WebApplicationException thrown if could not de-serialize response to the specified type reference
     */
    public static <R, T, K> JsonResult<R> jsonGet(String url, Map<String, Object> headers, Map<T, K> parameters, TypeReference<JsonResult<R>> typeref)
            throws UniformInterfaceException, ClientHandlerException, WebApplicationException {
        return jsonSend(url, HttpMethod.GET, headers, parameters, typeref);
    }
    /**
     * Gets the response result of the specified URL (HTTP GET METHOD)
     *
     * @param <R>
     *      the JSON result instance type
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param parameters
     *      the parameters to send
     * @param typeref
     *      the response entity type reference
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     * @throws WebApplicationException thrown if could not de-serialize response to the specified type reference
     */
    public static <R, T, K> JsonResult<R> jsonGet(String url, Map<T, K> parameters, TypeReference<JsonResult<R>> typeref)
            throws UniformInterfaceException, ClientHandlerException, WebApplicationException {
        return jsonSend(url, HttpMethod.GET, null, parameters, typeref);
    }

    /**
     * Gets the response result of the specified URL (HTTP OPTIONS METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param type
     *      the media type of {@link WebResource}
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse options(String url, MediaType type, Map<String, Object> headers, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.OPTIONS, type, headers, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP OPTIONS METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param type
     *      the media type of {@link WebResource}
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse options(String url, MediaType type, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.OPTIONS, type, null, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP OPTIONS METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse options(String url, Map<String, Object> headers, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.OPTIONS, null, headers, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP OPTIONS METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse options(String url, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.OPTIONS, null, null, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP OPTIONS METHOD)
     *
     * @param <R>
     *      the JSON result instance type
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     * @param typeref
     *      the response entity type reference
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     * @throws WebApplicationException thrown if could not de-serialize response to the specified type reference
     */
    public static <R, T, K> JsonResult<R> jsonOptions(String url, Map<String, Object> headers, Map<T, K> parameters, TypeReference<JsonResult<R>> typeref)
            throws UniformInterfaceException, ClientHandlerException, WebApplicationException {
        return jsonSend(url, HttpMethod.OPTIONS, headers, parameters, typeref);
    }
    /**
     * Gets the response result of the specified URL (HTTP OPTIONS METHOD)
     *
     * @param <R>
     *      the JSON result instance type
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param parameters
     *      the parameters to send
     * @param typeref
     *      the response entity type reference
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     * @throws WebApplicationException thrown if could not de-serialize response to the specified type reference
     */
    public static <R, T, K> JsonResult<R> jsonOptions(String url, Map<T, K> parameters, TypeReference<JsonResult<R>> typeref)
            throws UniformInterfaceException, ClientHandlerException, WebApplicationException {
        return jsonSend(url, HttpMethod.OPTIONS, null, parameters, typeref);
    }

    /**
     * Gets the response result of the specified URL (HTTP POST METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param type
     *      the media type of {@link WebResource}
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse post(String url, MediaType type, Map<String, Object> headers, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.POST, type, headers, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP POST METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param type
     *      the media type of {@link WebResource}
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse post(String url, MediaType type, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.POST, type, null, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP POST METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse post(String url, Map<String, Object> headers, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.POST, null, headers, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP POST METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse post(String url, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.POST, null, null, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP POST METHOD)
     *
     * @param <R>
     *      the JSON result instance type
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     * @param typeref
     *      the response entity type reference
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     * @throws WebApplicationException thrown if could not de-serialize response to the specified type reference
     */
    public static <R, T, K> JsonResult<R> jsonPost(String url, Map<String, Object> headers, Map<T, K> parameters, TypeReference<JsonResult<R>> typeref)
            throws UniformInterfaceException, ClientHandlerException, WebApplicationException {
        return jsonSend(url, HttpMethod.POST, headers, parameters, typeref);
    }
    /**
     * Gets the response result of the specified URL (HTTP POST METHOD)
     *
     * @param <R>
     *      the JSON result instance type
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param parameters
     *      the parameters to send
     * @param typeref
     *      the response entity type reference
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     * @throws WebApplicationException thrown if could not de-serialize response to the specified type reference
     */
    public static <R, T, K> JsonResult<R> jsonPost(String url, Map<T, K> parameters, TypeReference<JsonResult<R>> typeref)
            throws UniformInterfaceException, ClientHandlerException, WebApplicationException {
        return jsonSend(url, HttpMethod.POST, null, parameters, typeref);
    }

    /**
     * Gets the response result of the specified URL (HTTP PUT METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param type
     *      the media type of {@link WebResource}
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse put(String url, MediaType type, Map<String, Object> headers, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.PUT, type, headers, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP PUT METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param type
     *      the media type of {@link WebResource}
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse put(String url, MediaType type, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.PUT, type, null, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP PUT METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse put(String url, Map<String, Object> headers, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.PUT, null, headers, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP PUT METHOD)
     *
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param parameters
     *      the parameters to send
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     */
    public static <T, K> ClientResponse put(String url, Map<T, K> parameters)
            throws UniformInterfaceException, ClientHandlerException {
        return send(url, HttpMethod.PUT, null, null, parameters);
    }
    /**
     * Gets the response result of the specified URL (HTTP PUT METHOD)
     *
     * @param <R>
     *      the JSON result instance type
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param headers
     *      the request header parameters
     * @param parameters
     *      the parameters to send
     * @param typeref
     *      the response entity type reference
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     * @throws WebApplicationException thrown if could not de-serialize response to the specified type reference
     */
    public static <R, T, K> JsonResult<R> jsonPut(String url, Map<String, Object> headers, Map<T, K> parameters, TypeReference<JsonResult<R>> typeref)
            throws UniformInterfaceException, ClientHandlerException, WebApplicationException {
        return jsonSend(url, HttpMethod.PUT, headers, parameters, typeref);
    }
    /**
     * Gets the response result of the specified URL (HTTP PUT METHOD)
     *
     * @param <R>
     *      the JSON result instance type
     * @param <T>
     *      any instance type
     * @param <K>
     *      any instance type
     * @param url
     *      the URL to send parameters
     * @param parameters
     *      the parameters to send
     * @param typeref
     *      the response entity type reference
     *
     * @return the response result of the specified URL
     * @throws IllegalArgumentException thrown if URL is empty/NULL
     * @throws UniformInterfaceException thrown if invalid URL
     * @throws ClientHandlerException thrown if request is invalid
     * @throws WebApplicationException thrown if could not de-serialize response to the specified type reference
     */
    public static <R, T, K> JsonResult<R> jsonPut(String url, Map<T, K> parameters, TypeReference<JsonResult<R>> typeref)
            throws UniformInterfaceException, ClientHandlerException, WebApplicationException {
        return jsonSend(url, HttpMethod.PUT, null, parameters, typeref);
    }
}
