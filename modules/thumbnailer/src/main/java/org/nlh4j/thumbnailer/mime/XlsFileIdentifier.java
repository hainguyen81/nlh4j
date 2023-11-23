/*
 * @(#)XlsFileIdentifier.java
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
import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.nlh4j.util.LogUtils;
import org.nlh4j.util.StreamUtils;

/**
 * Excel (.xls, .xlsx) file identifier
 *
 * @author Hai Nguyen
 *
 */
public class XlsFileIdentifier extends OfficeFileIdentifier {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of class {@link XlsFileIdentifier}
     */
	public XlsFileIdentifier() {
		super();
		super.getExtions().add("xls");
		super.getExtions().add("xlsx");
	}

	/* (Non-Javadoc)
	 * @see org.nlh4j.thumbnailer.mime.MimeTypeIdentifier#identify(java.lang.String, byte[], java.io.File)
	 */
	@Override
	public String identify(String mimeType, byte[] bytes, File file) {
		if (super.isOfficeFile(mimeType) && !XLS_MIME_TYPE.equalsIgnoreCase(mimeType)) {
		    FileInputStream fis = null;
		    Workbook workbook = null;
            try {
                fis = new FileInputStream(file);
                workbook = WorkbookFactory.create(fis);
                return (workbook.getNumberOfSheets() > 0
                        ? XLS_MIME_TYPE : mimeType);
            } catch (Exception e) {
                LogUtils.logWarn(this.getClass(), e.getMessage());
            } finally {
                StreamUtils.closeQuitely(workbook);
                StreamUtils.closeQuitely(fis);
            }
		}
		return mimeType;
	}
}
