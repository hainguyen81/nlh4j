/*
 * @(#)TelephonyReceiver.java 1.0 Nov 7, 2016
 * Copyright 2016 by SystemEXE Inc. All rights reserved.
 */
package org.nlh4j.android.receivers;

import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import org.nlh4j.android.enums.TelephonyCallState;

/**
 * Telephony receiver.<br>
 * Require permissions in AndroidManifest.xml:<br>
 * &lt;uses-permission android:name="android.permission.READ_PHONE_STATE" /&gt;<br>
 * &lt;uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/&gt;<br>
 * and<br>
 * &lt;receiver android:name=".TelephonyReceiver"&gt;<br>
 *      &lt;intent-filter&gt;<br>
 *          &lt;action android:name="android.intent.action.PHONE_STATE" /&gt;<br>
 *      &lt;/intent-filter&gt;<br>
 *      &lt;intent-filter&gt;<br>
 *          &lt;action android:name="android.intent.action.NEW_OUTGOING_CALL" /&gt;<br>
 *      &lt;/intent-filter&gt;<br>
 * &lt;/receiver&gt;
 *
 * @author Hai Nguyen
 *
 */
public abstract class TelephonyReceiver extends AbstractBroadcastReceiver {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    private TelephonyCallState lastState = TelephonyCallState.UNKNOWN;
    private Date callStartTime;
    private boolean isIncoming = false;
    // because the passed incoming is only valid in ringing
    private String savedNumber;

    /**
     * Raise when an incoming call has been started.
     *
     * @param context {@link Context}
     * @param number incoming number
     * @param start the started date/time of this incoming call
     */
    protected abstract void onIncomingStarted(Context context, String number, Date start);
    /**
     * Raise when an outgoing call has been started.
     *
     * @param context {@link Context}
     * @param number outgoing number
     * @param start the started date/time of this outgoing call
     */
    protected abstract void onOutgoingStarted(Context context, String number, Date start);
    /**
     * Raise when an incoming call has been ended.
     *
     * @param context {@link Context}
     * @param number incoming number
     * @param start the started date/time of this incoming call
     * @param end the ended date/time of this incoming call
     */
    protected abstract void onIncomingEnded(Context context, String number, Date start, Date end);
    /**
     * Raise when an outgoing call has been ended.
     *
     * @param context {@link Context}
     * @param number outgoing number
     * @param start the started date/time of this outgoing call
     * @param end the ended date/time of this outgoing call
     */
    protected abstract void onOutgoingEnded(Context context, String number, Date start, Date end);
    /**
     * Raise when an missed incoming call has been started.
     *
     * @param context {@link Context}
     * @param number incoming number
     * @param start the started date/time of this missed incoming call
     */
    protected abstract void onMissedCall(Context context, String number, Date start);

    /* (Non-Javadoc)
     * @see org.nlh4j.android.receivers.AbstractBroadcastReceiver#receive(android.content.Context, android.content.Intent)
     */
    @Override
    protected void receive(Context context, Intent intent) {
        if (intent == null) return;
        // We listen to two intents. The new outgoing call only tells us of an outgoing call. We use it to get the number.
        Bundle extras = intent.getExtras();
        if ("android.intent.action.NEW_OUTGOING_CALL".equalsIgnoreCase(intent.getAction())) {
            this.savedNumber = (extras == null ? null : extras.getString("android.intent.extra.PHONE_NUMBER"));
        }
        else{
            String extraState = (extras == null ? null : extras.getString(TelephonyManager.EXTRA_STATE));
            String number = (extras == null ? null : extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER));
            TelephonyCallState state = TelephonyCallState.UNKNOWN;
            if (TelephonyManager.EXTRA_STATE_IDLE.equalsIgnoreCase(extraState)) {
                state = TelephonyCallState.IDLE;
            }
            else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equalsIgnoreCase(extraState)) {
                state = TelephonyCallState.OFFHOOK;
            }
            else if (TelephonyManager.EXTRA_STATE_RINGING.equalsIgnoreCase(extraState)) {
                state = TelephonyCallState.RINGING;
            }
            // state changed
            if (this.lastState != state) {
                this.onCallStateChanged(context, state, number);
            }
        }
    }

    /**
     * Raise when call state has been changed
     *
     * @param context {@link Context}
     * @param state call state
     * @param number call number
     */
    protected final void onCallStateChanged(Context context, TelephonyCallState state, String number) {
        // ringing
        if (TelephonyCallState.RINGING.equals(state)) {
            this.isIncoming = true;
            this.callStartTime = new Date();
            this.savedNumber = number;
            // raise incoming has been started
            this.onIncomingStarted(context, number, callStartTime);

            // off-hook
            // Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
        } else if (TelephonyCallState.OFFHOOK.equals(state)
                && !TelephonyCallState.RINGING.equals(this.lastState)) {
            this.isIncoming = false;
            this.callStartTime = new Date();
            this.onOutgoingStarted(context, this.savedNumber, this.callStartTime);

            // IDLE
            // Went to idle - this is the end of a call. What type depends on previous state(s)
        } else if (TelephonyCallState.IDLE.equals(state)) {
            // Ring but no pickup - a miss
            if (TelephonyCallState.RINGING.equals(this.lastState)) {
                this.onMissedCall(context, this.savedNumber, this.callStartTime);
            } else if (this.isIncoming) {
                this.onIncomingEnded(context, this.savedNumber, this.callStartTime, new Date());
            } else {
                this.onOutgoingEnded(context, this.savedNumber, this.callStartTime, new Date());
            }
        }

        // backup last call state
        this.lastState = state;
    }
}
