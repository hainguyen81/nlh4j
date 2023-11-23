/*
 * @(#)BaseDto.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.nlh4j.core.service.MessageService;
import org.nlh4j.core.servlet.ApplicationContextProvider;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Base DTO
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractDto implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
	 * slf4j
	 */
	protected final transient Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
     * {@link SpringContextHelper}<br>
     * transient variable for serialization exception
     */
    private transient SpringContextHelper contextHelper;
    /**
     * Get the SPRING context helper
     * @return the SPRING context helper
     */
    protected final SpringContextHelper getContextHelper() {
    	if (contextHelper == null) {
            contextHelper = new SpringContextHelper(ApplicationContextProvider.getAwareApplicationContext());
        }
    	if (contextHelper == null) {
    		HttpServletRequest request = RequestUtils.getHttpRequest();
    		if (request != null) {
    			contextHelper = new SpringContextHelper(request.getServletContext());
    		}
        }
    	if (contextHelper == null) return null;
        synchronized (contextHelper) {
            return contextHelper;
        }
    }
    /**
     * Get bean by the specified bean class
     *
     * @param beanClass the bean class to parse from context
     *
     * @return bean or NULL
     */
    protected final <K> K findBean(Class<K> beanClass) {
        return (this.getContextHelper() == null ? null : this.getContextHelper().searchBean(beanClass));
    }
    /**
     * Get bean by the specified bean class
     *
     * @param beanClass the bean class to parse from context
     *
     * @return bean or NULL
     */
    protected final <K> List<K> findBeans(Class<K> beanClass) {
        return (this.getContextHelper() == null ? null : this.getContextHelper().searchBeans(beanClass));
    }

	/**
	 * @see MessageService
	 */
	protected final MessageService getMessageService() {
		return this.findBean(MessageService.class);
	};
}
