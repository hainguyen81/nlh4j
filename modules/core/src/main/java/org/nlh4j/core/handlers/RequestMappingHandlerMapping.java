/*
 * @(#)RequestMappingHandlerMapping.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.handlers;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.nlh4j.core.controller.AbstractController;
import org.nlh4j.core.controller.AbstractMasterController;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.util.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

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
        implements DisposableBean, Serializable {

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
    public final Map<RequestMappingInfo, Pair<Object, Method>> getAmbiguousMap() {
        return Collections.unmodifiableMap(this.ambiguousMap);
    }
    
    /** Certainly handler mapping register */
    private Map<RequestMappingInfo, Pair<Object, Method>> certainHandlersMap =
            new LinkedHashMap<RequestMappingInfo, Pair<Object,Method>>();
    /**
     * Get the ambiguous handler mapping register
     * @return the ambiguous handler mapping register
     */
    public final Map<RequestMappingInfo, Pair<Object, Method>> getCertainHandlersMap() {
        return Collections.unmodifiableMap(this.certainHandlersMap);
    }
    
    /** The cached controllers set */
    private Set<AbstractController> controllersSet = new LinkedHashSet<AbstractController>();
    private Map<? extends AbstractController, Entry<Class<? extends AbstractDto>, Set<String>>> controllerEntityFieldsMap = new LinkedHashMap<>();
    private Map<RequestMapping, Entry<String, Set<String>>> flatControllerEntityFieldsMap = new LinkedHashMap<>();
    /**
     * Get the cached controllers set
     * @return the cached controllers set
     */
    public final Set<AbstractController> getControllersSet() {
    	// detect controllers metadata
    	detectControllersMetadata();
        return Collections.unmodifiableSet(this.controllersSet);
    }
    /**
     * Get the cached {@link AbstractMasterController} entity fields map:<br>
     * - key is {@link AbstractMasterController} instance
     * - value is a map of {@link AbstractMasterController#getMainEntityType()} and the entity field names set
     * 
     * @return the cached {@link AbstractMasterController} entity fields map
     */
    public final Map<? extends AbstractController, Entry<Class<? extends AbstractDto>, Set<String>>> getMasterControllerEntityFieldsMap() {
    	// detect controllers metadata
    	detectControllersMetadata();
    	return Collections.unmodifiableMap(this.controllerEntityFieldsMap);
    }
    /**
     * Get the cached {@link AbstractMasterController} entity fields map:<br>
     * - key is {@link RequestMapping} instance
     * - value is a map of {@link AbstractMasterController#getMainEntityType()} and the entity field names set
     * 
     * @return the cached {@link AbstractMasterController} entity fields map
     */
    public final Map<RequestMapping, Entry<String, Set<String>>> getFlatMasterControllerEntityFieldsMap() {
    	// detect controllers metadata
    	detectControllersMetadata();
    	return Collections.unmodifiableMap(this.flatControllerEntityFieldsMap);
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
    }
    
	@Override
    public void afterPropertiesSet() {
    	super.afterPropertiesSet();
    	// detect controllers metadata
    	detectControllersMetadata();
    }
    
    /**
     * Detect {@link AbstractController} for caching
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected final void detectControllersMetadata() {
    	// if already parsing
    	if (CollectionUtils.isNotEmpty(this.controllersSet)) {
    		return;
    	}

    	// scan controllers
    	this.controllersSet.addAll(contextHelper.searchBeans(AbstractController.class, true));
    	this.controllerEntityFieldsMap.putAll(
    			(Map) this.controllersSet.parallelStream()
    			.filter(AbstractMasterController.class::isInstance).map(AbstractMasterController.class::cast)
    			.filter(c -> BeanUtils.isInstanceOf(c.getMainEntityType(), AbstractDto.class))
    			.map(c -> new SimpleEntry<>(c, new SimpleEntry<>(c.getMainEntityType(),
    					Collections.unmodifiableSet(new LinkedHashSet<>(BeanUtils.getFieldNames(getClass(), true))))))
    			.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (k1, k2) -> k1)));
    	this.flatControllerEntityFieldsMap.putAll(
    			this.controllerEntityFieldsMap.entrySet().parallelStream()
    			.filter(e -> BeanUtils.isInstanceOf(e.getKey(), AbstractMasterController.class)
    					&& Objects.nonNull(BeanUtils.safeType(e.getKey(), AbstractMasterController.class).getMainEntityType())
    					&& Objects.nonNull(BeanUtils.getClassAnnotation(e.getKey().getClass(), RequestMapping.class)))
    			.map(e -> new SimpleEntry<>(
    					BeanUtils.getClassAnnotation(e.getKey().getClass(), RequestMapping.class),
    					new SimpleEntry<>(
    							BeanUtils.safeType(e.getKey(), AbstractMasterController.class).getMainEntityType().getName(),
    							e.getValue().getValue())))
    			.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (k1, k2) -> k1)));
    }
    
    @Override
    public final void destroy() throws Exception {
    	doDestroy();
    }

    /**
     * Release managed resources
     */
    protected void doDestroy() {
    	this.certainHandlersMap.clear();
    	this.ambiguousMap.clear();
    	this.controllersSet.clear();
    }
}
