/*
 * @(#)NumberUnitInWordsProcessor.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords.i18n.vi;

import org.apache.commons.lang3.ArrayUtils;

import org.nlh4j.core.text.numberinwords.AbstractNumberUnitInWordsProcessor;
import org.nlh4j.core.text.numberinwords.ScaleUnit;

/**
 * An extended class of {@link AbstractNumberUnitInWordsProcessor} for Vietnamese language
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class NumberUnitInWordsProcessor extends AbstractNumberUnitInWordsProcessor {

	/** default serial uid **/
	private static final long serialVersionUID = 1L;

	/**
	 * Specify whether using special characters in Vietnamese language
	 */
	private boolean specialCase = false;

	/**
	 * Get a boolean value indicating whether using special characters in Vietnamese language
	 * @return true for using; else false
	 */
	public boolean isSpecialCase() {
		return specialCase;
	}
	/**
	 * Set a boolean value indicating whether using special characters in Vietnamese language
	 * @param specialCase true for using; else false
	 */
	public void setSpecialCase(boolean specialCase) {
		this.specialCase = specialCase;
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.util.text.numberinwords.AbstractNumberUnitInWordsProcessor#getUnitTokens()
	 */
	@Override
	protected String[] getUnitTokens() {
		return NumberInWordsConstants.NUMBER_UNIT_TOKENS;
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.util.text.numberinwords.AbstractNumberInWordsProcessor#getScaleUnits()
	 */
	@Override
	protected ScaleUnit[] getScaleUnits() {
		return NumberInWordsConstants.SCALE_UNITS;
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.util.text.AbstractNumberInWordsProcessor#getName(java.lang.String)
	 */
	@Override
	public String getName(String value) {
		// check valid tokens
		String[] tokens = this.getUnitTokens();
		if (ArrayUtils.isEmpty(tokens)) return "";

		// convert into words
		StringBuilder buffer = new StringBuilder();

		// parse value to convert
		int offset = NO_VALUE;
		int number;
		if (value.length() > 3) {
			number = Integer.valueOf(value.substring(value.length() - 3), 10);
		}
		else {
			number = Integer.valueOf(value, 10);
		}

		// keep only one digit
		number %= 100;
		if (number < 10) {
			offset = (number % 10) - 1;
		}
		else if (number < 20) {
			offset = (number % 20) - 1;
		}

		// process number
		if (offset != NO_VALUE && 0 <= offset && offset < tokens.length) {
			if (this.isSpecialCase() && offset == 0) {
				buffer.append(NumberInWordsConstants.NUMBER_ONE_TOKEN);
			}
			else if (this.isSpecialCase() && offset == 4) {
				buffer.append(NumberInWordsConstants.NUMBER_FIVE_TOKEN);
			}
			else {
				buffer.append(tokens[offset]);
			}
		}

		// number in words
		return buffer.toString();
	}
}
