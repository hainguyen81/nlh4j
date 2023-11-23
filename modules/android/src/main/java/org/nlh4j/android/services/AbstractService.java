/*
 * @(#)AbstractService.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.services;

import java.io.Serializable;

import org.springframework.util.Assert;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Abstract base {@link Service}
 *
 * @author Hai Nguyen
 *
 */
public abstract class AbstractService extends Service implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    /** service context */
    private Context context;
    /** servcie binder */
    private IBinder binder;

    /**
     * Internal {@link Service} binder
     */
    public class ServiceBinder extends Binder implements Serializable {

        /**
         * default serial version id
         */
        private static final long serialVersionUID = 1L;

        /**
         * Get the service
         *
         * @return the service
         */
        public AbstractService getService() {
            return AbstractService.this;
        }
    }

    /**
     * Initialize a new instance of {@link AbstractService}
     *
     */
    protected AbstractService() {}
    /**
     * Initialize a new instance of {@link AbstractService}
     *
     */
    protected AbstractService(Context context) {
        Assert.notNull(context, "context");
        this.context = context;
    }

    /**
     * Get the context
     *
     * @return the context
     */
    public final Context getContext() {
        if (this.context == null) this.context = super.getApplicationContext();
        if (this.context == null) this.context = super.getBaseContext();
        if (this.context == null) return null;
        synchronized (this.context) {
            return this.context;
        }
    }

    /**
     * Create the {@link Service} binder.<br>
     * (Return the communication channel to the service.)<br>
     * TODO Children classes maybe override this method for customizing {@link Service} binder
     *
     * @return the {@link Service} binder or NULL for default {@link ServiceBinder}
     */
    protected IBinder createBinder() { return null; }
    /**
     * Get the binder
     *
     * @return the binder
     */
    protected final IBinder getBinder() {
        if (this.binder == null) {
            this.binder = this.createBinder();
        }
        if (this.binder == null) {
            this.binder = new ServiceBinder();
        }
        synchronized (this.binder) {
            return this.binder;
        }
    }

    /* (Non-Javadoc)
     * @see android.app.Service#onCreate()
     */
    @Override
    public final void onCreate() {
        // call by super class
        super.onCreate();
        // by class
        this.doCreate();
    }
    /**
     * Called by the system when the service is first created.<br>
     * TODO Children class maybe override this method for perform some actions
     */
    protected void doCreate() {}

    /* (Non-Javadoc)
     * @see android.app.Service#onBind(android.content.Intent)
     */
    @Override
    public final IBinder onBind(Intent intent) {
        return this.getBinder();
    }

    /* (Non-Javadoc)
     * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
     */
    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
        return this.doStartCommand(intent, flags, startId);
    }
    /**
     * Called by the system every time a client explicitly starts the service by calling {@link #startService(Intent)},
     * providing the arguments it supplied and a unique integer token representing the start request.<br>
     * TODO Children class maybe override this method for perform some actions
     *
     * @param intent
     *      The Intent supplied to {@link #startService(Intent)}, as given.
     *      This may be null if the service is being restarted after its process has gone away,
     *      and it had previously returned anything except {@link Service#START_STICKY_COMPATIBILITY}.
     * @param flags
     *      Additional data about this start request.
     *      Currently either 0, {@link Service#START_FLAG_REDELIVERY}, or {@link Service#START_FLAG_RETRY}.
     * @param startId
     *      A unique integer representing this specific request to start. Use with {@link #stopSelfResult(int)}.
     *
     * @return The return value indicates what semantics the system should use for the service's current started state.
     * It may be one of the constants associated with the {@link Service#START_CONTINUATION_MASK} bits.
     */
    protected int doStartCommand(Intent intent, int flags, int startId) { return START_NOT_STICKY; }

    /* (Non-Javadoc)
     * @see android.app.Service#onUnbind(android.content.Intent)
     */
    @Override
    public final boolean onUnbind(Intent intent) {
        return this.doUnbind(intent);
    }
    /**
     * Called when all clients have disconnected from a particular interface published by the service.<br>
     * The default implementation does nothing and returns false.<br>
     * TODO Children class maybe override this method for perform some actions
     *
     * @param intent
     *      The {@link Intent} that was used to bind to this service,
     *      as given to {@link Context#bindService(Intent, android.content.ServiceConnection, int)}.
     *      Note that any extras that were included with the Intent at that point will not be seen here.
     *
     * @return Return true if you would like to have the service's {@link #onRebind(Intent)}
     * method later called when new clients bind to it.
     */
    protected boolean doUnbind(Intent intent) { return false; }

    /* (Non-Javadoc)
     * @see android.app.Service#onRebind(android.content.Intent)
     */
    @Override
    public final void onRebind(Intent intent) {
        // call by super class
        super.onRebind(intent);
        // by class
        this.doRebind(intent);
    }
    /**
     * Called when new clients have connected to the service, after it had previously been notified
     * that all had disconnected in its {@link #onUnbind(Intent)}.<br>
     * This will only be called if the implementation of {@link #onUnbind(Intent)} was overridden to return true.<br>
     * TODO Children class maybe override this method for perform some actions
     *
     * @param intent
     *      The Intent that was used to bind to this service,
     *      as given to {@link Context#bindService(Intent, android.content.ServiceConnection, int)}.
     *      Note that any extras that were included with the Intent at that point will not be seen here.
     */
    protected void doRebind(Intent intent) {}

    /* (Non-Javadoc)
     * @see android.app.Service#onDestroy()
     */
    @Override
    public final void onDestroy() {
        // call by super class
        super.onDestroy();
        // by class
        this.doDestroy();
    }
    /**
     * Called by the system to notify a {@link Service} that it is no longer used and is being removed.
     * The service should clean up any resources it holds (threads, registered receivers, etc) at this point.
     * Upon return, there will be no more calls in to this Service object and it is effectively dead.
     * Do not call this method directly.<br>
     * TODO Children class maybe override this method for perform some actions
     */
    protected void doDestroy() {}
}
