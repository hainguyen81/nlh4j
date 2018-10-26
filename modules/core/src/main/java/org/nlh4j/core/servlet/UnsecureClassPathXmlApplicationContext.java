/*
 * @(#)UnsecureClassPathXmlApplicationContext.java 1.0 Mar 5, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.servlet;

import java.io.Serializable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.web.context.ConfigurableWebApplicationContext;

/**
 * An extended class of {@link ClassPathXmlApplicationContext} for SecurityException by
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
public class UnsecureClassPathXmlApplicationContext
		extends ClassPathXmlApplicationContext
		implements ConfigurableWebApplicationContext, Serializable {

	/** */
	private static final long serialVersionUID = 1L;
	/** {@link Resource} configuration */
	private Resource[] configResources;
	/** {@link ServletContext} */
	private ServletContext servletContext;
	/** {@link ServletConfig} */
	private ServletConfig servletConfig;
	/** context namespace */
	private String namespace;

	/**
	 * Create a new ClassPathXmlApplicationContext for bean-style configuration.
	 * @see #setConfigLocation
	 * @see #setConfigLocations
	 * @see #afterPropertiesSet()
	 */
	public UnsecureClassPathXmlApplicationContext() {
		super.setClassLoader(new UnsecureClassLoader());
	}

	/**
	 * Create a new ClassPathXmlApplicationContext for bean-style configuration.
	 * @param parent the parent context
	 * @see #setConfigLocation
	 * @see #setConfigLocations
	 * @see #afterPropertiesSet()
	 */
	public UnsecureClassPathXmlApplicationContext(ApplicationContext parent) {
		super(parent);
		super.setClassLoader(new UnsecureClassLoader());
	}

	/**
	 * Create a new ClassPathXmlApplicationContext, loading the definitions
	 * from the given XML file and automatically refreshing the context.
	 * @param configLocation resource location
	 * @throws BeansException if context creation failed
	 */
	public UnsecureClassPathXmlApplicationContext(String configLocation) throws BeansException {
		this(new String[] {configLocation}, false, null);
		super.setClassLoader(new UnsecureClassLoader());
		super.refresh();
	}

	/**
	 * Create a new ClassPathXmlApplicationContext, loading the definitions
	 * from the given XML files and automatically refreshing the context.
	 * @param configLocations array of resource locations
	 * @throws BeansException if context creation failed
	 */
	public UnsecureClassPathXmlApplicationContext(String... configLocations) throws BeansException {
		this(configLocations, false, null);
		super.setClassLoader(new UnsecureClassLoader());
		super.refresh();
	}

	/**
	 * Create a new ClassPathXmlApplicationContext with the given parent,
	 * loading the definitions from the given XML files and automatically
	 * refreshing the context.
	 * @param configLocations array of resource locations
	 * @param parent the parent context
	 * @throws BeansException if context creation failed
	 */
	public UnsecureClassPathXmlApplicationContext(String[] configLocations, ApplicationContext parent) throws BeansException {
		this(configLocations, false, parent);
		super.setClassLoader(new UnsecureClassLoader());
		super.refresh();
	}

	/**
	 * Create a new ClassPathXmlApplicationContext, loading the definitions
	 * from the given XML files.
	 * @param configLocations array of resource locations
	 * @param refresh whether to automatically refresh the context,
	 * loading all bean definitions and creating all singletons.
	 * Alternatively, call refresh manually after further configuring the context.
	 * @throws BeansException if context creation failed
	 * @see #refresh()
	 */
	public UnsecureClassPathXmlApplicationContext(String[] configLocations, boolean refresh) throws BeansException {
		this(configLocations, false, null);
		super.setClassLoader(new UnsecureClassLoader());
		if (refresh) {
			super.refresh();
		}
	}

	/**
	 * Create a new ClassPathXmlApplicationContext with the given parent,
	 * loading the definitions from the given XML files.
	 * @param configLocations array of resource locations
	 * @param refresh whether to automatically refresh the context,
	 * loading all bean definitions and creating all singletons.
	 * Alternatively, call refresh manually after further configuring the context.
	 * @param parent the parent context
	 * @throws BeansException if context creation failed
	 * @see #refresh()
	 */
	public UnsecureClassPathXmlApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent)
			throws BeansException {
		super(configLocations, false, parent);
		setConfigLocations(configLocations);
		super.setClassLoader(new UnsecureClassLoader());
		if (refresh) {
			refresh();
		}
	}

	/**
	 * Create a new ClassPathXmlApplicationContext, loading the definitions
	 * from the given XML file and automatically refreshing the context.
	 * <p>This is a convenience method to load class path resources relative to a
	 * given Class. For full flexibility, consider using a GenericApplicationContext
	 * with an XmlBeanDefinitionReader and a ClassPathResource argument.
	 * @param path relative (or absolute) path within the class path
	 * @param clazz the class to load resources with (basis for the given paths)
	 * @throws BeansException if context creation failed
	 * @see org.springframework.core.io.ClassPathResource#ClassPathResource(String, Class)
	 * @see org.springframework.context.support.GenericApplicationContext
	 * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader
	 */
	public UnsecureClassPathXmlApplicationContext(String path, Class<?> clazz) throws BeansException {
		this(new String[] {path}, clazz);
	}

	/**
	 * Create a new ClassPathXmlApplicationContext, loading the definitions
	 * from the given XML files and automatically refreshing the context.
	 * @param paths array of relative (or absolute) paths within the class path
	 * @param clazz the class to load resources with (basis for the given paths)
	 * @throws BeansException if context creation failed
	 * @see org.springframework.core.io.ClassPathResource#ClassPathResource(String, Class)
	 * @see org.springframework.context.support.GenericApplicationContext
	 * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader
	 */
	public UnsecureClassPathXmlApplicationContext(String[] paths, Class<?> clazz) throws BeansException {
		this(paths, clazz, null);
	}

	/**
	 * Create a new ClassPathXmlApplicationContext with the given parent,
	 * loading the definitions from the given XML files and automatically
	 * refreshing the context.
	 * @param paths array of relative (or absolute) paths within the class path
	 * @param clazz the class to load resources with (basis for the given paths)
	 * @param parent the parent context
	 * @throws BeansException if context creation failed
	 * @see org.springframework.core.io.ClassPathResource#ClassPathResource(String, Class)
	 * @see org.springframework.context.support.GenericApplicationContext
	 * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader
	 */
	public UnsecureClassPathXmlApplicationContext(String[] paths, Class<?> clazz, ApplicationContext parent)
			throws BeansException {
		super(paths);
		Assert.notNull(paths, "Path array must not be null");
		Assert.notNull(clazz, "Class argument must not be null");

		super.setClassLoader(new UnsecureClassLoader());
		this.configResources = new Resource[paths.length];
		for (int i = 0; i < paths.length; i++) {
			this.configResources[i] = new ClassPathResource(paths[i], clazz);
		}
		refresh();
	}

	/* (Non-Javadoc)
	 * @see org.springframework.context.support.ClassPathXmlApplicationContext#getConfigResources()
	 */
	@Override
	protected Resource[] getConfigResources() {
		return this.configResources;
	}

	/* (Non-Javadoc)
	 * @see org.springframework.web.context.WebApplicationContext#getServletContext()
	 */
	@Override
	public ServletContext getServletContext() {
		return this.servletContext;
	}

	/* (Non-Javadoc)
	 * @see org.springframework.web.context.ConfigurableWebApplicationContext#setServletContext(javax.servlet.ServletContext)
	 */
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	/* (Non-Javadoc)
	 * @see org.springframework.web.context.ConfigurableWebApplicationContext#setServletConfig(javax.servlet.ServletConfig)
	 */
	@Override
	public void setServletConfig(ServletConfig servletConfig) {
		this.servletConfig = servletConfig;
	}

	/* (Non-Javadoc)
	 * @see org.springframework.web.context.ConfigurableWebApplicationContext#getServletConfig()
	 */
	@Override
	public ServletConfig getServletConfig() {
		return this.servletConfig;
	}

	/* (Non-Javadoc)
	 * @see org.springframework.web.context.ConfigurableWebApplicationContext#setNamespace(java.lang.String)
	 */
	@Override
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	/* (Non-Javadoc)
	 * @see org.springframework.web.context.ConfigurableWebApplicationContext#getNamespace()
	 */
	@Override
	public String getNamespace() {
		return this.namespace;
	}

	/* (Non-Javadoc)
	 * @see org.springframework.context.support.AbstractRefreshableConfigApplicationContext#getConfigLocations()
	 */
	@Override
	public String[] getConfigLocations() {
		return super.getConfigLocations();
	}
}
