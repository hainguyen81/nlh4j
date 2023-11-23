/*
 * @(#)CompressionUtils.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.util.Assert;

/**
 * The compression/de-compression zip file utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public final class CompressionUtils implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Performs action for creating the compression (ZIP) file of all specified files
	 *
	 * @param destDir
	 * 		the temporary destination folder to save the compression (ZIP) file.
	 * 		Null or empty for default temporary directory
	 * @param zipFileName
	 * 		the compression (ZIP) file name only for compressing the files
	 * @param files
	 * 		the file paths array to compression (ZIP) file
	 *
	 * @return the compression (ZIP) file full path or null for exception
     * @throws Exception thrown if fail
	 */
	public static String compress(String destDir, String zipFileName, String...files)
	        throws Exception {
	    Assert.notEmpty(files, "files");
		// Creates download folder by mail id
		zipFileName = StringUtils.hasText(zipFileName)
		        ? zipFileName : String.valueOf(DateUtils.currentTime().getTime());
		// re-fixes file name
		zipFileName = StringUtils.refixWinFileName(zipFileName);
		zipFileName = zipFileName.toLowerCase();
		if (!zipFileName.endsWith(".zip")) zipFileName += ".zip";
		// Checks for ensuring destination directory
		if (StringUtils.hasText(destDir)
		        && !FileUtils.ensureDirectory(destDir)) {
	        throw new IllegalAccessError(MessageFormat.format("{0} is not found or is not enough permission to create it", destDir));
		} else if (!StringUtils.hasText(destDir)) {
		    destDir = FileUtils.createTempDir();
		}
		// Creates a buffer for reading the files
		ZipOutputStream out = null;
		FileOutputStream fos = null;
		FileInputStream fis = null;
		// Creates the ZIP file name by mail subject
		String zipFile = MessageFormat.format("{0}/{1}", destDir, zipFileName);
		try {
			// Creates zip file
			fos = new FileOutputStream(zipFile);
			out = new ZipOutputStream(fos);
			// Outs all files
			Map<String, Integer> dupFiles = new LinkedHashMap<String, Integer>();
			for(String filePath : files) {
				File f = new File(filePath);
				fis = new FileInputStream(f);
				// Adds ZIP entry to output stream.
				String safeFileName = StringUtils.refixWinFileName(FileUtils.getBaseFileName(f.getName()));
				String safeFileExt = FileUtils.getFileExtension(f.getName());
				if (!dupFiles.containsKey(f.getName())) {
					dupFiles.put(f.getName(), new Integer(0));
					out.putNextEntry(new ZipEntry(
							MessageFormat.format("{0}.{1}", safeFileName, safeFileExt)));
				}
				else {
					Integer cnt = (dupFiles.get(f.getName()) + 1);
					dupFiles.put(f.getName(), cnt);
					out.putNextEntry(new ZipEntry(
							MessageFormat.format("{0}_{1,number,#}.{2}", safeFileName, cnt, safeFileExt)));
				}
				// Transfers bytes from the file to the ZIP file
				StreamUtils.copyStream(fis, out);
				// Completes the entry
				out.closeEntry();
				StreamUtils.closeQuitely(fis);
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			zipFile = null;
		}
		catch (IOException e) {
		    e.printStackTrace();
			zipFile = null;
		}
		finally {
		    StreamUtils.closeQuitely(fis);
		    StreamUtils.closeQuitely(fos);
		    StreamUtils.closeQuitely(out);
		}
		// return the full path of zip file
		return zipFile;
	}

	/**
	 * Extracts the compression (ZIP) file data to the specified directory.
	 *
	 * @param zipFileData
	 * 		the compression (ZIP) file data to extract
	 * @param destDir
	 * 		the destination directory to extract. NULL for default temporary directory
	 * @param extractedFilePaths
	 * 		the extracted file paths (included <b>destDir</b> in path).
	 * 		NULL for doing not care
	 * @param deleteOnFail
	 * 		specifies the extracted files will be deleted in error case
	 *
	 * @return true for extracting successfully; else otherwise
     * @throws Exception thrown if fail
	 */
	public static boolean decompress(byte[] zipFileData, String destDir, List<String> extractedFilePaths, boolean deleteOnFail)
	        throws Exception {
		// Checks parameter
		Assert.isTrue(isZippedData(zipFileData), "Data is NULL or invalid zipped data!");
		// Ensures destination directory
		if (StringUtils.hasText(destDir)
                && !FileUtils.ensureDirectory(destDir)) {
            throw new IllegalAccessError(MessageFormat.format("{0} is not found or is not enough permission to create it", destDir));
        } else if (!StringUtils.hasText(destDir)) {
            destDir = FileUtils.createTempDir();
        }
		destDir = (!destDir.endsWith("/") ? (destDir + "/") : destDir);
		// Starts extracting
		List<String> cacheExtractedFiles = new LinkedList<String>();
		ZipEntry ze = null;
		boolean ok = false;
		ByteArrayInputStream bais = null;
		ZipInputStream zis = null;
		FileOutputStream fos = null;
		try {
			// Creates stream from zip data
			bais = new ByteArrayInputStream(zipFileData);
			zis = new ZipInputStream(bais);
			// Extracts every file
			while ((ze = zis.getNextEntry()) != null) {
				// Parses extracted file path
				String extractFilePath = MessageFormat.format("{0}{1}", destDir, ze.getName());
				// Extracts zip entry
				fos = new FileOutputStream(extractFilePath);
				StreamUtils.copyStream(zis, fos);
				// release
				zis.closeEntry();
				StreamUtils.closeQuitely(fos);
				// caches the extract file if necessary
				cacheExtractedFiles.add(extractFilePath);
			}
			ok = true;
		}
		catch (IOException e) {
		    e.printStackTrace();
			ok = false;
		}
		catch (Exception e) {
		    e.printStackTrace();
			ok = false;
		} finally {
		    StreamUtils.closeQuitely(fos);
		    StreamUtils.closeQuitely(bais);
		    StreamUtils.closeQuitely(zis);
		}
		// if fail; then deleting all extracted files
		if (!ok && deleteOnFail && !CollectionUtils.isEmpty(cacheExtractedFiles)) {
		    FileUtils.safeDelete(cacheExtractedFiles);
			cacheExtractedFiles.clear();
		}
		// returns extracted files if necessary
		if (ok && (extractedFilePaths != null)) {
			extractedFilePaths.addAll(cacheExtractedFiles);
		}
		return ok;
	}
	/**
	 * Extracts the compression (ZIP) file data to the specified directory.
	 *
	 * @param zipFileData
	 * 		the compression (ZIP) file data to extract
	 * @param destDir
	 * 		the destination directory to extract
	 * @param extractedFilePaths
	 * 		the extracted file paths (included <b>destDir</b> in path).
	 * 		NULL for doing not care
	 *
	 * @return true for extracting successfully; else otherwise
     * @throws Exception thrown if fail
	 */
	public static boolean decompress(byte[] zipFileData, String destDir, List<String> extractedFilePaths)
	        throws Exception {
		return decompress(zipFileData, destDir, extractedFilePaths, true);
	}
	/**
	 * Extracts the compression (ZIP) file data to the specified directory.
	 *
	 * @param zipFileData
	 * 		the compression (ZIP) file data to extract
	 * @param destDir
	 * 		the destination directory to extract
	 *
	 * @return true for extracting successfully; else otherwise
     * @throws Exception thrown if fail
	 */
	public static boolean decompress(byte[] zipFileData, String destDir)
	        throws Exception {
		return decompress(zipFileData, destDir, null, true);
	}

    /**
     * Extracts the compression (ZIP) file data to the specified directory.
     *
     * @param zipFileData
     *      the compression (ZIP) file data to extract
     * @param destDir
     *      the destination directory to extract. NULL for default temporary directory
     * @param extractedFilePaths
     *      the extracted file paths (included <b>destDir</b> in path).
     *      NULL for doing not care
     * @param deleteOnFail
     *      specifies the extracted files will be deleted in error case
     *
     * @return true for extracting successfully; else otherwise
     * @throws Exception thrown if fail
     */
    public static boolean decompress(InputStream zipFileData, String destDir, List<String> extractedFilePaths, boolean deleteOnFail)
            throws Exception {
        // Checks parameter
        Assert.isTrue(isZippedStream(zipFileData), "Input Stream is NULL or invalid zipped stream!");
        // Ensures destination directory
        if (StringUtils.hasText(destDir)
                && !FileUtils.ensureDirectory(destDir)) {
            throw new IllegalAccessError(MessageFormat.format("{0} is not found or is not enough permission to create it", destDir));
        } else if (!StringUtils.hasText(destDir)) {
            destDir = FileUtils.createTempDir();
        }
        destDir = (!destDir.endsWith("/") ? (destDir + "/") : destDir);
        // Starts extracting
        List<String> cacheExtractedFiles = new LinkedList<String>();
        ZipEntry ze = null;
        boolean ok = false;
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream bais = null;
        ZipInputStream zis = null;
        FileOutputStream fos = null;
        try {
            // Read stream to bytes
            baos = new ByteArrayOutputStream();
            StreamUtils.copyStream(zipFileData, baos);
            // Creates stream from zip data
            bais = new ByteArrayInputStream(baos.toByteArray());
            zis = new ZipInputStream(bais);
            // Extracts every file
            while ((ze = zis.getNextEntry()) != null) {
                // Parses extracted file path
                String extractFilePath = MessageFormat.format("{0}{1}", destDir, ze.getName());
                // Extracts zip entry
                fos = new FileOutputStream(extractFilePath);
                StreamUtils.copyStream(zis, fos);
                zis.closeEntry();
                StreamUtils.closeQuitely(fos);
                // caches the extract file if necessary
                cacheExtractedFiles.add(extractFilePath);
            }
            ok = true;
        }
        catch (IOException e) {
            e.printStackTrace();
            ok = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            ok = false;
        } finally {
            StreamUtils.closeQuitely(baos);
            StreamUtils.closeQuitely(fos);
            StreamUtils.closeQuitely(bais);
            StreamUtils.closeQuitely(zis);
        }
        // if fail; then deleting all extracted files
        if (!ok && deleteOnFail && !CollectionUtils.isEmpty(cacheExtractedFiles)) {
            FileUtils.safeDelete(cacheExtractedFiles);
            cacheExtractedFiles.clear();
        }
        // returns extracted files if necessary
        if (ok && (extractedFilePaths != null)) {
            extractedFilePaths.addAll(cacheExtractedFiles);
        }
        return ok;
    }
    /**
     * Extracts the compression (ZIP) file data to the specified directory.
     *
     * @param zipFileData
     *      the compression (ZIP) file data to extract
     * @param destDir
     *      the destination directory to extract
     * @param extractedFilePaths
     *      the extracted file paths (included <b>destDir</b> in path).
     *      NULL for doing not care
     *
     * @return true for extracting successfully; else otherwise
     * @throws Exception thrown if fail
     */
    public static boolean decompress(InputStream zipFileData, String destDir, List<String> extractedFilePaths)
            throws Exception {
        return decompress(zipFileData, destDir, extractedFilePaths, true);
    }
    /**
     * Extracts the compression (ZIP) file data to the specified directory.
     *
     * @param zipFileData
     *      the compression (ZIP) file data to extract
     * @param destDir
     *      the destination directory to extract
     *
     * @return true for extracting successfully; else otherwise
     * @throws Exception thrown if fail
     */
    public static boolean decompress(InputStream zipFileData, String destDir)
            throws Exception {
        return decompress(zipFileData, destDir, null, true);
    }

	/**
	 * Extracts the compression (ZIP) file to the specified directory.
	 *
	 * @param zipFilePath
	 * 		the compression (ZIP) file path to extract
	 * @param destDir
	 * 		the destination directory to extract
	 * @param extractedFilePaths
	 * 		the extracted file paths (included <b>destDir</b> in path).
	 * 		NULL for doing not care
	 * @param deleteOnFail
	 * 		specifies the extracted files will be deleted in error case
	 *
	 * @return true for extracting successfully; else otherwise
     * @throws Exception thrown if fail
	 */
	public static boolean decompress(String zipFilePath, String destDir, List<String> extractedFilePaths, boolean deleteOnFail)
	        throws Exception {
		// Checks parameter
		Assert.isTrue(isZippedFile(zipFilePath), "File path is NULL or not found or invalid zipped file!");
		// Ensures destination directory
		if (StringUtils.hasText(destDir)
                && !FileUtils.ensureDirectory(destDir)) {
            throw new IllegalAccessError(MessageFormat.format("{0} is not found or is not enough permission to create it", destDir));
        } else if (!StringUtils.hasText(destDir)) {
            destDir = FileUtils.createTempDir();
        }
		destDir = (!destDir.endsWith("/") ? (destDir + "/") : destDir);
		// Starts extracting
		File f = new File(zipFilePath);
		List<String> cacheExtractedFiles = new LinkedList<String>();
		ZipEntry ze = null;
		boolean ok = false;
		FileInputStream fis = null;
		ZipInputStream zis = null;
		FileOutputStream fos = null;
		try {
			// Creates stream from zip data
			fis = new FileInputStream(f);
			zis = new ZipInputStream(fis);
			// Extracts every file
			while ((ze = zis.getNextEntry()) != null) {
				// Parses extracted file path
				String extractFilePath = MessageFormat.format("{0}{1}", destDir, ze.getName());
				// Extracts zip entry
				fos = new FileOutputStream(extractFilePath);
				StreamUtils.copyStream(zis, fos);
				zis.closeEntry();
				StreamUtils.closeQuitely(fos);
				// caches the extract file if necessary
				cacheExtractedFiles.add(extractFilePath);
			}
			ok = true;
		}
		catch (IOException e) {
		    e.printStackTrace();
			ok = false;
		}
		catch (Exception e) {
		    e.printStackTrace();
			ok = false;
		} finally {
		    StreamUtils.closeQuitely(fos);
		    StreamUtils.closeQuitely(fis);
		    StreamUtils.closeQuitely(zis);
        }
		// if fail; then deleting all extracted files
		if (!ok && deleteOnFail && !CollectionUtils.isEmpty(cacheExtractedFiles)) {
		    FileUtils.safeDelete(cacheExtractedFiles);
			cacheExtractedFiles.clear();
		}
		// returns extracted files if necessary
		if (ok && (extractedFilePaths != null))
			extractedFilePaths.addAll(cacheExtractedFiles);
		return ok;
	}
	/**
	 * Extracts the compression (ZIP) file to the specified directory.
	 *
	 * @param zipFilePath
	 * 		the compression (ZIP) file path to extract
	 * @param destDir
	 * 		the destination directory to extract
	 * @param extractedFilePaths
	 * 		the extracted file paths (included <b>destDir</b> in path).
	 * 		NULL for doing not care
	 *
	 * @return true for extracting successfully; else otherwise
     * @throws Exception thrown if fail
	 */
	public static boolean decompress(String zipFilePath, String destDir, List<String> extractedFilePaths)
	        throws Exception {
		return decompress(zipFilePath, destDir, extractedFilePaths, true);
	}
	/**
	 * Extracts the compression (ZIP) file to the specified directory.
	 *
	 * @param zipFilePath
	 * 		the compression (ZIP) file path to extract
	 * @param destDir
	 * 		the destination directory to extract
	 *
	 * @return true for extracting successfully; else otherwise
     * @throws Exception thrown if fail
	 */
	public static boolean decompress(String zipFilePath, String destDir)
	        throws Exception {
		return decompress(zipFilePath, destDir, null, true);
	}

	/**
	 * Extracts the compression (ZIP) file data to the data map by the compressed entry file name.
	 *
	 * @param zipFileData
	 * 		the compression (ZIP) file data to extract
	 * @param extractedData
	 * 		the extracted data by the compressed entry file name key
	 *
	 * @return true for extracting successfully; else otherwise
     * @throws Exception thrown if fail
	 */
	public static boolean decompress(byte[] zipFileData, Map<String, byte[]> extractedData)
	    throws Exception {
		// Checks parameter
		Assert.isTrue(isZippedData(zipFileData), "Data is NULL or invalid zipped data!");
		Assert.notNull(extractedData, "Extracted Data Cache Storage");
		// Starts extracting
		ZipEntry ze = null;
		ByteArrayOutputStream baos = null;
		ByteArrayInputStream bais = null;
		ZipInputStream zis = null;
		boolean ok = false;
		try {
			// Creates stream from zip data
			bais = new ByteArrayInputStream(zipFileData);
			zis = new ZipInputStream(bais);
			// Extracts every file
			while ((ze = zis.getNextEntry()) != null) {
				// Extracts zip entry
				baos = new ByteArrayOutputStream();
				StreamUtils.copyStream(zis, baos);
				// caches the extracted data if necessary
				extractedData.put(ze.getName(), baos.toByteArray());
				StreamUtils.closeQuitely(baos);
				zis.closeEntry();
			}
			ok = true;
		}
		catch (IOException e) {
		    e.printStackTrace();
			ok = false;
		}
		catch (Exception e) {
		    e.printStackTrace();
			ok = false;
		} finally {
		    StreamUtils.closeQuitely(baos);
		    StreamUtils.closeQuitely(bais);
		    StreamUtils.closeQuitely(zis);
		}
		// if fail; then deleting all extracted files
		if (!ok && !CollectionUtils.isEmpty(extractedData)) {
			extractedData.clear();
		}
		// returns extracted data
		return ok;
	}

	 /**
	  * The method to test if an input stream is a zip archive.
	  *
	  * @param is the input stream to test.
	  *
	  * @return true for zipped stream; else false
	  */
	 public static boolean isZippedStream(InputStream is) {
	     boolean zipped = (is != null);
	     if (zipped) {
	         byte[] MAGIC = { 'P', 'K', 0x3, 0x4 };
	         if (!is.markSupported()) {
	             is = new BufferedInputStream(is);
	         }
	         try {
	             is.mark(MAGIC.length);
	             for (int i = 0; i < MAGIC.length; i++) {
	                 if (MAGIC[i] != (byte) is.read()) {
	                     zipped = false;
	                     break;
	                 }
	             }
	             is.reset();
	         } catch (IOException e) {
	             zipped = false;
	         }
	     }
	     return zipped;
	 }
	 /**
      * The method to test if the specified data is a zipped data.
      *
      * @param zippedData data to test.
      *
      * @return true for zipped data; else false
      */
     public static boolean isZippedData(byte[] zippedData) {
         boolean zipped = !CollectionUtils.isEmpty(zippedData);
         if (zipped) {
             ByteArrayInputStream bais = new ByteArrayInputStream(zippedData);
             zipped = isZippedStream(bais);
             StreamUtils.closeQuitely(bais);
         }
         return zipped;
     }
	 /**
	  * Test if a file is a zipped file.
	  *
	  * @param f the file to test.
	  *
	  * @return true for zipped file; else false
	  */
	 public static boolean isZippedFile(File f) {
	     boolean zipped = (f != null && f.exists() && f.isFile());
	     if (zipped) {
	         byte[] MAGIC = { 'P', 'K', 0x3, 0x4 };
	         byte[] buffer = new byte[MAGIC.length];
	         RandomAccessFile raf = null;
	         try {
	             raf = new RandomAccessFile(f, "r");
	             raf.readFully(buffer);
	             for (int i = 0; i < MAGIC.length; i++) {
	                 if (buffer[i] != MAGIC[i]) {
	                     zipped = false;
	                     break;
	                 }
	             }
	         } catch (Throwable e) {
	             zipped = false;
	         } finally {
	             StreamUtils.closeQuitely(raf);
	         }
	     }
	     return zipped;
	 }
     /**
      * Test if a file is a zipped file.
      *
      * @param filePath the file path to test.
      *
      * @return true for zipped file; else false
      */
     public static boolean isZippedFile(String filePath) {
         return isZippedFile(new File(filePath));
     }
}
