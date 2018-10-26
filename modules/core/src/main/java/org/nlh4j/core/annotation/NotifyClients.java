/*
 * @(#)NotifyClients.java 1.0 Mar 12, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to notify clients via socket
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NotifyClients {
	/**
	 * topic to notify
	 *
	 * @return topic to notify
	 */
	public String[] value() default {};
}
