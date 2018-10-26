/*
 * @(#)AbstractNumberTenthInWordsProcessor.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.StringUtils;

import reactor.util.Assert;

/**
 * Number tenth processor to convert number into words (number from 20 to 99)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public abstract class AbstractNumberTenthInWordsProcessor extends AbstractNumberInWordsProcessor {

	/** default serial uid **/
	private static final long serialVersionUID = 1L;

	/**
	 * Get the number tenth in words definition array
	 * (such as: 20 - twenty, 30 - thirty, 40 - forty, 50 - fifty, 60 - sixty, 70 - seventy, 80 - eighty, 90 - ninety in en-US locale)
	 *
	 * @return the number tenth in words definition array
	 */
	protected abstract String[] getTenthTokens();
	/**
	 * Get the word union separator (such as twenty-one (separator is '-') in en-US locale)
	 *
	 * @return the word union separator
	 */
	protected String getWordsUnionSeparator() {
		return null;
	}
	/**
	 * Get the lower limit of number tenth
	 *
	 * @return the lower limit of number tenth
	 */
	protected abstract int getLowerLimitTenth();

	/**
	 * The definition number unit processor
	 */
	protected abstract AbstractNumberUnitInWordsProcessor getNumberUnitProcessor();

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.util.text.AbstractNumberInWordsProcessor#getName(java.lang.String)
	 */
	@Override
	public String getName(String value) {
		// check valid number unit processor
		AbstractNumberUnitInWordsProcessor numberUnitProcessor = this.getNumberUnitProcessor();
		Assert.notNull(numberUnitProcessor);
		int lowerLimit = this.getLowerLimitTenth();
		Assert.isTrue(lowerLimit >= 10 && ((lowerLimit % 10) == 0),
				"The lower limit of number tenth must be equals or greater than 10 and (% 10) = 0");

		// check valid tokens
		String[] tokens = this.getTenthTokens();
		if (ArrayUtils.isEmpty(tokens)) return "";

		// convert into words
		StringBuilder buffer = new StringBuilder();
		boolean tensFound = false;

		// parse number
		int number;
		String sep = this.getWordsUnionSeparator();
		if (value.length() > 3) {
			number = Integer.valueOf(value.substring(value.length() - 3), 10);
		}
		else {
			number = Integer.valueOf(value, 10);
		}
		// keep only two digits
		number %= 100;

		// process number
		if (number >= lowerLimit) {
			int idx = ((number / 10) - (lowerLimit / 10));
			if (0 <= idx && idx < tokens.length) {
				buffer.append(tokens[idx]);
				number %= 10;
				tensFound = true;
			}
		}
		else {
			number %= lowerLimit;
		}
		if (number != 0) {
			if (tensFound) {
				if (StringUtils.hasText(sep)) buffer.append(sep);
				else buffer.append(WORDS_SEPARATOR);
			}
			buffer.append(numberUnitProcessor.getName(number));
		}

		// number in words
		return buffer.toString();
	}
}
