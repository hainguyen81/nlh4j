/*
 * @(#)AbstractEntity.java 1.0 Oct 26, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.db;

import java.io.Serializable;

import org.nlh4j.android.util.ResourceUtils;
import org.nlh4j.util.BeanUtils;

/**
 * Abstract entity
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public abstract class AbstractEntity implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Get resource string value
     *
     * @param resId resource identity
     * @param args resource string format arguments
     *
     * @return resource string value
     */
    public static String getResourceString(int resId, Object...args) {
        return ResourceUtils.getResourceString(resId, args);
    }
    /**
     * Get resource string value
     *
     * @param resId resource identity
     *
     * @return resource string value
     */
    public static String getResourceString(int resId) {
        return ResourceUtils.getResourceString(resId);
    }
    /**
     * Get resource string value
     *
     * @param resId resource identity
     * @param argsFromRes resource identity format arguments
     *
     * @return resource string value
     */
    public static String getResourceString(int resId, Integer...argsFromRes) {
        return ResourceUtils.getResourceString(resId, argsFromRes);
    }

    /* (Non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return BeanUtils.toString(this);
    }
}
