/*
 * @(#)NetworkStateReceiver.java 1.0 Oct 5, 2016
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.receivers;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo.State;
import org.nlh4j.android.util.LogUtils;
import org.nlh4j.android.util.NetworkUtils;

/**
 * Network state receiver.<br>
 * Require permissions in AndroidManifest.xml:<br>
 * &lt;uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /&gt;<br>
 * &lt;uses-permission android:name="android.permission.ACCESS_WIFI_STATE" /&gt;<br>
 * and<br>
 * &lt;receiver android:name=".NetworkStateReceiver"&gt;<br>
 *      &lt;intent-filter&gt;<br>
 *              &lt;action android:name="android.net.conn.CONNECTIVITY_CHANGE" /&gt;<br>
 *              &lt;action android:name="android.net.wifi.WIFI_STATE_CHANGED" /&gt;<br>
 *      &lt;/intent-filter&gt;<br>
 * &lt;/receiver&gt;
 *
 * @author Hai Nguyen
 *
 */
public class NetworkStateReceiver extends AbstractBroadcastReceiver {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Network state changed listener
     */
    public static interface NetworkListener extends Serializable {
        /**
         * Fire while network is un-available
         *
         * @param context {@link Context}
         * @param intent received {@link Intent}
         * @param state network state
         */
        void onListen(Context context, Intent intent, State state);
    }
    private WeakReference<NetworkListener> listener;

    /**
     * Initialize a new instance of {@link NetworkStateReceiver}
     */
    public NetworkStateReceiver() {}
    /**
     * Initialize a new instance of {@link NetworkStateReceiver}
     *
     * @param listener listener to fire while network is un-available
     */
    public NetworkStateReceiver(NetworkListener listener) {
        this.setListener(listener);
    }

    /**
     * Get the listener
     *
     * @return the listener
     */
    public NetworkListener getListener() {
        return (this.listener != null ? this.listener.get() : null);
    }
    /**
     * Set the listener
     *
     * @param listener the listener to set
     */
    public void setListener(NetworkListener listener) {
        this.listener = null;
        if (listener != null) {
            this.listener = new WeakReference<NetworkListener>(listener);
        }
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.android.receivers.AbstractBroadcastReceiver#receive(android.content.Context, android.content.Intent)
     */
    @Override
    protected void receive(Context context, Intent intent) {
        State state = NetworkUtils.getState(context);
        NetworkListener listener = this.getListener();
        LogUtils.d("Network State: {0}", state.name());
        if (listener != null) {
            this.getListener().onListen(context, intent, state);
        } else {
            this.onListen(context, intent, state);
        }
    }

    /**
     * Perform actions while receiving network state has been changed.<br>
     * TODO Children classes maybe override this method for perform some actions when {@link #getListener()} is NULL.<br>
     * <b>This method only be called when {@link #getListener()} is NULL</b>
     *
     * @param context {@link Context}
     * @param intent {@link Intent}
     * @param state network state
     */
    protected void onListen(Context context, Intent intent, State state) {}
}
