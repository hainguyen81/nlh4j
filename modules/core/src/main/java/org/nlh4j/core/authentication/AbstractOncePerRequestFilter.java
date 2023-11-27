/*
 * @(#)AbstractOncePerRequestFilter.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ErrorHandler;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.DispatcherServlet;

import org.nlh4j.core.handlers.GlobalExceptionResolver;
import org.nlh4j.core.service.MessageService;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.exceptions.ApplicationRuntimeException;

/**
 * Abstract filter once per request
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public abstract class AbstractOncePerRequestFilter extends OncePerRequestFilter implements Serializable {

	/** */
    private static final long serialVersionUID = 1L;
    /**
     * SLF4J
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** {@link MessageService} */
    @Inject
    protected MessageService messageService;

    /** {@link ErrorHandler} (spport for filtering {@link DispatcherServlet} exception) */
	private ErrorHandler errorHandler;

	/**
	 * Get the dispatch error handler
	 * @return the dispatch error handler
	 */
	protected final ErrorHandler getErrorHandler() {
		if (this.errorHandler == null) {
			this.errorHandler = this.getContextHelper().searchBean(ErrorHandler.class);
		}
		if (this.errorHandler == null) return this.errorHandler;
		synchronized(this.errorHandler) {
			return this.errorHandler;
		}
	}

    /** {@link ApplicationContext} */
    private ApplicationContext applicationContext;

    /**
     * Get the web application context {@link ApplicationContext}
     * @return the web application context {@link ApplicationContext}
     */
    protected final ApplicationContext getApplicationContext() {
    	if (this.applicationContext == null) {
    		this.applicationContext = WebApplicationContextUtils
    				.getRequiredWebApplicationContext(super.getServletContext());
    	}
    	if (this.applicationContext == null) return null;
    	synchronized(this.applicationContext) {
    		return this.applicationContext;
    	}
    }

    /** {@link SpringContextHelper} */
    @Inject
    private SpringContextHelper contextHelper;

    /**
     * Get the context helper to find bean if necessary
     * @return the context helper to find bean if necessary
     */
    protected final SpringContextHelper getContextHelper() {
    	if (this.contextHelper == null) {
    		ApplicationContext context = this.getApplicationContext();
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

//    /**
//	 * Get the exceptionResolver
//	 *
//	 * @return the exceptionResolver
//	 */
//	protected final GlobalExceptionResolver getExceptionResolver() {
//		if (this.exceptionResolver == null
//				&& this.getContextHelper() != null) {
//			this.exceptionResolver = this.getContextHelper().searchBean(GlobalExceptionResolver.class);
//		}
//		if (this.exceptionResolver == null) return null;
//		synchronized(this.exceptionResolver) {
//			return this.exceptionResolver;
//		}
//	}

    /*
     * (Non-Javadoc)
     * @see org.springframework.web.filter.OncePerRequestFilter#doFilterInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
     */
    @Override
	protected final void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
    	// do filter before
    	if (logger.isDebugEnabled()) {
    		logger.debug(">>> Do filter chain [" + request.getRequestURI() + "] - [" + this.getClass().getName() + "]....");
    	}
    	// check for helper
    	if (this.getContextHelper() == null) {
    		this.contextHelper = new SpringContextHelper(request.getServletContext());
    	}
    	// filter
    	try {
	    	if (!this.doBeforeFilter(request, response)) {
		    	// filter chain do filtering
		    	filterChain.doFilter(request, response);
		    	// do after filtering
		    	this.doAfterFilter(request, response);
	    	}
    	} catch (Exception e) {
    		if (logger.isErrorEnabled()) logger.error(e.getMessage(), e);
    		// apply error attribute
    		request.setAttribute("ERROR", e);
    		try {
    			// try diapatching exception resolve
    			this.resolveDispatchException(request, response, e);
    		} catch (Exception ex) {
    			// if not override or exception dispatcher exception resolver;
    			// then using global exception resolver
    			if (this.exceptionResolver != null) {
    				this.exceptionResolver.handleException(
    						null, request, response, ex,
    						(this.getContextHelper() == null ? null
    								: this.getContextHelper().searchHandler(request)));

    				// if not handle exception yet
    				// just throwing it
    			} else throw e;
    		}
    	}
    };

    /**
     * TODO Override for filtering before {@link FilterChain} do filtering
     *
     * @param request the current request
     * @param response the current response
     *
     * @return true for not doing filter chain; else doing it
     */
    protected abstract boolean doBeforeFilter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
    /**
     * TODO Override for filtering after {@link FilterChain} do filtering
     *
     * @param request the current request
     * @param response the current response
     */
    protected void doAfterFilter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	// TODO Override for filtering after {@link FilterChain} do filtering
    }
    /**
     * TODO Override for dispatching filter exception.<br>
     * Default just throwing the specified exception as {@link ApplicationRuntimeException}.<br>
     * Children classes maybe using {@link #getErrorHandler()} to dispatch exception to custom error page.
     *
     * @param request the current request
     * @param response the current response
     * @param ex current exception
     */
    protected void resolveDispatchException(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
    	// TODO Override for dispatching filter exception
    	throw new ApplicationRuntimeException(ex);
    }


}
