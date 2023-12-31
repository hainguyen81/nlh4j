/*
 * @(#)AbstractInterceptor.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.intercepter;

import java.text.MessageFormat;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StopWatch;

import org.nlh4j.exceptions.ApplicationRuntimeException;

/**
 * ServiceInterceptorAdapterのクラス。
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version 0.6
 *
 */
@Configuration
@Aspect
@Component
public class ServiceInterceptorAdapter extends AbstractHandlerInterceptorAdapter {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Service method logging interceptor.
	 * </p>
	 *
	 * @param joinPoint {@link ProceedingJoinPoint}
	 * @return result after invoking the specified {@link ProceedingJoinPoint}
     * @throws Throwable thrown if processing fail
	 */
	@Around("org.nlh4j.core.intercepter.AspectSystemArchitecture.inServiceLayer()")
//	@Around("execution(public * org.nlh4j.core.service.AbstractService+.*(..))")
	public Object inServiceLayer(ProceedingJoinPoint joinPoint) throws Throwable {
		final String methodName = joinPoint.getSignature().getName();
		final Class<?> targetClass = (joinPoint.getTarget() == null ? null : joinPoint.getTarget().getClass());
		final String className = (targetClass == null ? null : targetClass.getName());
		final String method = MessageFormat.format("{0}.{1}", className, methodName);
		logger.debug("[START[{}]] {}", ClassUtils.getUserClass(getClass()).getSimpleName(), method);
		final StopWatch sw = new StopWatch();
		sw.start();

		Object value = null;
		try {
			value = joinPoint.proceed();
		} catch (RuntimeException ex) {
			logger.error("[EXCEPTION[{}]] Exception while executing method {}: {}",
					ClassUtils.getUserClass(getClass()).getSimpleName(), method, ex.getMessage(), ex);
			throw new ApplicationRuntimeException(ex);
		} catch (Throwable t) {
			logger.error("[EXCEPTION[{}]] Exception while executing method {}: {}",
					ClassUtils.getUserClass(getClass()).getSimpleName(), method, t.getMessage(), t);
			throw new ApplicationRuntimeException(t);
		} finally {
			sw.stop();
			logger.debug(sw.prettyPrint());
			logger.debug("[END[{}]] {} [time] {} ms",
					ClassUtils.getUserClass(getClass()).getSimpleName(), method, sw.getTotalTimeMillis());
		}

		return value;
	}
}
