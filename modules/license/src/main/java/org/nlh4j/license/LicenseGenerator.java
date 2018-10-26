/*
 * @(#)LicenseGenerator.java 1.0 Feb 24, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.license;

import java.io.File;
import java.io.Serializable;
import java.security.KeyPair;

import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.EncryptUtils;
import org.nlh4j.util.FileUtils;
import org.nlh4j.util.LicenseUtils;
import org.nlh4j.util.StringUtils;

/**
 * License Generator
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class LicenseGenerator implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Main
	 *
	 * @param args arguments
	 */
	public static void generate(String[] args) {
		/**
		 * Check parameters
		 */
		boolean validParams = CollectionUtils.isElementsNumberGreaterThan(args, 10, Boolean.TRUE);
		if (validParams) {
			for(int i = 0; i < args.length; i += 2) {
				// check license-in/keystore/keystore type/keystore password
				validParams = (("-f".equalsIgnoreCase(args[i].trim())
						|| "-ks".equalsIgnoreCase(args[i].trim())
						|| "-t".equalsIgnoreCase(args[i].trim())
						|| "-p".equalsIgnoreCase(args[i].trim())
						|| "-s".equalsIgnoreCase(args[i].trim())
						|| "-o".equalsIgnoreCase(args[i].trim())
						|| "-a".equalsIgnoreCase(args[i].trim())
						|| "-kp".equalsIgnoreCase(args[i].trim()))
						&& StringUtils.hasText(args[i + 1].trim()));
				if (!validParams) break;
			}
		}

		/**
		 * Usage
		 */
		if (!validParams) {
			StringBuilder usage = new StringBuilder();
			usage.append("Usage:\\n");
			usage.append("Command line (by following orders):\\n");
			usage.append("java LicenseTool.jar -f [...] -s [...] -ks [...] -t [...] -p [...] -o [...] -a [...] -kp [...]\\n");
			usage.append("Parameters:\\n");
			usage.append("	Required:\\n");
			usage.append("		-f	<license-in file path>\\n");
			usage.append("		-ks	<keystore file>\\n");
			usage.append("		-t	<keystore type, eg. JKS, PKCS12>\\n");
			usage.append("		-p	<keystore password>\\n");
			usage.append("		-s	<serial key (property key) from license-in file to encrypt>\\n");
			usage.append("	Optional:\\n");
			usage.append("		-o	<license-out file path, eg. empty or null for using license-in file path>\\n");
			usage.append("		-a	<keystore alias>\\n");
			usage.append("		-kp	<key password, eg. empty or null for using same keystore password>\\n");
			System.out.println(usage.toString());
			return;
		}

		/**
		 * Parse parameters
		 */
		String licenseIn = null;
		String serialKey = null;
		String keyStore = null;
		String keyStoreType = null;
		String keyStorePass = null;
		String licenseOut = null;
		String alias = null;
		String keyPass = null;
		for(int i = 0; i < args.length; i += 2) {
			// parse license-in
			if ("-f".equalsIgnoreCase(args[i].trim()) && !StringUtils.hasText(licenseIn)) {
				licenseIn = args[i + 1].trim();
			}
			// parse serial key
			if ("-s".equalsIgnoreCase(args[i].trim()) && !StringUtils.hasText(serialKey)) {
				serialKey = args[i + 1].trim();
			}
			// parse keystore
			if ("-ks".equalsIgnoreCase(args[i].trim()) && !StringUtils.hasText(keyStore)) {
				keyStore = args[i + 1].trim();
			}
			// parse keystore type
			if ("-t".equalsIgnoreCase(args[i].trim()) && !StringUtils.hasText(keyStoreType)) {
				keyStoreType = args[i + 1].trim();
			}
			// parse keystore password
			if ("-p".equalsIgnoreCase(args[i].trim()) && !StringUtils.hasText(keyStorePass)) {
				keyStorePass = args[i + 1].trim();
			}
			// parse license-out
			if ("-o".equalsIgnoreCase(args[i].trim()) && !StringUtils.hasText(licenseOut)) {
				licenseOut = args[i + 1].trim();
			}
			// parse alias
			if ("-a".equalsIgnoreCase(args[i].trim()) && !StringUtils.hasText(alias)) {
				alias = args[i + 1].trim();
			}
			// parse key password
			if ("-kp".equalsIgnoreCase(args[i].trim()) && !StringUtils.hasText(keyPass)) {
				keyPass = args[i + 1].trim();
			}
		}

		// check for license-in
		File licenseInFile = new File(licenseIn);
		if (!licenseInFile.exists()) {
			System.out.println("--> Could not found license-in file [" + licenseIn + "]!!!");
			return;
		}

		// check for keystore file
		File keyStoreFile = new File(keyStore);
		if (!keyStoreFile.exists()) {
			System.out.println("--> Could not found keystore file [" + keyStore + "]!!!");
			return;
		}

		// check for license-out
		licenseOut = (!StringUtils.hasText(licenseOut)
				? (licenseInFile.getParent() + "/" + FileUtils.getBaseFileName(licenseIn)
				+ ".out." + FileUtils.getFileExtension(licenseIn)) : licenseOut);

		// delete old file if existed
		FileUtils.safeDelete(licenseOut);

		// check key password
		keyPass = (!StringUtils.hasText(keyPass) ? keyStorePass : keyPass);

		// parse private key from keystore
		KeyPair keyPair = EncryptUtils.getKeyPairFrom(keyStoreFile, keyStoreType, keyStorePass, keyPass, alias);
		if (keyPair == null) {
			System.out.println("--> Could not load keystore for private key!!!");
			return;
		}

		// create license-out file
		try {
			String privateKey = EncryptUtils.base64encode(keyPair.getPrivate().getEncoded());
			String publicKey = EncryptUtils.base64encode(keyPair.getPublic().getEncoded());
			if (LicenseUtils.createRsaLicense(privateKey, serialKey, licenseIn, null, licenseOut, null)) {
				System.out.println("Private Key: " + privateKey);
				System.out.println("Public Key: " + publicKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
