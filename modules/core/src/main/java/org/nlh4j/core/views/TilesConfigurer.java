/*
 * @(#)TilesConfigurer.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.tiles.TilesException;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.ApplicationResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.servlet.view.tiles3.SpringWildcardServletTilesApplicationContext;

import org.nlh4j.core.servlet.ApplicationContextProvider;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.StringUtils;

/**
 * An extended class of {@link org.springframework.web.servlet.view.tiles3.TilesConfigurer}
 * for checking valid definitions while initializing
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class TilesConfigurer extends org.springframework.web.servlet.view.tiles3.TilesConfigurer
		implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    private static final String CLASSPATH_PREFIX = "classpath";

	/**
	 * SLF4J
	 */
	private static final Logger logger = LoggerFactory.getLogger(TilesConfigurer.class);

	/** tile definitions */
	private ServletContext servletContext;
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
     * Parse valid definitions from the specified definitions
     *
     * @param context application context
     * @param definitions resource definitions
     */
    private void parseValidDefinitions(ApplicationContext context, String...definitions) {
    	// if defined definitions
    	List<String> validDefinitions = new LinkedList<String>();
    	if (!CollectionUtils.isEmpty(definitions)) {
    		// debug
    		if (logger.isDebugEnabled()) {
    			logger.debug("Resolving definitions ["
    					+ org.springframework.util.StringUtils.arrayToCommaDelimitedString(definitions) + "]");
    		}
    		// check definition
    		SpringContextHelper helper = this.getContextHelper();
    		org.springframework.context.ApplicationContext appCtx =
    				(helper != null ? helper.getContext() : null);
			for(String definition : definitions) {
				boolean valid = false;
				Resource[] ctxRes = null;

				// try with Apache context
				if (context != null) {
					// resolve all occured resources
					Collection<ApplicationResource> resources = null;
					try {
						// with classpath prefix
						resources = context.getResources(definition);
						valid = !CollectionUtils.isEmpty(resources);
						// cache valid definition
						if (valid) {
							validDefinitions.add(definition);
						}
						// debug
						if (logger.isTraceEnabled() && valid) {
							List<String> resolved = new LinkedList<String>();
							for(ApplicationResource resource : resources) {
								String path = null;
								// resource path
								if (!StringUtils.hasText(path)) {
									try {
										path = resource.getLocalePath();
									} catch (Exception e) {}
								}
								if (!StringUtils.hasText(path)) resolved.add(path);
							}
							if (!CollectionUtils.isEmpty(resources)) {

							}
							if (logger.isTraceEnabled()) {
								logger.debug("Resolved resource pattern [" + definition + "] to resource ["
										+ (CollectionUtils.isEmpty(resolved)
												? "" : org.springframework.util.StringUtils.collectionToCommaDelimitedString(resolved)));
							}
						}
					} catch (Exception e) {
						if (!CollectionUtils.isEmpty(resources)) {
							resources.clear();
						}
						valid = false;
					}
				}

				// try with application context
				if (!valid && appCtx != null) {
					try {
						// resource resolve
						PathMatchingResourcePatternResolver resourceResolver =
								new PathMatchingResourcePatternResolver(appCtx.getClassLoader());
						// with classpath prefix
						if (definition.toLowerCase().startsWith(CLASSPATH_PREFIX)) {
							ctxRes = resourceResolver.getResources(definition);
						} else {
							ctxRes = appCtx.getResources(definition);
						}
						valid = !CollectionUtils.isEmpty(ctxRes);
						// parse for correct resource path
						// because super class could not found it
						if (valid) {
							List<String> resolved = new LinkedList<String>();
							for(Resource resource : ctxRes) {
								String path = null;
                                //	// file path
                                //	if (!StringUtils.hasText(path)) {
                                //		try {
                                //			path = resource.getFile().getPath();
                                //		} catch (Exception e) {}
                                //	}
                                //	// URI path
                                //	if (!StringUtils.hasText(path)) {
                                //		try {
                                //			path = resource.getURI().getPath();
                                //		} catch (Exception e) {}
                                //	}
								// URL path
								if (!StringUtils.hasText(path)) {
									try {
										path = resource.getURL().getPath();
									} catch (Exception e) {}
								}
								// cache valid resource path as definition
								if (StringUtils.hasText(path)) resolved.add(path);
							}
							// if valid; then using it as definition
							if (!CollectionUtils.isEmpty(resolved)) {
								validDefinitions.addAll(resolved);
							}
							// debug
							if (logger.isTraceEnabled()) {
								logger.debug("Resolved resource pattern [" + definition + "] to resource ["
										+ (CollectionUtils.isEmpty(resolved)
												? "" : org.springframework.util.StringUtils.collectionToCommaDelimitedString(resolved)));
							}
						}
					} catch (Exception e) {
						ctxRes = null;
						valid = false;
					}
				}

				// if invalid
				if (!valid && logger.isTraceEnabled()) {
					logger.error("Could not found resource at location: [" + definition + "]");
				}
			}
		}
    	// re-apply valid definitions
    	this.definitions = (CollectionUtils.isEmpty(validDefinitions)
    			? new String[] {} : validDefinitions.toArray(new String[validDefinitions.size()]));
    	super.setDefinitions(this.definitions);
    	if (CollectionUtils.isEmpty(validDefinitions)) {
    		logger.warn("Could not found any valid definitions"
    				+ (CollectionUtils.isEmpty(definitions)
    						? "" : " from [" + org.springframework.util.StringUtils.arrayToCommaDelimitedString(definitions) + "]"));
    	} else {
    	    logger.info("Resolved [" + validDefinitions.size() + "] valid tiles definitions to ["
                    + org.springframework.util.StringUtils.collectionToCommaDelimitedString(validDefinitions));
    	}
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
