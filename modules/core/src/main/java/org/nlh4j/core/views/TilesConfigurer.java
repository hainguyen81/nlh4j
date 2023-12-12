/*
 * @(#)TilesConfigurer.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.io.Serializable;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import com.machinezoo.noexception.Exceptions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tiles.TilesException;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.ApplicationResource;
import org.nlh4j.core.servlet.ApplicationContextProvider;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.util.ExceptionUtils;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.view.tiles3.SpringWildcardServletTilesApplicationContext;

import lombok.extern.slf4j.Slf4j;

/**
 * An extended class of {@link org.springframework.web.servlet.view.tiles3.TilesConfigurer}
 * for checking valid definitions while initializing
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Slf4j
public class TilesConfigurer extends org.springframework.web.servlet.view.tiles3.TilesConfigurer
		implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;

	/** tile definitions */
	private transient ServletContext servletContext;
	private String[] definitions;

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
    	if (this.contextHelper == null && this.servletContext != null) {
    		this.contextHelper = new SpringContextHelper(this.servletContext);

        } else if (this.contextHelper == null && this.contextProvider != null
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
        return Optional.ofNullable(this.getContextHelper()).map(ctx -> ctx.searchBean(beanClass)).orElse(null);
    }
    /**
     * Get bean by the specified bean class
     *
     * @param beanClass the bean class to parse from context
     *
     * @return bean or NULL
     */
    protected final <K> List<K> findBeans(Class<K> beanClass) {
        return Optional.ofNullable(this.getContextHelper()).map(ctx -> ctx.searchBeans(beanClass)).orElseGet(LinkedList::new);
    }

    /**
     * Parse valid definitions from the specified definitions
     *
     * @param context application context
     * @param definitions resource definitions
     */
    private void parseValidDefinitions(ApplicationContext context, String...definitions) {
    	// if defined definitions
    	Set<String> validDefinitions = new LinkedHashSet<>();
    	if (ArrayUtils.isNotEmpty(definitions)) {
    		// debug
    		if (log.isDebugEnabled()) {
    			log.debug("Resolving definitions ["
    					+ org.springframework.util.StringUtils.arrayToCommaDelimitedString(definitions) + "]");
    		}

    		// find definitions from Apache context
    		List<String> definitionsList = Stream.of(Optional.ofNullable(definitions).orElseGet(() -> new String[0]))
    	    		.parallel().filter(StringUtils::isNotBlank).collect(Collectors.toCollection(LinkedList::new));
    		Map<String, Collection<ApplicationResource>> definitionResources = definitionsList.parallelStream()
    				.map(d -> new SimpleEntry<>(d, Optional.ofNullable(context)
    						.map(ExceptionUtils.wrap(log).function(Exceptions.wrap().function(c -> c.getResources(d))))
    						.filter(Optional::isPresent).map(Optional::get)
    						.filter(CollectionUtils::isNotEmpty).orElseGet(LinkedList::new)))
    				.filter(e -> CollectionUtils.isNotEmpty(e.getValue()))
    				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (k1, k2) -> k1));
    		// remove resolved definitions
    		definitionsList.removeAll(definitionResources.keySet());
    		validDefinitions.addAll(
    				definitionResources.entrySet().parallelStream()
    				.map(Entry::getValue).flatMap(Collection<ApplicationResource>::parallelStream)
    				.map(ExceptionUtils.wrap(log).function(Exceptions.wrap().function(ApplicationResource::getLocalePath)))
    				.filter(Optional::isPresent).map(Optional::get)
    				.filter(StringUtils::isNotBlank)
    				.distinct().collect(Collectors.toCollection(LinkedList::new)));
    		if (log.isTraceEnabled()) {
    			log.debug("APACHE CONTEXT: Resolved resources [{}]", StringUtils.join(validDefinitions, ", "));
    		}

    		// find remaining definitions from Spring context
    		SpringContextHelper helper = this.getContextHelper();
    		if (CollectionUtils.isNotEmpty(definitionsList) && helper != null) {
	    		validDefinitions.addAll(
	    				definitionsList.parallelStream()
	    				.map(d -> Optional.ofNullable(helper)
	    						.map(ExceptionUtils.wrap(log).function(Exceptions.wrap().function(h -> h.searchResources(d))))
	    						.filter(Optional::isPresent).map(Optional::get).map(Map<String, List<Resource>>::values)
	    						.filter(CollectionUtils::isNotEmpty).orElseGet(LinkedList::new))
	    				.filter(CollectionUtils::isNotEmpty)
	    				.flatMap(Collection<List<Resource>>::parallelStream)
	    				.flatMap(List<Resource>::parallelStream)
	    				.map(ExceptionUtils.wrap(log).function(Exceptions.wrap().function(Resource::getURL)))
	    				.filter(Optional::isPresent).map(Optional::get)
	    				.map(ExceptionUtils.wrap(log).function(Exceptions.wrap().function(URL::getPath)))
	    				.filter(Optional::isPresent).map(Optional::get)
	    				.filter(StringUtils::isNotBlank)
	    				.distinct().collect(Collectors.toCollection(LinkedList::new)));
	    		if (log.isTraceEnabled()) {
	    			log.debug("SPRING CONTEXT: Resolved resources [{}]", StringUtils.join(validDefinitions, ", "));
	    		}
    		}
		}
    	// re-apply valid definitions
    	this.definitions = validDefinitions.toArray(new String[0]);
    	super.setDefinitions(this.definitions);
    }

    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.view.tiles3.TilesConfigurer#setServletContext(javax.servlet.ServletContext)
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
    	super.setServletContext(servletContext);
    	this.servletContext = servletContext;
    }
	/* (Non-Javadoc)
	 * @see org.springframework.web.servlet.view.tiles3.TilesConfigurer#setDefinitions(java.lang.String[])
	 */
	@Override
	public void setDefinitions(String... definitions) {
		// apply valid definitions
		super.setDefinitions(definitions);
		this.definitions = definitions;
	}

	/* (Non-Javadoc)
	 * @see org.springframework.web.servlet.view.tiles3.TilesConfigurer#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws TilesException {
		// check valid definitions
		ApplicationContext appContext = new SpringWildcardServletTilesApplicationContext(this.servletContext);
		this.parseValidDefinitions(appContext, this.definitions);
		// continue with super initialize
		super.afterPropertiesSet();
	}
}
