/*
 * @(#)AbstractInterceptor.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.intercepter;

import java.io.Serializable;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * AbstractHandlerInterceptorAdapterのクラス。
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version 0.6
 *
 */
@Aspect
public abstract class AbstractHandlerInterceptorAdapter extends HandlerInterceptorAdapter implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/** logger. */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/*
	 * (Non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public final void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		logger.debug("[START] POSTHANDLE [{}::{}]",
				request.getMethod(), request.getPathInfo());
		final StopWatch sw = new StopWatch();
		sw.start();
		try {
			this.post(request, response, handler, modelAndView);
			long startTime = NumberUtils.toLong(Objects.toString(request.getAttribute("startTime")), 0L);
			long endTime = System.currentTimeMillis();
			request.setAttribute("endTime", endTime);
			request.setAttribute("totalTime", endTime - startTime);
			logger.debug("Processing request [" + request.getRequestURI() + "] "
					+ "- ModelAndView: [" + (modelAndView != null ? modelAndView.getViewName() : "NO VIEW") + "] "
					+ "- Handler: [" + (handler != null ? handler.getClass().getName() : "NO HANDLER") + "] "
					+ "in [" + (endTime - startTime) + " ms]");
		}
		catch(Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			sw.stop();
			logger.debug(sw.prettyPrint());
			logger.debug("[END] POSTHANDLE [{}::{}] [time] {} ms",
					request.getMethod(), request.getPathInfo(), sw.getTotalTimeMillis());
		}
	}
	/**
	 * Intercept the execution of a handler.
	 * Called after {@link HandlerAdapter} actually invoked the handler,
	 * but before the {@link DispatcherServlet} renders the view.
	 * Can expose additional model objects to the view via the given {@link ModelAndView}.
	 * {@link DispatcherServlet} processes a handler in an execution chain, consisting of any number of interceptors,
	 * with the handler itself at the end.
	 * With this method, each interceptor can post-process an execution, getting applied
	 * in inverse order of the execution chain.
	 *
	 * @Note: special considerations apply for asynchronous request processing.
	 *
	 * TODO Override for handling manipulating the model attributes in it
	 *
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param handler handler (or {@link HandlerMethod}) that started async execution, for type and/or instance examination
	 * @param modelAndView the ModelAndView that the handler returned (can also be null)
	 */
	protected void post(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
		// TODO Override for handling manipulating the model attributes in it
	}

	/*
	 * (Non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.debug("[START] PREHANDLE [{}::{}]",
				request.getMethod(), request.getPathInfo());
		final StopWatch sw = new StopWatch();
		sw.start();
		try {
			request.setAttribute("startTime", System.currentTimeMillis());
			return this.pre(request, response, handler);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			sw.stop();
			logger.debug(sw.prettyPrint());
			logger.debug("[END] PREHANDLE [{}::{}] [time] {} ms",
					request.getMethod(), request.getPathInfo(), sw.getTotalTimeMillis());
		}
	}
	/**
	 * Intercept the execution of a handler.
	 * Called after {@link HandlerMapping} determined an appropriate handler object,
	 * but before {@link HandlerAdapter} invokes the handler.
	 * {@link DispatcherServlet} processes a handler in an execution chain, consisting of any number of interceptors,
	 * with the handler itself at the end.
	 * With this method, each interceptor can decide to abort the execution chain,
	 * typically sending a HTTP error or writing a custom response.
	 *
	 * @Note: special considerations apply for asynchronous request processing.
	 *
	 * TODO Override for handling it
	 *
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param handler chosen handler to execute, for type and/or instance evaluation
	 *
	 * @return true if the execution chain should proceed with the next interceptor or the handler itself.
	 * Else, DispatcherServlet assumes that this interceptor has already dealt with the response itself.
	 */
	protected boolean pre(HttpServletRequest request, HttpServletResponse response, Object handler) {
		// TODO Override for handling
		return true;
	}

	/*
	 * (Non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public final void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		logger.debug("[START] AFTERCOMPLETION [{}::{}]",
				request.getMethod(), request.getPathInfo());
		final StopWatch sw = new StopWatch();
		sw.start();
		try {
			if (ex != null) logger.error(ex.getMessage(), ex);
			this.after(request, response, handler, ex);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			sw.stop();
			logger.debug(sw.prettyPrint());
			logger.debug("[END] AFTERCOMPLETION [{}::{}] [time] {} ms",
					request.getMethod(), request.getPathInfo(), sw.getTotalTimeMillis());
		}
	}
	/**
	 * Callback after completion of request processing, that is, after rendering the view.
	 * Will be called on any outcome of handler execution, thus allows for proper resource cleanup.
	 *
	 * @Note: Will only be called if this interceptor's preHandle method has successfully completed and returned true!
	 * As with the post method, the method will be invoked on each interceptor in the chain in reverse order,
	 * so the first interceptor will be the last to be invoked.
	 *
	 * @Note: special considerations apply for asynchronous request processing.
	 *
	 * TODO Override for handling completion
	 *
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param handler handler (or {@link HandlerMethod}) that started async execution, for type and/or instance examination
	 * @param ex exception thrown on handler execution, if any
	 */
	protected void after(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		// TODO Override for handling completion
	}

	/*
	 * (Non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterConcurrentHandlingStarted(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public final void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.debug("[START] AFTERCONCURRENTHANDLINGSTARTED [{}::{}]",
				request.getMethod(), request.getPathInfo());
		final StopWatch sw = new StopWatch();
		sw.start();
		try {
			this.afterConcurrent(request, response, handler);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			sw.stop();
			logger.debug(sw.prettyPrint());
			logger.debug("[END] AFTERCONCURRENTHANDLINGSTARTED [{}::{}] [time] {} ms",
					request.getMethod(), request.getPathInfo(), sw.getTotalTimeMillis());
		}
	}
	/**
	 * Called instead of post and after, when the a handler is being executed concurrently.
	 * Implementations may use the provided request and response but should avoid modifying them in ways
	 * that would conflict with the concurrent execution of the handler.
	 * A typical use of this method would be to clean thread local variables.
	 *
	 * TODO Override for handling completion
	 *
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param handler handler (or HandlerMethod) that started async execution, for type and/or instance examination
	 */
	protected void afterConcurrent(HttpServletRequest request, HttpServletResponse response, Object handler) {
		// TODO Override for handling completion
	}
}
