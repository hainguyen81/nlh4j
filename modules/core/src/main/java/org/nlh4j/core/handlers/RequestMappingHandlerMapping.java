/*
 * @(#)RequestMappingHandlerMapping.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.handlers;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import org.nlh4j.core.servlet.SpringContextHelper;

/**
 * An extended class of {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping}.<br>
 * Support for ambigous request mapping (override an controller class).<br>
 * Using {@link Component} with SPRING request mapping component name to override
 * default SPRING {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Component(value = "requestMappingHandlerMapping")
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Singleton
public class RequestMappingHandlerMapping
        extends org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
        implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * slf4j
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** {@link SpringContextHelper} */
    @Inject
    private SpringContextHelper contextHelper;

    /**
     * Get the context helper to find bean if necessary
     * @return the context helper to find bean if necessary
     */
    protected final SpringContextHelper getContextHelper() {
        if (this.contextHelper == null) {
            ApplicationContext context = super.getApplicationContext();
            this.contextHelper = new SpringContextHelper(context);
        }
        return this.contextHelper;
    }
    /**
     * Get bean by the specified bean class
     *
     * @param beanClass the bean class to parse from context
     *
     * @return bean or NULL
     */
    protected final <K> K findBean(Class<K> beanClass) {
        return (this.getContextHelper() == null ? null : this.getContextHelper().searchBean(beanClass));
    }
    /**
     * Get bean by the specified bean class
     *
     * @param beanClass the bean class to parse from context
     *
     * @return bean or NULL
     */
    protected final <K> List<K> findBeans(Class<K> beanClass) {
        return (this.getContextHelper() == null ? null : this.getContextHelper().searchBeans(beanClass));
    }

    /** {@link GlobalExceptionResolver} */
    @Inject
    protected GlobalExceptionResolver exceptionResolver;

    /** Ambiguous handler mapping register */
    private Map<RequestMappingInfo, Pair<Object, Method>> ambiguousMap =
            new LinkedHashMap<RequestMappingInfo, Pair<Object,Method>>();
    /**
     * Get the ambiguous handler mapping register
     * @return the ambiguous handler mapping register
     */
    protected Map<RequestMappingInfo, Pair<Object, Method>> getAmbiguousMap() {
        return Collections.unmodifiableMap(this.ambiguousMap);
    }

    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.handler.AbstractHandlerMethodMapping#lookupHandlerMethod(java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
        HandlerMethod method = null;
        try {
            method = super.lookupHandlerMethod(lookupPath, request);
        } catch (IllegalStateException e) {
            logger.warn(e.getMessage());
        }
        return method;
    };

    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.handler.AbstractHandlerMethodMapping#registerMapping(java.lang.Object, java.lang.Object, java.lang.reflect.Method)
     */
    @Override
    public void registerMapping(RequestMappingInfo mapping, Object handler, Method method) {
        try {
            super.registerMapping(mapping, handler, method);
        } catch (IllegalStateException e) {
            logger.warn(e.getMessage());
            this.ambiguousMap.put(mapping, new MutablePair<Object, Method>(handler, method));
        }
    }

    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.handler.AbstractHandlerMethodMapping#registerHandlerMethod(java.lang.Object, java.lang.reflect.Method, java.lang.Object)
     */
    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
        try {
            super.registerHandlerMethod(handler, method, mapping);
        } catch (IllegalStateException e) {
            logger.warn(e.getMessage());
            this.ambiguousMap.put(mapping, new MutablePair<Object, Method>(handler, method));
        }
    };
}
