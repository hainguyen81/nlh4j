/*
 * @(#)IntervalJob.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.scheduler;

import java.io.Serializable;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Profile;

import org.nlh4j.core.context.profiles.SpringProfiles;

/**
 * Recurrence job
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Profile(value = { SpringProfiles.PROFILE_QUARTZ, SpringProfiles.PROFILE_FULL })
public interface IntervalJob extends Job, Serializable {

	public static final int DEFAULT_REPEAT_COUNT = 3;
	public static final int DEFAULT_INTERVAL_MINUTES = 24 * 60;	// daily

	/**
	 * Build JobDetails the will execute by scheduler
	 * It is used to set name or build job data map for paramatters transferring in real execution
	 *
	 * @return number of minutes
	 */
	public JobDetail getJobDetail();

	/**
	 * Build job name
	 * @return number of minutes
	 */
	public String getJobName();

	/**
	 * Interval time to repeat job (in minutes)
	 * @return number of minutes
	 */
	public int getInterval();

	/**
	 * The number of minutes to start this job
	 * @return number of minutes
	 */
	public int getStartTime();

	/**
	 * The number of recurrence of executing
	 * If 0, repeat forever
	 * @return the number of recurrence of executing
	 */
	public int getRepeatCount();
}
