/*
 * @(#)AbstractUrlBasedViewResolver.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom {@link UrlBasedViewResolver} for appTheme
 */
@Slf4j
public abstract class AbstractUrlBasedViewResolver extends UrlBasedViewResolver implements DisposableBean, Serializable {

	/** */
	private static final long serialVersionUID = 1L;
	
	/** default SQL cache capacity */
    private static final int DEFAULT_CACHE_CAPACITY = 200;
	protected final ConcurrentMap<String, AbstractUrlBasedView> cacheUrlBasedView = new ConcurrentHashMap<>(DEFAULT_CACHE_CAPACITY);

	@Value("${app.theme:}")
	@Getter
	@Setter
	private String appTheme;
	
	/**
	 * Creates a new View instance of the specified view class and configures it.
	 * Does <i>not</i> perform any lookup for pre-defined View instances.
	 * <p>Spring lifecycle methods as defined by the bean container do not have to
	 * be called here; those will be applied by the {@code loadView} method
	 * after this method returns.
	 * <p>Subclasses will typically call {@code super.buildView(viewName)}
	 * first, before setting further properties themselves. {@code loadView}
	 * will then apply Spring lifecycle methods at the end of this process.
	 * 
	 * @param viewName the name of the view to build
	 * @param prefix the URL prefix
	 * @param suffix the view suffix extension
	 * @param appTheme view by theme
	 * 
	 * @return the View instance
	 * @throws Exception if the view couldn't be resolved
	 * @see #loadView(String, java.util.Locale)
	 */
	protected final AbstractUrlBasedView doBuildView(String prefix, String appTheme, String viewName, String suffix) throws Exception {
		String viewUrl = buildViewUrl(prefix, appTheme, viewName, suffix);
		log.info("Build view URL: [{}]", viewUrl);

		AbstractUrlBasedView view = instantiateView();
		view.setUrl(viewUrl);
		view.setAttributesMap(getAttributesMap());

		String contentType = getContentType();
		if (contentType != null) {
			view.setContentType(contentType);
		}

		String requestContextAttribute = getRequestContextAttribute();
		if (requestContextAttribute != null) {
			view.setRequestContextAttribute(requestContextAttribute);
		}

		Boolean exposePathVariables = getExposePathVariables();
		if (exposePathVariables != null) {
			view.setExposePathVariables(exposePathVariables);
		}
		Boolean exposeContextBeansAsAttributes = getExposeContextBeansAsAttributes();
		if (exposeContextBeansAsAttributes != null) {
			view.setExposeContextBeansAsAttributes(exposeContextBeansAsAttributes);
		}
		String[] exposedContextBeanNames = getExposedContextBeanNames();
		if (exposedContextBeanNames != null) {
			view.setExposedContextBeanNames(exposedContextBeanNames);
		}

		return view;
	}
	
	/**
	 * Child class override this method for customizing the built {@link AbstractUrlBasedView}
	 * 
	 * @param view to customize
	 */
	protected abstract void customizeView(AbstractUrlBasedView view);
	
	/**
	 * Make the specified string to `/&lt;val&gt;` or `.&lt;val&gt;`
	 * 
	 * @param val to parse
	 * @param slash true for making `/`; else false for `.`
	 * 
	 * @return `/&lt;val&gt;` or `.&lt;val&gt;` or empty if NULL/BLANK
	 */
	protected static String strip(String val, boolean slash) {
		val = StringUtils.trimToEmpty(val);
		if (StringUtils.isNotBlank(val)) {
			if (!StringUtils.startsWith(val, slash ? "/" : ".")) {
				val = String.format(slash ? "/%s" : ".%s", val);
			}
			if (slash && StringUtils.endsWith(val, "/")) {
				val = val.substring(0, val.length() - 1);
			}
		}
		return val;
	}
	/**
	 * Support for building the view URL
	 * 
	 * @param viewName the name of the view to build
	 * @param prefix the URL prefix
	 * @param suffix the view suffix extension
	 * @param appTheme view by theme
	 * 
	 * @return the view URL
	 */
	protected static String buildViewUrl(String prefix, String appTheme, String viewName, String suffix) {
		// trip slash values
		prefix = strip(prefix, true);
		appTheme = strip(appTheme, true);
		viewName = strip(viewName, true);
		suffix = Optional.ofNullable(strip(suffix, false)).filter(StringUtils::isNotBlank).orElse(StringUtils.EMPTY);

		// prefix wasn't included appTheme
		if (StringUtils.isNotBlank(appTheme) && !StringUtils.endsWith(prefix, appTheme)) {
			return String.format("%s%s%s%s",
					// prefix (included appTheme)
					prefix,
					// appTheme
					appTheme,
					// view name
					viewName,
					// suffix
					suffix
			);

			// not using appTheme or prefix already included appTheme
		} else {
			// 
			return String.format("%s%s%s",
					// prefix (included appTheme)
					prefix,
					// view name
					viewName,
					// suffix
					suffix
			);
		}
	}

	@Override
	protected final AbstractUrlBasedView buildView(String viewName) throws Exception {
		// appTheme has been specified case
		AbstractUrlBasedView view = null;
		if (StringUtils.isNotBlank(getAppTheme())) {
			// build/check view URL by appTheme
			String viewUrl = buildViewUrl(getPrefix(), getAppTheme(), viewName, getSuffix());
			view = cacheUrlBasedView.getOrDefault(viewUrl, null);
			if (view == null) {
				InputStream viewResourceStream = Optional.ofNullable(super.getServletContext())
						.map(ctx -> ctx.getResourceAsStream(viewUrl)).orElse(null);
				// invalid resource
				if (viewResourceStream == null) {
					if (log.isDebugEnabled()) {
						log.warn("Could not find the view [{}] by appTheme [{}]", viewUrl, appTheme);
					}
		
					/**
					 * return the view that has been excluded the appTheme<br>
					 * for using NLH4J core view. if view doesn't exist, it will throw exception automatically
					 */
					view = doBuildView(getPrefix(), null, viewName, getSuffix());
	
					// valid resource; so building view by appTheme
				} else {
					view = doBuildView(getPrefix(), getAppTheme(), viewName, getSuffix());
				}
			}

			// not specify appTheme
		} else {
			// build view without appTheme
			view = doBuildView(getPrefix(), null, viewName, getSuffix());
		}

		// cache the view
		Optional.ofNullable(view).ifPresent(v -> cacheUrlBasedView.putIfAbsent(v.getUrl(), v));
		return view;
	}

	@Override
	public void destroy() throws Exception {
		cacheUrlBasedView.clear();
	}
}
