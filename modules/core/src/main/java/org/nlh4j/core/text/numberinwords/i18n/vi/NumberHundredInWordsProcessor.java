/*
 * @(#)NumberHundredInWordsProcessor.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords.i18n.vi;

import org.springframework.util.StringUtils;

import java.util.Objects;

import org.nlh4j.core.text.numberinwords.AbstractNumberHundredInWordsProcessor;
import org.nlh4j.core.text.numberinwords.AbstractNumberTenthInWordsProcessor;
import org.nlh4j.core.text.numberinwords.AbstractNumberUnitInWordsProcessor;
import org.nlh4j.core.text.numberinwords.ScaleUnit;

/**
 * An extended class of {@link AbstractNumberHundredInWordsProcessor} for Vietnamese language
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class NumberHundredInWordsProcessor extends AbstractNumberHundredInWordsProcessor {

	/** default serial uid **/
	private static final long serialVersionUID = 1L;

	/**
	 * Specify whether should add words union separator
	 */
	private boolean useWordsUnionSeparator = false;

	/**
	 * Get a boolean value indicating should add words union separator
	 * @return true for adding; else false
	 */
	public boolean isUseWordsUnionSeparator() {
		return useWordsUnionSeparator;
	}
	/**
	 * Set a boolean value indicating should add words union separator
	 * @param useWordsUnionSeparator true for adding; else false
	 */
	public void setUseWordsUnionSeparator(boolean useWordsUnionSeparator) {
		this.useWordsUnionSeparator = useWordsUnionSeparator;
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.util.text.numberinwords.AbstractNumberHundredInWordsProcessor#getNumberUnitProcessor()
	 */
	@Override
	protected AbstractNumberUnitInWordsProcessor getNumberUnitProcessor() {
		return new NumberUnitInWordsProcessor();
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.util.text.numberinwords.AbstractNumberHundredInWordsProcessor#getNumberTenthProcessor()
	 */
	@Override
	protected AbstractNumberTenthInWordsProcessor getNumberTenthProcessor() {
		return new NumberTenthInWordsProcessor();
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
	 * @see org.nlh4j.common.util.NumberInWordsUtils.AbstractProcessor#getName(java.lang.String)
	 */
	@Override
	public String getName(String value) {
		// check valid number processors
		AbstractNumberUnitInWordsProcessor numberUnitProcessor =
				Objects.requireNonNull(this.getNumberUnitProcessor(), "numberUnitProcessor");
		AbstractNumberTenthInWordsProcessor numberTenthProcessor =
				Objects.requireNonNull(this.getNumberTenthProcessor(), "numberTenthProcessor");

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
		boolean shouldAddWordsUnion = (number >= 100 || (value.length() >= 3 && value.charAt(1) == '0'));
		if (number >= 100) {
			buffer.append(numberUnitProcessor.getName(number / 100));
			buffer.append(WORDS_SEPARATOR);
			buffer.append(super.getScaleName(EXPONENT, this.getScaleNameDefinitionIndex()));
		}
		else if (value.length() >= 3 &&  value.charAt(0) == '0' && value.charAt(1) != '0') {
			buffer.append(NumberInWordsConstants.NUMBER_ZERO_TOKEN);
			buffer.append(WORDS_SEPARATOR);
			buffer.append(super.getScaleName(EXPONENT, this.getScaleNameDefinitionIndex()));
			buffer.append(WORDS_SEPARATOR);
		}

		// process number tenth (10-20)
		((NumberTenthInWordsProcessor) numberTenthProcessor).setUseWordsUnionSeparator(
				this.isUseWordsUnionSeparator() || shouldAddWordsUnion);
		String tensName = numberTenthProcessor.getName(number % 100);
		if (StringUtils.hasText(tensName) && (number >= 100)) {
			buffer.append(WORDS_SEPARATOR);
		}
		buffer.append(tensName);

		// number in words
		return buffer.toString();
	}
}
