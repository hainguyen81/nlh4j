/*
 * @(#)TestReloadableResourceBundleMessageSource.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.context.properties;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.nlh4j.util.ClassLoaderUtils;
import org.nlh4j.util.LocaleUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

/**
 * Test class of {@link ReloadableResourceBundleMessageSource}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class TestReloadableResourceBundleMessageSource {

	@Test
	public void loadMessages() {
		String libsDir = "E:\\Java.Working\\20.eclispe.2022-06\\hrm-workspace\\hrm\\modules"
				+ "\\hrm-web\\modules\\hrm-app\\target\\hrm-app-1.0-jre11\\WEB-INF";
		ClassLoader cl = ClassLoaderUtils.createDirectoryLoader(libsDir);
		ResourceLoader rl = new DefaultResourceLoader(cl);
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setResourceLoader(rl);
		messageSource.setHighPriorityOnFirstLoad(false); // TODO reverse the order of base names on loading
		List<String> baseNames = Arrays.asList(
				"classpath*:**/properties/i18n/systemResources",
				"classpath*:properties/i18n/systemResources",
				"classpath*:**/i18n/systemResources",
				"classpath*:i18n/systemResources",
				"classpath*:**/systemRes",
				"classpath*:systemResources",

			    "classpath*:**/properties/i18n/baseResources",
				"classpath*:properties/i18n/baseResources",
				"classpath*:**/i18n/baseResources",
				"classpath*:i18n/baseResources",
				"classpath*:**/baseResources",
				"classpath*:baseResources",

				"classpath*:**/properties/i18n/ApplicationResources",
				"classpath*:**/properties/i18n/messages",
				"classpath*:properties/i18n/ApplicationResources",
				"classpath*:properties/i18n/messages",
				"classpath*:**/i18n/ApplicationResources",
				"classpath*:**/i18n/messages",
				"classpath*:i18n/ApplicationResources",
				"classpath*:i18n/messages",
				"classpath*:**/ApplicationResources",
				"classpath*:**/messages",
				"classpath*:ApplicationResources",
				"classpath*:messages",

				"classpath*:**/properties/i18n/moduleResources",
				"classpath*:properties/i18n/moduleResources",
				"classpath*:**/i18n/moduleResources",
				"classpath*:i18n/moduleResources",
				"classpath*:**/moduleResources",
				"classpath*:moduleResources",

				"classpath*:**/properties/i18n/hrmResources",
				"classpath*:**/properties/i18n/hrmMessages",
				"classpath*:properties/i18n/hrmResources",
				"classpath*:properties/i18n/hrmMessages",
				"classpath*:**/i18n/hrmResources",
				"classpath*:**/i18n/hrmMessages",
				"classpath*:i18n/hrmResources",
				"classpath*:i18n/hrmMessages",
				"classpath*:**/hrmResources",
				"classpath*:**/hrmMessages",
				"classpath*:hrmResources",
				"classpath*:hrmMessages",

				"classpath*:**/properties/i18n/systemValidationMessages",
				"classpath*:properties/i18n/systemValidationMessages",
				"classpath*:**/i18n/systemValidationMessages",
				"classpath*:i18n/systemValidationMessages",
				"classpath*:**/systemValidationMessages",
				"classpath*:systemValidationMessages",

				"classpath*:**/properties/i18n/baseValidationMessages",
				"classpath*:properties/i18n/baseValidationMessages",
				"classpath*:**/i18n/baseValidationMessages",
				"classpath*:i18n/baseValidationMessages",
				"classpath*:**/baseValidationMessages",
				"classpath*:baseValidationMessages",

				"classpath*:**/properties/i18n/validationMessages",
				"classpath*:properties/i18n/validationMessages",
				"classpath*:**/i18n/validationMessages",
				"classpath*:i18n/validationMessages",
				"classpath*:**/validationMessages",
				"classpath*:validationMessages",

				"classpath*:**/properties/i18n/moduleValidationMessages",
				"classpath*:properties/i18n/moduleValidationMessages",
				"classpath*:**/i18n/moduleValidationMessages",
				"classpath*:i18n/moduleValidationMessages",
				"classpath*:**/moduleValidationMessages",
				"classpath*:moduleValidationMessages",

				"classpath*:**/properties/i18n/hrmValidationMessages",
				"classpath*:properties/i18n/hrmValidationMessages",
				"classpath*:**/i18n/hrmValidationMessages",
				"classpath*:i18n/hrmValidationMessages",
				"classpath*:**/hrmValidationMessages",
				"classpath*:hrmValidationMessages"
		);
		messageSource.setBasenames(baseNames.toArray(new String[0]));
		
		String messageKey = "application.content.title";
		System.out.println(String.format("------- [%s] -------", messageKey));
		String message = messageSource.getMessage(messageKey, new Object[0], LocaleUtils.toLocale("vi"));
		System.out.println(message);
		assertEquals("<b>Human Resources Management</b> SYSTEM", message);
		
		messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setResourceLoader(rl);
		messageSource.setHighPriorityOnFirstLoad(true); // TODO keep the order of base names on loading
		messageSource.setBasenames(baseNames.toArray(new String[0]));

		messageKey = "application.content.title";
		System.out.println(String.format("------- [%s] -------", messageKey));
		message = messageSource.getMessage(messageKey, new Object[0], LocaleUtils.toLocale("vi"));
		System.out.println(message);
		assertEquals("<b>NLH4J</b> SYSTEM", message);
	}
}
