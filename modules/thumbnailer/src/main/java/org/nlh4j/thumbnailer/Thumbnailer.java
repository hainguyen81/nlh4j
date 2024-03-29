/*
 * @(#)Thumbnailer.java
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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import org.nlh4j.exceptions.ThumbnailerException;
import org.nlh4j.util.CollectionUtils;

/**
 * This interface is implemented by any method suitable to create a thumbnail of a given File.
 *
 * @author Benjamin
 */
public interface Thumbnailer extends Closeable, Serializable {

    /** Default thumbnail height */
    static final int THUMBNAIL_DEFAULT_HEIGHT = 120;
    /** Default thumbnail width */
    static final int THUMBNAIL_DEFAULT_WIDTH = 160;

	/**
	 * Generate a Thumbnail of the input file.
	 *
	 * @param input		Input file that should be processed
	 * @param output	File in which should be written
	 * @param mimeType	MIME-Type of input file (null if unknown)
	 * @throws IOException			If file cannot be read/written
	 * @throws ThumbnailerException If the thumbnailing process failed.
	 */
	default void generateThumbnail(File input, File output, String mimeType) throws IOException, ThumbnailerException {
	    String[] supportedMimeTypes = this.getAcceptedMIMETypes();
	    if (!CollectionUtils.isEmpty(supportedMimeTypes)
	            && Arrays.asList(supportedMimeTypes).indexOf(mimeType) < 0) {
	        throw new ThumbnailerException();
	    }
	    this.generateThumbnail(input, output);
	}

	/**
	 * Generate a Thumbnail of the input file.
	 *
	 * @param input		Input file that should be processed
	 * @param output	File in which should be written
	 * @throws IOException			If file cannot be read/written
	 * @throws ThumbnailerException If the thumbnailing process failed.
	 */
	void generateThumbnail(File input, File output) throws IOException, ThumbnailerException;

	/**
	 * This function will be called after all Thumbnails are generated.
	 * Note: This acts as a Deconstructor. Do not expect this object to work
	 * after calling this method.
	 *
	 * @throws IOException	If some errors occured during finalising
	 */
	void close() throws IOException;


	/**
	 * Set a new Thumbnail size. All following thumbnails will be generated in this size.
	 *
	 * @param width					Width in Pixel
	 * @param height				Height in Pixel
	 * @param imageResizeOptions	Options for ResizeImage (currently ignored)
	 */
	void setImageSize(int width, int height, int imageResizeOptions);

	/**
	 * Get the currently set Image Width of this Thumbnailer.
	 *
	 * @return	image width of created thumbnails.
	 */
	default int getThumbWidth() {
	    return THUMBNAIL_DEFAULT_WIDTH;
	}

	/**
	 * Get the currently set Image Height of this Thumbnailer.
	 *
	 * @return	image height of created thumbnails.
	 */
	default int getThumbHeight() {
	    return THUMBNAIL_DEFAULT_HEIGHT;
	}

	/**
	 * Get a list of all MIME Types that this Thumbnailer is ready to process.
	 *
	 * @return List of MIME Types. If null, all Files may be passed to this Thumbnailer.
	 */
	String[] getAcceptedMIMETypes();
}
