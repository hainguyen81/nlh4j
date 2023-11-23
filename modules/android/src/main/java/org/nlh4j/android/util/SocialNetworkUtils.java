/*
 * @(#)SocialNetworkUtils.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;
import java.text.MessageFormat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Social network utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class SocialNetworkUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    public static final String PACKAGE_TWITTER = "com.twitter.android";
    public static final String PACKAGE_TWITTER_URI_PATTERN = "twitter://user?user_id={0}";
    public static final String URI_TWITTER_PATTERN = "https://twitter.com/intent/user?user_id={0}";
    public static final String PACKAGE_FACEBOOK = "com.facebook.katana";
    public static final String PACKAGE_FACEBOOK_URI_PATTERN = "fb://page/{0}";
    public static final String URI_FACEBOOK_PATTERN = "https://www.facebook.com/{0}";

    /**
     * Launch twitter
     *
     * @param context {@link Context}
     * @param userId twitter user identity
     *
     * @return true if successful; else false
     */
    public static boolean launchTwitter(Context context, String userId) {
        Intent intent = null;
        boolean ok = false;
        try {
            // get the Twitter application if possible
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi = pm.getPackageInfo(PACKAGE_TWITTER, 0);
                Uri uri = Uri.parse(MessageFormat.format(PACKAGE_TWITTER_URI_PATTERN, userId));
                if (pi != null && uri != null) {
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            }
            ok = (intent != null);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            // try with application context
            if (context != null && context.getApplicationContext() != null) {
                ok = launchTwitter(context.getApplicationContext(), userId);
            }
        }
        // try using browser
        if (!ok) {
            try {
                Uri uri = Uri.parse(MessageFormat.format(URI_TWITTER_PATTERN, userId));
                intent = new Intent(Intent.ACTION_VIEW, uri);
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
                intent = null;
            }
        }
        // result
        ok = (context != null && intent != null);
        if (ok) context.startActivity(intent);
        return ok;
    }

    /**
     * Launch facebook
     *
     * @param context {@link Context}
     * @param userId facebook user identity
     *
     * @return true if successful; else false
     */
    public static boolean launchFacebook(Context context, String userId) {
        Intent intent = null;
        boolean ok = false;
        try {
            // get the Twitter application if possible
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi = pm.getPackageInfo(PACKAGE_FACEBOOK, 0);
                Uri uri = Uri.parse(MessageFormat.format(PACKAGE_FACEBOOK_URI_PATTERN, userId));
                if (pi != null && uri != null) {
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
            }
            ok = (intent != null);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            // try with application context
            if (context != null && context.getApplicationContext() != null) {
                ok = launchFacebook(context.getApplicationContext(), userId);
            }
        }
        // try using browser
        if (!ok) {
            try {
                Uri uri = Uri.parse(MessageFormat.format(URI_FACEBOOK_PATTERN, userId));
                intent = new Intent(Intent.ACTION_VIEW, uri);
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
                intent = null;
            }
        }
        // result
        ok = (context != null && intent != null);
        if (ok) context.startActivity(intent);
        return ok;
    }
}
