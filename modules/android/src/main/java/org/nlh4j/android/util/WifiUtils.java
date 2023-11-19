/*
 * @(#)WifiUtils.java 1.0 Nov 14, 2016
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import org.nlh4j.android.enums.WifiState;

/**
 * Wifi Utilities
 *
 * @author Hai Nguyen
 *
 */
public final class WifiUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Get a boolean value indicating that wifi has been enabled
     *
     * @param context {@link Context}
     *
     * @return true for enabled; else false
     */
    public static boolean isWifiEnabled(Context context) {
        WifiManager wm = ServiceUtils.getWifiManager(context);
        return (wm == null ? false : wm.isWifiEnabled());
    }
    /**
     * Turn wifi on/off
     *
     * @param context {@link Context}
     * @param onOff true for turning on; else off
     *
     * @return true for successful; else false
     */
    public static boolean turnWifi(Context context, boolean onOff) {
        WifiManager wm = ServiceUtils.getWifiManager(context);
        if (wm != null) {
            try { return wm.setWifiEnabled(onOff); }
            catch (Exception e) {
                LogUtils.w(e.getMessage());
            }
        }
        return false;
    }

    /**
     * Get wifi state
     *
     * @param context {@link Context}
     *
     * @return wifi state
     */
    public static WifiState getState(Context context) {
        WifiManager wm = ServiceUtils.getWifiManager(context);
        return (wm == null ? WifiState.UNKNOWN : WifiState.valueOf(wm.getWifiState()));
    }

    /**
     * Get wifi connection information
     *
     * @param context {@link Context}
     *
     * @return wifi connection information or NULL if failed
     */
    public static WifiInfo getConnectionInfo(Context context) {
        WifiManager wm = ServiceUtils.getWifiManager(context);
        try {
            return (wm == null ? null : wm.getConnectionInfo());
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return null;
        }
    }

    /**
     * Get DHCP information
     *
     * @param context {@link Context}
     *
     * @return DHCP information or NULL if failed
     */
    public static DhcpInfo getDhcpInfo(Context context) {
        WifiManager wm = ServiceUtils.getWifiManager(context);
        try {
            return (wm == null ? null : wm.getDhcpInfo());
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return null;
        }
    }
}
