/*
 * @(#)CompositeBigNumberInWordsProcessor.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords.i18n.vi;

import org.nlh4j.core.text.numberinwords.AbstractCompositeBigNumberInWordsProcessor;
import org.nlh4j.core.text.numberinwords.AbstractNumberHundredInWordsProcessor;
import org.nlh4j.core.text.numberinwords.ScaleUnit;

/**
 * An extended class of {@link AbstractCompositeBigNumberInWordsProcessor} for Vietnamese language
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class CompositeBigNumberInWordsProcessor extends AbstractCompositeBigNumberInWordsProcessor {

	/** default serial uid **/
	private static final long serialVersionUID = 1L;

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.util.text.numberinwords.AbstractCompositeBigNumberInWordsProcessor#getNumberExponent()
	 */
	@Override
	protected int getNumberExponent() {
		return NumberInWordsConstants.SCALE_UNITS_MAX_EXPONENT;
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
	 * @see org.nlh4j.common.util.text.numberinwords.AbstractCompositeBigNumberInWordsProcessor#getNumberHundredProcessor()
	 */
	@Override
	protected AbstractNumberHundredInWordsProcessor getNumberHundredProcessor() {
		return new NumberHundredInWordsProcessor();
	}
}
