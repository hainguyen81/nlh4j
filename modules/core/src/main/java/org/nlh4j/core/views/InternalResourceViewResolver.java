/*
 * @(#)InternalResourceViewResolver.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.nlh4j.util.BeanUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceView;

/**
 * Custom {@link org.springframework.web.servlet.view.InternalResourceViewResolver} by cloning code.
 */
public class InternalResourceViewResolver extends AbstractUrlBasedViewResolver {

	/** */
	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_VIEW_SUFFIX_JSP = ".jsp";

	private static final boolean JSTL_REFRESH_SPRING = ClassUtils.isPresent(
            "javax.servlet.jsp.jstl.core.Config", org.springframework.web.servlet.view.InternalResourceViewResolver.class.getClassLoader());
	private static final boolean JSTL_REFRESH_NLH4J = ClassUtils.isPresent(
			"javax.servlet.jsp.jstl.core.Config", InternalResourceViewResolver.class.getClassLoader());
	private static final boolean JSTL_REFRESH = JSTL_REFRESH_SPRING || JSTL_REFRESH_NLH4J;

	@Nullable
	private Boolean alwaysInclude;

	/**
	 * Sets the default {@link #setViewClass view class} to {@link #requiredViewClass}:
	 * by default {@link InternalResourceView}, or {@link JstlView} if the JSTL API
	 * is present.
	 */
	public InternalResourceViewResolver() {
		Class<?> viewClass = requiredViewClass();
		if (InternalResourceView.class == viewClass && JSTL_REFRESH) {
			viewClass = JstlView.class;
		}
		setViewClass(viewClass);
	}

	/**
	 * A convenience constructor that allows for specifying {@link #setPrefix prefix}
	 * and {@link #setSuffix suffix} as constructor arguments.
	 * @param prefix the prefix that gets prepended to view names when building a URL
	 * @param suffix the suffix that gets appended to view names when building a URL
	 * @since 4.3
	 */
	public InternalResourceViewResolver(String prefix, String suffix) {
		this();
		setPrefix(prefix);
		setSuffix(suffix);
	}

	/**
	 * Specify whether to always include the view rather than forward to it.
	 * <p>Default is "false". Switch this flag on to enforce the use of a
	 * Servlet include, even if a forward would be possible.
	 * 
	 * @param alwaysInclude {@link InternalResourceView#setAlwaysInclude(boolean)}
	 * 
	 * @see InternalResourceView#setAlwaysInclude
	 */
	public void setAlwaysInclude(boolean alwaysInclude) {
		this.alwaysInclude = alwaysInclude;
	}

	@Override
	protected String getSuffix() {
		return Optional.ofNullable(super.getSuffix())
				.filter(StringUtils::isNotBlank).orElse(DEFAULT_VIEW_SUFFIX_JSP);
	}

	@Override
	protected Class<?> requiredViewClass() {
		return InternalResourceView.class;
	}

	@Override
	protected AbstractUrlBasedView instantiateView() {
		return Optional.ofNullable(getViewClass())
		        .filter(InternalResourceView.class::isAssignableFrom).map(clz -> new InternalResourceView())
		        .filter(AbstractUrlBasedView.class::isInstance).map(AbstractUrlBasedView.class::cast)
		        .orElseGet(() -> Optional.ofNullable(getViewClass())
                        .filter(JstlExposingBeansView.class::isAssignableFrom).map(clz -> new JstlExposingBeansView())
		                .filter(AbstractUrlBasedView.class::isInstance).map(AbstractUrlBasedView.class::cast)
		                .orElseGet(() -> Optional.ofNullable(getViewClass())
		                        .filter(JstlView.class::isAssignableFrom).map(clz -> new JstlView())
		                        .filter(AbstractUrlBasedView.class::isInstance).map(AbstractUrlBasedView.class::cast)
		                        .orElseGet(() -> Optional.ofNullable(getViewClass())
        		                        .filter(org.springframework.web.servlet.view.JstlView.class::isAssignableFrom).map(clz -> new JstlView())
        		                        .map(clz -> new org.springframework.web.servlet.view.JstlView())
        		                        .filter(AbstractUrlBasedView.class::isInstance).map(AbstractUrlBasedView.class::cast)
        		                        .orElseGet(super::instantiateView))));
	}

	@Override
	protected void customizeView(AbstractUrlBasedView view) {
		Optional.ofNullable(BeanUtils.safeType(view, InternalResourceView.class))
		.ifPresent(v -> {
			if (this.alwaysInclude != null) {
				v.setAlwaysInclude(this.alwaysInclude);
			}
			v.setPreventDispatchLoop(true);
		});
	}
}
