/*
 * @(#)ReloadableResourceBundleMessageSource.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.context.properties;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.nlh4j.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Customize {@link org.springframework.context.support.ReloadableResourceBundleMessageSource}
 */
@Slf4j
public class ReloadableResourceBundleMessageSource
		extends org.springframework.context.support.ReloadableResourceBundleMessageSource
		implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	@Override
	public void addBasenames(String... basenames) {
		// resolve base names for including classpath
		getBasenameSet().addAll(
				Stream.of(Optional.ofNullable(basenames).orElseGet(() -> new String[0]))
				.parallel().filter(StringUtils::hasText).map(StringUtils::resolveResourceNames)
				.flatMap(Set<String>::parallelStream).filter(StringUtils::hasText)
				.collect(Collectors.toCollection(LinkedHashSet::new)));
		if (log.isDebugEnabled()) {
			log.debug("Solved resource base names: [{}]",
					org.apache.commons.lang3.StringUtils.join(getBasenameSet(), System.lineSeparator()));
		}
	}
}
