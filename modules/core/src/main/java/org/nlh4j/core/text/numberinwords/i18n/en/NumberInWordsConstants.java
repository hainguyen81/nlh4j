/*
 * @(#)NumberInWordsConstants.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords.i18n.en;

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
	public static final String NUMBER_IN_WORDS_UNION_SEPARATOR = "-";

	/**
	 * Number negative signal in word definition
	 */
	public static final String NUMBER_NEGATIVE_SIGNAL_IN_WORD = "minus";

	/**
	 * Number in words union separator definition
	 */
	public static final String NUMBER_IN_WORDS_UNION_AND = "and";

	/**
	 * Number zero (0) token definition
	 */
	public static final String NUMBER_ZERO_TOKEN = "zero";

	/**
	 * Number unit tokens definition
	 */
	public static final String[] NUMBER_UNIT_TOKENS = new String[] {
		"one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
		"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
	};

	/**
	 * Number tenth tokens definition
	 */
	public static final String[] NUMBER_TENTH_TOKENS = new String[] {
		"twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
	};

	/**
	 * See http://www.wordiq.com/definition/Names_of_large_numbers
	 */
	public static final int SCALE_UNITS_MAX_EXPONENT = 63;
	public static final ScaleUnit[] SCALE_UNITS = new ScaleUnit[] {
			new ScaleUnit(63, "vigintillion", "decilliard"),
			new ScaleUnit(60, "novemdecillion", "decillion"),
			new ScaleUnit(57, "octodecillion", "nonilliard"),
			new ScaleUnit(54, "septendecillion", "nonillion"),
			new ScaleUnit(51, "sexdecillion", "octilliard"),
			new ScaleUnit(48, "quindecillion", "octillion"),
			new ScaleUnit(45, "quattuordecillion", "septilliard"),
			new ScaleUnit(42, "tredecillion", "septillion"),
			new ScaleUnit(39, "duodecillion", "sextilliard"),
			new ScaleUnit(36, "undecillion", "sextillion"),
			new ScaleUnit(33, "decillion", "quintilliard"),
			new ScaleUnit(30, "nonillion", "quintillion"),
			new ScaleUnit(27, "octillion", "quadrilliard"),
			new ScaleUnit(24, "septillion", "quadrillion"),
			new ScaleUnit(21, "sextillion", "trilliard"),
			new ScaleUnit(18, "quintillion", "trillion"),
			new ScaleUnit(15, "quadrillion", "billiard"),
			new ScaleUnit(12, "trillion", "billion"),
			new ScaleUnit(9, "billion", "milliard"),
			new ScaleUnit(6, "million", "million"),
			new ScaleUnit(3, "thousand", "thousand"),
			new ScaleUnit(2, "hundred", "hundred"),
			//new ScaleUnit(1, "ten", "ten"),
			//new ScaleUnit(0, "one", "one"),
			new ScaleUnit(-1, "tenth", "tenth"),
			new ScaleUnit(-2, "hundredth", "hundredth"),
			new ScaleUnit(-3, "thousandth", "thousandth"),
			new ScaleUnit(-4, "ten-thousandth", "ten-thousandth"),
			new ScaleUnit(-5, "hundred-thousandth", "hundred-thousandth"),
			new ScaleUnit(-6, "millionth", "millionth"),
			new ScaleUnit(-7, "ten-millionth", "ten-millionth"),
			new ScaleUnit(-8, "hundred-millionth", "hundred-millionth"),
			new ScaleUnit(-9, "billionth", "milliardth"),
			new ScaleUnit(-10, "ten-billionth", "ten-milliardth"),
			new ScaleUnit(-11, "hundred-billionth", "hundred-milliardth"),
			new ScaleUnit(-12, "trillionth", "billionth"),
			new ScaleUnit(-13, "ten-trillionth", "ten-billionth"),
			new ScaleUnit(-14, "hundred-trillionth", "hundred-billionth"),
			new ScaleUnit(-15, "quadrillionth", "billiardth"),
			new ScaleUnit(-16, "ten-quadrillionth", "ten-billiardth"),
			new ScaleUnit(-17, "hundred-quadrillionth", "hundred-billiardth"),
			new ScaleUnit(-18, "quintillionth", "trillionth"),
			new ScaleUnit(-19, "ten-quintillionth", "ten-trillionth"),
			new ScaleUnit(-20, "hundred-quintillionth", "hundred-trillionth"),
			new ScaleUnit(-21, "sextillionth", "trilliardth"),
			new ScaleUnit(-22, "ten-sextillionth", "ten-trilliardth"),
			new ScaleUnit(-23, "hundred-sextillionth", "hundred-trilliardth"),
			new ScaleUnit(-24, "septillionth", "quadrillionth"),
			new ScaleUnit(-25, "ten-septillionth", "ten-quadrillionth"),
			new ScaleUnit(-26, "hundred-septillionth", "hundred-quadrillionth"),
	};
}
