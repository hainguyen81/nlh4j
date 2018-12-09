/*
 * @(#)ObserveThread.java Aug 18, 2018
 * Copyright 2018 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.observation;

import java.io.Serializable;

import org.nlh4j.util.MultiThreading;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Main observe {@link Thread}
 *
 * @author Hai Nguyen
 *
 */
public class ObserveThread extends MultiThreading implements Serializable {

	/** */
    private static final long serialVersionUID = 1L;

    /** slf4j */
    protected final transient Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Observe Listeners
	 */
	private transient SimpleObserve[] listeners;

	/**
	 * Initialize a new instance of {@link ObserveThread} class
	 * @param listeners {@link SimpleObserve}
	 */
	public ObserveThread(final SimpleObserve[] listeners) {
		Assert.notEmpty(listeners, "listeners");
		this.listeners = listeners;

		// initialize
		this.initialize();
	}

	/**
	 * Initialize tasks with listeners
	 */
	private void initialize() {
		for(final SimpleObserve listener : this.listeners) {
			Thread listenerThread = new Thread(new Runnable() {

				/*
				 * (非 Javadoc)
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					try {
						logger.info("Observe Listener: [" + listener.getClass().getSimpleName() + "]");
						listener.observe();
					} catch (Exception ie) {
						logger.error(ie.getMessage(), ie);
					}
				}
			}) {

				/* (非 Javadoc)
				 * @see java.lang.Thread#interrupt()
				 */
				@Override
				public void interrupt() {
					// close listener
					try { listener.close(); }
					catch (Exception e) {}
					// interupt by super
					super.interrupt();
				}

				/* (非 Javadoc)
				 * @see java.lang.Object#finalize()
				 */
				@Override
				protected void finalize() throws Throwable {
					// close listener
					try { listener.close(); }
					catch (Exception e) {}
					// finalize by super
					super.finalize();
				}
			};
			listenerThread.setContextClassLoader(this.getClass().getClassLoader());
			listenerThread.setDaemon(Boolean.TRUE);
			listenerThread.setName(listener.getClass().getSimpleName());
			listenerThread.setPriority(Thread.MIN_PRIORITY);

			// exception handler
            UncaughtExceptionHandler exHandler = new InternalUncaughtExceptionHandler();
            listenerThread.setUncaughtExceptionHandler(exHandler);

            // add task
			super.addTask(listenerThread, Boolean.FALSE);
		}
	}

	/**
	 * Internal {@link UncaughtExceptionHandler}
	 */
	private class InternalUncaughtExceptionHandler implements UncaughtExceptionHandler, Serializable {

		/** */
	    private static final long serialVersionUID = 1L;

		/* (non-Javadoc)
		 * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
		 */
		@Override
		public void uncaughtException(Thread t, Throwable e) {
			// process un-caught exception
            ObserveThread.this.uncaughtException(t, e);
		}
	}

	/* (non-Javadoc)
	 * @see com.api.common.MultiThreading#performSharing(java.lang.Thread)
	 */
	@Override
	protected void performSharing(Thread task) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.api.common.MultiThreading#performFinish()
	 */
	@Override
	protected void performFinish() {}
}
