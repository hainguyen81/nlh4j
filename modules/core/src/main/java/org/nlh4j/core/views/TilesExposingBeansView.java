/*
 * @(#)TilesExposingBeansView.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.nlh4j.util.CollectionUtils;
import org.springframework.web.context.support.ContextExposingHttpServletRequest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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
    @Getter(value = AccessLevel.PROTECTED)
    private boolean exposeContextBeansAsAttributes = false;
    /** specify exposing context bean names */
    @Setter
    @Getter(value = AccessLevel.PROTECTED)
    private Set<String> exposedContextBeanNames;
    /** specify exposing context bean names */
    @Setter
    @Getter(value = AccessLevel.PROTECTED)
    private String[] exposedContextBeanNamesArray;

    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.view.AbstractView#getRequestToExpose(javax.servlet.http.HttpServletRequest)
     */
    //@Override
    protected HttpServletRequest getRequestToExpose(HttpServletRequest originalRequest) {
        if (isExposeContextBeansAsAttributes() || !CollectionUtils.isEmpty(getExposedContextBeanNames())) {
            return new ContextExposingHttpServletRequest(
                    originalRequest, super.getWebApplicationContext(), getExposedContextBeanNames());

        } else if (isExposeContextBeansAsAttributes() || ArrayUtils.isNotEmpty(getExposedContextBeanNamesArray())) {
            return new ContextExposingHttpServletRequest(
                    originalRequest, super.getWebApplicationContext(),
                    CollectionUtils.toSet(getExposedContextBeanNamesArray()));
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
