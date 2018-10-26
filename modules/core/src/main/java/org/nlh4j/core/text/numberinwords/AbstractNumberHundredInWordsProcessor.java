/*
 * @(#)AbstractNumberHundredInWordsProcessor.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords;

import reactor.util.Assert;


/**
 * Number hundred processor to convert number into words (number from 100 to 999)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public abstract class AbstractNumberHundredInWordsProcessor extends AbstractNumberInWordsProcessor {

	/** default serial uid **/
	private static final long serialVersionUID = 1L;

	/**
	 * Number exponent for hundred
	 */
	protected final int EXPONENT = 2;

	/**
	 * The definition number unit processor
	 */
	protected abstract AbstractNumberUnitInWordsProcessor getNumberUnitProcessor();
	/**
	 * The definition number tenth processor
	 */
	protected abstract AbstractNumberTenthInWordsProcessor getNumberTenthProcessor();

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.util.NumberInWordsUtils.AbstractProcessor#getName(java.lang.String)
	 */
	@Override
	public String getName(String value) {
		// check valid number processors
		AbstractNumberUnitInWordsProcessor numberUnitProcessor = this.getNumberUnitProcessor();
		Assert.notNull(numberUnitProcessor);
		AbstractNumberTenthInWordsProcessor numberTenthProcessor = this.getNumberTenthProcessor();
		Assert.notNull(numberTenthProcessor);

		// convert number into words
		StringBuilder buffer = new StringBuilder();

		// parse number
		int number;
		if (value.isEmpty()) {
			number = 0;
		}
		else if (value.length() > 4) {
			number = Integer.valueOf(value.substring(value.length() - 4), 10);
		}
		else {
			number = Integer.valueOf(value, 10);
		}
		// keep at least three digits
		number %= 1000;

		// process number unit
		if (number >= 100) {
			buffer.append(numberUnitProcessor.getName(number / 100));
			buffer.append(WORDS_SEPARATOR);
			buffer.append(super.getScaleName(EXPONENT, this.getScaleNameDefinitionIndex()));
		}

		// process number tenth (10-20)
		String tensName = numberTenthProcessor.getName(number % 100);
		if (!tensName.isEmpty() && (number >= 100)) {
			buffer.append(WORDS_SEPARATOR);
		}
		buffer.append(tensName);

		// number in words
		return buffer.toString();
	}
}
