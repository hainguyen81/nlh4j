/*
 * @(#)ClassnameFactoryBean.java 1.0 Jan 11, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.factories;

import java.util.List;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import lombok.Getter;
import lombok.Setter;
import org.nlh4j.util.BeanUtils;

/**
 * An extended class of {@link AbstractFactoryBean} for creating bean with class name string
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class ClassnameFactoryBean<T> extends AbstractFactoryBean<T> {

	/** bean class name */
	@Getter
	@Setter
	private Class<T> targetClass;

	@Getter
	@Setter
	private List<Object> arguments;

	/* (Non-Javadoc)
	 * @see org.springframework.beans.factory.config.AbstractFactoryBean#getObjectType()
	 */
	@Override
	public Class<T> getObjectType() {
		return this.getTargetClass();
	}

	/* (Non-Javadoc)
	 * @see org.springframework.beans.factory.config.AbstractFactoryBean#createInstance()
	 */
	@Override
	protected T createInstance() throws Exception {
		return BeanUtils.safeType(
				BeanUtils.newInstance(this.getTargetClass(), this.getArguments()),
				this.getTargetClass());
	}

}
