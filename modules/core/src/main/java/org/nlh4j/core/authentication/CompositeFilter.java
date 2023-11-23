/*
 * @(#)CompositeFilter.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import org.nlh4j.core.servlet.ApplicationContextProvider;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;

/**
 * Custom {@link org.springframework.web.filter.CompositeFilter}
 * for filters from properties
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class CompositeFilter
    extends org.springframework.web.filter.CompositeFilter
    implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * SLF4J
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private List<String> filterBeanNames = new ArrayList<String>();

    /** {@link ApplicationContext} */
    private ApplicationContext applicationContext;
    /** {@link ApplicationContextProvider} */
    @Inject
    protected ApplicationContextProvider contextProvider;
    /** context helper */
    @Inject
    private SpringContextHelper contextHelper;

    /**
     * Get the context helper
     * @return the context helper
     */
    protected final SpringContextHelper getContextHelper() {
    	if (this.contextHelper == null && this.getContext() != null) {
    		this.contextHelper = new SpringContextHelper(this.getContext());
    	}
    	if (this.contextHelper == null) return null;
    	synchronized(this.contextHelper) {
    		return this.contextHelper;
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

    /**
     * Get the application context
     * @return the application context
     */
    protected final ApplicationContext getContext() {
    	return (this.applicationContext != null
    			? this.applicationContext
    					: this.contextProvider != null
    					? this.contextProvider.getApplicationContext() : null);
    }

    /**
     * Set application context
     * @param applicationContext context
     */
    public final void setApplicationContext(ApplicationContext applicationContext) {
    	this.applicationContext = applicationContext;
    }

    /**
     * Initialize a new instance of {@link CompositeFilter}
     */
    public CompositeFilter() {}
    /**
     * Initialize a new instance of {@link CompositeFilter}
     *
     * @param applicationContext context
     */
    public CompositeFilter(ApplicationContext applicationContext) {
    	this.setApplicationContext(applicationContext);
    }

    /**
     * @param filterBeanNames the filterBeanNames to set
     */
    public void setFilterBeanNames(String filterBeanNames) {
        List<String> filters = new LinkedList<String>();
        if (StringUtils.hasText(filterBeanNames)) {
            String[] filterNames = StringUtils.commaDelimitedListToStringArray(filterBeanNames);
            if (!ArrayUtils.isEmpty(filterNames)) {
                for(String filterName : filterNames) {
                    if (!StringUtils.hasText(filterName)) continue;
                    filters.add(filterName.trim());
                }
            }
        }
        this.setFilterBeanNamesList(filters);
    }
    /**
     * @param filterBeanNames the filterBeanNames to set
     */
    public void setFilterBeanNamesList(List<String> filterBeanNames) {
        this.filterBeanNames = filterBeanNames;
        List<Filter> filters = new LinkedList<Filter>();
        if (!CollectionUtils.isEmpty(this.filterBeanNames)) {
            for(String filterName : this.filterBeanNames) {
                if (!StringUtils.hasText(filterName)) continue;
                filterName = filterName.trim();
                String[] filterNames = null;
                if (filterName.indexOf(",") > 0) {
                    filterNames = StringUtils.commaDelimitedListToStringArray(filterName);
                }
                else {
                    filterNames = new String[] { filterName };
                }
                for(String filter : filterNames) {
                    Object bean = this.getContextHelper().searchBean(filter);
                    if (bean == null) bean = this.getContextHelper().searchBean(filter, Filter.class);
                    if (bean == null) {
                        // search with filter name as filter class
                        try {
                            Class<?> filterClass = Class.forName(filter);
                            bean = this.getContextHelper().searchBean(filterClass);
                        }
                        catch(Exception e) {}
                    }
                    if (BeanUtils.isInstanceOf(bean, Filter.class)) filters.add((Filter) bean);
                }
            }
        }
        // apply filters
        super.setFilters(filters);
    }

    /* (Non-Javadoc)
     * @see org.springframework.web.filter.CompositeFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	// check for context helper
    	if (this.getContextHelper() == null && request != null) {
    		this.contextHelper = new SpringContextHelper(request.getServletContext());
    	}
    	// by super class
    	super.doFilter(request, response, chain);
    }
}
