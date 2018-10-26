/*
 * @(#)NumberHundredInWordsProcessor.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords.i18n.en;

import org.nlh4j.core.text.numberinwords.AbstractNumberHundredInWordsProcessor;
import org.nlh4j.core.text.numberinwords.AbstractNumberTenthInWordsProcessor;
import org.nlh4j.core.text.numberinwords.AbstractNumberUnitInWordsProcessor;
import org.nlh4j.core.text.numberinwords.ScaleUnit;

/**
 * An extended class of {@link AbstractNumberHundredInWordsProcessor} for English language
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class NumberHundredInWordsProcessor extends AbstractNumberHundredInWordsProcessor {

	/** default serial uid **/
	private static final long serialVersionUID = 1L;

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
}
