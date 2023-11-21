/*
 * @(#)JobClassUtil.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.scheduler;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utilities for job classes
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public class JobClassUtil implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Get job by name
	 *
	 * @param jobName job name
	 *
	 * @return job or null
	 */
	public static Class<IntervalJob> get(String jobName) {
		return getJobClasses().get(jobName);
	}

	/**
	 * Remove job by name
	 *
	 * @param jobName job name to remove
	 */
	public static void remove(String jobName) {
	    getJobClasses().remove(jobName);
	}

	/**
	 * Put job with the specified name key
	 *
	 * @param jobName job name key
	 * @param intervalJobClass job class
	 */
	public static void put(
		String jobName, Class<IntervalJob> intervalJobClass) {
	    getJobClasses().put(jobName, intervalJobClass);
	}

	private transient static Map<String, Class<IntervalJob>> _jobClasses;
	/**
	 * Get the synchronized map of job classes
	 * @return the synchronized map of job classes
	 */
	private static final Map<String, Class<IntervalJob>> getJobClasses() {
	    if (_jobClasses == null) {
	        _jobClasses = Collections.synchronizedMap(new LinkedHashMap<String, Class<IntervalJob>>());
	    }
	    synchronized (_jobClasses) {
            return _jobClasses;
        }
	}
}
