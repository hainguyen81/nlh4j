/*
 * @(#)XmlViewResolver.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jfree.util.Log;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.springframework.beans.BeansException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.servlet.View;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom {@link org.springframework.web.servlet.view.XmlViewResolver} for location classpath
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Slf4j
public class XmlViewResolver extends org.springframework.web.servlet.view.XmlViewResolver implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    
    /** {@link SpringContextHelper} */
    @Inject
    private SpringContextHelper contextHelper;
    
    /**
     * Get the {@link SpringContextHelper} instance
     * 
     * @return the {@link SpringContextHelper} instance
     */
    protected final SpringContextHelper getContextHelper() {
        if (contextHelper == null) {
            contextHelper = new SpringContextHelper();
        }
        return contextHelper;
    }

    /**
     * Set the location of the XML file that defines the view beans.
     * <p>The default is "/WEB-INF/views.xml".
     */
    @Getter(value = AccessLevel.PROTECTED)
    @Setter
    private String locationClasspath;

	/* (Non-Javadoc)
	 * @see org.springframework.web.servlet.view.XmlViewResolver#loadView(java.lang.String, java.util.Locale)
	 */
	@Override
	protected View loadView(String viewName, Locale locale) throws BeansException {
		// if afterPropertiesSet not resolve location, so resolve it now
		if (!super.isCache()) {
			this.resolveLocationClasspath();
		}
		// load by super
		return super.loadView(viewName, locale);
	}

	/* (Non-Javadoc)
	 * @see org.springframework.web.servlet.view.XmlViewResolver#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws BeansException {
		// if cache, super will call initFactory function, so need to resolve location
		if (super.isCache()) {
			this.resolveLocationClasspath();
		}
		// apply by super
		super.afterPropertiesSet();
		Optional.ofNullable(super.getApplicationContext())
		.ifPresent(ac -> getContextHelper().setApplicationContext(ac));
	}

	/**
	 * Resolve location classpath if necessary
	 */
	private void resolveLocationClasspath() throws ApplicationRuntimeException {
		Resource resource = null;
		try {
		    resource = findResourceByLocation(StringUtils.trimToEmpty(getLocationClasspath()));
		} catch (Exception e) {
		    throw new ApplicationRuntimeException(e.getMessage(), e);
		} finally {
		    if (resource != null) {
		        super.setLocation(resource);
		    }
		}
	}
	
	/**
	 * Find {@link Resource} by the specified location
	 * 
	 * @param location to find
	 * 
	 * @return {@link Resource}/NULL
	 */
	protected Resource findResourceByLocation(String location) {
	    Resource[] resources = null;
	    if (StringUtils.isNotBlank(location)) {
            // resource resolve
            PathMatchingResourcePatternResolver resourceResolver =
                    Optional.ofNullable(super.getApplicationContext())
                    .map(ac -> new PathMatchingResourcePatternResolver(ac.getClassLoader()))
                    .orElseGet(() -> Optional.ofNullable(super.obtainApplicationContext())
                            .map(ac -> new PathMatchingResourcePatternResolver(ac.getClassLoader())).orElseGet(null));
            // by patch matching resource pattern
            try {
                // resolve all occured resources
                resources = resourceResolver.getResources(location);
                
            } catch (Exception e) {
                if (Log.isDebugEnabled()) {
                    log.warn("Could not found resource based on [{}] by [{}]: {}", location,
                            PathMatchingResourcePatternResolver.class.getSimpleName(), e.getMessage(), e);
                }
            }

            // try with context helper
            if (ArrayUtils.isEmpty(resources)) {
                resources = Optional.ofNullable(getContextHelper()).map(ctx -> ctx.searchResources(
                        org.apache.commons.lang3.StringUtils.trimToEmpty(getLocationClasspath()))).orElseGet(LinkedHashMap::new)
                        .values().parallelStream().filter(CollectionUtils::isNotEmpty)
                        .flatMap(List<Resource>::parallelStream).collect(Collectors.toCollection(LinkedList::new)).toArray(new Resource[0]);
            }
        }
	    
	    // exception if multiple resources
        if (ArrayUtils.isEmpty(resources)) {
            throw new IllegalArgumentException(
                    "Could not found any resource by location [" + location + "]");

            // multiple resources
        } else if (resources != null && resources.length > 1) {
            throw new IllegalArgumentException(
                    "Found [" + resources.length + "] resources. Conflict for multiple resources.");
        }

        return Stream.of(resources).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Could not found any resource by location [" + location + "]"));
	}
}
