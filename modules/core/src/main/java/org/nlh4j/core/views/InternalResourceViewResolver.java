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
import org.springframework.web.servlet.view.JstlView;

/**
 * Custom {@link org.springframework.web.servlet.view.InternalResourceViewResolver} by cloning code.
 */
public class InternalResourceViewResolver extends AbstractUrlBasedViewResolver {

	/** */
	private static final long serialVersionUID = 1L;
	
	private static final String DEFAULT_VIEW_SUFFIX_JSP = ".jsp";

	private static final boolean jstlPresent = ClassUtils.isPresent(
			"javax.servlet.jsp.jstl.core.Config", InternalResourceViewResolver.class.getClassLoader());

	@Nullable
	private Boolean alwaysInclude;

	/**
	 * Sets the default {@link #setViewClass view class} to {@link #requiredViewClass}:
	 * by default {@link InternalResourceView}, or {@link JstlView} if the JSTL API
	 * is present.
	 */
	public InternalResourceViewResolver() {
		Class<?> viewClass = requiredViewClass();
		if (InternalResourceView.class == viewClass && jstlPresent) {
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
		return (getViewClass() == InternalResourceView.class ? new InternalResourceView() :
				(getViewClass() == JstlView.class ? new JstlView() : super.instantiateView()));
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
