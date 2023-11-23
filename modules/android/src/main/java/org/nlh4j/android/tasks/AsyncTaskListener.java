/*
 * @(#)AsyncTaskListener.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.tasks;

import java.io.Serializable;

import android.os.AsyncTask;

/**
 * {@link AsyncTask} listener
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public interface AsyncTaskListener<Params, Progress, Result> extends Serializable {
    /**
     * Call-back when the specified {@link AsyncTask} post execute
     *
     * @param task current {@link AsyncTask} post execute
     * @param exceptions {@link Exception} list every attempting time in fail case. NULL in success case
     * @param result the task result
     */
    void callback(AsyncTask<Params, Progress, Result> task, Result result, Throwable...exceptions);
}
