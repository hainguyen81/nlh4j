/*
 * @(#)FileUtils.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import android.content.Context;
import android.os.Environment;
import org.nlh4j.util.StringUtils;

/**
 * Android {@link File} utilities
 *
 * @author Hai Nguyen
 */
public final class FileUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_BUFFER_SIZE = (4 * 1024);

    /**
     * Return the name of files and folders in assets
     *
     * @param context {@link Context}
     * @param folder the folder name, if you want to list the root send null
     *
     * @return the name of files and folders in assets or empty array if failed
     */
    public static String[] listAssetsFiles(Context context, String folder) {
        folder = (!StringUtils.hasText(folder) ? "" : folder);
        try { return context.getAssets().list(folder); }
        catch (IOException e) {
            LogUtils.w(e.getMessage());
            return new String[] {};
        }
    }

    /**
     * Gets the data storage directory(pictures dir) for the device. If the external storage is not
     * available, this returns the reserved application data storage directory. SD Card storage will
     * be preferred over internal storage.
     *
     * @param context {@link Context}
     * @param dirName
     *            if the directory name is specified, it is created inside the DIRECTORY_PICTURES
     *            directory.
     * @return Data storage directory on the device. Maybe be a directory on SD Card or internal
     *         storage of the device. NULL if failed
     **/
    public static File getStorageDirectory(Context context, String dirName) {
        File f = null;
        String path = null;
        String exStorage = null;
        try {
            exStorage = Environment.getExternalStorageState();
            // if media
            if (Environment.MEDIA_MOUNTED.equals(exStorage)) {
                path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getPath();
                if (StringUtils.hasText(dirName)) {
                    path += "/" + dirName;
                }

                // application cache path
            } else {
                // media is removed, unmounted etc
                // Store image in
                // /data/data/<package-name>/cache/atemp/photograph.jpeg
                path = context.getCacheDir().getPath();
                if (StringUtils.hasText(dirName)) {
                    path += "/" + dirName;
                }
            }
            // ensure directory
            if (org.nlh4j.util.FileUtils.ensureDirectory(path)) {
                f = new File(path);
            } else {
                LogUtils.w("Could not create directory {0}", new Object[] { path });
            }
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            // delete if failed
            org.nlh4j.util.FileUtils.safeDelete(f);
        }
        return f;
    }
}
