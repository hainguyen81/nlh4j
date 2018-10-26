/*
 * @(#)AbstractInterceptor.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.intercepter;

import java.text.MessageFormat;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.util.StopWatch;

import org.nlh4j.exceptions.ApplicationRuntimeException;

/**
 * ServiceInterceptorAdapterのクラス。
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version 0.6
 *
 */
@Aspect
public class ServiceInterceptorAdapter extends AbstractHandlerInterceptorAdapter {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	//	/** transaction manager. */
	//	@Inject
	//	private PlatformTransactionManager transactionManager;

	/**
	 * <p>
	 * Service method logging interceptor.
	 * </p>
	 *
	 * @param joinPoint {@link ProceedingJoinPoint}
	 * @return result after invoking the specified {@link ProceedingJoinPoint}
     * @throws Throwable thrown if processing fail
	 */
	@Around("execution(public * org.nlh4j.core.service.AbstractService+.*(..))")
	public Object inServiceLayer(ProceedingJoinPoint joinPoint) throws Throwable {
		final String method = MessageFormat.format(
				"{0}.{1}",
				joinPoint.getTarget().getClass().getName(),
				joinPoint.getSignature().getName());
		logger.debug("[START] {}", method);

		// Using transactional of Spring for transaction management
		//		DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
		//		definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		//		TransactionStatus transactionStatus = transactionManager.getTransaction(definition);

		final StopWatch sw = new StopWatch();
		sw.start();
		Object value = null;
		try {
			value = joinPoint.proceed();
			//transactionManager.commit(transactionStatus);
			//logger.debug("do commit");
		} catch (RuntimeException ex) {
			//transactionManager.rollback(transactionStatus);
			//logger.debug("do rollback");
			logger.error(ex.getMessage(), ex);
			throw new ApplicationRuntimeException(ex);
		} catch (Throwable t) {
			//transactionManager.rollback(transactionStatus);
			//logger.debug("do rollback");
			logger.error(t.getMessage(), t);
			throw new ApplicationRuntimeException(t);
		} finally {
			sw.stop();
			logger.debug(sw.prettyPrint());
			logger.debug("[END] {} [time] {} ms", method, sw.getTotalTimeMillis());
		}

		return value;
	}
}
