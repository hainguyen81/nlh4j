/*
 * @(#)TestLicenseUtils.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;

import org.junit.Test;

/**
 * Test class {@link LicenseUtils}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class TestLicenseUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;
    private static String PRIVATE_KEY;
    private static String PUBLIC_KEY;

    /**
     * Test {@link LicenseUtils#createLicense(String, String, String, String, String, String, String)}
     */
    @Test
    public void testCreateLicense() {
    	KeyPair keyPair = EncryptUtils.buildDsaRandomKeypair();
    	PRIVATE_KEY = EncryptUtils.base64encode(keyPair.getPrivate().getEncoded());
    	PUBLIC_KEY = EncryptUtils.base64encode(keyPair.getPublic().getEncoded());
        System.out.println("privateKey: " + PRIVATE_KEY);
        System.out.println("publicKey: " + PUBLIC_KEY);
        String serialKey = "Serial.Key";
        String licenseIn = "E:/licenseIn.lic";
        String licenseOut = "E:/licenseOut.lic";
        // create license
        LicenseUtils.createDsaLicense(PRIVATE_KEY, serialKey, licenseIn, null, licenseOut, null);
    }

    /**
     * Test {@link LicenseUtils#validateLicense(String, String, String, String, String)}
     */
    @Test
    public void testCheckLicense() {
        String serialKey = "Serial.Key";
        String licenseOut = "E:/licenseOut.lic";
        // re-check if ok
        LicenseUtils.validateDsaLicense(PUBLIC_KEY, licenseOut, serialKey, null);
    }

    /**
     * Test {@link LicenseUtils#createLicense(String, String, String, String, String, String, String)}
     */
    @Test
    public void testCreateLicenseFromKeyStore() {
    	String keyStore = "F:\\Java.Working\\17.Temp\\LicenseTool\\.keystore.jks";
    	String licenseIn = "F:\\Java.Working\\17.Temp\\LicenseTool\\license.lic";
    	String licenseOut = "F:\\Java.Working\\17.Temp\\LicenseTool\\license.out.lic";
    	String serialKey = "Serial.Key";
    	String keyStoreType = "JKS";
    	String keyStorePass = "01101981";
    	String alias = "org.nlh4j";
    	if (Files.exists(Paths.get(keyStore))) {
        	KeyPair keyPair = EncryptUtils.getKeyPairFrom(keyStore, keyStoreType, keyStorePass, keyStorePass, alias);
        	PRIVATE_KEY = EncryptUtils.base64encode(keyPair.getPrivate().getEncoded());
        	PUBLIC_KEY = EncryptUtils.base64encode(keyPair.getPublic().getEncoded());
            System.out.println("privateKey: " + PRIVATE_KEY);
            System.out.println("publicKey: " + PUBLIC_KEY);
            // create license
            if (LicenseUtils.createRsaLicense(PRIVATE_KEY, serialKey, licenseIn, null, licenseOut, null)) {

            	// re-check if ok
    	        if (!LicenseUtils.validateRsaLicense(PUBLIC_KEY, licenseOut, serialKey, null)) {
    	        	System.err.println("Could not validate lincense or invalid license!");
    	        } else System.out.println("License is valid!");

            } else System.err.println("Could not create lincense!");
    	} else {
    	    System.err.println(keyStore + " is not found!");
    	}
    }
}
