/*
 * @(#)ContextLoaderListener.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.servlet;

import java.io.Serializable;

import javax.servlet.ServletContext;

import org.springframework.util.ClassUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import org.nlh4j.util.BeanUtils;

/**
 * An extended class of {@link org.springframework.web.context.ContextLoaderListener} for SecurityException by
 * maven jarsigner and Spring AOP:<br>
 * FIXME This class could not apply final because jarsigner and Spring CGLIB is conflict
 * by error (this is a error of Spring AOP and maven jarsigner plugin):
 * Could not generate CGLIB subclass... Common causes of this problem include
 * using a final class or a non-visible class; nested exception is java.lang.IllegalArgumentException:
 * Cannot subclass final class...<br>
 * See http://dimafeng.com/2015/08/16/cglib/
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class ContextLoaderListener
		extends org.springframework.web.context.ContextLoaderListener
		implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;

	/* (Non-Javadoc)
	 * @see org.springframework.web.context.ContextLoader#createWebApplicationContext(javax.servlet.ServletContext)
	 */
	@Override
	protected WebApplicationContext createWebApplicationContext(ServletContext sc) {
		WebApplicationContext wac = super.createWebApplicationContext(sc);
		ClassUtils.overrideThreadContextClassLoader(new UnsecureClassLoader());
		BeanUtils.safeType(wac, ConfigurableWebApplicationContext.class).refresh();
		return wac;
	}
}
