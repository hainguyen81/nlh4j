/*
 * @(#)TagUtils.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.taglibs;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import org.nlh4j.core.context.profiles.SpringProfilesEnum;
import org.nlh4j.util.LocaleUtils;
import org.nlh4j.util.RequestUtils;

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
     * {@link Environment}
     */
    private static Environment environment;
    /**
     * Get the configuration enviroment
     * @return the configuration enviroment
     */
    private static Environment getEnviroment() {
    	environment = (environment == null ? RequestUtils.getEnviroment() : environment);
    	if (environment == null) return null;
    	synchronized(environment) {
    		return environment;
    	}
    }

    /**
     * Check the specified object whether existed in the specified list
     *
     * @param list the list to check
     * @param o the value to check
     *
     * @return true for existed; else false
     */
    public static boolean contains(List<Object> list, Object o) {
        return (!CollectionUtils.isEmpty(list) && list.contains(o));
	}
    /**
     * Check the specified object whether existed in the specified list
     *
     * @param list the list to check
     * @param objs the values to check
     *
     * @return true for existed; else false
     */
    public static boolean containsAtLeast(List<Object> list, Object...objs) {
    	boolean contained = false;
    	if (!CollectionUtils.isEmpty(list) && !ArrayUtils.isEmpty(objs)) {
    		for(Object o : objs) {
    			contained = list.contains(o);
    			if (contained) break;
    		}
    	}
        return contained;
	}
    /**
     * Get the index of the specified object in the specified list
     *
     * @param list the list to check
     * @param o the value to check
     *
     * @return index of object or -1 if not found
     */
    public static int indexOf(List<Object> list, Object o) {
        return (CollectionUtils.isEmpty(list) ? -1 : list.indexOf(o));
	}

    /**
     *
     * Format body attribute.
     *
     * @param id HTML element identity
     * @param module module name
     * @param controller controller name
     * @param ngPreInit ngPreInit function
     * @param ngInit ngInit function
     * @param cssClass CSS class
     *
     * @return angular HTML attribute
     */
    public static String angularBodyAttr(String id, String module, String controller, String ngPreInit, String ngInit, String cssClass) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.hasText(id)) {
            sb.append(" id=\"").append(id.trim()).append("\"");
        }
        if (StringUtils.hasText(module)) {
            sb.append(" ng-app=\"").append(module.trim()).append("\"");
        }
        if (StringUtils.hasText(controller)) {
            sb.append(" ng-controller=\"").append(controller.trim()).append("\"");
        }
        if (StringUtils.hasText(ngPreInit)) {
            sb.append(" ng-preinit=\"").append(ngPreInit.trim()).append("\"");
        }
        if (StringUtils.hasText(ngInit)) {
            sb.append(" ng-init=\"").append(ngInit.trim()).append("\"");
        }
        if (StringUtils.hasText(cssClass)) {
            sb.append(" class=\"").append(cssClass.trim()).append("\"");
        }
        return sb.toString();
    }

    /**
     * concatenates all the elements of an array into a string
     *
     * @param sep separator
     * @param args objects to concatenate
     *
     * @return the concatenated string
     */
    public static String concat(String sep, Object...args) {
        StringBuffer joined = new StringBuffer();
        if (!ArrayUtils.isEmpty(args)) {
            for(Object arg : args) {
                if (joined.length() > 0) joined.append(sep);
                if (arg == null) {
                    joined.append("");
                }
                else {
                    joined.append(arg.toString());
                }
            }
        }
        return joined.toString();
    }

    /**
     * Get the display language of the specified language
     *
     * @param language language to check
     *
     * @return the display language
     */
    public static String getDisplayLanguage(String language) {
        String display = "";
        if (StringUtils.hasText(language)) {
            Locale locale = LocaleUtils.toLocale(language);
            if (locale != null) {
                display = locale.getDisplayLanguage();
            }
        }
        return display;
    }

    /**
     * Check the specified profile whether is valid from configuration eviroment
     *
     * @param profile to check
     *
     * @return true for valid; else false
     */
    public static boolean isProfile(String profile) {
        return SpringProfilesEnum.isProfile(profile, getEnviroment());
    }
    /**
     * Check active profile from configuration enviroment whether is "default" (or simple or null) profile
     *
     * @return true for "default" (or simple or null); else false
     */
    public static boolean isDefaultProfile() {
        return (SpringProfilesEnum.isProfile(SpringProfilesEnum.DEFAULT, getEnviroment())
                || SpringProfilesEnum.isProfile(SpringProfilesEnum.SIMPLE, getEnviroment()));
    }
    /**
     * Check active profile from configuration enviroment whether is "quartz" profile
     *
     * @return true for "quartz"; else false
     */
    public static boolean isQuartzProfile() {
        return SpringProfilesEnum.isProfile(SpringProfilesEnum.QUARTZ, getEnviroment());
    }
    /**
     * Check active profile from configuration enviroment whether is "mail" profile
     *
     * @return true for "mail"; else false
     */
    public static boolean isMailProfile() {
        return SpringProfilesEnum.isProfile(SpringProfilesEnum.MAIL, getEnviroment());
    }
    /**
     * Check active profile from configuration enviroment whether is "template" profile
     *
     * @return true for "template"; else false
     */
    public static boolean isTemplateProfile() {
        return SpringProfilesEnum.isProfile(SpringProfilesEnum.TEMPLATE, getEnviroment());
    }
    /**
     * Check active profile from configuration enviroment whether is "socket" profile
     *
     * @return true for "socket"; else false
     */
    public static boolean isSocketProfile() {
        return SpringProfilesEnum.isProfile(SpringProfilesEnum.SOCKET, getEnviroment());
    }
    /**
     * Check active profile from configuration enviroment whether is "socket-chat" profile
     *
     * @return true for "socket-chat"; else false
     */
    public static boolean isSocketChatProfile() {
        return SpringProfilesEnum.isProfile(SpringProfilesEnum.SOCKET_CHAT, getEnviroment());
    }
    /**
     * Check active profile from configuration enviroment whether is "socket-notification" profile
     *
     * @return true for "socket-notification"; else false
     */
    public static boolean isSocketNotificationProfile() {
        return SpringProfilesEnum.isProfile(SpringProfilesEnum.SOCKET_NOTIFICATION, getEnviroment());
    }
    /**
     * Check active profile from configuration enviroment whether is "socket-online" profile
     *
     * @return true for "socket-online"; else false
     */
    public static boolean isSocketOnlineProfile() {
        return SpringProfilesEnum.isProfile(SpringProfilesEnum.SOCKET_ONLINE, getEnviroment());
    }
    /**
     * Check active profile from configuration enviroment whether is "socket-queue" profile
     *
     * @return true for "socket-queue"; else false
     */
    public static boolean isSocketQueueProfile() {
        return SpringProfilesEnum.isProfile(SpringProfilesEnum.SOCKET_QUEUE, getEnviroment());
    }
    /**
     * Check active profile from configuration enviroment whether is "full" profile
     *
     * @return true for "full"; else false
     */
    public static boolean isFullProfile() {
        return SpringProfilesEnum.isProfile(SpringProfilesEnum.FULL, getEnviroment());
    }
}
