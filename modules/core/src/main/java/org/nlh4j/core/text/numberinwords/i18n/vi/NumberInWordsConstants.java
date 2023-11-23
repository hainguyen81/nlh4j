/*
 * @(#)NumberInWordsConstants.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords.i18n.vi;

import java.io.Serializable;

import org.nlh4j.core.text.numberinwords.ScaleUnit;

/**
 * The number in words definition (en-US locale)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public final class NumberInWordsConstants implements Serializable {

	/** default serial uid **/
	private static final long serialVersionUID = 1L;

	/**
	 * Number in words union separator definition
	 */
	public static final String NUMBER_IN_WORDS_UNION_SEPARATOR = "lẻ";

	/**
	 * Number negative signal in word definition
	 */
	public static final String NUMBER_NEGATIVE_SIGNAL_IN_WORD = "trừ";

	/**
	 * Number zero (0) token definition
	 */
	public static final String NUMBER_ZERO_TOKEN = "không";
	public static final String NUMBER_ONE_TOKEN = "mốt";
	public static final String NUMBER_FIVE_TOKEN = "lăm";

	/**
	 * Number unit tokens definition
	 */
	public static final String[] NUMBER_UNIT_TOKENS = new String[] {
		"một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín"
	};

	/**
	 * Number tenth tokens definition
	 */
	public static final String[] NUMBER_TENTH_TOKENS = new String[] {
		"mười", "hai mươi", "ba mươi", "bốn mươi", "năm mươi", "sáu mươi", "bảy mươi", "tám mươi", "chín mươi"
	};

	/**
	 * See http://www.wordiq.com/definition/Names_of_large_numbers
	 */
	public static final int SCALE_UNITS_MAX_EXPONENT = 18;
	public static final ScaleUnit[] SCALE_UNITS = new ScaleUnit[] {
		new ScaleUnit(18, "nghìn triệu tỷ", "trăm nghìn tỷ"),
		new ScaleUnit(15, "trăm nghìn tỷ", "nghìn tỷ"),
		new ScaleUnit(12, "nghìn tỷ", "tỷ"),
		new ScaleUnit(9, "tỷ", "nghìn triệu"),
		new ScaleUnit(6, "triệu", "triệu"),
		new ScaleUnit(3, "nghìn", "nghìn"),
		new ScaleUnit(2, "trăm", "trăm"),
		new ScaleUnit(-1, "mười", "mười"),
		new ScaleUnit(-2, "trăm", "trăm"),
		new ScaleUnit(-3, "nghìn", "nghìn"),
		new ScaleUnit(-4, "mười nghìn", "mười nghìn")
	};
}
