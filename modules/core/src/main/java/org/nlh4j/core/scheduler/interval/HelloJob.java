/*
 * @(#)HelloJob.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.scheduler.interval;

import org.quartz.JobExecutionException;

import org.nlh4j.core.scheduler.BaseIntervalJob;

/**
 * Example Quartz scheduler job
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public class HelloJob extends BaseIntervalJob {

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.scheduler.BaseIntervalJob#doJob()
	 */
	@Override
	protected void doJob() throws JobExecutionException {
		try {
			logger.info("Do job: Hello World!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			super.removeMe();
			logger.info("Removed HelloJob [" + super.getJobContext().getJobDetail().getKey().getName() + "]...");
		}
	}
}
