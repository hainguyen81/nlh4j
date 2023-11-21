/*
 * @(#)PptFileIdentifier.java
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

import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.sl.usermodel.SlideShowFactory;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.LogUtils;
import org.nlh4j.util.StreamUtils;

/**
 * Powerpoint (.ppt, .pps) file identifier
 *
 * @author Hai Nguyen
 *
 */
public class PptFileIdentifier extends OfficeFileIdentifier {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * Initialize a new instance of class {@link PptFileIdentifier}
     */
	public PptFileIdentifier() {
		super();
		super.getExtions().add("ppt");
		super.getExtions().add("pps");
	}

	/* (Non-Javadoc)
	 * @see org.nlh4j.thumbnailer.mime.MimeTypeIdentifier#identify(java.lang.String, byte[], java.io.File)
	 */
	@Override
	public String identify(String mimeType, byte[] bytes, File file) {
		if (super.isOfficeFile(mimeType) && !PPT_MIME_TYPE.equalsIgnoreCase(mimeType)) {
		    FileInputStream fis = null;
		    SlideShow<?, ?> ss = null;
            try {
                fis = new FileInputStream(file);
                ss = SlideShowFactory.create(fis);
                return (!CollectionUtils.isEmpty(ss.getSlides())
                        ? PPT_MIME_TYPE : mimeType);
            } catch (Exception e) {
                LogUtils.logWarn(this.getClass(), e.getMessage());
            } finally {
                StreamUtils.closeQuitely(ss);
                StreamUtils.closeQuitely(fis);
            }
		}
		return mimeType;
	}
}
