/*
 * @(#)Nlh4jApplicationContextInitializer.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.context;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link ApplicationContextInitializer}
 */
@Slf4j
public abstract class AbstractApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Serializable {
	
	/** */
	private static final long serialVersionUID = 1L;

	/** {@link ConfigurableApplicationContext} */
	@Getter(value = AccessLevel.PROTECTED)
	@Setter(value = AccessLevel.PRIVATE)
	private ConfigurableApplicationContext applicationContext;
	
	/** {@link SpringContextHelper} */
	@Getter(value = AccessLevel.PROTECTED)
	@Setter(value = AccessLevel.PRIVATE)
	private SpringContextHelper contextHelper;

	/**
	 * Initialize a new instance of {@link ConfigurableApplicationContext}
	 */
	protected AbstractApplicationContextInitializer() {
		this.contextHelper = new SpringContextHelper();
	}
	
	/**
	 * Get the context configured parameter names
	 * 
	 * @return the context configured parameter names
	 */
	protected abstract String[] getContextParams();
	
	/**
	 * Get the {@link ConfigurableEnvironment} instance
	 * 
	 * @return the {@link ConfigurableEnvironment} instance
	 */
	protected final ConfigurableEnvironment getEnviroment() {
		return Optional.ofNullable(getApplicationContext())
				.map(ConfigurableApplicationContext::getEnvironment).orElse(null);
	}
	
	/**
	 * Get the {@link MutablePropertySources} instance
	 * 
	 * @return the {@link MutablePropertySources} instance
	 */
	protected final MutablePropertySources getPropertySources() {
		return Optional.ofNullable(getEnviroment())
				.map(ConfigurableEnvironment::getPropertySources).orElse(null);
	}

	@Override
    public final void initialize(final ConfigurableApplicationContext applicationContext) {
		StopWatch sw = new StopWatch();
		sw.start();
		log.info("-------------------------------------------------");
		log.info("START: Context Initialization");

		// stored context
		setApplicationContext(applicationContext);
		Optional.ofNullable(getApplicationContext())
		.ifPresent(ctx -> setContextHelper(new SpringContextHelper(getApplicationContext())));

		// initializing
		try {
			Map<String, String> contextParamsMap = Collections.unmodifiableMap(parseContextParameters());
			log.info("Configured context parameters: [{}]", contextParamsMap);
			doInitialize(contextParamsMap);
		} catch (Exception e) {
			log.warn("Could not initialize context: {}", e.getMessage(), e);
			throw new ApplicationRuntimeException(e);

		} finally {
			sw.stop();
			log.info("END: Context Initialization in {} ms", sw.getTime());
			log.info("-------------------------------------------------");
		}
	}
	
	/**
	 * Perform initializing
	 * 
	 * @param contextParamsMap
	 * 			  the context configured parameters
	 */
	protected abstract void doInitialize(Map<String, String> contextParamsMap);
	
	/**
	 * Parse the configured parameters
	 * 
	 * @return a map of configured parameters with key is a pair of parameter name/value
	 */
	protected Map<String, String> parseContextParameters() {
		return Stream.of(Optional.ofNullable(getContextParams()).orElseGet(() -> new String[0]))
				.parallel().filter(StringUtils::isNotBlank)
				.map(p -> new SimpleEntry<>(p, parseContextParam(p, StringUtils.EMPTY)))
				.collect(Collectors.toMap(Entry<String, String>::getKey, Entry<String, String>::getValue, (k1, k2) -> k1));
	}
	
	/**
	 * Parse the context parameters `propertiesLoadOrder`
	 * 
	 * @param paramName
	 * 			  parameter name to parse
	 * @param defaultValue
	 * 			  default value if not found or value is NULL/EMPTY/BLANK
	 * 
	 * @return parameter value
	 */
	protected String parseContextParam(String paramName, String defaultValue) {
		return Optional.ofNullable(getEnviroment()).map(env -> env.getProperty(paramName))
				.filter(StringUtils::isNotBlank).orElse(defaultValue);
	}
	
	/**
	 * Parse the context parameters `propertiesLoadOrder`
	 * 
	 * @param paramName
	 * 			  parameter name to parse
	 * 
	 * @return parameter value
	 */
	protected String parseContextParam(String paramName) {
		return parseContextParam(paramName, null);
	}
}
