/*
 * @(#)TilesExposingBeansViewResolver.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * An extended class of {@link AbstractUrlBasedViewResolver} for exposing context beans to view.<br>
 * Spring framework verion &gt; 4.0.6.RELEASE will be support
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class TilesExposingBeansViewResolver extends AbstractUrlBasedViewResolver {

    /** */
    private static final long serialVersionUID = 1L;
    /** specify exposing context beans as view attributes */
    @Setter
    @Getter(value = AccessLevel.PROTECTED)
    private boolean exposeContextBeansAsAttributes = false;
    /** specify exposing context bean names */
    @Setter
    @Getter(value = AccessLevel.PROTECTED)
    private Set<String> exposedContextBeanNamesSet;

    @Override
    protected AbstractUrlBasedView instantiateView() {
        return Optional.ofNullable(getViewClass())
                .filter(TilesExposingBeansView.class::isAssignableFrom).map(clz -> new TilesExposingBeansView())
                .filter(AbstractUrlBasedView.class::isInstance).map(AbstractUrlBasedView.class::cast)
                .orElseGet(() -> Optional.ofNullable(getViewClass())
                        .filter(TilesView.class::isAssignableFrom).map(clz -> new TilesView())
                        .filter(AbstractUrlBasedView.class::isInstance).map(AbstractUrlBasedView.class::cast)
                        .orElseGet(() -> Optional.ofNullable(getViewClass())
                                .filter(org.springframework.web.servlet.view.tiles3.TilesView.class::isAssignableFrom)
                                .map(clz -> new org.springframework.web.servlet.view.tiles3.TilesView())
                                .filter(AbstractUrlBasedView.class::isInstance).map(AbstractUrlBasedView.class::cast)
                                .orElseGet(super::instantiateView)));
    }

    @Override
    protected void customizeView(AbstractUrlBasedView view) {
    	Optional.ofNullable(BeanUtils.safeType(view, TilesExposingBeansView.class))
    	.ifPresent(v -> {
    		// expose as attributes
            if (isExposeContextBeansAsAttributes()) {
                v.setExposeContextBeansAsAttributes(isExposeContextBeansAsAttributes());
            }
            // expose bean names
            if (ArrayUtils.isNotEmpty(getExposedContextBeanNames())) {
                v.setExposedContextBeanNamesArray(getExposedContextBeanNames());

            } else if (!CollectionUtils.isEmpty(getExposedContextBeanNamesSet())) {
                v.setExposedContextBeanNames(getExposedContextBeanNamesSet());
            }
    	});
    }
}
