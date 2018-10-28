/*
 * @(#)Office2K7FileIdentifier.java 1.0 May 29, 2015
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
package org.nlh4j.thumbnailer.mime;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.nlh4j.util.LogUtils;
import org.nlh4j.util.StreamUtils;
import org.springframework.util.StringUtils;

/**
 * Add detection of Office2007 files (and OpenOffice files).
 * Magic numbers don't help here, only introspection of the zip.
 */
public class Office2K7FileIdentifier implements MimeTypeIdentifier {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    protected static final String DOC_ZIP_ENTRY = "word/document.xml";
    protected static final String DOC_MIME_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml";
    protected static final String PPT_ZIP_ENTRY = "ppt/presentation.xml";
    protected static final String PPT_MIME_TYPE = "application/vnd.openxmlformats-officedocument.presentationml";
    protected static final String XL_ZIP_ENTRY = "xl/workbook.xml";
    protected static final String XL_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml";

    /* (Non-Javadoc)
     * @see org.nlh4j.thumbnailer.mime.MimeTypeIdentifier#identify(java.lang.String, byte[], java.io.File)
     */
	@Override
	public String identify(String mimeType, byte[] bytes, File file) {
		if (!StringUtils.hasText(mimeType)
		        || "application/zip".equalsIgnoreCase(mimeType)
		        || mimeType.startsWith("application/vnd.")) {
			ZipFile zipFile = null;
			ZipEntry entry = null;
			InputStream is = null;
			try {
				 zipFile = new ZipFile(file);
				 entry = zipFile.getEntry(DOC_ZIP_ENTRY);
				 if (entry != null) return DOC_MIME_TYPE;
				 entry = zipFile.getEntry(PPT_ZIP_ENTRY);
				 if (entry != null) return PPT_MIME_TYPE;
				 entry = zipFile.getEntry(XL_ZIP_ENTRY);
				 if (entry != null) return XL_MIME_TYPE;
				 entry = zipFile.getEntry("mimetype");
				 if (entry != null) {
				     is = zipFile.getInputStream(entry);
				     return detectOpenOfficeMimeType(is);
				 }
			} catch (ZipException e) {
			    LogUtils.logWarn(this.getClass(), e.getMessage());
				return mimeType; // Zip file damaged or whatever. Silently give up.
			} catch (IOException e) {
			    LogUtils.logWarn(this.getClass(), e.getMessage());
				return mimeType; // Zip file damaged or whatever. Silently give up.
			} finally {
				StreamUtils.closeQuitely(is);
				StreamUtils.closeQuitely(zipFile);
			}
		}
		return mimeType;

	}

	/**
	 * Detect MIME type of the office file {@link InputStream}
	 * @param is to detect
	 * @return office MIME type
	 * @throws IOException thrown if failed
	 */
	protected String detectOpenOfficeMimeType(InputStream is) throws IOException {
	    InputStreamReader isr = null;
	    BufferedReader br = null;
		try {
		    isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			return br.readLine();
		} finally {
		    StreamUtils.closeQuitely(br);
			StreamUtils.closeQuitely(isr);
		}
	}

	/* (Non-Javadoc)
	 * @see org.nlh4j.thumbnailer.mime.MimeTypeIdentifier#getExtensionsFor(java.lang.String)
	 */
	@Override
	public List<String> getExtensionsFor(String mimeType) {
		return new LinkedList<String>(); // I don't know - Aperture knows better than me.
	}
}
