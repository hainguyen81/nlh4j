/*
 * @(#)LocaleUtils.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.util.WebUtils;

/**
 * <p>Operations to assist when working with a {@link Locale}.</p>
 *
 * <p>This class tries to handle <code>null</code> input gracefully.
 * An exception will not be thrown for a <code>null</code> input.
 * Each method documents its behaviour in more detail.</p>
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public class LocaleUtils implements Serializable {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Unmodifiable list of available locales.
	 */
    private static List<Locale> cAvailableLocaleList;
    /**
     * <p>Obtains an unmodifiable list of installed locales.</p>
     *
     * <p>This method is a wrapper around {@link Locale#getAvailableLocales()}.
     * It is more efficient, as the JDK method must create a new array each
     * time it is called.</p>
     *
     * @return the unmodifiable list of available locales
     */
    public static final List<Locale> getAvailableLocaleList() {
        if (CollectionUtils.isEmpty(cAvailableLocaleList)) {
            List<Locale> list = Arrays.asList(Locale.getAvailableLocales());
            cAvailableLocaleList = Collections.unmodifiableList(list);
        }
        synchronized (cAvailableLocaleList) {
            return cAvailableLocaleList;
        }
    }
    /**
     * Unmodifiable set of available locales.
     */
    private static Set<Locale> cAvailableLocaleSet;
    /**
     * Unmodifiable map of language locales by country.
     */
    private transient static Map<String, List<Locale>> cLanguagesByCountry = null;
    /**
     * Get the synchronized map of language locales by country
     * @return the synchronized map of language locales by country
     */
    private static final Map<String, List<Locale>> getLanguagesByCountry() {
        if (cLanguagesByCountry == null) {
            cLanguagesByCountry = Collections.synchronizedMap(new HashMap<String, List<Locale>>());
        }
        synchronized (cLanguagesByCountry) {
            return cLanguagesByCountry;
        }
    }
    /**
     * Unmodifiable map of country locales by language.
     */
    private transient static Map<String, List<Locale>> cCountriesByLanguage = null;
    /**
     * Get the synchronized map of country locales by language
     * @return the synchronized map of country locales by language
     */
    private static final Map<String, List<Locale>> getCountriesByLanguage() {
        if (cCountriesByLanguage == null) {
            cCountriesByLanguage = Collections.synchronizedMap(new HashMap<String, List<Locale>>());
        }
        synchronized (cCountriesByLanguage) {
            return cCountriesByLanguage;
        }
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Converts a String to a Locale.</p>
     *
     * <p>This method takes the string format of a locale and creates the
     * locale object from it.</p>
     *
     * <pre>
     *   LocaleUtils.toLocale("en")         = new Locale("en", "")
     *   LocaleUtils.toLocale("en_GB")      = new Locale("en", "GB")
     *   LocaleUtils.toLocale("en_GB_xxx")  = new Locale("en", "GB", "xxx")   (#)
     * </pre>
     *
     * <p>(#) The behaviour of the JDK variant constructor changed between JDK1.3 and JDK1.4.
     * In JDK1.3, the constructor upper cases the variant, in JDK1.4, it doesn't.
     * Thus, the result from getVariant() may vary depending on your JDK.</p>
     *
     * <p>This method validates the input strictly.
     * The language code must be lowercase.
     * The country code must be uppercase.
     * The separator must be an underscore.
     * The length must be correct.
     * </p>
     *
     * @param str  the locale String to convert, null returns null
     * @return a Locale, null if null input
     * @throws IllegalArgumentException if the string is an invalid format
     */
    public static Locale toLocale(String str) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len != 2 && len != 5 && len < 7) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        char ch0 = str.charAt(0);
        char ch1 = str.charAt(1);
        if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        if (len == 2) {
            return new Locale(str, "");
        } else {
            if (str.charAt(2) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            char ch3 = str.charAt(3);
            if (ch3 == '_') {
                return new Locale(str.substring(0, 2), "", str.substring(4));
            }
            char ch4 = str.charAt(4);
            if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (len == 5) {
                return new Locale(str.substring(0, 2), str.substring(3, 5));
            } else {
                if (str.charAt(5) != '_') {
                    throw new IllegalArgumentException("Invalid locale format: " + str);
                }
                return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
            }
        }
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Obtains the list of locales to search through when performing
     * a locale search.</p>
     *
     * <pre>
     * localeLookupList(Locale("fr","CA","xxx"))
     *   = [Locale("fr","CA","xxx"), Locale("fr","CA"), Locale("fr")]
     * </pre>
     *
     * @param locale  the locale to start from
     * @return the unmodifiable list of Locale objects, 0 being locale, never null
     */
    public static List<Locale> localeLookupList(Locale locale) {
        return localeLookupList(locale, locale);
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Obtains the list of locales to search through when performing
     * a locale search.</p>
     *
     * <pre>
     * localeLookupList(Locale("fr", "CA", "xxx"), Locale("en"))
     *   = [Locale("fr","CA","xxx"), Locale("fr","CA"), Locale("fr"), Locale("en"]
     * </pre>
     *
     * <p>The result list begins with the most specific locale, then the
     * next more general and so on, finishing with the default locale.
     * The list will never contain the same locale twice.</p>
     *
     * @param locale  the locale to start from, null returns empty list
     * @param defaultLocale  the default locale to use if no other is found
     * @return the unmodifiable list of Locale objects, 0 being locale, never null
     */
    public static List<Locale> localeLookupList(Locale locale, Locale defaultLocale) {
        List<Locale> list = new ArrayList<Locale>(4);
        if (locale != null) {
            list.add(locale);
            if (locale.getVariant().length() > 0) {
                list.add(new Locale(locale.getLanguage(), locale.getCountry()));
            }
            if (locale.getCountry().length() > 0) {
                list.add(new Locale(locale.getLanguage(), ""));
            }
            if (list.contains(defaultLocale) == false) {
                list.add(defaultLocale);
            }
        }
        return Collections.unmodifiableList(list);
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Obtains an unmodifiable set of installed locales.</p>
     *
     * <p>This method is a wrapper around {@link Locale#getAvailableLocales()}.
     * It is more efficient, as the JDK method must create a new array each
     * time it is called.</p>
     *
     * @return the unmodifiable set of available locales
     */
    public static Set<Locale> getAvailableLocaleSet() {
        Set<Locale> set = cAvailableLocaleSet;
        if (set == null) {
            set = new HashSet<Locale>(getAvailableLocaleList());
            set = Collections.unmodifiableSet(set);
            cAvailableLocaleSet = set;
        }
        return set;
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Checks if the locale specified is in the list of available locales.</p>
     *
     * @param locale the Locale object to check if it is available
     * @return true if the locale is a known locale
     */
    public static boolean isAvailableLocale(Locale locale) {
        return getAvailableLocaleList().contains(locale);
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Obtains the list of languages supported for a given country.</p>
     *
     * <p>This method takes a country code and searches to find the
     * languages available for that country. Variant locales are removed.</p>
     *
     * @param countryCode  the 2 letter country code, null returns empty
     * @return an unmodifiable List of Locale objects, never null
     */
    public static List<Locale> languagesByCountry(String countryCode) {
        Map<String, List<Locale>> cLanguagesByCountry = getLanguagesByCountry();
        List<Locale> langs = cLanguagesByCountry.get(countryCode);  //syncd
        if (langs == null || langs.size() <= 0) {
            if (countryCode != null) {
                langs = new ArrayList<Locale>();
                List<Locale> locales = getAvailableLocaleList();
                for (int i = 0; i < locales.size(); i++) {
                    Locale locale = locales.get(i);
                    if (countryCode.equals(locale.getCountry()) &&
                            locale.getVariant().length() == 0) {
                        langs.add(locale);
                    }
                }
                langs = Collections.unmodifiableList(langs);
            } else {
                langs = new LinkedList<Locale>();
            }
            cLanguagesByCountry.put(countryCode, langs);  //syncd
        }
        return langs;
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Obtains the list of countries supported for a given language.</p>
     *
     * <p>This method takes a language code and searches to find the
     * countries available for that language. Variant locales are removed.</p>
     *
     * @param languageCode  the 2 letter language code, null returns empty
     * @return an unmodifiable List of Locale objects, never null
     */
    public static List<Locale> countriesByLanguage(String languageCode) {
        Map<String, List<Locale>> cCountriesByLanguage = getCountriesByLanguage();
        List<Locale> countries = cCountriesByLanguage.get(languageCode);  //syncd
        if (countries == null || countries.size() <= 0) {
            if (languageCode != null) {
                countries = new ArrayList<Locale>();
                List<Locale> locales = getAvailableLocaleList();
                for (int i = 0; i < locales.size(); i++) {
                    Locale locale = locales.get(i);
                    if (languageCode.equals(locale.getLanguage()) &&
                            locale.getCountry().length() != 0 &&
                            locale.getVariant().length() == 0) {
                        countries.add(locale);
                    }
                }
                countries = Collections.unmodifiableList(countries);
            } else {
                countries = new LinkedList<Locale>();
            }
            cCountriesByLanguage.put(languageCode, countries);  //syncd
        }
        return countries;
    }

    /**
     * Resolve {@link Locale} from {@link LocaleResolver}, {@link HttpServletRequest} and {@link HttpServletResponse}
     *
     * @param localeResolver {@link LocaleResolver}
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param defaultLocale specify locale, after resolving, will be applied as default by {@link Locale#setDefault(Locale)} function
     *
     * @return resolved locale
     */
    public static Locale resolveLocale(
            LocaleResolver localeResolver,
            HttpServletRequest request, HttpServletResponse response,
            boolean defaultLocale) {
        // セッションのタイムアウト判定を行う。
        Locale locale = null;
        if (localeResolver != null) {
            // resolve locale
            locale = (request != null ? localeResolver.resolveLocale(request) : Locale.getDefault());
            if (locale != null) {
                // apply response locale
                if (response != null) response.setLocale(LocaleContextHolder.getLocale());
                // apply system default locale
                if (defaultLocale) Locale.setDefault(locale);
            }
        }
        return locale;
    }
    /**
     * Resolve {@link Locale} from {@link LocaleResolver}, {@link HttpServletRequest} and {@link HttpServletResponse}
     *
     * @param localeResolver {@link LocaleResolver}
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     *
     * @return resolved locale
     */
    public static Locale resolveLocale(
            LocaleResolver localeResolver,
            HttpServletRequest request, HttpServletResponse response) {
        return resolveLocale(localeResolver, request, response, Boolean.TRUE);
    }
    /**
     * Apply the specified {@link Locale} to cookie
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param locale {@link Locale}
     * @param cookieName cookie name
     */
    public static void applyCookieLocale(
            HttpServletRequest request, HttpServletResponse response,
            Locale locale, String cookieName) {
        if (StringUtils.hasText(cookieName) && request != null && response != null) {
            locale = (locale == null ? Locale.getDefault() : locale);
            // apply cookie locale
            Cookie cookie = WebUtils.getCookie(request, cookieName);
            if (cookie == null) {
                cookie = new Cookie(cookieName, locale.getLanguage());
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            else {
                cookie.setValue(locale.getLanguage());
                response.addCookie(cookie);
            }
        }
    }
}