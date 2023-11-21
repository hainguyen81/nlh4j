/*
 * @(#)ThumbnailerManager.java
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
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.nlh4j.exceptions.ThumbnailerException;
import org.nlh4j.thumbnailer.util.MimeTypeDetector;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.LogUtils;
import org.nlh4j.util.StreamUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * This class manages all available Thumbnailers.
 * Its purpose is to delegate a File to the appropriate Thumbnailer in order to get a Thumbnail of it.
 * This is done in a fall-through manner: If several Thumbnailer can handle a specific filetype,
 * all are tried until a Thumbnail could be created.
 *
 * Fill this class with available Thumbnailers via the registerThumbnailer()-Method.
 * Then call generateThumbnail().
 *
 * @author Benjamin
 */
public class ThumbnailerManager extends AbstractThumbnailer {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

	/**
	 * @var MIME Type for "all MIME" within thumbnailers Hash
	 */
	private static final String ALL_MIME_WILDCARD = "*/*";

	/**
	 * Thumbnailers per MIME-Type they accept (ALL_MIME_WILDCARD for all)
	 */
	private transient Map<String, List<Thumbnailer>> thumbnailers;
	protected final Map<String, List<Thumbnailer>> getThumbnailers() {
	    if (this.thumbnailers == null) {
	        this.thumbnailers = new LinkedHashMap<String, List<Thumbnailer>>();
	    }
        return this.thumbnailers;
	}

	/**
	 * All Thumbnailers.
	 */
	private transient Queue<Thumbnailer> allThumbnailers;
    protected final Queue<Thumbnailer> getAllThumbnailers() {
        if (this.allThumbnailers == null) {
            this.allThumbnailers = new LinkedList<Thumbnailer>();
        }
        return this.allThumbnailers;
    }

	/**
	 * Magic Mime Detection ... a wrapper class to Aperature's Mime thingies.
	 */
	private MimeTypeDetector mimeTypeDetector;
    protected final MimeTypeDetector getMimeTypeDetector() {
        if (this.mimeTypeDetector == null) {
            this.mimeTypeDetector = new MimeTypeDetector();
        }
        return this.mimeTypeDetector;
    }
    protected void setMimeTypeDetector(MimeTypeDetector mimeTypeDetector) {
        Assert.notNull(mimeTypeDetector, "mimeTypeDetector");
        this.mimeTypeDetector = mimeTypeDetector;
    }

	/**
	 * Initialize a new instance of class {@link ThumbnailerManager}
	 */
	public ThumbnailerManager() {
		// Execute close() when JVM shuts down (if it wasn't executed before).
		final ThumbnailerManager self = this;
		Runtime.getRuntime().addShutdownHook(
		        new Thread() {

		            /* (Non-Javadoc)
		             * @see java.lang.Thread#run()
		             */
		            @Override
		            public void run() {
		                StreamUtils.closeQuitely(self);
		            }
		        });
	}

	/**
     * Add a Thumbnailer-Class to the list of available Thumbnailers
     * Note that the order you add Thumbnailers may make a difference:
     * First added Thumbnailers are tried first, if one fails, the next
     * (that claims to be able to treat such a document) is tried.
     * (Thumbnailers that claim to treat all MIME Types are tried last, though.)
     *
     * @param thumbnailer   Thumbnailer to add.
     */
    public void registerThumbnailer(Thumbnailer thumbnailer) {
        Assert.notNull(thumbnailer, "thumbnailer");
        String[] acceptMIME = thumbnailer.getAcceptedMIMETypes();
        if (CollectionUtils.isEmpty(acceptMIME)) {
            if (!this.getThumbnailers().containsKey(ALL_MIME_WILDCARD)) {
                this.getThumbnailers().put(ALL_MIME_WILDCARD, new LinkedList<Thumbnailer>());
            }
            this.getThumbnailers().get(ALL_MIME_WILDCARD).add(thumbnailer);
        } else {
            for (String mime : acceptMIME) {
                if (!this.getThumbnailers().containsKey(mime)) {
                    this.getThumbnailers().put(mime, new LinkedList<Thumbnailer>());
                }
                this.getThumbnailers().get(mime).add(thumbnailer);
            }
        }
        this.getAllThumbnailers().add(thumbnailer);
        thumbnailer.setImageSize(this.getThumbWidth(), this.getThumbHeight(), this.getImageResizeOptions());
    }

    /**
     * Instead of a deconstructor:
     * De-initialize ThumbnailManager and its thumbnailers.
     *
     * This functions should be called before termination of the program,
     * and Thumbnails can't be generated after calling this function.
     */
    public void close() {
        for (Thumbnailer thumbnailer: this.getAllThumbnailers()) {
            StreamUtils.closeQuitely(thumbnailer);
        }
        this.getThumbnailers().clear();
        this.getAllThumbnailers().clear();
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.thumbnailer.AbstractThumbnailer#performGenerate(java.io.File, java.io.File)
     */
    @Override
    protected final void performGenerate(File input, File output) throws IOException, ThumbnailerException {
        this.generateThumbnail(input, output, null);
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.thumbnailer.Thumbnailer#generateThumbnail(java.io.File, java.io.File, java.lang.String)
     */
    @Override
    public final void generateThumbnail(File input, File output, String mimeType) throws IOException, ThumbnailerException {
        Assert.isTrue(input != null && input.exists() && input.canRead(), "sourceFile");
        boolean ok = false;
        String detectMimeType = mimeType;
        // check for MIME type
        if (!StringUtils.hasText(detectMimeType)) {
            detectMimeType = this.getMimeTypeDetector().getMimeType(input);
            mimeType = detectMimeType;
        }
        // if valid MIME type but not registered it with any thumbnailer
        if (StringUtils.hasText(detectMimeType) && !this.getThumbnailers().containsKey(detectMimeType)) {
            detectMimeType = ALL_MIME_WILDCARD;
        }
        // if valid MIME type and valid thumbnailer for it
        if (StringUtils.hasText(detectMimeType) && this.getThumbnailers().containsKey(detectMimeType)) {
            for(Thumbnailer thumbnailer : this.getThumbnailers().get(detectMimeType)) {
                boolean error = false;
                try {
                    thumbnailer.generateThumbnail(input, output, detectMimeType);
                } catch (Exception e) {
                    LogUtils.logWarn(this.getClass(), e.getMessage());
                    error = true;
                } finally {
                    ok = (!error && output != null && output.exists());
                }
                // correct thumbnailer
                if (ok) break;
            }
        }
        // throw exception if failed
        if (!ok) {
            mimeType = (!StringUtils.hasText(mimeType) ? "NULL" : mimeType);
            throw new ThumbnailerException(
                    "Not found any thumbnailer for MIME {" + mimeType + "}");
        }
    }

	/* (Non-Javadoc)
	 * @see org.nlh4j.thumbnailer.Thumbnailer#setImageSize(int, int, int)
	 */
    @Override
    public void setImageSize(int thumbWidth, int thumbHeight, int imageResizeOptions) {
        Assert.isTrue(thumbHeight > 0, "Height must be greater than 0!");
        Assert.isTrue(thumbWidth > 0, "Width must be greater than 0!");
        this.setThumbHeight(thumbHeight);
        this.setThumbWidth(thumbWidth);
        this.setImageResizeOptions(imageResizeOptions);
        for(Thumbnailer thumbnailer : this.getAllThumbnailers()) {
            thumbnailer.setImageSize(thumbWidth, thumbHeight, imageResizeOptions);
        }
    }

	/* (Non-Javadoc)
	 * @see org.nlh4j.thumbnailer.Thumbnailer#getAcceptedMIMETypes()
	 */
	@Override
	public String[] getAcceptedMIMETypes() {
		if (CollectionUtils.isEmpty(this.getThumbnailers())
		        || this.getThumbnailers().containsKey(ALL_MIME_WILDCARD)) {
			return new String[] {}; // All MIME Types
		} else {
			return this.getThumbnailers().keySet().toArray(new String[] {});
		}
	}
}
