/*
 * @(#)ImageUtils.java 1.0 Oct 12, 2016
 * Copyright 2016 by SystemEXE Inc. All rights reserved.
 */
package org.nlh4j.android.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.EncryptUtils;
import org.nlh4j.util.StreamUtils;
import org.nlh4j.util.StringUtils;
import org.springframework.util.Assert;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Image utilities
 *
 * @author Hai Nguyen
 *
 */
public final class ImageUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Convert from BASE64 string to {@link Bitmap}
     *
     * @param base64 to convert
     * @param charset charset
     *
     * @return converted {@link Bitmap} or NULL if fail
     */
    public static Bitmap fromBase64(String base64, String charset) {
        byte[] data = null;
        try {
            // get image data
            data = EncryptUtils.base64decodeBytesArray(base64, charset);
            return from(data);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
        }
        return null;
    }
    /**
     * Convert from bytes array to {@link Bitmap}
     *
     * @param data to convert
     *
     * @return converted {@link Bitmap} or NULL if fail
     */
    public static Bitmap from(byte[] data) {
        try {
            return (!CollectionUtils.isEmpty(data)
                    ? BitmapFactory.decodeByteArray(data, 0, data.length) : null);
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
        }
        return null;
    }

    /**
     * Convert from {@link Bitmap} to BASE64 string
     *
     * @param image to convert
     * @param cf image compression format
     * @param quality image quality
     * @param charset charset
     *
     * @return converted BASE64 string or NULL if fail
     */
    public static String toBase64(Bitmap image, CompressFormat cf, int quality, String charset) {
        byte[] data = to(image, cf, quality);
        return (!CollectionUtils.isEmpty(data)
                ? EncryptUtils.base64encode(data, charset) : null);
    }
    /**
     * Convert from {@link Bitmap} to bytes array
     *
     * @param image to convert
     * @param cf image compression format
     * @param quality image quality
     *
     * @return converted bytes array or NULL if fail
     */
    public static byte[] to(Bitmap image, CompressFormat cf, int quality) {
        ByteArrayOutputStream bos = null;
        byte[] data = null;
        try {
            // get image data
            bos = new ByteArrayOutputStream();
            image.compress(cf, quality, bos);
            data = bos.toByteArray();
            bos.flush();
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            data = null;
        } finally {
            StreamUtils.closeQuitely(bos);
        }
        return data;
    }

    /**
     * Scale and keep the specified {@link Bitmap} aspect ratio
     *
     * @param image to scale
     * @param width scale to width
     *
     * @return the scaled {@link Bitmap}
     */
    public static Bitmap scaleToFitWidth(Bitmap image, int width) {
        Bitmap scaled = image;
        if (image != null) {
            float factor = (width / (float) image.getWidth());
            scaled = Bitmap.createScaledBitmap(image, width, (int) (image.getHeight() * factor), true);
        }
        return scaled;
    }

    /**
     * Scale and keep the specified {@link Bitmap} aspect ratio
     *
     * @param image to scale
     * @param width scale to width
     *
     * @return the scaled {@link Bitmap}
     */
    public static Bitmap scaleToFitHeight(Bitmap image, int height) {
        Bitmap scaled = image;
        if (image != null) {
            float factor = (height / (float) image.getHeight());
            scaled = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * factor), height, true);
        }
        return scaled;
    }

    /**
     * Scale and keep the specified {@link Bitmap} aspect ratio
     *
     * @param image to scale
     * @param width scale to width
     * @param height scale to height
     *
     * @return the scaled {@link Bitmap}
     */
    public static Bitmap scaleToFill(Bitmap image, int width, int height) {
        Bitmap scaled = image;
        if (image != null) {
            float factorH = (height / (float) image.getWidth());
            float factorW = (width / (float) image.getWidth());
            float factorToUse = (factorH > factorW) ? factorW : factorH;
            scaled = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * factorToUse), (int) (image.getHeight() * factorToUse), true);
        }
        return scaled;
    }

    /**
     * Scale and don't keep the specified {@link Bitmap} aspect ratio
     *
     * @param image to scale
     * @param width scale to width
     * @param height scale to height
     *
     * @return the scaled {@link Bitmap}
     */
    public static Bitmap strechToFill(Bitmap image, int width, int height) {
        Bitmap scaled = image;
        if (image != null) {
            float factorH = (height / (float) image.getHeight());
            float factorW = (width / (float) image.getWidth());
            scaled = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * factorW), (int) (image.getHeight() * factorH), true);
        }
        return scaled;
    }

    /**
     * Read image from assets
     *
     * @param context {@link Context}
     * @param folder folder name, if you want the root send null or empty string
     * @param filename image name with extension e.g.image.png
     *
     * @return {@link Bitmap} or null if failed
     * @throws IOException throw exception if the image not found or error while reading
     */
    public static Bitmap fromAssets(Context context, String folder, String filename) {
        Assert.hasText(filename, "filename");
        folder = (!StringUtils.hasText(folder) ? "" : folder);
        if (StringUtils.hasText(folder) && !folder.endsWith("/")) {
            folder += "/";
        }
        // open file stream
        InputStream is = null;
        try {
            is = context.getAssets().open(folder + filename);
            return BitmapFactory.decodeStream(is);
        } catch(Exception e) {
            LogUtils.w(e.getMessage());
        } finally {
            StreamUtils.closeQuitely(is);
        }
        return null;
    }

    /**
     * Decode {@link Bitmap} from resource by identity
     *
     * @param context {@link Context}
     * @param resId resource identity
     *
     * @return {@link Bitmap} or NULL if failed
     */
    public static Bitmap fromResource(Context context, int resId) {
        Resources res = (context != null ? context.getResources() : Resources.getSystem());
        try {
            return (res != null ? BitmapFactory.decodeResource(res, resId) : null);
        } catch (Exception e) {
            LogUtils.w(e.getMessage());
            return (context == null ? null
                    : fromResource(context.getApplicationContext(), resId));
        }
    }

    /**
     * Convert from {@link Drawable} to {@link Bitmap}
     *
     * @param drawable to convert
     * @param bmpCfg {@link Bitmap} color configuration. such as {@link Config#ARGB_8888}, etc...
     *
     * @return {@link Bitmap} or NULL if failed
     **/
    public static Bitmap fromDrawable(Drawable drawable, Config bmpCfg) {
        Bitmap bmp = null;
        Canvas canvas = null;
        if (drawable != null) {
            try {
                // if it's a bitmap
                if (BeanUtils.isInstanceOf(drawable, Bitmap.class)) {
                    bmp = BeanUtils.safeType(drawable, Bitmap.class);

                    // else if valid width, height
                } else if (drawable.getIntrinsicWidth() > 0 || drawable.getIntrinsicHeight() > 0) {
                    bmp = Bitmap.createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            (bmpCfg == null ? Config.ARGB_8888 : bmpCfg));
                    canvas = new Canvas(bmp);
                    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                    drawable.draw(canvas);
                }
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
                bmp = null;
            }
        }
        return bmp;
    }
    /**
     * Convert from {@link Drawable} to {@link Bitmap} with default {@link Config#ARGB_8888}
     *
     * @param drawable to convert
     *
     * @return {@link Bitmap} or NULL if failed
     */
    public static Bitmap fromDrawable(Drawable drawable) {
        return fromDrawable(drawable, Config.ARGB_8888);
    }

    /**
     * Convert from {@link Bitmap} to {@link BitmapDrawable}
     *
     * @param context {@link Context}
     * @param image to convert
     *
     * @return {@link BitmapDrawable} or NULL if failed
     */
    public static BitmapDrawable toDrawable(Context context, Bitmap image) {
        BitmapDrawable drawable = null;
        if (image != null) {
            Resources res = (context != null ? context.getResources() : Resources.getSystem());
            try {
                drawable = new BitmapDrawable(res, image);
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
                if (context != null && context.getApplicationContext() != null) {
                    drawable = toDrawable(context.getApplicationContext(), image);
                } else {
                    drawable = null;
                }
            }
        }
        return drawable;
    }
    /**
     * Convert from resource identity to {@link BitmapDrawable}
     *
     * @param context {@link Context}
     * @param resId to convert
     *
     * @return {@link BitmapDrawable} or NULL if failed
     */
    public static BitmapDrawable toDrawable(Context context, int resId) {
        BitmapDrawable drawable = null;
        // parse bitmap from resource
        Bitmap image = fromResource(context, resId);
        if (image != null) {
            Resources res = (context != null ? context.getResources() : Resources.getSystem());
            try {
                drawable = new BitmapDrawable(res, image);
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
                if (context != null && context.getApplicationContext() != null) {
                    drawable = toDrawable(context.getApplicationContext(), image);
                } else {
                    drawable = null;
                }
            }
        }
        return drawable;
    }

    /**
     * Convert from {@link Bitmap} to {@link InputStream}
     *
     * @param image to convert
     * @param format image compression format type
     * @param quality compression quality
     *
     * @return {@link InputStream} or NULL if failed
     */
    public static InputStream toInputStream(Bitmap image, CompressFormat format, int quality) {
        InputStream is = null;
        if (image != null) {
            ByteArrayOutputStream baos = null;
            format = (format == null ? CompressFormat.PNG : format);
            try {
                baos = new ByteArrayOutputStream();
                image.compress(format, quality, baos);
                is = new ByteArrayInputStream(baos.toByteArray());
                baos.flush();
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
                StreamUtils.closeQuitely(is);
                is = null;
            } finally {
                StreamUtils.closeQuitely(baos);
            }
        }
        return is;
    }
    /**
     * Convert from {@link Bitmap} to PNG {@link InputStream}
     *
     * @param image to convert
     *
     * @return {@link InputStream} or NULL if failed
     */
    public static InputStream toInputStream(Bitmap image) {
        return toInputStream(image, CompressFormat.PNG, 100);
    }

    /**
     * Compress the specified {@link Bitmap}
     *
     * @param image to compress
     * @param bmpCfg {@link Bitmap} color configuration. such as {@link Config#ARGB_8888}, etc...
     * @param format image compression format type
     * @param quality compression quality
     * @param factor powers of 2 are often faster/easier for the decoder to honor
     *
     * @return compressed {@link Bitmap} or NULL if failed
     */
    public static Bitmap compressImage(
           Bitmap image, Config bmpCfg, CompressFormat format, int quality, int factor) {
        Bitmap compressed = null;
        if (image != null) {
            ByteArrayOutputStream baos = null;
            BitmapFactory.Options opts = null;
            try {
                // decode options
                opts = new BitmapFactory.Options();
                opts.inPreferredConfig = Config.ARGB_8888;
                opts.inSampleSize = factor;
                if (CompatibilityUtils.isGingerBreadMr1()) {
                    opts.inPreferQualityOverSpeed = true;
                }

                // compress image
                baos = new ByteArrayOutputStream();
                image.compress(format, quality, baos);
                compressed = from(baos.toByteArray());
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
                compressed = null;
            } finally {
                StreamUtils.closeQuitely(baos);
            }
        }
        return compressed;
    }
    /**
     * Compress the specified {@link Bitmap} with default {@link Config#ARGB_8888}
     *
     * @param image to compress
     * @param format image compression format type
     * @param quality compression quality
     * @param factor powers of 2 are often faster/easier for the decoder to honor
     *
     * @return compressed {@link Bitmap} or NULL if failed
     */
    public static Bitmap compressImage(
           Bitmap image, CompressFormat format, int quality, int factor) {
        return compressImage(image, Config.ARGB_8888, format, quality, factor);
    }
    /**
     * Compress the specified {@link Bitmap} with default {@link Config#ARGB_8888}, {@link CompressFormat#PNG}
     *
     * @param image to compress
     * @param format image compression format type
     * @param quality compression quality
     * @param factor powers of 2 are often faster/easier for the decoder to honor
     *
     * @return compressed {@link Bitmap} or NULL if failed
     */
    public static Bitmap compressImage(Bitmap image, int factor) {
        return compressImage(image, Config.ARGB_8888, CompressFormat.PNG, 100, factor);
    }
}
