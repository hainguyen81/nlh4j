/*
 * @(#)ControllerInterceptorAdapter.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.intercepter;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.nlh4j.core.annotation.ExecutePermission;
import org.nlh4j.core.annotation.NotifyClients;
import org.nlh4j.core.service.MessageService;
import org.nlh4j.core.util.AuthenticationUtils;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.DateUtils;
import org.nlh4j.web.core.dto.UserDto;
import org.nlh4j.web.core.service.ModuleService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * WEB controller intercepter adapter
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Aspect
@Component(value = "controllerInterceptorAdapter")
@SuppressWarnings("unchecked")
public class ControllerInterceptorAdapter extends org.nlh4j.core.intercepter.ControllerInterceptorAdapter {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * {@link ModuleService}
     */
    @Inject
    private ModuleService moduleService;
    /**
     * {@link MessageService}
     */
    @Inject
    private MessageService messageService;

    /**
     * Check the specified method whether is {@link RequestMapping} handler method
     *
     * @param targetClass pointcut class
     * @param method pointcut method
     * @param medArgs pointcut method arguments
     *
     * @return true for request mapping; else false
     */
    private boolean isRequestMapping(Class<?> targetClass, String method, Object[] medArgs) {
    	// only intercept on RequestMapping methods
        List<? extends Annotation> medReqMap = BeanUtils.getMethodAnnotations(
                targetClass, method, RequestMapping.class);
        return !CollectionUtils.isEmpty(medReqMap);
    }
    /*
     * (Non-Javadoc)
     * @see org.nlh4j.common.intercepter.MethodInterceptor#checkInvoke(java.lang.Class, java.lang.String, java.lang.Object[])
     */
    @Override
    protected boolean checkInvoke(Class<?> targetClass, String method, Object[] medArgs) {
        // only intercept on RequestMapping methods
        if (!this.isRequestMapping(targetClass, method, medArgs)) return true;

        // check normal user permission (has permission on modules)
        UserDto userprofile = getInvokeMethodUser(medArgs);
        boolean allowed = (userprofile != null);
        if (allowed && !userprofile.isSysadmin()) {
            // check user has permission
            allowed = this.hasUserPermission(userprofile.getUsername(), targetClass, method);
        }
        // check root user (system administrator) permission (modules has been existed and enabled)
        else if (allowed) {
            // check if modules has been existed and enabled
            allowed = this.hasRootPermission(targetClass, method);
        }
        return allowed;
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.core.intercepter.ControllerInterceptorAdapter#checkForward(java.lang.Class, java.lang.String, java.lang.Object[])
     */
    @Override
    protected String checkForward(Class<?> targetClass, String method, Object[] medArgs) {
    	// only intercept on RequestMapping methods
        if (!this.isRequestMapping(targetClass, method, medArgs)) return null;

        // check if this forward module
        UserDto userprofile = getInvokeMethodUser(medArgs);
        if (userprofile != null) {
        	// parse target class permission info
            ExecutePermission medPerm = BeanUtils.getClassAnnotation(targetClass, ExecutePermission.class);
            if (medPerm != null && !CollectionUtils.isEmpty(medPerm.value())) {
            	List<String> fwUrls = moduleService.getModuleForwardUrls(
            			userprofile.getUsername(), medPerm.value());
            	if (!CollectionUtils.isEmpty(fwUrls)) {
            		return fwUrls.get(0);
            	}
            }
        }
        return null;
    }

    /*
     * (Non-Javadoc)
     * @see org.nlh4j.common.intercepter.MethodInterceptor#afterInvoke(java.lang.Class, java.lang.String, java.lang.Object[], java.lang.Object)
     */
    @Override
    protected void afterInvoke(Class<?> targetClass, String method, Object[] medArgs, Object value) {
        // if result is a model and view; then applying common attribute if necessary
        // at this moment, it means current user had the accessible permission with this module
        if (BeanUtils.isInstanceOf(value, ModelAndView.class)) {
            // parse class module information
            List<String> modList = new ArrayList<String>();
            List<String> funcList = new ArrayList<String>();
            ExecutePermission clzzPerm = BeanUtils.getClassAnnotation(targetClass, ExecutePermission.class);
            String[] modules = (clzzPerm != null && !ArrayUtils.isEmpty(clzzPerm.value()) ? clzzPerm.value() : new String[] {});
            if (!ArrayUtils.isEmpty(modules)) modList.addAll(Arrays.asList(modules));
            // parse method modules information
            List<? extends Annotation> medperms = BeanUtils.getMethodAnnotations(
                    targetClass, method, ExecutePermission.class);
            if (CollectionUtils.isCollectionOf(medperms, ExecutePermission.class)) {
                List<ExecutePermission> perms = (List<ExecutePermission>) medperms;
                for(ExecutePermission perm : perms) {
                    String[] medModules = (!ArrayUtils.isEmpty(perm.value()) ? perm.value() : new String[] {});
                    if (!ArrayUtils.isEmpty(medModules)) funcList.addAll(Arrays.asList(medModules));
                }
            }

            // parse module styles list
            List<String> cssModList = moduleService.getModuleStylesheets(modList);
            String moduleCss = (!CollectionUtils.isEmpty(cssModList) ? cssModList.get(0) : null);

            // update functions list by current user if not found (such as index page)
            UserDto userprofile = getInvokeMethodUser(medArgs);
            if (CollectionUtils.isEmpty(funcList)
                    && (userprofile == null || !userprofile.isSysadmin())) {
                Map<String, List<String>> functions = this.moduleService.getModuleFunctions(
                        (userprofile == null ? null : userprofile.getUsername()), modList);
                if (!CollectionUtils.isEmpty(functions)) {
                    for(final Iterator<String> it = functions.keySet().iterator(); it.hasNext();) {
                        String moduleCd = it.next();
                        List<String> funcCds = functions.get(moduleCd);
                        if (CollectionUtils.isEmpty(funcCds)) continue;
                        funcList.addAll(funcCds);
                    }
                }
            }

            // applies modules that user has writable permission
            ModelAndView mav = BeanUtils.safeType(value, ModelAndView.class);
            if (mav != null) {
	            mav.addObject("modules", modList);
	            mav.addObject("functions", funcList);
	            mav.addObject("functionsString", StringUtils.join(funcList, "|"));
	            mav.addObject("moduleClass", moduleCss);
	            mav.addObject("time", DateUtils.currentTimestamp().getTime());
            }

            // notify clients
            NotifyClients notify = BeanUtils.getMethodAnnotation(targetClass, method, NotifyClients.class);
            if (notify != null && messageService != null && !CollectionUtils.isEmpty(notify.value())) {
            	for(String topic : notify.value()) {
            		messageService.sendMessage(topic, DateUtils.currentTimestamp());
            	}
            }
        }
        else {
            logger.debug("Class: [" + targetClass.getName()
                    + "] - Method: [" + method
                    + "] - Value: [" + (value == null ? "NULL" : value.getClass().getName()) + "]");
        }
    }

    /**
     * Check permission of root user
     *
     * @param targetClass the target controller class
     * @param method the request handler method
     *
     * @return true for allowing; otherwise false
     */
    private boolean hasRootPermission(Class<?> targetClass, String method) {
        // check permission base on target class
        ExecutePermission clzzPerm = BeanUtils.getClassAnnotation(targetClass, ExecutePermission.class);
        String[] modules = (clzzPerm != null && !CollectionUtils.isEmpty(clzzPerm.value()) ? clzzPerm.value() : new String[] {});

        // check module whether has been enabled all or not any module neet to check
        return (ArrayUtils.isEmpty(modules) || moduleService.isEnabled(modules));
    }
    /**
     * Check permission of normal user
     *
     * @param userName the user code
     * @param targetClass the target controller class
     * @param method the request handler method
     *
     * @return true for allowing; otherwise false
     */
    private boolean hasUserPermission(String userName, Class<?> targetClass, String method) {
        // parse info to check
        boolean allowed = false;
        List<String> modules = new LinkedList<String>();
        List<String> functions = new LinkedList<String>();

        // parse target class permission info
        ExecutePermission medPerm = BeanUtils.getClassAnnotation(targetClass, ExecutePermission.class);
        allowed = (medPerm == null || CollectionUtils.isEmpty(medPerm.value()));
        if (!allowed) {
            modules.addAll(Arrays.asList(medPerm.value()));
        }
        else logger.debug(">>> Not require permission on [" + targetClass.getName() + "] controller");

        // parse method permission info
        if (!allowed) {
            ExecutePermission funcPerm = BeanUtils.getMethodAnnotation(targetClass, method, ExecutePermission.class);
            if (funcPerm != null && !CollectionUtils.isEmpty(funcPerm.value())) {
                functions.addAll(Arrays.asList(funcPerm.value()));
            }
            else logger.debug(">>> Not require permission on method [" + method + "] of [" + targetClass.getName() + "] controller");

            // check permissions
            String[] arrModules = (CollectionUtils.isEmpty(modules)
                    ? new String[] {} : modules.toArray(new String[modules.size()]));
            String[] arrFuncs = (CollectionUtils.isEmpty(functions)
                    ? new String[] {} : functions.toArray(new String[functions.size()]));

            // if require function permission; just checking modules has been enabled
            if (!CollectionUtils.isEmpty(functions)) {
                // if allowing; then checking modules whether existed and enabled
                allowed = moduleService.isEnabled(arrModules);
                if (!allowed) {
                    logger.warn(
                            "ACCESS DENIED - One of [" + StringUtils.join(arrModules, "|") + "] modules "
                            + "maybe disabled or not existed from database!!!");
                }

            } // else requiring function permission from database
            else {
                allowed = moduleService.hasPermission(userName, arrModules, arrFuncs);
                if (!allowed) {
                    logger.warn(
                            "ACCESS DENIED - [" + userName + "] hasnt enough [" + StringUtils.join(arrFuncs, "|") + "] "
                            + "permission on [" + StringUtils.join(arrModules, "|") + "] modules!!!");
                }
            }
        }
        return allowed;
    }
    
    /**
     * Get the user that has been invoked method
     * 
     * @param medArgs method argument to detect
     * 
     * @return {@link UserDto}/NULL
     */
    protected final UserDto getInvokeMethodUser(Object[] medArgs) {
    	// clear cache every action
        UserDto userprofile = AuthenticationUtils.getCurrentProfile(UserDto.class);
        // TODO Waiting to get authentication from spring socket
        // If current request is from socket, current authentication will be invalid
        if (userprofile == null && !ArrayUtils.isEmpty(medArgs)) {
            for(Object medArg : medArgs) {
                userprofile = AuthenticationUtils.getProfile(medArg, UserDto.class);
                if (userprofile != null) break;
            }
        }
        return userprofile;
    }
}
