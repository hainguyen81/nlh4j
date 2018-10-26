/*
 * @(#)JobSchedulerImpl.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.scheduler;

import static org.quartz.TriggerBuilder.*;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.simpl.SimpleJobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

import org.nlh4j.core.context.profiles.SpringProfiles;

/**
 * Implement of {@link JobScheduler} implement
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Profile(value = { SpringProfiles.PROFILE_QUARTZ, SpringProfiles.PROFILE_FULL })
public class JobSchedulerImpl implements JobScheduler {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * slf4j
	 */
	protected transient static Logger logger = LoggerFactory.getLogger(JobScheduler.class);
	private Scheduler quartzScheduler;

	/**
	 * Sets the quartzScheduler value
	 *
	 * @param scheduler the quartzScheduler
	 */
	public void setScheduler(Scheduler scheduler) {
		quartzScheduler = scheduler;
	}
	/**
	 * Gets the quartzScheduler value
	 *
	 * @return the quartzScheduler
	 */
	public Scheduler getScheduler() {
		return this.quartzScheduler;
	}

	/*
	 * (non-Javadoc)
	 * @see vn.com.kpms.scheduler.job.JobScheduler#schedule(vn.com.kpms.scheduler.job.IntervalJob, java.lang.Boolean)
	 */
	@Override
	public void schedule(IntervalJob intervalJob, Boolean rescheduledIfExisted) {
		if (intervalJob == null || this.isShutdown() || !this.isStarted()) {
			return;
		}

		// start the first running after interval
		long durationTime = ((intervalJob.getStartTime() > 0 ? intervalJob.getStartTime() : 0) * 60 * 1000);

		// build schedule
		SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();

		// set interval
		int minutesInterval = (intervalJob.getInterval() > 0)
				? intervalJob.getInterval()
				: IntervalJob.DEFAULT_INTERVAL_MINUTES;
		scheduleBuilder.withIntervalInMinutes(minutesInterval);

		// set repeat
		if (intervalJob.getRepeatCount() == 0) {
			// repeat forever
			scheduleBuilder.repeatForever();
		}
		else {
			// repeat N times
			scheduleBuilder.withRepeatCount(
					intervalJob.getRepeatCount());
		}

		// build job detail
		JobDetail jobDetail = intervalJob.getJobDetail();

		// build trigger
		Trigger trigger = newTrigger()
			.withIdentity(intervalJob.getJobName(), Scheduler.DEFAULT_GROUP)
			.withSchedule(scheduleBuilder)
			.startAt((durationTime > 0) ? new Date((new Date()).getTime() + durationTime) : new Date())
			.build();

		// schedule job
		try {
			boolean existed = quartzScheduler.checkExists(trigger.getKey());
			if (!existed) {
				Date dt = quartzScheduler.scheduleJob(jobDetail, trigger);
				logger.debug(
						"Scheduled cron job [" + intervalJob.getJobName() + "] " +
						"- Date: [" + (dt == null ? "NULL" : dt.toString()) + "]");
			}
			// re-schedules if existed job
			else if (Boolean.TRUE.equals(rescheduledIfExisted)) {
				Date dt = quartzScheduler.rescheduleJob(trigger.getKey(), trigger);
				logger.debug(
						"Re-scheduled cron job [" + intervalJob.getJobName() + "] " +
						"- Date: [" + (dt == null ? "NULL" : dt.toString()) + "]");
			}
			// debug
			else logger.debug(
					"Could not schedule cron job [" + intervalJob.getJobName() + "] " +
					"because it existed!");
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see vn.com.kpms.scheduler.job.JobScheduler#shutdown()
	 */
	@Override
	public void shutdown() {
		if (this.isShutdown()) {
			return;
		}
		try {
			quartzScheduler.shutdown();
			logger.debug("Shut down scheduler!");
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see vn.com.kpms.scheduler.job.JobScheduler#start()
	 */
	@Override
	public void start(boolean reschedulerJobs) {
		try {
			if (!this.isStarted()) {
				// FIXME fix for issue of Quartz 2.0.x and Spring 3.x integration
				logger.debug("Applying job factory!");
				quartzScheduler.setJobFactory(new SimpleJobFactory());

				// Starts scheduler
				logger.debug("Starting scheduler!");
				quartzScheduler.start();
			}
			// rescheduler for old jobs
			if (reschedulerJobs) {
				reschedulerJobsInStore();
			}
		}
		catch (SchedulerException e) {
			logger.error("Cant not start Quartz Scheduler: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Reschedule old jobs which stored in DB.
	 * Just load only one time in the first of server starting
	 *
	 * @param excludedJobNames excluding the specified job names
	 */
	@Override
	public void reschedulerJobsInStore(String...excludedJobNames) {
		if (this.isShutdown() || !this.isStarted()) {
			return;
		}
		logger.debug("===> Rescheduling old jobs...");
		// enumerate each trigger group
		try {
			int jobsCount = 0;
			for (String group: quartzScheduler.getTriggerGroupNames()) {
				Set<TriggerKey> triggerKeys = quartzScheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(group));
				for (TriggerKey triggerKey : triggerKeys) {
					Trigger trigger = quartzScheduler.getTrigger(triggerKey);
					// excludes the specified jobs
					if ((trigger == null)
							|| !ArrayUtils.isEmpty(excludedJobNames)
							&& (ArrayUtils.indexOf(excludedJobNames, trigger.getJobKey().getName()) != ArrayUtils.INDEX_NOT_FOUND)) continue;
					quartzScheduler.rescheduleJob(triggerKey, trigger);
					logger.debug(MessageFormat.format(
							"     + Rescheduled [{0}] job",
							trigger.getJobKey().getName()));
					jobsCount++;
				}
			}
			logger.debug("===> Number of jobs have been recovered to continue:" + jobsCount);
		}
		catch (SchedulerException e) {
			logger.error("Can not reschedule old jobs " +
					"that stored in database... Error: " + e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see vn.com.kpms.scheduler.job.JobScheduler#unschedule(vn.com.kpms.scheduler.job.IntervalJob)
	 */
	@Override
	public void unschedule(String jobName) {
		if (!StringUtils.hasText(jobName) || this.isShutdown()) {
			return;
		}
		try {
			boolean unscheduled = quartzScheduler.unscheduleJob(
					new TriggerKey(jobName, Scheduler.DEFAULT_GROUP));
			if (unscheduled) {
				logger.debug("Un-scheduled [" + jobName + "]");
			}
			else {
				logger.debug("Could not un-schedule [" + jobName + "]");
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see vn.com.kpms.scheduler.job.JobScheduler#pauseTrigger(java.lang.String)
	 */
	@Override
	public void pauseTrigger(String jobName) {
		if (!StringUtils.hasText(jobName) || this.isShutdown()) {
			return;
		}
		try {
			logger.debug("Pausing job [" + jobName + "]");
			quartzScheduler.pauseTrigger(
					new TriggerKey(jobName, Scheduler.DEFAULT_GROUP));
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see vn.com.kpms.scheduler.job.JobScheduler#resumeTrigger(java.lang.String)
	 */
	@Override
	public void resumeTrigger(String jobName) {
		if (!StringUtils.hasText(jobName) || this.isShutdown()) {
			return;
		}
		try {
			logger.debug("Resuming job [" + jobName + "]");
			quartzScheduler.resumeTrigger(
					new TriggerKey(jobName, Scheduler.DEFAULT_GROUP));
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see vn.com.kpms.scheduler.job.JobScheduler#delete(org.quartz.JobKey)
	 */
	@Override
	public void delete(JobKey jobKey) {
		if (this.isShutdown()) {
			return;
		}
		try {
			boolean deleted = quartzScheduler.deleteJob(jobKey);
			if (deleted) {
				logger.debug("Deleted job [" + jobKey.getName() + "]");
			}
			else {
				logger.debug("Could not delete [" + jobKey.getName() + "]");
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.scheduler.JobScheduler#isStarted()
	 */
	@Override
	public boolean isStarted() {
		try {
			return quartzScheduler.isStarted();
		}
		catch (SchedulerException se) {
			logger.error("Can not start Quartz scheduler. Error: " + se.getMessage());
			return false;
		}
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.scheduler.JobScheduler#isShutdown()
	 */
	@Override
	public boolean isShutdown() {
		try {
			return quartzScheduler.isShutdown();
		}
		catch (SchedulerException se) {
			logger.error("Can not start Quartz scheduler. Error: " + se.getMessage());
			return false;
		}
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.scheduler.JobScheduler#completeDelete(java.lang.String)
	 */
	@Override
	public void completeDelete(String jobName) {
		// pauses trigger
		this.pauseTrigger(jobName);
		// un-schedules trigger
		this.unschedule(jobName);
		// deletes the found trigger
		this.delete(new JobKey(jobName, Scheduler.DEFAULT_GROUP));
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.scheduler.JobScheduler#getContext()
	 */
	@Override
	public SchedulerContext getContext() throws SchedulerException {
		return this.getScheduler().getContext();
	}
}
