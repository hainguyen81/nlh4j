/*
 * @(#)Nlh4jApplicationContextInitializer.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.context;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;

import lombok.extern.slf4j.Slf4j;

/**
 * {@link ApplicationContextInitializer}
 */
@Slf4j
public class Nlh4jApplicationContextInitializer extends AbstractApplicationContextInitializer {
	
	/** */
	private static final long serialVersionUID = 1L;

	/** propertiesLoadOrder */
	private static final String PROPERTIES_LOAD_ORDER_FIRST = "FIRST";
	private static final String PROPERTIES_LOAD_ORDER_LAST = "LAST";
	private static final String PROPERTIES_LOAD_ORDER_CONTEXT_PARAM = "propertiesLoadOrder";

	/** propertiesLocations */
	private static final String PROPERTIES_CLASSPATH_PATTERN = "classpath:";
	private static final String PROPERTIES_CLASSPATH_WILDCARD_PATTERN = "classpath*:";
	private static final String PROPERTIES_LOCATIONS_CONTEXT_PARAM = "propertiesLocations";
	private static final String PROPERTIES_LOCATIONS_SEPARATORS = System.lineSeparator() + ",; ";
	
	@Override
	protected String[] getContextParams() {
		return new String[] {
				PROPERTIES_LOAD_ORDER_CONTEXT_PARAM,
				PROPERTIES_LOCATIONS_CONTEXT_PARAM
		};
	}
	
	@Override
	protected void doInitialize(Map<String, String> contextParamsMap) {
		final MutablePropertySources propertySources = getPropertySources();
		if (propertySources == null) {
			log.warn("Could not found mutable property sources from enviroment to push properties!");
			return;
		}

		String propertiesLoadOrder = Optional.ofNullable(contextParamsMap.getOrDefault(
				PROPERTIES_LOAD_ORDER_CONTEXT_PARAM, PROPERTIES_LOAD_ORDER_FIRST))
				.filter(StringUtils::isNotBlank).filter(p -> StringUtils.equalsIgnoreCase(PROPERTIES_LOAD_ORDER_LAST, p))
				.map(p -> PROPERTIES_LOAD_ORDER_LAST).orElse(PROPERTIES_LOAD_ORDER_FIRST);
		log.info("`{}`: {}", PROPERTIES_LOAD_ORDER_CONTEXT_PARAM, propertiesLoadOrder);
		String propertiesLocations = contextParamsMap.getOrDefault(PROPERTIES_LOCATIONS_CONTEXT_PARAM, null);
		List<String> propertiesLocationsList = Stream.of(
				Optional.ofNullable(StringUtils.split(propertiesLocations, PROPERTIES_LOCATIONS_SEPARATORS))
				.orElseGet(() -> new String[0])).parallel()
				.filter(StringUtils::isNotBlank).map(StringUtils::trimToEmpty)
				.collect(Collectors.toCollection(LinkedList::new));
		log.info("`{}`: {} - separate: [{}]", PROPERTIES_LOCATIONS_CONTEXT_PARAM, propertiesLocations, propertiesLocationsList);
		if (CollectionUtils.isNotEmpty(propertiesLocationsList)) {
			List<PropertySource<?>> resourcePropertySources = propertiesLocationsList.parallelStream()
					.map(propertyLocation -> loadPropertiesResources(propertyLocation))
					.filter(CollectionUtils::isNotEmpty).flatMap(List<PropertySource<?>>::stream)
					.collect(Collectors.toCollection(LinkedList::new));
			if (CollectionUtils.isNotEmpty(resourcePropertySources)) {
				switch(propertiesLoadOrder) {
					case PROPERTIES_LOAD_ORDER_LAST:
						resourcePropertySources.parallelStream().forEach(propertySources::addLast);
						break;

					default:
						Collections.reverse(resourcePropertySources);
						resourcePropertySources.parallelStream().forEach(propertySources::addFirst);
						break;
				}
				
			} else log.warn("Not found any valid `propertiesLocations` to load! Please re-check properties locations again!");

		} else log.warn("Not found the `propertiesLocations` context-param to load! Please configure it if necessary!");
	}
	
	/**
	 * Customize the {@link ConfigurableEnvironment} from the {@link ConfigurableApplicationContext}
	 */
	protected void customizeEnviroment() {
		Optional.ofNullable(getEnviroment()).ifPresent(env -> {
			env.setIgnoreUnresolvableNestedPlaceholders(true);
		});
	}
	
	/**
	 * Load properties {@link ResourcePropertySource}
	 * 
	 * @param propertiesLocation
	 * 			  to load
	 * 
	 * @return {@link ResourcePropertySource}(s)
	 */
	protected List<PropertySource<?>> loadPropertiesResources(final String propertiesLocation) {
		try {
			// check for resource from classpath
			if (StringUtils.startsWithIgnoreCase(propertiesLocation, PROPERTIES_CLASSPATH_PATTERN)
					|| StringUtils.startsWithIgnoreCase(propertiesLocation, PROPERTIES_CLASSPATH_WILDCARD_PATTERN)) {
				List<String> resolvableResourcePaths = org.nlh4j.util.StringUtils.resolveResourceNames(null, propertiesLocation);
				if (log.isDebugEnabled() || log.isTraceEnabled()) {
					log.debug("Resolvable resource paths [{}] from [{}]", StringUtils.join(resolvableResourcePaths, ", "), propertiesLocation);
				}
				Set<Resource> resources = resolvableResourcePaths.parallelStream()
						.map(resourcePath -> Optional.ofNullable(getContextHelper())
								.map(ctx -> ctx.searchResources(resourcePath))
								.map(Map<String, List<Resource>>::entrySet).orElseGet(LinkedHashSet::new))
						.flatMap(Set<Entry<String, List<Resource>>>::stream)
						.map(Entry<String, List<Resource>>::getValue)
						.flatMap(List<Resource>::stream).filter(Resource::exists)
						.collect(Collectors.toCollection(LinkedHashSet::new));
				return resources.parallelStream().map(res -> {
							PropertySource<?> propertySource = null;
							try {
								propertySource = new ResourcePropertySource(res);
							} catch (IOException e) {
								// for tracing
								if (log.isDebugEnabled() || log.isTraceEnabled()) {
									log.warn("Could not load resource [{}]: {}", res, e.getMessage(), e);

								} else {
									log.warn("Could not load resource [{}]: {}", res, e.getMessage());
								}

							} finally {
								if (log.isDebugEnabled() && propertySource != null) {
									log.debug("Load properties from [{}]", res.toString());
								}
							}
							return tracePropertiesSource(propertySource);
						}).filter(Objects::nonNull)
						.collect(Collectors.toCollection(LinkedList::new));

				// non-classpath
			} {
				if (log.isDebugEnabled()) {
					log.debug("Load properties from [{}]", propertiesLocation);
				}
				return Arrays.asList(tracePropertiesSource(new ResourcePropertySource(propertiesLocation)));
			}
		} catch (IOException e) {
			// for tracing
			if (log.isDebugEnabled() || log.isTraceEnabled()) {
				log.warn("Could not load properties [{}]: {}", propertiesLocation, e.getMessage(), e);

			} else {
				log.warn("Could not load properties [{}]: {}", propertiesLocation, e.getMessage());
			}
			return Collections.emptyList();
		}
	}
	
	/**
	 * Tracing the specified {@link PropertySource}
	 * 
	 * @param <T> {@link PropertySource} generic type
	 * @param propertiesSource {@link PropertySource}
	 * 
	 * @return the specified {@link PropertySource}
	 */
	private <T> PropertySource<T> tracePropertiesSource(final PropertySource<T> propertiesSource) {
		// tracing
		if (log.isTraceEnabled() && propertiesSource != null) {
			log.trace("[{}] = [{}]", propertiesSource.getName(), propertiesSource.getSource());

		} else if (log.isTraceEnabled()) {
			log.warn("PropertySource is EMPTY/NULL!");
		}
		return propertiesSource;
	}
}
