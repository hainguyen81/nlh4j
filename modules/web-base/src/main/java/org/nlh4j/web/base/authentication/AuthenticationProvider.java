/*
 * @(#)AuthenticationProvider.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.authentication;

import java.util.List;

import javax.inject.Inject;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import org.nlh4j.core.authentication.Nlh4jAuthenticationProvider;
import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.web.base.dashboard.dto.SidebarDto;
import org.nlh4j.web.base.dashboard.service.DashboardService;
import org.nlh4j.web.base.util.SessionUtils;
import org.nlh4j.web.base.util.SidebarUtils;
import org.nlh4j.web.core.dto.UserDto;
import org.nlh4j.web.core.service.ModuleService;

/**
 * An extended class of {@link Nlh4jAuthenticationProvider}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Component(value = "authenticationProvider")
public class AuthenticationProvider extends Nlh4jAuthenticationProvider {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * {@link ModuleService}
     */
    @Inject
    protected ModuleService moduleService;
    /**
     * {@link DashboardService}
     */
    @Inject
    protected DashboardService dashboardService;

    /* (Non-Javadoc)
     * @see org.nlh4j.core.authentication.Nlh4jAuthenticationProvider#afterAuthenticate(org.springframework.security.core.Authentication)
     */
    @Override
    protected void afterAuthenticate(Authentication authentication) {
    	Assert.notNull(authentication, "authentication");
    	// detect authentication
        UserDto udto = AuthenticationUtils.getProfile(authentication, UserDto.class);
        // save side-bar modules list into session
        SessionUtils.setSidebarSession(this.getSidebarModules(udto.getUsername()));
    }

    /**
     * Get the {@link SidebarDto} modules list
     *
     * @param userName user name
     *
     * @return the {@link SidebarDto} modules list
     */
    protected final List<SidebarDto> getSidebarModules(String userName) {
        return SidebarUtils.parseSidebarData(this.moduleService.getUIModulesList(userName, null, null));
    }
}
