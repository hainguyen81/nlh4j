/*
 * @(#)SpringContextHelper.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.servlet;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import lombok.Getter;
import lombok.Setter;

/**
 * WEB SPRING context helper.
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Singleton
public final class SpringContextHelper implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/** SLF4J */
	private static final Logger logger = LoggerFactory.getLogger(SpringContextHelper.class);

	/**
	 * WEBコンテクスト
	 */
	@Getter
	@Setter
	private ServletConfig servletConfig;
	@Getter
	@Setter
	private ServletContext servletContext;
	@Setter
	private ApplicationContext applicationContext;

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
        	while(ctx != null && bean == null) {
	            try {
	                bean = ctx.getBean(beanRef);
	            }
	            catch (NoSuchBeanDefinitionException e) {
	                logger.warn(e.getMessage());
	                bean = null;
	            }
	            catch (BeansException e) {
	            	logger.warn(e.getMessage());
	                bean = null;
	            } finally {
	            	if (bean != null) break;
	            	else ctx = ctx.getParent();
	            }
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
        	while(ctx != null && bean == null) {
	            try {
	                bean = ctx.getBean(beanRef, requiredType);
	            }
	            catch (NoSuchBeanDefinitionException e) {
	                logger.warn(e.getMessage());
	                bean = null;
	            }
	            catch (BeansException e) {
	            	logger.warn(e.getMessage());
	                bean = null;
	            } finally {
	            	if (bean != null) break;
	            	else ctx = ctx.getParent();
	            }
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
        	while(ctx != null && bean == null) {
	            try {
	                bean = ctx.getBean(beanRef, args);
	            }
	            catch (NoSuchBeanDefinitionException e) {
	                logger.warn(e.getMessage());
	                bean = null;
	            }
	            catch (BeansException e) {
	            	logger.warn(e.getMessage());
	                bean = null;
	            } finally {
	            	if (bean != null) break;
	            	else ctx = ctx.getParent();
	            }
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
		    	try {
		    		beans = ctx.getBeansOfType(beanClass, Boolean.TRUE, Boolean.FALSE);
		    	}
		    	catch (BeansException e) {
		    		logger.warn(e.getMessage());
		    		beans = null;
		    	} finally {
			    	if (!CollectionUtils.isEmpty(beans)) break;
			    	else ctx = ctx.getParent();
		    	}
    		}
	    	if (!CollectionUtils.isEmpty(beans)) {
		    	for(final Iterator<String> it = beans.keySet().iterator(); it.hasNext();) {
		    		String beanref = it.next();
		    		Object fbean = beans.get(beanref);
		    		if (beanClass.equals(fbean.getClass())
		    				|| (inherited && BeanUtils.isInstanceOf(fbean.getClass(), beanClass))) {
		    			bean = BeanUtils.safeType(fbean, beanClass);
		    			break;
		    		}
		    	}
	    	}
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
    	List<T> beansLst = new LinkedList<T>();
    	if (applicationContext != null && beanClass != null) {
    		ApplicationContext ctx = applicationContext;
    		while(CollectionUtils.isEmpty(beans) && ctx != null) {
		    	try {
		    		beans = ctx.getBeansOfType(beanClass, Boolean.TRUE, Boolean.FALSE);
		    	}
		    	catch (BeansException e) {
		    		logger.warn(e.getMessage());
		    		beans = null;
		    	} finally {
			    	if (!CollectionUtils.isEmpty(beans)) break;
			    	else ctx = ctx.getParent();
		    	}
    		}
	    	if (!CollectionUtils.isEmpty(beans)) {
		    	for(final Iterator<String> it = beans.keySet().iterator(); it.hasNext();) {
		    		String beanref = it.next();
		    		Object fbean = beans.get(beanref);
		    		if (beanClass.equals(fbean.getClass())
		    				|| (inherited && BeanUtils.isInstanceOf(fbean.getClass(), beanClass))) {
		    			T bean = BeanUtils.safeType(fbean, beanClass);
		    			if (bean != null) beansLst.add(bean);
		    		}
		    	}
	    	}
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
     * Log the specified resource for debug
     *
     * @param originalPath the original resource pattern
     * @param resource to debug
     */
    private static void debugResource(String originalPath, Resource resource) {
    	if (logger.isDebugEnabled() && resource != null) {
    		String resPath = null;
    		// URL
    		if (!StringUtils.hasText(resPath)) {
    			try { resPath = resource.getURL().getPath(); }
    			catch (Exception e) {}
    		}
    		// URI
    		if (!StringUtils.hasText(resPath)) {
    			try { resPath = resource.getURI().getPath(); }
    			catch (Exception e) {}
    		}
    		// File
    		if (!StringUtils.hasText(resPath)) {
    			try { resPath = resource.getFile().getPath(); }
    			catch (Exception e) {}
    		}
    		logger.debug("Resolved [" + originalPath + "] to [" + resPath + "]");
    	}
    }
    /**
     * Get the resource as {@link Resource} array of the specified resource path
     *
     * @param applicationContext context
     * @param path resource path
     *
     * @return {@link Resource} map by pattern and resources list or null if failed
     */
    public static Map<String, List<Resource>> findResources(final ApplicationContext applicationContext, String path) {
    	Map<String, List<Resource>> resources = new LinkedHashMap<String, List<Resource>>();
        if (applicationContext != null) {
            // resolve all possible resource paths
            List<String> resourcePaths = StringUtils.resolveResourceNames(path);
            if (!CollectionUtils.isEmpty(resourcePaths)) {
                ApplicationContext ctx = applicationContext;
                while(ctx != null) {
    	            try {
    	                for(String resourcePath : resourcePaths) {
        	                Resource[] arrResources = ctx.getResources(resourcePath);
        	                if (!CollectionUtils.isEmpty(arrResources)) {
        	                	resources.put(resourcePath,
        	                			CollectionUtils.toList(arrResources));
        	                	// debug
        	                	Resource resource = arrResources[0];
        	                	debugResource(resourcePath, resource);
        	                }
    	                }
    	                // continue with parent context if not found
    	                if (CollectionUtils.isEmpty(resources)) ctx = ctx.getParent();
    	                else if (!CollectionUtils.isEmpty(resources)) break;
    	            }
    	            catch (Exception e) {
    	                logger.error(e.getMessage(), e);
    	                if (!CollectionUtils.isEmpty(resources)) {
    	                	resources.clear();
    	                }
    	            }
                }
            }
        }
        // try detecting local file
        if (CollectionUtils.isEmpty(resources) && StringUtils.hasText(path)) {
            File f = new File(path);
            if (f.exists() && f.canRead()) {
                resources = new LinkedHashMap<String, List<Resource>>();
                resources.put(path, new LinkedList<Resource>());
                resources.get(path).add(new FileSystemResource(path));
                debugResource(path, resources.get(path).get(0));
            }
        }
        // try using class loader
		if (CollectionUtils.isEmpty(resources) && StringUtils.hasText(path)) {
        	// resolve all possible resource paths
        	ClassLoader loader = SpringContextHelper.class.getClassLoader();
        	while (loader != null) {
				try {
					URL url = loader.getResource(path);
					Resource resource = (url != null ? new UrlResource(url) : null);
					if (resource != null) {
						String resourcePath = url.getPath();
						if (resources.get(resourcePath) == null) {
							resources.put(resourcePath, new LinkedList<Resource>());
						}
						resources.get(resourcePath).add(resource);
						debugResource(resourcePath, resource);
					}
					// continue with parent context if not found
					loader = loader.getParent();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
        }
        // debug
        if (CollectionUtils.isEmpty(resources) && logger.isDebugEnabled()) {
        	logger.warn("Not found resource path [" + path + "]");
        }
        return resources;
    }
    /**
     * Get the resource as {@link Resource} of the specified resource path
     *
     * @param path resource path
     *
     * @return {@link Resource} map by pattern and resources list or null if failed
     */
    public Map<String, List<Resource>> searchResources(String path) {
    	return findResources(this.getContext(), path);
    }
    /**
     * Get the resource as {@link Resource} of the specified resource path
     *
     * @param applicationContext context
     * @param path resource path
     * @param firstOccurs specify returning stream of first occured resource. false for last occured resource
     *
     * @return {@link Resource} or null if failed
     */
    public static Resource findResource(final ApplicationContext applicationContext, String path, boolean firstOccurs) {
    	Resource resource = null;
        if (applicationContext != null) {
            // resolve all possible resource paths
            List<String> resourcePaths = StringUtils.resolveResourceNames(path);
            Resource[] resources = null;
            if (!CollectionUtils.isEmpty(resourcePaths)) {
                ApplicationContext ctx = applicationContext;
                while(resource == null && ctx != null) {
    	            try {
    	                for(String resourcePath : resourcePaths) {
        	                resources = ctx.getResources(resourcePath);
        	                resource = (CollectionUtils.isEmpty(resources)
        	                        ? null : firstOccurs ? resources[0] : resources[resources.length - 1]);
        	                if (resource != null) {
        	                	// debug
        	                	debugResource(resourcePath, resource);
        	                	break;
        	                }
    	                }
    	                // continue with parent context if not found
    	                if (resource == null) ctx = ctx.getParent();
    	            }
    	            catch (Exception e) {
    	                logger.error(e.getMessage(), e);
    	                resources = null;
    	                resource = null;
    	            }
                }
            }
        }
        // try detecting local file
        if (resource == null && StringUtils.hasText(path)) {
            File f = new File(path);
            if (f.exists() && f.canRead()) {
                resource = new FileSystemResource(path);
                debugResource(path, resource);
            }
        }
        // try using class loader
        if (resource == null && StringUtils.hasText(path)) {
        	// resolve all possible resource paths
        	ClassLoader loader = SpringContextHelper.class.getClassLoader();
        	while (resource == null && loader != null) {
				try {
					URL url = loader.getResource(path);
					resource = (url != null ? new UrlResource(url) : null);
					if (resource != null) {
						// debug
						debugResource(path, resource);
						break;
					}
					// continue with parent context if not found
					if (resource == null) loader = loader.getParent();
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
					resource = null;
				}
			}
        }
        // debug
        if (resource == null && logger.isDebugEnabled()) {
        	logger.warn("Not found resource path [" + path + "]");
        }
        return resource;
    }
    /**
     * Get the resource as {@link Resource} of the specified resource path
     *
     * @param path resource path
     * @param firstOccurs specify returning stream of first occured resource. false for last occured resource
     *
     * @return {@link Resource} or null if failed
     */
    public static Resource findResource(String path, boolean firstOccurs) {
    	return findResource(ApplicationContextProvider.getAwareApplicationContext(), path, firstOccurs);
    }
    /**
     * Get the resource as {@link Resource} of the specified resource path
     *
     * @param path resource path
     * @param firstOccurs specify returning stream of first occured resource. false for last occured resource
     *
     * @return {@link Resource} or null if failed
     */
    public Resource searchResource(String path, boolean firstOccurs) {
    	return findResource(this.getContext(), path, firstOccurs);
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
     * @param firstOccurs specify returning stream of first occured resource. false for last occured resource
     *
     * @return {@link InputStream} or null if failed
     */
    public static InputStream findResourceAsStream(String path, boolean firstOccurs) {
    	Resource resource = findResource(path, firstOccurs);
    	try {
    		return (resource == null ? null : resource.getInputStream());
    	} catch (Exception e) {
    		logger.warn(e.getMessage());
    	}
    	return null;
    }
    /**
     * Get the resource as {@link InputStream} of the specified resource path
     *
     * @param path resource path
     * @param firstOccurs specify returning stream of first occured resource. false for last occured resource
     *
     * @return {@link InputStream} or null if failed
     */
    public InputStream searchResourceAsStream(String path, boolean firstOccurs) {
    	Resource resource = searchResource(path, firstOccurs);
    	try {
    		return (resource == null ? null : resource.getInputStream());
    	} catch (Exception e) {
    		logger.warn(e.getMessage());
    	}
    	return null;
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
     * Get the resource as string of the specified resource path
     *
     * @param path resource path
     * @param firstOccurs specify returning string of first occured resource. false for last occured resource
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
     * @param firstOccurs specify returning string of first occured resource. false for last occured resource
     *
     * @return SQL string or null if failed
     */
    public String searchResourceAsString(String path, boolean firstOccurs) {
        InputStream is = searchResourceAsStream(path, firstOccurs);
        return (is == null ? null : StringUtils.toString(is));
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
    public static HandlerExecutionChain findExecutionChain(HttpServletRequest request) {
    	List<HandlerMapping> mappings = findHandlerMappings();
    	HandlerExecutionChain mappedHandler = null;
    	if (!CollectionUtils.isEmpty(mappings)) {
    		for(HandlerMapping mapping : mappings) {
    			try { mappedHandler = mapping.getHandler(request); }
    			catch (Exception e) { mappedHandler = null; }
    			finally {
    				if (mappedHandler != null) {
    					break;
    				}
    			}
    		}
    	}
    	return mappedHandler;
    }
    /**
     * Find {@link HandlerExecutionChain} of the specified {@link HttpServletRequest}
     *
     * @param request to parse
     *
     * @return {@link HandlerExecutionChain} or NULL if not found
     */
    public HandlerExecutionChain searchExecutionChain(HttpServletRequest request) {
    	List<HandlerMapping> mappings = searchHandlerMappings();
    	HandlerExecutionChain mappedHandler = null;
    	if (!CollectionUtils.isEmpty(mappings)) {
    		for(HandlerMapping mapping : mappings) {
    			try { mappedHandler = mapping.getHandler(request); }
    			catch (Exception e) { mappedHandler = null; }
    			finally {
    				if (mappedHandler != null) {
    					break;
    				}
    			}
    		}
    	}
    	return mappedHandler;
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
