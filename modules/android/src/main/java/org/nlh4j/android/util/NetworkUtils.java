/*
 * @(#)NetworkUtils.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.telephony.TelephonyManager;

/**
 * Network utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class NetworkUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    /** {@link ConnectivityManager} */
    private static ConnectivityManager connectivityManager;
    /**
     * Get the {@link ConnectivityManager} system service
     *
     * @param context to parse service
     *
     * @return the {@link ConnectivityManager} system service
     */
    private static ConnectivityManager getConnectivityManager(Context context) {
        if (connectivityManager == null) {
            connectivityManager = ServiceUtils.getConnectivityManager(context);
        }
        if (connectivityManager == null) return null;
        synchronized (connectivityManager) {
            return connectivityManager;
        }
    }
    /**
     * Get the activated {@link NetworkInfo}
     *
     * @param context to parse service
     *
     * @return the activated {@link NetworkInfo}
     */
    private static NetworkInfo getActiveNetwork(Context context) {
        ConnectivityManager cm = getConnectivityManager(context);
        return (cm != null ? cm.getActiveNetworkInfo() : null);
    }
    /**
     * Get the activated {@link NetworkInfo}
     *
     * @param context to parse service
     * @param networkType network type
     *
     * @return the activated {@link NetworkInfo}
     */
    private static NetworkInfo getNetworkInfo(Context context, int networkType) {
        ConnectivityManager cm = getConnectivityManager(context);
        return (cm != null ? cm.getNetworkInfo(networkType) : null);
    }

    /**
     * Get network state.<br>
     * <i>&lt;uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /&gt;</i><br>
     * <i>&lt;uses-permission android:name="android.permission.ACCESS_WIFI_STATE" /&gt;</i><br>
     * <p><i>
     * &lt;intent-filter&gt;<br>
     * &lt;action android:name="android.net.wifi.WIFI_STATE_CHANGED"/&gt;<br>
     * &lt;action android:name="android.net.wifi.STATE_CHANGE"/&gt;<br>
     * &lt;/intent-filter&gt;
     * </i></p>
     *
     * @param context {@link Context}
     *
     * @return network state
     */
    public static State getState(Context context) {
        NetworkInfo net = getActiveNetwork(context);
        return (net != null ? net.getState() : State.UNKNOWN);
    }

    /**
     * Get a boolean value indicating that network has been connected
     *
     * @param context {@link Context}
     *
     * @return true for connected, else false
     */
    public static boolean isConnected(Context context) {
        NetworkInfo net = getActiveNetwork(context);
        boolean connected = (net == null ? false : net.isConnected());
        if (!connected) connected = isConnectedWifi(context);
        if (!connected) connected = isConnectedMobile(context);
        if (!connected) connected = isConnectedBluetooth(context);
        return connected;
    }

    /**
     * Get a boolean value indicating that network has been connected via WIFI network
     *
     * @param context {@link Context}
     *
     * @return true for connected via WIFI network; else false
     */
    public static boolean isConnectedWifi(Context context){
        NetworkInfo net = getActiveNetwork(context);
        boolean connected = (net != null && net.isConnected() && net.getType() == ConnectivityManager.TYPE_WIFI);
        if (!connected) {
            net = getNetworkInfo(context, ConnectivityManager.TYPE_WIFI);
            connected = (net != null && net.isConnected());
        }
        return connected;
    }
    /**
     * Get a boolean value indicating that network has been connected via data (mobile/3G) network
     *
     * @param context {@link Context}
     *
     * @return true for connected via data (mobile/3G) network; else false
     */
    public static boolean isConnectedMobile(Context context){
        NetworkInfo net = getActiveNetwork(context);
        boolean connected = (net != null && net.isConnected() && net.getType() == ConnectivityManager.TYPE_MOBILE);
        if (!connected) {
            net = getNetworkInfo(context, ConnectivityManager.TYPE_MOBILE);
            connected = (net != null && net.isConnected());
        }
        return connected;
    }
    /**
     * Get a boolean value indicating that network has been connected via bluetooth network
     *
     * @param context {@link Context}
     *
     * @return true for connected via bluetooth network; else false
     */
    public static boolean isConnectedBluetooth(Context context){
        NetworkInfo net = getActiveNetwork(context);
        boolean connected = (net != null && net.isConnected() && net.getType() == ConnectivityManager.TYPE_BLUETOOTH);
        if (!connected) {
            net = getNetworkInfo(context, ConnectivityManager.TYPE_BLUETOOTH);
            connected = (net != null && net.isConnected());
        }
        return connected;
    }

    /**
     * Get a boolean value indicating that network has been connected via fast network
     *
     * @param context {@link Context}
     *
     * @return true for connected via fast network; else false
     */
    public static boolean isConnectedFast(Context context){
        NetworkInfo net = getActiveNetwork(context);
        return (net != null && net.isConnected() && isConnectionFast(net.getType(), net.getSubtype()));
    }

    /**
     * Get a boolean value indicating that the network type/sub-type whether is fast network
     *
     * @param type network type
     * @param subType network sub-type
     *
     * @return true for fast network; else false
     */
    public static boolean isConnectionFast(int type, int subType){
        switch(type) {
            case ConnectivityManager.TYPE_WIFI:
            case ConnectivityManager.TYPE_WIMAX:
                return true;
            case ConnectivityManager.TYPE_MOBILE:
            case ConnectivityManager.TYPE_MOBILE_DUN:
            case ConnectivityManager.TYPE_MOBILE_HIPRI:
            case ConnectivityManager.TYPE_MOBILE_MMS:
            case ConnectivityManager.TYPE_MOBILE_SUPL:
                switch(subType) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        return false; // ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        return false; // ~ 14-64 kbps
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        return false; // ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        return true; // ~ 400-1000 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        return true; // ~ 600-1400 kbps
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        return false; // ~ 100 kbps
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        return true; // ~ 2-14 Mbps
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        return true; // ~ 700-1700 kbps
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        return true; // ~ 1-23 Mbps
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        return true; // ~ 400-7000 kbps
                    /*
                     * Above API level 7, make sure to set android:targetSdkVersion
                     * to appropriate level to use these
                     */
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                        return true; // ~ 1-2 Mbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                        return true; // ~ 5 Mbps
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                        return true; // ~ 10-20 Mbps
                    case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                        return false; // ~25 kbps
                    case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                        return true; // ~ 10+ Mbps
                    // Unknown
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    default:
                        return false;
                }
            default:
                return false;
        }
    }
}
