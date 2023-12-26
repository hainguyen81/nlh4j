/*
 * @(#)RequestMappingHandlerMapping.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.controller.metadata;

import java.io.Serializable;
import java.lang.reflect.Field;
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

import org.apache.commons.collections4.CollectionUtils;
import org.nlh4j.core.controller.AbstractController;
import org.nlh4j.core.controller.AbstractMasterController;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.core.servlet.ApplicationContextProvider;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.util.BeanUtils;
import org.seasar.doma.Column;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Support for scanning controller metadata
 */
@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Singleton
public class ControllerMetadataSupport implements InitializingBean, DisposableBean, Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/**
     * {@link ApplicationContextProvider}
     */
	@Inject
    protected ApplicationContextProvider contextProvider;
    /**
     * {@link SpringContextHelper}
     */
	@Inject
    private SpringContextHelper contextHelper;
    /**
     * Get the SPRING context helper
     * @return the SPRING context helper
     */
    protected final SpringContextHelper getContextHelper() {
    	if (this.contextHelper == null && this.contextProvider != null
    			&& this.contextProvider.getApplicationContext() != null) {
    		this.contextHelper = new SpringContextHelper(this.contextProvider.getApplicationContext());
        }
    	if (this.contextHelper == null) return null;
        synchronized (contextHelper) {
            return contextHelper;
        }
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
    
    /** The cached controllers set */
    private Set<AbstractController> controllersSet = new LinkedHashSet<AbstractController>();
    private Map<? extends AbstractController, Entry<Class<? extends AbstractDto>, Set<Field>>> controllerEntityFieldsMap = new LinkedHashMap<>();
    private Map<RequestMapping, Entry<String, Set<String>>> flatControllerEntityFieldsMap = new LinkedHashMap<>();
    private Map<? extends AbstractController, Entry<Class<? extends AbstractDto>, Set<Field>>> controllerEntityColumnFieldsMap = new LinkedHashMap<>();
    private Map<RequestMapping, Entry<String, Set<String>>> flatControllerEntityColumnFieldsMap = new LinkedHashMap<>();
    private Map<? extends AbstractController, Entry<Class<? extends AbstractDto>, Set<Column>>> controllerEntityColumnsMap = new LinkedHashMap<>();
    private Map<RequestMapping, Entry<String, Set<String>>> flatControllerEntityColumnsMap = new LinkedHashMap<>();
    /**
     * Get the cached controllers set
     * @return the cached controllers set
     */
    public final Set<AbstractController> getControllersSet() {
    	// detect controllers metadata
    	scanMetadata();
        return Collections.unmodifiableSet(this.controllersSet);
    }
    /**
     * Get the cached {@link AbstractMasterController} entity fields map:<br>
     * - key is {@link AbstractMasterController} instance
     * - value is a map of {@link AbstractMasterController#getMainEntityType()} and the entity field names set
     * 
     * @return the cached {@link AbstractMasterController} entity fields map
     */
    public final Map<? extends AbstractController, Entry<Class<? extends AbstractDto>, Set<Field>>> getMasterControllerEntityFieldsMap() {
    	// detect controllers metadata
    	scanMetadata();
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
    	scanMetadata();
    	return Collections.unmodifiableMap(this.flatControllerEntityFieldsMap);
    }
    /**
     * Get the cached {@link AbstractMasterController} entity {@link Column} fields map:<br>
     * - key is {@link AbstractMasterController} instance
     * - value is a map of {@link AbstractMasterController#getMainEntityType()} and the entity field names set
     * 
     * @return the cached {@link AbstractMasterController} entity fields map
     */
    public final Map<? extends AbstractController, Entry<Class<? extends AbstractDto>, Set<Field>>> getMasterControllerEntityColumnFieldsMap() {
    	// detect controllers metadata
    	scanMetadata();
    	return Collections.unmodifiableMap(this.controllerEntityColumnFieldsMap);
    }
    /**
     * Get the cached {@link AbstractMasterController} entity {@link Column} fields map:<br>
     * - key is {@link RequestMapping} instance
     * - value is a map of {@link AbstractMasterController#getMainEntityType()} and the entity field names set
     * 
     * @return the cached {@link AbstractMasterController} entity fields map
     */
    public final Map<RequestMapping, Entry<String, Set<String>>> getFlatMasterControllerEntityColumnFieldsMap() {
    	// detect controllers metadata
    	scanMetadata();
    	return Collections.unmodifiableMap(this.flatControllerEntityColumnFieldsMap);
    }
    /**
     * Get the cached {@link AbstractMasterController} entity {@link Column} fields map:<br>
     * - key is {@link AbstractMasterController} instance
     * - value is a map of {@link AbstractMasterController#getMainEntityType()} and the entity field names set
     * 
     * @return the cached {@link AbstractMasterController} entity fields map
     */
    public final Map<? extends AbstractController, Entry<Class<? extends AbstractDto>, Set<Column>>> getMasterControllerEntityColumnsMap() {
    	// detect controllers metadata
    	scanMetadata();
    	return Collections.unmodifiableMap(this.controllerEntityColumnsMap);
    }
    /**
     * Get the cached {@link AbstractMasterController} entity {@link Column} fields map:<br>
     * - key is {@link RequestMapping} instance
     * - value is a map of {@link AbstractMasterController#getMainEntityType()} and the entity field names set
     * 
     * @return the cached {@link AbstractMasterController} entity fields map
     */
    public final Map<RequestMapping, Entry<String, Set<String>>> getFlatMasterControllerEntityColumnsMap() {
    	// detect controllers metadata
    	scanMetadata();
    	return Collections.unmodifiableMap(this.flatControllerEntityColumnsMap);
    }
    
    /**
     * Scan {@link AbstractController} for caching
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected final void scanMetadata() {
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
    					Collections.unmodifiableSet(new LinkedHashSet<>(BeanUtils.getFields(c.getMainEntityType(), true))))))
    			.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (k1, k2) -> k1)));
    	this.flatControllerEntityFieldsMap.putAll(
    			(Map<? extends RequestMapping, ? extends Entry<String, Set<String>>>)
    			this.controllerEntityFieldsMap.entrySet().parallelStream()
    			.filter(e -> BeanUtils.isInstanceOf(e.getKey(), AbstractMasterController.class)
    					&& Objects.nonNull(BeanUtils.safeType(e.getKey(), AbstractMasterController.class).getMainEntityType())
    					&& Objects.nonNull(BeanUtils.getClassAnnotation(e.getKey().getClass(), RequestMapping.class)))
    			.map(e -> new SimpleEntry<>(
    					BeanUtils.getClassAnnotation(e.getKey().getClass(), RequestMapping.class),
    					new SimpleEntry<>(
    							BeanUtils.safeType(e.getKey(), AbstractMasterController.class).getMainEntityType().getName(),
    							e.getValue().getValue().parallelStream()
    							.map(Field::getName).collect(Collectors.toCollection(LinkedHashSet::new)))))
    			.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (k1, k2) -> k1)));

    	this.controllerEntityColumnFieldsMap.putAll(
    			(Map) this.controllerEntityFieldsMap.entrySet().parallelStream()
    			.map(e -> new SimpleEntry<>(e.getKey(), new SimpleEntry<>(e.getValue().getKey(),
    					e.getValue().getValue().parallelStream()
    					.filter(f -> BeanUtils.getFieldAnnotation(f, Column.class) != null)
    					.collect(Collectors.toCollection(LinkedHashSet::new)))))
    			.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (k1, k2) -> k1)));
    	this.flatControllerEntityColumnFieldsMap.putAll(
    			(Map) this.controllerEntityColumnFieldsMap.entrySet().parallelStream()
    			.filter(e -> BeanUtils.isInstanceOf(e.getKey(), AbstractMasterController.class)
    					&& Objects.nonNull(BeanUtils.safeType(e.getKey(), AbstractMasterController.class).getMainEntityType())
    					&& Objects.nonNull(BeanUtils.getClassAnnotation(e.getKey().getClass(), RequestMapping.class)))
    			.map(e -> new SimpleEntry<>(
    					BeanUtils.getClassAnnotation(e.getKey().getClass(), RequestMapping.class),
    					new SimpleEntry<>(
    							BeanUtils.safeType(e.getKey(), AbstractMasterController.class).getMainEntityType().getName(),
    							e.getValue().getValue().parallelStream()
    							.map(Field::getName).collect(Collectors.toCollection(LinkedHashSet::new)))))
    			.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (k1, k2) -> k1)));

    	this.controllerEntityColumnsMap.putAll(
    			(Map) this.controllerEntityColumnFieldsMap.entrySet().parallelStream()
    			.map(e -> new SimpleEntry<>(e.getKey(), new SimpleEntry<>(e.getValue().getKey(),
    					e.getValue().getValue().parallelStream()
    					.map(f -> BeanUtils.getFieldAnnotation(f, Column.class))
    					.collect(Collectors.toCollection(LinkedHashSet::new)))))
    			.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (k1, k2) -> k1)));
    	this.flatControllerEntityColumnsMap.putAll(
    			(Map) this.controllerEntityColumnsMap.entrySet().parallelStream()
    			.filter(e -> BeanUtils.isInstanceOf(e.getKey(), AbstractMasterController.class)
    					&& Objects.nonNull(BeanUtils.safeType(e.getKey(), AbstractMasterController.class).getMainEntityType())
    					&& Objects.nonNull(BeanUtils.getClassAnnotation(e.getKey().getClass(), RequestMapping.class)))
    			.map(e -> new SimpleEntry<>(
    					BeanUtils.getClassAnnotation(e.getKey().getClass(), RequestMapping.class),
    					new SimpleEntry<>(
    							BeanUtils.safeType(e.getKey(), AbstractMasterController.class).getMainEntityType().getName(),
    							e.getValue().getValue().parallelStream()
    							.map(Column::name).collect(Collectors.toCollection(LinkedHashSet::new)))))
    			.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (k1, k2) -> k1)));
    }
    
    @Override
    public void afterPropertiesSet() {
    	// scan controllers metadata
    	scanMetadata();
    }
    
    @Override
    public final void destroy() throws Exception {
    	doDestroy();
    }

    /**
     * Release managed resources
     */
    protected void doDestroy() {
    	this.controllersSet.clear();
    }
}
