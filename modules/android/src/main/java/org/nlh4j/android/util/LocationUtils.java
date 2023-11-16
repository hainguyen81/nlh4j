/*
 * @(#)LocationUtils.java 1.0 Nov 10, 2016
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.springframework.util.Assert;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.StringUtils;

/**
 * {@link Location} utilities
 *
 * @author Hai Nguyen
 *
 */
public final class LocationUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Get the providers list from {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the providers list or empty if failed
     */
    public static List<String> getAllProviders(Context context) {
        List<String> providers = new LinkedList<String>();
        LocationManager lm = ServiceUtils.getLocationManager(context);
        if (lm != null) providers.addAll(lm.getAllProviders());
        return providers;
    }

    /**
     * Get the enabled-only providers list from {@link Context}
     *
     * @param context {@link Context}
     * @param enabled specify getting only enabled providers
     *
     * @return the enabled-only providers list or empty if failed
     */
    public static List<String> getProviders(Context context, boolean enabled) {
        List<String> providers = new LinkedList<String>();
        LocationManager lm = ServiceUtils.getLocationManager(context);
        if (lm != null) providers.addAll(lm.getProviders(enabled));
        return providers;
    }

    /**
     * Get a boolean value indicating that the specified provider whether is valid and enabled
     *
     * @param context {@link Context}
     * @param provider to check
     *
     * @return true for valid and enabled; else false
     */
    public static boolean isProviderEnabled(Context context, String provider) {
        LocationManager lm = ServiceUtils.getLocationManager(context);
        return (lm != null && lm.isProviderEnabled(provider));
    }
    /**
     * Get the first occurred enabled provider from network, gps, and passive providers.
     *
     * @param context {@link Context}
     *
     * @return the first occurred enabled provider from network, gps, and passive providers. NULL for not any enabled provider
     */
    public static String getAnyProviderEnabled(Context context) {
        if (isProviderEnabled(context, LocationManager.NETWORK_PROVIDER)) {
            return LocationManager.NETWORK_PROVIDER;
        } else if (isProviderEnabled(context, LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        } else if (isProviderEnabled(context, LocationManager.PASSIVE_PROVIDER)) {
            return LocationManager.PASSIVE_PROVIDER;
        } else {
            return null;
        }
    }

    /**
     * Get the last known location by the specified provider
     *
     * @param context {@link Context}
     * @param provider to check
     *
     * @return the last known location or null if failed or provider is not enabled
     */
    public static Location getLastKnownLocation(Context context, String provider) {
        Location loc = null;
        boolean enabled = isProviderEnabled(context, provider);
        LocationManager lm = ServiceUtils.getLocationManager(context);
        if (lm != null && enabled) {
            try {
                loc = lm.getLastKnownLocation(provider);
            } catch(Exception e) {
                LogUtils.w(e.getMessage());
                loc = null;
            }
        }
        return loc;
    }
    /**
     * Get the last known location by the network provider
     *
     * @param context {@link Context}
     *
     * @return the last known location or null if failed
     */
    public static Location getLastKnownNetworkLocation(Context context) {
        return getLastKnownLocation(context, LocationManager.NETWORK_PROVIDER);
    }
    /**
     * Get the last known location by the GPS provider
     *
     * @param context {@link Context}
     *
     * @return the last known location or null if failed
     */
    public static Location getLastKnownGpsLocation(Context context) {
        return getLastKnownLocation(context, LocationManager.GPS_PROVIDER);
    }
    /**
     * Get the last known location by the passive provider
     *
     * @param context {@link Context}
     *
     * @return the last known location or null if failed
     */
    public static Location getLastKnownPassiveLocation(Context context) {
        return getLastKnownLocation(context, LocationManager.PASSIVE_PROVIDER);
    }

    /**
     * Require {@link Address} from the specified {@link Location} by {@link Geocoder}.<br>
     * If not found any {@link Address}, handler will be receive a empty {@link Address} list.<br>
     * {@link Handler} will receive data from {@link Bundle#getByteArray(String)} with the bundle key.<br>
     * Maybe use helper function {@link BeanUtils#fromByteArray(byte[], Class)} to get data correctly.<br>
     * {@link Message} that {@link Handler} receives with {@link Message#what} is 1 for valid addresses; else will be 0.
     *
     * @param location {@link Location}
     * @param context {@link Context}
     * @param handler {@link Handler} to receive {@link Address}
     * @param locale {@link Locale} to require. NULL for default
     * @param bundleKey
     *      the key to create message for sending to handler.
     *      Handler will be using this key to receive bundle data.
     *      NULL for handler class name
     */
    public static void requireAddress(
            final Context context, final Location location, final Locale locale, final Handler handler,
            final String bundleKey) {
        Assert.notNull(context, "context");
        Assert.notNull(location, "location");
        Assert.notNull(handler, "handler");
        Thread thread = new Thread() {

            /*
             * (Non-Javadoc)
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                Locale loc = (locale == null ? Locale.getDefault() : locale);
                String key = (!StringUtils.hasText(bundleKey) ? handler.getClass().getName() : bundleKey);
                Geocoder geocoder = new Geocoder(context, loc);
                Bundle bundle = new Bundle();
                List<Address> addresses = null;
                try {
                    // request addresses
                    addresses = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(), 1);
                } catch (Exception e) {
                    LogUtils.w(e.getMessage());
                } finally {
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    msg.what = (!CollectionUtils.isEmpty(addresses) ? 1 : 0);
                    if (!CollectionUtils.isEmpty(addresses)) {
                        bundle.putByteArray(key, BeanUtils.toByteArray(addresses));
                    }
                    // send to handler
                    msg.sendToTarget();
                }
            }
        };
        thread.start();
    }
}
