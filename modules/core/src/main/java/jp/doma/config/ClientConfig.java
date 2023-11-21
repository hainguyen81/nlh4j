/*
 * @(#)ClientConfig.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package jp.doma.config;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.DomaAbstractConfig;
import org.seasar.doma.jdbc.dialect.Dialect;

/**
 * DOMA client configuration
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public class ClientConfig extends DomaAbstractConfig {
	protected DataSource dataSource;
	protected Dialect dialect;

	/** */
	public ClientConfig() {}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.seasar.doma.jdbc.Config#getDataSource()
	 */
	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
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

	/**
	 * @param dialect
	 *            the dialect to set
	 */
	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}
}
