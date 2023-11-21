/*
 * @(#)NumberTenthInWordsProcessor.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords.i18n.en;

import org.nlh4j.core.text.numberinwords.AbstractNumberTenthInWordsProcessor;
import org.nlh4j.core.text.numberinwords.AbstractNumberUnitInWordsProcessor;
import org.nlh4j.core.text.numberinwords.ScaleUnit;

/**
 * An extended class of {@link AbstractNumberTenthInWordsProcessor} for English language
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
	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.util.text.numberinwords.AbstractNumberTenthInWordsProcessor#getWordsUnionSeparator()
	 */
	@Override
	protected String getWordsUnionSeparator() {
		return NumberInWordsConstants.NUMBER_IN_WORDS_UNION_SEPARATOR;
	}

	@Override
	protected int getLowerLimitTenth() {
		return 20;
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
}
