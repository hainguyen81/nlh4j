/*
 * @(#)NativeImageThumbnailer.java 1.0 May 29, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL).
 * All rights reserved.
 */
package org.nlh4j.thumbnailer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.nlh4j.exceptions.ThumbnailerException;
import org.nlh4j.util.ImageUtils;

/**
 * {@link BufferedImage} Thumbnailer
 *
 * @author Hai Nguyen
 *
 */
public final class NativeImageThumbnailer extends AbstractThumbnailer {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /* (Non-Javadoc)
     * @see org.nlh4j.thumbnailer.AbstractThumbnailer#performGenerate(java.io.File, java.io.File)
     */
    @Override
    protected void performGenerate(File input, File output) throws IOException, ThumbnailerException {
        // parse source image format
        ImageInputStream iis = ImageIO.createImageInputStream(input);
        String srcImgFmt = ImageUtils.getImageFormat(iis);
        iis.close();
        // read image from source file
        BufferedImage srcImg = ImageIO.read(input);
        // resize source image
        BufferedImage destImg = ImageUtils.resize(srcImg,
                super.getCurrentImageWidth(), super.getCurrentImageHeight());
        // save to output file
        ImageIO.write(destImg, srcImgFmt, output);
    }

    /*
     * (Non-Javadoc)
     * @see org.nlh4j.thumbnailer.AbstractThumbnailer#getAcceptedMIMETypes()
     */
    @Override
    public String[] getAcceptedMIMETypes() {
        return ImageIO.getReaderMIMETypes();
    }
}
