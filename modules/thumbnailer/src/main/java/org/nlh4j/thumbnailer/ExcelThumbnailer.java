/*
 * @(#)ExcelThumbnailer.java
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
 * Dummy class for converting Spreadsheet documents into Openoffice-Textfiles.
 * @see JodThumbnailer
 */
public class ExcelThumbnailer extends JodThumbnailer {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /* (Non-Javadoc)
     * @see org.nlh4j.thumbnailer.JodThumbnailer#getStandardOpenOfficeExtension()
     */
	protected String getStandardOpenOfficeExtension() {
		return "ods";
	}

	/* (Non-Javadoc)
	 * @see org.nlh4j.thumbnailer.JodThumbnailer#getStandardZipExtension()
	 */
	protected String getStandardZipExtension() {
		return "xlsx";
	}

	/* (Non-Javadoc)
	 * @see org.nlh4j.thumbnailer.JodThumbnailer#getStandardOfficeExtension()
	 */
	protected String getStandardOfficeExtension() {
		return "xls";
	}

	/**
     * Get a List of accepted File Types.
     * All Spreadsheet Office Formats that OpenOffice understands are accepted.
     *
     * @return MIME-Types
     * @see <a href="http://www.artofsolving.com/opensource/jodconverter/guide/supportedformats">supportedformats</a>
     */
	@Override
	public String[] getAcceptedMIMETypes() {
		return new String[] {
				"application/vnd.ms-excel"
		        , "application/vnd.openxmlformats-officedocument.spreadsheetml"
		        , "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                //    , "application/vnd.ms-office" // xls?
                //    , "application/zip" // xlsx?
		};
	}
}
