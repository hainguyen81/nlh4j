/*
 * @(#)AbstractBroadcastReceiver.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.receivers;

import java.io.Serializable;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Abstract {@link BroadcastReceiver}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public abstract class AbstractBroadcastReceiver extends BroadcastReceiver implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /* (Non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    @Override
    public final void onReceive(Context context, Intent intent) {
        // check intent
        if (this.validIntent(intent)) {
            // receive
            this.receive(context, intent);
        }
    }
    /**
     * Perform actions while receiving a broadcast
     *
     * @param context {@link Context}
     * @param intent {@link Intent}
     */
    protected abstract void receive(Context context, Intent intent);

    /**
     * Get a boolean value indicating that the specified {@link Intent} whether is valid.<br>
     * Default is <b>true</b><br>
     * TODO Children class maybe override this method to check correctly {@link Intent} if necessary
     *
     * @param intent to check
     *
     * @return true for valid; else false
     */
    protected boolean validIntent(Intent intent) {
        return Boolean.TRUE;
    }
}
