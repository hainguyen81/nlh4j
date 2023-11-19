/*
 * @(#)SessionUtils.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * {@link HttpSession} utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class SessionUtils implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Gets the current application session
     *
     * @param create specify whether creating session
     *
     * @return the current application session
     */
    public static HttpSession requireHttpSession(boolean create) {
    	HttpServletRequest req = RequestUtils.getHttpRequest();
        return (req == null ? null : req.getSession(create));
    }
    /**
     * Gets the current application session
     *
     * @return the current application session
     */
    public static HttpSession getHttpSession() {
        return requireHttpSession(false);
    }

    /**
     * Sets the specified <code>value</code> into session by the session variable key <code>name</code>
     *
     * @param name
     *      the session variable key
     * @param value
     *      the session variable's value
     * @param force
     *      specify creating session if empty
     */
    public static void setSessionValue(String name, Object value, boolean force) {
        HttpSession session = requireHttpSession(force);
        if (session != null) {
            try { session.setAttribute(name, value); }
            catch (Exception e) {
            	LogUtils.logError(SessionUtils.class, e.getMessage());
            }
        }
    }
    /**
     * Sets the specified <code>value</code> into session by the session variable key <code>name</code>
     *
     * @param name
     *      the session variable key
     * @param value
     *      the session variable's value
     */
    public static void setSessionValue(String name, Object value) {
        setSessionValue(name, value, Boolean.TRUE);
    }
    /**
     * Gets the session variable's value by the specified session variable key <code>name</code>
     *
     * @param name the session variable key for parsing value
     *
     * @return the session variable's value by the specified session variable key
     */
    public static Object getSessionValue(String name) {
        Object sessVal = null;
        HttpSession session = getHttpSession();
        if (session != null) {
            try { sessVal = session.getAttribute(name); }
            catch (Exception e) {
            	LogUtils.logError(SessionUtils.class, e.getMessage());
                sessVal = null;
            }
        }
        return sessVal;
    }
    /**
     * Gets the session variable's value by the specified session variable key <code>name</code>
     *
     * @param <T> entity type
     * @param name the session variable key for parsing value
     * @param valueClzz entity class
     *
     * @return the session variable's value by the specified session variable key
     */
    public static <T> T getSessionValue(String name, Class<T> valueClzz) {
        Object sessVal = getSessionValue(name);
        return BeanUtils.safeType(sessVal, valueClzz);
    }
    /**
     * Removes the session variable by the specified session variable key <code>name</code>
     *
     * @param name the session variable key for removing
     */
    public static void clearSessionValue(String name) {
        HttpSession session = getHttpSession();
        if (session != null) {
            try { session.removeAttribute(name); }
            catch (Exception e) {
            	LogUtils.logError(SessionUtils.class, e.getMessage());
            }
        }
    }
    /**
     * Removes all session variables
     */
    public static void clearSessionValues() {
        HttpSession session = getHttpSession();
        if (session != null) {
            Enumeration<String> enuSessAttrNames = session.getAttributeNames();
            while(enuSessAttrNames.hasMoreElements()) {
                clearSessionValue(enuSessAttrNames.nextElement());
            }
        }
    }

    /**
     * Returns the maximum time interval, in seconds, that the servlet container
     * will keep this session open between client accesses.
     * After this interval, the servlet container will invalidate the session.
     * A return value of zero or less indicates that the session will never timeout.
     *
     * @return the maximum time interval, in seconds.
     */
    public static int getSessionInterval() {
    	HttpSession session = getHttpSession();
    	return (session == null ? 0 : session.getMaxInactiveInterval());
    }

    /**
     * Get the session identity
     * @return the session identity
     */
    public static String getSessionId() {
        HttpSession session = getHttpSession();
        return (session == null ? null : session.getId());
    }
}
