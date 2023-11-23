/*
 * @(#)NumberUnitInWordsProcessor.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords.i18n.en;

import org.nlh4j.core.text.numberinwords.AbstractNumberUnitInWordsProcessor;
import org.nlh4j.core.text.numberinwords.ScaleUnit;

/**
 * An extended class of {@link AbstractNumberUnitInWordsProcessor} for English language
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class NumberUnitInWordsProcessor extends AbstractNumberUnitInWordsProcessor {

	/** default serial uid **/
	private static final long serialVersionUID = 1L;

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
}
