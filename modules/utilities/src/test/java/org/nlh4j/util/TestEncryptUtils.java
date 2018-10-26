/*
 * @(#)TestEncryptUtils.java 1.0 Nov 2, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import org.junit.Test;

/**
 * Test class {@link EncryptUtils}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class TestEncryptUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Test {@link EncryptUtils#buildPublicKey(byte[], String)}
     */
    @Test
    public void testPublicKey()
            throws InvalidKeySpecException, NoSuchAlgorithmException, NullPointerException {
    	String algorithm = "DSA";
    	String key = EncryptUtils.toHex("publicKey");
        PublicKey publicKey = EncryptUtils.buildPublicKey(key, algorithm);
        System.out.println(new String(publicKey.getEncoded()));
    }

    /**
     * Test {@link EncryptUtils#buildPrivateKey(byte[], String)}
     */
    @Test
    public void testPrivateKey()
            throws InvalidKeySpecException, NoSuchAlgorithmException, NullPointerException {
    	String algorithm = "DSA";
    	String key = EncryptUtils.toHex("privateKey");
        PrivateKey privateKey = EncryptUtils.buildPrivateKey(EncryptUtils.hexToByte(key), algorithm);
        System.out.println(new String(privateKey.getEncoded()));
    }

    /**
     * Test {@link EncryptUtils#buildRandomKeypair(String)}
     */
    @Test
    public void testRandomKeyPair()
            throws InvalidKeySpecException, NoSuchAlgorithmException, NullPointerException {
        KeyPair keyPair = EncryptUtils.buildDsaRandomKeypair();
        System.out.println("Private Key: " + new String(keyPair.getPrivate().getEncoded()));
        System.out.println("Public Key: " + new String(keyPair.getPrivate().getEncoded()));
    }

    /**
     * Test create public/private key, generate license and re-check it
     */
    @Test
    public void testLicense() {
        String password = null; //"vn.co.system.exex";
        String serialKey = "Serial";
        String licenseIn = "D:/licenseIn.lic";
        String licenseOut = "D:/licenseOut.lic";
        // generate key pair
        KeyPair keyPair = EncryptUtils.buildDsaRandomKeypair();
        boolean ok = (keyPair != null);
        if (ok) {
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            // save serial number to file
            ok = LicenseUtils.createLicense(privateKey, serialKey, licenseIn, null, licenseOut, password);

            // check license file
            if (ok) {
                ok = LicenseUtils.validateLicense(publicKey, licenseOut, serialKey, password);
            }
        }
        if (!ok) System.err.println("Failed!");
        else System.out.println("OK!");
    }
}
