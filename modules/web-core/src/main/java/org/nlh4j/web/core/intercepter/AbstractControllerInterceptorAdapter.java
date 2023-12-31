/*
 * @(#)AbstractControllerInterceptorAdapter.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.intercepter;

import javax.inject.Inject;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * WEB controller intercepter adapter
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Configuration
@Aspect
@Component
public abstract class AbstractControllerInterceptorAdapter extends org.nlh4j.core.intercepter.ControllerInterceptorAdapter {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * {@link ControllerInterceptorAdapterService}
     */
    @Inject
    @Getter(value = AccessLevel.PROTECTED)
    private ControllerInterceptorAdapterService controllerInterceptorAdapterService;

    /*
     * (Non-Javadoc)
     * @see org.nlh4j.common.intercepter.MethodInterceptor#checkInvoke(java.lang.Class, java.lang.String, java.lang.Object[])
     */
    @Override
    protected boolean checkInvoke(Class<?> targetClass, String method, Object[] medArgs) {
        return controllerInterceptorAdapterService.checkInvoke(targetClass, method, medArgs);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.intercepter.ControllerInterceptorAdapter#checkForward(java.lang.Class, java.lang.String, java.lang.Object[])
     */
    @Override
    protected String checkForward(Class<?> targetClass, String method, Object[] medArgs) {
    	return controllerInterceptorAdapterService.checkForward(targetClass, method, medArgs);
    }

    /*
     * (Non-Javadoc)
     * @see org.nlh4j.common.intercepter.MethodInterceptor#afterInvoke(java.lang.Class, java.lang.String, java.lang.Object[], java.lang.Object)
     */
    @Override
    protected void afterInvoke(Class<?> targetClass, String method, Object[] medArgs, Object value) {
        controllerInterceptorAdapterService.afterInvoke(targetClass, method, medArgs, value);
    }
}
