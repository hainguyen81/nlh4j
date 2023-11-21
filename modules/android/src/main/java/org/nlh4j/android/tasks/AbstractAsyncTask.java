/*
 * @(#)AbstractAsyncTask.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.tasks;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import lombok.Getter;
import org.nlh4j.android.util.LogUtils;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.util.CollectionUtils;

/**
 * Abstract a-synchronized task
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 * @param <Params> the task parameter type
 * @param <Progress> the task progress to update
 * @param <Result> the task result type
 */
@SuppressWarnings("unchecked")
public abstract class AbstractAsyncTask<Params, Progress, Result>
        extends AsyncTask<Params, Progress, Result>
        implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    protected static final int RETRY_MAX_ATTEMPTS = 1; // default value for max number of retries
    protected static final long RETRY_INIT_BACKOFF = 1000L; // initial sleep time before retry
    private ProgressDialog progressDialog;

    /** exceptions list every attempting */
    private List<Throwable> exceptions;
    public List<Throwable> getExceptions() {
        if (this.exceptions == null) {
            this.exceptions = new LinkedList<Throwable>();
        }
        return this.exceptions;
    }

    /** listener for posting execution */
    private WeakReference<AsyncTaskListener<Params, Progress, Result>> listener;
    /**
     * Get the posting execution task listener
     * @return the posting execution task listener
     */
    protected AsyncTaskListener<Params, Progress, Result> getListener() {
        return (listener != null ? listener.get() : null);
    }
    /**
     * Set the posting execution task listener
     * @param asynclistener the posting execution task listener
     */
    public void setListener(AsyncTaskListener<Params, Progress, Result> asynclistener) {
        this.listener = null;
        if (asynclistener != null) {
            this.listener = new WeakReference<AsyncTaskListener<Params,Progress,Result>>(asynclistener);
        }
    }

    /** maximum attempts number */
    @Getter
    private int maxAttempts = RETRY_MAX_ATTEMPTS;
    /**
     * Set the maximum attempts number
     * @param maxAttempts the maximum attempts number
     */
    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = Math.max(maxAttempts, RETRY_MAX_ATTEMPTS);
    }

    /** sleep time (milliseconds) between every retry time */
    @Getter
    private long sleepTime = RETRY_INIT_BACKOFF;
    /**
     * Set the sleep time (milliseconds) between every retry time
     *
     * @param sleepTime the sleep time (milliseconds) between every retry time
     */
    public void setSleepTime(long sleepTime) {
        this.sleepTime = Math.max(sleepTime, RETRY_INIT_BACKOFF);
    }

    /**
     * Initialize a new instance of {@link AbstractAsyncTask}
     */
    protected AbstractAsyncTask() {}
    /**
     * Initialize a new instance of {@link AbstractAsyncTask}
     *
     * @param listener the posting execution listener
     */
    protected AbstractAsyncTask(AsyncTaskListener<Params, Progress, Result> listener) {
        this(RETRY_MAX_ATTEMPTS, RETRY_INIT_BACKOFF, listener);
    }
    /**
     * Initialize a new instance of {@link AbstractAsyncTask}
     *
     * @param maxAttempts the maximum attempts number
     * @param sleepTime the sleep time (milliseconds) every attempting time
     * @param listener the posting execution listener
     */
    protected AbstractAsyncTask(int maxAttempts, long sleepTime, AsyncTaskListener<Params, Progress, Result> listener) {
        this.setMaxAttempts(maxAttempts);
        this.setSleepTime(sleepTime);
        this.setListener(listener);
    }

    /* (Non-Javadoc)
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected final void onPreExecute() {
        // pre-execute
        this.preExecute();
        // show progress dialog if necessary
        this.progressDialog = this.usingProgressDialog();
        if (this.progressDialog != null && !this.progressDialog.isShowing()) {
            this.progressDialog.show();
        }
    }
    /**
     * Alias of {@link #onPreExecute()}<br>
     * TODO Children classes maybe override this method for performing some actions
     * before progress dialog has been shown, and {@link #doInBackground(Object...)} has been called
     */
    protected void preExecute() {}

    /* (Non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
     */
    @Override
    protected final Result doInBackground(Params... params) {
        // sleep time before retry
        long backoff = RETRY_INIT_BACKOFF;
        this.getExceptions().clear();
        Result result = null;
        int maxAttempts = this.getMaxAttempts();
        for(int i = 0; i < maxAttempts; i++) {
            boolean shouldAttempt = false;
            try {
                /** execute task */
                result = this.doTask(params);
            } catch (Exception e) {
                LogUtils.e(e.getMessage(), e);
                this.getExceptions().add(e);
                shouldAttempt = (i < maxAttempts - 1);
                result = null;
            }

            // try attempting if exception
            if (shouldAttempt) {
                try {
                    LogUtils.d("Sleep to try attempting again!");
                    Thread.sleep(backoff);
                } catch (Exception e) {
                    LogUtils.e(e.getMessage(), e);
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        /** task result */
        return result;
    }
    /**
     * Perform actions to do task
     * @param params parameters if necessary
     * @return the task result
     */
    protected abstract Result doTask(Params... params) throws ApplicationRuntimeException;

    /* (Non-Javadoc)
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected final void onPostExecute(Result result) {
        // call by super class
        super.onPostExecute(result);

        // dismiss progress dialog
        if (this.progressDialog != null && this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }

        // callback
        if (!this.isCancelled() && this.getListener() != null) {
            if (CollectionUtils.isEmpty(this.getExceptions())) {
                this.getListener().callback(this, result, (Throwable[]) null);
            } else {
                this.getListener().callback(
                        this, result,
                        this.getExceptions().toArray(
                                new Throwable[this.getExceptions().size()]));
            }

        } else if (!this.isCancelled() && this.getListener() == null) {
            if (CollectionUtils.isEmpty(this.getExceptions())) {
                this.callbackPostExecute(this, result, (Throwable[]) null);
            } else {
                this.callbackPostExecute(
                        this, result,
                        this.getExceptions().toArray(
                                new Throwable[this.getExceptions().size()]));
            }
        }
    }
    /**
     * Call-back when the specified {@link AsyncTask} post execute.<br>
     * TODO Children classes maybe override this method for call-backing when {@link #onPostExecute(Object)}
     * if children classes is not using {@link AsyncTaskListener}
     *
     * @param task current {@link AsyncTask} post execute
     * @param exceptions {@link Exception} list every attempting time in fail case. NULL in success case
     * @param result the task result
     */
    protected void callbackPostExecute(AsyncTask<Params, Progress, Result> task, Result result, Throwable...exceptions) {}

    /**
     * Create progress dialog for async-task. NULL for not using
     * @return progress dialog
     */
    protected ProgressDialog usingProgressDialog() { return null; }
}
