/*
 * @(#)CustomNumberEditor.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto.bindeditor;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;
import org.nlh4j.util.BeanUtils;

/**
 * <ul>
 *     <li><code>java.lang.Byte</code></li>
 *     <li><code>java.lang.Short</code></li>
 *     <li><code>java.lang.Integer</code></li>
 *     <li><code>java.lang.Long</code></li>
 *     <li><code>java.lang.Float</code></li>
 *     <li><code>java.lang.Double</code></li>
 *     <li><code>java.math.BigDecimal</code></li>
 *     <li><code>java.math.BigInteger</code></li>
 * </ul>
 * データ型エディタ
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public class CustomNumberEditor extends PropertyEditorSupport implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /** number class for serialization/de-serialization */
	@Getter
	@Setter
	private Class<? extends Number> numberClass;

	/** number format information for serialization/de-serialization */
	@Getter
	@Setter
	private NumberFormat numberFormat;

	/** number format pattern for serialization/de-serialization */
	@Getter
	@Setter
	private String pattern;

	/** specify the number whether allows empty/null */
	@Getter
	@Setter
	private boolean allowEmpty;

	/**
	 * Create a new CustomNumberEditor instance, using the default
	 * {@code valueOf} methods for parsing and {@code toString}
	 * methods for rendering.
	 * <p>The "allowEmpty" parameter states if an empty String should
	 * be allowed for parsing, i.e. get interpreted as {@code null} value.
	 * Else, an IllegalArgumentException gets thrown in that case.
	 * @param numberClass Number subclass to generate
	 * @param allowEmpty if empty strings should be allowed
	 * @throws IllegalArgumentException if an invalid numberClass has been specified
	 * @see org.springframework.util.NumberUtils#parseNumber(String, Class)
	 * @see Integer#valueOf
	 * @see Integer#toString
	 */
	public CustomNumberEditor(Class<? extends Number> numberClass, boolean allowEmpty) throws IllegalArgumentException {
		this(numberClass, NumberFormat.getCurrencyInstance(LocaleContextHolder.getLocale()), allowEmpty);
	}

	/**
	 * Create a new CustomNumberEditor instance, using the given NumberFormat
	 * for parsing and rendering.
	 * <p>The allowEmpty parameter states if an empty String should
	 * be allowed for parsing, i.e. get interpreted as {@code null} value.
	 * Else, an IllegalArgumentException gets thrown in that case.
	 * @param numberClass Number subclass to generate
	 * @param numberFormat NumberFormat to use for parsing and rendering
	 * @param allowEmpty if empty strings should be allowed
	 * @throws IllegalArgumentException if an invalid numberClass has been specified
	 * @see org.springframework.util.NumberUtils#parseNumber(String, Class, java.text.NumberFormat)
	 * @see java.text.NumberFormat#parse
	 * @see java.text.NumberFormat#format
	 */
	public CustomNumberEditor(Class<? extends Number> numberClass, NumberFormat numberFormat, boolean allowEmpty) throws IllegalArgumentException {
		if (numberClass == null || !Number.class.isAssignableFrom(numberClass)) {
			throw new IllegalArgumentException("Property class must be a subclass of Number");
		}
		this.numberClass = numberClass;
		this.numberFormat = numberFormat;
		this.allowEmpty = allowEmpty;
	}
	/**
	 * Create a new CustomNumberEditor instance, using the given NumberFormat
	 * for parsing and rendering.
	 * <p>The allowEmpty parameter states if an empty String should
	 * be allowed for parsing, i.e. get interpreted as {@code null} value.
	 * Else, an IllegalArgumentException gets thrown in that case.
	 *
	 * @param numberClass Number subclass to generate
	 * @param pattern number format pattern
	 * @param allowEmpty if empty strings should be allowed
	 *
	 * @throws IllegalArgumentException if an invalid numberClass has been specified
	 * @see org.springframework.util.NumberUtils#parseNumber(String, Class, java.text.NumberFormat)
	 * @see java.text.NumberFormat#parse
	 * @see java.text.NumberFormat#format
	 */
	public CustomNumberEditor(Class<? extends Number> numberClass, String pattern, boolean allowEmpty) throws IllegalArgumentException {
		if (numberClass == null || !Number.class.isAssignableFrom(numberClass)) {
			throw new IllegalArgumentException("Property class must be a subclass of Number");
		}
		this.numberClass = numberClass;
		this.pattern = pattern;
		this.allowEmpty = allowEmpty;
	}

	/**
	 * Format the Number as String, using the specified NumberFormat.
	 */
	@Override
	public String getAsText() {
		Object value = getValue();
		if (value == null) return "";
		if (this.getNumberFormat() != null) {
			// Use NumberFormat for rendering value.
			if (!StringUtils.hasText(this.getPattern())) {
				return this.getNumberFormat().format(value);
			}
			else {
				// TODO Always using group separator as comma (,) and decimal separator as dot (.)
				DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(LocaleContextHolder.getLocale());
				dfs.setDecimalSeparator('.');
				dfs.setGroupingSeparator(',');
				DecimalFormat df = new DecimalFormat(this.getPattern(), dfs);
				boolean resetBigDecimal = false;
				if (BigDecimal.class.equals(this.getNumberClass()) && !df.isParseBigDecimal()) {
					df.setParseBigDecimal(true);
					resetBigDecimal = true;
				}
				String fmtvalue = df.format(value);
				if (resetBigDecimal) {
					df.setParseBigDecimal(false);
				}
				return fmtvalue;
			}
		}
		else {
			// Use toString method for rendering value.
			return value.toString();
		}
	}

	/**
	 * Parse the Number from the given text, using the specified NumberFormat.
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.isAllowEmpty() && !StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
		}
		else {
			// Use given NumberFormat for parsing text.
			if (!StringUtils.hasText(this.getPattern()) && this.getNumberFormat() != null) {
				setValue(NumberUtils.parseNumber(text, this.getNumberClass(), this.getNumberFormat()));
			}
			else {
				DecimalFormat df = null;
				boolean resetBigDecimal = false;
				try {
					if (BeanUtils.isInstanceOf(this.getNumberFormat(), DecimalFormat.class)) {
						df = (DecimalFormat) this.getNumberFormat();
					}
					else {
						df = new DecimalFormat();
					}
					// TODO Always using group separator as comma (,) and decimal separator as dot (.)
					DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(LocaleContextHolder.getLocale());
					dfs.setDecimalSeparator('.');
					dfs.setGroupingSeparator(',');
					df.setDecimalFormatSymbols(dfs);
					if (StringUtils.hasText(this.getPattern())) df.applyPattern(this.getPattern());
					if (BigDecimal.class.equals(this.getNumberClass()) && !df.isParseBigDecimal()) {
						df.setParseBigDecimal(true);
						resetBigDecimal = true;
					}
					setValue(df.parse(text));
				}
				catch (Exception e) {
					throw new IllegalArgumentException(
							"Could not parse number: " + e.getMessage() + " by the specified pattern [" + this.getPattern() + "]", e);
				}
				finally {
					if (resetBigDecimal && df != null) {
						df.setParseBigDecimal(false);
					}
				}
			}
		}
	}
}
