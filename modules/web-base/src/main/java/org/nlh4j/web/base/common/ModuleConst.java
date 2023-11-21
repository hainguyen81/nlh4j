/*
 * @(#)ModuleConst.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.common;

import java.io.Serializable;

/**
 * The module code constants
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class ModuleConst implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Common LOGIN modules
	 */
	public static final String CMN_LOGIN = "C00";

	/**
	 * Common SOCKET modules
	 */
	public static final String CMN_SPRING_SEC = "C01";
	public static final String CMN_SOCK = "C02";
	public static final String CMN_SOCK_WS = "C03";
	public static final String CMN_SOCK_CHAT = "C04";
	public static final String CMN_SOCK_USERS_ONLINE = "C05";
	/**
	 * Common security modules
	 */
	public static final String CMN_SEC_TOKEN = "C06";
	public static final String CMN_SEC_USERNAME = "C07";

	/**
	 * Common PERSONAL HOME modules
	 */
	public static final String CMN_HOME = "C08";

	/**
	 * Common PERSONAL TASK TODAY/NEXT modules (current user info)
	 */
	public static final String CMN_TASK_TODAY = "C09";
	public static final String CMN_TASK_NEXT = "C10";

	/**
	 * Common DASHBOARD modules
	 */
	public static final String CMN_DOWNLOAD_SERVICE = "C11";

    /**
     * Common CHANGE PASSWORD modules
     */
    public static final String CMN_CHANGE_PASSWORD = "C12";

    /**
     * Common LOGOUT modules
     */
    public static final String CMN_LOGOUT = "C13";

	/**
	 * Common DASHBOARD modules
	 */
	public static final String CMN_DASHBOARD = "C20";
	public static final String CMN_DASHBOARD_MASTER = "C21";
	public static final String CMN_DASHBOARD_SYSTEM = "C22";

    /**
     * System modules
     */
	public static final String SYSTEM_ROLE = "S01";
	public static final String SYSTEM_USER = "S02";
    public static final String SYSTEM_MODULE = "S04";
    public static final String SYSTEM_FUNCTION = "S05";
}
