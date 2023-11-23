/*
 * @(#)BarcodeUtils.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.io.StringReader;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.krysalis.barcode4j.BarcodeGenerator;
import org.krysalis.barcode4j.BarcodeUtil;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.springframework.util.Assert;
import org.xml.sax.InputSource;

/**
 * Barcode utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class BarcodeUtils implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * Generate barcode to output image file
     *
     * @param configuration XML barcode configuration string or file path
     * @param data barcode data
     * @param outputFile the generated barcode output file path
     * @param resolution the desired image resolution (dots per inch)
     * @param imageType the desired image type (Values: BufferedImage.TYPE_*)
     *
     * @return true for successfully; else false
     */
    public static boolean generate(
            String configuration, String data, String outputFile, int resolution, int imageType) {
        return generate(configuration, data, outputFile, "image/png", resolution, imageType, Boolean.FALSE, 0);
    }
    /**
     * Generate barcode to output image file
     *
     * @param configuration XML barcode configuration string or file path
     * @param data barcode data
     * @param outputFile the generated barcode output file path
     * @param mime MIME type of the desired output format (ex. "image/png") (may be NULL)
     * @param resolution the desired image resolution (dots per inch)
     * @param imageType the desired image type (Values: BufferedImage.TYPE_*)
     *
     * @return true for successfully; else false
     */
    public static boolean generate(
            String configuration, String data, String outputFile,
            String mime, int resolution, int imageType) {
        return generate(configuration, data, outputFile, mime, resolution, imageType, Boolean.FALSE, 0);
    }
    /**
     * Generate barcode to output image file
     *
     * @param configuration XML barcode configuration string or file path
     * @param data barcode data
     * @param outputFile the generated barcode output file path
     * @param mime MIME type of the desired output format (ex. "image/png") (may be NULL)
     * @param resolution the desired image resolution (dots per inch)
     * @param imageType the desired image type (Values: BufferedImage.TYPE_*)
     * @param antiAlias true if anti-aliasing should be enabled
     * @param orientation orientation
     *
     * @return true for successfully; else false
     */
    public static boolean generate(
            String configuration, String data, String outputFile,
            String mime, int resolution, int imageType,
            boolean antiAlias, int orientation) {
        // check parameters
        Assert.hasText(configuration, "barcode-configuration");
        Assert.hasText(data, "barcode-data");
        Assert.hasText(outputFile, "barcode-output-file");

        // create barcode configuration
        boolean ok = false;
        Configuration cfg = null;
        DefaultConfigurationBuilder cfgBuilder = null;
        try {
            cfgBuilder = new DefaultConfigurationBuilder();
            File cfgFile = new File(configuration);
            if (cfgFile.exists()) {
                cfg = cfgBuilder.buildFromFile(cfgFile);
            } else {
                cfg = cfgBuilder.build(new InputSource(new StringReader(configuration)));
            }
        } catch (Exception e) {
            LogUtils.logError(BarcodeUtils.class, e.getMessage());
            cfg = null;
        } finally { ok = (cfg != null); }

        // create barcode generator
        BarcodeGenerator generator = null;
        if (ok) {
            BarcodeUtil util = null;
            try {
                util = BarcodeUtil.getInstance();
                generator = util.createBarcodeGenerator(cfg);
            } catch (Exception e) {
                LogUtils.logError(BarcodeUtils.class, e.getMessage());
                generator = null;
            } finally { ok = (generator != null); }
        }

        // generate barcode
        if (ok) {
            FileOutputStream fos = null;
            BitmapCanvasProvider canvas = null;
            try {
                fos = new FileOutputStream(outputFile);
                canvas = new BitmapCanvasProvider(
                        fos, mime, resolution, imageType,
                        antiAlias, orientation);
                generator.generateBarcode(canvas, data);
                ok = true;
            } catch (Exception e) {
                LogUtils.logError(BarcodeUtils.class, e.getMessage());
                ok = false;
            } finally {
                if (canvas != null) {
                    try { canvas.finish(); }
                    catch (Exception e) {}
                }
                StreamUtils.closeQuitely(fos);
            }
        }
        return ok;
    }

    /**
     * Generate barcode to image object
     *
     * @param configuration XML barcode configuration string or file path
     * @param data barcode data
     * @param resolution the desired image resolution (dots per inch)
     * @param imageType the desired image type (Values: BufferedImage.TYPE_*)
     *
     * @return the generated barcode output image object
     */
    public static BufferedImage generate(String configuration, String data, int resolution, int imageType) {
        return generate(configuration, data, resolution, imageType, Boolean.FALSE, 0);
    }
    /**
     * Generate barcode to image object
     *
     * @param configuration XML barcode configuration string or file path
     * @param data barcode data
     * @param resolution the desired image resolution (dots per inch)
     * @param imageType the desired image type (Values: BufferedImage.TYPE_*)
     * @param antiAlias true if anti-aliasing should be enabled
     * @param orientation orientation
     *
     * @return true for successfully; else false
     */
    public static BufferedImage generate(
            String configuration, String data, int resolution, int imageType,
            boolean antiAlias, int orientation) {
        // check parameters
        Assert.hasText(configuration, "barcode-configuration");
        Assert.hasText(data, "barcode-data");

        // create barcode configuration
        boolean ok = false;
        BufferedImage outputImage = null;
        Configuration cfg = null;
        DefaultConfigurationBuilder cfgBuilder = null;
        try {
            cfgBuilder = new DefaultConfigurationBuilder();
            File cfgFile = new File(configuration);
            if (cfgFile.exists()) {
                cfg = cfgBuilder.buildFromFile(cfgFile);
            } else {
                cfg = cfgBuilder.build(new InputSource(new StringReader(configuration)));
            }
        } catch (Exception e) {
            LogUtils.logError(BarcodeUtils.class, e.getMessage());
            cfg = null;
        } finally { ok = (cfg != null); }

        // create barcode generator
        BarcodeGenerator generator = null;
        if (ok) {
            BarcodeUtil util = null;
            try {
                util = BarcodeUtil.getInstance();
                generator = util.createBarcodeGenerator(cfg);
            } catch (Exception e) {
                LogUtils.logError(BarcodeUtils.class, e.getMessage());
                generator = null;
            } finally { ok = (generator != null); }
        }

        // generate barcode
        if (ok) {
            BitmapCanvasProvider canvas = null;
            try {
                canvas = new BitmapCanvasProvider(
                        null, null, resolution, imageType,
                        antiAlias, orientation);
                generator.generateBarcode(canvas, data);
                outputImage = canvas.getBufferedImage();
                ok = true;
            } catch (Exception e) {
                LogUtils.logError(BarcodeUtils.class, e.getMessage());
                outputImage = null;
            } finally {
                if (canvas != null) {
                    try { canvas.finish(); }
                    catch (Exception e) {}
                }
            }
        }
        return (ok ? outputImage : null);
    }
}
