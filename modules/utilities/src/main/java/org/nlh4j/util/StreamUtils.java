/*
 * @(#)StreamUtils.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class StreamUtils implements Serializable {
    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_BUFFER_LENGTH = (4 * 1024);

    /**
     * Copy data from the specified {@link InputStream} to the specified file path.<br>
     *
     * @param is {@link InputStream} to read
     * @param filePath file path to write
     * @param buffSize the buffer size. &lt;= 0 for default buffer size (4 * 1024)
     *
     * @return the copied data length
     * @throws IOException thrown if {@link InputStream#read(byte[])} throws exception
     */
    public static long copyStream(InputStream is, String filePath, int buffSize) throws IOException {
        FileOutputStream fos = new FileOutputStream(filePath);
        long length = copyStream(is, fos, buffSize, Boolean.TRUE);
        closeQuitely(fos);
        return length;
    }
    /**
     * Copy data from the specified {@link InputStream} to the specified file path.<br>
     *
     * @param is {@link InputStream} to read
     * @param filePath file path to write
     *
     * @return the copied data length
     * @throws IOException thrown if {@link InputStream#read(byte[])} throws exception
     */
    public static long copyStream(InputStream is, String filePath) throws IOException {
        return copyStream(is, filePath, DEFAULT_BUFFER_LENGTH);
    }
    /**
     * Copy data from the specified {@link InputStream} to {@link File}.<br>
     *
     * @param is {@link InputStream} to read
     * @param f {@link File} to write
     * @param buffSize the buffer size. &lt;= 0 for default buffer size (4 * 1024)
     *
     * @return the copied data length
     * @throws IOException thrown if {@link InputStream#read(byte[])} throws exception
     */
    public static long copyStream(InputStream is, File f, int buffSize) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        long length = copyStream(is, fos, buffSize, Boolean.TRUE);
        closeQuitely(fos);
        return length;
    }
    /**
     * Copy data from the specified {@link InputStream} to {@link File}.<br>
     *
     * @param is {@link InputStream} to read
     * @param f {@link File} to write
     *
     * @return the copied data length
     * @throws IOException thrown if {@link InputStream#read(byte[])} throws exception
     */
    public static long copyStream(InputStream is, File f) throws IOException {
        return copyStream(is, f, DEFAULT_BUFFER_LENGTH);
    }
    /**
     * Copy data from the specified source file path to the specified destination file path.<br>
     *
     * @param fileSrcPath source file path to read
     * @param fileDestPath destination file path to write
     * @param buffSize the buffer size. &lt;= 0 for default buffer size (4 * 1024)
     *
     * @return the copied data length
     * @throws IOException thrown if {@link InputStream#read(byte[])} throws exception
     */
    public static long copyStream(String fileSrcPath, String fileDestPath, int buffSize) throws IOException {
        FileInputStream fis = new FileInputStream(fileSrcPath);
        FileOutputStream fos = new FileOutputStream(fileDestPath);
        long length = copyStream(fis, fos, buffSize, Boolean.TRUE);
        closeQuitely(fos);
        closeQuitely(fis);
        return length;
    }
    /**
     * Copy data from the specified source file path to the specified destination file path.<br>
     *
     * @param fileSrcPath source file path to read
     * @param fileDestPath destination file path to write
     *
     * @return the copied data length
     * @throws IOException thrown if {@link InputStream#read(byte[])} throws exception
     */
    public static long copyStream(String fileSrcPath, String fileDestPath) throws IOException {
        return copyStream(fileSrcPath, fileDestPath, DEFAULT_BUFFER_LENGTH);
    }
    /**
     * Copy data from the specified {@link File} to the specified file path.<br>
     *
     * @param f {@link File} to read
     * @param filePath file path to write
     * @param buffSize the buffer size. &lt;= 0 for default buffer size (4 * 1024)
     *
     * @return the copied data length
     * @throws IOException thrown if {@link InputStream#read(byte[])} throws exception
     */
    public static long copyStream(File f, String filePath, int buffSize) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        FileOutputStream fos = new FileOutputStream(filePath);
        long length = copyStream(fis, fos, buffSize, Boolean.TRUE);
        closeQuitely(fos);
        closeQuitely(fis);
        return length;
    }
    /**
     * Copy data from the specified {@link File} to the specified file path.<br>
     *
     * @param f {@link File} to read
     * @param filePath file path to write
     *
     * @return the copied data length
     * @throws IOException thrown if {@link InputStream#read(byte[])} throws exception
     */
    public static long copyStream(File f, String filePath) throws IOException {
        return copyStream(f, filePath, DEFAULT_BUFFER_LENGTH);
    }
    /**
     * Copy data from the specified {@link InputStream} to {@link OutputStream}.<br>
     * The specified {@link InputStream} and {@link OutputStream} will not be closed,
     * so the caller method must be closed them.
     *
     * @param is {@link InputStream} to read
     * @param os {@link OutputStream} to write
     * @param buffSize the buffer size. &lt;= 0 for default buffer size (4 * 1024)
     * @param flush specify {@link OutputStream} should flush
     *
     * @return the copied data length
     * @throws IOException thrown if {@link InputStream#read(byte[])} throws exception
     */
    public static long copyStream(InputStream is, OutputStream os, int buffSize, boolean flush) throws IOException {
        long dataLength = 0;
        if (is != null && os != null) {
            int length = 0;
            buffSize = NumberUtils.min(
                    new int[] {
                            NumberUtils.max(new int[] { buffSize, 0 }),
                            DEFAULT_BUFFER_LENGTH
                    });
            byte[] buff = new byte[buffSize];
            while((length = is.read(buff)) > 0) {
                os.write(buff, 0, length);
                dataLength += length;
            }
            if (flush) os.flush();
        }
        return dataLength;
    }
    /**
     * Copy data from the specified {@link InputStream} to {@link OutputStream}.<br>
     * The specified {@link InputStream} and {@link OutputStream} will not be closed,
     * so the caller method must be closed them.
     *
     * @param is {@link InputStream} to read
     * @param os {@link OutputStream} to write
     * @param flush specify {@link OutputStream} should flush
     *
     * @return the copied data length
     * @throws IOException thrown if {@link InputStream#read(byte[])} throws exception
     */
    public static long copyStream(InputStream is, OutputStream os, boolean flush) throws IOException {
        return copyStream(is, os, DEFAULT_BUFFER_LENGTH, flush);
    }
    /**
     * Copy data from the specified {@link InputStream} to {@link OutputStream}.<br>
     * The specified {@link InputStream} and {@link OutputStream} will not be closed,
     * so the caller method must be closed them.
     *
     * @param is {@link InputStream} to read
     * @param os {@link OutputStream} to write
     *
     * @return the copied data length
     * @throws IOException thrown if {@link InputStream#read(byte[])} throws exception
     */
    public static long copyStream(InputStream is, OutputStream os) throws IOException {
        return copyStream(is, os, Boolean.TRUE);
    }

    /**
     * Get the {@link InputStream} of the specified file/resource path
     *
     * @param filePathOrResource the file/resource path (maybe try with classpath*)
     *
     * @return the {@link InputStream} of the specified file/resource path
     */
    public static InputStream getFileOrResourceStream(String filePathOrResource) {
        InputStream is = null;
        ClassLoader loader = null;
        if (StringUtils.hasText(filePathOrResource)) {
            // check by file
            File f = new File(filePathOrResource);
            if (f.exists()) {
                try { is = new FileInputStream(f); }
                catch (FileNotFoundException e) {
                	LogUtils.logWarn(StreamUtils.class,
                			"The specified file path [" + filePathOrResource
                			+ "] is not a valid physical path!!!");
                }
            }
            // check by resource
            if (is == null) {
                // resolve resource path
            	Set<String> resourcePaths = StringUtils.resolveResourceNames(filePathOrResource);
                if (!CollectionUtils.isEmpty(resourcePaths)) {
                    for(String resourcePath : resourcePaths) {
                        try {
                            loader = StreamUtils.class.getClassLoader();
                            while(is == null && loader != null) {
                                is = loader.getResourceAsStream(resourcePath);
                                if (is != null) {
                                	LogUtils.logInfo(StreamUtils.class,
                                            "Resolved the specified path ["
                                            + filePathOrResource + "] to [" + resourcePath
                                            + "] from [" + loader.getClass().getName() + "]!!!");
                                    break;
                                } else {
                                	LogUtils.logWarn(StreamUtils.class,
                                            "Could not found the resolved resource path ["
                                            + resourcePath + "] from [" + loader.getClass().getName() + "]!!!");
                                    loader = loader.getParent();
                                }
                            }
                        } catch (Exception e) {
                            is = null;
                            LogUtils.logError(StreamUtils.class,
                                    "Could not found the specified path ["
                                    + filePathOrResource + "] from resource!!!");
                        }
                        // break if found
                        if (is != null) break;
                    }
                }
            }
        }
        return is;
    }

    /**
     * Invoke close method of the specified object, and restrict all exceptions if necessary
     *
     * @param obj object to invoke
     */
    public static void closeQuitely(Object obj) {
        if (obj != null) {
            try {
                BeanUtils.invokeMethod(obj, "flush", Boolean.TRUE, (Class<?>[]) null, (Object[]) null);
                BeanUtils.invokeMethod(obj, "close", Boolean.TRUE, (Class<?>[]) null, (Object[]) null);
                obj = null;
            } catch (Exception e) {}
        }
    }

    /**
     * Read the specified {@link InputStream} to bytes array
     *
     * @param is to read
     *
     * @return bytes array or null/empty
     */
    public static byte[] toByteArray(InputStream is) {
        byte[] data = null;
        if (is != null) {
            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();
                if (StreamUtils.copyStream(is, baos) > 0) {
                    data = baos.toByteArray();
                    baos.flush();
                }
            } catch (Exception e) {
                data = null;
                LogUtils.logError(StreamUtils.class,
                        "Could not read data from input stream ["
                        + e.getMessage() + "]!!!");
            } finally {
                closeQuitely(baos);
            }
        }
        return data;
    }
    /**
     * Read the specified {@link InputStream} to {@link ByteArrayOutputStream}
     *
     * @param is to read
     *
     * @return {@link ByteArrayOutputStream} or null/empty
     */
    public static ByteArrayOutputStream toByteArrayOutputStream(InputStream is) {
        ByteArrayOutputStream baos = null;
        if (is != null) {
            try {
                baos = new ByteArrayOutputStream();
                StreamUtils.copyStream(is, baos);
            } catch (Exception e) {
                closeQuitely(baos);
                LogUtils.logError(StreamUtils.class,
                        "Could not read data from input stream ["
                        + e.getMessage() + "]!!!");
            }
        }
        return baos;
    }
}
