/*
 * @(#)OfficeFileIdentifier.java 1.0 May 29, 2015
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

import java.util.LinkedList;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * Improve detection of non-XML Office files.
 *
 * Requires:
 *  - POI (version 3.7 or higher)
 */
public abstract class OfficeFileIdentifier implements MimeTypeIdentifier {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
	protected static final String PPT_MIME_TYPE = "application/vnd.ms-powerpoint";
	protected static final String XLS_MIME_TYPE = "application/vnd.ms-excel";
	protected static final String DOC_MIME_TYPE = "application/vnd.ms-word";
	protected static final String MS_OFFICE_MIME_TYPE = "application/vnd.ms-office";

	/** office file extension(s) */
	private List<String> extions;
    protected final List<String> getExtions() {
        if (this.extions == null) {
            this.extions = new LinkedList<String>();
        }
        return this.extions;
    }

	/* (Non-Javadoc)
	 * @see org.nlh4j.thumbnailer.mime.MimeTypeIdentifier#getExtensionsFor(java.lang.String)
	 */
	@Override
	public List<String> getExtensionsFor(String mimeType) {
		return (PPT_MIME_TYPE.equalsIgnoreCase(mimeType) ? this.getExtions() : null);
	}

	/**
	 * Get a boolean value indicating that the specified MIME type whether is office file
	 * @param mimeType to check
	 * @return true for office file
	 */
	protected boolean isOfficeFile(String mimeType) {
	    return (StringUtils.hasText(mimeType)
	            && (MS_OFFICE_MIME_TYPE.equalsIgnoreCase(mimeType)
	                    || mimeType.toLowerCase().startsWith("application/vnd.ms")
	                    || mimeType.toLowerCase().startsWith("application/vnd.openxmlformats")));
	}
}
