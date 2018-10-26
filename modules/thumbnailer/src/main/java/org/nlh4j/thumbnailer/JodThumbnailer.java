/*
 * @(#)JodThumbnailer.java 1.0 May 29, 2015
 * Copyright (C) 2011 Come_IN Computerclubs (University of Siegen)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: Come_IN-Team <come_in-team@listserv.uni-siegen.de>
 */
package org.nlh4j.thumbnailer;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.jodconverter.OfficeDocumentConverter;
import org.jodconverter.office.OfficeException;
import org.jodconverter.office.OfficeManager;
import org.nlh4j.exceptions.ThumbnailerException;

import com.sun.media.jfxmediaimpl.platform.Platform;

/**
 * This generates a thumbnail of Office-Files by converting them into the OpenOffice-Format first.
 *
 * Performance note: this may take several seconds per file. The first time this Thumbnailer is called, OpenOffice is started.
 *
 * Depends on:
 * <li>OpenOffice 3 / LibreOffice
 * <li>JODConverter 3 beta5 or higher
 * <li>Aperture Core (MIME Type detection)
 * <li>OpenOfficeThumbnailer
 *
 * @TODO Be stricter about which kind of files to process. Currently it converts just like everything. (See tests/ThumbnailersFailingTest)
 *
 * @author Benjamin
 */
public abstract class JodThumbnailer extends AbstractThumbnailer {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

	/**
	 * JOD Office Manager
	 */
	private static transient OfficeManager officeManager = null;
	static {
	    if (officeManager == null) {
	        DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration()
	                .setPortNumber(openOfficePort)
	                .setTaskExecutionTimeout(JOD_DOCUMENT_TIMEOUT);

	            if (openOfficeHomeFolder != null)
	                config.setOfficeHome(openOfficeHomeFolder);

	            if (openOfficeTemplateProfileDir != null)
	            {
	                if (openOfficeTemplateProfileDir.exists())
	                    config.setTemplateProfileDir(openOfficeTemplateProfileDir);
	                else
	                    mLog.info("No Template Profile Folder found at " + openOfficeTemplateProfileDir.getAbsolutePath() + " - Creating temporary one.");
	            }
	            else
	                mLog.info("Creating temporary profile folder...");

	            officeManager = config.buildOfficeManager();
	    }
	}

	/**
	 * JOD Converter
	 */
	protected static OfficeDocumentConverter officeConverter = null;

	/**
	 * Thumbnail Extractor for OpenOffice Files
	 */
	protected OpenOfficeThumbnailer ooo_thumbnailer = null;

	/**
	 * MimeIdentification
	 */
	protected MimeTypeDetector mimeTypeDetector = null;

	private TemporaryFilesManager temporaryFilesManager = null;

	/**
	 * OpenOffice Home Folder (Configurable)
	 */
	private static String openOfficeHomeFolder = null;

	/**
	 * The Port on which to connect (must be unoccupied)
	 */
	private final static int OOO_DEFAULT_PORT = 8100;

	/**
	 * OpenOffice Home Folder (Configurable)
	 */
	private static int openOfficePort = OOO_DEFAULT_PORT;

	/**
	 * How long may a conversion take? (in ms)
	 */
	private static final long JOD_DOCUMENT_TIMEOUT = 12000;

	public static void setOpenOfficePort(int openOfficePort) {
		if (openOfficePort > 0)
			JodThumbnailer.openOfficePort = openOfficePort;
	}

	public static void setOpenOfficeHomeFolder(String openOfficeHomeFolder)
	{
		JodThumbnailer.openOfficeHomeFolder = openOfficeHomeFolder;
	}

	public static void setOpenOfficeProfileFolder(String paramOpenOfficeProfile)
	{
		if (paramOpenOfficeProfile != null)
			JodThumbnailer.openOfficeTemplateProfileDir = new File(paramOpenOfficeProfile);
	}

	public JodThumbnailer()
	{
		ooo_thumbnailer = new OpenOfficeThumbnailer();
		mimeTypeDetector = new MimeTypeDetector();
		temporaryFilesManager = new TemporaryFilesManager();
	}

	protected static File openOfficeTemplateProfileDir = null;


	/**
	 * Start OpenOffice-Service and connect to it.
	 * (Does not reconnect if already connected.)
	 */
	public static void connect() { 	connect(false); }

	/**
	 * Start OpenOffice-Service and connect to it.
	 * @param forceReconnect	Connect even if he is already connected.
	 */
	public static void connect(boolean forceReconnect)
	{
		if (!forceReconnect && isConnected())
			return;

		DefaultOfficeManagerConfiguration config = new DefaultOfficeManagerConfiguration()
			.setPortNumber(openOfficePort)
			.setTaskExecutionTimeout(JOD_DOCUMENT_TIMEOUT);

		if (openOfficeHomeFolder != null)
			config.setOfficeHome(openOfficeHomeFolder);

		if (openOfficeTemplateProfileDir != null)
		{
			if (openOfficeTemplateProfileDir.exists())
				config.setTemplateProfileDir(openOfficeTemplateProfileDir);
			else
				mLog.info("No Template Profile Folder found at " + openOfficeTemplateProfileDir.getAbsolutePath() + " - Creating temporary one.");
		}
		else
			mLog.info("Creating temporary profile folder...");

		officeManager = config.buildOfficeManager();
		officeManager.start();

		officeConverter = new OfficeDocumentConverter(officeManager);
	}

	/**
	 * Check if a connection to OpenOffice is established.
	 * @return	True if connected.
	 */
	public static boolean isConnected()
	{
		return officeManager != null && officeManager.isRunning();
	}

	/**
	 * Stop the OpenOffice Process and disconnect.
	 */
	public static void disconnect()
	{
		// close the connection
		if (officeManager != null)
			officeManager.stop();
		officeManager = null;
	}

	public void close() throws IOException
	{
		try {
			try {
				temporaryFilesManager.deleteAllTempfiles();
				ooo_thumbnailer.close();
			} finally {
				disconnect();
			}
		} finally {
			super.close();
		}
	}



	/**
	 * Generates a thumbnail of Office files.
	 *
	 * @param input		Input file that should be processed
	 * @param output	File in which should be written
	 * @throws IOException			If file cannot be read/written
	 * @throws ThumbnailerException If the thumbnailing process failed.
	 */
	@Override
	public void generateThumbnail(File input, File output) throws IOException, ThumbnailerException {
		// Connect on first use
		if (!isConnected())
			connect();

		File outputTmp = null;
		try {
			outputTmp = File.createTempFile("jodtemp", "." + getStandardOpenOfficeExtension());

			// Naughty hack to circumvent invalid URLs under windows (C:\\ ...)
			if (Platform.isWindows())
				input = new File(input.getAbsolutePath().replace("\\\\", "\\"));

			try {
				officeConverter.convert(input, outputTmp);
			} catch (OfficeException e) {
				throw new ThumbnailerException("Could not convert into OpenOffice-File", e);
			}
			if (outputTmp.length() == 0)
			{
				throw new ThumbnailerException("Could not convert into OpenOffice-File (file was empty)...");
			}

			ooo_thumbnailer.generateThumbnail(outputTmp, output);
		} finally {
			IOUtil.deleteQuietlyForce(outputTmp);
		}
	}

	/**
	 * Generate a Thumbnail of the input file.
	 * (Fix file ending according to MIME-Type).
	 *
	 * @param input		Input file that should be processed
	 * @param output	File in which should be written
	 * @param mimeType	MIME-Type of input file (null if unknown)
	 * @throws IOException			If file cannot be read/written
	 * @throws ThumbnailerException If the thumbnailing process failed.
	 */
	public void generateThumbnail(File input, File output, String mimeType) throws IOException, ThumbnailerException {
		String ext = FilenameUtils.getExtension(input.getName());
		if (!mimeTypeDetector.doesExtensionMatchMimeType(ext, mimeType))
		{
			String newExt;
			if ("application/zip".equals(mimeType))
				newExt = getStandardZipExtension();
			else if ("application/vnd.ms-office".equals(mimeType))
				newExt = getStandardOfficeExtension();
			else
				newExt = mimeTypeDetector.getStandardExtensionForMimeType(mimeType);

			input = temporaryFilesManager .createTempfileCopy(input, newExt);
		}

		generateThumbnail(input, output);
	}

	protected abstract String getStandardZipExtension();
	protected abstract String getStandardOfficeExtension();
	protected abstract String getStandardOpenOfficeExtension();

	public void setImageSize(int thumbWidth, int thumbHeight, int imageResizeOptions) {
		super.setImageSize(thumbWidth, thumbHeight, imageResizeOptions);
		ooo_thumbnailer.setImageSize(thumbWidth, thumbHeight, imageResizeOptions);
	}
}
