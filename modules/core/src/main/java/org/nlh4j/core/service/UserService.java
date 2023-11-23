/*
 * @(#)UserService.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.nlh4j.core.dto.UserDetails;

/**
 * User service
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public interface UserService extends UserDetailsService {
	/**
	 * Login user with password
	 *
	 * @param userName user name
	 * @param credentials user credentials
	 * @param encrypted specify the password from credentials whether has been encrypted
	 *
	 * @return user detail information or null if login fail
	 */
	UserDetails login(String userName, Object credentials, boolean encrypted);
	/**
	 * Log-out the specified users
	 *
	 * @param users users to log-out
	 *
	 * @return effected
	 */
	int logout(List<UserDetails> users);
	/**
	 * Log-out the specified users
	 *
	 * @param users users to log-out
	 *
	 * @return effected
	 */
	int logout(UserDetails...users);

    /**
     * Locates the user based on the username. In the actual implementation, the search may possibly be case
     * sensitive, or case insensitive depending on how the implementation instance is configured. In this case, the
     * <code>UserDetails</code> object that comes back may have a username that is of a different case than what was
     * actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @param tokens the more tokens from {@link UserDetails}.getRememberTokenSignatures()
     *
     * @return a fully populated user record (never <code>null</code>)
     *
     * @throws UsernameNotFoundException if the user could not be found or the user has no GrantedAuthority
     */
    UserDetails loadUserByTokens(String username, String...tokens) throws UsernameNotFoundException;
    /**
     * TODO This method had been deprecated. Instead of using {@link loadUserByTokens} method
     *
     * @param username user name
     *
     * @return user details
     */
    @Deprecated
    UserDetails loadUserByUsername(String username);
    /**
     * Load user profile {@link UserDetails} by identity (as logged-in profile)
     *
     * @param uid user identity
     *
     * @return the user profile {@link UserDetails} by identity (as logged-in profile)
     */
    UserDetails findProfile(Long uid) throws UsernameNotFoundException;
}
