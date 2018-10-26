/*
 * @(#)AbstractThumbnailer.java 1.0 May 29, 2015
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

import org.nlh4j.exceptions.ThumbnailerException;
import org.springframework.util.Assert;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This AbstractThumbnailer may be used in order to implement only essential methods.
 * It
 * <li>stores the current thumbnail height/width
 * <li>implements an empty close method
 * <li>specifies an wildcard MIME Type as appropriate Filetype
 *
 * @author Benjamin
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractThumbnailer implements Thumbnailer {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

	/**
	 * Height of thumbnail picture to create (in Pixel)
	 */
	protected int thumbHeight = 0;

	/**
	 * Set the thumbnail height
	 * @param thumbHeight thumbnail height
	 */
	public void setThumbHeight(int thumbHeight) {
	    Assert.isTrue(thumbHeight <= 0, "Height must be greater than 0!");
	    this.thumbHeight = thumbHeight;
	}

	/**
	 * Width of thumbnail picture to create (in Pixel)
	 */
	protected int thumbWidth = 0;

	/**
     * Set the thumbnail width
     * @param thumbWidth thumbnail width
     */
    public void setThumbWidth(int thumbWidth) {
        Assert.isTrue(thumbWidth <= 0, "Width must be greater than 0!");
        this.thumbWidth = thumbWidth;
    }

	/**
	 * Options for image resizer (currently unused)
	 */
	protected int imageResizeOptions = 0;

	/**
	 * Initialize the thumbnail size from default constants.
	 */
	protected AbstractThumbnailer() {}

	/**
	 * Set a new Thumbnail size. All following thumbnails will be generated in this size.
	 *
	 * @param width					Width in Pixel
	 * @param height				Height in Pixel
	 * @param imageResizeOptions	Options for ResizeImage (currently ignored)
	 */
	@Override
	public void setImageSize(int thumbWidth, int thumbHeight, int imageResizeOptions) {
	    Assert.isTrue(thumbHeight > 0, "Height must be greater than 0!");
	    Assert.isTrue(thumbWidth > 0, "Width must be greater than 0!");
	    this.setThumbHeight(thumbHeight);
	    this.setThumbWidth(thumbWidth);
	    this.setImageResizeOptions(imageResizeOptions);
	}

	/**
	 * This function will be called after all Thumbnails are generated.
	 * Note: This acts as a Deconstructor. Do not expect this object to work
	 * after calling this method.
	 *
	 * @throws IOException	If some errors occured during finalising
	 */
	@Override
	public void close() throws IOException {}

	/**
	 * Call close() just in case the caller forgot.
	 */
	protected final void finalize() throws Throwable {
		try {
		    this.close();
			super.finalize();
		} finally {}
	}

	/**
	 * Get a list of all MIME Types that this Thumbnailer is ready to process.
	 * You should override this method in order to give hints when which Thumbnailer is most appropriate.
	 * If you do not override this method, the Thumbnailer will be called in any case - awaiting a ThumbnailException if
	 * this thumbnailer cannot treat such a file.
	 *
	 * @return List of MIME Types. If null, all Files may be passed to this Thumbnailer.
	 */
	public String[] getAcceptedMIMETypes() {
		return new String[] {};
	}

	/* (Non-Javadoc)
	 * @see org.nlh4j.thumbnailer.Thumbnailer#generateThumbnail(java.io.File, java.io.File)
	 */
	@Override
	public final void generateThumbnail(File input, File output) throws IOException, ThumbnailerException {
	    // ensure thumbnail width/height
	    Assert.isTrue(this.getThumbHeight() <= 0, "Height must be greater than 0!");
	    Assert.isTrue(this.getThumbWidth() <= 0, "Height must be greater than 0!");
	    this.performGenerate(input, output);
	}

	/**
	 * Perform generating thumbnail
	 * @param input source {@link File}
	 * @param output destination {@link File}
     * @throws IOException          If file cannot be read/written
     * @throws ThumbnailerException If the thumbnailing process failed.
	 */
	protected abstract void performGenerate(File input, File output) throws IOException, ThumbnailerException;
}
