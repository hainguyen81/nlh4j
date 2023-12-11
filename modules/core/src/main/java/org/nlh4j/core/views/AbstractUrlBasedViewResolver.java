/*
 * @(#)AbstractUrlBasedViewResolver.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.io.InputStream;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import com.machinezoo.noexception.Exceptions;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.util.ExceptionUtils;
import org.nlh4j.util.RequestUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom {@link UrlBasedViewResolver} for appTheme
 */
@Slf4j
public abstract class AbstractUrlBasedViewResolver extends UrlBasedViewResolver implements InitializingBean, DisposableBean, Serializable {

	/** */
	private static final long serialVersionUID = 1L;
	
	/** default SQL cache capacity */
    private static final int DEFAULT_CACHE_CAPACITY = 200;
    @Getter(value = AccessLevel.PROTECTED)
    private final ConcurrentMap<String, Set<String>> cacheResolvableBasedUrls = new ConcurrentHashMap<>(DEFAULT_CACHE_CAPACITY);
    @Getter(value = AccessLevel.PROTECTED)
	private final ConcurrentMap<String, AbstractUrlBasedView> cacheUrlBasedViews = new ConcurrentHashMap<>(DEFAULT_CACHE_CAPACITY);

	/** {@link SpringContextHelper} */
    @Inject
    private SpringContextHelper contextHelper;
    
    /**
     * Get the {@link SpringContextHelper} instance
     * 
     * @return the {@link SpringContextHelper} instance
     */
    protected final SpringContextHelper getContextHelper() {
    	if (contextHelper == null) {
    		contextHelper = new SpringContextHelper();
    	}
		return contextHelper;
	}

	@Getter
	@Setter
	private String appTheme;
	@Setter
	@Getter(value = AccessLevel.PROTECTED)
	private Set<String> additionalPrefixes;
	
	/**
	 * Specify whether should check the existing view resource on building view
	 */
	@Setter
	@Getter(value = AccessLevel.PROTECTED)
	private boolean checkResourceOnBuildView = true;
	
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
	 * @param viewUrl the URL of the view to build
	 * 
	 * @return the View {@link AbstractUrlBasedView} instance
	 * @throws Exception if the view couldn't be resolved
	 * @see #loadView(String, java.util.Locale)
	 */
	protected final AbstractUrlBasedView doBuildView(String viewUrl) throws Exception {
		// trace
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
	 * @return the View {@link AbstractUrlBasedView} instance
	 * @throws Exception if the view couldn't be resolved
	 * @see #loadView(String, java.util.Locale)
	 */
	protected final AbstractUrlBasedView doBuildView(String prefix, String appTheme, String viewName, String suffix) throws Exception {
		return doBuildView(buildViewUrl(prefix, appTheme, viewName, suffix));
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
			if (StringUtils.startsWith(val, slash ? "/" : ".")) {
				val = val.substring(1);
			}
			if (StringUtils.endsWith(val, slash ? "/" : ".")) {
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
					Optional.ofNullable(prefix).filter(StringUtils::isNotBlank)
					.map(s -> String.format("/%s", s)).orElse(StringUtils.EMPTY),
					// appTheme
					Optional.ofNullable(appTheme).filter(StringUtils::isNotBlank)
					.map(s -> String.format("/%s/", s)).orElse(StringUtils.EMPTY),
					// view name
					viewName,
					// suffix
					Optional.ofNullable(suffix).filter(StringUtils::isNotBlank)
					.map(s -> String.format(".%s", s)).orElse(StringUtils.EMPTY)
			);

			// not using appTheme or prefix already included appTheme
		} else {
			// 
			return String.format("%s%s%s",
					// prefix (included appTheme)
					Optional.ofNullable(prefix).filter(StringUtils::isNotBlank)
					.map(s -> String.format("/%s/", s)).orElse(StringUtils.EMPTY),
					// view name
					viewName,
					// suffix
					Optional.ofNullable(suffix).filter(StringUtils::isNotBlank)
					.map(s -> String.format(".%s", s)).orElse(StringUtils.EMPTY)
			);
		}
	}
	
	/**
	 * Build a set of view URL(s) based on the specified view name
	 * 
	 * @param viewName to build
	 * 
	 * @return a set of view URL(s)
	 */
	protected final Set<String> buildViewUrls(String viewName) {
		List<String> viewUrls = new LinkedList<>();

		// with appTheme (high priority on appTheme if settings)
		if (StringUtils.isNotBlank(getAppTheme())) {
			viewUrls.add(buildViewUrl(getPrefix(), getAppTheme(), viewName, getSuffix()));
			viewUrls.addAll(
					Optional.ofNullable(getAdditionalPrefixes()).orElseGet(LinkedHashSet::new)
					.parallelStream().filter(StringUtils::isNotBlank).map(StringUtils::trimToEmpty)
					.map(p -> buildViewUrl(p, getAppTheme(), viewName, getSuffix()))
					.collect(Collectors.toCollection(LinkedList::new)));
		}

		// without appTheme
		viewUrls.add(buildViewUrl(getPrefix(), null, viewName, getSuffix()));
		viewUrls.addAll(
				Optional.ofNullable(getAdditionalPrefixes()).orElseGet(LinkedHashSet::new)
				.parallelStream().filter(StringUtils::isNotBlank).map(StringUtils::trimToEmpty)
				.map(p -> buildViewUrl(p, null, viewName, getSuffix()))
				.collect(Collectors.toCollection(LinkedList::new)));

		// distinct
		return viewUrls.parallelStream().collect(Collectors.toCollection(LinkedHashSet::new));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected View loadView(String viewName, Locale locale) throws Exception {
		AbstractUrlBasedView view = buildView(viewName);
		View result = Optional.ofNullable(view).map(v -> applyLifecycleMethods(viewName, v)).orElse(null);
		return Optional.ofNullable(view)
				.filter(ExceptionUtils.wrap(log).predicate(
						Exceptions.wrap().predicate((AbstractUrlBasedView v) -> v.checkResource(locale))).orElse(false))
				.map(v -> result).orElse(null);
	}

	@Override
	protected final AbstractUrlBasedView buildView(String viewName) throws Exception {
		// build view url(s) w/o appTheme
		Set<String> viewUrls = getCacheResolvableBasedUrls().putIfAbsent(viewName, buildViewUrls(viewName));

		// check from cached views
		AbstractUrlBasedView view = viewUrls.parallelStream()
				.filter(url -> getCacheUrlBasedViews().containsKey(url))
				.findFirst().map(url -> getCacheUrlBasedViews().getOrDefault(url, null)).orElse(null);
		if (view == null) {
			// if need to check the existing resource on building view
			for(String viewUrl : viewUrls) {
				if (isCheckResourceOnBuildView()) {
					view = checkResource(viewUrl);

				} else {
					view = doBuildView(viewUrl);
				}
				if (view != null) {
					view = getCacheUrlBasedViews().putIfAbsent(viewUrl, view);
					break;
				}
			}
		}

		// view could be null, but waiting for rendering the view
		return view;
	}
	
	/**
	 * Get a boolean value indicating the specified view URL whether is valid resource for building
	 * 
	 * @param viewUrl to check
	 * 
	 * @return {@link AbstractUrlBasedView} for valid; else NULL
	 */
	protected AbstractUrlBasedView checkResource(final String viewUrl) {
		AbstractUrlBasedView view = ExceptionUtils.wrap(log).function(Exceptions.wrap().function((String url) -> doBuildView(url)))
				.apply(viewUrl).filter(Objects::nonNull).orElse(null);
		InputStream viewResourceStream = Optional.ofNullable(super.getServletContext())
				.map(ctx -> ctx.getResourceAsStream(viewUrl)).orElseGet(
						() -> getContextHelper().searchFirstResourceAsStream(viewUrl));
		boolean validView = viewResourceStream != null;
		if (!validView) {
			// try to check via the built view
			validView = Optional.ofNullable(view)
					// check resource by request locale
					.map(ExceptionUtils.wrap(log).function(Exceptions.wrap().function(v -> v.checkResource(RequestUtils.getRequestLocale()))))
					.filter(Optional::isPresent).map(Optional::get).orElse(Boolean.FALSE);
		}

		// tracing
		if (validView && view != null) {
			log.info("Found the resource for the view [{}] [appTheme: {}]", viewUrl, appTheme);

		} else if (validView && view == null && log.isDebugEnabled()) {
			log.debug("Found the resource for the view [{}] [appTheme: {}] but occuring exception while building view", viewUrl, appTheme);

		} else if (!validView && log.isDebugEnabled()) {
			log.debug("Could not found the resource for the view [{}] [appTheme: {}] or exception has been occurred while building view", viewUrl, appTheme);
		}
		return validView && view != null ? view : null;
	}

	@Override
	public final void destroy() throws Exception {
		doDestroy();
		getCacheUrlBasedViews().clear();
		getCacheResolvableBasedUrls().clear();
	}

	/**
	 * Override for releasing resources when destroying bean
	 * 
	 * @throws Exception if failed
	 */
	protected void doDestroy() throws Exception {
		// do nothing
	}
	
	@Override
	protected void initApplicationContext(ApplicationContext context) {
		Optional.ofNullable(context).ifPresent(c -> getContextHelper().setApplicationContext(c));
		super.initApplicationContext(context);
	}

	@Override
	protected void initServletContext(ServletContext servletContext) {
		Optional.ofNullable(servletContext).ifPresent(c -> getContextHelper().setServletContext(c));
		super.initServletContext(servletContext);
	}
}
