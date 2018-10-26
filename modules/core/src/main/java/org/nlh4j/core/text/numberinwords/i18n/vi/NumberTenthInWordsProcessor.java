/*
 * @(#)NumberTenthInWordsProcessor.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords.i18n.vi;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.StringUtils;

import reactor.util.Assert;
import org.nlh4j.core.text.numberinwords.AbstractNumberTenthInWordsProcessor;
import org.nlh4j.core.text.numberinwords.AbstractNumberUnitInWordsProcessor;
import org.nlh4j.core.text.numberinwords.ScaleUnit;

/**
 * An extended class of {@link AbstractNumberTenthInWordsProcessor} for Vietnamese language
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class NumberTenthInWordsProcessor extends AbstractNumberTenthInWordsProcessor {

	/** default serial uid **/
	private static final long serialVersionUID = 1L;

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.util.text.numberinwords.AbstractNumberTenthInWordsProcessor#getTenthTokens()
	 */
	@Override
	protected String[] getTenthTokens() {
		return NumberInWordsConstants.NUMBER_TENTH_TOKENS;
	}

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
	 * @see org.nlh4j.common.util.text.numberinwords.AbstractNumberTenthInWordsProcessor#getWordsUnionSeparator()
	 */
	@Override
	protected String getWordsUnionSeparator() {
		return NumberInWordsConstants.NUMBER_IN_WORDS_UNION_SEPARATOR;
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.util.text.numberinwords.AbstractNumberTenthInWordsProcessor#getLowerLimitTenth()
	 */
	@Override
	protected int getLowerLimitTenth() {
		return 10;
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
	 * @see org.nlh4j.common.util.text.numberinwords.AbstractNumberTenthInWordsProcessor#getNumberUnitProcessor()
	 */
	@Override
	protected AbstractNumberUnitInWordsProcessor getNumberUnitProcessor() {
		return new NumberUnitInWordsProcessor();
	}

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
		boolean useSpecialUnion = false;
		boolean unitSpecialCase = false;
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
		useSpecialUnion = (number < lowerLimit);
		unitSpecialCase = (
				(number >= lowerLimit && ((number % lowerLimit) == 5))
				|| (number >= (2 * lowerLimit) && ((number % lowerLimit) == 1))
		);
		if (number >= lowerLimit) {
			int idx = ((number / 10) - (lowerLimit / 10));
			if (0 <= idx && idx < tokens.length) {
				buffer.append(tokens[idx]);
				buffer.append(WORDS_SEPARATOR);
				number %= 10;
				tensFound = true;
			}
		}
		else {
			number %= lowerLimit;
		}
		if (number != 0) {
			((NumberUnitInWordsProcessor) numberUnitProcessor).setSpecialCase(unitSpecialCase);
			String unitName = numberUnitProcessor.getName(number);
			if (!tensFound && StringUtils.hasText(unitName)
					&& this.isUseWordsUnionSeparator() && useSpecialUnion && StringUtils.hasText(sep)) {
				buffer.append(sep);
				buffer.append(WORDS_SEPARATOR);
			}
			buffer.append(unitName);
		}

		// number in words
		return buffer.toString();
	}
}
