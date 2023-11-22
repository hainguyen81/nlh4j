/*
 * @(#)ApplicationContextProvider.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.servlet;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.WebApplicationContextUtils;

import org.nlh4j.util.RequestUtils;

/**
 * {@link ApplicationContext} aware provider.
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Singleton
public final class ApplicationContextProvider implements ApplicationContextAware, ServletContextAware, ServletConfigAware, Serializable {

    /** */
    private static final long serialVersionUID = 1L;

	/** {@link ServletConfig} */
	private static ServletConfig servletConfig;
	/** {@link ServletContext} */
	private static ServletContext servletContext;
	/** {@link ApplicationContext} */
	private static ApplicationContext applicationContext;
    /** {@link HttpServletRequest} */
	@Inject
    private HttpServletRequest request;

	/**
	 * Get the servlet configuration
	 *
	 * @return the servlet configuration
	 */
	public ServletConfig getServletConfig() {
	    return getAwareServletConfig();
    }
	/**
	 * Get the servlet configuration
	 *
	 * @return the servlet configuration
	 */
	public static ServletConfig getAwareServletConfig() {
	    if (servletConfig == null) return null;
	    synchronized (servletConfig) {
	        return servletConfig;
        }
    }

	/**
	 * Get the servlet context
	 *
	 * @return the servlet context
	 */
    public ServletContext getServletContext() {
        return getAwareServletContext();
    }
	/**
	 * Get the servlet context
	 *
	 * @return the servlet context
	 */
    public static ServletContext getAwareServletContext() {
        if (servletContext == null && servletConfig != null) {
            servletContext = getServletContext(servletConfig);
        }
        if (servletContext == null) return null;
        synchronized (servletContext) {
            return servletContext;
        }
    }

    /**
     * Get the servlet context from {@link ServletConfig}
     *
     * @param servletConfig {@link ServletConfig}
     *
     * @return the servlet context
     */
    public static ServletContext getServletContext(ServletConfig servletConfig) {
        ServletContext servletContext = null;
        if (servletConfig != null) {
            servletContext = servletConfig.getServletContext();
        }
        return servletContext;
    }
    /**
     * Get the servlet context from {@link HttpServletRequest}
     *
     * @param request {@link HttpServletRequest}
     *
     * @return the servlet context
     */
    public static ServletContext getServletContext(HttpServletRequest request) {
        ServletContext servletContext = null;
        if (request != null) {
            servletContext = request.getServletContext();
        }
        return servletContext;
    }

    /**
     * Get the application context
     *
     * @return the application context
     */
    public ApplicationContext getApplicationContext() {
    	return getAwareApplicationContext();
	}
    /**
     * Get the application context
     *
     * @return the application context
     */
    public static ApplicationContext getAwareApplicationContext() {
    	// try detect context from config
	    if (applicationContext == null && servletConfig != null) {
            applicationContext = getApplicationContext(servletConfig);
        }
	    // try detect context from servlet
	    if (applicationContext == null && servletContext != null) {
            applicationContext = getApplicationContext(servletContext);
        }
	    if (applicationContext == null) return null;
	    synchronized (applicationContext) {
	        return applicationContext;
        }
	}
    /**
     * Get the application context from {@link ServletConfig}
     *
     * @param servletConfig {@link ServletConfig}
     *
     * @return the application context
     */
    public static ApplicationContext getApplicationContext(ServletConfig servletConfig) {
        return getApplicationContext(getServletContext(servletConfig));
    }
    /**
     * Get the application context from {@link ServletContext}
     *
     * @param servletContext {@link ServletContext}
     *
     * @return the application context
     */
    public static ApplicationContext getApplicationContext(ServletContext servletContext) {
        ApplicationContext applicationContext = null;
        if (servletContext != null) {
            applicationContext = WebApplicationContextUtils
                    .getRequiredWebApplicationContext(servletContext);
        }
        return applicationContext;
    }
    /**
     * Get the application context from {@link HttpServletRequest}
     *
     * @param request {@link HttpServletRequest}
     *
     * @return the application context
     */
    public static ApplicationContext getApplicationContext(HttpServletRequest request) {
        return getApplicationContext(getServletContext(request));
    }

    /* (Non-Javadoc)
     * @see org.springframework.web.context.ServletConfigAware#setServletConfig(javax.servlet.ServletConfig)
     */
    @Override
    public void setServletConfig(ServletConfig servConfig) {
        servletConfig = servConfig;
    }
    /* (Non-Javadoc)
     * @see org.springframework.web.context.ServletContextAware#setServletContext(javax.servlet.ServletContext)
     */
	@Override
	public void setServletContext(ServletContext servContext) {
		servletContext = servContext;
		if (servletContext == null) {
        	if (this.request == null) this.request = RequestUtils.getHttpRequest();
            servletContext = getServletContext(this.request);
        }
	}
	/* (Non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
    @Override
    public void setApplicationContext(ApplicationContext appContext) {
        applicationContext = appContext;
		if (applicationContext == null) {
        	if (this.request == null) this.request = RequestUtils.getHttpRequest();
        	applicationContext = getApplicationContext(this.request);
        }
    }
}
