/*
 * @(#)LocationService.java 1.0 Nov 10, 2016
 * Copyright 2016 by SystemEXE Inc. All rights reserved.
 */
package org.nlh4j.android.services;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import org.nlh4j.android.util.AlertUtils;
import org.nlh4j.android.util.LocationUtils;
import org.nlh4j.android.util.LogUtils;
import org.nlh4j.android.util.ServiceUtils;
import org.nlh4j.android.util.SystemUtils;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.StringUtils;

/**
 * {@link Location} service.<br>
 * Require permissions in AndroidManifest.xml:<br>
 * &lt;uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /&gt;<br>
 * &lt;uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" /&gt;
 *
 * @author Hai Nguyen
 *
 */
public abstract class LocationService extends AbstractService implements LocationListener {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    protected static final int REQUEST_LOCATION_RESPONSE_CODE = 200;
    private static final String REQUEST_ADDRESS_KEY = "Addresses";
    /** 1 minute */
    protected static final long MIN_TIME_LOCATION_UPDATES = (1000 * 60);
    /** 10 meters */
    protected static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    /** {@link LocationManager} */
    private LocationManager locationManager;
    /** {@link Address} list from {@link Location} via {@link Geocoder} */
    private List<Address> locationAddresses;
    /** last known {@link Location} */
    private Location location;

    /**
     * Handler to receive address of {@link Location} from {@link Geocoder}
     */
    @SuppressWarnings("unchecked")
    protected class GeoCoderHandler extends Handler implements Serializable {

        /**
         * default serial version id
         */
        private static final long serialVersionUID = 1L;

        /*
         * (Non-Javadoc)
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            if (LocationService.this.locationAddresses == null) {
                LocationService.this.locationAddresses = new LinkedList<Address>();
            } else {
                LocationService.this.locationAddresses.clear();
            }
            switch (message.what) {
                case 1:
                    byte[] data = bundle.getByteArray(REQUEST_ADDRESS_KEY);
                    if (!CollectionUtils.isEmpty(data)) {
                        List<Address> addresses = null;
                        try {
                            addresses = (List<Address>) BeanUtils.fromByteArray(data, List.class);
                        } catch(Exception e) {
                            LogUtils.w(e.getMessage());
                            addresses = null;
                        }
                        if (!CollectionUtils.isEmpty(addresses)) {
                            LocationService.this.locationAddresses.addAll(addresses);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Initialize a new instance of {@link LocationService}
     *
     * @param context {@link Context}
     */
    public LocationService(Context context) {
        super(context);
    }

    /**
     * Get the location manager
     *
     * @return the location manager
     */
    protected final LocationManager getLocationManager() {
        if (this.locationManager == null) {
            this.locationManager = ServiceUtils.getLocationManager(super.getContext());
        }
        if (this.locationManager == null) return null;
        synchronized (this.locationManager) {
            return this.locationManager;
        }
    }
    /**
     * Get the location address from {@link Geocoder}
     *
     * @return the location address
     */
    public final List<Address> getLocationAddress() {
        if (this.locationAddresses == null) {
            this.locationAddresses = new LinkedList<Address>();
        }
        return Collections.unmodifiableList(this.locationAddresses);
    }

    /**
     * Get the location (every call this method, location will also be required updating)
     *
     * @return the location
     */
    public final Location getLocation() {
        // check provider
        this.checkProvider();
        return this.location;
    }

    /**
     * Get the minimum time for requesting location updates (milliseconds).<br>
     * Always must be equals or greater than 1 minute.<br>
     * TODO Children class maybe override this method for changing the time
     *
     * @return the minimum time for requesting location updates (milliseconds)
     */
    public long getMinTimeLocationUpdates() {
        return MIN_TIME_LOCATION_UPDATES;
    }

    /**
     * Get the minimum distance for requesting location updates (meters).<br>
     * TODO Children class maybe override this method for changing the distance
     *
     * @return the minimum distance for requesting location updates (meters)
     */
    public long getMinDistanceLocationUpdates() {
        return MIN_DISTANCE_CHANGE_FOR_UPDATES;
    }

    /**
     * Get a boolean value indicating that should be required address of {@link Location} from {@link Geocoder}
     *
     * @return true for requiring; else false
     */
    protected abstract boolean requiredAddress();
    /**
     * Get a boolean value indicating that should showing alert dialog on requiring {@link Location} failed
     *
     * @return true for showing; else false
     */
    protected abstract boolean alertOnFailed();
    /**
     * Get message resource identity to show while requiring {@link Location} on failed.<br>
     * Only applying on {@link #alertOnFailed()} is <b>TRUE</b>
     *
     * @return message resource identity
     */
    protected abstract int getMessageOnFailed();
    /**
     * Get positive button resource identity to show while requiring {@link Location} on failed.<br>
     * Only applying on {@link #alertOnFailed()} is <b>TRUE</b>
     *
     * @return positive button resource identity
     */
    protected abstract int getPositiveOnFailed();
    /**
     * Get negative button resource identity to show while requiring {@link Location} on failed.<br>
     * Only applying on {@link #alertOnFailed()} is <b>TRUE</b>
     *
     * @return negative button resource identity
     */
    protected abstract int getNegativeOnFailed();

    /* (Non-Javadoc)
     * @see android.location.LocationListener#onLocationChanged(android.location.Location)
     */
    @Override
    public final void onLocationChanged(Location location) {
        // by class
        this.location = location;
        this.changedLocation(location);
    }
    /**
     * Perform actions while location has been changed
     *
     * @param location new location
     */
    protected abstract void changedLocation(Location location);

    /* (Non-Javadoc)
     * @see android.location.LocationListener#onStatusChanged(java.lang.String, int, android.os.Bundle)
     */
    @Override
    public final void onStatusChanged(String provider, int status, Bundle extras) {
        // by class
        this.changedStatus(provider, status, extras);
    }
    /**
     * Perform actions while status has been changed
     *
     * @param provider location provider
     * @param status status
     * @param extras bundle data
     */
    protected abstract void changedStatus(String provider, int status, Bundle extras);

    /* (Non-Javadoc)
     * @see android.location.LocationListener#onProviderEnabled(java.lang.String)
     */
    @Override
    public final void onProviderEnabled(String provider) {
        // check provider
        this.checkProvider();
        // by class
        this.enabledProvider(provider);
    }
    /**
     * Perform actions while provider has been enabled
     *
     * @param provider location provider
     */
    protected abstract void enabledProvider(String provider);

    /* (Non-Javadoc)
     * @see android.location.LocationListener#onProviderDisabled(java.lang.String)
     */
    @Override
    public final void onProviderDisabled(String provider) {
        // check provider
        this.checkProvider();
        // by class
        this.enabledProvider(provider);
    }
    /**
     * Perform actions while provider has been disabled
     *
     * @param provider location provider
     */
    protected abstract void disabledProvider(String provider);

    /* (Non-Javadoc)
     * @see org.nlh4j.android.services.AbstractService#doCreate()
     */
    @Override
    protected void doCreate() {
        // check permission
        this.requirePermissions();
        // check provider
        this.checkProvider();
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.android.services.AbstractService#doRebind(android.content.Intent)
     */
    @Override
    protected void doRebind(Intent intent) {
        // check permission
        this.requirePermissions();
        // check provider
        this.checkProvider();
    }

    /**
     * Get a boolean value indicating that service whether has permissions to access {@link Location}
     * @return
     */
    public final boolean hasAccessPermissions() {
        int selfFinePerm = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION);
        int selfCoarsePerm = ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return (selfFinePerm == PackageManager.PERMISSION_GRANTED
                || selfCoarsePerm == PackageManager.PERMISSION_GRANTED);
    }
    /**
     * Require permission on {@link Location} accessing
     */
    private void requirePermissions() {
        if (!this.hasAccessPermissions()) {
            Activity activity = SystemUtils.getActivity();
            if (activity != null) {
                ActivityCompat.requestPermissions(
                        activity,
                        new String[] {
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        }, REQUEST_LOCATION_RESPONSE_CODE);
            }
        }
    }

    /**
     * Check any enabled providers; if not found, show alert if necessary
     */
    private void checkProvider() {
        this.location = null;
        String provider = LocationUtils.getAnyProviderEnabled(super.getContext());
        if (!StringUtils.hasText(provider)) {
            this.alertRequiredLocation();
        } else {
            // require update location
            long minTime = this.getMinTimeLocationUpdates();
            minTime = Math.max(minTime, MIN_TIME_LOCATION_UPDATES);
            long minDistance = this.getMinDistanceLocationUpdates();
            minDistance = Math.max(minDistance, 0);
            this.getLocationManager().requestLocationUpdates(provider, minTime, minDistance, this);
            // require last known location
            this.location = this.getLocationManager().getLastKnownLocation(provider);
            // request address
            LocationUtils.requireAddress(
                    super.getContext(), this.location, null,
                    new GeoCoderHandler(), REQUEST_ADDRESS_KEY);
        }
    }
    /**
     * Show {@link AlertDialog} while requiring {@link Location} on failed
     */
    private void alertRequiredLocation() {
        // if not requiring
        if (!alertOnFailed()) return;
        // show alert
        AlertUtils.alertLocationSourceSettings(
                super.getContext(), this.getMessageOnFailed(),
                this.getPositiveOnFailed(), this.getNegativeOnFailed());
    }
}
