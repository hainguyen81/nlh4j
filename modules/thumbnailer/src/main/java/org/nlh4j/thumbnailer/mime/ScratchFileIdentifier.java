/*
 * @(#)ScratchFileIdentifier.java 1.0 May 29, 2015
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

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.nlh4j.util.EncryptUtils;
import org.nlh4j.util.FileUtils;
import org.nlh4j.util.LogUtils;
import org.springframework.util.StringUtils;

/**
 * Add detection of Scratch files.
 * A MIME Type didn't exist, so I invented "application/x-mit-scratch".
 */
public class ScratchFileIdentifier implements MimeTypeIdentifier {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
	protected static final String SCRATCH_MIME_TYPE = "application/x-mit-scratch";
	protected static final String SCRATCH_EXTENSION = "sb";
	protected static final String MAGIC_SCRATCH_HEADER = "ScratchV0";

	/* (Non-Javadoc)
	 * @see org.nlh4j.thumbnailer.mime.MimeTypeIdentifier#identify(java.lang.String, byte[], java.io.File)
	 */
	@Override
	public String identify(String mimeType, byte[] bytes, File file) {
		if (!StringUtils.hasText(mimeType)
		        && SCRATCH_EXTENSION.equalsIgnoreCase(
		                FileUtils.getFileExtension(file.getName()))) {
			return SCRATCH_MIME_TYPE;
		}
		if (startWith(bytes, MAGIC_SCRATCH_HEADER)) {
			return SCRATCH_MIME_TYPE;
		}
		return mimeType;
	}

	/**
	 * Detect the specified byte whether start with the specified string value
	 * @param haystick to check
	 * @param needle to check startWith
	 * @return true for start-with
	 */
	private boolean startWith(byte[] haystick, String needle) {
	    byte[] needleBytes = null;
	    boolean ok = true;
		try {
		    needleBytes = needle.getBytes("US-ASCII");
			for (int i = 0; i < needleBytes.length; i++) {
				if (haystick[i] != needleBytes[i]) {
				    ok = false;
				    break;
				}
			}
		} catch (Exception e) {
		    LogUtils.logWarn(this.getClass(), e.getMessage());
		    ok = false;
		}
		if (!ok) {
    		try {
    		    ok = true;
                needleBytes = needle.getBytes(EncryptUtils.ENCODING_UTF8);
                for (int i = 0; i < needleBytes.length; i++) {
                    if (haystick[i] != needleBytes[i]) {
                        ok = false;
                        break;
                    }
                }
            } catch (Exception e) {
                LogUtils.logWarn(this.getClass(), e.getMessage());
                ok = false;
            }
		}
		return ok;
	}

	/* (Non-Javadoc)
	 * @see org.nlh4j.thumbnailer.mime.MimeTypeIdentifier#getExtensionsFor(java.lang.String)
	 */
	@Override
	public List<String> getExtensionsFor(String mimeType) {
	    List<String> extions = new LinkedList<String>();
		if (SCRATCH_MIME_TYPE.equalsIgnoreCase(mimeType)) {
			extions.add(SCRATCH_EXTENSION);
		}
		return extions;
	}
}
