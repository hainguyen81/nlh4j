/*
 * @(#)ClipboardUtils.java 1.0 Nov 19, 2016 Copyright 2016 by GNU Lesser General Public License (LGPL). All
 * rights reserved.
 */
package org.nlh4j.android.util;

import java.io.FileInputStream;
import java.io.Serializable;

import org.nlh4j.util.StreamUtils;
import org.nlh4j.util.StringUtils;

import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;

/**
 * Clipboard utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public final class ClipboardUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_BUFFER_SIZE = (4 * 1024);

    /**
     * Coerce the specified clipboard item data to text
     *
     * @param context {@link Context}
     * @param item clipboard item data
     *
     * @return text or NULL if failed
     */
    public CharSequence coerceClipboard(Context context, Item item) {
        // If this Item has a URI value, try using that.
        Uri uri = item.getUri();
        if (uri != null) {
            // First see if the URI can be opened as a plain text stream
            // (of any sub-type). If so, this is the best textual
            // representation for it.
            FileInputStream fis = null;
            try {
                // Ask for a stream of the desired type.
                AssetFileDescriptor descr = context.getContentResolver()
                        .openTypedAssetFileDescriptor(uri, "text/*", null);
                fis = descr.createInputStream();
                return StringUtils.toString(fis);
            } catch (Exception e) {
                LogUtils.w(e.getMessage());
            } finally {
                StreamUtils.closeQuitely(fis);
            }

            // If we couldn't open the URI as a stream, then the URI itself
            // probably serves fairly well as a textual representation.
            return uri.toString();
        }

        // Finally, if all we have is an Intent, then we can just turn that
        // into text. Not the most user-friendly thing, but it's something.
        Intent intent = item.getIntent();
        if (intent != null) { return intent.toUri(Intent.URI_INTENT_SCHEME); }

        // If this Item has an explicit textual value, simply return that.
        String htmlText = item.getHtmlText();
        if (StringUtils.isHtml(htmlText)) { return htmlText; }

        // If this Item has an explicit textual value, simply return that.
        CharSequence text = item.getText();
        if (text != null) { return text; }

        // Shouldn't get here, but just in case...
        return null;
    }

    /**
     * Copy the specified text to clipboard
     *
     * @param context {@link Context}
     * @param label User-visible label for the clip data.
     * @param text The actual text in the clip.
     *
     * @return true for successful; else false
     */
    @SuppressWarnings("deprecation")
    public static boolean textToClipboard(Context context, CharSequence label, CharSequence text) {
        try {
            // if API level is 10 or before
            if (!CompatibilityUtils.isHoneycomb() && StringUtils.hasText(text)) {
                android.text.ClipboardManager cm = ServiceUtils.getTextClipboardManager(context);
                if (cm != null) {
                    cm.setText(text);
                    return true;
                }

                // else API level is 11 or later
            } else if (StringUtils.hasText(text)) {
                ClipboardManager cm = ServiceUtils.getClipboardManager(context);
                if (cm != null) {
                    ClipData clip = ClipData.newPlainText(label, text);
                    cm.setPrimaryClip(clip);
                    return true;
                }
            }
        }
        catch (Exception e) {
            LogUtils.w(e.getMessage());
        }
        return false;
    }
    /**
     * Read text/HTML text/{@link Uri}/{@link Intent} from clipboard
     *
     * @param context {@link Context}
     * @param idx clipboard data index
     *
     * @return text or NULL if failed
     */
    @SuppressWarnings("deprecation")
    public String textFromClipboard(Context context, int idx) {
        if (!CompatibilityUtils.isHoneycomb()) {
            android.text.ClipboardManager cm = ServiceUtils.getTextClipboardManager(context);
            return (cm != null && cm.getText() != null ? cm.getText().toString() : null);
        }
        else {
            // Gets a content resolver instance
            ClipboardManager cm = ServiceUtils.getClipboardManager(context);
            if (cm != null && cm.hasPrimaryClip()) {
                // Gets the clipboard data from the clipboard
                ClipData clip = cm.getPrimaryClip();
                if (clip != null) {
                    // Gets the first item from the clipboard data
                    if (0 <= idx && idx < clip.getItemCount()) {
                        ClipData.Item item = clip.getItemAt(idx);
                        CharSequence text = coerceClipboard(context, item);
                        return (text == null ? null : text.toString());
                    }
                }
            }
        }
        return null;
    }

    /**
     * Copy the specified HTML text to clipboard
     *
     * @param context {@link Context}
     * @param label User-visible label for the clip data.
     * @param text The text of clip as plain text, for receivers that don't handle HTML. This is required.
     * @param htmlText The actual HTML text in the clip.
     *
     * @return true for successful; else false
     */
    @SuppressWarnings("deprecation")
    public static boolean htmlToClipboard(Context context, CharSequence label, CharSequence text, String htmlText) {
        try {
            // if API level is 10 or before
            if (!CompatibilityUtils.isHoneycomb() && StringUtils.hasText(text)) {
                android.text.ClipboardManager cm = ServiceUtils.getTextClipboardManager(context);
                if (cm != null) {
                    cm.setText(text);
                    return true;
                }

                // else API level is 11 or later
            } else if (StringUtils.hasText(text) && StringUtils.isHtml(htmlText)) {
                ClipboardManager cm = ServiceUtils.getClipboardManager(context);
                if (cm != null) {
                    ClipData clip = ClipData.newHtmlText(label, text, htmlText);
                    cm.setPrimaryClip(clip);
                    return true;
                }
            }
        }
        catch (Exception e) {
            LogUtils.w(e.getMessage());
        }
        return false;
    }
    /**
     * Copy the specified {@link Intent} to clipboard
     *
     * @param context {@link Context}
     * @param label User-visible label for the clip data.
     * @param intent The actual Intent in the clip.
     *
     * @return true for successful; else false
     */
    public static boolean intentToClipboard(Context context, CharSequence label, Intent intent) {
        try {
            // if API level is 11 or later
            if (CompatibilityUtils.isHoneycomb() && intent != null) {
                ClipboardManager cm = ServiceUtils.getClipboardManager(context);
                if (cm != null) {
                    ClipData clip = ClipData.newIntent(label, intent);
                    cm.setPrimaryClip(clip);
                    return true;
                }
            }
        }
        catch (Exception e) {
            LogUtils.w(e.getMessage());
        }
        return false;
    }
    /**
     * Copy the specified {@link Uri} to clipboard
     *
     * @param context {@link Context}
     * @param label User-visible label for the clip data.
     * @param uri The URI in the clip.
     *
     * @return true for successful; else false
     */
    public static boolean uriToClipboard(Context context, CharSequence label, Uri uri) {
        try {
            // if API level is 11 or later
            if (CompatibilityUtils.isHoneycomb() && uri != null) {
                ClipboardManager cm = ServiceUtils.getClipboardManager(context);
                if (cm != null) {
                    ClipData clip = ClipData.newRawUri(label, uri);
                    cm.setPrimaryClip(clip);
                    return true;
                }
            }
        }
        catch (Exception e) {
            LogUtils.w(e.getMessage());
        }
        return false;
    }
}
