/*
 * @(#)ModulePermission.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Execution permission annotation
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ExecutePermission {
	/**
	 * permission value such as module/function name, module/function code, etc.
	 *
	 * @return module/function names that need to check permissions
	 */
	public String[] value() default {};
}
