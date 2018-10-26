/*
 * @(#)TagUtils.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.util;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.nlh4j.web.base.dashboard.dto.SidebarDto;

/**
 * Function utilities for tag-lib
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class TagUtils implements Serializable {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;

	/** logger */
    protected static Logger logger = LoggerFactory.getLogger(TagUtils.class);

    /**
     * Get the {@link SidebarDto} modules list from session
     *
     * @return the {@link SidebarDto} modules list from session
     */
    public static Object getSidebarModules() {
        return SessionUtils.getSidebarModules();
    }

    /**
     * Get a boolean value indicating that the current user whether is SYSTEM ADMINISTRATOR
     *
     * @return true for SYSTEM ADMINISTRATOR; else false
     */
    public static boolean isSystemAdmin() {
        return SessionUtils.isSystemAdmin();
    }
}
