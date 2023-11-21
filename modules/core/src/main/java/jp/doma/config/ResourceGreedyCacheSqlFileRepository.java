/*
 * @(#)ResourceGreedyCacheSqlFileRepository.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package jp.doma.config;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.nlh4j.core.servlet.ApplicationContextProvider;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.StreamUtils;
import org.nlh4j.util.StringUtils;
import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.internal.Constants;
import org.seasar.doma.internal.jdbc.sql.SqlParser;
import org.seasar.doma.internal.jdbc.util.SqlFileUtil;
import org.seasar.doma.internal.util.ResourceUtil;
import org.seasar.doma.jdbc.SqlFile;
import org.seasar.doma.jdbc.SqlFileNotFoundException;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.SqlNode;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Extend {@link SqlFileRepository} for classpath repository
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Singleton
public class ResourceGreedyCacheSqlFileRepository implements InitializingBean, DisposableBean, SqlFileRepository, Serializable {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * SLF4J
     */
    private static final Logger logger = LoggerFactory.getLogger(ResourceGreedyCacheSqlFileRepository.class);

    /** default SQL cache capacity */
    private static final int DEFAULT_CACHE_CAPACITY = 200;

    /** SQLのパスをキー、SQLファイルを値とするマップです。 */
    protected final ConcurrentMap<String, SqlFile> sqlFileMap = new ConcurrentHashMap<String, SqlFile>(DEFAULT_CACHE_CAPACITY);
    private final ConcurrentMap<String, String> sqlMap = new ConcurrentHashMap<String, String>(DEFAULT_CACHE_CAPACITY);
    private final ConcurrentMap<String, List<Resource>> resourceScanner = new ConcurrentHashMap<String, List<Resource>>(DEFAULT_CACHE_CAPACITY);

    /** specify finding the last occured resource SQL file. false for first occured */
    @Getter
    @Setter
    private boolean override = true;
    /** specify finding SQL file with classpath resource */
    @Getter
    @Setter
    private boolean wirecast = true;
    /** specify folder prefix after "META-INF/" right now **/
    @Getter
    @Setter
    private String prefix;

    /** {@link ApplicationContext} */
    private ApplicationContext applicationContext;
    /** {@link ApplicationContextProvider} */
    @Inject
    protected ApplicationContextProvider contextProvider;
    /** context helper */
    @Inject
    private SpringContextHelper contextHelper;

    /**
     * Get the context helper
     * @return the context helper
     */
    protected final SpringContextHelper getContextHelper() {
    	if (this.contextHelper == null && this.getContext() != null) {
    		this.contextHelper = new SpringContextHelper(this.getContext());
    	}
    	if (this.contextHelper == null) return null;
    	synchronized(this.contextHelper) {
    		return this.contextHelper;
    	}
    }
    /**
     * Get bean by the specified bean class
     *
     * @param beanClass the bean class to parse from context
     *
     * @return bean or NULL
     */
    protected final <K> K findBean(Class<K> beanClass) {
        return (this.getContextHelper() == null ? null : this.getContextHelper().searchBean(beanClass));
    }
    /**
     * Get bean by the specified bean class
     *
     * @param beanClass the bean class to parse from context
     *
     * @return bean or NULL
     */
    protected final <K> List<K> findBeans(Class<K> beanClass) {
        return (this.getContextHelper() == null ? null : this.getContextHelper().searchBeans(beanClass));
    }

    /**
     * Get the application context
     * @return the application context
     */
    protected final ApplicationContext getContext() {
    	return (this.applicationContext != null
    			? this.applicationContext
    					: this.contextProvider != null
    					? this.contextProvider.getApplicationContext() : null);
    }

    /**
     * Set application context
     * @param applicationContext context
     */
    public final void setApplicationContext(ApplicationContext applicationContext) {
    	this.applicationContext = applicationContext;
    }

    /**
     * Initialize a new instance of {@link ResourceGreedyCacheSqlFileRepository}
     */
    public ResourceGreedyCacheSqlFileRepository() {}
    /**
     * Initialize a new instance of {@link ResourceGreedyCacheSqlFileRepository}
     *
     * @param applicationContext context
     */
    public ResourceGreedyCacheSqlFileRepository(ApplicationContext applicationContext) {
    	this.setApplicationContext(applicationContext);
    }

    /**
     * Get a boolean value indicating that the specified SQL file is valid.<br>
     * TODO Children classes maybe override this method for checking SQL file
     *
     * @param path to check
     *
     * @return true for valid; else false
     */
    protected boolean validSqlFile(String path) {
        // empty sql path
        if (!StringUtils.hasText(path)) {
            throw new DomaNullPointerException("path");
        }
        // not start with META-INF/ or classpath: or classpath*:
        if (!path.toLowerCase().startsWith(Constants.SQL_PATH_PREFIX.toLowerCase())
                && !path.toLowerCase().startsWith(StringUtils.PREFIX_CLASSPATH)
                && !path.toLowerCase().startsWith(StringUtils.PREFIX_WIRECAST_CLASSPATH)) {
            throw new DomaIllegalArgumentException("path",
                    "The path does not start with "
                    + "'" + Constants.SQL_PATH_PREFIX + "'"
                    + " or '" + StringUtils.PREFIX_CLASSPATH + "'"
                    + " or '" + StringUtils.PREFIX_WIRECAST_CLASSPATH + "'");
        }
        // not end with .sql
        if (!path.toLowerCase().endsWith(Constants.SQL_PATH_SUFFIX)) {
            throw new DomaIllegalArgumentException("path",
                    "The path does not end with '" + Constants.SQL_PATH_SUFFIX + "'");
        }
        return true;
    }

    /* (Non-Javadoc)
     * @see org.seasar.doma.jdbc.SqlFileRepository#getSqlFile(java.lang.String, org.seasar.doma.jdbc.dialect.Dialect)
     */
    @Override
    public final SqlFile getSqlFile(String path, Dialect dialect) {
        // check SQL file
        if (!this.validSqlFile(path)) return null;
        // check valid dialect
        if (dialect == null) throw new DomaNullPointerException("dialect");
        return this.getSqlFileWithCacheControl(path, dialect);
    }

    /**
     * Get SQL file from cache storage.<br>
     * If not found, SQL file will be loaded
     *
     * @param path sql path as key storage
     * @param dialect {@link Dialect}
     *
     * @return SQL file
     */
    protected final SqlFile getSqlFileWithCacheControl(String path, Dialect dialect) {
        SqlFile file = sqlFileMap.get(path);
        if (file != null) {
            return file;
        }
        file = createSqlFile(path, dialect);
        SqlFile current = sqlFileMap.putIfAbsent(path, file);
        return current != null ? current : file;
    }

    /**
     * Create SQL file if not found in cache storage
     *
     * @param path sql path
     * @param dialect {@link Dialect}
     *
     * @return SQL file
     */
    protected SqlFile createSqlFile(String path, Dialect dialect) {
        String sql = null;
        String resPath = path;
        String prefix = this.getPrefix();
        if (StringUtils.hasText(prefix)) {
        	prefix = prefix.trim();
        	prefix = (prefix.startsWith("/") ? prefix.substring(1)
        			: prefix.endsWith("/") ? prefix.substring(0, prefix.length() - 1)
        					: prefix);
        }
        if (resPath.toLowerCase().contains(Constants.SQL_PATH_PREFIX.toLowerCase())) {
            resPath = resPath.replace(Constants.SQL_PATH_PREFIX, "");
        }

        // if need wirecast
        if (this.isWirecast() && this.getContextHelper() != null) {
            if (!sqlMap.containsKey(path) || !StringUtils.hasText(sqlMap.get(path))) {
            	// check from resource scanner
            	List<Resource> resources = this.resourceScanner.get(prefix + "/" + resPath);
            	resources = (CollectionUtils.isEmpty(resources)
            			? this.resourceScanner.get(resPath) : resources);
            	if (!CollectionUtils.isEmpty(resources)) {
            	    logger.debug("Create repository SQL by prefix {" + prefix + "} - path {" + prefix + "/" + resPath + "}");
            		Resource resource = (this.isOverride()
            				? resources.get(resources.size() - 1) : resources.get(0));
            		InputStream is = null;
            		try { is = resource.getInputStream(); }
            		catch (Exception e) {}
            		finally {
            			if (is != null) sql = StringUtils.toString(is);
            			StreamUtils.closeQuitely(is);
            		}
            	}

            	// if not found from scanner; try re-scan resource
            	if (!StringUtils.hasText(sql)) {
	                // resolve resource with META-INF/"prefix"
	                List<String> resourcePaths = StringUtils.resolveResourceNames(
	                        Constants.SQL_PATH_PREFIX + prefix, resPath);
	                // search resource
	                if (!CollectionUtils.isEmpty(resourcePaths)) {
	                    for(String resourcePath : resourcePaths) {
	                        sql = (this.isOverride() ? this.getContextHelper().searchLastResourceAsString(resourcePath)
	                                : this.getContextHelper().searchFirstResourceAsString(resourcePath));
	                        if (StringUtils.hasText(sql)) {
	                            if (logger.isDebugEnabled()) {
	                                logger.debug("Found SQL file by pattern [" + resourcePath + "]");
	                            }
	                            sqlMap.put(path, sql);
	                            break;
	                        }
	                        else if (logger.isDebugEnabled()) {
	                            logger.debug("Not found SQL file by pattern [" + resourcePath + "]");
	                        }
	                    }
	                }
            	} else {
            		// cache SQL
            		sqlMap.put(path, sql);
            	}
            } else {
            	// require SQL from cache
                sql = sqlMap.get(path);
            }
        }
        // if not wildcast of invalid resource path
        // then trying load as DOMA default
        if (!StringUtils.hasText(sql)) {
            resPath = SqlFileUtil.convertToDbmsSpecificPath(path, dialect);
            try {
                sql = ResourceUtil.getResourceAsString(resPath);
            } catch (Exception e1) {
                logger.warn(e1.getMessage(), e1);
                // try as current path
                try {
                    sql = ResourceUtil.getResourceAsString(path);
                } catch (Exception e2) {
                    logger.error(e2.getMessage(), e2);
                }
            }
        }
        // if valid SQL string
        if (StringUtils.hasText(sql)) {
            SqlNode sqlNode = parse(sql);
            return new SqlFile(path, sql, sqlNode);
        }
        throw new SqlFileNotFoundException(path);
    }

    /**
     * Parse SQL string
     *
     * @param sql SQL string to parse
     *
     * @return SQL node
     */
    protected final SqlNode parse(String sql) {
        SqlParser parser = new SqlParser(sql);
        return parser.parse();
    }

    /* (Non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // detect all SQL resources
    	if (this.getContextHelper() != null) {
    		// search all possible resources list
    		Map<String, List<Resource>> resources =
    				this.getContextHelper().searchResources(
    						Constants.SQL_PATH_PREFIX + "**/*.sql");
    		if (!CollectionUtils.isEmpty(resources)) {
    			for(final Iterator<String> it = resources.keySet().iterator(); it.hasNext();) {
    				String resPattern = it.next();
    				List<Resource> resourcesList = resources.get(resPattern);
    				if (CollectionUtils.isEmpty(resourcesList)) continue;
    				for(Resource resource : resourcesList) {
    					// parse resource path
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
    		    		// if resource path is valid
    		    		// then read and cache SQL
    		    		if (StringUtils.hasText(resPath)
    		    				&& resPath.toLowerCase().contains(
    		    						Constants.SQL_PATH_PREFIX.toLowerCase())) {
    		    			resPath = resPath.trim();
    		    			int idx = resPath.indexOf(Constants.SQL_PATH_PREFIX);
    		    			resPath = resPath.substring(idx + Constants.SQL_PATH_PREFIX.length());
    		    			resPath = resPath.replace("\\", "/");
    		    			resPath = (resPath.startsWith("/") ? resPath.substring(1) : resPath);
    		    			if (!this.resourceScanner.containsKey(resPath)) {
    		    				this.resourceScanner.put(resPath, new LinkedList<Resource>());
    		    			}
    		    			this.resourceScanner.get(resPath).add(resource);
    		    			logger.debug("Found repository SQL resource path {" + resPath + "}");
    		    		}
    				}
    			}
    		}
    	}
    }

	/* (Non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		this.resourceScanner.clear();
		this.sqlFileMap.clear();
		this.sqlMap.clear();
	}
}
