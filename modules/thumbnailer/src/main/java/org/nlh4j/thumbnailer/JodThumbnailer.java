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
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.jodconverter.office.OfficeException;
import org.nlh4j.exceptions.ThumbnailerException;
import org.nlh4j.util.LogUtils;
import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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
    /** The Port on which to connect (must be unoccupied) */
    private static final int OOO_DEFAULT_PORT = 8100;
    /** How long may a conversion take? (in ms) */
    private static final long JOD_EXEUTION_DOCUMENT_TIMEOUT = 12000;

    /**
     * OpenOffice Home Folder (Configurable)
     */
    @Getter
    private int openOfficePort = OOO_DEFAULT_PORT;
    public void setOpenOfficePort(int openOfficePort) {
        if (openOfficePort > 0) {
            this.openOfficePort = openOfficePort;
        }
    }

    /**
     * OpenOffice Connection Timeout
     */
    @Getter
    private long openOfficeExecutionTimeout = JOD_EXEUTION_DOCUMENT_TIMEOUT;
    public void setOpenOfficeExecutionTimeout(long openOfficeExecutionTimeout) {
        this.openOfficeExecutionTimeout = Math.max(openOfficeExecutionTimeout, 0);
    }

    /**
     * OpenOffice Home Folder (Configurable)
     */
    @Getter
    @Setter
    private String openOfficeHomeFolder = null;

    /**
     * OpenOffice Template Profile Folder (Configurable)
     */
    @Getter
    @Setter
    private String openOfficeTemplateProfileFolder = null;

    /** JOD Converter */
    @Getter(value = AccessLevel.PROTECTED)
    private transient OfficeDocumentConverter officeConverter = null;
	/** JOD Office Manager */
	private transient OfficeManager officeManager = null;
	/**
     * Check if a connection to OpenOffice is established.
     * @return true if connected.
     */
    public boolean isConnected() {
        return (officeManager != null && officeManager.isRunning());
    }
    /**
     * Disconnect OpenOffice
     */
    public void disconnect() {
        if (this.officeManager != null) {
            try { this.officeManager.stop(); }
            catch (Exception e) {
                LogUtils.logWarn(this.getClass(), e.getMessage());
            } finally {
                this.officeManager = null;
            }
        }
    }
    /**
     * Connect OpenOffice
     */
    public boolean connect() {
        return this.connect(Boolean.FALSE);
    }
	/**
	 * Connect OpenOffice
	 * @param forced specify forcing connection OpenOffice
	 */
	public boolean connect(boolean forced) {
	    // if connected
	    if (!forced && this.isConnected()) {
	        return true;
	    }
	    // if connected and forced, then closing it and re-conecting
	    if (this.isConnected()) this.disconnect();
	    // perform connecting
	    try {
    	    // connect office
    	    DefaultOfficeManagerConfiguration config =
    	            new DefaultOfficeManagerConfiguration()
    	            .setPortNumber(this.getOpenOfficePort())
    	            .setTaskExecutionTimeout(this.getOpenOfficeExecutionTimeout());
    	    // OpenOffice home folder
    	    if (StringUtils.hasText(this.getOpenOfficeHomeFolder())
    	            && Files.exists(Paths.get(this.getOpenOfficeHomeFolder()))) {
    	        config.setOfficeHome(this.getOpenOfficeHomeFolder());
    	    }
    	    // OpenOffice template profile folder
    	    if (StringUtils.hasText(this.getOpenOfficeTemplateProfileFolder())
    	            && Files.exists(Paths.get(this.getOpenOfficeTemplateProfileFolder()))
    	            && Files.isDirectory(Paths.get(this.getOpenOfficeTemplateProfileFolder()))) {
    	        config.setTemplateProfileDir(new File(this.getOpenOfficeTemplateProfileFolder()));
    	    } else if (StringUtils.hasText(this.getOpenOfficeTemplateProfileFolder())
                    && !Files.exists(Paths.get(this.getOpenOfficeTemplateProfileFolder()))) {
    	        LogUtils.logWarn(this.getClass(),
    	                "The template profile folder {" + this.getOpenOfficeTemplateProfileFolder() + "} "
    	                        + "not found. Creating temporary template profile folder!");
    	    } else if (!StringUtils.hasText(this.getOpenOfficeTemplateProfileFolder())) {
    	        LogUtils.logWarn(this.getClass(), "Creating temporary template profile folder!");
    	    }
            // build connection
            officeManager = config.buildOfficeManager();
            officeManager.start();
            officeConverter = new OfficeDocumentConverter(officeManager);
	    } catch (Exception e) {
	        LogUtils.logError(this.getClass(), e.getMessage());
	        throw new ThumbnailerException(e);
	    }
        return true;
	}

	/**
	 * Thumbnail Extractor for OpenOffice Files
	 */
	@Getter(value = AccessLevel.PROTECTED)
	private OpenOfficeThumbnailer openOfficeThumbnailer = null;

	/**
	 * MimeIdentification
	 */
	protected MimeTypeDetector mimeTypeDetector = null;




	public JodThumbnailer()
	{
		ooo_thumbnailer = new OpenOfficeThumbnailer();
		mimeTypeDetector = new MimeTypeDetector();
		temporaryFilesManager = new TemporaryFilesManager();
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
