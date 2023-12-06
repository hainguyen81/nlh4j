/*
 * @(#)XmlViewResolver.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.io.Serializable;
import java.util.Locale;

import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.servlet.View;

import lombok.Setter;

/**
 * Custom {@link org.springframework.web.servlet.view.XmlViewResolver} for location classpath
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class XmlViewResolver extends org.springframework.web.servlet.view.XmlViewResolver implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    private static final String CLASSPATH_PREFIX = "classpath";
    /**
     * Set the location of the XML file that defines the view beans.
     * <p>The default is "/WEB-INF/views.xml".
     */
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
	}

	/**
	 * Resolve location classpath if necessary
	 */
	private void resolveLocationClasspath() throws ApplicationRuntimeException {
		// classpath prefix
		if (StringUtils.hasText(this.locationClasspath)
				&& this.locationClasspath.trim().toLowerCase().startsWith(CLASSPATH_PREFIX)) {
			// resource resolve
			PathMatchingResourcePatternResolver resourceResolver =
					new PathMatchingResourcePatternResolver(
							super.getApplicationContext().getClassLoader());
			try {
				// resolve all occured resources
				Resource[] resources = resourceResolver.getResources(this.locationClasspath);
				// exception if multiple resources
				if (!CollectionUtils.isElementsNumber(resources, 1)) {
					throw new IllegalArgumentException(
							"Found [" + String.valueOf(CollectionUtils.getSize(resources))
							+ "] resources. Conflict for multiple resources.");

					// if found one resource; else just using as default
				} else if (CollectionUtils.isElementsNumber(resources, 1)) {
					super.setLocation(resources[0]);
				}
			} catch (Exception e) {
				throw new ApplicationRuntimeException(e);
			}


		} else if (StringUtils.hasText(this.locationClasspath)) {
			super.setLocation(super.getApplicationContext().getResource(this.locationClasspath));
		}
	}
}
