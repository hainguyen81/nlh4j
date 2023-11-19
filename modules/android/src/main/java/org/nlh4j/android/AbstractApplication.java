/*
 * @(#)AbstractApplication.java 1.0 Oct 4, 2016
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android;

import java.io.Serializable;

import org.nlh4j.android.activities.AbstractSplashScreenActivity;
import org.nlh4j.android.db.SqlLiteAssetHelper;
import org.nlh4j.android.receivers.NetworkStateReceiver;
import org.nlh4j.android.receivers.NetworkStateReceiver.NetworkListener;
import org.nlh4j.android.util.AlertUtils;
import org.nlh4j.android.util.NotificationUtils;
import org.nlh4j.android.util.ToastUtils;
import org.nlh4j.util.StreamUtils;
import org.nlh4j.util.StringUtils;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.ConnectivityManager;
import android.support.multidex.MultiDex;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract base {@link Application}
 *
 * @author Hai Nguyen
 *
 */
public abstract class AbstractApplication extends Application implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    private SQLiteDatabase database;
    private NetworkStateReceiver networkReceiver;
    /** common data */
    @Getter
    @Setter
    private Object data;

    /**
     * Get the {@link SQLiteDatabase}
     * @return the {@link SQLiteDatabase}
     */
    public final SQLiteDatabase getDatabase() {
        if (this.database == null && StringUtils.hasText(this.getDbFileName())) {
            if (this.isDbWritable()) {
                this.database = (new SqlLiteAssetHelper(
                        this,
                        this.getDbFileName(),
                        this.getDbStorage(),
                        this.getDbCursor(),
                        this.getDbVersion())).getWritableDatabase();
            } else {
                this.database = (new SqlLiteAssetHelper(
                        this,
                        this.getDbFileName(),
                        this.getDbStorage(),
                        this.getDbCursor(),
                        this.getDbVersion())).getReadableDatabase();
            }
        }
        if (this.database == null) return null;
        synchronized (this.database) {
            return this.database;
        }
    }
    /**
     * Get the network receiver
     *
     * @return the network receiver
     */
    public final NetworkStateReceiver getNetworkReceiver() {
        if (this.networkReceiver == null && this.getNetworkListener() != null) {
            this.networkReceiver = new NetworkStateReceiver(this.getNetworkListener());
        }
        if (this.networkReceiver == null) return null;
        synchronized (this.networkReceiver) {
            return this.networkReceiver;
        }
    }

    /* (Non-Javadoc)
     * @see android.support.multidex.MultiDexApplication#attachBaseContext(android.content.Context)
     */
    @Override
    protected final void attachBaseContext(Context base) {
        // attach by supper
        super.attachBaseContext(base);
        // quick launch
        if (this.useQuickLaunch()) {
            // install DEX file
            MultiDex.install(this);
        }
        // post attach base context
        this.postAttachBaseContext(base);
    }
    /**
     * Specify not using splash activity and load {@link MultiDex} immediately.<br>
     * TODO If children class not use quick launch, children class must loading MultiDex
     * via {@link MultiDex#install(Context)} method or using {@link AbstractSplashScreenActivity}
     * @return true for loading; else using via {@link AbstractSplashScreenActivity}
     */
    protected boolean useQuickLaunch() {
        return true;
    }
    /**
     * This method will be invoked after {@link #attachBaseContext(Context)}.<br>
     * TODO Children classes maybe override this method such as invoking {@link #attachBaseContext(Context)}
     *
     * @param base context
     */
    protected void postAttachBaseContext(Context base) {}

    /* (Non-Javadoc)
     * @see android.app.Application#onCreate()
     */
    @Override
    public final void onCreate() {
        // by super class
        super.onCreate();
        // register network receiver if necessary
        NetworkStateReceiver receiver = this.getNetworkReceiver();
        if (receiver != null) {
            super.registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
    /**
     * This method will be invoked after {@link #onCreate()}<br>
     * TODO Children classes maybe override this method such as invoking {@link #onCreate()}
     */
    protected void postCreate() {}

    /**
     * Show toast message box
     *
     * @param msg message to show
     */
    protected final void toast(String msg) {
        ToastUtils.toast(this, msg);
    }
    /**
     * Show toast message box
     *
     * @param msg message to show
     * @param duration duration time
     */
    protected final void toast(String msg, int duration) {
        ToastUtils.toast(this, msg, duration);
    }
    /**
     * Show toast message box
     *
     * @param resId message resource identity to show
     */
    protected final void toast(int resId) {
        ToastUtils.toast(this, resId);
    }
    /**
     * Show toast message box
     *
     * @param resId message resource identity to show
     * @param duration duration time
     */
    protected final void toast(int resId, int duration) {
        ToastUtils.toast(this, resId, duration);
    }
    /**
     * Show toast message box
     *
     * @param resId message resource identity to show
     * @param argIds message argument resource identities
     */
    protected final void toast(int resId, Integer...argIds) {
        ToastUtils.toast(this, resId, argIds);
    }
    /**
     * Show toast message box
     *
     * @param resId message resource identity to show
     * @param duration duration time
     * @param argIds message argument resource identities
     */
    protected final void toast(int resId, int duration, Integer...argIds) {
        ToastUtils.toast(this, resId, duration, argIds);
    }

    /**
     * Show alert/confirm dialog
     *
     * @param icoId dialog icon resource identity
     * @param titleId dialog title resource identity
     * @param messageId dialog message resource identity
     * @param positiveId positive button title resource identity
     * @param positiveListener positive button click listener
     * @param negativeId negative button title resource identity
     * @param negativeListener negative button click listener
     */
    protected final void alert(
            int icoId, int titleId, int messageId,
            int positiveId, OnClickListener positiveListener,
            int negativeId, OnClickListener negativeListener) {
        AlertUtils.alert(this, icoId, titleId, messageId, positiveId, positiveListener, negativeId, negativeListener);
    }
    /**
     * Show alert/confirm dialog
     *
     * @param icoId dialog icon resource identity
     * @param title dialog title
     * @param message dialog message
     * @param positive positive button title
     * @param positiveListener positive button click listener
     * @param negative negative button title
     * @param negativeListener negative button click listener
     */
    protected final void alert(
            int icoId, String title, String message,
            String positive, OnClickListener positiveListener,
            String negative, OnClickListener negativeListener) {
        AlertUtils.alert(this, icoId, title, message, positive, positiveListener, negative, negativeListener);
    }
    /**
     * Show alert/confirm dialog
     *
     * @param icoId dialog icon resource identity
     * @param title dialog title
     * @param messageId dialog message resource identity
     * @param positiveId positive button title resource identity
     * @param positiveListener positive button click listener
     * @param negativeId negative button title resource identity
     * @param negativeListener negative button click listener
     */
    protected final void alert(
            int icoId, String title, int messageId,
            int positiveId, OnClickListener positiveListener,
            int negativeId, OnClickListener negativeListener) {
        AlertUtils.alert(this, icoId, title, messageId, positiveId, positiveListener, negativeId, negativeListener);
    }
    /**
     * Show alert/confirm dialog
     *
     * @param message dialog message
     * @param positive positive button title
     * @param positiveListener positive button click listener
     * @param negative negative button title
     * @param negativeListener negative button click listener
     */
    protected final void alert(
            String message,
            String positive, OnClickListener positiveListener,
            String negative, OnClickListener negativeListener) {
        AlertUtils.alert(this, message, positive, positiveListener, negative, negativeListener);
    }
    /**
     * Show alert/confirm dialog
     *
     * @param messageId dialog message resource identity
     * @param positiveId positive button title resource identity
     * @param positiveListener positive button click listener
     * @param negativeId negative button title resource identity
     * @param negativeListener negative button click listener
     */
    protected final void alert(
            int messageId,
            int positiveId, OnClickListener positiveListener,
            int negativeId, OnClickListener negativeListener) {
        AlertUtils.alert(this, messageId, positiveId, positiveListener, negativeId, negativeListener);
    }
    /**
     * Show alert/confirm dialog
     *
     * @param icoId dialog icon resource identity
     * @param titleId dialog title resource identity
     * @param messageId dialog message resource identity
     * @param positiveId positive button title resource identity
     * @param positiveListener positive button click listener
     */
    protected final void alert(
            int icoId, int titleId, int messageId,
            int positiveId, OnClickListener positiveListener) {
        AlertUtils.alert(this, icoId, titleId, messageId, positiveId, positiveListener);
    }
    /**
     * Show alert/confirm dialog
     *
     * @param icoId dialog icon resource identity
     * @param title dialog title
     * @param message dialog message
     * @param positive positive button title
     * @param positiveListener positive button click listener
     */
    protected final void alert(
            int icoId, String title, String message,
            String positive, OnClickListener positiveListener) {
        AlertUtils.alert(this, icoId, title, message, positive, positiveListener);
    }
    /**
     * Show alert/confirm dialog
     *
     * @param icoId dialog icon resource identity
     * @param title dialog title
     * @param messageId dialog message resource identity
     * @param positiveId positive button title resource identity
     * @param positiveListener positive button click listener
     */
    protected final void alert(
            int icoId, String title, int messageId,
            int positiveId, OnClickListener positiveListener) {
        AlertUtils.alert(this, icoId, title, messageId, positiveId, positiveListener);
    }
    /**
     * Show alert/confirm dialog
     *
     * @param message dialog message
     * @param positive positive button title
     * @param positiveListener positive button click listener
     */
    protected final void alert(String message, String positive, OnClickListener positiveListener) {
        AlertUtils.alert(this, message, positive, positiveListener);
    }
    /**
     * Show alert/confirm dialog
     *
     * @param messageId dialog message resource identity
     * @param positiveId positive button title resource identity
     * @param positiveListener positive button click listener
     */
    protected final void alert(int messageId, int positiveId, OnClickListener positiveListener) {
        AlertUtils.alert(this, messageId, positiveId, positiveListener);
    }

    /**
     * Show notification
     *
     * @param icoId icon resource identity
     * @param title notification title
     * @param message notification message
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     */
    protected final void notify(Intent intent, int notifyId, int icoId, String title, String message) {
        NotificationUtils.notify(this, intent, notifyId, icoId, title, message);
    }
    /**
     * Show notification
     *
     * @param icoId icon resource identity
     * @param titleId notification title resource identity
     * @param messageId notification message resource identity
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     */
    protected final void notify(Intent intent, int notifyId, int icoId, int titleId, int messageId) {
        NotificationUtils.notify(this, intent, notifyId, icoId, titleId, messageId);
    }
    /**
     * Show notification
     *
     * @param icoId icon resource identity
     * @param titleId notification title resource identity
     * @param message notification message
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     */
    protected final void notify(Intent intent, int notifyId, int icoId, int titleId, String message) {
        NotificationUtils.notify(this, intent, notifyId, icoId, titleId, message);
    }
    /**
     * Show notification
     *
     * @param icoId icon resource identity
     * @param title notification title
     * @param messageId notification message resource identity
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     */
    protected final void notify(Intent intent, int notifyId, int icoId, String title, int messageId) {
        NotificationUtils.notify(this, intent, notifyId, icoId, title, messageId);
    }
    /**
     * Show notification
     *
     * @param icoId icon resource identity
     * @param title notification title
     * @param messageId notification message resource identity
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     * @param msgArgs message argument resource identities
     */
    protected final void notify(Intent intent, int notifyId, int icoId, String title, int messageId, Integer...msgArgs) {
        NotificationUtils.notify(this, intent, notifyId, icoId, title, messageId, msgArgs);
    }
    /**
     * Show notification
     *
     * @param message notification message
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     */
    protected final void notify(Intent intent, int notifyId, String message) {
        NotificationUtils.notify(this, intent, notifyId, message);
    }
    /**
     * Show notification
     *
     * @param messageId notification message resource identity
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     */
    protected final void notify(Intent intent, int notifyId, int messageId) {
        NotificationUtils.notify(this, intent, notifyId, messageId);
    }
    /**
     * Show notification
     *
     * @param messageId notification message resource identity
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     * @param msgArgs message argument resource identities
     */
    protected final void notify(Intent intent, int notifyId, int messageId, Integer...msgArgs) {
        NotificationUtils.notify(this, intent, notifyId, messageId, msgArgs);
    }

    // -------------------------------------------------
    // DATABASE CONFIGURATION
    // -------------------------------------------------

    /**
     * Get the SQLite database storage path. NULL for default data/databases directory.<br>
     * TODO Children classes maybe override this method for using SQLite database via {@link #getDatabase()} method
     *
     * @return the SQLite database storage path
     */
    protected String getDbStorage() {
        return null;
    }
    /**
     * Get the SQLite database file name. NULL for not using database.<br>
     * TODO Children classes maybe override this method for using SQLite database via {@link #getDatabase()} method
     *
     * @return the SQLite database file name
     */
    protected String getDbFileName() {
        return null;
    }
    /**
     * Get the SQLite database file version.<br>
     * Only using when {@link #getDbFileName()} is valid and version must be equals or greater than 1.<br>
     * TODO Children classes maybe override this method for using SQLite database via {@link #getDatabase()} method
     * @return the SQLite database file version.
     */
    protected int getDbVersion() {
        return 1;
    }
    /**
     * Get the SQLite database {@link CursorFactory}.<br>
     * Only using when {@link #getDbFileName()} is valid and version must be equals or greater than 1.<br>
     * TODO Children classes maybe override this method for using SQLite database via {@link #getDatabase()} method
     * @return the SQLite database {@link CursorFactory}.
     */
    protected CursorFactory getDbCursor() {
        return null;
    }
    /**
     * Specify the SQLite database whether is writable/readable.<br>
     * Only using when {@link #getDbFileName()} is valid and version must be equals or greater than 1.<br>
     * TODO Children classes maybe override this method for using SQLite database via {@link #getDatabase()} method
     * @return true for writable; else readable
     */
    protected boolean isDbWritable() {
        return true;
    }
    /**
     * Reset database with the present configuration.<br>
     * TODO Children classes maybe using dynamic database configuration
     * and call this method for resetting database after that.
     */
    protected void resetDatabase() {
        // close old database if necessary
        StreamUtils.closeQuitely(this.database);
        this.database = null;
        // re-initialize database
        if (StringUtils.hasText(this.getDbFileName())) {
            if (this.isDbWritable()) {
                this.database = (new SqlLiteAssetHelper(
                        this,
                        this.getDbFileName(),
                        this.getDbCursor(),
                        this.getDbVersion())).getWritableDatabase();
            } else {
                this.database = (new SqlLiteAssetHelper(
                        this,
                        this.getDbFileName(),
                        this.getDbCursor(),
                        this.getDbVersion())).getReadableDatabase();
            }
        }
    }

    // -------------------------------------------------
    // NETWORK CONFIGURATION
    // -------------------------------------------------

    /**
     * Get the network state listener. NULL for not using network listener.<br>
     * Require permissions in AndroidManifest.xml:<br>
     * &lt;uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /&gt;<br>
     * &lt;uses-permission android:name="android.permission.ACCESS_WIFI_STATE" /&gt;<br>
     * TODO Children classes maybe override this method for using network listener via {@link #getNetworkReceiver()} method
     * @return the network state listener
     */
    protected NetworkListener getNetworkListener() {
        return null;
    }
}
