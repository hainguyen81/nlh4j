/*
 * @(#)AbstractNumberUnitInWordsProcessor.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Number unit processor to convert number into words (number from 0 to 19)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public abstract class AbstractNumberUnitInWordsProcessor extends AbstractNumberInWordsProcessor {

	/** default serial uid **/
	private static final long serialVersionUID = 1L;

	/**
	 * Get the number unit in words definition array
	 * (such as: 1 - one, 2 - two, 3 - three, 4 - four, etc..., 19 - nineteen in en-US local)
	 *
	 * @return the number unit in words definition array
	 */
	protected abstract String[] getUnitTokens();

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
			buffer.append(tokens[offset]);
		}

		// number in words
		return buffer.toString();
	}
}
