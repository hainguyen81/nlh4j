/*
 * @(#)TelephonyUtils.java 1.0 Nov 7, 2016 Copyright 2016 by SystemEXE Inc. All
 * rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;

import android.content.Context;
import android.telephony.TelephonyManager;
import org.nlh4j.android.enums.TelephonyActivityType;
import org.nlh4j.android.enums.TelephonyCallState;
import org.nlh4j.android.enums.TelephonyDataState;
import org.nlh4j.android.enums.TelephonyNetworkType;
import org.nlh4j.android.enums.TelephonySimState;
import org.nlh4j.android.enums.TelephonyType;

/**
 * {@link TelephonyManager} utilities
 *
 * @author Hai Nguyen
 */
public final class TelephonyUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Get the device software version.
     *
     * @param context {@link Context}
     *
     * @return the device software version.
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        return (tm == null ? null : tm.getDeviceId());
    }

    /**
     * Get the device software version.
     *
     * @param context {@link Context}
     *
     * @return the device software version.
     */
    public static String getDeviceSoftwareVersion(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        return (tm == null ? null : tm.getDeviceSoftwareVersion());
    }

    /**
     * Get the network operator
     *
     * @param context {@link Context}
     *
     * @return the network operator
     */
    public static String getNetworkOperator(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return (tm == null ? null : tm.getNetworkOperator());
    }

    /**
     * Get the carrier(operator) name
     *
     * @param context {@link Context}
     *
     * @return the carrier(operator) name
     */
    public static String getNetworkOperatorName(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        return (tm == null ? null : tm.getNetworkOperatorName());
    }

    /**
     * Get the network country ISO name.
     *
     * @param context {@link Context}
     *
     * @return the network country ISO name.
     */
    public static String getNetworkContryIso(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        return (tm == null ? null : tm.getNetworkCountryIso());
    }

    /**
     * Get country ISO name for the user using the SIM card
     *
     * @param context {@link Context}
     *
     * @return ISO name for the country
     */
    public static String getSimCountryIso(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        return (tm == null ? null : tm.getSimCountryIso());
    }

    /**
     * Get the carrier(operator) name form SIM card
     *
     * @param context {@link Context}
     *
     * @return the carrier(operator) name form SIM card
     */
    public static String getSimOperatorName(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        return (tm == null ? null : tm.getSimOperatorName());
    }

    /**
     * Get the carrier(operator) form SIM card
     *
     * @param context {@link Context}
     *
     * @return the carrier(operator) form SIM card
     */
    public static String getSimOperator(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        return (tm == null ? null : tm.getSimOperator());
    }

    /**
     * Get the SIM card serial number
     *
     * @param context {@link Context}
     *
     * @return the SIM card serial number
     */
    public static String getSimSerialNumber(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        return (tm == null ? null : tm.getSimSerialNumber());
    }

    /**
     * Get the subscriber identity
     *
     * @param context {@link Context}
     *
     * @return the subscriber identity
     */
    public static String getSubscriberId(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        return (tm == null ? null : tm.getSubscriberId());
    }

    /**
     * Get the voice mail number
     *
     * @param context {@link Context}
     *
     * @return the voice mail number
     */
    public static String getVoiceMailNumber(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        return (tm == null ? null : tm.getVoiceMailNumber());
    }

    /**
     * Get a boolean indicating that network roaming.
     *
     * @param context {@link Context}
     *
     * @return true for roaming; else false.
     */
    public static boolean isNetworkRoaming(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        return (tm == null ? false : tm.isNetworkRoaming());
    }

    /**
     * Get a boolean indicating that having ICC Card.
     *
     * @param context {@link Context}
     *
     * @return true for having ICC Card; else false.
     */
    public static boolean hasIccCard(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        return (tm == null ? false : tm.hasIccCard());
    }

    /**
     * Get the mobile network type
     *
     * @param context {@link Context}
     *
     * @return the mobile network type
     */
    public TelephonyNetworkType getNetworkType(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        TelephonyNetworkType type = TelephonyNetworkType.UNKNOWN;
        if (tm != null) {
            switch(tm.getNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    type = TelephonyNetworkType.M2G;
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    type = TelephonyNetworkType.M3G;
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    type = TelephonyNetworkType.M4G;
                    break;
            }
        }
        return type;
    }

    /**
     * Get the mobile type
     *
     * @param context {@link Context}
     *
     * @return the mobile type
     */
    public TelephonyType getPhoneType(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        TelephonyType type = TelephonyType.UNKNOWN;
        if (tm != null) {
            switch(tm.getPhoneType()) {
                case TelephonyManager.PHONE_TYPE_GSM:
                    type = TelephonyType.GSM;
                    break;
                case TelephonyManager.PHONE_TYPE_CDMA:
                    type = TelephonyType.CDMA;
                    break;
                case TelephonyManager.PHONE_TYPE_SIP:
                    type = TelephonyType.SIP;
                    break;
            }
        }
        return type;
    }

    /**
     * Get the mobile SIM state
     *
     * @param context {@link Context}
     *
     * @return the mobile SIM state
     */
    public TelephonySimState getSimState(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        TelephonySimState state = TelephonySimState.UNKNOWN;
        if (tm != null) {
            switch(tm.getSimState()) {
                case TelephonyManager.SIM_STATE_ABSENT:
                    state = TelephonySimState.ABSENT;
                    break;
                case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                    state = TelephonySimState.PIN_REQUIRED;
                    break;
                case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                    state = TelephonySimState.PUK_REQUIRED;
                    break;
                case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                    state = TelephonySimState.NETWORK_LOCKED;
                    break;
                case TelephonyManager.SIM_STATE_READY:
                    state = TelephonySimState.READY;
                    break;
            }
        }
        return state;
    }

    /**
     * Get the mobile call state
     *
     * @param context {@link Context}
     *
     * @return the mobile call state
     */
    public TelephonyCallState getCallState(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        TelephonyCallState state = TelephonyCallState.UNKNOWN;
        if (tm != null) {
            switch(tm.getCallState()) {
                case TelephonyManager.CALL_STATE_IDLE:
                    state = TelephonyCallState.IDLE;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    state = TelephonyCallState.OFFHOOK;
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    state = TelephonyCallState.RINGING;
                    break;
            }
        }
        return state;
    }

    /**
     * Get the mobile data activity type
     *
     * @param context {@link Context}
     *
     * @return the mobile data activity type
     */
    public TelephonyActivityType getDataActivity(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        TelephonyActivityType type = TelephonyActivityType.UNKNOWN;
        if (tm != null) {
            switch(tm.getDataActivity()) {
                case TelephonyManager.DATA_ACTIVITY_DORMANT:
                    type = TelephonyActivityType.DORMANT;
                    break;
                case TelephonyManager.DATA_ACTIVITY_IN:
                    type = TelephonyActivityType.IN;
                    break;
                case TelephonyManager.DATA_ACTIVITY_INOUT:
                    type = TelephonyActivityType.INOUT;
                    break;
                case TelephonyManager.DATA_ACTIVITY_OUT:
                    type = TelephonyActivityType.OUT;
                    break;
            }
        }
        return type;
    }

    /**
     * Get the mobile data state
     *
     * @param context {@link Context}
     *
     * @return the mobile data state
     */
    public TelephonyDataState getDataState(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        TelephonyDataState state = TelephonyDataState.UNKNOWN;
        if (tm != null) {
            switch(tm.getDataState()) {
                case TelephonyManager.DATA_CONNECTED:
                    state = TelephonyDataState.CONNECTED;
                    break;
                case TelephonyManager.DATA_CONNECTING:
                    state = TelephonyDataState.CONNECTING;
                    break;
                case TelephonyManager.DATA_DISCONNECTED:
                    state = TelephonyDataState.DISCONNECTED;
                    break;
                case TelephonyManager.DATA_SUSPENDED:
                    state = TelephonyDataState.SUSPENDED;
                    break;
            }
        }
        return state;
    }

    /**
     * Get the line 1 phone number.
     *
     * @param context {@link Context}
     *
     * @return the line 1 phone number.
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager tm = ServiceUtils.getTelephonyManager(context);
        return (tm == null ? null : tm.getLine1Number());
    }
}
