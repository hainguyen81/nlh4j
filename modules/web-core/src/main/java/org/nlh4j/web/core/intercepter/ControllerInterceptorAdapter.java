/*
 * @(#)ControllerInterceptorAdapter.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.intercepter;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * WEB controller intercepter adapter
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Aspect
@Component(value = "controllerInterceptorAdapter")
public class ControllerInterceptorAdapter extends AbstractControllerInterceptorAdapter {

    /** */
    private static final long serialVersionUID = 1L;
}
