/*
 * @(#)NetworkUtils.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

/**
 * Network utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class NetworkUtils implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    private static final String MAC_PATTERN = "%02X%s";

    /**
     * Get all MAC addresses of all network cards (include the broken network)
     * @return all MAC addresses of all network cards or null/empty if fail
     */
    public static List<String> getMacAddresses() {
        List<String> macs = new LinkedList<String>();
        Enumeration<NetworkInterface> nics = null;
        try {
            nics = NetworkInterface.getNetworkInterfaces();
            while(nics.hasMoreElements()) {
                NetworkInterface nic = nics.nextElement();
                byte[] mac = nic.getHardwareAddress();
                if (!CollectionUtils.isEmpty(mac)) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format(MAC_PATTERN, mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    macs.add(sb.toString().toLowerCase());
                }
            }
        } catch(Exception e) {
            LogUtils.logError(NetworkUtils.class, e);
            if (!CollectionUtils.isEmpty(macs)) {
                macs.clear();
            }
        }
        return macs;
    }

    /**
     * Get all MAC address of all activated network cards (exclude the broken network)
     * @return all MAC address of all activated network cards or null/empty if fail
     */
    public static List<String> getLocalMacAddresses() {
        List<String> macs = new LinkedList<String>();
        InetAddress[] ips = null;
        try {
            ips = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
            for(InetAddress ip : ips) {
                NetworkInterface nic = NetworkInterface.getByInetAddress(ip);
                byte[] mac = nic.getHardwareAddress();
                if (!CollectionUtils.isEmpty(mac)) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format(MAC_PATTERN, mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    macs.add(sb.toString().toLowerCase());
                }
            }
        } catch(Exception e) {
            LogUtils.logError(NetworkUtils.class, e);
            if (!CollectionUtils.isEmpty(macs)) {
                macs.clear();
            }
        }
        return macs;
    }
}
