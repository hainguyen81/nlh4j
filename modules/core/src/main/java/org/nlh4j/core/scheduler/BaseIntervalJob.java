/*
 * @(#)BaseIntervalJob.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.scheduler;

import static org.quartz.JobBuilder.*;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import freemarker.template.Configuration;
import org.nlh4j.core.context.profiles.SpringProfiles;
import org.nlh4j.core.service.MessageService;
import org.nlh4j.core.service.TemplateService;
import org.nlh4j.core.service.TemplateServiceImpl;
import org.nlh4j.core.service.mail.MailService;
import org.nlh4j.core.service.mail.MailServiceImpl;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.core.util.JobSchedulerUtils;
import org.nlh4j.util.DateUtils;

/**
 * The base job
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Profile(value = { SpringProfiles.PROFILE_QUARTZ, SpringProfiles.PROFILE_FULL })
public abstract class BaseIntervalJob implements IntervalJob {

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;
	private int interval = 0;
	private int repeatCount = 0;
	private int startTime = 0;

	/**
	 * log4j
	 */
	protected transient final Logger logger = LoggerFactory.getLogger(JobScheduler.class);

	/**
	 * The job data map
	 */
	private transient Map<String, Object> data;
	/**
	 * The job context
	 */
	private JobExecutionContext context;

	/** context helper */
	@Inject
	private SpringContextHelper contextHelper;

	/**
	 * Initializes a new instance of the {@link BaseIntervalJob} class.
	 *
	 */
	protected BaseIntervalJob() {}
	/**
	 * Initializes a new instance of the {@link BaseIntervalJob} class.
	 *
	 */
	protected BaseIntervalJob(String key, Object value) {
		this.setJobData(key, value);
	}
	/**
	 * Initializes a new instance of the {@link BaseIntervalJob} class.
	 *
	 */
	protected BaseIntervalJob(Map<String, Object> data) {
		this.setJobData(data);
	}
	/**
	 * Initializes a new instance of the {@link BaseIntervalJob} class.
	 *
	 */
	protected BaseIntervalJob(String[] keys, Object[] values) {
		this.setJobData(keys, values);
	}

	/**
	 * Build job name for each job instance (not duplicate)
	 * @return the job name
	 */
	@Override
	public String getJobName() {
		return this.getClass().getName() + "@" + this.hashCode();
	}

	/**
	 * Gets the current job context; only valid while job is executing
	 *
	 * @return the current job context; only valid while job is executing
	 */
	protected final JobExecutionContext getJobContext() {
		return this.context;
	}

	/**
	 * (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info(
				MessageFormat.format(
						"Starting {0} [{1}]...",
						this.getClass().getName(),
						context.getJobDetail().getKey().getName()
				)
		);
		// captures job context
		this.context = context;

		// gets job datamap
		JobDataMap dataMap = context.getMergedJobDataMap();
		if ((dataMap != null) && (dataMap.size() > 0)) {
			for(final Iterator<?> it = dataMap.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				if (key == null || !StringUtils.hasLength(String.valueOf(key))) continue;
				this.getJobData().put(String.valueOf(key), dataMap.get(key));
			}
		}

		// does job
		try { this.doJob(); }
		catch (Exception e) {
		    logger.error(e.getMessage(), e);
		} finally {
		    if (Boolean.TRUE.equals(this.removeAfterExecute())) {
		        this.removeMe();
		    }
		}

		// releases job context
		this.context = null;
	}

	/**
	 * Does this job.<br/>
	 * Overrides for doing anything of this job.
	 */
	protected void doJob() throws JobExecutionException {
		// TODO Overrides for doing anything of this job.
	}

	/**
	 * Get a boolean value indicating this job that will be removed after executing
	 * TODO Children classes maybe override this method for handling
	 * @return true for removed; else false
	 */
	protected boolean removeAfterExecute() {
	    return Boolean.TRUE;
	}

	/**
	 * Overrides this method and using <code>getJobData().put(key, value)</code> method for adding job data map
	 */
	protected void createJobData() {
		// TODO Overrides this method and using <code>getJobData().put(key, value)</code> method for adding job data map
	}
	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.scheduler.IntervalJob#getJobDetail()
	 */
	@Override
	public JobDetail getJobDetail() {
		// creates job data map if necessary
		this.createJobData();
		// creates job detail with data map
		if (this.getJobData().size() > 0)
			return newJob(this.getClass())
					.withIdentity(getJobName(), Scheduler.DEFAULT_GROUP)
					.usingJobData(new JobDataMap(this.getJobData()))
					.build();
		// creates job detail without data map
		return newJob(this.getClass())
			.withIdentity(getJobName(), Scheduler.DEFAULT_GROUP)
			.build();
	}

	/**
	 * Gets the job data map.<br/>
	 * <b>* Note:</b> Using this method for registering data for job by user-defined data key
	 *
	 * @return the job data map
	 */
	protected final Map<String, Object> getJobData() {
		if (this.data == null)
			this.data = new LinkedHashMap<String, Object>();
		return this.data;
	}

	/**
	 * Get the job data from map by the data key
	 *
	 * @param key the data key
	 *
	 * @return the job data from map by the data key. the method will return null if invalid key
	 */
	public Object getJobData(String key) {
		if (this.getJobData().containsKey(key))
			return this.getJobData().get(key);
		return null;
	}
	/**
	 * Get the job data under integer type
	 *
	 * @param key data key
	 * @param def default value
	 *
	 * @return the job data under integer type
	 */
	public int getIntJobData(String key, int def) {
		try {
			return NumberUtils.toInt(String.valueOf(this.getJobData(key)));
		}
		catch(Exception e) {
			return def;
		}
	}
	/**
     * Get the job data under long type
     *
     * @param key data key
     * @param def default value
     *
     * @return the job data under long type
     */
    public long getLongJobData(String key, long def) {
		try {
			return NumberUtils.toLong(String.valueOf(this.getJobData(key)));
		}
		catch(Exception e) {
			return def;
		}
	}
    /**
     * Get the job data under string type
     *
     * @param key data key
     *
     * @return the job data under string type
     */
    public String getStringJobData(String key) {
		Object value = this.getJobData(key);
		return (value == null ? null : String.valueOf(value));
	}
	/**
	 * Sets the job data by the data key
	 *
	 * @param key
	 * 		the job data key
	 * @param value
	 * 		the job data
	 */
	public void setJobData(String key, Object value) {
		this.getJobData().put(key, value);
	}
	/**
	 * Sets the job data by the data key
	 *
	 * @param data job data
	 */
	public void setJobData(Map<String, Object> data) {
		if (!CollectionUtils.isEmpty(data)) {
			this.getJobData().putAll(data);
		}
	}
	/**
	 * Sets the job data by the data key
	 *
	 * @param keys the job data key
	 * @param values the job data
	 */
	public void setJobData(String[] keys, Object[] values) {
		if (!ArrayUtils.isEmpty(keys)) {
			int vallen = (ArrayUtils.isEmpty(values) ? 0 : values.length);
			for(int i = 0; i < keys.length; i++) {
				this.getJobData().put(keys[i], (i < vallen ? values[i] : null));
			}
		}
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.scheduler.IntervalJob#getInterval()
	 */
	@Override
	public int getInterval() {
		return this.interval;
	}

	/**
	 * Gets the startTime value
	 *
	 * @return the startTime
	 */
	@Override
	public int getStartTime() {
		return this.startTime;
	}
	/**
	 * Sets the startTime value
	 *
	 * @param startTime the startTime to set
	 */
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	/**
	 * Sets the date that this job has been started
	 *
	 * @param dt the date to apply
	 */
	public void setStartDate(Date dt) {
		this.setStartDate(dt, true);
	}
	/**
	 * Sets the date that this job has been started
	 *
	 * @param dt the date to apply
	 * @param startNowInSameDate specify starting job now (after 1 minute)
	 */
	public void setStartDate(Date dt, boolean startNowInSameDate) {
		// gets the current date
		Date curDate = new Date();
		// checks parameter
		if (DateUtils.compareTo(curDate, dt, Calendar.DATE) < 0)
			throw new IllegalArgumentException("The date, that this job has been started, must be equals or greater than current date!!!");
		// calculates the number of days from the start of current date to the start of job date
		int days = DateUtils.daysBetween(
				DateUtils.startOfDay(curDate),
				DateUtils.startOfDay(dt)
		);
		// applies start time of this job
		if (days == 0 && startNowInSameDate) {
			this.setStartTime(1);
		}
		// else if start this job after some days
		else if (days == 0) {
			this.setStartTime((int) (dt.getTime() - curDate.getTime()) / (1000 * 60));
		}
		else {
			this.setStartTime(DateUtils.daysToMinutes(days));
		}
	}

	/**
	 * Sets the interval value
	 *
	 * @param interval the interval to set
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}
	/**
	 * Sets the repeatCount value
	 *
	 * @param repeatCount the repeatCount to set
	 */
	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	/**
	 * Gets the repeatCount value
	 *
	 * @return the repeatCount
	 */
	@Override
	public int getRepeatCount() {
		return repeatCount;
	}
	/**
	 * Throw execution to remove this job form all triggers
	 * @throws JobExecutionException thrown if execution is fail
	 */
	public void removeMe() throws JobExecutionException {
		if (this.getJobContext() == null)
			JobSchedulerUtils.delete(this.getJobDetail().getKey());
		else
			JobSchedulerUtils.delete(this.getJobContext().getJobDetail().getKey());
	}

	/**
	 * Get context helper
	 * @return context helper
	 */
	private SpringContextHelper getContextHelper() {
		if (this.contextHelper == null) {
			this.contextHelper = new SpringContextHelper(JobSchedulerUtils.getSpringContext());
		}
		if (this.contextHelper == null) return null;
		synchronized (this.contextHelper) {
			return this.contextHelper;
		}
	}
	/**
	 * Get bean from context
	 * @param beanClass to search
	 * @return bean or null
	 */
	protected final <T> T findBean(Class<T> beanClass) {
		SpringContextHelper helper = this.getContextHelper();
		return (helper == null ? null : helper.searchBean(beanClass));
	}
	/**
	 * Get {@link MessageService} service
	 * @return {@link MessageService} service
	 */
	protected final MessageService getMessageService() {
		return this.findBean(MessageService.class);
	}
	public String getMessage(String key, Object... args) {
		return this.getMessageService().getMessage(key, args);
	}

	/**
     * {@link TemplateService}
     */
    private TemplateService templateService;
    /**
     * Get the template service {@link TemplateService}
     *
     * @return the template service {@link TemplateService}
     */
    protected final TemplateService getTemplateService() {
        if (this.templateService == null) {
            this.templateService = this.findBean(TemplateService.class);
            if (this.templateService == null) {
                Configuration fmCfg = this.findBean(Configuration.class);
                if (fmCfg != null) {
                    this.templateService = new TemplateServiceImpl(fmCfg);
                }
            }
        }
        if (this.templateService == null) return null;
        synchronized (this.templateService) {
            return this.templateService;
        }
    }

	/**
     * {@link MailService}
     */
    private MailService mailService;
    /**
     * Get the mail service {@link MailService}
     *
     * @return the mail service {@link MailService}
     */
    protected final MailService getMailService() {
        if (this.mailService == null) {
            this.mailService = this.findBean(MailService.class);
            if (this.mailService == null) {
                JavaMailSender sender = this.findBean(JavaMailSender.class);
                if (sender != null) {
                    this.mailService = new MailServiceImpl(sender);
                }
            }
        }
        if (this.mailService == null) return null;
        synchronized (this.mailService) {
            return this.mailService;
        }
    }
}
