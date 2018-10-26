/*
 * @(#)HandlerExceptionResolver.java 1.0 Feb 18, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.handlers;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.ContextExposingHttpServletRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import lombok.Getter;
import lombok.Setter;
import org.nlh4j.core.servlet.ApplicationContextProvider;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.exceptions.ApplicationException;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.EncryptUtils;
import org.nlh4j.util.StringUtils;

/**
 * Handle request HANDLER exception (request page view (ModelAndView) and response page view)<br>
 * An extended class of {@link DefaultHandlerExceptionResolver}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Component
@ControllerAdvice
@Singleton
public class HandlerExceptionResolver extends DefaultHandlerExceptionResolver implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * slf4j
	 */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    /** specify exposing context beans as view attributes */
	@Getter
    @Setter
    private boolean exposeContextBeansAsAttributes = false;
    /** specify exposing context bean names */
	@Getter
    @Setter
    private Set<String> exposedContextBeanNames;
    /** specify exposing context bean names */
	@Getter
    @Setter
    private String[] exposedContextBeanNamesArray;
    /** specify exposing context bean names */
	@Getter
    @Setter
    private Map<Integer, String> errorStatusPages;
    /** specify exposing context bean names */
	@Getter
    @Setter
    private Map<String, String> errorExceptionPages;
    /** view reolvers to check */
    @Setter
    private List<ViewResolver> viewResolvers;
    /**
     * Get the {@link ViewResolver} list from context
     * @return the {@link ViewResolver} list from context
     */
    protected final List<ViewResolver> getViewResolvers() {
    	if (CollectionUtils.isEmpty(this.viewResolvers)
    			&& this.getContextHelper() != null) {
    		this.viewResolvers = this.getContextHelper().searchBeans(ViewResolver.class);
    	}
    	if (this.viewResolvers == null) return null;
    	synchronized(this.viewResolvers) {
    		return this.viewResolvers;
    	}
    }

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

    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.view.AbstractView#getRequestToExpose(javax.servlet.http.HttpServletRequest)
     */
    //@Override
    protected HttpServletRequest getRequestToExpose(HttpServletRequest originalRequest) {
    	ApplicationContext applicationContext = (this.contextProvider != null
    			? this.contextProvider.getApplicationContext()
    					: this.getContextHelper() != null
    					? this.getContextHelper().getContext()
    							: ApplicationContextProvider.getApplicationContext(originalRequest));
    	WebApplicationContext webAppContext = BeanUtils.safeType(
    			applicationContext, WebApplicationContext.class);
        if (applicationContext != null
        		&& (this.isExposeContextBeansAsAttributes()
        				|| !CollectionUtils.isEmpty(this.getExposedContextBeanNames()))) {
            return new ContextExposingHttpServletRequest(
                    originalRequest, webAppContext,
                    this.getExposedContextBeanNames());
        } else if (applicationContext != null
        		&& (this.isExposeContextBeansAsAttributes()
        				|| !CollectionUtils.isEmpty(this.getExposedContextBeanNamesArray()))) {
            return new ContextExposingHttpServletRequest(
                    originalRequest, webAppContext,
                    CollectionUtils.toSet(this.getExposedContextBeanNamesArray()));
        }
        return originalRequest;
    }

	/* (Non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver#doResolveException(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	protected final ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		// check for context helper
		if (this.getContextHelper() == null && request != null
				&& request.getServletContext() != null) {
			this.contextHelper = new SpringContextHelper(request.getServletContext());
		}
		// check for inner exception
		if (BeanUtils.isInstanceOf(ex, ApplicationException.class)) {
			Exception innerEx = BeanUtils.safeType(BeanUtils.safeType(ex, ApplicationException.class).getCause(), Exception.class);
			if (innerEx != null) ex = innerEx;

			// check for runtime inner exception
		} else if (BeanUtils.isInstanceOf(ex, ApplicationRuntimeException.class)) {
			Exception innerEx = BeanUtils.safeType(BeanUtils.safeType(ex, ApplicationRuntimeException.class).getCause(), Exception.class);
			if (innerEx != null) ex = innerEx;
		}
		// resolve exception as supper class
		boolean handledError = (!CollectionUtils.isEmpty(this.getErrorStatusPages())
				|| !CollectionUtils.isEmpty(this.getErrorExceptionPages()));
		ModelAndView mav = null;
		if (handledError) {
			// check existed error view
			String viewName = null;
			HttpStatus status = null;
			// by status code mapping
			if (!CollectionUtils.isEmpty(this.getErrorStatusPages())) {
				status = this.mapException(ex);
				viewName = this.getErrorStatusPages().get(status.value());
			}
			// or by exception class name mapping
			if (!StringUtils.hasText(viewName)
					&& !CollectionUtils.isEmpty(this.getErrorExceptionPages())) {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				viewName = this.getErrorExceptionPages().get(ex.getClass().getName());
			}
			// return valid view if handled
			handledError = StringUtils.hasText(viewName);
			if (handledError) {
				View view = this.resolveView(request, viewName);
				handledError = (view != null);
				if (handledError) {
					// apply view
					mav = new ModelAndView();
					mav.addObject("exception", ex);
					mav.setView(view);
					// apply response status
					response.setStatus(status.value());
					response.setCharacterEncoding(EncryptUtils.ENCODING_UTF8);
				}
			}
		}
		// if not handling error
		if (!handledError) {
			// expose beans into request
			HttpServletRequest exposedRequest = this.getRequestToExpose(request);
			mav = super.doResolveException(exposedRequest, response, handler, ex);
		}
		// debug
		if ((mav == null || mav.isEmpty()) && logger.isDebugEnabled()) {
			logger.warn("Not found definition of error pages for response status code (could not using exposed context beans in your definition error pages in web.xml)");
		}
		return mav;
	}

	/**
	 * Resolve the specified view name to {@link View}
	 *
	 * @param request to parse context if necessary
	 * @param viewName view name
	 *
	 * @return {@link View} or NULL
	 */
	protected final View resolveView(HttpServletRequest request, String viewName) {
		// check parameter
		if (!StringUtils.hasText(viewName)) return null;
		// finds all view resolvers to check view
		// if not configuration view resolvers, try searching veiw resolvers
		if (CollectionUtils.isEmpty(this.getViewResolvers())) {
			ApplicationContext applicationContext = (this.contextProvider != null
	    			? this.contextProvider.getApplicationContext()
	    					: this.getContextHelper() != null
	    					? this.getContextHelper().getContext()
	    							: ApplicationContextProvider.getApplicationContext(request));
			Map<String, ViewResolver> matchingBeans =
					BeanFactoryUtils.beansOfTypeIncludingAncestors(
							applicationContext, ViewResolver.class, true, false);
			if (!CollectionUtils.isEmpty(matchingBeans)) {
				this.viewResolvers = new LinkedList<ViewResolver>(matchingBeans.values());
			}
		}
		// if found resolvers
		View view = null;
		if (!CollectionUtils.isEmpty(this.getViewResolvers())) {
			for(ViewResolver resolver : this.getViewResolvers()) {
				try { view = resolver.resolveViewName(viewName, LocaleContextHolder.getLocale()); }
				catch (Exception e) {
					if (logger.isDebugEnabled()) {
						logger.warn(resolver.getClass().getName() + " could not found view [" + viewName + "]");
					}
					view = null;
				}
				if (view != null) break;
			}
		}
		return view;
	}

	/**
	 * Map exception to response status to found configured error page.<br>
	 * TODO Override to re-map error page by response status for exception
	 *
	 * @param e to map
	 *
	 * @return {@link HttpStatus}
	 */
	protected HttpStatus mapException(Exception e) {
		// 404
		if (BeanUtils.isInstanceOf(e, NoHandlerFoundException.class)) {
			return HttpStatus.NOT_FOUND;
		}
		// 405
		else if (BeanUtils.isInstanceOf(e, HttpRequestMethodNotSupportedException.class)) {
			return HttpStatus.METHOD_NOT_ALLOWED;
		}
		// 415
		else if (BeanUtils.isInstanceOf(e, HttpMediaTypeNotSupportedException.class)) {
			return HttpStatus.UNSUPPORTED_MEDIA_TYPE;
		}
		// 400
		else if (BeanUtils.isInstanceOf(e, HttpMediaTypeNotAcceptableException.class)
				|| BeanUtils.isInstanceOf(e, MissingServletRequestParameterException.class)
				|| BeanUtils.isInstanceOf(e, ServletRequestBindingException.class)
				|| BeanUtils.isInstanceOf(e, TypeMismatchException.class)
				|| BeanUtils.isInstanceOf(e, HttpMessageNotReadableException.class)
				|| BeanUtils.isInstanceOf(e, MethodArgumentNotValidException.class)
				|| BeanUtils.isInstanceOf(e, MissingServletRequestPartException.class)
				|| BeanUtils.isInstanceOf(e, BindException.class)) {
			return HttpStatus.BAD_REQUEST;
		}
		// 500
		else return HttpStatus.INTERNAL_SERVER_ERROR;
	}
}
