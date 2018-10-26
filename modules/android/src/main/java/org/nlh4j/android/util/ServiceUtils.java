/*
 * @(#)ServiceUtils.java 1.0 Nov 6, 2016
 * Copyright 2016 by SystemEXE Inc. All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.Serializable;

import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.app.WallpaperManager;
import android.app.admin.DevicePolicyManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.hardware.SensorManager;
import android.hardware.input.InputManager;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaRouter;
import android.net.ConnectivityManager;
import android.net.nsd.NsdManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.NfcManager;
import android.os.DropBoxManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.storage.StorageManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.TextServicesManager;

/**
 * System service utilities
 *
 * @author Hai Nguyen
 *
 */
public final class ServiceUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Get the {@link ConnectivityManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link ConnectivityManager} system service from the specified {@link Context}
     */
    public static ConnectivityManager getConnectivityManager(Context context) {
        return SystemUtils.getSystemService(context, Context.CONNECTIVITY_SERVICE, ConnectivityManager.class);
    }
    /**
     * Get the {@link PowerManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link PowerManager} system service from the specified {@link Context}
     */
    public static PowerManager getPowerManager(Context context) {
        return SystemUtils.getSystemService(context, Context.POWER_SERVICE, PowerManager.class);
    }
    /**
     * Get the {@link WindowManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link WindowManager} system service from the specified {@link Context}
     */
    public static WindowManager getWindowManager(Context context) {
        return SystemUtils.getSystemService(context, Context.WINDOW_SERVICE, WindowManager.class);
    }
    /**
     * Get the {@link LayoutInflater} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link LayoutInflater} system service from the specified {@link Context}
     */
    public static LayoutInflater getLayoutInflater(Context context) {
        return SystemUtils.getSystemService(context, Context.LAYOUT_INFLATER_SERVICE, LayoutInflater.class);
    }
    /**
     * Get the {@link AccountManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link AccountManager} system service from the specified {@link Context}
     */
    public static AccountManager getAccountManager(Context context) {
        return SystemUtils.getSystemService(context, Context.ACCOUNT_SERVICE, AccountManager.class);
    }
    /**
     * Get the {@link ActivityManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link ActivityManager} system service from the specified {@link Context}
     */
    public static ActivityManager getActivityManager(Context context) {
        return SystemUtils.getSystemService(context, Context.ACTIVITY_SERVICE, ActivityManager.class);
    }
    /**
     * Get the {@link AlarmManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link AlarmManager} system service from the specified {@link Context}
     */
    public static AlarmManager getAlarmManager(Context context) {
        return SystemUtils.getSystemService(context, Context.ALARM_SERVICE, AlarmManager.class);
    }
    /**
     * Get the {@link NotificationManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link NotificationManager} system service from the specified {@link Context}
     */
    public static NotificationManager getNotificationManager(Context context) {
        return SystemUtils.getSystemService(context, Context.NOTIFICATION_SERVICE, NotificationManager.class);
    }
    /**
     * Get the {@link AccessibilityManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link AccessibilityManager} system service from the specified {@link Context}
     */
    public static AccessibilityManager getAccessibilityManager(Context context) {
        return SystemUtils.getSystemService(context, Context.ACCESSIBILITY_SERVICE, AccessibilityManager.class);
    }
    /**
     * Get the {@link KeyguardManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link KeyguardManager} system service from the specified {@link Context}
     */
    public static KeyguardManager getKeyguardManager(Context context) {
        return SystemUtils.getSystemService(context, Context.KEYGUARD_SERVICE, KeyguardManager.class);
    }
    /**
     * Get the {@link LocationManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link LocationManager} system service from the specified {@link Context}
     */
    public static LocationManager getLocationManager(Context context) {
        return SystemUtils.getSystemService(context, Context.LOCATION_SERVICE, LocationManager.class);
    }
    /**
     * Get the {@link SearchManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link SearchManager} system service from the specified {@link Context}
     */
    public static SearchManager getSearchManager(Context context) {
        return SystemUtils.getSystemService(context, Context.SEARCH_SERVICE, SearchManager.class);
    }
    /**
     * Get the {@link SensorManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link SensorManager} system service from the specified {@link Context}
     */
    public static SensorManager getSensorManager(Context context) {
        return SystemUtils.getSystemService(context, Context.SENSOR_SERVICE, SensorManager.class);
    }
    /**
     * Get the {@link StorageManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link StorageManager} system service from the specified {@link Context}
     */
    public static StorageManager getStorageManager(Context context) {
        return SystemUtils.getSystemService(context, Context.STORAGE_SERVICE, StorageManager.class);
    }
    /**
     * Get the {@link WallpaperManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link WallpaperManager} system service from the specified {@link Context}
     */
    public static WallpaperManager getWallpaperManager(Context context) {
        return SystemUtils.getSystemService(context, Context.WALLPAPER_SERVICE, WallpaperManager.class);
    }
    /**
     * Get the {@link Vibrator} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link Vibrator} system service from the specified {@link Context}
     */
    public static Vibrator getVibrator(Context context) {
        return SystemUtils.getSystemService(context, Context.VIBRATOR_SERVICE, Vibrator.class);
    }
    /**
     * Get the {@link WifiManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link WifiManager} system service from the specified {@link Context}
     */
    public static WifiManager getWifiManager(Context context) {
        return SystemUtils.getSystemService(context, Context.WIFI_SERVICE, WifiManager.class);
    }
    /**
     * Get the {@link WifiP2pManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link WifiP2pManager} system service from the specified {@link Context}
     */
    public static WifiP2pManager getWifiP2pManager(Context context) {
        return SystemUtils.getSystemService(context, Context.WIFI_P2P_SERVICE, WifiP2pManager.class);
    }
    /**
     * Get the {@link NsdManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link NsdManager} system service from the specified {@link Context}
     */
    public static NsdManager getNsdManager(Context context) {
        return SystemUtils.getSystemService(context, Context.NSD_SERVICE, NsdManager.class);
    }
    /**
     * Get the {@link AudioManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link AudioManager} system service from the specified {@link Context}
     */
    public static AudioManager getAudioManager(Context context) {
        return SystemUtils.getSystemService(context, Context.AUDIO_SERVICE, AudioManager.class);
    }
    /**
     * Get the {@link MediaRouter} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link MediaRouter} system service from the specified {@link Context}
     */
    public static MediaRouter getMediaRouter(Context context) {
        return SystemUtils.getSystemService(context, Context.MEDIA_ROUTER_SERVICE, MediaRouter.class);
    }
    /**
     * Get the {@link TelephonyManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link TelephonyManager} system service from the specified {@link Context}
     */
    public static TelephonyManager getTelephonyManager(Context context) {
        return SystemUtils.getSystemService(context, Context.TELEPHONY_SERVICE, TelephonyManager.class);
    }
    /**
     * Get the {@link ClipboardManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link ClipboardManager} system service from the specified {@link Context}
     */
    public static ClipboardManager getClipboardManager(Context context) {
        return SystemUtils.getSystemService(context, Context.CLIPBOARD_SERVICE, ClipboardManager.class);
    }
    /**
     * Get the {@link android.text.ClipboardManager} system service from the specified {@link Context}.<br>
     * <b>Note: Only for API level 10 or before</b>
     *
     * @param context {@link Context}
     *
     * @return the {@link android.text.ClipboardManager} system service from the specified {@link Context}
     */
    @Deprecated
    public static android.text.ClipboardManager getTextClipboardManager(Context context) {
        return getClipboardManager(context);
    }
    /**
     * Get the {@link InputMethodManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link InputMethodManager} system service from the specified {@link Context}
     */
    public static InputMethodManager getInputMethodManager(Context context) {
        return SystemUtils.getSystemService(context, Context.INPUT_METHOD_SERVICE, InputMethodManager.class);
    }
    /**
     * Get the {@link TextServicesManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link TextServicesManager} system service from the specified {@link Context}
     */
    public static TextServicesManager getTextServicesManager(Context context) {
        return SystemUtils.getSystemService(context, Context.TEXT_SERVICES_MANAGER_SERVICE, TextServicesManager.class);
    }
    /**
     * Get the {@link DropBoxManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link DropBoxManager} system service from the specified {@link Context}
     */
    public static DropBoxManager getDropBoxManager(Context context) {
        return SystemUtils.getSystemService(context, Context.DROPBOX_SERVICE, DropBoxManager.class);
    }
    /**
     * Get the {@link DevicePolicyManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link DevicePolicyManager} system service from the specified {@link Context}
     */
    public static DevicePolicyManager getDevicePolicyManager(Context context) {
        return SystemUtils.getSystemService(context, Context.DEVICE_POLICY_SERVICE, DevicePolicyManager.class);
    }
    /**
     * Get the {@link UiModeManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link UiModeManager} system service from the specified {@link Context}
     */
    public static UiModeManager getUiModeManager(Context context) {
        return SystemUtils.getSystemService(context, Context.UI_MODE_SERVICE, UiModeManager.class);
    }
    /**
     * Get the {@link DownloadManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link DownloadManager} system service from the specified {@link Context}
     */
    public static DownloadManager getDownloadManager(Context context) {
        return SystemUtils.getSystemService(context, Context.DOWNLOAD_SERVICE, DownloadManager.class);
    }
    /**
     * Get the {@link NfcManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link NfcManager} system service from the specified {@link Context}
     */
    public static NfcManager getNfcManager(Context context) {
        return SystemUtils.getSystemService(context, Context.NFC_SERVICE, NfcManager.class);
    }
    /**
     * Get the {@link UsbManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link UsbManager} system service from the specified {@link Context}
     */
    public static UsbManager getUsbManager(Context context) {
        return SystemUtils.getSystemService(context, Context.USB_SERVICE, UsbManager.class);
    }
    /**
     * Get the {@link InputManager} system service from the specified {@link Context}
     *
     * @param context {@link Context}
     *
     * @return the {@link InputManager} system service from the specified {@link Context}
     */
    public static InputManager getInputManager(Context context) {
        return SystemUtils.getSystemService(context, Context.INPUT_SERVICE, InputManager.class);
    }
}
