/*
 * @(#)AlertUtils.java 1.0 Nov 7, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;

import org.springframework.util.Assert;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.provider.Settings;
import org.nlh4j.util.StringUtils;

/**
 * {@link AlertDialog} utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class AlertUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Show alert/confirm dialog
     *
     * @param context {@link Context}
     * @param icoId dialog icon resource identity
     * @param titleId dialog title resource identity
     * @param messageId dialog message resource identity
     * @param positiveId positive button title resource identity
     * @param positiveListener positive button click listener
     * @param negativeId negative button title resource identity
     * @param negativeListener negative button click listener
     */
    public static void alert(Context context,
            int icoId, int titleId, int messageId,
            int positiveId, OnClickListener positiveListener,
            int negativeId, OnClickListener negativeListener) {
        Assert.notNull(context, "context");
        Builder dlg = new AlertDialog.Builder(context);
        dlg.setIcon(icoId);
        dlg.setTitle(titleId);
        dlg.setMessage(messageId);
        if (positiveId > 0 || positiveListener != null) {
            dlg.setPositiveButton(positiveId, positiveListener);
        }
        if (negativeId > 0 || negativeListener != null) {
            // default canceling
            if (negativeListener == null) {
                negativeListener = new OnClickListener() {

                    /*
                     * (Non-Javadoc)
                     * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                };
            }
            dlg.setNegativeButton(negativeId, negativeListener);
        }
        dlg.show();
    }
    /**
     * Show alert/confirm dialog
     *
     * @param context {@link Context}
     * @param icoId dialog icon resource identity
     * @param title dialog title
     * @param message dialog message
     * @param positive positive button title
     * @param positiveListener positive button click listener
     * @param negative negative button title
     * @param negativeListener negative button click listener
     */
    public static void alert(Context context,
            int icoId, String title, String message,
            String positive, OnClickListener positiveListener,
            String negative, OnClickListener negativeListener) {
        Assert.notNull(context, "context");
        Builder dlg = new AlertDialog.Builder(context);
        dlg.setIcon(icoId);
        dlg.setTitle(title);
        dlg.setMessage(message);
        if (StringUtils.hasText(positive) || positiveListener != null) {
            dlg.setPositiveButton(positive, positiveListener);
        }
        if (StringUtils.hasText(negative) || negativeListener != null) {
            // default canceling
            if (negativeListener == null) {
                negativeListener = new OnClickListener() {

                    /*
                     * (Non-Javadoc)
                     * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                };
            }
            dlg.setNegativeButton(negative, negativeListener);
        }
        dlg.show();
    }
    /**
     * Show alert/confirm dialog
     *
     * @param context {@link Context}
     * @param icoId dialog icon resource identity
     * @param title dialog title
     * @param messageId dialog message resource identity
     * @param positiveId positive button title resource identity
     * @param positiveListener positive button click listener
     * @param negativeId negative button title resource identity
     * @param negativeListener negative button click listener
     */
    public static void alert(Context context,
            int icoId, String title, int messageId,
            int positiveId, OnClickListener positiveListener,
            int negativeId, OnClickListener negativeListener) {
        Assert.notNull(context, "context");
        Builder dlg = new AlertDialog.Builder(context);
        dlg.setIcon(icoId);
        dlg.setTitle(title);
        dlg.setMessage(messageId);
        if (positiveId > 0 || positiveListener != null) {
            dlg.setPositiveButton(positiveId, positiveListener);
        }
        if (negativeId > 0 || negativeListener != null) {
            // default canceling
            if (negativeListener == null) {
                negativeListener = new OnClickListener() {

                    /*
                     * (Non-Javadoc)
                     * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                };
            }
            dlg.setNegativeButton(negativeId, negativeListener);
        }
        dlg.show();
    }
    /**
     * Show alert/confirm dialog
     *
     * @param context {@link Context}
     * @param message dialog message
     * @param positive positive button title
     * @param positiveListener positive button click listener
     * @param negative negative button title
     * @param negativeListener negative button click listener
     */
    public static void alert(
            Context context, String message,
            String positive, OnClickListener positiveListener,
            String negative, OnClickListener negativeListener) {
        Assert.notNull(context, "context");
        alert(context,
            SystemUtils.getAppIcon(context),
            SystemUtils.getAppName(context),
            message,
            positive, positiveListener,
            negative, negativeListener);
    }
    /**
     * Show alert/confirm dialog
     *
     * @param context {@link Context}
     * @param messageId dialog message resource identity
     * @param positiveId positive button title resource identity
     * @param positiveListener positive button click listener
     * @param negativeId negative button title resource identity
     * @param negativeListener negative button click listener
     */
    public static void alert(
            Context context, int messageId,
            int positiveId, OnClickListener positiveListener,
            int negativeId, OnClickListener negativeListener) {
        Assert.notNull(context, "context");
        alert(context,
            SystemUtils.getAppIcon(context),
            SystemUtils.getAppName(context),
            messageId,
            positiveId, positiveListener,
            negativeId, negativeListener);
    }
    /**
     * Show alert/confirm dialog
     *
     * @param context {@link Context}
     * @param icoId dialog icon resource identity
     * @param titleId dialog title resource identity
     * @param messageId dialog message resource identity
     * @param positiveId positive button title resource identity
     * @param positiveListener positive button click listener
     */
    public static void alert(Context context,
            int icoId, int titleId, int messageId,
            int positiveId, OnClickListener positiveListener) {
        Assert.notNull(context, "context");
        alert(context,
            icoId, titleId, messageId,
            positiveId, positiveListener,
            0, null);
    }
    /**
     * Show alert/confirm dialog
     *
     * @param context {@link Context}
     * @param icoId dialog icon resource identity
     * @param title dialog title
     * @param message dialog message
     * @param positive positive button title
     * @param positiveListener positive button click listener
     */
    public static void alert(Context context,
            int icoId, String title, String message,
            String positive, OnClickListener positiveListener) {
        Assert.notNull(context, "context");
        alert(context,
            icoId, title, message,
            positive, positiveListener,
            null, null);
    }
    /**
     * Show alert/confirm dialog
     *
     * @param context {@link Context}
     * @param icoId dialog icon resource identity
     * @param title dialog title
     * @param messageId dialog message resource identity
     * @param positiveId positive button title resource identity
     * @param positiveListener positive button click listener
     */
    public static void alert(Context context,
            int icoId, String title, int messageId,
            int positiveId, OnClickListener positiveListener) {
        Assert.notNull(context, "context");
        alert(context,
            icoId, title, messageId,
            positiveId, positiveListener,
            0, null);
    }
    /**
     * Show alert/confirm dialog
     *
     * @param context {@link Context}
     * @param message dialog message
     * @param positive positive button title
     * @param positiveListener positive button click listener
     */
    public static void alert(
            Context context, String message,
            String positive, OnClickListener positiveListener) {
        Assert.notNull(context, "context");
        alert(context,
            SystemUtils.getAppIcon(context),
            SystemUtils.getAppName(context),
            message,
            positive, positiveListener,
            null, null);
    }
    /**
     * Show alert/confirm dialog
     *
     * @param context {@link Context}
     * @param messageId dialog message resource identity
     * @param positiveId positive button title resource identity
     * @param positiveListener positive button click listener
     */
    public static void alert(
            Context context, int messageId,
            int positiveId, OnClickListener positiveListener) {
        Assert.notNull(context, "context");
        alert(context,
            SystemUtils.getAppIcon(context),
            SystemUtils.getAppName(context),
            messageId,
            positiveId, positiveListener,
            0, null);
    }

    /**
     * Show alert with the specified message, after showing, it'll open {@link Settings#ACTION_LOCATION_SOURCE_SETTINGS}
     * activity if clicking on positive button
     *
     * @param context {@link Context}
     * @param msgResId message resource identity
     * @param positiveId positive button resource identity
     * @param negativeId negative button resource identity
     */
    public static void alertLocationSourceSettings(final Context context, int msgResId, int positiveId, int negativeId) {
        alert(context, msgResId,
                positiveId,
                new OnClickListener() {

                    /*
                     * (Non-Javadoc)
                     * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavigationUtils.openLocationSourceSettings(context);
                    }
                },
                negativeId, null);
    }
    /**
     * Show alert with the specified message, after showing, it'll open {@link Settings#ACTION_LOCATION_SOURCE_SETTINGS}
     * activity if clicking on positive button
     *
     * @param context {@link Context}
     * @param message message resource
     * @param positive positive button resource
     * @param negative negative button resource
     */
    public static void alertLocationSourceSettings(final Context context, String message, String positive, String negative) {
        alert(context, message,
                positive,
                new OnClickListener() {

                    /*
                     * (Non-Javadoc)
                     * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavigationUtils.openLocationSourceSettings(context);
                    }
                },
                negative, null);
    }

    /**
     * Show alert with the specified message, after showing, it'll open {@link Settings#ACTION_NETWORK_OPERATOR_SETTINGS}
     * activity if clicking on positive button
     *
     * @param context {@link Context}
     * @param msgResId message resource identity
     * @param positiveId positive button resource identity
     * @param negativeId negative button resource identity
     */
    public static void alertNetworkOperatorSettings(final Context context, int msgResId, int positiveId, int negativeId) {
        alert(context, msgResId,
                positiveId,
                new OnClickListener() {

                    /*
                     * (Non-Javadoc)
                     * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavigationUtils.openNetworkOperatorSettings(context);
                    }
                },
                negativeId, null);
    }
    /**
     * Show alert with the specified message, after showing, it'll open {@link Settings#ACTION_NETWORK_OPERATOR_SETTINGS}
     * activity if clicking on positive button
     *
     * @param context {@link Context}
     * @param message message resource identity
     * @param positive positive button resource identity
     * @param negative negative button resource identity
     */
    public static void alertNetworkOperatorSettings(final Context context, String message, String positive, String negative) {
        alert(context, message,
                positive,
                new OnClickListener() {

                    /*
                     * (Non-Javadoc)
                     * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavigationUtils.openNetworkOperatorSettings(context);
                    }
                },
                negative, null);
    }

    /**
     * Show alert with the specified message, after showing, it'll open {@link Settings#ACTION_WIFI_SETTINGS}
     * activity if clicking on positive button
     *
     * @param context {@link Context}
     * @param msgResId message resource identity
     * @param positiveId positive button resource identity
     * @param negativeId negative button resource identity
     */
    public static void alertWirelessSettings(final Context context, int msgResId, int positiveId, int negativeId) {
        alert(context, msgResId,
                positiveId,
                new OnClickListener() {

                    /*
                     * (Non-Javadoc)
                     * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavigationUtils.openWirelessSettings(context);
                    }
                },
                negativeId, null);
    }
    /**
     * Show alert with the specified message, after showing, it'll open {@link Settings#ACTION_WIFI_SETTINGS}
     * activity if clicking on positive button
     *
     * @param context {@link Context}
     * @param message message resource identity
     * @param positive positive button resource identity
     * @param negative negative button resource identity
     */
    public static void alertWirelessSettings(final Context context, String message, String positive, String negative) {
        alert(context, message,
                positive,
                new OnClickListener() {

                    /*
                     * (Non-Javadoc)
                     * @see android.content.DialogInterface.OnClickListener#onClick(android.content.DialogInterface, int)
                     */
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavigationUtils.openWirelessSettings(context);
                    }
                },
                negative, null);
    }
}
