/*
 * @(#)ClientConfig.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package jp.doma.config;

import javax.inject.Singleton;
import javax.sql.DataSource;

import org.seasar.doma.SingletonConfig;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.Dialect;

import lombok.Setter;

/**
 * DOMA client configuration
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Singleton
public class DomaConfig implements Config {

	@Setter
	protected DataSource dataSource;
	@Setter
	protected Dialect dialect;

	/** */
	public DomaConfig() {}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.seasar.doma.jdbc.Config#getDataSource()
	 */
	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.seasar.doma.jdbc.Config#getDialect()
	 */
	@Override
	public Dialect getDialect() {
		return dialect;
	}
}
