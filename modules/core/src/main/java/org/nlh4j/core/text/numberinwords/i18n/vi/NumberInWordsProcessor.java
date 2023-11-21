/*
 * @(#)NumberInWordsProcessor.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords.i18n.vi;

import org.apache.commons.lang3.StringUtils;
import org.nlh4j.core.text.numberinwords.AbstractCompositeBigNumberInWordsProcessor;
import org.nlh4j.core.text.numberinwords.BaseAbstractNumberInWordsProcessor;

/**
 * An extended class of {@link BaseAbstractNumberInWordsProcessor} for Vietnamese language
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class NumberInWordsProcessor extends BaseAbstractNumberInWordsProcessor {

	/** default serial uid **/
	private static final long serialVersionUID = 1L;

	/**
	 * The number in words processor
	 */
	private AbstractCompositeBigNumberInWordsProcessor numberProcessor = new CompositeBigNumberInWordsProcessor();

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.util.text.numberinwords.BaseAbstractNumberInWordsProcessor#getName(java.lang.String)
	 */
	@Override
	public String getName(String value) {
		boolean negative = false;
		// check negative
		if (value.startsWith("-")) {
			negative = true;
			value = value.substring(1);
		}

		// check decimal
		int decimals = value.indexOf(".");
		String decimalValue = null;
		if (0 <= decimals) {
			decimalValue = value.substring(decimals + 1);
			value = value.substring(0, decimals);
		}

		// process positive number part without negative and processor
		String name = numberProcessor.getName(value);
		if (name.isEmpty()) {
			name = NumberInWordsConstants.NUMBER_ZERO_TOKEN;
		}
		else if (negative) {
			name = NumberInWordsConstants.NUMBER_NEGATIVE_SIGNAL_IN_WORD
					.concat(WORDS_SEPARATOR)
					.concat(name);
		}

		// process decimal number part
		if (StringUtils.isNotBlank(decimalValue)) {
			name = name
					.concat(WORDS_SEPARATOR)
					.concat(numberProcessor.getName(decimalValue))
					.concat(WORDS_SEPARATOR)
					.concat(numberProcessor.getScaleName(-decimalValue.length()));
		}

		// number in words
		return name;
	}
}
