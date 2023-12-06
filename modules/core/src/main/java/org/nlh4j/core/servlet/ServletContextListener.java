/*
 * @(#)ServletContextListener.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.servlet;

import javax.servlet.ServletContextEvent;

import org.nlh4j.core.util.JobSchedulerUtils;
import org.nlh4j.util.SystemUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @see ServletContextListener
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Slf4j
public class ServletContextListener implements javax.servlet.ServletContextListener {

	/**
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
	    // tracking session
        //    SystemUtils.trackingSession(
        //            sce.getServletContext(),
        //            new SessionTrackingMode[] {
        //                    SessionTrackingMode.URL,
        //                    SessionTrackingMode.SSL
        //            });

	    // start core jobs
		log.info("Context Initialized!");
		JobSchedulerUtils.startCoreJobs();
	}

	/**
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	    // shut down core jobs
		JobSchedulerUtils.shutdownCoreJobs();

		// unregist all driver data source
		SystemUtils.unregistDrivers();

		// clean thread locals
		SystemUtils.cleanThreadLocals();

		// TODO Always at lastest position for turning logger
		SystemUtils.turnOffLogger();
		
		// debug
		log.info("Context Destroyed!");
	}
}
