/*
 * @(#)MethodInterceptorAdapter.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.intercepter;

import java.text.MessageFormat;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.ModelAndView;

import org.nlh4j.core.controller.AbstractController;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.StringUtils;

/**
 * MethodInterceptorのクラス。
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version 0.6
 *
 */
@Aspect
@Component
public abstract class ControllerInterceptorAdapter extends AbstractHandlerInterceptorAdapter {

	private static final long serialVersionUID = 1L;

	/**
	 * controller methods point-cut
	 *
	 * @param joinPoint {@link ProceedingJoinPoint}
	 * @return result after invoking the specified {@link ProceedingJoinPoint}
	 * @throws Throwable thrown if processing fail
	 */
	@Around("execution(public * org.nlh4j.core.controller.AbstractController+.*(..))")
	public final Object inWebLayer(ProceedingJoinPoint joinPoint) throws Throwable {
		final String methodName = joinPoint.getSignature().getName();
		final Class<?> targetClass = (joinPoint.getTarget() == null ? null : joinPoint.getTarget().getClass());
		final String className = (targetClass == null ? null : targetClass.getName());
		final String method = MessageFormat.format("{0}.{1}", className, methodName);
		logger.debug("[START] {}", method);
		final StopWatch sw = new StopWatch();
		sw.start();

		Object value = null;
		Object[] medArgs = null;
		try {
			// checks before invoking
			boolean supported = BeanUtils.isInstanceOf(targetClass, AbstractController.class);
			boolean allowed = !supported;
			if (supported) {
				medArgs = joinPoint.getArgs();
				allowed = this.checkInvoke(targetClass, methodName, medArgs);
			}

			// if allowing to invoke method
			if (allowed) {
				// check forward URL
				String fwUrl = this.checkForward(targetClass, methodName, medArgs);
				if (StringUtils.hasText(fwUrl)) {
					fwUrl = fwUrl.replace("\\", "/").replace("//", "/");
					if (fwUrl.trim().startsWith("/")) fwUrl = fwUrl.substring(1);
				}

				// continue controller handling method
				if (!StringUtils.hasText(fwUrl)) {
					value = joinPoint.proceed();
				} else {
					// create new view with forward URL
					logger.debug("-------> Found forward URL: {}", fwUrl);
					value = new ModelAndView("forward:/" + fwUrl);
				}
			}
			else if (supported) {
				logger.warn("[ERRPERM] Not enough permission to invoking {}", method);
				throw new AccessDeniedException("[ERRPERM] Not enough permission to invoking " + method);
			}

			// if supporting for this target class;
			// then should execute after invoking
			if (supported && allowed) {
				this.afterInvoke(targetClass, methodName, medArgs, value);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (BeanUtils.isInstanceOf(e, AccessDeniedException.class)) {
				throw new ApplicationRuntimeException(403, e);
			}
			throw e;
		} finally {
			sw.stop();
			logger.debug(sw.prettyPrint());
			logger.debug("[END] {} [time] {} ms", method, sw.getTotalTimeMillis());
		}
		return value;
	}

	/**
	 * Check conditions to allow invoking method
	 * TODO Override for checking or default is true
	 *
	 * @param targetClass the target class invocation
	 * @param method the method name
	 * @param medArgs the method arguments
	 *
	 * @return true for allowing
	 */
	protected boolean checkInvoke(Class<?> targetClass, String method, Object[] medArgs) {
		return true;
	}
	/**
	 * Require forward URL
	 * TODO Override for requiring forward URL
	 *
	 * @param targetClass the target class invocation
	 * @param method the method name
	 * @param medArgs the method arguments
	 *
	 * @return valid forward URL or NULL/empty to continue controller handling method
	 */
	protected String checkForward(Class<?> targetClass, String method, Object[] medArgs) {
		return null;
	}
	/**
	 * Do something after invoking method
	 * TODO Override for doing something
	 *
	 * @param targetClass the target class invocation
	 * @param method the method name
	 * @param medArgs the method arguments
	 * @param value the result of method invoking
	 */
	protected void afterInvoke(Class<?> targetClass, String method, Object[] medArgs, Object value) {
		// do nothing
	}
}
