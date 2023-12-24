/*
 * @(#)UserServiceImpl.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.service;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.lang3.ArrayUtils;
import org.nlh4j.core.context.profiles.SpringProfilesEnum;
import org.nlh4j.core.dto.UserDetails;
import org.nlh4j.core.service.AbstractService;
import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.DateUtils;
import org.nlh4j.util.EncryptUtils;
import org.nlh4j.util.StringUtils;
import org.nlh4j.web.core.domain.dao.RoleDao;
import org.nlh4j.web.core.domain.dao.UserDao;
import org.nlh4j.web.core.domain.dao.UserOnlineDao;
import org.nlh4j.web.core.domain.entity.User;
import org.nlh4j.web.core.domain.entity.UserEx;
import org.nlh4j.web.core.domain.entity.UserOnline;
import org.nlh4j.web.core.dto.RoleDto;
import org.nlh4j.web.core.dto.UserDto;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Getter;
import lombok.Setter;

/**
 * The implement of {@link UserService} interface
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Service(value = "userService")
@Transactional
@Singleton
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserServiceImpl extends AbstractService implements UserService {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * {@link LdapTemplate}
     */
    @Getter
    @Setter
    private LdapTemplate ldapTemplate;

    /**
     * {@link Environment}
     */
    @Inject
    private Environment environment;
    /**
     * {@link UserDao}
     */
    @Inject
    private UserDao userDao;
    /**
     * {@link RoleDao}
     */
    @Inject
    private RoleDao roleDao;
    /**
     * {@link UserOnlineDao}
     */
    @Inject
    private UserOnlineDao useronlineDao;

    /* (Non-Javadoc)
     * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
     */
    @Override
    @Deprecated
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        throw new UnsupportedOperationException();
    }
    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.UserService#loadUserByTokens(java.lang.String, java.lang.String[])
     */
    @Override
    @Transactional
    public UserDetails loadUserByTokens(String username, String... tokens) throws UsernameNotFoundException {
        // TODO Waiting for loading user from LDAP
        // check from LDAP
        UserDto udto = this.parseLdapProfile(username);
        if (udto != null) return udto;
        // detect token
        String companyCode = null;
        if (!ArrayUtils.isEmpty(tokens) && tokens.length >= 1) {
            companyCode = tokens[0];
        }
        // check from database
        logger.debug("Token company code: '" + (!StringUtils.hasText(companyCode) ? "NULL" : companyCode) + "'");
        return this.tranformUser(
        		userDao.findByUserName(username),
        		DateUtils.currentTimestamp());
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.UserService#login(java.lang.String, java.lang.Object, boolean)
     */
    @Override
    @Transactional
    public UserDetails login(String username, Object credentials, boolean encrypted) {
        // parse password
        String password = null;
        if (BeanUtils.isInstanceOf(credentials, String[].class)
                && !ArrayUtils.isEmpty((String[]) credentials)
                && ((String[]) credentials).length > 0) {
            password = ((String[]) credentials)[0];
        }
        else {
            password = (credentials == null ? null : String.valueOf(credentials));
        }

        // check parameters
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            logger.error("Invalid user name or credentials!");
            return null;
        }

        // TODO Waiting for LDAP authentication
        // check from LDAP
        UserDto udto = this.parseLdapProfile(username);
        UserEx user = null;
        if (udto == null) {
            // check from database
            user = userDao.findByUserName(username);
            // Check the user is existing
            if (user == null) return null;

            // Check the passwords match
            String suppliedPasswordHash = (encrypted ? password : EncryptUtils.md5(password));
            if (!StringUtils.hasText(suppliedPasswordHash)
                    || !suppliedPasswordHash.equals(user.getPassword())) {
                return null;
            }
        }
        // TODO Waiting for LDAP authentication
        else {
            try {
                user = BeanUtils.copyBean(udto, UserEx.class);
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        }

        // transform user information
        return this.tranformUser(user, null);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.UserService#logout(java.util.List)
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    public int logout(List<UserDetails> users) {
        int effected = 0;
        if (!CollectionUtils.isEmpty(users)) {
            List<String> loggedUserNames = new LinkedList<String>();
            for (UserDetails user : users) {
                if (user != null && user.getUsername() != null) {
                    loggedUserNames.add(user.getUsername());
                }
            }
            if (!CollectionUtils.isEmpty(loggedUserNames)) {
                effected = this.useronlineDao.logoutByUserNames(loggedUserNames);
                // notification for all users
                this.broadcastOnlineUsers();
            }
        }
        return effected;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.service.UserService#logout(org.nlh4j.core.dto.UserDetails[])
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    public int logout(UserDetails... users) {
        List<UserDetails> usersLst = new LinkedList<UserDetails>();
        if (!ArrayUtils.isEmpty(users)) {
            for (UserDetails user : users) {
                usersLst.add(user);
            }
        }
        return this.logout(usersLst);
    }

    /**
     * Broadcast to the online users
     */
    @Override
    @Transactional
    public void broadcastOnlineUsers() {
        // check socket profile
        boolean isSocket = (this.useronlineDao != null
        		&& SpringProfilesEnum.isProfile(
        				SpringProfilesEnum.SOCKET_ONLINE, this.environment));
        if (!isSocket) return;

        // notification for all online users
        List<UserDto> onlUsers = null;
        try {
            onlUsers = BeanUtils.copyBeansList(
                    userDao.findOnlineUsers(),
                    UserDto.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            onlUsers = null;
        }
        if (!CollectionUtils.isEmpty(onlUsers)) {
            messageService.sendMessage("/onlinelist", onlUsers);
        }
    }

    /**
     * Parse {@link UserDto} from {@link UserEx}
     *
     * @param user {@link UserEx} to parse
     * @param loginAt the logged-in date/time
     *
     * @return {@link UserDto}
     */
    @Transactional
    private UserDto parseProfile(UserEx user, Timestamp loginAt) {
        UserDto udto = null;
        if (user != null) {
            // Create user profile
            Set<RoleDto> roles = new LinkedHashSet<RoleDto>();

            // prepare profile
            try {
                udto = BeanUtils.copyBean(user, UserDto.class);
            }
            catch (Exception e) {
                logger.error(e.getMessage(), e);
                udto = null;
            }

            // if valid; then loading user roles
            if (udto != null) {
                udto.setLoginAt(loginAt);
                if (!udto.isSysadmin()) {
                    try {
                        roles.addAll(this.roleDao.findRoles(user.getUsername()));
                    } catch (Exception e) {
                        roles.clear();
                    }
                }
                udto.setRoles(roles);
            }
        }
        return udto;
    }
    /**
     * Parse {@link UserDto} from LDAP
     * TODO Waiting for synchronizing LDAP user with database (onlined users, user name, password, etc.)
     *
     * @param user {@link User} to parse
     *
     * @return {@link UserDto}
     */
    @Transactional
    private UserDto parseLdapProfile(String username) {
        // check from LDAP
        UserDto udto = null;
        if (this.getLdapTemplate() == null) return udto;
        List<UserDto> udtoLst = this.getLdapTemplate().search(
                LdapQueryBuilder.query()
                .where("objectclass").is("user")
                .and(LdapQueryBuilder.query()
                        .where("cn").is(username)
                        .or("sn").is(username)
                ),
                new UserDto());
        if (!CollectionUtils.isEmpty(udtoLst)) {
            udto = udtoLst.get(0);
            udto.setLoginAt(DateUtils.currentTimestamp());
        }
        return udto;
    }
    /**
     * Transform {@link UserEx} information to SPRING {@link UserDetails} principal
     *
     * @param user to transform
     * @param loginAt the logged-in date/time
     *
     * @return the SPRING {@link UserDetails} principal or NULL
     */
    @Transactional
    private UserDetails tranformUser(UserEx user, Timestamp loginAt) throws UsernameNotFoundException {
    	// check parameter
    	if (user == null || !StringUtils.hasText(user.getUsername())
    			|| !StringUtils.hasText(user.getPassword())) {
    		if (user == null || !StringUtils.hasText(user.getUsername())) {
    			throw new UsernameNotFoundException("User name must not be empty");
    		} else {
	    		throw new UsernameNotFoundException(
	                    MessageFormat.format("{0} has not found from database", user.getUsername()));
    		}
    	}

        // Turns on user online status
        boolean isSocket = (this.useronlineDao != null
        		&& SpringProfilesEnum.isProfile(
        				SpringProfilesEnum.SOCKET_ONLINE, this.environment));
        if (isSocket) {
            UserOnline online = this.useronlineDao.findOnlineUser(user.getId());
            int onlineEff = 0;
            if (online != null) {
                onlineEff = this.useronlineDao.update(online);
            }
            else {
                online = new UserOnline();
                online.setLoginAt(DateUtils.currentTimestamp());
                online.setUid(user.getId());
                onlineEff = this.useronlineDao.insert(online);
            }
            // Save data be failed
            if (onlineEff == 0) return null;
            // Create user profile
            return this.parseProfile(user,
            		(loginAt == null ? online.getLoginAt() : loginAt));
        }
        else {
            // Create user profile
            return this.parseProfile(user,
            		(loginAt == null ? DateUtils.currentTimestamp() : loginAt));
        }
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.web.core.service.UserService#changePassword(org.nlh4j.core.dto.UserDetails, java.lang.String)
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    public UserDto changePassword(UserDto user, String newPassword) throws Exception {
        // detect current user
        UserDto udto = AuthenticationUtils.getCurrentProfile(UserDto.class);
        if (udto == null || user == null
                || !StringUtils.hasText(user.getUsername())
                || !StringUtils.hasText(newPassword)) return null;
        // load user by user name
        UserEx dbUser = this.userDao.findByUserName(user.getUsername());
        User usr = BeanUtils.copyBean(dbUser, User.class);
        usr.setPassword(EncryptUtils.md5(newPassword));
        dbUser.setPassword(usr.getPassword());
        usr.setUpdatedAt(DateUtils.currentTimestamp());
        usr.setUpdatedUser(udto.getId());
        // update password
        if (this.userDao.update(usr) > 0) {
            udto = this.parseProfile(dbUser, dbUser.getLoginAt());
        }
        else {
            udto = null;
        }
        return udto;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.web.core.service.UserService#updateLocale(java.lang.Long, java.util.Locale)
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    public boolean updateLocale(Long uid, Locale locale, Long updater) {
        String language = (locale == null ? null : locale.getLanguage());
        return (this.userDao.updateLanguage(uid, language, updater) > 0);
    }

	/* (Non-Javadoc)
	 * @see org.nlh4j.core.service.UserService#findProfile(java.lang.Long)
	 */
	@Override
	@Transactional
    public UserDetails findProfile(Long uid) throws UsernameNotFoundException {
		try {
			// check user from database
			UserEx user = BeanUtils.copyBean(this.userDao.selectByEnabledId(uid), UserEx.class);
			if (user == null) {
				throw new UsernameNotFoundException(
	                    MessageFormat.format("{0} has not found from database",
	                    		String.valueOf(uid == null ? 0 : uid)));
			}
			// transform information
			return this.tranformUser(user, user.getLoginAt());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new UsernameNotFoundException(
                    MessageFormat.format("{0} has not found from database",
                    		String.valueOf(uid == null ? 0 : uid)));
		}
	}
}
