/*
 * @(#)FileUtils.java 1.0 Sep 28, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * The {@link File} utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class FileUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_BUFFER_LENGTH = (4 * 1024);

    /**
     * Checks and creates the specified directory path
     *
     * @param dirPath the path to check
     *
     * @return true for creating directory successfully; else otherwise
     */
    public static boolean ensureDirectory(String dirPath) {
        // Checks for creating folder
        File folder = new File(dirPath);
        if (!folder.exists() && !folder.mkdirs()) return false;
        return true;
    }
    /**
     * Checks and creates the specified file path
     *
     * @param filePath the path to check
     *
     * @return true for creating file successfully; else otherwise
     */
    public static boolean ensureFile(String filePath) {
        // Checks for creating file
        File file = new File(filePath);
        try { if (!file.exists() && !file.createNewFile()) return false; }
        catch (IOException e) { LogUtils.logWarn(FileUtils.class, e.getMessage()); }
        return true;
    }

    /**
     * Creates a new directory in the specified directory, using the given prefix to generate its name.
     *
     * @param parentDir parent path
     * @param prefix temporary directory prefix
     * @param deleteOnExit specify temporary directory whether will be deleted on exiting
     * @return temporary directory path
     * @throws IOException thrown if failed
     * @throws IllegalArgumentException thrown if failed
     */
    public static String createTempDir(String parentDir, String prefix, boolean deleteOnExit)
        throws IOException, IllegalArgumentException {
        parentDir = (!StringUtils.hasText(parentDir) ? System.getProperty("java.io.tmpdir", null) : parentDir);
        Assert.hasText(parentDir, "parentDir");
        parentDir = parentDir.trim();
        parentDir = parentDir.replace("\\\\", "\\").replace("\\", "/");
        Assert.isTrue(ensureDirectory(parentDir), "Could not found/create parent temporary directory!");
        prefix = (!StringUtils.hasText(prefix) || prefix.length() < 3 ? ("___" + prefix).substring(0, 3) : prefix);
        String tmpDir = prefix + "temp" + String.valueOf((new Date()).getTime());
        if (!parentDir.endsWith("/")) parentDir += "/";
        tmpDir = parentDir + tmpDir;
        Assert.isTrue(ensureDirectory(tmpDir), "Could not create temporary directory '" + tmpDir + "'!");
        // need to delete on exiting
        if (deleteOnExit) {
            File f = new File(tmpDir);
            f.deleteOnExit();
        }
        return tmpDir;
    }
    /**
     * Creates a new directory in the specified directory, using the given prefix to generate its name.
     *
     * @param parentDir parent path
     * @param prefix temporary directory prefix
     * @return temporary directory path
     * @throws IOException thrown if failed
     * @throws IllegalArgumentException thrown if failed
     */
    public static String createTempDir(String parentDir, String prefix)
        throws IOException, IllegalArgumentException {
        return createTempDir(parentDir, prefix, false);
    }
    /**
     * Creates a new directory in the system temporary directory, using the given prefix to generate its name.
     *
     * @param prefix temporary directory prefix
     * @return temporary directory path
     * @throws IOException thrown if failed
     * @throws IllegalArgumentException thrown if failed
     */
    public static String createTempDir(String prefix)
        throws IOException, IllegalArgumentException {
        return createTempDir(null, prefix, false);
    }
    /**
     * Creates a new directory in the system temporary directory.
     *
     * @return temporary directory path
     * @throws IOException thrown if failed
     * @throws IllegalArgumentException thrown if failed
     */
    public static String createTempDir()
        throws IOException, IllegalArgumentException {
        return createTempDir(null, null, false);
    }

    /**
     * Creates a new temporary file in the specified directory, using the given prefix to generate its name.
     *
     * @param parentDir parent path
     * @param prefix temporary file prefix
     * @param deleteOnExit specify temporary file whether will be deleted on exiting
     * @return temporary file path
     * @throws IOException thrown if failed
     * @throws IllegalArgumentException thrown if failed
     */
    public static String createTempFile(String parentDir, String prefix, boolean deleteOnExit)
        throws IOException, IllegalArgumentException {
        parentDir = (!StringUtils.hasText(parentDir) ? System.getProperty("java.io.tmpdir", null) : parentDir);
        Assert.hasText(parentDir, "parentDir");
        parentDir = parentDir.trim();
        parentDir = parentDir.replace("\\\\", "\\").replace("\\", "/");
        Assert.isTrue(ensureDirectory(parentDir), "Could not found/create parent temporary directory!");
        prefix = (!StringUtils.hasText(prefix) || prefix.length() < 3 ? ("___" + prefix).substring(0, 3) : prefix);
        String tmpFile = prefix + "temp" + String.valueOf((new Date()).getTime());
        if (!parentDir.endsWith("/")) parentDir += "/";
        tmpFile = parentDir + tmpFile;
        Assert.isTrue(ensureFile(tmpFile), "Could not create temporary file '" + tmpFile + "'!");
        // need to delete on exiting
        if (deleteOnExit) {
            File f = new File(tmpFile);
            f.deleteOnExit();
        }
        return tmpFile;
    }
    /**
     * Creates a new temporary file in the specified directory, using the given prefix to generate its name.
     *
     * @param parentDir parent path
     * @param prefix temporary file prefix
     * @return temporary file path
     * @throws IOException thrown if failed
     * @throws IllegalArgumentException thrown if failed
     */
    public static String createTempFile(String parentDir, String prefix)
        throws IOException, IllegalArgumentException {
        return createTempFile(parentDir, prefix, false);
    }
    /**
     * Creates a new temporary file in the system temporary directory, using the given prefix to generate its name.
     *
     * @param prefix temporary file prefix
     * @return temporary file path
     * @throws IOException thrown if failed
     * @throws IllegalArgumentException thrown if failed
     */
    public static String createTempFile(String prefix)
        throws IOException, IllegalArgumentException {
        return createTempFile(null, prefix, false);
    }
    /**
     * Creates a new temporary file in the system temporary directory.
     *
     * @return temporary file path
     * @throws IOException thrown if failed
     * @throws IllegalArgumentException thrown if failed
     */
    public static String createTempFile()
        throws IOException, IllegalArgumentException {
        return createTempFile(null, null, false);
    }

    /**
     * Gets the base name, minus the full path and extension, from a full filename.
     * <p>
     * This method will handle a file in either Unix or Windows format.
     * The text after the last forward or backslash and before the last dot is returned.
     * <pre>
     * a/b/c.txt --&gt; c
     * a.txt     --&gt; a
     * a/b/c     --&gt; c
     * a/b/c/    --&gt; ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filePath  the file path/name to query, null returns null
     * @return the name of the file without the path, or an empty string if none exists
     */
    public static String getBaseFileName(String filePath) {
        return FilenameUtils.getBaseName(filePath);
    }

    /**
     * Gets the extension of a filename.
     * <p>
     * This method returns the textual part of the filename after the last dot.
     * There must be no directory separator after the dot.
     * <pre>
     * foo.txt      --&gt; "txt"
     * a/b/c.jpg    --&gt; "jpg"
     * a/b.txt/c    --&gt; ""
     * a/b/c        --&gt; ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     *
     * @param filePath the file path/name to retrieve the extension of.
     * @return the extension of the file or an empty string if none exists.
     */
    public static String getFileExtension(String filePath) {
        return FilenameUtils.getExtension(filePath);
    }

    /**
     * Delete the specified {@link File} safety
     *
     * @param file to delete
     */
    public static void safeDelete(File file) {
        if (file != null) {
            try { file.delete(); }
            catch(Exception e) {
                try { file.deleteOnExit(); }
                catch (Exception ee) {}
            }
        }
    }
    /**
     * Delete the specified {@link File} path safety
     *
     * @param file to delete
     */
    public static void safeDelete(String file) {
        if (StringUtils.hasText(file)) {
            try { safeDelete(new File(file)); }
            catch (Exception e) {}
        }
    }
    /**
     * Delete the specified {@link File} list safety
     *
     * @param files to delete
     */
    public static void safeDelete(String...files) {
        if (!CollectionUtils.isEmpty(files)) {
            for(String file : files) {
                safeDelete(file);
            }
        }
    }
    /**
     * Delete the specified {@link File} list safety
     *
     * @param files to delete
     */
    public static void safeDelete(List<String> files) {
        if (!CollectionUtils.isEmpty(files)) {
            for(String file : files) {
                safeDelete(file);
            }
        }
    }

    /**
     * Write the specified {@link InputStream} to {@link File}
     *
     * @param is {@link InputStream} to read
     * @param f {@link File} to write
     * @param buffSize the buffer size. &lt;= 0 for default buffer size (4 * 1024)
     * @param overwrite specify overwriting the existed file
     * @param closeSrcStream specify the source {@link InputStream} whether has been closed after processing
     *
     * @return the wroten data length
     *
     * @throws Exception thrown if failed
     */
    public static long toFile(InputStream is, File f, int buffSize, boolean overwrite, boolean closeSrcStream) throws Exception {
        long dataLength = 0;
        // write to file
        if (is != null && f != null) {
            // check if file existed
            if (f.exists() && overwrite) safeDelete(f);

            // if not existed or has been overwrite; just do nothing
            if (!f.exists() || overwrite) {
                // write to file
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f);
                    dataLength = StreamUtils.copyStream(is, fos);
                } catch (Exception e) {
                	LogUtils.logError(FileUtils.class, e.getMessage());
                    // if file has been wrote; deleting it in error
                    safeDelete(f);
                    throw e;

                } finally {
                    StreamUtils.closeQuitely(fos);
                }
            }
        }
        // close source stream if necessary
        if (is != null && closeSrcStream) {
            StreamUtils.closeQuitely(is);
        }
        return dataLength;
    }
    /**
     * Write the specified {@link InputStream} to {@link File}.<br>
     * If {@link File} has been existed; it will be overwroten;
     * and the specified source {@link InputStream} will be closed automatically after processing
     *
     * @param is {@link InputStream} to read
     * @param f {@link File} to write
     * @param overwrite specify overwriting the existed file
     * @param closeSrcStream specify the source {@link InputStream} whether has been closed after processing
     *
     * @return the wroten data length
     *
     * @throws Exception thrown if failed
     */
    public static long toFile(InputStream is, File f, boolean overwrite, boolean closeSrcStream) throws Exception {
        return toFile(is, f, DEFAULT_BUFFER_LENGTH, overwrite, closeSrcStream);
    }
    /**
     * Write the specified {@link InputStream} to {@link File}.<br>
     * The specified source {@link InputStream} will be closed automatically after processing
     *
     * @param is {@link InputStream} to read
     * @param f {@link File} to write
     * @param buffSize the buffer size. &lt;= 0 for default buffer size (4 * 1024)
     * @param overwrite specify overwriting the existed file
     *
     * @return the wroten data length
     *
     * @throws Exception thrown if failed
     */
    public static long toFile(InputStream is, File f, int buffSize, boolean overwrite) throws Exception {
        return toFile(is, f, buffSize, overwrite, Boolean.TRUE);
    }
    /**
     * Write the specified {@link InputStream} to {@link File}.<br>
     * If {@link File} has been existed; it will be overwroten;
     * and the specified source {@link InputStream} will be closed automatically after processing
     *
     * @param is {@link InputStream} to read
     * @param f {@link File} to write
     * @param buffSize the buffer size. &lt;= 0 for default buffer size (4 * 1024)
     *
     * @return the wroten data length
     *
     * @throws Exception thrown if failed
     */
    public static long toFile(InputStream is, File f, int buffSize) throws Exception {
        return toFile(is, f, buffSize, Boolean.TRUE, Boolean.TRUE);
    }
    /**
     * Write the specified {@link InputStream} to {@link File}.<br>
     * If {@link File} has been existed; it will be overwroten;
     * and the specified source {@link InputStream} will be closed automatically after processing
     *
     * @param is {@link InputStream} to read
     * @param f {@link File} to write
     *
     * @return the wroten data length
     *
     * @throws Exception thrown if failed
     */
    public static long toFile(InputStream is, File f) throws Exception {
        return toFile(is, f, DEFAULT_BUFFER_LENGTH, Boolean.TRUE, Boolean.TRUE);
    }

    /**
     * Write the specified {@link InputStream} to {@link File}
     *
     * @param is {@link InputStream} to read
     * @param filePath {@link File} path to write
     * @param buffSize the buffer size. &lt;= 0 for default buffer size (4 * 1024)
     * @param overwrite specify overwriting the existed file
     * @param closeSrcStream specify the source {@link InputStream} whether has been closed after processing
     *
     * @return the wroten data length
     *
     * @throws Exception thrown if failed
     */
    public static long toFile(InputStream is, String filePath, int buffSize, boolean overwrite, boolean closeSrcStream) throws Exception {
        return toFile(is, new File(filePath), buffSize, overwrite, closeSrcStream);
    }
    /**
     * Write the specified {@link InputStream} to {@link File}.<br>
     * If {@link File} has been existed; it will be overwroten;
     * and the specified source {@link InputStream} will be closed automatically after processing
     *
     * @param is {@link InputStream} to read
     * @param filePath {@link File} path to write
     * @param overwrite specify overwriting the existed file
     * @param closeSrcStream specify the source {@link InputStream} whether has been closed after processing
     *
     * @return the wroten data length
     *
     * @throws Exception thrown if failed
     */
    public static long toFile(InputStream is, String filePath, boolean overwrite, boolean closeSrcStream) throws Exception {
        return toFile(is, new File(filePath), DEFAULT_BUFFER_LENGTH, overwrite, closeSrcStream);
    }
    /**
     * Write the specified {@link InputStream} to {@link File}.<br>
     * The specified source {@link InputStream} will be closed automatically after processing
     *
     * @param is {@link InputStream} to read
     * @param filePath {@link File} path to write
     * @param buffSize the buffer size. &lt;= 0 for default buffer size (4 * 1024)
     * @param overwrite specify overwriting the existed file
     *
     * @return the wroten data length
     *
     * @throws Exception thrown if failed
     */
    public static long toFile(InputStream is, String filePath, int buffSize, boolean overwrite) throws Exception {
        return toFile(is, new File(filePath), buffSize, overwrite, Boolean.TRUE);
    }
    /**
     * Write the specified {@link InputStream} to {@link File}.<br>
     * If {@link File} has been existed; it will be overwroten;
     * and the specified source {@link InputStream} will be closed automatically after processing
     *
     * @param is {@link InputStream} to read
     * @param filePath {@link File} path to write
     * @param buffSize the buffer size. &lt;= 0 for default buffer size (4 * 1024)
     *
     * @return the wroten data length
     *
     * @throws Exception thrown if failed
     */
    public static long toFile(InputStream is, String filePath, int buffSize) throws Exception {
        return toFile(is, new File(filePath), buffSize, Boolean.TRUE, Boolean.TRUE);
    }
    /**
     * Write the specified {@link InputStream} to {@link File}.<br>
     * If {@link File} has been existed; it will be overwroten;
     * and the specified source {@link InputStream} will be closed automatically after processing
     *
     * @param is {@link InputStream} to read
     * @param filePath {@link File} path to write
     *
     * @return the wroten data length
     *
     * @throws Exception thrown if failed
     */
    public static long toFile(InputStream is, String filePath) throws Exception {
        return toFile(is, new File(filePath), DEFAULT_BUFFER_LENGTH, Boolean.TRUE, Boolean.TRUE);
    }

    /**
     * Get the file/sub-directory path(s) list in the specified folder path
     * @param dirPath to count
     * @return the file/sub-directory path(s) list
     */
    public static List<Path> files(String dirPath) {
        return files(dirPath, (String) null, (Boolean) null, Boolean.FALSE);
    }
    /**
     * Get the file/sub-directory path(s) list in the specified folder path
     * @param dirPath to count
     * @param glob glob pattern such as &lt;*.{java,class,jar}&gt;
     * @return the file/sub-directory path(s) list
     */
    public static List<Path> files(String dirPath, String glob) {
        return files(dirPath, glob, (Boolean) null, Boolean.FALSE);
    }
    /**
     * Get the file/sub-directory path(s) list in the specified folder path
     * @param dirPath to count
     * @param onlyFile specify returning only file/sub-directory path(s). true for file; false for sub-directory; else for all
     * @return the file/sub-directory path(s) list
     */
    public static List<Path> files(String dirPath, Boolean onlyFile) {
        return files(dirPath, (String) null, onlyFile, Boolean.FALSE);
    }
    /**
     * Get the file/sub-directory path(s) list in the specified folder path
     * @param dirPath to count
     * @param glob glob pattern such as &lt;*.{java,class,jar}&gt;
     * @param onlyFile specify returning only file/sub-directory path(s). true for file; false for sub-directory; else for all
     * @return the file/sub-directory path(s) list
     */
    public static List<Path> files(String dirPath, String glob, Boolean onlyFile) {
        return files(dirPath, glob, onlyFile, Boolean.FALSE);
    }
    /**
     * Get the file/sub-directory path(s) list in the specified folder path
     * @param dirPath to count
     * @param glob glob pattern such as &lt;*.{java,class,jar}&gt;
     * @param onlyFile specify returning only file/sub-directory path(s). true for file; false for sub-directory; else for all
     * @param recursively specify counting recursively
     * @return the file/sub-directory path(s) list
     */
    public static List<Path> files(String dirPath, String glob, Boolean onlyFile, boolean recursively) {
        List<Path> paths = new LinkedList<Path>();
        if (!StringUtils.hasText(dirPath)) return paths;
        File f = new File(dirPath);
        if (!f.exists() || !f.isDirectory()) return paths;
        DirectoryStream<Path> ds = null;
        try {
            if (!recursively || !StringUtils.hasText(glob)) {
                ds = Files.newDirectoryStream(f.toPath());
            } else {
                ds = Files.newDirectoryStream(f.toPath(), glob);
            }
        } catch (Exception e) {
            LogUtils.logError(FileUtils.class, e.getMessage());
            ds = null;
        }
        // count
        if (ds != null) {
            for(Path path : ds) {
                // check for path
                if (Boolean.TRUE.equals(onlyFile) && path.toFile().isFile()) {
                    paths.add(path);
                } else if (Boolean.FALSE.equals(onlyFile) && path.toFile().isDirectory()) {
                    paths.add(path);
                } else {
                    paths.add(path);
                }
                // count recursively
                if (recursively && path.toFile().isDirectory()) {
                    paths.addAll(files(path.toFile().getPath(), glob, onlyFile, recursively));
                }
            }
        }
        return paths;
    }
    /**
     * Get the file/sub-directory path(s) list in the specified folder path
     * @param dirPath to count
     * @param filter filter path
     * @return the file/sub-directory path(s) list
     */
    public static List<Path> files(String dirPath, Filter<Path> filter) {
        return files(dirPath, filter, (Boolean) null, Boolean.FALSE);
    }
    /**
     * Get the file/sub-directory path(s) list in the specified folder path
     * @param dirPath to count
     * @param filter filter path
     * @param onlyFile specify returning only file/sub-directory path(s). true for file; false for sub-directory; else for all
     * @return the file/sub-directory path(s) list
     */
    public static List<Path> files(String dirPath, Filter<Path> filter, Boolean onlyFile) {
        return files(dirPath, filter, onlyFile, Boolean.FALSE);
    }
    /**
     * Get the file/sub-directory path(s) list in the specified folder path
     * @param dirPath to count
     * @param filter filter path
     * @param onlyFile specify returning only file/sub-directory path(s). true for file; false for sub-directory; else for all
     * @param recursively specify counting recursively
     * @return the file/sub-directory path(s) list
     */
    public static List<Path> files(String dirPath, Filter<Path> filter, Boolean onlyFile, boolean recursively) {
        List<Path> paths = new LinkedList<Path>();
        if (!StringUtils.hasText(dirPath)) return paths;
        File f = new File(dirPath);
        if (!f.exists() || !f.isDirectory()) return paths;
        DirectoryStream<Path> ds = null;
        try {
            if (!recursively || filter == null) {
                ds = Files.newDirectoryStream(f.toPath());
            } else {
                ds = Files.newDirectoryStream(f.toPath(), filter);
            }
        } catch (Exception e) {
            LogUtils.logError(FileUtils.class, e.getMessage());
            ds = null;
        }
        // count
        if (ds != null) {
            for(Path path : ds) {
                // check for path
                if (Boolean.TRUE.equals(onlyFile) && path.toFile().isFile()) {
                    paths.add(path);
                } else if (Boolean.FALSE.equals(onlyFile) && path.toFile().isDirectory()) {
                    paths.add(path);
                } else {
                    paths.add(path);
                }
                // count recursively
                if (recursively && path.toFile().isDirectory()) {
                    paths.addAll(files(path.toFile().getPath(), filter, onlyFile, recursively));
                }
            }
        }
        return paths;
    }

    /**
     * Get the number of file(s) / sub-directory(es) in the specified folder path
     * @param dirPath to count
     * @return the number of file(s) / sub-directory(es)
     */
    public static long filesCounter(String dirPath) {
        return filesCounter(dirPath, (String) null, (Boolean) null, Boolean.FALSE);
    }
    /**
     * Get the number of file(s) / sub-directory(es) in the specified folder path
     * @param dirPath to count
     * @param glob glob pattern such as &lt;*.{java,class,jar}&gt;
     * @return the number of file(s) / sub-directory(es)
     */
    public static long filesCounter(String dirPath, String glob) {
        return filesCounter(dirPath, glob, (Boolean) null, Boolean.FALSE);
    }
    /**
     * Get the number of file(s) / sub-directory(es) in the specified folder path
     * @param dirPath to count
     * @param onlyFile specify returning only file/sub-directory path(s). true for file; false for sub-directory; else for all
     * @return the number of file(s) / sub-directory(es)
     */
    public static long filesCounter(String dirPath, Boolean onlyFile) {
        return filesCounter(dirPath, (String) null, onlyFile, Boolean.FALSE);
    }
    /**
     * Get the number of file(s) / sub-directory(es) in the specified folder path
     * @param dirPath to count
     * @param glob glob pattern such as &lt;*.{java,class,jar}&gt;
     * @param onlyFile specify returning only file/sub-directory path(s). true for file; false for sub-directory; else for all
     * @return the number of file(s) / sub-directory(es)
     */
    public static long filesCounter(String dirPath, String glob, Boolean onlyFile) {
        return filesCounter(dirPath, glob, onlyFile, Boolean.FALSE);
    }
    /**
     * Get the number of file(s) / sub-directory(es) in the specified folder path
     * @param dirPath to count
     * @param glob glob pattern such as &lt;*.{java,class,jar}&gt;
     * @param onlyFile specify returning only file/sub-directory path(s). true for file; false for sub-directory; else for all
     * @param recursively specify counting recursively
     * @return the number of file(s) / sub-directory(es)
     */
    public static long filesCounter(String dirPath, String glob, Boolean onlyFile, boolean recursively) {
        return CollectionUtils.getSize(files(dirPath, glob, onlyFile, recursively));
    }
    /**
     * Get the number of file(s) / sub-directory(es) in the specified folder path
     * @param dirPath to count
     * @param filter filter path
     * @return the number of file(s) / sub-directory(es)
     */
    public static long filesCounter(String dirPath, Filter<Path> filter) {
        return filesCounter(dirPath, filter, (Boolean) null, Boolean.FALSE);
    }
    /**
     * Get the number of file(s) / sub-directory(es) in the specified folder path
     * @param dirPath to count
     * @param filter filter path
     * @param onlyFile specify returning only file/sub-directory path(s). true for file; false for sub-directory; else for all
     * @return the number of file(s) / sub-directory(es)
     */
    public static long filesCounter(String dirPath, Filter<Path> filter, Boolean onlyFile) {
        return filesCounter(dirPath, filter, onlyFile, Boolean.FALSE);
    }
    /**
     * Get the number of file(s) / sub-directory(es) in the specified folder path
     * @param dirPath to count
     * @param filter filter path
     * @param onlyFile specify returning only file/sub-directory path(s). true for file; false for sub-directory; else for all
     * @param recursively specify counting recursively
     * @return the number of file(s) / sub-directory(es)
     */
    public static long filesCounter(String dirPath, Filter<Path> filter, Boolean onlyFile, boolean recursively) {
        return CollectionUtils.getSize(files(dirPath, filter, onlyFile, recursively));
    }
}
