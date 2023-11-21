/*
 * @(#)NotificationUtils.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;

import org.springframework.util.Assert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import org.nlh4j.util.StringUtils;

/**
 * {@link Notification} utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class NotificationUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_NOTIFICATION_ID = 100;

    /**
     * Show notification
     *
     * @param context {@link Context}
     * @param icoId icon resource identity
     * @param title notification title
     * @param message notification message
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     */
    public static void notify(Context context, Intent intent, int notifyId, int icoId, String title, String message) {
        Assert.notNull(context, "context");
        // check message
        if (!StringUtils.hasText(message)) return;
        // if application is background
        if (SystemUtils.isAppOnBackground(context)) {
            // prepare pending intent
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(
                            context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationCompat.InboxStyle noticeIbStyle = new NotificationCompat.InboxStyle();
            NotificationCompat.Builder noticeBuilder = new NotificationCompat.Builder(context);
            Notification notification = noticeBuilder
                    .setSmallIcon(icoId)
                    .setTicker(title)
                    .setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(noticeIbStyle)
                    .setContentIntent(resultPendingIntent)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setLargeIcon(ImageUtils.fromResource(context, icoId))
                    .setContentText(message)
                    .build();

            // notify
            NotificationManager nm = ServiceUtils.getNotificationManager(context);
            nm.notify(Math.max(notifyId, 0), notification);

            // application is foreground
        } else {
            intent.putExtra("title", title);
            intent.putExtra("message", message);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        }
    }
    /**
     * Show notification
     *
     * @param context {@link Context}
     * @param icoId icon resource identity
     * @param titleId notification title resource identity
     * @param messageId notification message resource identity
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     */
    public static void notify(Context context, Intent intent, int notifyId, int icoId, int titleId, int messageId) {
        Assert.notNull(context, "context");
        // parse resource
        String title = ResourceUtils.getResourceString(context, titleId);
        String message = ResourceUtils.getResourceString(context, messageId);
        notify(context, intent, notifyId, icoId, title, message);
    }
    /**
     * Show notification
     *
     * @param context {@link Context}
     * @param icoId icon resource identity
     * @param titleId notification title resource identity
     * @param message notification message
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     */
    public static void notify(Context context, Intent intent, int notifyId, int icoId, int titleId, String message) {
        Assert.notNull(context, "context");
        // parse resource
        String title = ResourceUtils.getResourceString(context, titleId);
        notify(context, intent, notifyId, icoId, title, message);
    }
    /**
     * Show notification
     *
     * @param context {@link Context}
     * @param icoId icon resource identity
     * @param title notification title
     * @param messageId notification message resource identity
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     */
    public static void notify(Context context, Intent intent, int notifyId, int icoId, String title, int messageId) {
        Assert.notNull(context, "context");
        // parse resource
        String message = ResourceUtils.getResourceString(context, messageId);
        notify(context, intent, notifyId, icoId, title, message);
    }
    /**
     * Show notification
     *
     * @param context {@link Context}
     * @param icoId icon resource identity
     * @param title notification title
     * @param messageId notification message resource identity
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     * @param msgArgs message argument resource identities
     */
    public static void notify(Context context, Intent intent, int notifyId, int icoId, String title, int messageId, Integer...msgArgs) {
        Assert.notNull(context, "context");
        // parse resource
        String message = ResourceUtils.getResourceString(context, messageId, msgArgs);
        notify(context, intent, notifyId, icoId, title, message);
    }
    /**
     * Show notification
     *
     * @param context {@link Context}
     * @param message notification message
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     */
    public static void notify(Context context, Intent intent, int notifyId, String message) {
        Assert.notNull(context, "context");
        notify(context, intent, notifyId, SystemUtils.getAppIcon(context), SystemUtils.getAppName(context), message);
    }
    /**
     * Show notification
     *
     * @param context {@link Context}
     * @param messageId notification message resource identity
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     */
    public static void notify(Context context, Intent intent, int notifyId, int messageId) {
        Assert.notNull(context, "context");
        // parse resource
        String message = ResourceUtils.getResourceString(context, messageId);
        notify(context, intent, notifyId, SystemUtils.getAppIcon(context), SystemUtils.getAppName(context), message);
    }
    /**
     * Show notification
     *
     * @param context {@link Context}
     * @param messageId notification message resource identity
     * @param intent action intent while clicking notification
     * @param notifyId notification identity. -1 for default {@link NotificationUtils#DEFAULT_NOTIFICATION_ID}
     * @param msgArgs message argument resource identities
     */
    public static void notify(Context context, Intent intent, int notifyId, int messageId, Integer...msgArgs) {
        Assert.notNull(context, "context");
        // parse resource
        String message = ResourceUtils.getResourceString(context, messageId, msgArgs);
        notify(context, intent, notifyId, SystemUtils.getAppIcon(context), SystemUtils.getAppName(context), message);
    }
}
