/*
 * @(#)RoutingDataSource.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package jp.doma.config;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Routing data source
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class RoutingDataSource extends AbstractRoutingDataSource {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource#
	 * determineCurrentLookupKey()
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession();
		String client = (String) session.getAttribute("USER_PROFILE");
		if (client == null) return "default";
		return client;
	}
}
