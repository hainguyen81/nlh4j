/*
 * @(#)LicenseUtils.java 1.0 Oct 17, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.File;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * License utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class LicenseUtils implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;

    /*************************************************
     * VALIDATE LICENSE DATA
     *************************************************/

    /**
     * Validate license file with the specified public key
     *
     * @param publicKey public key to validate
     * @param algorithm signature algorithm method
     * @param serialKey serial property key to get license key from license file
     * @param licenseFile license file
     * @param password that has been used to encrypted information
     *
     * @return license data if valid; else null/empty if failed
     * @throws IllegalArgumentException
     *      throw if file name is empty or public/serial key is null
     */
    private static Map<String, Object> validate(PublicKey publicKey, String algorithm, String licenseFile, String serialKey, String password) {
        Assert.hasText(licenseFile, "license file");
        Assert.hasText(serialKey, "serial key");
        Assert.notNull(publicKey, "key");
        PropertyUtils licenseProp = null;
        Map<String, Object> data = null;
        File licensFile = new File(licenseFile);
        if (licensFile.exists()) {
            try {
                // load properties
                licenseProp = new PropertyUtils(licenseFile, password);
                // parse license data
                StringBuffer licenseInfo = new StringBuffer();
                data = licenseProp.getPropertiesMap(null);
                Assert.notEmpty(data, "Invalid license data");
                Assert.isTrue(data.containsKey(serialKey), "Invalid license serial");
                // parse signature
                String signature = String.valueOf(data.get(serialKey));
                Assert.hasLength(signature, "Invalid license serial");
                // convert license data to bytes
                data.remove(serialKey);
                for(final Iterator<String> it = data.keySet().iterator(); it.hasNext();) {
                    String key = it.next();
                    licenseInfo.append(String.valueOf(data.get(key)));
                }
                // check license data with serial number
                byte[] licenseData = StringUtils.toByte(licenseInfo.toString());
                byte[] signatureData = EncryptUtils.hexToByte(signature);
                Assert.isTrue(!CollectionUtils.isEmpty(licenseData), "Invalid license data");
                Assert.isTrue(!CollectionUtils.isEmpty(signatureData), "Invalid license serial");
                // verify license
                if (EncryptUtils.verifySignature(publicKey, algorithm, licenseData, signatureData)) {
                    return data;
                }
                else Assert.isTrue(false, "Invalid license");
            }
            catch (Exception e) {
                e.printStackTrace();
                if (!CollectionUtils.isEmpty(data)) {
                    data.clear();
                }
            }
        }
        return data;
    }

    /*************************************************
     * CHECK LICENSE
     *************************************************/

    /**
     * Check the specified license file and return license data
     *
     * @param publicKey public key to decrypt
     * @param licenseFile license properties file
     * @param serialKey serial property key
     * @param password that has been used to encrypted information
     *
     * @return license data or NULL if failed
     */
    public static Map<String, Object> checkLicense(PublicKey publicKey, String licenseFile, String serialKey, String password) {
    	return (publicKey != null
    			? checkLicense(publicKey, publicKey.getAlgorithm(), serialKey, password)
    					: new LinkedHashMap<String, Object>());
    }
    /**
     * Check the specified license file and return license data
     *
     * @param publicKey public key to decrypt
     * @param algorithm algorithm method
     * @param licenseFile license properties file
     * @param serialKey serial property key
     * @param password that has been used to encrypted information
     *
     * @return license data or NULL if failed
     */
    public static Map<String, Object> checkLicense(PublicKey publicKey, String algorithm, String licenseFile, String serialKey, String password) {
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        try {
            data = validate(publicKey, algorithm, licenseFile, serialKey, password);
        } catch (Exception e) {
            e.printStackTrace();
            data.clear();
        }
        return data;
    }
    /**
     * Check the specified license file and return license data
     *
     * @param publicKey public key to decrypt
     * @param keyAlgorithm key algorithm method
     * @param licenseFile license properties file
     * @param serialKey serial property key
     * @param signatureAlgorithm signature algorithm method
     * @param password that has been used to encrypted information
     *
     * @return license data or NULL if failed
     */
    public static Map<String, Object> checkLicense(
            String publicKey, String keyAlgorithm,
            String serialKey, String signatureAlgorithm,
            String licenseFile, String password) {
        try {
            return validate(
                    EncryptUtils.buildPublicKey(publicKey, keyAlgorithm),
                    signatureAlgorithm, licenseFile, serialKey, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Check the specified license file and return license data with DSA provider and DSA signature algorithm method
     *
     * @param publicKey public key to decrypt
     * @param licenseFile license properties file
     * @param serialKey serial property key
     * @param password that has been used to encrypted information
     *
     * @return license data or NULL if failed
     */
    public static Map<String, Object> checkDsaLicense(String publicKey, String licenseFile, String serialKey, String password) {
        return checkLicense(
        		publicKey, EncryptUtils.ENCRYPT_PROVIDER_DSA,
        		serialKey, EncryptUtils.ENCRYPT_PROVIDER_DSA,
        		licenseFile, password);
    }
    /**
     * Check the specified license file and return license data with RSA provider and SHA256withRSA signature algorithm method
     *
     * @param publicKey public key to decrypt
     * @param licenseFile license properties file
     * @param serialKey serial property key
     * @param password that has been used to encrypted information
     *
     * @return license data or NULL if failed
     */
    public static Map<String, Object> checkRsaLicense(String publicKey, String licenseFile, String serialKey, String password) {
        return checkLicense(
        		publicKey, EncryptUtils.ENCRYPT_PROVIDER_RSA,
        		serialKey, EncryptUtils.ENCRYPT_PROVIDER_SHA256WITHRSA,
        		licenseFile, password);
    }

    /*************************************************
     * VALIDATE LICENSE
     *************************************************/

    /**
     * Check the specified license file and return license data
     *
     * @param publicKey public key to decrypt
     * @param keyAlgorithm key algorithm method
     * @param licenseFile license properties file
     * @param serialKey serial property key
     * @param signatureAlgorithm signature algorithm method
     * @param password that has been used to encrypted information
     *
     * @return true if license valid else false
     */
    public static boolean validateLicense(
            String publicKey, String keyAlgorithm,
            String serialKey, String signatureAlgorithm,
            String licenseFile, String password) {
    	return !CollectionUtils.isEmpty(checkLicense(
    			publicKey, keyAlgorithm,
    			serialKey, signatureAlgorithm,
    			licenseFile, password));
    }
    /**
     * Check the specified license file and return license data
     *
     * @param publicKey public key to decrypt
     * @param licenseFile license properties file
     * @param serialKey serial property key
     * @param signatureAlgorithm signature algorithm method
     * @param password that has been used to encrypted information
     *
     * @return true if license valid else false
     */
    public static boolean validateLicense(
    		PublicKey publicKey, String serialKey, String signatureAlgorithm,
    		String licenseFile, String password) {
        return !CollectionUtils.isEmpty(checkLicense(
        		publicKey, signatureAlgorithm, serialKey,
        		licenseFile, password));
    }
    /**
     * Check the specified license file and return license data
     *
     * @param publicKey public key to decrypt
     * @param licenseFile license properties file
     * @param serialKey serial property key
     * @param password that has been used to encrypted information
     *
     * @return true if license valid else false
     */
    public static boolean validateLicense(PublicKey publicKey, String licenseFile, String serialKey, String password) {
        return !CollectionUtils.isEmpty(checkLicense(publicKey, licenseFile, serialKey, password));
    }
    /**
     * Check the specified license file and return license data with DSA provider and DSA signature algorithm method
     *
     * @param publicKey public key to decrypt
     * @param licenseFile license properties file
     * @param serialKey serial property key
     * @param password that has been used to encrypted information
     *
     * @return true if license valid else false
     */
    public static boolean validateDsaLicense(String publicKey, String licenseFile, String serialKey, String password) {
        return validateLicense(
        		publicKey, EncryptUtils.ENCRYPT_PROVIDER_DSA,
        		serialKey, EncryptUtils.ENCRYPT_PROVIDER_DSA,
        		licenseFile, password);
    }
    /**
     * Check the specified license file and return license data with RSA provider and SHA256withRSA signature algorithm method
     *
     * @param publicKey public key to decrypt
     * @param licenseFile license properties file
     * @param serialKey serial property key
     * @param password that has been used to encrypted information
     *
     * @return true if license valid else false
     */
    public static boolean validateRsaLicense(String publicKey, String licenseFile, String serialKey, String password) {
        return validateLicense(
        		publicKey, EncryptUtils.ENCRYPT_PROVIDER_RSA,
        		serialKey, EncryptUtils.ENCRYPT_PROVIDER_SHA256WITHRSA,
        		licenseFile, password);
    }

    /*************************************************
     * CREATE LICENSE
     *************************************************/

    /**
     * Create license
     *
     * @param privateKey private key
     * @param serialKey serial property key to get license key from license file
     * @param licenseIn license information properties file
     * @param passwordIn password that has been used to encrypted source license information
     * @param licenseOut encrypted license information properties file to write-out
     * @param passwordOut password that has been used to encrypted destination license information
     *
     * @return true for successful; else false if failed
     */
    public static boolean createLicense(PrivateKey privateKey, String serialKey, String licenseIn, String passwordIn, String licenseOut, String passwordOut) {
    	Assert.notNull(privateKey, "private key");
    	return createLicense(privateKey, privateKey.getAlgorithm(), serialKey, licenseIn, passwordIn, licenseOut, passwordOut);
    }
    /**
     * Create license
     *
     * @param privateKey private key
     * @param algorithm signature algorithm method
     * @param serialKey serial property key to get license key from license file
     * @param licenseIn license information properties file
     * @param passwordIn password that has been used to encrypted source license information
     * @param licenseOut encrypted license information properties file to write-out
     * @param passwordOut password that has been used to encrypted destination license information
     *
     * @return true for successful; else false if failed
     */
    public static boolean createLicense(PrivateKey privateKey, String algorithm, String serialKey, String licenseIn, String passwordIn, String licenseOut, String passwordOut) {
    	Assert.hasText(licenseIn, "license file");
        Assert.hasText(serialKey, "serial key");
        Assert.notNull(privateKey, "private key");
    	PropertyUtils licenseInProp = null;
    	PropertyUtils licenseOutProp = null;
        Map<String, Object> data = null;
    	File fileIn = new File(licenseIn);
    	File fileOut = new File(licenseOut);
    	boolean ok = fileIn.exists();
    	if (ok) {
    		try {
    			// load properties
    			licenseInProp = new PropertyUtils(licenseIn, passwordIn);
                // parse license data
                StringBuffer licenseInfo = new StringBuffer();
                data = licenseInProp.getPropertiesMap(null);
                Assert.notEmpty(data, "Invalid license data");
                // Assert.isTrue(data.containsKey(serialKey), "Invalid license serial");
                // data.remove(serialKey);
                // convert license data to bytes
                for(final Iterator<String> it = data.keySet().iterator(); it.hasNext();) {
                    String key = it.next();
                    licenseInfo.append(String.valueOf(data.get(key)));
                }
                // check license data
                byte[] licenseData = StringUtils.toByte(licenseInfo.toString());
                Assert.isTrue(!CollectionUtils.isEmpty(licenseData), "Invalid license data");
                // create serial key
                String signature = EncryptUtils.toHex(EncryptUtils.createSignature(privateKey, algorithm, licenseData));
                Assert.notNull(signature, "signature");
                data.put(serialKey, signature);
                // check license out file
                if (fileOut.exists() || fileOut.createNewFile()) {
                	licenseOutProp = new PropertyUtils(licenseOut, passwordOut);
                	// write data out
                	for(final Iterator<String> it = data.keySet().iterator(); it.hasNext();) {
                        String key = it.next();
                        licenseOutProp.setProperty(key, data.get(key));
                    }
                	licenseOutProp.store();
                }
                ok = true;
    		} catch (Exception e) {
    			e.printStackTrace();
    			ok = false;
    		}
    	}
    	// if failed, removing license out file
    	if (!ok) FileUtils.safeDelete(fileOut);
    	return ok;
    }
    /**
     * Create license
     *
     * @param privateKey private key
     * @param keyAlgorithm key algorithm method
     * @param serialKey serial property key to get license key from license file
     * @param signatureAlgorithm signature algorithm method
     * @param licenseIn license information properties file
     * @param passwordIn password that has been used to encrypted source license information
     * @param licenseOut encrypted license information properties file to write-out
     * @param passwordOut password that has been used to encrypted destination license information
     *
     * @return true for successful; else false if failed
     */
    public static boolean createLicense(
    		String privateKey, String keyAlgorithm,
    		String serialKey, String signatureAlgorithm,
    		String licenseIn, String passwordIn, String licenseOut, String passwordOut) {
    	PrivateKey key = null;
        try {
            key = EncryptUtils.buildPrivateKey(privateKey, keyAlgorithm);
            return createLicense(key, signatureAlgorithm, serialKey, licenseIn, passwordIn, licenseOut, passwordOut);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Create license with DSA provider and DSA signature algorithm method
     *
     * @param privateKey private key
     * @param serialKey serial property key to get license key from license file
     * @param licenseIn license information properties file
     * @param passwordIn password that has been used to encrypted source license information
     * @param licenseOut encrypted license information properties file to write-out
     * @param passwordOut password that has been used to encrypted destination license information
     *
     * @return true for successful; else false if failed
     */
    public static boolean createDsaLicense(String privateKey, String serialKey, String licenseIn, String passwordIn, String licenseOut, String passwordOut) {
        return createLicense(
        		privateKey, EncryptUtils.ENCRYPT_PROVIDER_DSA,
        		serialKey, EncryptUtils.ENCRYPT_PROVIDER_DSA,
        		licenseIn, passwordIn, licenseOut, passwordOut);
    }
    /**
     * Create license with RSA provider and SHA256withRSA signature algorithm method
     *
     * @param privateKey private key
     * @param serialKey serial property key to get license key from license file
     * @param licenseIn license information properties file
     * @param passwordIn password that has been used to encrypted source license information
     * @param licenseOut encrypted license information properties file to write-out
     * @param passwordOut password that has been used to encrypted destination license information
     *
     * @return true for successful; else false if failed
     */
    public static boolean createRsaLicense(String privateKey, String serialKey, String licenseIn, String passwordIn, String licenseOut, String passwordOut) {
        return createLicense(
        		privateKey, EncryptUtils.ENCRYPT_PROVIDER_RSA,
        		serialKey, EncryptUtils.ENCRYPT_PROVIDER_SHA256WITHRSA,
        		licenseIn, passwordIn, licenseOut, passwordOut);
    }
}
