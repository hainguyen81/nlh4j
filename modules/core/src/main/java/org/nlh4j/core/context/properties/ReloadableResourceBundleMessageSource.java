/*
 * @(#)ReloadableResourceBundleMessageSource.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.context.properties;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Singleton;

import org.nlh4j.util.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * Customize {@link org.springframework.context.support.ReloadableResourceBundleMessageSource}
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Singleton
public class ReloadableResourceBundleMessageSource
		extends org.springframework.context.support.ReloadableResourceBundleMessageSource
		implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private boolean highPriorityOnFirstLoad = false;
	
	@Override
	public void addBasenames(String... basenames) {
		// resolve base names for including classpath
		List<String> additionalBaseNames = Stream.of(Optional.ofNullable(basenames).orElseGet(() -> new String[0]))
				.parallel().filter(StringUtils::hasText).map(StringUtils::resolveResourceNames)
				.flatMap(Set<String>::parallelStream).filter(StringUtils::hasText)
				.distinct()
				.collect(Collectors.toCollection(LinkedList::new));
		// TODO if high priority on first loaded base names;
		// then reversing the base names list
		if (!isHighPriorityOnFirstLoad()) {
			Collections.reverse(additionalBaseNames);
		}
		getBasenameSet().addAll(additionalBaseNames);
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Solved resource base names: [%s]",
					org.apache.commons.lang3.StringUtils.join(getBasenameSet(), System.lineSeparator())));
		}
	}
}
