/*
 * @(#)WebMvcConfiguration.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.servlet;

import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import lombok.Setter;

/**
 * An extended class of {@link WebMvcConfigurationSupport}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
// @Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    /** {@link ContentNegotiationManager} */
    @Setter
    private ContentNegotiationManager contentNegotiationManager;

    /**
     * Initialize a new instance of {@link WebMvcConfiguration}
     *
     * @param contentNegotiationManager {@link ContentNegotiationManager}
     */
    public WebMvcConfiguration(ContentNegotiationManager contentNegotiationManager) {
        this.contentNegotiationManager = contentNegotiationManager;
    }

    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#createRequestMappingHandlerMapping()
     */
    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new org.nlh4j.core.handlers.RequestMappingHandlerMapping();
    }

    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#mvcContentNegotiationManager()
     */
    @Override
    public ContentNegotiationManager mvcContentNegotiationManager() {
        return (this.contentNegotiationManager == null
                ? super.mvcContentNegotiationManager() : this.contentNegotiationManager);
    }
}
