/*
 * @(#)TestBeanUtils.java 1.0 Oct 29, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import org.junit.Test;

/**
 * Test class {@link BarcodeUtils}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class TestBarcodeUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Test {@link BarcodeUtils#generate(String, String, String, String, int, int, boolean, int)}
     */
    @Test
    public void testGenerateIntl25OutputFile() {
        String configuration = "<barcode><intl2of5>"
                + "<human-readable>"
                + "<placement>bottom</placement>"
                + "</human-readable>"
                + "</intl2of5></barcode>";
        String data = "0123456789";
        String outputFile = "D:/intl2of5.jpg";
        String mime = "image/jpeg";
        int resolution = 200;
        int imageType = BufferedImage.TYPE_BYTE_BINARY;
        try {
            BarcodeUtils.generate(configuration, data, outputFile, mime, resolution, imageType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test {@link BarcodeUtils#generate(String, String, String, String, int, int, boolean, int)}
     */
    @Test
    public void testGenerateCode128OutputFile() {
        String configuration = "<barcode><code128>"
                + "<human-readable>"
                + "<placement>bottom</placement>"
                + "</human-readable>"
                + "</code128></barcode>";
        String data = "E002559";
        String outputFile = "D:/code128.png";
        String mime = "image/png";
        int resolution = 200;
        int imageType = BufferedImage.TYPE_BYTE_BINARY;
        try {
            BarcodeUtils.generate(configuration, data, outputFile, mime, resolution, imageType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
