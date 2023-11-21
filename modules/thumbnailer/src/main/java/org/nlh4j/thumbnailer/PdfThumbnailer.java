/*
 * @(#)PdfThumbnailer.java
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.PageDrawer;
import org.apache.pdfbox.rendering.PageDrawerParameters;
import org.nlh4j.exceptions.ThumbnailerException;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.FileUtils;
import org.nlh4j.util.ImageUtils;
import org.nlh4j.util.LogUtils;
import org.nlh4j.util.StreamUtils;
import org.springframework.util.Assert;

/**
 * Renders the first page of a PDF file into a thumbnail.
 *
 * Performance note: This takes about 2-3 seconds per file.
 * (TODO : Try to override PDPage.convertToImage - this is where the heavy lifting takes place)
 *
 * Depends on:
 * <ul>
 * <li>PDFBox (&gt;= 1.5.0)</li>
 * </ul>
 */
public class PdfThumbnailer extends AbstractThumbnailer {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
	private static final Color TRANSPARENT_WHITE = new Color(255, 255, 255, 0);

	/* (Non-Javadoc)
	 * @see org.nlh4j.thumbnailer.AbstractThumbnailer#performGenerate(java.io.File, java.io.File)
	 */
	@Override
	protected void performGenerate(File input, File output) throws IOException, ThumbnailerException {
	    // check source file
	    Assert.isTrue(input != null && input.exists() && input.canRead(), "sourceFile");

	    // delete output file
        FileUtils.safeDelete(output);

        PDDocument document = null;
        try {
            // load source file
            document = PDDocument.load(input);
            // generate thumbnail of first page
            BufferedImage tmpImage = this.writeImageFirstPage(document, BufferedImage.TYPE_INT_RGB);
            if (tmpImage != null && tmpImage.getWidth() == getThumbWidth()) {
                ImageIO.write(tmpImage, "PNG", output);
            } else if (tmpImage != null) {
                ImageUtils.resize(tmpImage, getThumbWidth(), getThumbHeight());
            }
        } finally {
            StreamUtils.closeQuitely(document);
        }
	}

	/**
	 * Loosely based on the commandline-Tool PDFImageWriter
	 * @param document to write image
	 * @param imageType image type
	 * @return {@link BufferedImage}
	 * @throws IOException thrown if failed
	 */
    private BufferedImage writeImageFirstPage(PDDocument document, int imageType) throws IOException {
        PDPageTree pages = (document == null || document.getDocumentCatalog() == null
                ? null : document.getDocumentCatalog().getPages());
    	PDPage page = (pages != null && pages.getCount() > 0 ? pages.get(0) : null);
    	Assert.notNull(page, "Empty PDF document!");
    	return this.convertToImage(document, page, imageType, getThumbWidth(), getThumbHeight());
    }

    /**
     * Convert the specified {@link PDPage} to {@link BufferedImage}
     * @param document to write image
     * @param page to convert
     * @param imageType converted image type
     * @param thumbWidth image thumbnail width
     * @param thumbHeight image thumbnail height
     * @return {@link BufferedImage}
     * @throws IOException thrown if failed
     */
    private BufferedImage convertToImage(
            PDDocument document, PDPage page, int imageType,
            int thumbWidth, int thumbHeight) throws IOException {
        PDRectangle mBox = page.getMediaBox();
        float widthPt = mBox.getWidth();
        // Math.round(widthPt * scaling)
        int widthPx = thumbWidth;
        // Math.round(heightPt * scaling)
        int heightPx = thumbHeight;
        // resolution / 72.0F
        float scaling = thumbWidth / widthPt;

        // draw image to PDF
        BufferedImage retval = new BufferedImage(widthPx, heightPx, imageType);
        Graphics2D graphics = BeanUtils.safeType(retval.getGraphics(), Graphics2D.class);
        graphics.setBackground(TRANSPARENT_WHITE);
        graphics.clearRect(0, 0, retval.getWidth(), retval.getHeight());
        graphics.scale(scaling, scaling);
        PDFRenderer renderer = new PDFRenderer(document);
        PageDrawerParameters drawerParameters = BeanUtils.safeNewInstance(
                PageDrawerParameters.class, renderer, page, true);
        Assert.notNull(drawerParameters, "Could not create parameter(s) to draw!");
        PageDrawer drawer = new PageDrawer(drawerParameters);
        drawer.drawPage(graphics, mBox);
        try {
            int rotation = page.getRotation();
            if ((rotation == 90) || (rotation == 270)) {
                int w = retval.getWidth();
                int h = retval.getHeight();
                BufferedImage rotatedImg = new BufferedImage(w, h, retval.getType());
                Graphics2D g = rotatedImg.createGraphics();
                g.rotate(Math.toRadians(rotation), w / 2, h / 2);
                g.drawImage(retval, null, 0, 0);
            }
        } catch (ImagingOpException e) {
            LogUtils.logError(this.getClass(), e.getMessage(), e);
            retval = null;
        }
        return retval;
    }

    /**
     * Get a List of accepted File Types.
     * Only PDF Files are accepted.
     *
     * @return MIME-Types
     */
    @Override
	public String[] getAcceptedMIMETypes() {
		return new String[] { "application/pdf" };
	}
}
