/*
 * @(#)SpringContextHelper.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.servlet;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.ExceptionUtils;
import org.nlh4j.util.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import com.machinezoo.noexception.Exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * WEB SPRING context helper.
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Singleton
public final class SpringContextHelper implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * WEBコンテクスト
	 */
	@Getter
	@Setter
	private transient ServletConfig servletConfig;
	@Getter
	@Setter
	private transient ServletContext servletContext;
	@Setter
	private transient ApplicationContext applicationContext;

	/** {@link ApplicationContextProvider} */
	@Inject
	private ApplicationContextProvider contextProvider;

    /**
     * Get the application context<br>
     * If context from constructor is valid, it will be returned;
     * else context will be awared
     *
     * @return the application context
     */
    public ApplicationContext getContext() {
    	// try detect context from config
	    if (this.applicationContext == null && this.servletConfig != null) {
            this.applicationContext =
            		ApplicationContextProvider.getApplicationContext(this.servletConfig);
        }
	    // try detect context from servlet
	    if (this.applicationContext == null && this.servletContext != null) {
	    	this.applicationContext =
	    			ApplicationContextProvider.getApplicationContext(this.servletContext);
        }
	    // try detect context from aware
	    if (this.applicationContext == null && this.contextProvider != null) {
	    	this.applicationContext = this.contextProvider.getApplicationContext();
        }
	    if (this.applicationContext == null) return null;
	    synchronized (this.applicationContext) {
	        return this.applicationContext;
        }
    }

	/**
	 * Initialize an instance of {@link SpringContextHelper}
	 */
	public SpringContextHelper() {}
	/**
	 * Initialize a new instance of {@link SpringContextHelper}
	 *
	 * @param appContext application context
	 */
	public SpringContextHelper(ApplicationContext appContext) {
		this.setApplicationContext(appContext);
	}
	/**
     * Initialize a new instance of {@link SpringContextHelper}
     *
     * @param servletConfig servlet configuration
     */
    public SpringContextHelper(ServletConfig servletConfig) {
        this.setServletConfig(servletConfig);
    }
    /**
     * Initialize a new instance of {@link SpringContextHelper}
     *
     * @param servletContext servlet context
     */
    public SpringContextHelper(ServletContext servletContext) {
        this.setServletContext(servletContext);
    }

    /***************************************************
     * BEANS SUPPORT
     ***************************************************/

    /**
     * Gets bean object from servlet context
     *
     * @param applicationContext context
     * @param beanRef the bean name/id
     *
     * @return the bean object from servlet context
     */
    public static Object findBean(final ApplicationContext applicationContext, final String beanRef) {
        Object bean = null;
        if (applicationContext != null) {
        	ApplicationContext ctx = applicationContext;
        	while(ctx != null) {
	            try { bean = ctx.getBean(beanRef); }
	            catch (BeansException e) {
	            	log.warn(e.getMessage());
	            }
            	if (bean != null) break;
            	else ctx = ctx.getParent();
        	}
        }
        return bean;
    }
    /**
     * Gets bean object from servlet context
     *
     * @param beanRef the bean name/id
     *
     * @return the bean object from servlet context
     */
    public static Object findBean(final String beanRef) {
        return findBean(ApplicationContextProvider.getAwareApplicationContext(), beanRef);
    }
    /**
     * Gets bean object from servlet context
     *
     * @param beanRef the bean name/id
     *
     * @return the bean object from servlet context
     */
    public Object searchBean(final String beanRef) {
        return findBean(this.getContext(), beanRef);
    }

    /**
     * Get bean object from servlet context
     *
     * @param <T> bean type
     * @param applicationContext context
     * @param beanRef bean identity reference
     * @param requiredType the bean class
     *
     * @return the bean object from servlet context
     */
    public static <T> T findBean(final ApplicationContext applicationContext, final String beanRef, final Class<T> requiredType) {
    	T bean = null;
        if (applicationContext != null) {
        	ApplicationContext ctx = applicationContext;
        	while(ctx != null) {
	            try { bean = ctx.getBean(beanRef, requiredType); }
	            catch (BeansException e) {
	            	log.warn(e.getMessage());
	            }
            	if (bean != null) break;
            	else ctx = ctx.getParent();
        	}
        }
        return bean;
    }
    /**
     * Get bean object from servlet context
     *
     * @param <T> bean type
     * @param beanRef bean identity reference
     * @param requiredType the bean class
     *
     * @return the bean object from servlet context
     */
    public static <T> T findBean(final String beanRef, final Class<T> requiredType) {
        return findBean(ApplicationContextProvider.getAwareApplicationContext(), beanRef, requiredType);
    }
    /**
     * Get bean object from servlet context
     *
     * @param <T> bean type
     * @param beanRef bean identity reference
     * @param requiredType the bean class
     *
     * @return the bean object from servlet context
     */
    public <T> T searchBean(final String beanRef, final Class<T> requiredType) {
        return findBean(this.getContext(), beanRef, requiredType);
    }

    /**
     * Get bean object from servlet context
     *
     * @param applicationContext context
     * @param beanRef bean identity reference
     * @param args the bean arguments
     *
     * @return the bean object from servlet context
     */
    public static Object findBean(final ApplicationContext applicationContext, final String beanRef, final Object...args) {
    	Object bean = null;
        if (applicationContext != null) {
        	ApplicationContext ctx = applicationContext;
        	while(ctx != null) {
	            try { bean = ctx.getBean(beanRef, args); }
	            catch (BeansException e) {
	            	log.warn(e.getMessage());
	            }
            	if (bean != null) break;
            	else ctx = ctx.getParent();
        	}
        }
        return bean;
    }
    /**
     * Get bean object from servlet context
     *
     * @param beanRef bean identity reference
     * @param args the bean arguments
     *
     * @return the bean object from servlet context
     */
    public static Object findBean(final String beanRef, final Object...args) {
        return findBean(ApplicationContextProvider.getAwareApplicationContext(), beanRef, args);
    }
    /**
     * Get bean object from servlet context
     *
     * @param beanRef bean identity reference
     * @param args the bean arguments
     *
     * @return the bean object from servlet context
     */
    public Object searchBean(final String beanRef, final Object...args) {
        return findBean(this.getContext(), beanRef, args);
    }
    /**
     * Gets bean object from servlet context
     *
     * @param <T> bean type
     * @param beanClass the bean class
     *
     * @return the bean object from servlet context
     */
    public static <T> T findBean(final Class<T> beanClass) {
    	return findBean(ApplicationContextProvider.getAwareApplicationContext(), beanClass, Boolean.TRUE);
    }
    /**
     * Gets bean objects list from servlet context
     *
     * @param <T> bean type
     * @param beanClass the bean class
     *
     * @return the bean objects list from servlet context
     */
    public static <T> List<T> findBeans(final Class<T> beanClass) {
    	return findBeans(ApplicationContextProvider.getAwareApplicationContext(), beanClass, Boolean.TRUE);
    }
    /**
     * Gets bean object from servlet context
     *
     * @param <T> bean type
     * @param beanClass the bean class
     *
     * @return the bean object from servlet context
     */
    public <T> T searchBean(final Class<T> beanClass) {
        return findBean(this.getContext(), beanClass, Boolean.TRUE);
    }
    /**
     * Gets bean objects list from servlet context
     *
     * @param <T> bean type
     * @param beanClass the bean class
     *
     * @return the bean objects list from servlet context
     */
    public <T> List<T> searchBeans(final Class<T> beanClass) {
        return findBeans(this.getContext(), beanClass, Boolean.TRUE);
    }
    /**
     * Gets bean object from servlet context
     *
     * @param <T> bean type
     * @param applicationContext context
     * @param beanClass the bean class
     * @param inherited
     * 		specifies the finder whether search bean base on the specified bean class;
     * 		if not found; it will search with the specified bean class as parent class
     *
     * @return the bean object from servlet context
     */
	public static <T> T findBean(final ApplicationContext applicationContext, final Class<T> beanClass, boolean inherited) {
		Map<String, ?> beans = null;
    	T bean = null;
    	if (applicationContext != null && beanClass != null) {
    		ApplicationContext ctx = applicationContext;
    		while(CollectionUtils.isEmpty(beans) && ctx != null) {
		    	try { beans = ctx.getBeansOfType(beanClass, Boolean.TRUE, Boolean.FALSE); }
		    	catch (BeansException e) {
		    		log.warn(e.getMessage());
		    		beans = null;
		    	}
		    	if (!CollectionUtils.isEmpty(beans)) break;
		    	else ctx = ctx.getParent();
    		}
	    	bean = Optional.ofNullable(beans).orElseGet(LinkedHashMap::new)
	    	        .entrySet().parallelStream()
		    		.filter(e -> Objects.nonNull(e.getValue())
		    				&& (beanClass.equals(e.getValue().getClass())
		    						|| (inherited && BeanUtils.isInstanceOf(e.getValue().getClass(), beanClass))))
		    		.map(e -> BeanUtils.safeType(e.getValue(), beanClass))
		    		.filter(Objects::nonNull).findFirst().orElse(null);
    	}
    	// if not found the bean with specified class but using inherited flag;
    	// then returning the found first bean
    	return bean;
	}
    /**
     * Gets bean object from servlet context
     *
     * @param <T> bean type
     * @param beanClass the bean class
     * @param inherited
     * 		specifies the finder whether search bean base on the specified bean class;
     * 		if not found; it will search with the specified bean class as parent class
     *
     * @return the bean object from servlet context
     */
	public static <T> T findBean(final Class<T> beanClass, boolean inherited) {
    	return findBean(ApplicationContextProvider.getAwareApplicationContext(), beanClass, inherited);
    }

	/**
     * Gets bean objects list from servlet context
     *
     * @param <T> bean type
     * @param applicationContext context
     * @param beanClass the bean class
     * @param inherited
     * 		specifies the finder whether search bean base on the specified bean class;
     * 		if not found; it will search with the specified bean class as parent class
     *
     * @return the bean objects list from servlet context
     */
	public static <T> List<T> findBeans(final ApplicationContext applicationContext, final Class<T> beanClass, boolean inherited) {
		Map<String, ?> beans = null;
    	List<T> beansLst = new LinkedList<>();
    	if (applicationContext != null && beanClass != null) {
    		ApplicationContext ctx = applicationContext;
    		while(CollectionUtils.isEmpty(beans) && ctx != null) {
		    	try { beans = ctx.getBeansOfType(beanClass, Boolean.TRUE, Boolean.FALSE); }
		    	catch (BeansException e) {
		    		ExceptionUtils.traceException(log, e);
		    		beans = null;
		    	}
		    	if (!CollectionUtils.isEmpty(beans)) break;
		    	else ctx = ctx.getParent();
    		}
    		beansLst.addAll(
    				Optional.ofNullable(beans).orElseGet(LinkedHashMap::new)
    				.entrySet().parallelStream()
		    		.filter(e -> Objects.nonNull(e.getValue())
		    				&& (beanClass.equals(e.getValue().getClass())
		    						|| (inherited && BeanUtils.isInstanceOf(e.getValue().getClass(), beanClass))))
		    		.map(e -> BeanUtils.safeType(e.getValue(), beanClass))
		    		.filter(Objects::nonNull).collect(Collectors.toCollection(LinkedList::new)));
    	}
    	// if not found the bean with specified class but using inherited flag;
    	// then returning the found first bean
    	return beansLst;
	}
    /**
     * Gets bean objects list from servlet context
     *
     * @param <T> bean type
     * @param beanClass the bean class
     * @param inherited
     * 		specifies the finder whether search bean base on the specified bean class;
     * 		if not found; it will search with the specified bean class as parent class
     *
     * @return the bean objects list from servlet context
     */
	public static <T> List<T> findBeans(final Class<T> beanClass, boolean inherited) {
    	return findBeans(ApplicationContextProvider.getAwareApplicationContext(), beanClass, inherited);
    }
	/**
     * Gets bean object from servlet context
     *
     * @param <T> bean type
     * @param beanClass the bean class
     * @param inherited
     *      specifies the finder whether search bean base on the specified bean class;
     *      if not found; it will search with the specified bean class as parent class
     *
     * @return the bean object from servlet context
     */
    public <T> T searchBean(final Class<T> beanClass, boolean inherited) {
        return findBean(this.getContext(), beanClass, inherited);
    }
	/**
     * Gets bean objects list from servlet context
     *
     * @param <T> bean type
     * @param beanClass the bean class
     * @param inherited
     *      specifies the finder whether search bean base on the specified bean class;
     *      if not found; it will search with the specified bean class as parent class
     *
     * @return the bean objects list from servlet context
     */
    public <T> List<T> searchBeans(final Class<T> beanClass, boolean inherited) {
        return findBeans(this.getContext(), beanClass, inherited);
    }

    /***************************************************
     * RESOURCE SUPPORT
     ***************************************************/

    /**
     * Get the information of the specified {@link Resource} such as URL, URI, or file path.<br>
     * If failed, it will return {@link Resource#getDescription()} same as {@link Object#toString()}
     * 
     * @param resource {@link Resource}
     * 
     * @return the information of the specified {@link Resource}
     */
    public static String getResourceDescription(Resource resource) {
    	if (resource == null) {
    		return null;
    	}

    	String resPath = null;
		// URL
		if (!StringUtils.hasText(resPath)) {
			try { resPath = resource.getURL().getPath(); }
			catch (Exception e) {
			    // do nothing
			}
		}
		// URI
		if (!StringUtils.hasText(resPath)) {
			try { resPath = resource.getURI().getPath(); }
			catch (Exception e) {
                // do nothing
		    }
		}
		// File
		if (!StringUtils.hasText(resPath)) {
			try { resPath = resource.getFile().getPath(); }
			catch (Exception e) {
                // do nothing
		    }
		}

		// return information
		return Optional.ofNullable(resPath).filter(StringUtils::hasText).orElseGet(() -> resource.getDescription());
    }

    /**
     * Log the specified resource for debug
     *
     * @param originalPath the original resource pattern
     * @param resource to debug
     */
    private static void traceResource(String originalPath, Resource resource) {
    	if (!log.isTraceEnabled() || resource == null) {
    	    return;
    	}

    	// tracing
		log.trace("Resolved [" + originalPath + "] to [" + getResourceDescription(resource) + "]");
    }

    /**
     * Get the resource as {@link Resource} of the specified resource path<br>
     * by below strategies:<br>
     * - specified {@link ApplicationContext}<br>
     * - specified {@link ApplicationContext#getClassLoader()} (if not found by upper algorithms)<br>
     * - physical {@link File} by specified resource path (if not found by upper algorithms)<br>
     * - current {@link Thread#getContextClassLoader()} (if not found by upper algorithms)<br>
     * - this class {@link SpringContextHelper} loader (if not found by upper algorithms)
     *
     * @param path resource path
     *
     * @return {@link Resource} map by pattern and resources list or null if failed
     */
    public Map<String, List<Resource>> searchResources(String path) {
        return findResources(this.getContext(), path);
    }

    /**
     * Get the resource as {@link Resource} array of the specified resource path<br>
     * by below strategies:<br>
     * - specified {@link ApplicationContext} by {@link ApplicationContextProvider#getAwareApplicationContext()}<br>
     * - specified {@link ApplicationContext#getClassLoader()} by {@link ApplicationContextProvider#getAwareApplicationContext()}<br>
     *   (if not found by upper algorithms)<br>
     * - physical {@link File} by specified resource path (if not found by upper algorithms)<br>
     * - current {@link Thread#getContextClassLoader()} (if not found by upper algorithms)<br>
     * - this class {@link SpringContextHelper} loader (if not found by upper algorithms)
     *
     * @param path resource path
     *
     * @return {@link Resource} map by pattern and resources list or null if failed
     */
    public static Map<String, List<Resource>> findResources(String path) {
        return findResources(ApplicationContextProvider.getAwareApplicationContext(), path);
    }
    /**
     * Get the resource as {@link Resource} array of the specified resource path<br>
     * by below strategies:<br>
     * - specified {@link ApplicationContext}<br>
     * - specified {@link ApplicationContext#getClassLoader()} (if not found by upper algorithms)<br>
     * - physical {@link File} by specified resource path (if not found by upper algorithms)<br>
     * - current {@link Thread#getContextClassLoader()} (if not found by upper algorithms)<br>
     * - this class {@link SpringContextHelper} loader (if not found by upper algorithms)
     *
     * @param applicationContext context
     * @param path resource path
     *
     * @return {@link Resource} map by pattern and resources list or null if failed
     */
    public static Map<String, List<Resource>> findResources(ApplicationContext applicationContext, String path) {
    	Map<String, List<Resource>> resources = new LinkedHashMap<>();
        if (applicationContext != null && StringUtils.hasText(path)) {
            // resolve all possible resource paths
        	resources.putAll(findContextResourcesRecursively(applicationContext, path));
        }

        // try detecting local file
        if (CollectionUtils.isEmpty(resources) && StringUtils.hasText(path)) {
            File f = new File(path);
            if (f.exists() && f.canRead()) {
                resources = new LinkedHashMap<>();
                resources.put(path, new LinkedList<>());
                resources.get(path).add(new FileSystemResource(path));
                traceResource(path, resources.get(path).get(0));
            }
        }

        // try using current thread class loader
        if (CollectionUtils.isEmpty(resources) && StringUtils.hasText(path)) {
        	// resolve all possible resource paths
        	resources.putAll(findClassLoaderResourcesRecursively(Thread.currentThread().getContextClassLoader(), path));
        }

        // try using class loader
		if (CollectionUtils.isEmpty(resources) && StringUtils.hasText(path)) {
        	// resolve all possible resource paths
			resources.putAll(findClassLoaderResourcesRecursively(SpringContextHelper.class.getClassLoader(), path));
        }

        // debug
        if (CollectionUtils.isEmpty(resources) && log.isDebugEnabled()) {
        	log.warn("Not found resource path [" + path + "]");
        }
        return resources;
    }
    /**
     * Get the resource as {@link Resource} array of the specified resource path (recursively)<br>
     * by the specified {@link ClassLoader} and recursively finding on its parent {@link ClassLoader}
     *
     * @param loader {@link ClassLoader}
     * @param path resource path
     *
     * @return {@link Resource} map by pattern and resources list or null if failed
     */
	public static Map<String, List<Resource>> findClassLoaderResourcesRecursively(ClassLoader loader, String path) {
		Map<String, List<Resource>> resources = new LinkedHashMap<>();
		while (loader != null && StringUtils.hasText(path)) {
			try {
			    // find resources
			    Optional.ofNullable(findClassLoaderResources(loader, path))
                .filter(e -> !CollectionUtils.isEmpty(e.getValue()))
                .ifPresent(e -> resources.put(e.getKey(), e.getValue()));

				// continue with parent context if not found
				loader = loader.getParent();

			} catch (Exception e) {
				log.warn("Could not resolve resource [{}] by ClassLoader: {}", path, e.getMessage());
				ExceptionUtils.traceException(log, e);
				resources.clear();
			}
		}
		return resources;
	}
	/**
	 * Find the specified resource path from the specified {@link ClassLoader} (non-recursively)<br>
     * by the specified {@link ClassLoader}
	 * 
	 * @param loader {@link ClassLoader}
	 * @param path resource path
	 * 
	 * @return the pair of the specified resource path and a list of found {@link Resource}
	 */
    public static Entry<String, List<Resource>> findClassLoaderResources(ClassLoader loader, String path) {
        Entry<String, List<Resource>> resources = new SimpleEntry<>(path, new LinkedList<>());
        URL url = Optional.ofNullable(loader).map(cl -> cl.getResource(path)).orElse(null);
        Resource resource = Optional.ofNullable(url).map(UrlResource::new).orElse(null);
        if (resource != null && url != null) {
        	resources.getValue().add(resource);
        	traceResource(path, resource);
        }
        return resources;
    }
    /**
     * Get the resource as {@link Resource} array of the specified resource path (recursively)<br>
     * by current {@link Thread#getContextClassLoader()} (and recursively finding on its parent),<br>
     * if not found, then continue finding on this class {@link SpringContextHelper} loader<br>
     * (and recursively finding on its parent)
     *
     * @param path resource path
     *
     * @return {@link Resource} map by pattern and resources list or null if failed
     */
    public static Map<String, List<Resource>> findConcurrenceClassLoaderResources(String path) {
        Map<String, List<Resource>> resources = new LinkedHashMap<>();

        // try using current thread class loader
        if (CollectionUtils.isEmpty(resources) && StringUtils.hasText(path)) {
            // resolve all possible resource paths
            resources.putAll(findClassLoaderResourcesRecursively(Thread.currentThread().getContextClassLoader(), path));
        }

        // try using class loader
        if (CollectionUtils.isEmpty(resources) && StringUtils.hasText(path)) {
            // resolve all possible resource paths
            resources.putAll(findClassLoaderResourcesRecursively(SpringContextHelper.class.getClassLoader(), path));
        }

        return resources;
    }
    /**
     * Get the resource as {@link Resource} array of the specified resource path (recursively)<br>
     * by below strategies:<br>
     * - specified {@link ApplicationContext}<br>
     * - specified {@link ApplicationContext#getClassLoader()} (if not found by upper algorithms)
     *
     * @param path resource path
     *
     * @return {@link Resource} map by pattern and resources list or null if failed
     */
    public Map<String, List<Resource>> searchContextResourcesRecursively(String path) {
        return findContextResourcesRecursively(getContext(), path);
    }
    /**
     * Get the resource as {@link Resource} array of the specified resource path (recursively)<br>
     * by below strategies:<br>
     * - specified {@link ApplicationContext} by {@link ApplicationContextProvider#getAwareApplicationContext()}<br>
     * - specified {@link ApplicationContext#getClassLoader()} by {@link ApplicationContextProvider#getAwareApplicationContext()}<br>
     *   (if not found by upper algorithms)
     *
     * @param path resource path
     *
     * @return {@link Resource} map by pattern and resources list or null if failed
     */
    public static Map<String, List<Resource>> findContextResourcesRecursively(String path) {
        return findContextResourcesRecursively(ApplicationContextProvider.getAwareApplicationContext(), path);
    }
    /**
     * Get the resource as {@link Resource} array of the specified resource path (recursively)<br>
     * by below strategies:<br>
     * - specified {@link ApplicationContext}<br>
     * - specified {@link ApplicationContext#getClassLoader()} (if not found by upper algorithms)
     *
     * @param applicationContext context
     * @param path resource path
     *
     * @return {@link Resource} map by pattern and resources list or null if failed
     */
	public static Map<String, List<Resource>> findContextResourcesRecursively(ApplicationContext applicationContext, String path) {
		Map<String, List<Resource>> resources = new LinkedHashMap<>();
		Set<String> resourcePaths = StringUtils.resolveResourceNames(path);
		log.debug("Resolve location [{}] to solvable locations [{}]",
				path, org.apache.commons.lang3.StringUtils.join(path, ", "));
		if (applicationContext != null && !CollectionUtils.isEmpty(resourcePaths)) {
		    ApplicationContext ctx = applicationContext;
		    while(ctx != null) {
	        	final ApplicationContext tmpCtx = ctx;
	        	resources.putAll(
	        			resourcePaths.parallelStream().filter(StringUtils::hasText)
	        			.map(resourcePath -> findContextResources(tmpCtx, resourcePath))
	        			.filter(e -> !CollectionUtils.isEmpty(e.getValue()))
	        			.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (k1, k2) -> k1)));
	        	
	            // continue with parent context
	            ctx = ctx.getParent();
		    }
		}

		// try with classloader of application context
		if (applicationContext != null && CollectionUtils.isEmpty(resources)) {
			resources.putAll(findClassLoaderResourcesRecursively(applicationContext.getClassLoader(), path));
		}
		return resources;
	}

	/**
     * Find {@link Resource}(s) from the specified {@link ApplicationContext} and resource path (non-recursively)<br>
     * by below strategies:<br>
     * - specified {@link ApplicationContext}<br>
     * - specified {@link ApplicationContext#getClassLoader()} (if not found by upper algorithms)
     * 
     * @param resourcePath resource path
     * 
     * @return the {@link Entry} of the specified resource path and a list of {@link Resource}
     */
    public Entry<String, List<Resource>> searchContextResources(String resourcePath) {
        return findContextResources(getContext(), resourcePath);
    }
    /**
     * Find {@link Resource}(s) from the specified {@link ApplicationContext} and resource path (non-recursively)<br>
     * by below strategies:<br>
     * - specified {@link ApplicationContext} by {@link ApplicationContextProvider#getAwareApplicationContext()}<br>
     * - specified {@link ApplicationContext#getClassLoader()} by {@link ApplicationContextProvider#getAwareApplicationContext()}<br>
     *   (if not found by upper algorithms)
     * 
     * @param resourcePath resource path
     * 
     * @return the {@link Entry} of the specified resource path and a list of {@link Resource}
     */
    public static Entry<String, List<Resource>> findContextResources(String resourcePath) {
        return findContextResources(ApplicationContextProvider.getAwareApplicationContext(), resourcePath);
    }
	/**
	 * Find {@link Resource}(s) from the specified {@link ApplicationContext} and resource path (non-recursively)<br>
     * by below strategies:<br>
     * - specified {@link ApplicationContext}<br>
     * - specified {@link ApplicationContext#getClassLoader()} (if not found by upper algorithms)
	 * 
	 * @param ctx {@link ApplicationContext}
	 * @param resourcePath resource path
	 * 
	 * @return the {@link Entry} of the specified resource path and a list of {@link Resource}
	 */
    public static Entry<String, List<Resource>> findContextResources(ApplicationContext ctx, String resourcePath) {
        Entry<String, List<Resource>> resources = new SimpleEntry<>(resourcePath, new LinkedList<>());
        // find resources
        if (ctx != null) {
            try {
                resources.getValue().addAll(Arrays.asList(ctx.getResources(resourcePath)));
            } catch (Exception e) {
            	ExceptionUtils.traceException(log, e);
            }
        }
        
        // try to find by application context class loader
        if (ctx != null && ctx.getClassLoader() != null && CollectionUtils.isEmpty(resources.getValue())) {
            Optional.ofNullable(findClassLoaderResources(ctx.getClassLoader(), resourcePath))
            .filter(e -> !CollectionUtils.isEmpty(e.getValue()))
            .ifPresent(e -> resources.getValue().addAll(e.getValue()));
        }

        // tracing
        if (!CollectionUtils.isEmpty(resources.getValue())) {
        	if (log.isDebugEnabled()) {
        		log.debug("Resolved [{}] resources by path [{}]", resources.getValue().size(), resourcePath);
        	}
        	traceResource(resourcePath, resources.getValue().get(0));

        } else if (log.isDebugEnabled()) {
        	log.warn("Could not resolve resources by path [{}]", resourcePath);
        }
        return resources;
    }

    /**
     * Get the resource as {@link Resource} of the specified resource path<br>
     * by below strategies:<br>
     * - specified {@link ApplicationContext}<br>
     * - specified {@link ApplicationContext#getClassLoader()} (if not found by upper algorithms)<br>
     * - physical {@link File} by specified resource path (if not found by upper algorithms)<br>
     * - current {@link Thread#getContextClassLoader()} (if not found by upper algorithms)<br>
     * - this class {@link SpringContextHelper} loader (if not found by upper algorithms)
     *
     * @param path resource path
     * @param firstOccurs specify returning stream of first occurred resource. false for last occurred resource
     *
     * @return {@link Resource} or null if failed
     */
    public Resource searchResource(String path, boolean firstOccurs) {
        return findResource(getContext(), path, firstOccurs);
    }
    /**
     * Get the resource as {@link Resource} of the specified resource path<br>
     * by below strategies:<br>
     * - specified {@link ApplicationContext} by {@link ApplicationContextProvider#getAwareApplicationContext()}<br>
     * - specified {@link ApplicationContext#getClassLoader()} by {@link ApplicationContextProvider#getAwareApplicationContext()}<br>
     *   (if not found by upper algorithms)<br>
     * - physical {@link File} by specified resource path (if not found by upper algorithms)<br>
     * - current {@link Thread#getContextClassLoader()} (if not found by upper algorithms)<br>
     * - this class {@link SpringContextHelper} loader (if not found by upper algorithms)
     *
     * @param path resource path
     * @param firstOccurs specify returning stream of first occurred resource. false for last occurred resource
     *
     * @return {@link Resource} or null if failed
     */
    public static Resource findResource(String path, boolean firstOccurs) {
        return findResource(ApplicationContextProvider.getAwareApplicationContext(), path, firstOccurs);
    }
    /**
     * Get the resource as {@link Resource} of the specified resource path<br>
     * by below strategies:<br>
     * - specified {@link ApplicationContext}<br>
     * - specified {@link ApplicationContext#getClassLoader()} (if not found by upper algorithms)<br>
     * - physical {@link File} by specified resource path (if not found by upper algorithms)<br>
     * - current {@link Thread#getContextClassLoader()} (if not found by upper algorithms)<br>
     * - this class {@link SpringContextHelper} loader (if not found by upper algorithms)
     *
     * @param applicationContext context
     * @param path resource path
     * @param firstOccurs specify returning stream of first occurred resource. false for last occurred resource
     *
     * @return {@link Resource} or null if failed
     */
    public static Resource findResource(ApplicationContext applicationContext, String path, boolean firstOccurs) {
    	Resource resource = null;
        if (applicationContext != null && StringUtils.hasText(path)) {
            // resolve all possible resource paths
            resource = findContextResourceRecursively(applicationContext, path, firstOccurs);
        }

        // try detecting local file
        if (resource == null && StringUtils.hasText(path)) {
            File f = new File(path);
            if (f.exists() && f.canRead()) {
                resource = new FileSystemResource(path);
                traceResource(path, resource);
            }
        }

        // try using current thread class loader
        if (resource == null && StringUtils.hasText(path)) {
        	// resolve all possible resource paths
        	resource = findClassLoaderResourceRecursively(Thread.currentThread().getContextClassLoader(), path, firstOccurs);
        }

        // try using class loader
        if (resource == null && StringUtils.hasText(path)) {
        	// resolve all possible resource paths
        	resource = findClassLoaderResourceRecursively(SpringContextHelper.class.getClassLoader(), path, firstOccurs);
        }

        // debug
        if (resource == null && log.isDebugEnabled()) {
        	log.warn("Not found resource path [" + path + "]");
        }
        return resource;
    }
    /**
     * Get the resource as {@link Resource} of the specified resource path (recursively)<br>
     * by the specified {@link ClassLoader} and recursively finding on its parent {@link ClassLoader}
     *
     * @param loader {@link ClassLoader}
     * @param path resource path
     * @param firstOccurs specify returning stream of first occurred resource. false for last occurred resource
     *
     * @return {@link Resource} or null if failed
     */
    public static Resource findClassLoaderResourceRecursively(ClassLoader loader, String path, boolean firstOccurs) {
        Entry<String, List<Resource>> resources = null;
        Entry<String, List<Resource>> prevResources = null;
		while ((resources == null || CollectionUtils.isEmpty(resources.getValue())) && loader != null && StringUtils.hasText(path)) {
		    resources = findClassLoaderResources(loader, path);
		    if (!CollectionUtils.isEmpty(resources.getValue())) {
		        // debug
                traceResource(path, resources.getValue().get(0));

                // break for first found resource
                if (firstOccurs) {
                    break;

                } else {
                    prevResources = resources; // back-up the last found resources
                }
		    }

		    // continue finding on parent class loader
			loader = loader.getParent();
		}
		return (firstOccurs ? Optional.ofNullable(resources)
		        .map(Entry::getValue).orElseGet(LinkedList::new).parallelStream().findFirst().orElse(null)
		        : Optional.ofNullable(prevResources)
                .map(Entry::getValue).orElseGet(LinkedList::new).parallelStream().findFirst().orElse(null));
	}
    /**
     * Get the resource as {@link Resource} of the specified resource path (recursively)<br>
     * by below strategies:<br>
     * - specified {@link ApplicationContext} by {@link ApplicationContextProvider#getAwareApplicationContext()}<br>
     * - specified {@link ApplicationContext#getClassLoader()} by {@link ApplicationContextProvider#getAwareApplicationContext()}<br>
     *   (if not found by upper algorithms)
     *
     * @param path resource path
     * @param firstOccurs specify returning stream of first occurred resource. false for last occurred resource
     *
     * @return {@link Resource} or null if failed
     */
    public static Resource findContextResourceRecursively(String path, boolean firstOccurs) {
        return findContextResourceRecursively(ApplicationContextProvider.getAwareApplicationContext(), path, firstOccurs);
    }
    /**
     * Get the resource as {@link Resource} of the specified resource path (recursively)<br>
     * by below strategies:<br>
     * - specified {@link ApplicationContext}<br>
     * - specified {@link ApplicationContext#getClassLoader()} (if not found by upper algorithms)
     *
     * @param applicationContext context
     * @param path resource path
     * @param firstOccurs specify returning stream of first occurred resource. false for last occurred resource
     *
     * @return {@link Resource} or null if failed
     */
    /* java:S3776: Cognitive Complexity of methods should not be too high */
    @SuppressWarnings({ "java:S3776" })
    public static Resource findContextResourceRecursively(ApplicationContext applicationContext, String path, boolean firstOccurs) {
    	Set<String> resourcePaths = StringUtils.resolveResourceNames(path);
    	Entry<String, List<Resource>> prevResources = null;
		Resource resource = null;
		if (applicationContext != null && !CollectionUtils.isEmpty(resourcePaths)) {
		    ApplicationContext ctx = applicationContext;
		    while(resource == null && ctx != null) {
		        // loop for every resource path
	            for(String resourcePath : resourcePaths) {
	                Entry<String, List<Resource>> resources = findContextResources(ctx, resourcePath);
	                if (!CollectionUtils.isEmpty(resources.getValue())) {
	                    // debug
	                    resource = resources.getValue().get(0);
                        traceResource(resourcePath, resource);

                        // break for first found resource
                        if (firstOccurs) {
                            break;

                        } else {
                            prevResources = resources; // back-up the last found resources
                        }
	                }
	            }

	            // break loop
	            if (firstOccurs && resource != null) {
	                break;
	            }

	            // continue with parent context if not found
	            if (resource == null) ctx = ctx.getParent();
		    }
		}
		return (firstOccurs ? resource : Optional.ofNullable(prevResources)
		        .map(Entry::getValue).orElseGet(LinkedList::new).parallelStream().findFirst().orElse(null));
	}
    /**
     * Get the last ocurred resource as {@link Resource} of the specified resource path
     *
     * @param path resource path
     *
     * @return {@link Resource} or null if failed
     */
    public static Resource findLastResource(String path) {
        return findResource(path, false);
    }
    /**
     * Get the last ocurred resource as {@link Resource} of the specified resource path
     *
     * @param path resource path
     *
     * @return {@link Resource} or null if failed
     */
    public Resource searchLastResource(String path) {
        return searchResource(path, false);
    }
    /**
     * Get the first ocurred resource as {@link Resource} of the specified resource path
     *
     * @param path resource path
     *
     * @return {@link Resource} or null if failed
     */
    public static Resource findFirstResource(String path) {
        return findResource(path, true);
    }
    /**
     * Get the first ocurred resource as {@link Resource} of the specified resource path
     *
     * @param path resource path
     *
     * @return {@link Resource} or null if failed
     */
    public Resource searchFirstResource(String path) {
        return searchResource(path, true);
    }

    /**
     * Get the resource as {@link InputStream} of the specified resource path
     *
     * @param path resource path
     * @param firstOccurs specify returning stream of first occurred resource. false for last occurred resource
     *
     * @return {@link InputStream} or null if failed
     */
    public static InputStream findResourceAsStream(String path, boolean firstOccurs) {
    	return Optional.ofNullable(findResource(path, firstOccurs)).filter(Resource::exists)
    			.map(ExceptionUtils.wrap(log).function(Exceptions.wrap().function(Resource::getInputStream)))
    			.filter(Optional::isPresent).map(Optional::get).orElse(null);
    }
    /**
     * Get the resource as {@link InputStream} of the specified resource path
     *
     * @param path resource path
     * @param firstOccurs specify returning stream of first occurred resource. false for last occurred resource
     *
     * @return {@link InputStream} or null if failed
     */
    public InputStream searchResourceAsStream(String path, boolean firstOccurs) {
    	return Optional.ofNullable(searchResource(path, firstOccurs)).filter(Resource::exists)
    			.map(ExceptionUtils.wrap(log).function(Exceptions.wrap().function(Resource::getInputStream)))
    			.filter(Optional::isPresent).map(Optional::get).orElse(null);
    }

    /**
     * Get the resource as string of the specified resource path
     *
     * @param path resource path
     * @param firstOccurs specify returning stream of first occurred resource. false for last occurred resource
     *
     * @return SQL string or null if failed
     */
    public static String findResourceAsString(String path, boolean firstOccurs) {
        InputStream is = findResourceAsStream(path, firstOccurs);
        return (is == null ? null : StringUtils.toString(is));
    }
    /**
     * Get the resource as string of the specified resource path
     *
     * @param path resource path
     * @param firstOccurs specify returning stream of first occurred resource. false for last occurred resource
     *
     * @return SQL string or null if failed
     */
    public String searchResourceAsString(String path, boolean firstOccurs) {
        InputStream is = searchResourceAsStream(path, firstOccurs);
        return (is == null ? null : StringUtils.toString(is));
    }
    
    /**
     * Get the last ocurred resource as {@link InputStream} of the specified resource path
     *
     * @param path resource path
     *
     * @return {@link InputStream} or null if failed
     */
    public static InputStream findLastResourceAsStream(String path) {
        return findResourceAsStream(path, false);
    }
    /**
     * Get the last ocurred resource as {@link InputStream} of the specified resource path
     *
     * @param path resource path
     *
     * @return {@link InputStream} or null if failed
     */
    public InputStream searchLastResourceAsStream(String path) {
        return searchResourceAsStream(path, false);
    }
    /**
     * Get the first ocurred resource as {@link InputStream} of the specified resource path
     *
     * @param path resource path
     *
     * @return {@link InputStream} or null if failed
     */
    public static InputStream findFirstResourceAsStream(String path) {
        return findResourceAsStream(path, true);
    }
    /**
     * Get the first ocurred resource as {@link InputStream} of the specified resource path
     *
     * @param path resource path
     *
     * @return {@link InputStream} or null if failed
     */
    public InputStream searchFirstResourceAsStream(String path) {
        return searchResourceAsStream(path, true);
    }

    /**
     * Get the last ocurred resource as string of the specified resource path
     *
     * @param path resource path
     *
     * @return SQL string or null if failed
     */
    public static String findLastResourceAsString(String path) {
        return findResourceAsString(path, false);
    }
    /**
     * Get the last ocurred resource as string of the specified resource path
     *
     * @param path resource path
     *
     * @return SQL string or null if failed
     */
    public String searchLastResourceAsString(String path) {
        return searchResourceAsString(path, false);
    }
    /**
     * Get the resource as string of the specified resource path
     *
     * @param path resource path
     *
     * @return SQL string or null if failed
     */
    public static String findFirstResourceAsString(String path) {
        return findResourceAsString(path, true);
    }
    /**
     * Get the resource as string of the specified resource path
     *
     * @param path resource path
     *
     * @return SQL string or null if failed
     */
    public String searchFirstResourceAsString(String path) {
        return searchResourceAsString(path, true);
    }

    /***************************************************
     * HANDLER MAPPINGS SUPPORT
     ***************************************************/

    /**
     * Get the {@link HandlerMapping} list from context
     * @return the {@link HandlerMapping} list from context
     */
    public static List<HandlerMapping> findHandlerMappings() {
    	return findBeans(HandlerMapping.class);
    }
    /**
     * Get the {@link HandlerMapping} list from context
     * @return the {@link HandlerMapping} list from context
     */
    public List<HandlerMapping> searchHandlerMappings() {
    	return searchBeans(HandlerMapping.class);
    }
    /**
     * Find {@link HandlerExecutionChain} of the specified {@link HttpServletRequest}
     *
     * @param request to parse
     *
     * @return {@link HandlerExecutionChain} or NULL if not found
     */
    public static HandlerExecutionChain findExecutionChain(final HttpServletRequest request) {
    	List<HandlerMapping> mappings = findHandlerMappings();
    	return Optional.ofNullable(mappings).orElseGet(LinkedList::new)
    			.parallelStream()
    			.map(ExceptionUtils.wrap(log).function(Exceptions.wrap().function(mapping -> mapping.getHandler(request))))
    			.filter(Optional::isPresent).map(Optional::get)
    			.filter(Objects::nonNull).findFirst().orElse(null);
    }
    /**
     * Find {@link HandlerExecutionChain} of the specified {@link HttpServletRequest}
     *
     * @param request to parse
     *
     * @return {@link HandlerExecutionChain} or NULL if not found
     */
    public HandlerExecutionChain searchExecutionChain(final HttpServletRequest request) {
    	List<HandlerMapping> mappings = searchHandlerMappings();
    	return Optional.ofNullable(mappings).orElseGet(LinkedList::new)
    			.parallelStream()
    			.map(ExceptionUtils.wrap(log).function(Exceptions.wrap().function(mapping -> mapping.getHandler(request))))
    			.filter(Optional::isPresent).map(Optional::get)
    			.filter(Objects::nonNull).findFirst().orElse(null);
    }
    /**
     * Find handler object of the specified {@link HttpServletRequest}
     *
     * @param request to parse
     *
     * @return handler object or NULL if not found
     */
    public static Object findHandler(HttpServletRequest request) {
    	HandlerExecutionChain mappedHandler = (request == null ? null : findExecutionChain(request));
    	return (mappedHandler == null ? null : mappedHandler.getHandler());
    }
    /**
     * Find handler object of the specified {@link HttpServletRequest}
     *
     * @param request to parse
     *
     * @return handler object or NULL if not found
     */
    public Object searchHandler(HttpServletRequest request) {
    	HandlerExecutionChain mappedHandler = (request == null ? null : searchExecutionChain(request));
    	return (mappedHandler == null ? null : mappedHandler.getHandler());
    }
}
