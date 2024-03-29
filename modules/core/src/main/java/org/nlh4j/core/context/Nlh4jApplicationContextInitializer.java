/*
 * @(#)Nlh4jApplicationContextInitializer.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.context;

import java.io.IOException;
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

import javax.inject.Singleton;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.util.ExceptionUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * {@link ApplicationContextInitializer}
 */
@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Singleton
public class Nlh4jApplicationContextInitializer extends AbstractApplicationContextInitializer {
	
	/** */
	private static final long serialVersionUID = 1L;
	
	/** highPriorityOnFirstLoad */
	private static final String CONTEXT_PARAM_PROPERTIES_LOCATIONS_ORDER_REVERSE = "reverseLocationsOrder";

	/** propertiesLoadOrder */
	private static final String CONTEXT_PARAM_PROPERTIES_LOAD_ORDER = "propertiesLoadOrder";
	private static final String PROPERTIES_LOAD_ORDER_FIRST = "FIRST";
	private static final String PROPERTIES_LOAD_ORDER_LAST = "LAST";

	/** propertiesLocations */
	private static final String CONTEXT_PARAM_PROPERTIES_LOCATIONS = "propertiesLocations";
	private static final String PROPERTIES_LOCATIONS_SEPARATORS = System.lineSeparator() + ",; ";
	
	@Override
	protected String[] getContextParams() {
		return new String[] {
				CONTEXT_PARAM_PROPERTIES_LOAD_ORDER,
				CONTEXT_PARAM_PROPERTIES_LOCATIONS_ORDER_REVERSE,
				CONTEXT_PARAM_PROPERTIES_LOCATIONS
		};
	}
	
	@Override
	protected void doInitialize(Map<String, String> contextParamsMap) {
		final MutablePropertySources propertySources = getPropertySources();
		if (propertySources == null) {
			log.warn("Could not found mutable property sources from enviroment to push properties!");
			return;
		}

		boolean propertiesLocationsOrderReverse = Optional.ofNullable(contextParamsMap.getOrDefault(
				CONTEXT_PARAM_PROPERTIES_LOCATIONS_ORDER_REVERSE, Boolean.FALSE.toString()))
				.map(BooleanUtils::toBoolean).orElse(Boolean.FALSE);
		log.info("`{}`: {}", CONTEXT_PARAM_PROPERTIES_LOCATIONS_ORDER_REVERSE, propertiesLocationsOrderReverse);
		String propertiesLoadOrder = Optional.ofNullable(contextParamsMap.getOrDefault(
				CONTEXT_PARAM_PROPERTIES_LOAD_ORDER, PROPERTIES_LOAD_ORDER_FIRST))
				.filter(StringUtils::isNotBlank).filter(p -> StringUtils.equalsIgnoreCase(PROPERTIES_LOAD_ORDER_LAST, p))
				.map(p -> PROPERTIES_LOAD_ORDER_LAST).orElse(PROPERTIES_LOAD_ORDER_FIRST);
		log.info("`{}`: {}", CONTEXT_PARAM_PROPERTIES_LOAD_ORDER, propertiesLoadOrder);
		String propertiesLocations = contextParamsMap.getOrDefault(CONTEXT_PARAM_PROPERTIES_LOCATIONS, null);
		String[] propertiesLocationsArray = StringUtils.split(propertiesLocations, PROPERTIES_LOCATIONS_SEPARATORS);
		List<String> propertiesLocationsList = Stream.of(Optional.ofNullable(propertiesLocationsArray).orElseGet(() -> new String[0]))
				.parallel()
				.filter(StringUtils::isNotBlank).map(StringUtils::trimToEmpty)
				.distinct()
				.collect(Collectors.toCollection(LinkedList::new));
		// reverse properties locations
		if (propertiesLocationsOrderReverse) {
			Collections.reverse(propertiesLocationsList);
		}
		log.info("`{}`: {} - separate: [{}]", CONTEXT_PARAM_PROPERTIES_LOCATIONS, propertiesLocations, propertiesLocationsList);
		if (CollectionUtils.isNotEmpty(propertiesLocationsList)) {
			// FIXME Using parallel stream makes resources not keep correct order loading
			//	Set<PropertySource<?>> resourcePropertySources = propertiesLocationsSet.stream()
			//			.map(this::loadPropertiesResources).filter(CollectionUtils::isNotEmpty)
			//			.flatMap(Set<PropertySource<?>>::stream).collect(Collectors.toCollection(LinkedHashSet::new));
			Set<PropertySource<?>> resourcePropertySources = propertiesLocationsList.parallelStream()
					.map(this::loadPropertiesResources).filter(CollectionUtils::isNotEmpty)
					.flatMap(Set<PropertySource<?>>::parallelStream)
					.distinct()
					.collect(Collectors.toCollection(LinkedHashSet::new));
			if (log.isDebugEnabled()) {
				log.debug("Property Sources: [{}]", resourcePropertySources.parallelStream()
						.map(PropertySource::getName).collect(Collectors.joining(System.lineSeparator())));
			}
			if (CollectionUtils.isNotEmpty(resourcePropertySources)) {
			    if (StringUtils.equalsIgnoreCase(propertiesLoadOrder, PROPERTIES_LOAD_ORDER_LAST)) {
			        log.info("Load [{}] property sources at LAST!", resourcePropertySources.size());
			        // FIXME Using parallel stream makes resources not keep correct order loading
			        //    resourcePropertySources.parallelStream().forEach(propertySources::addLast);
			        resourcePropertySources.forEach(propertySources::addLast);

			    } else {
                    log.info("Load [{}] property sources at FIRST!", resourcePropertySources.size());
                    resourcePropertySources.forEach(propertySources::addFirst);
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
	protected Set<PropertySource<?>> loadPropertiesResources(final String propertiesLocation) {
		// check for resource from classpath
		Set<Resource> resources = Optional.ofNullable(getContextHelper())
				.map(ctx -> ctx.searchResources(propertiesLocation))
				.map(Map<String, List<Resource>>::entrySet).orElseGet(LinkedHashSet::new)
				.parallelStream()
				.map(Entry<String, List<Resource>>::getValue)
				.flatMap(List<Resource>::stream).filter(Resource::exists)
				.collect(Collectors.toCollection(LinkedHashSet::new));

		// solve property source
		Set<PropertySource<?>> propertySources = resources.parallelStream().map(res -> {
			ResourcePropertySource propertySource = null;
			try {
				propertySource = new ResourcePropertySource(getNameForResource(res), res);
			} catch (IOException e) {
				// for tracing
				if (log.isDebugEnabled()) {
					log.warn("Could not load resource [{}]: {}", res, e.getMessage());
				}
				ExceptionUtils.traceException(log, e);
				propertySource = null;

			} finally {
				if (log.isDebugEnabled()) {
					if (propertySource != null) {
						log.debug("Loaded [{}] properties from [{}]", propertySource.getSource().size(), res.toString());

					} else {
						log.warn("Could not loaded properties from [{}]", res.toString());
					}
				}
			}
			return tracePropertiesSource(propertySource);
		}).filter(Objects::nonNull)
		.collect(Collectors.toCollection(LinkedHashSet::new));

		// tracing
		if (log.isDebugEnabled()) {
			log.debug("==> Solved [{}] property sources for path [{}]", propertySources.size(), propertiesLocation);
		}
		return propertySources;
	}
	
	/**
	 * Return the description for the given Resource; if the description is
	 * empty, return the class name of the resource plus its identity hash code.
	 * @see org.springframework.core.io.Resource#getDescription()
	 */
	private static String getNameForResource(Resource resource) {
		String name = SpringContextHelper.getResourceDescription(resource);
		if (resource != null && StringUtils.isBlank(name)) {
			name = String.format("%s@%d", resource.getClass().getSimpleName(), System.identityHashCode(resource));

		} else if (StringUtils.isBlank(name)) {
			name = "Invalid Resource Detection!";
		}
		return String.format("%s@%s", Nlh4jApplicationContextInitializer.class.getSimpleName(), name);
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
