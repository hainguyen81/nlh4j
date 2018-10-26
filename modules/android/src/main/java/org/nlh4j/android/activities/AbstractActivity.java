/*
 * @(#)AbstractActivity.java 1.0 Sep 30, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.activities;

import java.io.Serializable;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import org.nlh4j.android.AbstractApplication;
import org.nlh4j.android.util.AlertUtils;
import org.nlh4j.android.util.NotificationUtils;
import org.nlh4j.android.util.SystemUtils;
import org.nlh4j.android.util.ToastUtils;
import org.nlh4j.util.BeanUtils;

/**
 * Abstract base {@link Activity}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public abstract class AbstractActivity extends Activity implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of {@link AbstractActivity} without database support
     */
    protected AbstractActivity() {}

    /*
     * (Non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        // pre-create activity
        this.onPreCreate(savedInstanceState);

        // create activity
        super.onCreate(savedInstanceState);

        // apply layout
        super.setContentView(this.getContentViewId());

        // apply application icon for action bar
        ActionBar actionBar = super.getActionBar();
        if (actionBar != null) {
            actionBar.setIcon(SystemUtils.getAppIcon(this));
        }
    }
    /**
     * TODO Child classes maybe override this method for doing something before #onCreate has been called
     * @param savedInstanceState
     */
    protected void onPreCreate(Bundle savedInstanceState) {
        // TODO Child classes maybe override this method for doing something before #onCreate has been called
    }

    /**
     * Get the layout content view identity
     *
     * @return the layout content view identity
     */
    protected abstract int getContentViewId();

    /**
     * Find view by identity
     *
     * @param <T> the component type
     * @param viewId view identity
     * @param viewClass view class to check
     *
     * @return view by the specified class, or null
     */
    public final <T extends View> T findViewById(int viewId, Class<T> viewClass) {
        View view = super.findViewById(viewId);
        return BeanUtils.safeType(view, viewClass);
    }
    /**
     * Find the content root view
     * @return the content root view or NULL if not found
     */
    public final View findContentRootView() {
        View view = super.findViewById(android.R.id.content);
        return (view != null ? view.getRootView() : null);
    }

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

    /**
     * Get the present application in safe-type
     * @return the present application in safe-type
     */
    protected final AbstractApplication getBaseApplication() {
        Application app = super.getApplication();
        return BeanUtils.safeType(app, AbstractApplication.class);
    }
}
