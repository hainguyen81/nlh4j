/*
 * @(#)BaseAbstractNumberInWordsProcessor.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Base number processor to convert number into words
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public abstract class BaseAbstractNumberInWordsProcessor implements Serializable {

	/** default serial uid **/
	private static final long serialVersionUID = 1L;

	/**
	 * Words separator
	 */
	protected static final String WORDS_SEPARATOR = " ";
	protected static final int NO_VALUE = -1;

	/**
	 * Parse number digits
	 *
	 * @param value the number to parse
	 * @return number digits
	 */
	protected List<Integer> getDigits(long value) {
		ArrayList<Integer> digits = new ArrayList<Integer>();
		if (value == 0) {
			digits.add(0);
		}
		else {
			while (value > 0) {
				digits.add(0, (int) value % 10);
				value /= 10;
			}
		}
		return digits;
	}

	/**
	 * Get the number in words
	 *
	 * @param value the number to parse
	 *
	 * @return number in words
	 */
	public String getName(long value) {
		return getName(Long.toString(value));
	}
	/**
     * Get the number in words
     *
     * @param value the number to parse
     *
     * @return number in words
     */
    public String getName(int value) {
		return getName(Integer.toString(value));
	}
    /**
     * Get the number in words
     *
     * @param value the number to parse
     *
     * @return number in words
     */
    public String getName(float value) {
		return getName(Float.toString(value));
	}
    /**
     * Get the number in words
     *
     * @param value the number to parse
     *
     * @return number in words
     */
    public String getName(double value) {
		return getName(Double.toString(value));
	}

	/**
	 * Process number
	 *
	 * @param value the number to process
	 *
	 * @return number in words
	 */
	public abstract String getName(String value);
}
