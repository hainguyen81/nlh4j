/*
 * @(#)SpringProfiles.java 1.0 Jan 8, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.context.profiles;

import java.io.Serializable;

/**
 * Spring context profiles constant
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class SpringProfiles implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;

	/** "default" profile */
	public static final String PROFILE_DEFAULT = "default";
	/** "simple" profile */
	public static final String PROFILE_SIMPLE = "simple";
	/** "quartz" profile */
	public static final String PROFILE_QUARTZ = "quartz";
	/** "mail" profile */
	public static final String PROFILE_MAIL = "mail";
	/** "template" profile */
	public static final String PROFILE_TEMPLATE = "template";
	/** "socket" profile for chat,notification,online,queue */
	public static final String PROFILE_SOCKET = "socket";
	/** "socket" profile for chat */
	public static final String PROFILE_SOCKET_CHAT = "socket-chat";
	/** "socket" profile for notification */
	public static final String PROFILE_SOCKET_NOTIFICATION = "socket-notification";
	/** "socket" profile for notification */
	public static final String PROFILE_SOCKET_ONLINE = "socket-online";
	/** "socket" profile for queue */
	public static final String PROFILE_SOCKET_QUEUE = "socket-queue";
	/** "full" profile */
	public static final String PROFILE_FULL = "full";
}
