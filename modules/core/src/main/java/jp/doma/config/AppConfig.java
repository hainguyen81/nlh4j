/*
 * @(#)AppConfig.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package jp.doma.config;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.nlh4j.core.servlet.ApplicationContextProvider;
import org.seasar.doma.SingletonConfig;
import org.seasar.doma.jdbc.ConfigSupport;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.springframework.context.ApplicationContext;

import lombok.Setter;

/**
 * DOMA application configuration
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Singleton
public class AppConfig extends DomaConfig {

	/** SQL file repository */
	@Setter
	private SqlFileRepository sqlFileRepository;
	/** {@link ApplicationContextProvider} */
	@Inject
	protected ApplicationContextProvider contextProvider;

	/** {@link ApplicationContext} */
	private ApplicationContext applicationContext;

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

    /* (Non-Javadoc)
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
     */
    public final void setApplicationContext(ApplicationContext applicationContext) {
    	this.applicationContext = applicationContext;
    }

	/**
	 * Initialize a new instance of {@link AppConfig}
	 */
	public AppConfig() {}
	/**
	 * Initialize a new instance of {@link AppConfig}
	 *
	 * @param applicationContext context
	 */
	public AppConfig(ApplicationContext applicationContext) {
		this.setApplicationContext(applicationContext);
	}

	/* (Non-Javadoc)
	 * @see org.seasar.doma.jdbc.DomaAbstractConfig#getSqlFileRepository()
	 */
	@Override
	public SqlFileRepository getSqlFileRepository() {
	    // using default if not specify
	    if (this.sqlFileRepository == null) {
	        this.sqlFileRepository = new ResourceGreedyCacheSqlFileRepository(this.getContext());
	    }
	    return this.sqlFileRepository;
	}
}
