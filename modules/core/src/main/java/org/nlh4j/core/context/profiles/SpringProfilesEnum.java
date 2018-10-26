/*
 * @(#)SpringProfile.java 1.0 Jan 8, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.context.profiles;

import java.util.LinkedList;
import java.util.List;

import org.springframework.core.env.Environment;

import org.nlh4j.core.dto.AbstractEnum;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.StringUtils;

/**
 * Spring context profiles enumeration
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class SpringProfilesEnum extends AbstractEnum {

    /** */
    private static final long serialVersionUID = 1L;

	/** "default" profile */
	public static final SpringProfilesEnum DEFAULT =
			new SpringProfilesEnum(SpringProfiles.PROFILE_DEFAULT);
	/** "simple" profile */
	public static final SpringProfilesEnum SIMPLE =
			new SpringProfilesEnum(SpringProfiles.PROFILE_SIMPLE);
	/** "quartz" profile */
	public static final SpringProfilesEnum QUARTZ =
			new SpringProfilesEnum(SpringProfiles.PROFILE_QUARTZ);
	/** "mail" profile */
	public static final SpringProfilesEnum MAIL =
			new SpringProfilesEnum(SpringProfiles.PROFILE_MAIL);
	/** "template" profile */
	public static final SpringProfilesEnum TEMPLATE =
			new SpringProfilesEnum(SpringProfiles.PROFILE_TEMPLATE);
	/** "socket" profile for chat,notification,online,queue */
	public static final SpringProfilesEnum SOCKET =
			new SpringProfilesEnum(SpringProfiles.PROFILE_SOCKET);
	/** "socket" profile for chat */
	public static final SpringProfilesEnum SOCKET_CHAT =
			new SpringProfilesEnum(SpringProfiles.PROFILE_SOCKET_CHAT);
	/** "socket" profile for notification */
	public static final SpringProfilesEnum SOCKET_NOTIFICATION =
			new SpringProfilesEnum(SpringProfiles.PROFILE_SOCKET_NOTIFICATION);
	/** "socket" profile for online */
	public static final SpringProfilesEnum SOCKET_ONLINE =
			new SpringProfilesEnum(SpringProfiles.PROFILE_SOCKET_ONLINE);
	/** "socket" profile for queue */
	public static final SpringProfilesEnum SOCKET_QUEUE =
			new SpringProfilesEnum(SpringProfiles.PROFILE_SOCKET_QUEUE);
	/** "full" profile */
	public static final SpringProfilesEnum FULL =
			new SpringProfilesEnum(SpringProfiles.PROFILE_FULL);

	/**
	 * Initialize a new instance of {@link SpringProfilesEnum}
	 *
	 * @param value profile value
	 */
	private SpringProfilesEnum(String value) {
		super(value);
	}

	/**
	 * Check the specified profiles list whether contain "default"/"simple" or null
	 *
	 * @param profiles to check
	 *
	 * @return true for "default"/"simple" or null profile; else false
	 */
	public static boolean isDefault(String...profiles) {
		boolean ok = false;
		if (!CollectionUtils.isEmpty(profiles)) {
			for(String profile : profiles) {
				SpringProfilesEnum profileEnum = BeanUtils.safeType(
						AbstractEnum.fromCode(SpringProfilesEnum.class, profile),
						SpringProfilesEnum.class);
				ok = (!StringUtils.hasText(profile) || profileEnum == null
						|| AbstractEnum.NONE.equals(profileEnum)
						|| SpringProfilesEnum.DEFAULT.equals(profileEnum)
						|| SpringProfilesEnum.SIMPLE.equals(profileEnum));
				if (ok) break;
			}
		}
		return ok;
	}
	/**
	 * Check the specified profiles list whether contain "quartz"
	 *
	 * @param profiles to check
	 *
	 * @return true for "quartz" profile; else false
	 */
	public static boolean isQuartz(String...profiles) {
		boolean ok = false;
		if (!CollectionUtils.isEmpty(profiles)) {
			for(String profile : profiles) {
				SpringProfilesEnum profileEnum = BeanUtils.safeType(
						AbstractEnum.fromCode(SpringProfilesEnum.class, profile),
						SpringProfilesEnum.class);
				ok = (profileEnum != null
						&& (SpringProfilesEnum.QUARTZ.equals(profileEnum)
								|| SpringProfilesEnum.FULL.equals(profileEnum)));
				if (ok) break;
			}
		}
		return ok;
	}
	/**
	 * Check the specified profiles list whether contain "mail"
	 *
	 * @param profiles to check
	 *
	 * @return true for "mail" profile; else false
	 */
	public static boolean isMail(String...profiles) {
		boolean ok = false;
		if (!CollectionUtils.isEmpty(profiles)) {
			for(String profile : profiles) {
				SpringProfilesEnum profileEnum = BeanUtils.safeType(
						AbstractEnum.fromCode(SpringProfilesEnum.class, profile),
						SpringProfilesEnum.class);
				ok = (profileEnum != null
						&& (SpringProfilesEnum.MAIL.equals(profileEnum)
								|| SpringProfilesEnum.FULL.equals(profileEnum)));
				if (ok) break;
			}
		}
		return ok;
	}
	/**
	 * Check the specified profiles list whether contain "template"
	 *
	 * @param profiles to check
	 *
	 * @return true for "template" profile; else false
	 */
	public static boolean isTemplate(String...profiles) {
		boolean ok = false;
		if (!CollectionUtils.isEmpty(profiles)) {
			for(String profile : profiles) {
				SpringProfilesEnum profileEnum = BeanUtils.safeType(
						AbstractEnum.fromCode(SpringProfilesEnum.class, profile),
						SpringProfilesEnum.class);
				ok = (profileEnum != null
						&& (SpringProfilesEnum.TEMPLATE.equals(profileEnum)
								|| SpringProfilesEnum.FULL.equals(profileEnum)));
				if (ok) break;
			}
		}
		return ok;
	}
	/**
	 * Check the specified profiles list whether contain "socket"
	 *
	 * @param profiles to check
	 *
	 * @return true for "socket" profile; else false
	 */
	public static boolean isSocket(String...profiles) {
		boolean ok = false;
		if (!CollectionUtils.isEmpty(profiles)) {
			for(String profile : profiles) {
				SpringProfilesEnum profileEnum = BeanUtils.safeType(
						AbstractEnum.fromCode(SpringProfilesEnum.class, profile),
						SpringProfilesEnum.class);
				ok = (profileEnum != null
						&& (SpringProfilesEnum.SOCKET.equals(profileEnum)
								|| SpringProfilesEnum.FULL.equals(profileEnum)));
				if (ok) break;
			}
		}
		return ok;
	}
	/**
	 * Check the specified profiles list whether contain "socket" for chat
	 *
	 * @param profiles to check
	 *
	 * @return true for "socket-chat" profile; else false
	 */
	public static boolean isSocketChat(String...profiles) {
		boolean ok = isSocket(profiles);
		if (ok) {
			ok = false;
			for(String profile : profiles) {
				SpringProfilesEnum profileEnum = BeanUtils.safeType(
						AbstractEnum.fromCode(SpringProfilesEnum.class, profile),
						SpringProfilesEnum.class);
				ok = (profileEnum != null
						&& (SpringProfilesEnum.SOCKET_CHAT.equals(profileEnum)
								|| SpringProfilesEnum.FULL.equals(profileEnum)));
				if (ok) break;
			}
		}
		return ok;
	}
	/**
	 * Check the specified profiles list whether contain "socket" for notification
	 *
	 * @param profiles to check
	 *
	 * @return true for "socket-notification" profile; else false
	 */
	public static boolean isSocketNotification(String...profiles) {
		boolean ok = isSocket(profiles);
		if (ok) {
			ok = false;
			for(String profile : profiles) {
				SpringProfilesEnum profileEnum = BeanUtils.safeType(
						AbstractEnum.fromCode(SpringProfilesEnum.class, profile),
						SpringProfilesEnum.class);
				ok = (profileEnum != null
						&& (SpringProfilesEnum.SOCKET_NOTIFICATION.equals(profileEnum)
								|| SpringProfilesEnum.FULL.equals(profileEnum)));
				if (ok) break;
			}
		}
		return ok;
	}
	/**
	 * Check the specified profiles list whether contain "socket" for online users list
	 *
	 * @param profiles to check
	 *
	 * @return true for "socket-online" profile; else false
	 */
	public static boolean isSocketOnline(String...profiles) {
		boolean ok = isSocket(profiles);
		if (ok) {
			ok = false;
			for(String profile : profiles) {
				SpringProfilesEnum profileEnum = BeanUtils.safeType(
						AbstractEnum.fromCode(SpringProfilesEnum.class, profile),
						SpringProfilesEnum.class);
				ok = (profileEnum != null
						&& (SpringProfilesEnum.SOCKET_ONLINE.equals(profileEnum)
								|| SpringProfilesEnum.FULL.equals(profileEnum)));
				if (ok) break;
			}
		}
		return ok;
	}
	/**
	 * Check the specified profiles list whether contain "socket" for system queue
	 *
	 * @param profiles to check
	 *
	 * @return true for "socket-queue" profile; else false
	 */
	public static boolean isSocketQueue(String...profiles) {
		boolean ok = isSocket(profiles);
		if (ok) {
			ok = false;
			for(String profile : profiles) {
				SpringProfilesEnum profileEnum = BeanUtils.safeType(
						AbstractEnum.fromCode(SpringProfilesEnum.class, profile),
						SpringProfilesEnum.class);
				ok = (profileEnum != null
						&& (SpringProfilesEnum.SOCKET_QUEUE.equals(profileEnum)
								|| SpringProfilesEnum.FULL.equals(profileEnum)));
				if (ok) break;
			}
		}
		return ok;
	}
	/**
	 * Check the specified profiles list whether contain "full"
	 *
	 * @param profiles to check
	 *
	 * @return true for "full" profile; else false
	 */
	public static boolean isFull(String...profiles) {
		boolean ok = false;
		if (!CollectionUtils.isEmpty(profiles)) {
			for(String profile : profiles) {
				SpringProfilesEnum profileEnum = BeanUtils.safeType(
						AbstractEnum.fromCode(SpringProfilesEnum.class, profile),
						SpringProfilesEnum.class);
				ok = (profileEnum != null && SpringProfilesEnum.FULL.equals(profileEnum));
				if (ok) break;
			}
		}
		return ok;
	}

	/**
	 * Find the correct profile from the specified profiles
	 *
	 * @param cfgprofiles to check
	 *
	 * @return the correct profiles array or "default" (as simple or null) profiles if not found
	 */
	public static SpringProfilesEnum[] checkProfile(String...cfgprofiles) {
		List<SpringProfilesEnum> profiles = new LinkedList<SpringProfilesEnum>();
		// always default profiles
		profiles.add(SpringProfilesEnum.DEFAULT);
		profiles.add(SpringProfilesEnum.SIMPLE);
		if (!CollectionUtils.isEmpty(cfgprofiles)) {
			// check full (if full then it means all profiles)
			if (isFull(cfgprofiles)) {
				profiles.add(SpringProfilesEnum.QUARTZ);
				profiles.add(SpringProfilesEnum.MAIL);
				profiles.add(SpringProfilesEnum.TEMPLATE);
				profiles.add(SpringProfilesEnum.SOCKET);
				profiles.add(SpringProfilesEnum.SOCKET_CHAT);
				profiles.add(SpringProfilesEnum.SOCKET_NOTIFICATION);
				profiles.add(SpringProfilesEnum.SOCKET_ONLINE);
				profiles.add(SpringProfilesEnum.SOCKET_QUEUE);
				profiles.add(SpringProfilesEnum.FULL);
				return CollectionUtils.toArray(profiles, SpringProfilesEnum.class);
			}

			// check quartz
			if (isQuartz(cfgprofiles)) {
				profiles.add(SpringProfilesEnum.QUARTZ);
			}

			// check mail
			if (isMail(cfgprofiles)) {
				profiles.add(SpringProfilesEnum.MAIL);
			}

			// check template
			if (isTemplate(cfgprofiles)) {
				profiles.add(SpringProfilesEnum.TEMPLATE);
			}

			// check socket
			if (isSocket(cfgprofiles)) {
				profiles.add(SpringProfilesEnum.SOCKET);
			}

			// check socket for chat
			if (isSocketChat(cfgprofiles)) {
				profiles.add(SpringProfilesEnum.SOCKET_CHAT);
			}

			// check socket for notification
			if (isSocketNotification(cfgprofiles)) {
				profiles.add(SpringProfilesEnum.SOCKET_NOTIFICATION);
			}

			// check socket for online
			if (isSocketOnline(cfgprofiles)) {
				profiles.add(SpringProfilesEnum.SOCKET_ONLINE);
			}

			// check socket for queue
			if (isSocketQueue(cfgprofiles)) {
				profiles.add(SpringProfilesEnum.SOCKET_QUEUE);
			}
		}
		return CollectionUtils.toArray(profiles, SpringProfilesEnum.class);
	}
	/**
	 * Find the correct profiles array from the enviroment
	 *
	 * @param env to check (parse configured profiles)
	 *
	 * @return the correct profiles array or "default" (as simple or null) profiles if not found
	 */
	public static SpringProfilesEnum[] checkProfile(Environment env) {
		SpringProfilesEnum[] profiles = null;
		if (env != null) {
			String[] cfgprofiles = env.getActiveProfiles();
			// default profiles if not found active profiles
			cfgprofiles = (CollectionUtils.isEmpty(cfgprofiles) ? env.getDefaultProfiles() : cfgprofiles);
			profiles = checkProfile(cfgprofiles);
		}
		return (profiles == null
				? new SpringProfilesEnum[] {
						SpringProfilesEnum.DEFAULT
						, SpringProfilesEnum.SIMPLE
				} : profiles);
	}

	/**
	 * Check the specified profile whether is valid in the specified profiles array
	 *
	 * @param cfgprofiles to check
	 * @param profile to check. NULL/empty for checking as "default" (or simple) profile
	 *
	 * @return true if valid profile; else false
	 */
	public static boolean isProfile(SpringProfilesEnum profile, String...cfgprofiles) {
		SpringProfilesEnum tmp = (profile == null ? SpringProfilesEnum.DEFAULT : profile);
		SpringProfilesEnum[] profiles = checkProfile(cfgprofiles);
		return (CollectionUtils.indexOf(profiles, tmp) >= 0);
	}
	/**
	 * Check the specified profile whether is valid in the specified enviroment
	 *
	 * @param env to check (parse configured profiles)
	 * @param profile to check. NULL/empty for checking as "default" (or simple) profile
	 *
	 * @return true if valid profile; else false
	 */
	public static boolean isProfile(SpringProfilesEnum profile, Environment env) {
		boolean ok = false;
		if (env != null) {
			String[] cfgprofiles = env.getActiveProfiles();
			// default profiles if not found active profiles
			cfgprofiles = (CollectionUtils.isEmpty(cfgprofiles) ? env.getDefaultProfiles() : cfgprofiles);
			ok = isProfile(profile, cfgprofiles);
		}
		return ok;
	}
	/**
	 * Check the specified profile whether is valid in the specified profiles array
	 *
	 * @param cfgprofiles to check
	 * @param profile to check. NULL/empty for checking as "default" (or simple) profile
	 *
	 * @return true if valid profile; else false
	 */
	public static boolean isProfile(String profile, String...cfgprofiles) {
		SpringProfilesEnum tmp = BeanUtils.safeType(
				fromCode(SpringProfilesEnum.class, profile),
				SpringProfilesEnum.class);
		return isProfile(tmp, cfgprofiles);
	}
	/**
	 * Check the specified profile whether is valid in the specified enviroment
	 *
	 * @param env to check (parse configured profiles)
	 * @param profile to check. NULL/empty for checking as "default" (or simple) profile
	 *
	 * @return true if valid profile; else false
	 */
	public static boolean isProfile(String profile, Environment env) {
		SpringProfilesEnum tmp = BeanUtils.safeType(
				fromCode(SpringProfilesEnum.class, profile),
				SpringProfilesEnum.class);
		return isProfile(tmp, env);
	}
}
