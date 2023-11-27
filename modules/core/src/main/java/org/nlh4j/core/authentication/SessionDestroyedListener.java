/*
 * @(#)SessionDestroyedListener.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.util.CollectionUtils;

import org.nlh4j.core.dto.UserDetails;
import org.nlh4j.core.service.UserService;
import org.nlh4j.core.util.AuthenticationUtils;

/**
 * Session destroyed listener
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class SessionDestroyedListener implements ApplicationListener<SessionDestroyedEvent>, Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * SLF4J
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * {@link UserService}
	 */
    @Inject
	protected UserService userService;

	/*
	 * (Non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(SessionDestroyedEvent event) {
		List<SecurityContext> securityContexts = event.getSecurityContexts();
		if (!CollectionUtils.isEmpty(securityContexts)) {
			List<UserDetails> loggedUsers = new LinkedList<UserDetails>();
	        for (SecurityContext securityContext : securityContexts) {
	        	Authentication authentication = securityContext.getAuthentication();
	        	UserDetails user = AuthenticationUtils.getProfile(authentication);
	        	if (user != null) loggedUsers.add(user);
	        }
	        try { userService.logout(loggedUsers); }
	        catch (Exception e) {}
		}
	}
}
