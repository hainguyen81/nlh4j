/*
 * @(#)TelephonySimState.java 1.0 Nov 11, 2016
 * Copyright 2016 by SystemEXE Inc. All rights reserved.
 */
package org.nlh4j.android.enums;

/**
 * Mobile SIM state
 *
 * @author Hai Nguyen
 *
 */
public enum TelephonySimState {
    /** Unknown */
    UNKNOWN,
    /** Absent */
    ABSENT,
    /** PIN required */
    PIN_REQUIRED,
    /** PUK required */
    PUK_REQUIRED,
    /** Network locked */
    NETWORK_LOCKED,
    /** Ready */
    READY
}
