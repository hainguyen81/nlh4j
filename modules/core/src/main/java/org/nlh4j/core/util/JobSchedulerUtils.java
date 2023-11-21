/*
 * @(#)JobSchedulerUtils.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.util;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.util.Assert;

import org.nlh4j.core.context.profiles.SpringProfiles;
import org.nlh4j.core.scheduler.BaseIntervalJob;
import org.nlh4j.core.scheduler.IntervalJob;
import org.nlh4j.core.scheduler.JobScheduler;
import org.nlh4j.core.scheduler.JobSchedulerImpl;
import org.nlh4j.util.BeanUtils;

/**
 * Scheduled job manager utility
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Profile(value = { SpringProfiles.PROFILE_QUARTZ, SpringProfiles.PROFILE_FULL })
public final class JobSchedulerUtils implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * slf4j
	 */
	protected transient static final Logger logger = LoggerFactory.getLogger(JobSchedulerUtils.class);
	/** job scheduler */
	private static JobScheduler _jobScheduler;

	/**
	 * Set scheduler
	 * @param jobScheduler scheduler
	 */
	public void setJobScheduler(JobScheduler jobScheduler) {
		_jobScheduler = jobScheduler;
	}
	/**
	 * Get scheduler
	 * @return scheduler
	 */
	public static JobScheduler getJobScheduler() {
	    if (_jobScheduler == null && getSchedulerFactory() != null) {
	        _jobScheduler = new JobSchedulerImpl();
	        ((JobSchedulerImpl) _jobScheduler).setScheduler(getSchedulerFactory());
	    }
	    if (_jobScheduler != null) {
    	    synchronized (_jobScheduler) {
    	        return _jobScheduler;
            }
	    }
	    return null;
	}

	/** QUARTZ scheduler factory */
	private static Scheduler _quartzScheduler;
	/**
	 * Set scheduler factory
	 * @param _schedulerFactory factory
	 */
    public void setSchedulerFactory(Scheduler _schedulerFactory) {
        _quartzScheduler = _schedulerFactory;
    }
    /**
     * Get scheduler factory
     * @return factory
     */
    public static Scheduler getSchedulerFactory() {
        if (_quartzScheduler == null) return null;
        synchronized (_quartzScheduler) {
            return _quartzScheduler;
        }
    }

    /**
     * Schedule the specified job
     *
     * @param intervalJob job to schedule
     * @param rescheduledIfExisted specify re-scheduling if job existed
     */
	public static void schedule(IntervalJob intervalJob, Boolean rescheduledIfExisted) {
		if (getJobScheduler() != null) {
			getJobScheduler().schedule(intervalJob, rescheduledIfExisted);
		}
	}
	/**
     * Schedule the specified job
     *
     * @param intervalJob job to schedule
     */
    public static void schedule(IntervalJob intervalJob) {
		schedule(intervalJob, Boolean.TRUE);
	}

    /**
     * Get a boolean value indicating that scheduler has been started
     * @return true for started; else false
     */
	public static boolean isStarted() {
		return (getJobScheduler() == null ? false : getJobScheduler().isStarted());
	}

	/**
     * Get a boolean value indicating that scheduler has been shut down
     * @return true for shut down; else false
     */
    public static boolean isShutdown() {
		return (getJobScheduler() == null ? true : getJobScheduler().isShutdown());
	}

    /**
     * Start scheduler
     * @param reschedulerJobs specify whether re-scheduling jobs
     */
	public static void start(boolean reschedulerJobs) {
		if (getJobScheduler() != null) {
			getJobScheduler().start(reschedulerJobs);
		}
	}

	/**
	 * Re-schedule jobs that has been stored
	 */
	public static void reschedulerJobsInStore() {
		if (getJobScheduler() != null) {
			getJobScheduler().reschedulerJobsInStore((String[]) null);
		}
	}
	/**
	 * Re-schedule jobs that has been stored
	 * @param excludedJobNames excluding job names
	 */
	public static void reschedulerJobsInStore(String...excludedJobNames) {
		if (getJobScheduler() != null) {
			getJobScheduler().reschedulerJobsInStore(excludedJobNames);
		}
	}

	/**
	 * Shut scheduler down
	 */
	public static void shutdown() {
		if (getJobScheduler() != null) {
			getJobScheduler().shutdown();
		}
	}

	/**
	 * Un-schedule the specified job name
	 * @param jobName job name
	 */
	public static void unschedule(String jobName) {
		if (getJobScheduler() != null) {
			getJobScheduler().unschedule(jobName);
		}
	}

	/**
     * Pause the <code>{@link Trigger}</code> with the given key.
     * @see #resumeTrigger(String)
     *
     * @param jobName job name
     */
    public static void pauseTrigger(String jobName) {
    	if (getJobScheduler() != null) {
    		getJobScheduler().pauseTrigger(jobName);
    	}
    }

    /**
     * Resume (un-pause) the <code>{@link Trigger}</code> with the given
     * key.
     *
     * <p>
     * If the <code>Trigger</code> missed one or more fire-times, then the
     * <code>Trigger</code>'s misfire instruction will be applied.
     * </p>
     * @see #pauseTrigger(String)
     *
     * @param jobName job name
     */
    public static void resumeTrigger(String jobName) {
    	if (getJobScheduler() != null) {
    		getJobScheduler().resumeTrigger(jobName);
    	}
    }

	/**
	 * Completes deleting the specified job name by passing over
	 * {@link #pauseTrigger(String)}, {@link #unschedule(String)}, {@link #delete(JobKey)} functions
	 *
	 * @param jobName the job name to complete deleting
	 */
	public static void completeDelete(String jobName) {
		if (getJobScheduler() != null) {
			getJobScheduler().completeDelete(jobName);
		}
	}

	/**
	 * Delete the specified job
	 * @param jobKey job key
	 */
	public static void delete(JobKey jobKey) {
		if (getJobScheduler() != null) {
			getJobScheduler().delete(jobKey);
		}
	}

	/**
	 * Delete the specified job
	 * @param jobName job name
	 */
	public static void delete(String jobName) {
		if (getJobScheduler() != null) {
			getJobScheduler().unschedule(jobName);
		}
	}

	/**
	 * Get application context
	 * @return application context
	 */
	public static ApplicationContext getSpringContext() {
		SchedulerContext sc = null;

		try {
			sc = _jobScheduler.getContext();
		} catch (SchedulerException e) {
			logger.error("Can not get Spring context from scheduler context. Error: " + e.getMessage());
		}

		return (ApplicationContext) sc.get("applicationContext");
	}

	/***************************************************************
	 * HELPERS
	 ***************************************************************/

	/**
	 * Schedule the specified job
	 *
	 * @param jobClass job class
	 * @param startDt start date/time
	 * @param jobData job data
	 */
	public static void schedule(Class<?> jobClass, Date startDt, Map<String, Object> jobData) {
		Assert.isTrue(BeanUtils.isInstanceOf(jobClass, BaseIntervalJob.class), "Job must be an instance of " + BaseIntervalJob.class.getName());
		try {
			BaseIntervalJob job = (BaseIntervalJob) jobClass.newInstance();
			job.setStartDate(startDt);
			job.setJobData(jobData);
			schedule(job);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	/**
     * Schedule the specified job
     *
     * @param jobClass job class
     * @param startDt start date/time
     * @param jobDataKeys job data keys
     * @param jobDataValues job data values
     */
    public static void schedule(Class<?> jobClass, Date startDt, String[] jobDataKeys, Object[] jobDataValues) {
		Assert.isTrue(BeanUtils.isInstanceOf(jobClass, BaseIntervalJob.class), "Job must be an instance of " + BaseIntervalJob.class.getName());
		try {
			BaseIntervalJob job = (BaseIntervalJob) jobClass.newInstance();
			job.setStartDate(startDt);
			job.setJobData(jobDataKeys, jobDataValues);
			schedule(job);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
    /**
     * Schedule the specified job
     *
     * @param jobClass job class
     * @param startTime start date/time
     * @param jobData job data
     */
    public static void schedule(Class<?> jobClass, int startTime, Map<String, Object> jobData) {
		Assert.isTrue(BeanUtils.isInstanceOf(jobClass, BaseIntervalJob.class), "Job must be an instance of " + BaseIntervalJob.class.getName());
		try {
			BaseIntervalJob job = (BaseIntervalJob) jobClass.newInstance();
			job.setStartTime(startTime);
			job.setJobData(jobData);
			schedule(job);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
    /**
     * Schedule the specified job
     *
     * @param jobClass job class
     * @param startTime start date/time
     * @param jobDataKeys job data keys
     * @param jobDataValues job data values
     */
    public static void schedule(Class<?> jobClass, int startTime, String[] jobDataKeys, Object[] jobDataValues) {
		Assert.isTrue(BeanUtils.isInstanceOf(jobClass, BaseIntervalJob.class), "Job must be an instance of " + BaseIntervalJob.class.getName());
		try {
			BaseIntervalJob job = (BaseIntervalJob) jobClass.newInstance();
			job.setStartTime(startTime);
			job.setJobData(jobDataKeys, jobDataValues);
			schedule(job);
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Starts wanted jobs at context initialization
	 */
	public static void startCoreJobs() {
		try {
			// only applying on started scheduler
			if (JobSchedulerUtils.isStarted()) {
				// starts old jobs
				logger.debug("*********************** Regists [ALL OLD JOBS] [START] ***********************");
				reschedulerJobsInStore();
				logger.debug("*********************** Regists [ALL OLD JOBS] [END] ***********************");
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	/**
	 * Stops wanted jobs at context destroy
	 */
	public static void shutdownCoreJobs() {
		try {
			// only applying on non-stopped scheduler
			if (!isShutdown()) {
				// shuts down jobs
				shutdown();
			}

	        // sleep for a bit so that we don't get any errors
	        try {
				Thread.sleep(5000);
			}
			catch (InterruptedException e) {
				// ignore
			}
		}
		catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
}
