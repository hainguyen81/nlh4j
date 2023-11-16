/*
 * @(#)ContentValuesUtils.java 1.0 Oct 5, 2016
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.StreamUtils;
import org.nlh4j.util.StringUtils;

import android.content.ContentValues;

/**
 * {@link ContentValues} utilities
 *
 * @author Hai Nguyen
 *
 */
public final class ContentValuesUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Map key + value to ContentValues.
     * Convenient method to map generic object to a ContentValues.
     *
     * @param contentValues content values to map these key + value
     * @param key the key
     * @param value value.
     */
    public static void put(ContentValues contentValues, String key, Object value) {
        if (value == null
                || (BeanUtils.isInstanceOf(value, String.class)
                        && !StringUtils.hasLength((String) value))) {
            contentValues.putNull(key);
        } else if (BeanUtils.isInstanceOf(value, String.class)) {
            contentValues.put(key, (String) value);
        } else if (BeanUtils.isInstanceOf(value, Integer.class)) {
            contentValues.put(key, (Integer) value);
        } else if (BeanUtils.isInstanceOf(value, Boolean.class)) {
            contentValues.put(key, (Boolean) value);
        } else if (BeanUtils.isInstanceOf(value, Byte.class)) {
            contentValues.put(key, (Byte) value);
        } else if (BeanUtils.isInstanceOf(value, byte[].class)) {
            contentValues.put(key, (byte[]) value);
        } else if (BeanUtils.isInstanceOf(value, Double.class)) {
            contentValues.put(key, (Double) value);
        } else if (BeanUtils.isInstanceOf(value, BigDecimal.class)) {
            contentValues.put(key, ((BigDecimal) value).doubleValue());
        } else if (BeanUtils.isInstanceOf(value, Float.class)) {
            contentValues.put(key, (Float) value);
        } else if (BeanUtils.isInstanceOf(value, Long.class)) {
            contentValues.put(key, (Long) value);
        } else if (BeanUtils.isInstanceOf(value, BigInteger.class)) {
            contentValues.put(key, ((BigInteger) value).toByteArray());
        } else if (BeanUtils.isInstanceOf(value, Short.class)) {
            contentValues.put(key, (Short) value);
        } else if (BeanUtils.isInstanceOf(value, Date.class)) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            contentValues.put(key, df.format((Date) value));
        } else {
            // try value to bytes array
            ByteArrayOutputStream bos = null;
            ObjectOutputStream oos = null;
            try {
                bos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(bos);
                oos.writeObject(value);
                oos.flush();
                contentValues.put(key, bos.toByteArray());
            } catch (Exception e) {
                LogUtils.e(e.getMessage(), e);
                throw new ApplicationRuntimeException(new IllegalArgumentException("Unmapped"));
            } finally {
                StreamUtils.closeQuitely(bos);
                StreamUtils.closeQuitely(oos);
            }
        }
    }
}
