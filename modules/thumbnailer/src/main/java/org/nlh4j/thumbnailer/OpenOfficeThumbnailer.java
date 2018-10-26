/*
 * @(#)OpenOfficeThumbnailer.java 1.0 May 29, 2015
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

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import org.nlh4j.exceptions.ThumbnailerException;
import org.nlh4j.util.ImageUtils;
import org.nlh4j.util.StreamUtils;

/**
 * This class extracts Thumbnails from OpenOffice-Files.
 *
 * Depends:
 * <li> <i>NOT</i> on OpenOffice, as the Thumbnail is already inside the file. (184x256px regardless of page orientation)
 * 		(So if the thumbnail generation is not correct, it's OpenOffice's fault, not our's :-)
 *
 */
public final class OpenOfficeThumbnailer extends AbstractThumbnailer {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;
    /** internal OpenOffice thumbnail file */
    private static final String INTERNAL_OPEN_OFFICE_THUMBNAIL = "Thumbnails/thumbnail.png";

    /* (Non-Javadoc)
     * @see org.nlh4j.thumbnailer.AbstractThumbnailer#performGenerate(java.io.File, java.io.File)
     */
    @Override
    protected void performGenerate(File input, File output) throws IOException, ThumbnailerException {
        BufferedInputStream bis = null;
        ZipFile zipFile = null;
        ZipEntry entry = null;

        // open compress source file
        try {
             zipFile = new ZipFile(input);
        } catch (ZipException e) {
            throw new ThumbnailerException("This is not a zipped file. Is this really an OpenOffice-File?", e);
        }

        // extract source compress file for internal thumbnail
        try {
            entry = zipFile.getEntry(INTERNAL_OPEN_OFFICE_THUMBNAIL);
            if (entry == null) {
                throw new ThumbnailerException(
                        "Zip file does not contain '" + INTERNAL_OPEN_OFFICE_THUMBNAIL + "'. "
                                + "Is this really an OpenOffice-File?");
            }

            // read internal thumbnail
            bis = new BufferedInputStream(zipFile.getInputStream(entry));
            String imgFmt = ImageUtils.getImageFormat(bis);
            BufferedImage srcImg = ImageIO.read(bis);
            BufferedImage dstImg = ImageUtils.resize(srcImg, this.getCurrentImageWidth(), this.getCurrentImageHeight());
            ImageIO.write(dstImg, imgFmt, output);
        } catch (Exception e) {
            throw new ThumbnailerException(e.getMessage(), e);
        } finally {
            StreamUtils.closeQuitely(bis);
            StreamUtils.closeQuitely(zipFile);
        }
    }

    /* (Non-Javadoc)
     * @see org.nlh4j.thumbnailer.AbstractThumbnailer#getAcceptedMIMETypes()
     */
    @Override
    public String[] getAcceptedMIMETypes() {
        return new String[] {
                "application/vnd.sun.xml.writer",
                "application/vnd.sun.xml.writer.template",
                "application/vnd.sun.xml.writer.global",
                "application/vnd.sun.xml.calc",
                "application/vnd.sun.xml.calc.template",
                "application/vnd.stardivision.calc",
                "application/vnd.sun.xml.impress",
                "application/vnd.sun.xml.impress.template ",
                "application/vnd.stardivision.impress sdd",
                "application/vnd.sun.xml.draw",
                "application/vnd.sun.xml.draw.template",
                "application/vnd.stardivision.draw",
                "application/vnd.sun.xml.math",
                "application/vnd.stardivision.math",
                "application/vnd.oasis.opendocument.text",
                "application/vnd.oasis.opendocument.text-template",
                "application/vnd.oasis.opendocument.text-web",
                "application/vnd.oasis.opendocument.text-master",
                "application/vnd.oasis.opendocument.graphics",
                "application/vnd.oasis.opendocument.graphics-template",
                "application/vnd.oasis.opendocument.presentation",
                "application/vnd.oasis.opendocument.presentation-template",
                "application/vnd.oasis.opendocument.spreadsheet",
                "application/vnd.oasis.opendocument.spreadsheet-template",
                "application/vnd.oasis.opendocument.chart",
                "application/vnd.oasis.opendocument.formula",
                "application/vnd.oasis.opendocument.database",
                "application/vnd.oasis.opendocument.image",
                "application/zip" /* Could be an OpenOffice file! */
        };
    }
}
