/*
 * @(#)AbstractNumberInWordsProcessor.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Common number processor to convert number into words
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public abstract class AbstractNumberInWordsProcessor extends BaseAbstractNumberInWordsProcessor {

	/** default serial uid **/
	private static final long serialVersionUID = 1L;

	/**
	 * Get the number scale units array definition (such as 2 - hundred; 3 - thousand; etc...)
	 *
	 * @return the number scale units array definition
	 */
	protected abstract ScaleUnit[] getScaleUnits();
	/**
	 * Get the scale unit name index that has been defined in {@link AbstractNumberInWordsProcessor} getScaleUnits() function
	 *
	 * @return the scale unit name index that has been defined in {@link AbstractNumberInWordsProcessor} getScaleUnits() function
	 */
	public int getScaleNameDefinitionIndex() {
		return 0;
	}
	/**
	 * Get the scale unit word definition by the specified number exponent
	 *
	 * @param exponent the number exponent
	 * @param nameidx the index of name that has been defined in {@link ScaleUnit}
	 *
	 * @return the scale unit word definition by the specified number exponent
	 */
	public final String getScaleName(int exponent, int nameidx) {
		ScaleUnit[] units = this.getScaleUnits();
		String name = "";
		if (!ArrayUtils.isEmpty(units)) {
			for (ScaleUnit unit : units) {
				if (unit.getExponent() == exponent) {
					name = unit.getName(nameidx);
					break;
				}
			}
		}
		return name;
	}
	public final String getScaleName(int exponent) {
		return this.getScaleName(exponent, this.getScaleNameDefinitionIndex());
	}
}
