/*
 * @(#)AppPropKeys.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.common;

import java.io.Serializable;

/**
 * The system properties constant interface
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public final class AppPropKeys implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
	public static final String MAX_ITEMS_PER_PAGE = "app.max.items.per.page";
	public static final String MIN_PASSWORD_LENGTH = "app.min.password.length";
	public static final String LIKE_OPERATOR = "app.search.like.operator";
	public static final String APPLICATION_REPOSITORY = "app.repository";
}
