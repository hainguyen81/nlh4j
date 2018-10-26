/*
 * @(#)LicenseTool.java 1.0 May 29, 2015 Copyright 2015 by GNU Lesser General Public License (LGPL).
 * All rights reserved.
 */
package org.nlh4j.license;

import java.io.Serializable;

/**
 * License Generation Tool
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public final class LicenseTool implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

	/**
	 * Main
	 *
	 * @param args arguments
	 */
	public static void main(String[] args) {
		LicenseGenerator.generate(args);
	}
}