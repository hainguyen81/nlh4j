/*
 * @(#)TilesExposingBeansView.java 1.0 Jan 12, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.ContextExposingHttpServletRequest;

import lombok.Setter;
import org.nlh4j.util.CollectionUtils;

/**
 * An extended class of {@link org.springframework.web.servlet.view.tiles3.TilesView} for exposing context beans
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class TilesExposingBeansView extends TilesView {

    /** */
    private static final long serialVersionUID = 1L;
    /** specify exposing context beans as view attributes */
    @Setter
    private boolean exposeContextBeansAsAttributes = false;
    /** specify exposing context bean names */
    @Setter
    private Set<String> exposedContextBeanNames;
    /** specify exposing context bean names */
    @Setter
    private String[] exposedContextBeanNamesArray;

    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.view.AbstractView#getRequestToExpose(javax.servlet.http.HttpServletRequest)
     */
    //@Override
    protected HttpServletRequest getRequestToExpose(HttpServletRequest originalRequest) {
        if (this.exposeContextBeansAsAttributes || !CollectionUtils.isEmpty(this.exposedContextBeanNames)) {
            return new ContextExposingHttpServletRequest(
                    originalRequest, super.getWebApplicationContext(),
                    this.exposedContextBeanNames);
        } else if (this.exposeContextBeansAsAttributes || !CollectionUtils.isEmpty(this.exposedContextBeanNamesArray)) {
            return new ContextExposingHttpServletRequest(
                    originalRequest, super.getWebApplicationContext(),
                    CollectionUtils.toSet(this.exposedContextBeanNamesArray));
        }
        return originalRequest;
    }

    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.view.tiles3.TilesView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequest requestToExpose = this.getRequestToExpose(request);
        super.exposeModelAsRequestAttributes(model, requestToExpose);
        super.renderMergedOutputModel(model, requestToExpose, response);
    }
}
