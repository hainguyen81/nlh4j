/*
 * @(#)StringUtils.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;

/**
 * String utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class StringUtils implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    private static final String ENCODING_UTF8 = "UTF-8";
    private static final String ENCODING_ASCII = "US-ASCII";
    public static final String HTML_PATTERN_REGEX = ".*\\<[^>]+>.*";
    public static final int DEFAULT_BUFFER_SIZE = (4 * 1024);
    public static final String PREFIX_CLASSPATH = "classpath:";
    public static final String PREFIX_WIRECAST_CLASSPATH = "classpath*:";
    public static final String PREFIX_SUB_CLASSPATH = "classpath:**";
    public static final String PREFIX_SUB_WIRECAST_CLASSPATH = "classpath*:**";

    /**
     * Window special characters restriction
     */
    private transient static String[] WIN_FILE_NAME_SYMBOLS = null;
    /**
     * Get the window special characters restriction
     * @return the window special characters restriction
     */
    private static final String[] getSpecialWinFileNameSymbols() {
        if (WIN_FILE_NAME_SYMBOLS == null) {
            WIN_FILE_NAME_SYMBOLS = new String[] {
                    "<", ">", ":", "\"", "/", "\\", "|", "?", "*", "{", "}", "\t", "\r\n", "\n"
            };
        }
        synchronized (WIN_FILE_NAME_SYMBOLS) {
            return WIN_FILE_NAME_SYMBOLS;
        }
    }
    /**
     * Window special characters restriction
     */
    private transient static String[] WIN_FILE_NAME_PHRASE = null;
    private static final String[] getSpecialWinFileNamePhrases() {
        if (WIN_FILE_NAME_PHRASE == null) {
            WIN_FILE_NAME_PHRASE = new String[] {
                    "CON", "PRN", "AUX", "NUL",
                    "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
                    "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9",
            };
        }
        synchronized (WIN_FILE_NAME_PHRASE) {
            return WIN_FILE_NAME_PHRASE;
        }
    }

    /**
     * Check that the given string is neither null nor of length 0.
     * Alias of {@link org.springframework.util.StringUtils#hasLength(String)} function
     *
     * @param s the string to check (may be null)
     *
     * @return true if the string is not null and has length
     */
    public static boolean hasLength(String s) {
        return org.springframework.util.StringUtils.hasLength(s);
    }
    /**
     * Check that the given string is neither null nor of length 0.
     * Alias of {@link org.springframework.util.StringUtils#hasLength(CharSequence)} function
     *
     * @param s the string to check (may be null)
     *
     * @return true if the string is not null and has length
     */
    public static boolean hasLength(CharSequence s) {
        return org.springframework.util.StringUtils.hasLength(s);
    }
    /**
     * Check that the given string is neither null nor of length 0.
     * Alias of {@link org.springframework.util.StringUtils#hasLength(String)} function
     *
     * @param s the {@link Object#toString()} to check (may be null)
     *
     * @return true if the string is not null and has length
     */
    public static boolean hasLength(Object s) {
        return (s != null && hasLength(s.toString()));
    }
    /**
     * Check whether the given string contains actual text.
     * Alias of {@link org.springframework.util.StringUtils#hasText(String)} function
     *
     * @param s the string to check (may be null)
     *
     * @return true if the string is not null, its length is greater than 0, and it does not contain whitespace only
     */
    public static boolean hasText(String s) {
        return org.springframework.util.StringUtils.hasText(s);
    }
    /**
     * Check whether the given string contains actual text.
     * Alias of {@link org.springframework.util.StringUtils#hasText(CharSequence)} function
     *
     * @param s the string to check (may be null)
     *
     * @return true if the string is not null, its length is greater than 0, and it does not contain whitespace only
     */
    public static boolean hasText(CharSequence s) {
        return org.springframework.util.StringUtils.hasText(s);
    }
    /**
     * Check whether the given string contains actual text.
     * Alias of {@link org.springframework.util.StringUtils#hasText(String)} function
     *
     * @param s the {@link Object#toString()} to check (may be null)
     *
     * @return true if the string is not null, its length is greater than 0, and it does not contain whitespace only
     */
    public static boolean hasText(Object s) {
        return (s != null && hasText(s.toString()));
    }
    /**
     * Check whether the given string ends with actual text ignore-case-sensitive.
     * Alias of {@link org.springframework.util.StringUtils#endsWithIgnoreCase(String, String)} function
     *
     * @param s the string to check (may be null)
     * @param suffix the suffix to check (may be null)
     *
     * @return true if the string is not null, suffix is not null and the string ends with suffix case-insensitive
     */
    public static boolean endsWithIgnoreCase(String s, String suffix) {
        return (hasLength(s) && hasLength(suffix)
                && org.springframework.util.StringUtils.endsWithIgnoreCase(s, suffix));
    }
    /**
     * Check whether the given string ends with actual text ignore-case-sensitive.
     * Alias of {@link org.springframework.util.StringUtils#endsWithIgnoreCase(String, String)} function
     *
     * @param s the string to check (may be null)
     * @param suffix the suffix to check (may be null)
     *
     * @return true if the string is not null, suffix is not null and the string ends with suffix case-insensitive
     */
    public static boolean endsWithIgnoreCase(Object s, String suffix) {
        return (hasLength(s) && hasLength(suffix)
                && endsWithIgnoreCase(s.toString(), suffix));
    }

    /**
     * Remove all HTML tags
     *
     * @param text to remove
     *
     * @return removed string
     */
    public static String removeHtmlTags(String text) {
        if (hasLength(text)) {
            HtmlCleaner cleaner = new HtmlCleaner();
            TagNode root = cleaner.clean(text == null ? "" : text);
            return root.getText().toString();
        }
        return text;
    }

    /**
     * Replace all specified characters by empty string
     *
     * @param source the source string to remove
     * @param specialChars the characters to find and remove
     *
     * @return the string after removing the specified characters
     */
    public static String removeSpecChar(String source, String... specialChars) {
        if (hasLength(source) && !CollectionUtils.isEmpty(specialChars)) {
            for (String specialChar : specialChars) {
                source = source.replaceAll(specialChar, "");
            }
        }
        return source;
    }

    /**
     * Convert to lower case the first char in string sentence.
     *
     * @param text the string sentence to convert
     *
     * @return the string after converting
     */
    public static String toLowerCaseFirstChar(String text) {
        if (hasText(text)) {
            text = text.substring(0, 1).toLowerCase() + text.substring(1);
        }
        return text;
    }
    /**
     * Convert to upper case the first char in string sentence.
     *
     * @param text the string sentence to convert
     *
     * @return the string after converting
     */
    public static String toUpperCaseFirstChar(String text) {
        if (hasText(text)) {
            text = text.substring(0, 1).toUpperCase() + text.substring(1);
        }
        return text;
    }

    /**
     * Splits message by the specified length
     *
     * @param message the transmittal message
     * @param length the length to split. &lt;= 0 for not splitting
     * @param extra the extra string such as [...]. Default is [...]
     * @param checkWithEol specifies checking with end-of-line character.
     * @param eol the end-of-line character
     * @param html specifies the message whether is HTML message
     *
     * @return the split message
     */
    public static String splitMessage(String message, int length, String extra, boolean checkWithEol, String eol, boolean html) {
        // checks parameter
        length = Math.max(length, 0);
        if (length == 0) return message;
        extra = (!hasText(extra) ? "..." : extra);
        eol = (!hasText(eol) ? "\n" : eol);
        message = (!hasLength(message) ? "" : message);
        // convert message to plain text
        message = org.springframework.util.StringUtils.trimWhitespace(html ? Jsoup.parse(message).text() : message);
        boolean needDetail = (message.length() > length);
        // substring by length
        if (needDetail) {
            String msgTmp = "";
            if (!checkWithEol || message.indexOf(eol) <= 0) {
                msgTmp = message.substring(0, length);
            }
            else if (checkWithEol && 0 < message.indexOf(eol) && message.indexOf(eol) <= length) {
                msgTmp = message.substring(0, (message.indexOf(eol) - 1));
            }
            else {
                msgTmp = message.substring(0, length);
            }
            // substring by the last index of space character
            if (msgTmp.lastIndexOf(" ") > 0) {
                message = message.substring(0, msgTmp.lastIndexOf(" ")).trim();
            }
            else {
                message = msgTmp;
            }
            message += extra;
        }
        else if (checkWithEol && 0 < message.indexOf(eol) && message.indexOf(eol) <= message.length()) {
            message = message.substring(0, (message.indexOf(eol) - 1));
            // substring by the last index of space character
            if (message.lastIndexOf(" ") > 0) {
                message = message.substring(0, message.lastIndexOf(" ")).trim();
            }
            message += extra;
        }
        // returns splitted message
        return message;
    }
    /**
     * Splits message by the specified length
     *
     * @param message the transmittal message
     * @param length the length to split. &lt;= 0 for not spliting
     * @param extra the extra string such as [...]. Default is [...]
     * @param checkWithEol specifies checking with end-of-line character.
     *
     * @return the split message
     */
    public static String splitMessage(String message, int length, String extra, boolean checkWithEol) {
        return splitMessage(message, length, extra, checkWithEol, "\n", false);
    }
    /**
     * Splits message by the specified length
     *
     * @param message the transmittal message
     * @param length the length to split. &lt;= 0 for not spliting
     *
     * @return the split message
     */
    public static String splitMessage(String message, int length) {
        return splitMessage(message, length, null, false, null, false);
    }

    /**
     * Splits message by the specified length
     *
     * @param message the transmittal message
     * @param length the length to split. &lt;= 0 for not spliting
     * @param brkStr the break-string such as . Default is ''
     * @param html specifies the message whether is HTML message
     *
     * @return the split message
     */
    public static String breakMessage(String message, int length, String brkStr, boolean html) {
        if (!hasLength(message)) return message;
        // checks parameter
        length = Math.max(length, 0);
        if (length == 0) return message;
        brkStr = (!hasText(brkStr) ? "" : brkStr);
        message = (!hasLength(message) ? "" : message);
        // convert message to plain text
        message = org.springframework.util.StringUtils.trimWhitespace(html ? Jsoup.parse(message).text() : message);
        boolean needDetail = (message.length() > length);
        // substring by length
        if (needDetail) {
            String brokeMsg = "";
            while(message.length() > length) {
                if (hasText(brokeMsg)) {
                    brokeMsg += brkStr;
                }
                String msgTmp = message.substring(0, length);
                if (msgTmp.lastIndexOf(" ") > 0) {
                    brokeMsg += message.substring(0, msgTmp.lastIndexOf(" ")).trim();
                    message = message.substring(msgTmp.lastIndexOf(" "));
                }
                else {
                    brokeMsg += msgTmp;
                    message = message.substring(length);
                }
            }
            // last part
            if (message.length() > 0) {
                brokeMsg += brkStr + message;
            }
            // resets message
            message = brokeMsg;
        }
        // returns broken message
        return message;
    }
    /**
     * Splits message by the specified length
     *
     * @param message the transmittal message
     * @param length the length to split. &lt;= 0 for not splitting
     *
     * @return the split message
     */
    public static String breakMessage(String message, int length) {
        return breakMessage(message, length, null, false);
    }

    /**
     * Converts signal characters to no-signal characters
     *
     * @param org the original string to convert
     *
     * @return the converted string
     */
    public static String unsign(String org) {
        if (!hasText(org)) return org;
        char arrChar[] = org.toCharArray();
        char result[] = new char[arrChar.length];
        for (int i = 0; i < arrChar.length; i++) {
            switch(arrChar[i]) {
                case '\u00E1':
                case '\u00E0':
                case '\u1EA3':
                case '\u00E3':
                case '\u1EA1':
                case '\u0103':
                case '\u1EAF':
                case '\u1EB1':
                case '\u1EB3':
                case '\u1EB5':
                case '\u1EB7':
                case '\u00E2':
                case '\u1EA5':
                case '\u1EA7':
                case '\u1EA9':
                case '\u1EAB':
                case '\u1EAD':
                case '\u0203':
                case '\u01CE': {
                    result[i] = 'a';
                    break;
                }
                case '\u00E9':
                case '\u00E8':
                case '\u1EBB':
                case '\u1EBD':
                case '\u1EB9':
                case '\u00EA':
                case '\u1EBF':
                case '\u1EC1':
                case '\u1EC3':
                case '\u1EC5':
                case '\u1EC7':
                case '\u0207': {
                    result[i] = 'e';
                    break;
                }
                case '\u00ED':
                case '\u00EC':
                case '\u1EC9':
                case '\u0129':
                case '\u1ECB': {
                    result[i] = 'i';
                    break;
                }
                case '\u00F3':
                case '\u00F2':
                case '\u1ECF':
                case '\u00F5':
                case '\u1ECD':
                case '\u00F4':
                case '\u1ED1':
                case '\u1ED3':
                case '\u1ED5':
                case '\u1ED7':
                case '\u1ED9':
                case '\u01A1':
                case '\u1EDB':
                case '\u1EDD':
                case '\u1EDF':
                case '\u1EE1':
                case '\u1EE3':
                case '\u020F': {
                    result[i] = 'o';
                    break;
                }
                case '\u00FA':
                case '\u00F9':
                case '\u1EE7':
                case '\u0169':
                case '\u1EE5':
                case '\u01B0':
                case '\u1EE9':
                case '\u1EEB':
                case '\u1EED':
                case '\u1EEF':
                case '\u1EF1': {
                    result[i] = 'u';
                    break;
                }
                case '\u00FD':
                case '\u1EF3':
                case '\u1EF7':
                case '\u1EF9':
                case '\u1EF5': {
                    result[i] = 'y';
                    break;
                }
                case '\u0111': {
                    result[i] = 'd';
                    break;
                }
                case '\u00C1':
                case '\u00C0':
                case '\u1EA2':
                case '\u00C3':
                case '\u1EA0':
                case '\u0102':
                case '\u1EAE':
                case '\u1EB0':
                case '\u1EB2':
                case '\u1EB4':
                case '\u1EB6':
                case '\u00C2':
                case '\u1EA4':
                case '\u1EA6':
                case '\u1EA8':
                case '\u1EAA':
                case '\u1EAC':
                case '\u0202':
                case '\u01CD': {
                    result[i] = 'A';
                    break;
                }
                case '\u00C9':
                case '\u00C8':
                case '\u1EBA':
                case '\u1EBC':
                case '\u1EB8':
                case '\u00CA':
                case '\u1EBE':
                case '\u1EC0':
                case '\u1EC2':
                case '\u1EC4':
                case '\u1EC6':
                case '\u0206': {
                    result[i] = 'E';
                    break;
                }
                case '\u00CD':
                case '\u00CC':
                case '\u1EC8':
                case '\u0128':
                case '\u1ECA': {
                    result[i] = 'I';
                    break;
                }
                case '\u00D3':
                case '\u00D2':
                case '\u1ECE':
                case '\u00D5':
                case '\u1ECC':
                case '\u00D4':
                case '\u1ED0':
                case '\u1ED2':
                case '\u1ED4':
                case '\u1ED6':
                case '\u1ED8':
                case '\u01A0':
                case '\u1EDA':
                case '\u1EDC':
                case '\u1EDE':
                case '\u1EE0':
                case '\u1EE2':
                case '\u020E': {
                    result[i] = 'O';
                    break;
                }
                case '\u00DA':
                case '\u00D9':
                case '\u1EE6':
                case '\u0168':
                case '\u1EE4':
                case '\u01AF':
                case '\u1EE8':
                case '\u1EEA':
                case '\u1EEC':
                case '\u1EEE':
                case '\u1EF0': {
                    result[i] = 'U';
                    break;
                }
                case '\u00DD':
                case '\u1EF2':
                case '\u1EF6':
                case '\u1EF8':
                case '\u1EF4': {
                    result[i] = 'Y';
                    break;
                }
                case '\u0110':
                case '\u00D0':
                case '\u0089': {
                    result[i] = 'D';
                    break;
                }
                default: {
                    result[i] = arrChar[i];
                    break;
                }
            }
        }
        return new String(result);
    }

    /**
     * Returns a human-readable version of the file size, where the input represents a specific number of bytes.
     *
     * @param bytes bytes length
     * @param si specify format SI
     * @param deci decimal number
     *
     * @return a human-readable version of the file size, where the input represents a specific number of bytes.
     */
    public static String byteCountToDisplaySize(long bytes, boolean si, int deci) {
        if (bytes <= 0) return "0 B";
        int unit = (si ? 1000 : 1024);
        if (bytes < unit) return (bytes + " B");
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = ((si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i"));
        return String.format(("%." + String.valueOf(deci) + "f %sB"), (bytes / Math.pow(unit, exp)), pre);
    }
    /**
     * Returns a human-readable version of the file size, where the input represents a specific number of bytes.
     *
     * @param bytes bytes length
     * @param si specify format SI
     *
     * @return a human-readable version of the file size, where the input represents a specific number of bytes.
     */
    public static String byteCountToDisplaySize(long bytes, boolean si) {
        return byteCountToDisplaySize(bytes, si, 2);
    }
    /**
     * Returns a human-readable version of the file size, where the input represents a specific number of bytes.
     *
     * @param bytes bytes length
     *
     * @return a human-readable version of the file size, where the input represents a specific number of bytes.
     */
    public static String byteCountToDisplaySize(long bytes) {
        return byteCountToDisplaySize(bytes, Boolean.TRUE);
    }

    /**
     * Cut the specified expression from the start byte by the bytes number under the specified charset
     *
     * @param expression expression to cut
     * @param iByteStart start byte index
     * @param bytesLength bytes length
     * @param charset charset
     * @return the cut string
     */
    public static String substringByte(String expression, int iByteStart, int bytesLength, String charset) {
        String subString = null;
        iByteStart = Math.max(iByteStart, 0);
        bytesLength = Math.max(bytesLength, 0);
        // 文字列 == nullの場合、
        if (!StringUtils.hasLength(expression) || bytesLength <= 0) return subString;
        // 文字セット
        Charset objCharset = null;
        if (!StringUtils.hasText(charset)) {
            charset = ENCODING_UTF8;
        }
        try {
            objCharset = Charset.forName(charset);
        }
        catch (Exception e) {
            e.printStackTrace();
            objCharset = Charset.defaultCharset();
        }
        // バイトに「文字列」に変換
        try {
            int cntBytes = 0;
            int iStartChar = 0;
            while(cntBytes < iByteStart && iStartChar < expression.length()) {
                String tmpStr = expression.substring(iStartChar, iStartChar + 1);
                byte[] tmpByt = tmpStr.getBytes(objCharset);
                cntBytes += tmpByt.length;
                iStartChar++;
            }

            StringBuilder sb = new StringBuilder();
            cntBytes = 0;
            for(int i = iStartChar; i < expression.length(); i++) {
                String tmpStr = expression.substring(i, i + 1);
                byte[] tmpByt = tmpStr.getBytes(objCharset);
                cntBytes += tmpByt.length;
                if (cntBytes <= bytesLength) {
                    sb.append(tmpStr);
                }
                else break;
            }
            subString = sb.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
            subString = null;
        }
        // 切出した文字列
        return subString;
    }
    /**
     * 文字列切り出し（Byte単位）
     * 先頭から指定バイト数分文字列を切り出す。
     * 切り出し終了部分が日本語の途中にかかる場合は直前の文字までを切り出す。
     * @param expression 文字列
     * @param iByteStart バイトインデックス開始
     * @param bytesLength バイト数
     * @param charset 文字セット
     * @return 切出した文字列
     */
    public static String leftByte(String expression, int iByteStart, int bytesLength, String charset) {
        return substringByte(expression, 0, bytesLength, charset);
    }

    /**
     * Detect and replace all special characters from SQL object name
     * (such as table name, column name, sequence name, etc.)
     *
     * @param objNm SQL object name
     *
     * @return refixed name
     */
    public static String refixSqlObjectName(String objNm) {
        if (hasText(objNm)) {
            objNm = objNm
                    .replaceAll("'", "")
                    .replaceAll("\"", "")
                    .replaceAll("\\[", "")
                    .replaceAll("\\]", "");
        }
        return objNm;
    }

    /**
     * Refixes file name by special characters
     *
     * @param fileName
     *      the file name to re-fix
     *
     * @return the fixed file name
     */
    public static String refixWinFileName(String fileName) {
        // convert to ASCII
        try {
            fileName = new String(fileName.getBytes(ENCODING_ASCII), ENCODING_ASCII);
        }
        catch (UnsupportedEncodingException e) {}
        // replaces special symbols
        String[] fileNameSymbols = getSpecialWinFileNameSymbols();
        for(String symbol : fileNameSymbols) {
            fileName = fileName.replace(symbol, "_");
        }
        // replaces special phrase
        String[] fileNamePhrases = getSpecialWinFileNamePhrases();
        for(String phrase : fileNamePhrases) {
            if (fileName.startsWith(phrase + ".")) {
                fileName = ("_" + fileName);
                break;
            }
        }
        return fileName;
    }

    /**
     * Convert the specified {@link InputStream} to string
     *
     * @param is {@link InputStream} to convert
     * @param charset encoded charset
     * @param close specify closing stream when finishing
     *
     * @return string value after converting
     */
    public static String toString(InputStream is, String charset, boolean close) {
        String str = null;
        if (is != null) {
            StringBuilder sb = null;
            BufferedReader br = null;
            InputStreamReader isr = null;
            try {
                sb = new StringBuilder();
                isr = new InputStreamReader(is, charset);
                br = new BufferedReader(isr);
                String line = null;
                while((line = br.readLine()) != null) {
                    sb.append(line);
                    sb.append('\n');
                }
                str = sb.toString();
            } catch (Exception e) {
                str = null;
            } finally {
                if (close) {
                    StreamUtils.closeQuitely(isr);
                    StreamUtils.closeQuitely(br);
                    StreamUtils.closeQuitely(is);
                }
            }
        }
        return str;
    }
    /**
     * Convert the specified {@link InputStream} to UTF-8 string and close {@link InputStream} automatically
     *
     * @param is {@link InputStream} to convert
     *
     * @return string value after converting
     */
    public static String toString(InputStream is) {
        return toString(is, ENCODING_UTF8, Boolean.TRUE);
    }

    /**
     * Capitalizes each word in the string.
     *
     * @param string to capitalize
     *
     * @return capitalized string
     */
    public static String capitalize(String string) {
        String capped = null;
        if (StringUtils.hasText(string)) {
            char[] chars = string.toLowerCase().toCharArray();
            boolean found = false;
            for (int i = 0; i < chars.length; i++) {
                if (!found && Character.isLetter(chars[i])) {
                    chars[i] = Character.toUpperCase(chars[i]);
                    found = true;
                } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You
                    // can add other chars here
                    found = false;
                }
            }
            capped = String.valueOf(chars);
        }
        return capped;
    }

    /**
     * Join the specified object (maybe {@link Collection}, {@link Set}, {@link List}, {@link Array})
     * to string value with the specified delimiter (NULL for empty). If the specified object is not
     * a {@link Collection}, {@link Set}, {@link List}, {@link Array}; then the returned string value
     * is {@link Object#toString()}
     *
     * @param value to join
     * @param delimiter delimiter
     *
     * @return the joined string value or NULL if failed
     */
    public static String join(Object value, String delimiter) {
        String joined = null;
        if (value != null) {
            StringBuffer buff = new StringBuffer();
            delimiter = (delimiter == null ? "" : delimiter);
            // if array
            if (CollectionUtils.isArray(value)) {
                for(int i = 0; i < Array.getLength(value); i++) {
                    Object item = Array.get(value, i);
                    if (buff.length() > 0) buff.append(delimiter);
                    buff.append(String.valueOf(item));
                }

                // if collection
            } else if (CollectionUtils.isCollection(value)) {
                Collection<Object> col = CollectionUtils.safeCollection(value, Object.class);
                if (!CollectionUtils.isEmpty(col)) {
                    for(final Iterator<Object> it = col.iterator(); it.hasNext();) {
                        Object item = it.next();
                        if (buff.length() > 0) buff.append(delimiter);
                        buff.append(String.valueOf(item));
                    }
                }

                // if simple object
            } else {
                buff.append(String.valueOf(value));
            }
            joined = buff.toString();
        }
        return joined;
    }

    /**
     * Get a boolean value indicating that the specified text whether is HTML string
     *
     * @param text to check
     *
     * @return true for HTML; else false
     */
    public static boolean isHtml(String text) {
        boolean html = hasText(text);
        if (html) {
            Pattern regex = Pattern.compile(HTML_PATTERN_REGEX, Pattern.DOTALL);
            html = regex.matcher(text).matches();
        }
        return html;
    }
    /**
     * Get a boolean value indicating that the specified text whether is HTML string
     *
     * @param text to check
     *
     * @return true for HTML; else false
     */
    public static boolean isHtml(CharSequence text) {
        boolean html = hasText(text);
        if (html) {
            Pattern regex = Pattern.compile(HTML_PATTERN_REGEX, Pattern.DOTALL);
            html = regex.matcher(text).matches();
        }
        return html;
    }

    /**
     * Convert string value to bytes array by UTF-8 charset
     *
     * @param value to convert
     *
     * @return bytes array or null if failed
     */
    public static byte[] toByte(String value) {
        return toByte(value, ENCODING_UTF8);
    }
    /**
     * Convert string value to bytes array by charset
     *
     * @param value to convert
     * @param charset encoded charset
     *
     * @return bytes array or null if failed
     */
    public static byte[] toByte(String value, String charset) {
        if (hasText(value)) {
            try {
                if (!StringUtils.hasText(charset)) {
                    return value.getBytes();
                } else {
                    return value.getBytes(charset);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Fix the specified string with the specified length
     * and full-length with the specified {@link Character}
     *
     * @param s to fix
     * @param length the length of new string. &lt;= 0 to not fix
     * @param c {@link Character} to fill. NULL for not fix
     * @param prefix true for filling prefix of new string
     * @param overflow specify whether sub string from 0 index
     * if the specified string length is greater than the specified fixed-length.
     * true for not fix
     *
     * @return the fixed length string
     */
    public static String fixLength(CharSequence s, int length, Character c, boolean prefix, boolean overflow) {
        length = NumberUtils.max(new int[] { length, 0 });
        if (length <= 0 || c == null) return (s == null ? null : s.toString());
        String newStr = (s == null ? "" : s.toString());
        if (length <= newStr.length()) {
            return (overflow ? newStr : newStr.substring(0, length));
        } else {
            String pad = org.apache.commons.lang3.StringUtils.repeat(
                    c.charValue(), length - newStr.length());
            return (prefix ? pad : "") + newStr + (!prefix ? pad : "");
        }
    }
    /**
     * Fix the specified string with the specified length
     * and full-length with the specified {@link Character}
     * and not overflow ({@link #fixLength(CharSequence, int, Character, boolean, boolean)})
     *
     * @param s to fix
     * @param length the length of new string. &lt;= 0 to not fix
     * @param c {@link Character} to fill. NULL for not fix
     * @param prefix true for filling prefix of new string
     *
     * @return the fixed length string
     */
    public static String fixLength(CharSequence s, int length, Character c, boolean prefix) {
        return fixLength(s, length, c, prefix, Boolean.TRUE);
    }
    /**
     * Fix the specified string with the specified length
     * and full-length with the specified {@link Character};
     * not overflow ({@link #fixLength(CharSequence, int, Character, boolean, boolean)})
     * and fill the prefix of string
     *
     * @param s to fix
     * @param length the length of new string. &lt;= 0 to not fix
     * @param c {@link Character} to fill. NULL for not fix
     *
     * @return the fixed length string
     */
    public static String fixPrefixLength(CharSequence s, int length, Character c) {
        return fixLength(s, length, c, Boolean.TRUE, Boolean.TRUE);
    }
    /**
     * Fix the specified string with the specified length
     * and full-length with the specified {@link Character};
     * not overflow ({@link #fixLength(CharSequence, int, Character, boolean, boolean)})
     * and fill the suffix of string
     *
     * @param s to fix
     * @param length the length of new string. &lt;= 0 to not fix
     * @param c {@link Character} to fill. NULL for not fix
     *
     * @return the fixed length string
     */
    public static String fixSuffixLength(CharSequence s, int length, Character c) {
        return fixLength(s, length, c, Boolean.TRUE, Boolean.TRUE);
    }
    /**
     * Fix the specified string with the specified length
     * and full-length with the specified {@link Character}
     *
     * @param s to fix
     * @param length the length of new string. &lt;= 0 to not fix
     * @param c {@link Character} to fill. NULL for not fix
     * @param prefix true for filling prefix of new string
     * @param overflow specify whether sub string from 0 index
     * if the specified string length is greater than the specified fixed-length.
     * true for not fix
     *
     * @return the fixed length string
     */
    public static String fixLength(String s, int length, Character c, boolean prefix, boolean overflow) {
        return fixLength((CharSequence) s, length, c, prefix, overflow);
    }
    /**
     * Fix the specified string with the specified length
     * and full-length with the specified {@link Character}
     * and not overflow ({@link #fixLength(CharSequence, int, Character, boolean, boolean)})
     *
     * @param s to fix
     * @param length the length of new string. &lt;= 0 to not fix
     * @param c {@link Character} to fill. NULL for not fix
     * @param prefix true for filling prefix of new string
     *
     * @return the fixed length string
     */
    public static String fixLength(String s, int length, Character c, boolean prefix) {
        return fixLength(s, length, c, prefix, Boolean.TRUE);
    }
    /**
     * Fix the specified string with the specified length
     * and full-length with the specified {@link Character};
     * not overflow ({@link #fixLength(CharSequence, int, Character, boolean, boolean)})
     * and fill the prefix of string
     *
     * @param s to fix
     * @param length the length of new string. &lt;= 0 to not fix
     * @param c {@link Character} to fill. NULL for not fix
     *
     * @return the fixed length string
     */
    public static String fixPrefixLength(String s, int length, Character c) {
        return fixLength(s, length, c, Boolean.TRUE, Boolean.TRUE);
    }
    /**
     * Fix the specified string with the specified length
     * and full-length with the specified {@link Character};
     * not overflow ({@link #fixLength(CharSequence, int, Character, boolean, boolean)})
     * and fill the suffix of string
     *
     * @param s to fix
     * @param length the length of new string. &lt;= 0 to not fix
     * @param c {@link Character} to fill. NULL for not fix
     *
     * @return the fixed length string
     */
    public static String fixSuffixLength(String s, int length, Character c) {
        return fixLength(s, length, c, Boolean.TRUE, Boolean.TRUE);
    }

    /**
     * Resolve all possible resource names list from the specified resource name
     *
     * @param prefix resolve with prefix
     * @param resource to resolve
     *
     * @return the possible resource names list
     */
    public static List<String> resolveResourceNames(String prefix, String resource) {
        // resolve resource path
        List<String> resourcePaths = new LinkedList<String>();
        if (hasText(prefix) && prefix.startsWith("/")) prefix = prefix.substring(1);
        if (hasText(prefix) && prefix.endsWith("/")) prefix = prefix.substring(0, prefix.length() - 1);
        prefix = (hasText(prefix) ? prefix + "/" : "");
        String nonePrefixResource = resource
                .replace(PREFIX_SUB_WIRECAST_CLASSPATH, "")
                .replace(PREFIX_SUB_CLASSPATH, "")
                .replace(PREFIX_WIRECAST_CLASSPATH, "")
                .replace(PREFIX_CLASSPATH, "");
        if (nonePrefixResource.startsWith("/")) {
            nonePrefixResource = nonePrefixResource.substring(1);
        }
        if (hasText(nonePrefixResource)) {
            resourcePaths.add(PREFIX_SUB_WIRECAST_CLASSPATH + "/" + prefix + nonePrefixResource);
            resourcePaths.add(PREFIX_SUB_CLASSPATH + "/" + prefix + nonePrefixResource);
            resourcePaths.add(PREFIX_WIRECAST_CLASSPATH + "/" + prefix + nonePrefixResource);
            resourcePaths.add(PREFIX_CLASSPATH + "/" + prefix + nonePrefixResource);
            resourcePaths.add(PREFIX_WIRECAST_CLASSPATH + prefix + nonePrefixResource);
            resourcePaths.add(PREFIX_CLASSPATH + prefix + nonePrefixResource);
            resourcePaths.add("/" + prefix + nonePrefixResource);
            resourcePaths.add(prefix + nonePrefixResource);
            if (StringUtils.hasText(prefix)) {
                resourcePaths.addAll(resolveResourceNames(nonePrefixResource));
            }
        }
        return resourcePaths;
    }
    /**
     * Resolve all possible resource names list from the specified resource name
     *
     * @param resource to resolve
     *
     * @return the possible resource names list
     */
    public static List<String> resolveResourceNames(String resource) {
        return resolveResourceNames(null, resource);
    }
}
