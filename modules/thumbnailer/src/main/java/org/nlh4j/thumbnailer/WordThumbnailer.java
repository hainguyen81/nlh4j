/*
 * @(#)WordThumbnailer.java
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

/**
 * Dummy class for converting Text documents into Openoffice-Textfiles.
 *
 * Tika could be used to detect ms-word-files, but quite a heavy library. Maybe it would be useful as a preperator as well?
 * @see JodThumbnailer
 */
public class WordThumbnailer extends JodThumbnailer {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /* (Non-Javadoc)
     * @see org.nlh4j.thumbnailer.JodThumbnailer#getStandardOpenOfficeExtension()
     */
	protected String getStandardOpenOfficeExtension() {
		return ".odt";
	}

	/* (Non-Javadoc)
	 * @see org.nlh4j.thumbnailer.JodThumbnailer#getStandardZipExtension()
	 */
	protected String getStandardZipExtension() {
		return "docx";
	}

	/* (Non-Javadoc)
	 * @see org.nlh4j.thumbnailer.JodThumbnailer#getStandardOfficeExtension()
	 */
	protected String getStandardOfficeExtension() {
		return "doc";
	}

    /**
     * Get a List of accepted File Types.
     * All Text Office Formats that OpenOffice understands are accepted.
     * (txt, rtf, doc, docx, wpd)
     *
     * @return MIME-Types
     * @see <a href="http://www.artofsolving.com/opensource/jodconverter/guide/supportedformats">supportedformats</a>
     */
	@Override
	public String[] getAcceptedMIMETypes() {
		return new String[] {
				"text/plain"
		        , "text/rtf"
		        //    , "application/msword"
		        , "application/vnd.ms-word"
		        , "application/vnd.openxmlformats-officedocument.wordprocessingml"
		        , "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
		        , "application/wordperfect"
                //    , "application/vnd.ms-office" // doc?
                //	, "application/zip" // docx?
		};
	}

}
