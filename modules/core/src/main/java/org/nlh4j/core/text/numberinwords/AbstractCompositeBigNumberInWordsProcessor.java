/*
 * @(#)AbstractCompositeBigNumberInWordsProcessor.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * Composite big number processor to convert number into words (&gt; 999)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public abstract class AbstractCompositeBigNumberInWordsProcessor extends AbstractNumberInWordsProcessor {

	/** default serial uid **/
	private static final long serialVersionUID = 1L;

	/**
	 * The number exponent
	 */
	protected abstract int getNumberExponent();

	/**
	 * The definition number hundred processor
	 */
	protected abstract AbstractNumberHundredInWordsProcessor getNumberHundredProcessor();
	private AbstractNumberInWordsProcessor lowProcessor = null;

	/**
	 * Get the number unit processor (of high parts)
	 *
	 * @return the number unit processor (of high parts)
	 */
	protected final AbstractNumberInWordsProcessor getHighProcessor() {
		return this.getNumberHundredProcessor();
	}
	/**
	 * Get the number unit processor (of low parts).<br/>
	 * This function will be run recursively unless {@link AbstractCompositeBigNumberInWordsProcessor} getNumberExponent() &gt; 3
	 *
	 * @return the number unit processor (of low parts)
	 */
	protected final AbstractNumberInWordsProcessor getLowProcessor() {
		if (lowProcessor == null) {
			int exponent = this.getNumberExponent();
			if (exponent <= 3) {
				lowProcessor = this.getNumberHundredProcessor();
			}
			else {
				lowProcessor = new AbstractCompositeBigNumberInWordsProcessor() {

					/** default serial uid **/
					private static final long serialVersionUID = 1L;

					/*
					 * (Non-Javadoc)
					 * @see org.nlh4j.common.util.text.AbstractNumberInWordsProcessor#getScaleUnits()
					 */
					@Override
					protected ScaleUnit[] getScaleUnits() {
						return AbstractCompositeBigNumberInWordsProcessor.this.getScaleUnits();
					}

					/*
					 * (Non-Javadoc)
					 * @see org.nlh4j.common.util.text.AbstractCompositeBigNumberInWordsProcessor#getNumberHundredProcessor()
					 */
					@Override
					protected AbstractNumberHundredInWordsProcessor getNumberHundredProcessor() {
						return AbstractCompositeBigNumberInWordsProcessor.this.getNumberHundredProcessor();
					}

					/*
					 * (Non-Javadoc)
					 * @see org.nlh4j.common.util.text.AbstractCompositeBigNumberInWordsProcessor#getNumberExponent()
					 */
					@Override
					protected int getNumberExponent() {
						return (AbstractCompositeBigNumberInWordsProcessor.this.getNumberExponent() - 3);
					}
				};
			}
		}
		return lowProcessor;
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.common.util.NumberInWordsUtils.AbstractProcessor#getName(java.lang.String)
	 */
	@Override
	public String getName(String value) {
		// check valid number processors
		AbstractNumberHundredInWordsProcessor numberHundredProcessor =
				Objects.requireNonNull(this.getNumberHundredProcessor(), "numberHundredProcessor");

		// convert number into words
		StringBuilder buffer = new StringBuilder();
		int exponent = this.getNumberExponent();

		// check high or low parts
		String high, low;
		if (value.length() < exponent) {
			high = "";
			low = value;
		}
		else {
			int index = value.length() - exponent;
			high = value.substring(0, index);
			low = value.substring(index);
		}

		// process high/low parts of number
		String highName = this.getHighProcessor().getName(high);
		String lowName = this.getLowProcessor().getName(low);
		if (StringUtils.isNotBlank(highName)) {
			buffer.append(highName);
			buffer.append(WORDS_SEPARATOR);
			buffer.append(super.getScaleName(exponent, this.getScaleNameDefinitionIndex()));
			if (StringUtils.isNotBlank(lowName)) {
				buffer.append(WORDS_SEPARATOR);
			}
		}
		if (StringUtils.isNotBlank(lowName)) {
			buffer.append(lowName);
		}

		// number in words
		return buffer.toString();
	}
}
