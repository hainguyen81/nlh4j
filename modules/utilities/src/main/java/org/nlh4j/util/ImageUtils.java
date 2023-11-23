/*
 * @(#)ImageUtils.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.IndexColorModel;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.util.StringUtils;

/**
 * The image utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public final class ImageUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

	/**
	 * Creates an input stream from the specified image and format
	 *
	 * @param image The source image to read
	 * @param formatName The source image format
	 * @return The input stream to read source image
	 */
	public static InputStream createStreamSource(final BufferedImage image, final String formatName) {
		if (image == null) return null;
		byte[] imageData = toByteArray(image, formatName);
		if (CollectionUtils.isEmpty(imageData)) return null;
		return new ByteArrayInputStream(imageData);
    }
    /**
     * Draws the specified image with round corners
     *
     * @param image The source image to draw
     * @param cornerRadius The corners' radius
     *
     * @return The image that was made round corners
     */
    public static BufferedImage roundCorners(BufferedImage image, int cornerRadius) {
    	BufferedImage roundedImage = null;
    	if (image != null) {
	        roundedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g = roundedImage.createGraphics();
	        applyDefaultRenderingHints(g);
	        g.setColor(Color.WHITE);
	        g.fillRoundRect(0, 0, image.getWidth(), image.getHeight(), (cornerRadius * 2), (cornerRadius * 2));
	        g.setComposite(AlphaComposite.SrcIn);
	        g.drawImage(image, 0, 0, null);
	        g.dispose();
    	}
        return roundedImage;
    }
    /**
     * Makes a shadow effect for the specified image
     *
     * @param image The source image
     * @param direction The direction of the shadow
     * @param distance The witness of the shadow
     *
     * @return The image that was made shadow effect
     */
    public static BufferedImage shadow(BufferedImage image, int direction, int distance) {
    	BufferedImage shadowedImage = null;
    	if (image != null) {
	        int x = distance;
	        int y = distance;
	        int w = (image.getWidth() + (distance * 2));
	        int h = (image.getHeight() + (distance * 2));

	        int ix = (((direction < 337.5) && (direction > 202.5)) ? 0 : distance);
	        ix += (((direction > 22.5) && (direction < 157.5)) ? distance : 0);
	        ix = ((distance * 2) - ix);

	        int iy = (((direction > 67.5) && (direction < 337.5)) ? distance : 0);
	        iy += (((direction > 112.5) && (direction < 247.5)) ? distance : 0);
	        iy = ((distance * 2) - iy);

	        // Create mask
	        BufferedImage mask = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g = mask.createGraphics();
	        applyDefaultRenderingHints(g);
	        g.drawImage(image, x, y, null);
	        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 0.6f));
	        g.setColor(Color.BLACK);
	        g.fillRect(0, 0, mask.getWidth(), mask.getHeight());
	        g.dispose();

	        // Create shadow
	        BufferedImage shadow = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	        shadow = getLinearBlurOp(distance).filter(mask, shadow);

	        // Compose
	        shadowedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	        g = shadowedImage.createGraphics();
	        applyDefaultRenderingHints(g);
	        g.drawImage(shadow, 0, 0, null);
	        g.drawImage(image, ix, iy, null);
	        g.dispose();
    	}
        return shadowedImage;
    }
    /**
     * Creates convolve blur operation
     *
     * @param radius The radius
     * @param horizontal Specifies the effect whether follows horizontal or vertical
     *
     * @return The convolve blur operation
     */
    public static ConvolveOp getGaussianBlurOp(int radius, boolean horizontal) {
        if (radius < 1) {
            throw new IllegalArgumentException("Radius must be >= 1");
        }

        int size = ((radius * 2) + 1);
        float[] data = new float[size];

        float sigma = (radius / 3.0f);
        float twoSigmaSquare = (2.0f * sigma * sigma);
        float sigmaRoot = (float) Math.sqrt(twoSigmaSquare * Math.PI);
        float total = 0.0f;

        for (int i = -radius; i <= radius; i++) {
            float distance = (i * i);
            int index = (i + radius);
            data[index] = ((float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot);
            total += data[index];
        }

        for (int i = 0; i < data.length; i++) {
            data[i] /= total;
        }

        Kernel kernel = null;
        if (horizontal) {
            kernel = new Kernel(size, 1, data);
        } else {
            kernel = new Kernel(1, size, data);
        }
        return new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
    }
    /**
     * Creates convolve linear blur operation
     *
     * @param size The size of line
     *
     * @return The convolve linear blur operation
     */
    public static ConvolveOp getLinearBlurOp(int size) {
        float[] data = new float[size * size];
        float value = (1.0f / (size * size));
        for (int i = 0; i < data.length; i++) {
            data[i] = value;
        }
        return new ConvolveOp(new Kernel(size, size, data));
    }
    /**
     * Rotates the source image with the specified degrees
     *
     * @param image The source image
     * @param degrees The degrees to rotate image
     *
     * @return The image after rotating
     */
    public static BufferedImage rotate(BufferedImage image, int degrees) {
    	if (image == null) return null;

        int w = image.getWidth();
        int h = image.getHeight();

        // Enough space for the image
        int nd = ((int) Math.ceil(Math.sqrt((w * w) + (h * h))) + 2);

        BufferedImage t = new BufferedImage(nd, nd, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = t.createGraphics();
        applyDefaultRenderingHints(g);
        g.rotate(Math.toRadians(degrees), (nd / 2), (nd / 2));
        g.translate(((nd / 2) - (w / 2)), ((nd / 2) - (h / 2)));

        g.drawImage(image, 0, 0, null);
        g.dispose();

        // Crop the invisible parts
        return cropTransparent(t);
    }
    /**
     * Crops transparent the source image
     *
     * @param image	The source image
     *
     * @return The image after cropping
     */
    public static BufferedImage cropTransparent(BufferedImage image) {
    	if (image == null) return null;

        // Find the bounding box
        WritableRaster r = image.getRaster();
        int minx = -1;
        int miny = -1;
        int maxx = r.getWidth();
        int maxy = r.getHeight();
        double[] pv = new double[4];
        int x0 = 0;
        int x1 = r.getWidth();
        int y0 = 0;
        int y1 = r.getHeight();

        // min y
        boolean contentFound = false;
        for (int y = y0; y < y1 && !contentFound; y++) {
            for (int x = x0; x < x1; x++) {
                r.getPixel(x, y, pv);
                if (pv[3] != 0) {
                    contentFound = true;
                    miny = y;
                    break;
                }
            }
        }

        // max y
        contentFound = false;
        for (int y = y1 - 1; y > 0 && !contentFound; y--) {
            for (int x = x0; x < x1; x++) {
                r.getPixel(x, y, pv);
                if (pv[3] != 0) {
                    contentFound = true;
                    maxy = (y + 1);
                    break;
                }
            }
        }

        // min x
        contentFound = false;
        for (int x = x0; x < x1 && !contentFound; x++) {
            for (int y = y0; y < y1; y++) {
                r.getPixel(x, y, pv);
                if (pv[3] != 0) {
                    contentFound = true;
                    minx = x;
                    break;
                }
            }
        }

        // max x
        contentFound = false;
        for (int x = x1 - 1; x > x0 && !contentFound; x--) {
            for (int y = y0; y < y1; y++) {
                r.getPixel(x, y, pv);
                if (pv[3] != 0) {
                    contentFound = true;
                    maxx = x + 1;
                    break;
                }
            }
        }

        minx = ((minx < x0) ? x0 : minx);
        miny = ((miny < y0) ? y0 : miny);
        maxx = ((maxx > x1) ? x1 : maxx);
        maxy = ((maxy > y1) ? y1 : maxy);

        int nw = (maxx - minx);
        int nh = (maxy - miny);
        return crop(image, minx, miny, nw, nh);
    }
    /**
     * Crops the source image by the specified rectangle
     *
     * @param image The source image
     * @param x The left position
     * @param y The top position
     * @param w The width of clip
     * @param h The height of clip
     *
     * @return The image after cropping
     */
    public static BufferedImage crop(BufferedImage image, int x, int y, int w, int h) {
    	if (image == null) return null;

        int nw = ((w < image.getWidth()) ? w : image.getWidth());
        int nh = ((h < image.getHeight()) ? h : image.getHeight());
        BufferedImage croppedImage = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = croppedImage.createGraphics();
        applyDefaultRenderingHints(g);
        int x2 = (x + w);
        x2 = ((x2 > image.getWidth()) ? image.getWidth() : x2);
        int y2 = (y + h);
        y2 = ((y2 > image.getHeight()) ? image.getHeight() : y2);
        g.drawImage(image, 0, 0, nw, nh, x, y, x2, y2, null);
        g.dispose();

        return croppedImage;
    }
    /**
     * Scales the source image with the specified size
     *
     * @param image The source image
     * @param w The scale width
     * @param h The scale height
     *
     * @return The image after scaling
     */
    public static BufferedImage scale(BufferedImage image, int w, int h) {
    	BufferedImage scaledImage = null;
    	if (image != null) {
	    	scaledImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g = scaledImage.createGraphics();
	        applyDefaultRenderingHints(g);
	        g.drawImage(image, 0, 0, w, h, 0, 0, image.getWidth(), image.getHeight(), null);
	        g.dispose();
    	}
        return scaledImage;
    }
    /**
     * Flips the source image by horizontal
     *
     * @param image The source image
     *
     * @return The image after flip
     */
    public static BufferedImage flipHorizontal(BufferedImage image) {
    	BufferedImage flippedImage = null;
    	if (image != null) {
	        flippedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
	        tx.translate(-image.getWidth(null), 0);
	        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	        flippedImage = op.filter(image, flippedImage);
    	}
        return flippedImage;
    }
    /**
     * Flips the source image by vertical
     *
     * @param image The source image
     *
     * @return The image after flip
     */
    public static BufferedImage flipVertical(BufferedImage image) {
    	BufferedImage flippedImage = null;
    	if (image != null) {
	        flippedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
	        tx.translate(0, -image.getHeight(null));
	        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
	        flippedImage = op.filter(image, flippedImage);
    	}
        return flippedImage;
    }
    /**
     * Fits the source image by the specified size
     *
     * @param image The source image
     * @param w The clip width
     * @param h The clip height
     * @param cropToFit	Specifies the source image whether will be cropped to fit
     *
     * @return The image after fitting
     */
    public static BufferedImage fit(BufferedImage image, int w, int h, boolean cropToFit) {
    	BufferedImage fittedImage = null;
    	if (image != null) {
	        // Calculate scale
	        double x_scale = ((double) w / (double) image.getWidth());
	        double y_scale = ((double) h / (double) image.getHeight());
	        double scale = ((x_scale < y_scale) ? x_scale : y_scale);
	        int nw = (int) (image.getWidth() * scale);
	        int nh = (int) (image.getHeight() * scale);

	        // Center if not crop
	        int nx = (cropToFit ? 0 : ((w - nw) / 2));
	        int ny = (cropToFit ? 0 : ((h - nh) / 2));

	        fittedImage = new BufferedImage((cropToFit ? nw : w), (cropToFit ? nh : h), BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g = fittedImage.createGraphics();
	        applyDefaultRenderingHints(g);
	        g.drawImage(image, nx, ny, (nx + nw), (ny + nh), 0, 0, image.getWidth(), image.getHeight(), null);
	        g.dispose();
    	}
        return fittedImage;
    }
    /**
     * Makes the source image brightness and contrast.
     *
     * @param image image to apply
     * @param brightness brightness
     * @param contrast image contrast
     *
     * @return the applied image
     */
    public static BufferedImage brignessAndContrast(BufferedImage image, float brightness, float contrast) {
    	BufferedImage fixedImage = null;
    	if (image != null) {
	        RescaleOp op = new RescaleOp(new float[] { 0.9f, 0.9f, 0.9f },
	                new float[] { 0f }, null);
	        fixedImage = new BufferedImage(
	        		image.getWidth(), image.getHeight(),
	        		BufferedImage.TYPE_INT_ARGB);
	        op.filter(image, fixedImage);
    	}
        return fixedImage;
    }
    /**
     * Create new buffered image.
     *
     * @param w the image width
     * @param h the image height
     *
     * @return The new buffered image
     */
    public static BufferedImage createBufferedImage(int w, int h) {
        return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    }
    /**
     * Create new buffered image.
     *
     * @param w image width
     * @param h image height
     * @param it type of the created image
     *
     * @return image
     */
    public static BufferedImage createBufferedImage(int w, int h, int it) {
        return new BufferedImage(w, h, it);
    }
    /**
     * Create new buffered image.
     *
     * @param cm color model
     * @param raster writable raster
     * @param isRasterPremultiplied specify raster whether pre-multiplied
     * @param properties Hashtable of String/Object pairs.
     *
     * @return image
     */
    public static BufferedImage createBufferedImage(
    		ColorModel cm, WritableRaster raster,
    		boolean isRasterPremultiplied,
    		Hashtable<?,?> properties) {
        return new BufferedImage(cm, raster, isRasterPremultiplied, properties);
    }
    /**
     * Create new buffered image.
     *
     * @param w image width
     * @param h image height
     * @param it image type
     * @param cm color model
     *
     * @return image
     */
    public static BufferedImage createBufferedImage(int w, int h, int it, IndexColorModel cm) {
        return new BufferedImage(w, h, it, cm);
    }
    /**
     * Applies default rendering hints for the specified graphics object
     *
     * @param g The graphics object to apply
     */
    public static void applyDefaultRenderingHints(Graphics2D g) {
    	if (g != null) {
	        g.setRenderingHint(
	        		RenderingHints.KEY_ANTIALIASING,
	        		RenderingHints.VALUE_ANTIALIAS_ON);
	        g.setRenderingHint(
	        		RenderingHints.KEY_INTERPOLATION,
	        		RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	        g.setRenderingHint(
	        		RenderingHints.KEY_ALPHA_INTERPOLATION,
	        		RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    	}
    }
    /**
     * Converts a CMYK image to RGB image
     *
     * @param image the {@link BufferedImage} for converting
     *
     * @return the CMYK image
     * @throws IOException thrown if fail
     */
    public static BufferedImage convertCMYK2RGB(BufferedImage image) throws IOException {
    	BufferedImage rgbImage = null;
    	if (image != null) {
	    	int colorSpaceType = image.getColorModel().getColorSpace().getType();
	        if (colorSpaceType == ColorSpace.TYPE_CMYK) {
		        //Create a new RGB image
		        rgbImage = new BufferedImage(
		        		image.getWidth(), image.getHeight(),
		        		BufferedImage.TYPE_4BYTE_ABGR);
		        // then do a funky color convert
		        ColorConvertOp op = new ColorConvertOp(null);
		        op.filter(image, rgbImage);
	        }
    	}
        return rgbImage;
    }
    /**
     * Makes the source image translucently
     *
     * @param image the source image
     * @param transperancy the transperancy degree
     *
     * @return the translucented image
     */
    public static BufferedImage createTranslucentImage(BufferedImage image, float transperancy) {
    	BufferedImage translucentedImage = null;
    	if (image != null) {
			// Create the image using the
			translucentedImage = new BufferedImage(
					image.getWidth(), image.getHeight(),
					BufferedImage.TRANSLUCENT);
			// Get the images graphics
			Graphics2D g = translucentedImage.createGraphics();
			// Set the Graphics composite to Alpha
			g.setComposite(
					AlphaComposite.getInstance(
							AlphaComposite.SRC_OVER, transperancy));
			// Draw the LOADED image into the prepared receiver image
			g.drawImage(image, null, 0, 0);
			// let go of all system resources in this Graphics
			g.dispose();
    	}
		// Return the image
		return translucentedImage;
	}
    /**
     * Makes the specified color of the source image transparency
     *
     * @param image the source image
     * @param color the color to make transparent
     *
     * @return the transparency image
     */
    public static BufferedImage makeColorTransparent(BufferedImage image, Color color) {
    	BufferedImage transparentedImage = null;
    	if (image != null) {
			transparentedImage = new BufferedImage(
					image.getWidth(), image.getHeight(),
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = transparentedImage.createGraphics();
			g.setComposite(AlphaComposite.Src);
			g.drawImage(image, null, 0, 0);
			g.dispose();
			for(int i = 0; i < transparentedImage.getHeight(); i++) {
				for(int j = 0; j < transparentedImage.getWidth(); j++) {
					if(transparentedImage.getRGB(j, i) == color.getRGB()) {
					    transparentedImage.setRGB(j, i, 0x8F1C1C);
					}
				}
			}
    	}
		return transparentedImage;
	}
    /**
     * Resizes the source image to the specified size
     *
     * @param image the source image
     * @param newW the new height
     * @param newH the new width
     *
     * @return the resized image
     */
    public static BufferedImage resize(BufferedImage image, int newW, int newH) {
    	BufferedImage resizedImage = null;
    	if (image != null) {
			int w = image.getWidth();
			int h = image.getHeight();
			resizedImage = new BufferedImage(newW, newH, image.getType());
			Graphics2D g = resizedImage.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(image, 0, 0, newW, newH, 0, 0, w, h, null);
			g.dispose();
    	}
		return resizedImage;
	}
    /**
     * Splits an image to an images array
     *
     * @param image the source image to split
     * @param cols the number of columns
     * @param rows the number of rows
     *
     * @return an images array after splitting
     */
    public static BufferedImage[] split(BufferedImage image, int cols, int rows) {
    	BufferedImage[] splittedImages = null;
    	if (image != null) {
			int w = (image.getWidth() / cols);
			int h = (image.getHeight() / rows);
			int num = 0;
			splittedImages = new BufferedImage[w * h];
			for(int y = 0; y < rows; y++) {
				for(int x = 0; x < cols; x++) {
					splittedImages[num] = new BufferedImage(w, h, image.getType());
					// Tell the graphics to draw only one block of the image
					Graphics2D g = splittedImages[num].createGraphics();
					g.drawImage(image, 0, 0, w, h, w*x, h*y, w*x+w, h*y+h, null);
					g.dispose();
					num++;
				}
			}
    	}
		return splittedImages;
	}

    /**
     * Parses the image's format from the specified {@link ImageInputStream}
     *
     * @param iis The {@link ImageInputStream} of image
     *
     * @return The image's format or null
     */
    public static String getImageFormat(ImageInputStream iis) {
    	if (iis != null) {
	    	for(final Iterator<ImageReader> it = ImageIO.getImageReaders(iis); it.hasNext();) {
	    		ImageReader ir = it.next();
	    		if (ir != null) {
	    			try {
						return ir.getFormatName();
					}
	    			catch (IOException e) {}
	    		}
	    	}
    	}
    	return null;
    }
    /**
     * Parse the image format
     *
     * @param is The {@link InputStream} of image
     *
     * @return The image's format or null
     */
    public static String getImageFormat(InputStream is) {
        try { return (is == null ? null : getImageFormat(ImageIO.createImageInputStream(is))); }
        catch (IOException e) {
            LogUtils.logWarn(ImageUtils.class, e.getMessage(), e);
            return null;
        }
    }
    /**
     * Parse the image format
     *
     * @param imageUrl The {@link URL} to image
     *
     * @return The image's format or null
     */
    public static String getImageFormat(URL imageUrl) {
    	try { return (imageUrl == null ? null : getImageFormat(imageUrl.openStream())); }
    	catch (IOException e) {
    	    LogUtils.logWarn(ImageUtils.class, e.getMessage(), e);
    	    return null;
    	}
    }
    /**
     * Parse the image format
     *
     * @param bytes image data
     *
     * @return The image's format or null
     */
    public static String getImageFormat(byte[] bytes) {
    	return (!CollectionUtils.isEmpty(bytes)
    	        ? getImageFormat(new ByteArrayInputStream(bytes)) : null);
    }

    /**
	 * Gets {@link BufferedImage} from bytes array
	 *
	 * @param imagebytes The {@link BufferedImage} object under bytes array
	 *
	 * @return The {@link BufferedImage} or null
	 */
	public static BufferedImage fromByteArray(byte[] imagebytes) {
		try {
			if (!CollectionUtils.isEmpty(imagebytes)) {
				ByteArrayInputStream bais = new ByteArrayInputStream(imagebytes);
				ImageInputStream iis = ImageIO.createImageInputStream(bais);
				return ImageIO.read(iis);
			}
			return null;
		}
		catch (IOException e) {
		    LogUtils.logWarn(ImageUtils.class, e.getMessage());
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}
	/**
	 * Gets bytes array from a {@link BufferedImage}
	 *
	 * @param image The {@link BufferedImage}
	 * @param format The image format
	 *
	 * @return The bytes array of {@link BufferedImage} or empty bytes array
	 */
	public static byte[] toByteArray(BufferedImage image, String format) {
		if (image != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageOutputStream ios = null;
			try {
				ios = ImageIO.createImageOutputStream(baos);
				ImageIO.write(image, format, ios);
				byte[] b = baos.toByteArray();
				ios.flush();
				return b;
			}
			catch (IOException e) {
			    LogUtils.logWarn(ImageUtils.class, e.getMessage());
				throw new IllegalStateException(e.getMessage(), e);
			}
			finally {
			    StreamUtils.closeQuitely(ios);
			}
		}
		return new byte[0];
	}

	/**
	 * Gets {@link BufferedImage} from BASE64 string
	 *
	 * @param base64 encoded BASE64 string
	 * @param charset charset to encode/decode
	 *
	 * @return {@link BufferedImage} from BASE64 string or NULL if fail
	 */
	public static BufferedImage fromBase64(String base64, String charset) {
	    String[] base64Parts = base64.split(",");
	    base64 = (CollectionUtils.isElementsNumber(base64Parts, 2) ? base64Parts[1] : base64);
	    byte[] data = EncryptUtils.base64decodeBytesArray(base64, charset);
        return (!CollectionUtils.isEmpty(data) ? fromBase64(data) : null);
	}
	/**
     * Gets {@link BufferedImage} from BASE64 bytes array
     *
     * @param base64 encoded BASE64 bytes array
     *
     * @return {@link BufferedImage} from BASE64 bytes array or NULL if fail
     */
    public static BufferedImage fromBase64(byte[] base64) {
        BufferedImage image = null;
        ByteArrayInputStream bis = null;
        try {
            base64 = EncryptUtils.base64decodeBytesArray(base64);
            bis = new ByteArrayInputStream(base64);
            image = ImageIO.read(bis);
        } catch (Exception e) {
            LogUtils.logWarn(ImageUtils.class, e.getMessage(), e);
            image = null;
        } finally {
            StreamUtils.closeQuitely(bis);
        }
        return image;
    }

    /**
     * Gets BASE64 string from {@link BufferedImage}
     *
     * @param image to encode
     * @param imageFormat image format. Ex: PNG, JPG, ...
     * @param charset charset to encode/decode
     *
     * @return BASE64 string from {@link BufferedImage} or NULL if fail
     */
    public static String toBase64(BufferedImage image, String imageFormat, String charset) {
        byte[] data = toBase64(image, imageFormat);
        return (data != null ? EncryptUtils.base64encode(data, charset) : null);
    }
    /**
     * Gets BASE64 bytes array from {@link BufferedImage}
     *
     * @param image to encode
     * @param imageFormat image format. Ex: PNG, JPG, ...
     *
     * @return BASE64 bytes array from {@link BufferedImage} or NULL if fail
     */
    public static byte[] toBase64(BufferedImage image, String imageFormat) {
        byte[] data = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ImageIO.write(image, imageFormat, bos);
            data = bos.toByteArray();
            bos.flush();
            data = EncryptUtils.base64encodeBytesArray(data);
        } catch (Exception e) {
            LogUtils.logWarn(ImageUtils.class, e.getMessage(), e);
            data = null;
        } finally {
            StreamUtils.closeQuitely(bos);
        }
        return data;
    }

    /**
     * Gets {@link BufferedImage} from the specified file path
     *
     * @param filePath image file path
     *
     * @return {@link BufferedImage} from file path or NULL if fail
     */
    public static BufferedImage fromFile(String filePath) {
        File imageFile = (!StringUtils.hasText(filePath) ? null : new File(filePath));
        return fromFile(imageFile);
    }
    /**
     * Gets {@link BufferedImage} from the specified file path
     *
     * @param imagePath image file path
     *
     * @return {@link BufferedImage} from file path or NULL if fail
     */
    public static BufferedImage fromFile(Path imagePath) {
        BufferedImage image = null;
        if (imagePath != null
                && Files.exists(imagePath)
                && !Files.isDirectory(imagePath)
                && Files.isReadable(imagePath)) {
            try {
                image = ImageIO.read(imagePath.toFile());
            } catch (Exception e) {
                LogUtils.logWarn(ImageUtils.class, e.getMessage(), e);
                image = null;
            }
        }
        return image;
    }
    /**
     * Gets {@link BufferedImage} from the specified file path
     *
     * @param imageFile image file path
     *
     * @return {@link BufferedImage} from file path or NULL if fail
     */
    public static BufferedImage fromFile(File imageFile) {
        Path imagePath = (imageFile == null ? null : imageFile.toPath());
        return fromFile(imagePath);
    }
}
