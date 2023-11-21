/*
 * @(#)MimeTypeIdentifier.java
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
import java.io.Serializable;
import java.util.List;

/**
 * Helper Classes for MimeTypeDetector.
 * @author Benjamin
 */
public interface MimeTypeIdentifier extends Serializable {

	/**
	 * Try to identify the mimeType.
	 *
	 * Contract: If the implementing class doesn't know anything,
	 * it returns the current mimeType.
	 *
	 * @param mimeType	Currently detected mimeType
	 * @param bytes		512 Bytes of Header for Magic Detection
	 * @param file		Filename of the File to detect
	 * @return		MIME Type detected.
	 */
	String identify(String mimeType, byte[] bytes, File file);

	/**
	 * Get File Extensions for a known MIME Type.
	 * @param 	mimeType Currently detected mimeType
	 * @return	List of file extensions (main extension first).
	 */
	List<String> getExtensionsFor(String mimeType);
}
