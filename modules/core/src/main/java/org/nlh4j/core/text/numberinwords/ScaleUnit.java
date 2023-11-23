/*
 * @(#)ScaleUnit.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.text.numberinwords;

import java.io.Serializable;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

/**
 * Number scale unit definition
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public final class ScaleUnit implements Serializable {

	/** default serial uid **/
	private static final long serialVersionUID = 1L;

	/**
	 * The number exponent (to translate number to word)
	 */
	private int exponent;
	/**
	 * The number to word definition (such as 1 - one; 2 - two; 10 - ten, etc...)
	 */
	private String[] names;

	/**
	 * Initialize a new instance of {@link ScaleUnit}
	 *
	 * @param exponent exponent
	 * @param names unit names
	 */
	public ScaleUnit(int exponent, String... names) {
		Assert.notEmpty(names, "names");
		this.exponent = exponent;
		this.names = names;
	}

	/**
	 * Get the number exponent
	 *
	 * @return the number exponent
	 */
	public int getExponent() {
		return exponent;
	}

	/**
	 * Get the number to word definition
	 *
	 * @param index the definition inex
	 *
	 * @return the number to word definition
	 */
	public String getName(int index) {
		return (ArrayUtils.isEmpty(names) || index < 0 || index >= names.length ? "" : names[index]);
	}
}
