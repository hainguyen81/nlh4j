/*
 * @(#)AbstractSplashScreenActivity.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import org.nlh4j.android.tasks.AbstractAsyncTask;
import org.nlh4j.android.util.LogUtils;
import org.nlh4j.android.util.SystemUtils;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.util.CollectionUtils;

/**
 * Abstract splash screen activity
 *
 * @author Hai Nguyen
 *
 */
public abstract class AbstractSplashScreenActivity extends AbstractActivity {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /* (Non-Javadoc)
     * @see android.app.Activity#onPostCreate(android.os.Bundle)
     */
    @Override
    protected final void onPostCreate(Bundle savedInstanceState) {
        // by super class
        super.onPostCreate(savedInstanceState);
        // install data
        this.installData();
    }

    /**
     * Customize the start-up progress dialog if necessary.<br>
     * TODO Children classes maybe override this method for customizing progress dialog
     * @param progressDialog start-up progress dialog
     */
    protected abstract void customizeStartUpProgressDilaog(ProgressDialog progressDialog);
    /**
     * This method will be called after application started-up.<br>
     * TODO Children classes maybe override this method for performing some actions after start-up
     * such as start any activity, etc.
     *
     * @param task current start-up async-task
     * @param result result of this task
     * @param exceptions exceptions if failed
     */
    protected abstract void onPostStartUp(AsyncTask<Void, Void, Void> task, Void result, Throwable... exceptions);
    /**
     * This method will be called after application modules has been installed.<br>
     * TODO Children classes maybe override this method for performing some actions while application starting up
     * such as checking login, copy data, etc.
     */
    protected void onStartUp() {};

    /**
     * Install DEX parts
     */
    private void installData() {
        // create task for installing data
        AbstractAsyncTask<Void, Void, Void> task = new AbstractAsyncTask<Void, Void, Void>() {

            /**
             * default serial version id
             */
            private static final long serialVersionUID = 1L;

            /* (Non-Javadoc)
             * @see org.nlh4j.android.tasks.AbstractAsyncTask#usingProgressDialog()
             */
            @Override
            protected ProgressDialog usingProgressDialog() {
                ProgressDialog progressDialog = new ProgressDialog(AbstractSplashScreenActivity.this);
                progressDialog.setIcon(SystemUtils.getAppIcon(AbstractSplashScreenActivity.this));
                progressDialog.setTitle(SystemUtils.getAppName(AbstractSplashScreenActivity.this));
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.setOwnerActivity(AbstractSplashScreenActivity.this);
                AbstractSplashScreenActivity.this.customizeStartUpProgressDilaog(progressDialog);
                return progressDialog;
            }

            /*
             * (Non-Javadoc)
             * @see org.nlh4j.android.tasks.AbstractAsyncTask#doTask(java.lang.Object[])
             */
            @Override
            protected Void doTask(Void... params) throws ApplicationRuntimeException {
                // install modules
                MultiDex.install(AbstractSplashScreenActivity.this.getApplication());
                // perform starting up
                AbstractSplashScreenActivity.this.onStartUp();
                return null;
            }

            /* (Non-Javadoc)
             * @see org.nlh4j.android.tasks.AbstractAsyncTask#callbackPostExecute(android.os.AsyncTask, java.lang.Object, java.lang.Throwable[])
             */
            @Override
            protected void callbackPostExecute(AsyncTask<Void, Void, Void> task, Void result, Throwable... exceptions) {
                // debug exceptions
                if (!CollectionUtils.isEmpty(exceptions)) {
                    for(Throwable exception : exceptions) {
                        LogUtils.e(exception.getMessage(), exception);
                    }
                }
                // post strat-up
                AbstractSplashScreenActivity.this.onPostStartUp(task, result, exceptions);
                // stop progress
                AbstractSplashScreenActivity.this.finish();
            }
        };
        // run task
        task.execute();
    }
}
