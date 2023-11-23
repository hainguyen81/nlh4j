/*
 * @(#)JobScheduler.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.scheduler;

import java.io.Serializable;

import org.quartz.JobKey;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.Trigger;

import org.nlh4j.core.context.profiles.SpringProfiles;

/**
 * Interface of JobScheduler
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Profile(value = { SpringProfiles.PROFILE_QUARTZ, SpringProfiles.PROFILE_FULL })
public interface JobScheduler extends Serializable {

	/**
     * Returns the <code>SchedulerContext</code> of the <code>Scheduler</code>.
     *
     * @return the scheduler context
     * @throws SchedulerException if fail
     */
    SchedulerContext getContext() throws SchedulerException;

	/**
	 * Scheduling a job
	 * @param intervalJob {@link IntervalJob}
	 * @param rescheduledIfExisted specify rescheduling job if existed
	 */
	public void schedule(IntervalJob intervalJob, Boolean rescheduledIfExisted);

	/**
	 * Start the scheduler. It would be invoked only one time when server starts
	 * @param reschedulerJobs specify rescheduling job if existed
	 */
	public void start(boolean reschedulerJobs);

	/**
	 * Shutdown the scheduler. It would be invoked only one time when server shuts down
	 */
	public void shutdown();

	/**
	 * Re-schedules all jobs that had been saved in store
	 * @param excludedJobNames excluding the specified job names
	 */
	public void reschedulerJobsInStore(String...excludedJobNames);

	/**
	 * Unscheduling a job with given name.
	 * This job would be stop and be able to resume with rescheduling with new trigger
	 *
	 * @param jobName job name
	 */
	public void unschedule(String jobName);

	/**
     * Pause the <code>{@link Trigger}</code> with the given key.
     *
     * @param jobName job name
     * @see #resumeTrigger(String)
     */
    public void pauseTrigger(String jobName);

    /**
     * Resume (un-pause) the <code>{@link Trigger}</code> with the given
     * key.
     *
     * <p>
     * If the <code>{@link Trigger}</code> missed one or more fire-times, then the
     * <code>{@link Trigger}</code>'s misfire instruction will be applied.
     * </p>
     *
     * @param jobName job name
     * @see #pauseTrigger(String)
     */
    public void resumeTrigger(String jobName);

	/**
	 * Permanently delete a job with given name.
	 * This job would be deleted in the list and not be able resume
	 *
	 * @param jobKey {@link JobKey}
	 */
	public void delete(JobKey jobKey);

	/**
	 * Completes deleting the specified job name by passing over
	 * {@link #pauseTrigger(String)}, {@link #unschedule(String)}, {@link #delete(JobKey)} functions
	 *
	 * @param jobName the job name to complete deleting
	 */
	public void completeDelete(String jobName);

	/**
	 * Get state of scheduler is started or not
	 * @return true if started
	 */
	public boolean isStarted();

	/**
	 * Get state of scheduler is shutdown or not
	 * @return true if shutdown
	 */
	public boolean isShutdown();

}
