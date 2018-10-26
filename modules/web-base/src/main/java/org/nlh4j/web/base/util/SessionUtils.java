/*
 * @(#)SessionUtils.java 1.0 Mar 2, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.nlh4j.core.dto.UserDetails;
import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.web.base.dashboard.dto.SidebarDto;
import org.nlh4j.web.core.dto.UserDto;

/**
 * {@link HttpSession} utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@SuppressWarnings("unchecked")
public final class SessionUtils implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    private static final String SESSION_SIDEBAR = "SESSION_SIDEBAR";

    /**
     * Save the {@link SidebarDto} modules list into session
     *
     * @param sidebarModules the {@link SidebarDto} modules list to save
     */
    public static void setSidebarSession(List<SidebarDto> sidebarModules) {
        org.nlh4j.util.SessionUtils.setSessionValue(SESSION_SIDEBAR, sidebarModules);
    }
    /**
     * Get the {@link SidebarDto} modules list from session
     *
     * @return the {@link SidebarDto} modules list from session
     */
    public static Collection<SidebarDto> getSidebarModules() {
        Collection<SidebarDto> sidebar = null;
        Object sessVal = org.nlh4j.util.SessionUtils.getSessionValue(SESSION_SIDEBAR);
        if (CollectionUtils.isCollectionOf(sessVal, SidebarDto.class)) {
            sidebar = (Collection<SidebarDto>) sessVal;
        }
        return sidebar;
    }

    /**
     * Get the selected {@link UserDetails} logged-in identity
     *
     * @return the selected {@link UserDetails} logged-in identity
     */
    public static Long getUid() {
        UserDto user = AuthenticationUtils.getCurrentProfile(UserDto.class);
        return (user == null ? 0L : user.getId());
    }
    /**
     * Get the selected {@link UserDetails} logged-in name
     *
     * @return the selected {@link UserDetails} logged-in name
     */
    public static String getUsername() {
        UserDetails user = AuthenticationUtils.getCurrentProfile();
        return (user == null ? null : user.getUsername());
    }

    /**
     * Get a boolean value indicating that the current user whether is SYSTEM ADMINISTRATOR
     *
     * @return true for SYSTEM ADMINISTRATOR; else false
     */
    public static boolean isSystemAdmin() {
        UserDto udto = AuthenticationUtils.getCurrentProfile(UserDto.class);
        return (udto != null && udto.isSystemAdmin());
    }
}
