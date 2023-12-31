/*
 * @(#)AspectSystemArchitecture.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.intercepter;

import java.io.Serializable;

import org.aspectj.lang.annotation.Pointcut;

import lombok.extern.slf4j.Slf4j;

/**
 * Defines the advice point-cut for AspectJ
 */
@Slf4j
public final class AspectSystemArchitecture implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * A join point is in the web layer if the method is defined in a type in the
	 * com.xyz.someapp.web package or any sub-package under that.
	 */
	@Pointcut("execution(public * org.nlh4j.core.controller.AbstractController+.*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void inControllerLayer() {
		log.debug("in [CONTROLLER_LAYER - Pointcut [execution(public * org.nlh4j.core.controller.AbstractController+.*(..))]]");
	}

	/**
	 * A join point is in the service layer if the method is defined in a type in
	 * the com.xyz.someapp.service package or any sub-package under that.
	 */
	@Pointcut("execution(public * org.nlh4j.core.service.AbstractService+.*(..))")
	public void inServiceLayer() {
		log.debug("in [SERVICE_LAYER - Pointcut [execution(public * org.nlh4j.core.service.AbstractService+.*(..))]]");
	}

	/**
	 * A join point is in the data access layer if the method is defined in a type
	 * in the com.xyz.someapp.dao package or any sub-package under that.
	 */
	@Pointcut("execution(public * *(..))")
	public void inMethodLayer() {
		log.debug("in [METHOD_LAYER - Pointcut [execution(public * *(..))]]");
	}

	/**
	 * A data access operation is the execution of any method defined on a dao
	 * interface. This definition assumes that interfaces are placed in the "dao"
	 * package, and that implementation types are in sub-packages.
	 */
	@Pointcut("execution(public * org.seasar.doma.internal.jdbc.dao.AbstractDao+.*(..))")
	public void inDomaDaoLayer() {
		log.debug("in [DOMA_LAYER - Pointcut [execution(public * org.seasar.doma.internal.jdbc.dao.AbstractDao+.*(..))]]");
	}
}
