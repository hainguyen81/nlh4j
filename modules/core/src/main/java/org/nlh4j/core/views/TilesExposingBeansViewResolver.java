/*
 * @(#)TilesExposingBeansViewResolver.java 1.0 Jan 12, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.io.Serializable;
import java.util.Set;

import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import lombok.Setter;
import org.nlh4j.util.CollectionUtils;

/**
 * An extended class of {@link UrlBasedViewResolver} for exposing context beans to view.<br>
 * Spring framework verion &gt; 4.0.6.RELEASE will be support
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class TilesExposingBeansViewResolver extends UrlBasedViewResolver implements Serializable {

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
     * @see org.springframework.web.servlet.view.UrlBasedViewResolver#buildView(java.lang.String)
     */
    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        AbstractUrlBasedView superView = super.buildView(viewName);
        if (superView instanceof TilesExposingBeansView) {
            TilesExposingBeansView view = (TilesExposingBeansView) superView;
            // expose as attributes
            if (this.exposeContextBeansAsAttributes) {
                view.setExposeContextBeansAsAttributes(this.exposeContextBeansAsAttributes);
            }
            // expose bean names
            if (!CollectionUtils.isEmpty(this.exposedContextBeanNames)) {
                view.setExposedContextBeanNames(this.exposedContextBeanNames);
            } else if (!CollectionUtils.isEmpty(this.exposedContextBeanNames)) {
                view.setExposedContextBeanNamesArray(this.exposedContextBeanNamesArray);
            }
        }
        return superView;
    }
}
