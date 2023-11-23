/*
 * @(#)ApplicationUtils.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.common;

import java.io.Serializable;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

/**
 * Application utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class ApplicationUtils implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;
	/** logger */
    protected static Logger logger = LoggerFactory.getLogger(ApplicationUtils.class);

	public static String getLocaleLang() {
		Locale locale = LocaleContextHolder.getLocale();
		locale = (locale == null ? Locale.ENGLISH : locale);
		return locale.getLanguage();
	}
	public static boolean isEnglish() {
		return "en".equalsIgnoreCase(getLocaleLang());
	}
	public static boolean isJapanese() {
		return ("jp".equalsIgnoreCase(getLocaleLang())
		        || "ja".equalsIgnoreCase(getLocaleLang()));
	}
	public static boolean isVietnamese() {
		return "vi".equalsIgnoreCase(getLocaleLang());
	}
	public static String chooseByLocale(String viVal, String enVal, String jpVal) {
		String sVal = (StringUtils.hasText(viVal) ? viVal : StringUtils.hasText(enVal) ? enVal : jpVal);
		return (isVietnamese() && StringUtils.hasText(viVal) ? viVal
				: isEnglish() && StringUtils.hasText(enVal) ? enVal
						: isJapanese() && StringUtils.hasText(jpVal) ? jpVal
								: sVal);
	}
}
