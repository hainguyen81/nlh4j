/*
 * @(#)EncryptUtils.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

/**
 * Encryption/Decryption utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public final class EncryptUtils implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	public static final String ENCODING_UTF8 = "UTF-8";
	public static final String ENCRYPT_PROVIDER_AES = "AES";
	public static final String ENCRYPT_PROVIDER_DSA = "DSA";
	public static final String ENCRYPT_PROVIDER_RSA = "RSA";
	public static final String ENCRYPT_PROVIDER_SHA1WITHRSA = "SHA1withRSA";
	public static final String ENCRYPT_PROVIDER_SHA256WITHRSA = "SHA256withRSA";
	public static final String ENCRYPT_PROVIDER_SHA1WITHDSA = "SHA1withDSA";
	private final static String HEX = "0123456789ABCDEFAA001177AA";
	public final static int DEFAULT_SECURITY_KEY_SIZE = 1024;

	/**
	 * Encodes base64
	 *
	 * @param text the text for encrypting
	 *
	 * @return the base64 encrypted string
	 */
	public static String base64encode(String text) {
		return base64encode(text, ENCODING_UTF8);
	}
	/**
	 * Encodes base64
	 *
	 * @param text the text for encrypting
	 * @param charset encoding charset
	 *
	 * @return the base64 encrypted string
	 */
	public static String base64encode(String text, String charset) {
		try { return base64encode(StringUtils.toByte(text, charset), charset); }
		catch (Exception e) { return null; }
	}
	/**
	 * Encodes base64
	 *
	 * @param data to encrypt
	 *
	 * @return the base64 encrypted string
	 */
	public static String base64encode(byte[] data) {
		return base64encode(data, ENCODING_UTF8);
	}
	/**
	 * Encodes base64
	 *
	 * @param data to encrypt
	 * @param charset encoding charset
	 *
	 * @return the base64 encrypted string
	 */
	public static String base64encode(byte[] data, String charset) {
		try {
			if (StringUtils.hasText(charset)) {
				return new String(Base64.encodeBase64(data), charset);
			}
			else {
				return new String(Base64.encodeBase64(data));
			}
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
     * Encodes base64
     *
     * @param text the text for encrypting
     *
     * @return the base64 encrypted bytes array
     */
    public static byte[] base64encodeBytesArray(String text) {
        return base64encodeBytesArray(text, ENCODING_UTF8);
    }
    /**
     * Encodes base64
     *
     * @param text the text for encrypting
     * @param charset encoding charset
     *
     * @return the base64 encrypted bytes array
     */
    public static byte[] base64encodeBytesArray(String text, String charset) {
        try { return base64encodeBytesArray(StringUtils.toByte(text, charset)); }
        catch (Exception e) { return null; }
    }
	/**
     * Encodes base64
     *
     * @param data to encrypt
     *
     * @return the base64 encrypted bytes array
     */
    public static byte[] base64encodeBytesArray(byte[] data) {
        try { return Base64.encodeBase64(data); }
        catch(Exception e) { return null; }
    }

	/**
	 * Decodes base64
	 *
	 * @param text the base64 encrypted string for decrypting
	 *
	 * @return the base64 decrypted string
	 */
	public static String base64decode(String text) {
		return base64decode(text, ENCODING_UTF8);
	}
	/**
	 * Decodes base64
	 *
	 * @param text the base64 encrypted string for decrypting
	 * @param charset encoding charset
	 *
	 * @return the base64 decrypted string
	 */
	public static String base64decode(String text, String charset) {
		try { return base64decode(StringUtils.toByte(text, charset), charset); }
		catch (Exception e) { return null; }
	}
	/**
	 * Decodes base64
	 *
	 * @param data to decrypt
	 *
	 * @return the base64 decrypted string
	 */
	public static String base64decode(byte[] data) {
		return base64decode(data, ENCODING_UTF8);
	}
	/**
	 * Decodes base64
	 *
	 * @param data to decrypt
	 * @param charset encoding charset
	 *
	 * @return the base64 decrypted string
	 */
	public static String base64decode(byte[] data, String charset) {
		try {
			if (StringUtils.hasText(charset)) {
				return new String(Base64.decodeBase64(data), charset);
			}
			else {
				return new String(Base64.decodeBase64(data));
			}
		}
		catch (Exception e) {
			return null;
		}
	}
	/**
     * Decodes base64
     *
     * @param text the base64 encrypted string for decrypting
     *
     * @return the base64 decrypted bytes array
     */
    public static byte[] base64decodeBytesArray(String text) {
        return base64decodeBytesArray(text, ENCODING_UTF8);
    }
    /**
     * Decodes base64
     *
     * @param text the base64 encrypted string for decrypting
     * @param charset encoding charset
     *
     * @return the base64 decrypted bytes array
     */
    public static byte[] base64decodeBytesArray(String text, String charset) {
        try { return base64decodeBytesArray(StringUtils.toByte(text, charset)); }
        catch (Exception e) { return null; }
    }
    /**
     * Decodes base64
     *
     * @param data to decrypt
     *
     * @return the base64 decrypted bytes array
     */
    public static byte[] base64decodeBytesArray(byte[] data) {
        try { return Base64.decodeBase64(data); }
        catch (Exception e) { return null; }
    }

	/**
	 * XOR message
	 *
	 * @param message the message to execute
	 * @param key the key for encrypting
	 *
	 * @return the encrypted message or null if failed
	 */
	public static String xorMessage(String message, String key) {
	    if (!StringUtils.hasLength(message)) return null;
		try {
			char[] keys = key.toCharArray();
			char[] mesg = message.toCharArray();

			int ml = mesg.length;
			int kl = keys.length;
			char[] newmsg = new char[ml];

			for (int i = 0; i < ml; i++) {
				newmsg[i] = (char) (mesg[i] ^ keys[i % kl] ^ 862959098);
			}
			mesg = null;
			keys = null;
			return new String(newmsg);
		}
		catch (Exception e) {
		    e.printStackTrace();
			return null;
		}
	}

	/**
	 * Encrypts the specified string with password
	 *
	 * @param passwd the password
	 * @param value the string for encrypting with password
	 *
	 * @return the encrypted string or null if failed
	 */
	public static String encrypt(String passwd, String value) {
		if (!StringUtils.hasLength(passwd) || !StringUtils.hasLength(value)) return null;
        byte[] rawKey = null;
		try {
			rawKey = getAesRawKey(StringUtils.toByte(passwd, ENCODING_UTF8));
			return (!CollectionUtils.isEmpty(rawKey)
			        ? toHex(encrypt(rawKey, StringUtils.toByte(value, ENCODING_UTF8))) : null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
	/**
	 * Decrypts the specified string with password
	 *
	 * @param passwd the password
	 * @param encrypted the encrypted string for decrypting with password
	 *
	 * @return the decrypted string or null if failed
	 */
	public static String decrypt(String passwd, String encrypted) {
		if (!StringUtils.hasLength(passwd) || !StringUtils.hasLength(encrypted)) return null;
		byte[] rawKey = null;
		byte[] enc = null;
		try {
			rawKey = getAesRawKey(StringUtils.toByte(passwd, ENCODING_UTF8));
			if (!CollectionUtils.isEmpty(rawKey)) {
    			enc = hexToByte(encrypted);
    			return new String(decrypt(rawKey, enc), ENCODING_UTF8);
			}
		}
		catch (Exception e) {
		    e.printStackTrace();
		}
		return null;
    }

	/**
	 * Gets the AES bytes array of the specified password
	 *
	 * @param passwd the password for generating
	 *
	 * @return the AES bytes or null if failed
	 */
    private static byte[] getAesRawKey(byte[] passwd) {
    	if (CollectionUtils.isEmpty(passwd)) return null;
    	try {
            KeyGenerator kgen = KeyGenerator.getInstance(ENCRYPT_PROVIDER_AES);
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(passwd);
            kgen.init(128, sr); // 192 and 256 bits may not be available
            SecretKey skey = kgen.generateKey();
            byte[] raw = skey.getEncoded();
            return raw;
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
    	return null;
    }

    /**
	 * Encrypts the specified string with password
	 *
	 * @param aesKey the AES security key
	 * @param value the string for encrypting with password
	 *
	 * @return the encrypted string or null if failed
	 */
	private static byte[] encrypt(byte[] aesKey, byte[] value) {
		if (CollectionUtils.isEmpty(aesKey) || CollectionUtils.isEmpty(value)) return null;
		try {
            SecretKeySpec skeySpec = new SecretKeySpec(aesKey, ENCRYPT_PROVIDER_AES);
            Cipher cipher = Cipher.getInstance(ENCRYPT_PROVIDER_AES);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(value);
            return encrypted;
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return null;
    }
	/**
	 * Decrypts the specified string with password
	 *
	 * @param aesKey the AES security key
	 * @param value the encrypted string for decrypting with password
	 *
	 * @return the decrypted string or null if failed
	 */
	private static byte[] decrypt(byte[] aesKey, byte[] value) {
		if (CollectionUtils.isEmpty(aesKey) || CollectionUtils.isEmpty(value)) return null;
		try {
            SecretKeySpec skeySpec = new SecretKeySpec(aesKey, ENCRYPT_PROVIDER_AES);
            Cipher cipher = Cipher.getInstance(ENCRYPT_PROVIDER_AES);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decrypted = cipher.doFinal(value);
            return decrypted;
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return null;
    }

	/**
     * Converts the specified value to HEX
     *
     * @param txt the string to convert
     * @param charset text charset
     *
     * @return the converted HEX string
     */
    public static String toHex(String txt, String charset) {
        if (!StringUtils.hasLength(txt)) return null;
        return toHex(StringUtils.toByte(txt, charset));
    }
	/**
	 * Converts the specified value to HEX
	 *
	 * @param txt the string to convert
	 *
	 * @return the converted HEX string
	 */
    public static String toHex(String txt) {
    	return toHex(txt, ENCODING_UTF8);
    }
    /**
     * Converts from HEX to string
     *
     * @param hex the HEX string to convert
     * @param charset convert to charset
     *
     * @return the converted string
     */
    public static String fromHex(String hex, String charset) {
    	if (!StringUtils.hasLength(hex)) return null;
        try {
            return new String(hexToByte(hex), charset);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Converts from HEX to string
     *
     * @param hex the HEX string to convert
     *
     * @return the converted string or null if failed
     */
    public static String fromHex(String hex) {
        return fromHex(hex, ENCODING_UTF8);
    }

    /**
     * Converts the specified HEX string to bytes array
     *
     * @param hex the HEX string to convert
     *
     * @return the converted string
     */
    public static byte[] hexToByte(String hex) {
    	if (!StringUtils.hasLength(hex)) return null;
        int len = hex.length()/2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hex.substring(2*i, 2*i+2), 16).byteValue();
        }
        return result;
    }
    /**
     * Converts the bytes array to HEX string
     *
     * @param buf the bytes array to convert
     *
     * @return the converted string
     */
    public static String toHex(byte[] buf) {
    	if (CollectionUtils.isEmpty(buf)) return null;
        StringBuffer result = new StringBuffer(2*buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    /**
     * Appends hex value of the specified byte to the specified buffer
     *
     * @param sb the buffer to append
     * @param b the byte for converting to HEX
     */
    private static void appendHex(StringBuffer sb, byte b) {
    	if (sb == null) {
            return;
        }
        sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
    }

	/**
	 * Encrypts the specified file path with password by PBE
	 *
	 * @param passwd the password for encrypting
	 * @param is the source stream
	 * @param os the destination stream
	 *
	 * @throws NoSuchAlgorithmException thrown if fail
	 * @throws InvalidKeySpecException thrown if fail
	 * @throws NoSuchPaddingException thrown if fail
	 * @throws InvalidAlgorithmParameterException thrown if fail
	 * @throws InvalidKeyException thrown if fail
	 * @throws IOException thrown if fail
	 * @throws BadPaddingException thrown if fail
	 * @throws IllegalBlockSizeException thrown if fail
	 */
	public static void encryptPBE(String passwd, InputStream is, OutputStream os)
			throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IOException,
			IllegalBlockSizeException, BadPaddingException {
		// checks parameter
		if (!StringUtils.hasLength(passwd) || is == null || os == null) return;
		// generate password key
		PBEKeySpec keySpec = new PBEKeySpec(passwd.toCharArray());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey passwordKey = keyFactory.generateSecret(keySpec);
		// PBE = hashing + symmetric encryption.  A 64 bit random
		// number (the salt) is added to the password and hashed
		// using a Message Digest Algorithm (MD5 in this example.).
		// The number of times the password is hashed is determined
		// by the interation count.  Adding a random number and
		// hashing multiple times enlarges the key space.
		byte[] salt = new byte[8];
		Random rnd = new Random();
		rnd.nextBytes(salt);
		int iterations = 100;
		// creates the parameter spec for this salt and interation count
		PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);
		// creates the cipher and initialize it for encryption.
		Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
		cipher.init(Cipher.ENCRYPT_MODE, passwordKey, parameterSpec);
		// need to write the salt to the (encrypted) file.  The
		// salt is needed when reconstructing the key for decryption.
		os.write(salt);
		// reads the file and encrypt its bytes.
		byte[] input = new byte[64];
		int bytesRead;
		while((bytesRead = is.read(input)) != -1) {
			byte[] output = cipher.update(input, 0, bytesRead);
			if (output != null) {
                os.write(output);
            }
		}
		byte[] output = cipher.doFinal();
		if (output != null) {
            os.write(output);
        }
		// final release
		os.flush();
	}
	/**
	 * Decrypts the specified file path with password by PBE
	 *
	 * @param passwd the password for encrypting
	 * @param is the source stream
	 * @param os the destination stream
	 *
	 * @throws NoSuchAlgorithmException thrown if fail
	 * @throws InvalidKeySpecException thrown if fail
	 * @throws NoSuchPaddingException thrown if fail
	 * @throws InvalidAlgorithmParameterException thrown if fail
	 * @throws InvalidKeyException thrown if fail
	 * @throws IOException thrown if fail
	 * @throws BadPaddingException thrown if fail
	 * @throws IllegalBlockSizeException thrown if fail
	 */
	public static void decryptPBE(String passwd, InputStream is, OutputStream os)
			throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IOException,
			IllegalBlockSizeException, BadPaddingException {
		// checks parameter
		if (!StringUtils.hasLength(passwd) || is == null || os == null) return;
		// generate password key
		PBEKeySpec keySpec = new PBEKeySpec(passwd.toCharArray());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey passwordKey = keyFactory.generateSecret(keySpec);
		// reads in the previouly stored salt and set the iteration count.
		byte[] salt = new byte[8];
		is.read(salt);
		int iterations = 100;
		// creates the parameter spec for this salt and interation count
		PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterations);
		// creates the cipher and initialize it for encryption.
		Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
		cipher.init(Cipher.DECRYPT_MODE, passwordKey, parameterSpec);
		// reads the file and encrypt its bytes.
		byte[] input = new byte[64];
		int bytesRead;
		while((bytesRead = is.read(input)) != -1) {
			byte[] output = cipher.update(input, 0, bytesRead);
			if (output != null) {
                os.write(output);
            }
		}
		byte[] output = cipher.doFinal();
		if (output != null) {
            os.write(output);
        }
		// final release
		os.flush();
	}
	/**
	 * Encrypts the specified file path with password by PBE
	 *
	 * @param passwd the password for encrypting
	 * @param srcFilePath the source file path to encrypt
	 * @param destFilePath the destination encrypted file path
	 *
	 * @return	-1 invalid parameters
	 * 			-2 source file does not exist
	 * 			 0 encrypt successfully
	 *
	 * @throws NoSuchAlgorithmException thrown if fail
	 * @throws InvalidKeySpecException thrown if fail
	 * @throws NoSuchPaddingException thrown if fail
	 * @throws InvalidAlgorithmParameterException thrown if fail
	 * @throws InvalidKeyException thrown if fail
	 * @throws IOException thrown if fail
	 * @throws BadPaddingException thrown if fail
	 * @throws IllegalBlockSizeException thrown if fail
	 */
	public static int encryptPBE(String passwd, String srcFilePath, String destFilePath)
			throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IOException,
			IllegalBlockSizeException, BadPaddingException {
		// checks parameter
		if (!StringUtils.hasLength(passwd) || !StringUtils.hasLength(srcFilePath)
				|| !StringUtils.hasLength(destFilePath)) {
            return -1;
        }
		// checks valid source file
		File srcFile = new File(srcFilePath);
		if (!srcFile.exists()) return -2;
		// creates streams file
		FileInputStream fis = new FileInputStream(srcFile);
		FileOutputStream fos = new FileOutputStream(destFilePath, false);
		// encrypts stream
		encryptPBE(passwd, fis, fos);
		// final release
		StreamUtils.closeQuitely(fis);
		StreamUtils.closeQuitely(fos);
		return 0;
	}
	/**
	 * Decrypts the specified file path with password by PBE
	 *
	 * @param passwd
	 * 		the password for encrypting
	 * @param srcFilePath
	 * 		the source file path to decrypt
	 * @param destFilePath
	 * 		the destination decrypted file path
	 *
	 * @return	-1 invalid parameters
	 * 			-2 source file does not exist
	 * 			 0 decrypt successfully
	 *
	 * @throws NoSuchAlgorithmException thrown if fail
	 * @throws InvalidKeySpecException thrown if fail
	 * @throws NoSuchPaddingException thrown if fail
	 * @throws InvalidAlgorithmParameterException thrown if fail
	 * @throws InvalidKeyException thrown if fail
	 * @throws IOException thrown if fail
	 * @throws BadPaddingException thrown if fail
	 * @throws IllegalBlockSizeException thrown if fail
	 */
	public static int decryptPBE(String passwd, String srcFilePath, String destFilePath)
			throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IOException,
			IllegalBlockSizeException, BadPaddingException {
		// checks parameter
	    if (!StringUtils.hasLength(passwd) || !StringUtils.hasLength(srcFilePath)
                || !StringUtils.hasLength(destFilePath)) {
            return -1;
        }
		// checks valid source file
		File srcFile = new File(srcFilePath);
		if (!srcFile.exists()) return -2;

		// creates streams file
		FileInputStream fis = new FileInputStream(srcFile);
		FileOutputStream fos = new FileOutputStream(destFilePath, false);

		// decrypts stream
		decryptPBE(passwd, fis, fos);

		// final release
		StreamUtils.closeQuitely(fis);
		StreamUtils.closeQuitely(fos);
		return 0;
	}

	/**
	 * Writes the specified {@link Properties} to {@link ByteArrayInputStream}
	 *
	 * @param properties
	 * 		the {@link Properties} to write
	 *
	 * @return the {@link ByteArrayInputStream}
	 * @throws IOException thrown if fail
	 */
	private static ByteArrayInputStream writeProperties(Properties properties) throws IOException {
		if (properties == null || properties.isEmpty()) return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		properties.store(baos, "Encryption Properties");
		return new ByteArrayInputStream(baos.toByteArray());
	}
	/**
	 * Reads the specified {@link ByteArrayOutputStream} to {@link Properties}
	 *
	 * @param baos
	 * 		the {@link ByteArrayOutputStream} to read
	 *
	 * @return the {@link Properties}
	 *
	 * @throws IOException thrown if fail
	 */
	private static Properties readProperties(ByteArrayOutputStream baos) throws IOException {
		Properties properties = new Properties();
		if (baos == null) return null;
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		properties.load(bais);
		StreamUtils.closeQuitely(bais);
		return properties;
	}
	/**
	 * Encrypts the specified {@link Properties} to the specified destination file with password
	 *
	 * @param passwd
	 * 		the password for encrypting
	 * @param properties
	 * 		the {@link Properties} to encrypt
	 * @param destFilePath
	 * 		the destination encrypted file path
	 *
	 * @return	-1 invalid parameters
	 * 			-2 source properties is empty or invalid
	 * 			 0 encrypt successfully
	 *
	 * @throws IOException thrown if fail
	 * @throws BadPaddingException thrown if fail
	 * @throws IllegalBlockSizeException thrown if fail
	 * @throws InvalidAlgorithmParameterException thrown if fail
	 * @throws NoSuchPaddingException thrown if fail
	 * @throws InvalidKeySpecException thrown if fail
	 * @throws NoSuchAlgorithmException thrown if fail
	 * @throws InvalidKeyException thrown if fail
	 */
	public static int encryptPBE(String passwd, Properties properties, String destFilePath)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		// checks parameter
		if (!StringUtils.hasLength(passwd)
				|| properties == null || properties.isEmpty()
				|| !StringUtils.hasLength(destFilePath)) {
            return -1;
        }
		// writes properties to stream
		ByteArrayInputStream bais = writeProperties(properties);
		if (bais == null) return -2;

		// encrypts stream
		FileOutputStream fos = new FileOutputStream(destFilePath, false);
		encryptPBE(passwd, bais, fos);

		// final release
		StreamUtils.closeQuitely(bais);
		StreamUtils.closeQuitely(fos);
		return 0;
	}
	/**
	 * Decrypts the specified file to {@link Properties} with password
	 *
	 * @param passwd
	 * 		the password for encrypting
	 * @param srcFilePath
	 * 		the encrypted file path
	 *
	 * @return	the {@link Properties}
	 *
	 * @throws IOException thrown if fail
	 * @throws BadPaddingException thrown if fail
	 * @throws IllegalBlockSizeException thrown if fail
	 * @throws InvalidAlgorithmParameterException thrown if fail
	 * @throws NoSuchPaddingException thrown if fail
	 * @throws InvalidKeySpecException thrown if fail
	 * @throws NoSuchAlgorithmException thrown if fail
	 * @throws InvalidKeyException thrown if fail
	 */
	public static Properties decryptPBE(String passwd, String srcFilePath)
			throws IOException, InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		Properties properties = new Properties();
		// checks parameter
		if (!StringUtils.hasLength(passwd) || !StringUtils.hasLength(srcFilePath)) {
            return properties;
        }
		// checks valid source file
		File srcFile = new File(srcFilePath);
		if (!srcFile.exists()) return properties;

		// decrypts stream
		FileInputStream fis = new FileInputStream(srcFile);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		decryptPBE(passwd, fis, baos);

		// to properties
		properties = readProperties(baos);

		// final release
		StreamUtils.closeQuitely(fis);
		StreamUtils.closeQuitely(baos);
		return properties;
	}

	/**
	 * Encrypt MD5 the specified string sentence
	 *
	 * @param s the string sentence to encrypt
	 * @param charset the charset to encode
	 *
	 * @return the encrypted MD5 string
	 */
	public static String md5(String s, String charset) {
	    if (!StringUtils.hasLength(s)) return null;
        return md5(StringUtils.toByte(s, charset));
	}
    /**
     * Encrypt MD5 the specified string sentence
     *
     * @param s the string sentence to encrypt
     *
     * @return the encrypted MD5 string
     */
    public static String md5(String s) {
        return md5(s, ENCODING_UTF8);
    }
	/**
	 * Encrypt MD5 the specified bytes array
	 *
	 * @param bytes bytes array to encrypt
	 *
	 * @return the encrypted MD5 string
	 */
	public static String md5(byte[] bytes) {
	    if (CollectionUtils.isEmpty(bytes)) return null;
	    return DigestUtils.md5DigestAsHex(bytes);
    }

    /**
     * Build public key of license
     *
     * @param key public key
     * @param keyCharset key charset
     * @param algorithm algorithm method
     *
     * @return public key or null if failed
     */
    public static PublicKey buildPublicKey(String key, String keyCharset, String algorithm) {
        return buildPublicKey(base64decodeBytesArray(key, keyCharset), algorithm);
    }
    /**
     * Build public key of license
     *
     * @param key public key
     * @param algorithm algorithm method
     *
     * @return public key or null if failed
     */
    public static PublicKey buildPublicKey(String key, String algorithm) {
        return buildPublicKey(key, ENCODING_UTF8, algorithm);
    }
	/**
     * Build public key of license
     *
     * @param bytes public key
     * @param algorithm algorithm method
     *
     * @return public key or null if failed
     */
    public static PublicKey buildPublicKey(byte[] bytes, String algorithm) {
        if (!CollectionUtils.isEmpty(bytes) && StringUtils.hasText(algorithm)) {
            try {
                KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
                EncodedKeySpec keySpec = new X509EncodedKeySpec(bytes);
                return keyFactory.generatePublic(keySpec);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * Build public key of license
     *
     * @param key public key
     *
     * @return public key or null if failed
     */
    public static PublicKey buildDsaPublicKey(String key) {
        return buildPublicKey(key, ENCODING_UTF8, ENCRYPT_PROVIDER_DSA);
    }
    /**
     * Build public key of license
     *
     * @param bytes public key
     *
     * @return public key or null if failed
     */
    public static PublicKey buildDsaPublicKey(byte[] bytes) {
        return buildPublicKey(bytes, ENCRYPT_PROVIDER_DSA);
    }
    /**
     * Build public key of license
     *
     * @param key public key
     *
     * @return public key or null if failed
     */
    public static PublicKey buildRsaPublicKey(String key) {
        return buildPublicKey(key, ENCODING_UTF8, ENCRYPT_PROVIDER_RSA);
    }
    /**
     * Build public key of license
     *
     * @param bytes public key
     *
     * @return public key or null if failed
     */
    public static PublicKey buildRsaPublicKey(byte[] bytes) {
        return buildPublicKey(bytes, ENCRYPT_PROVIDER_RSA);
    }

    /**
     * Build private key of license
     *
     * @param key public key
     * @param keyCharset key charset
     * @param algorithm algorithm method
     *
     * @return private key or null if failed
     */
    public static PrivateKey buildPrivateKey(String key, String keyCharset, String algorithm) {
        return buildPrivateKey(base64decodeBytesArray(key, keyCharset), algorithm);
    }
    /**
     * Build private key of license
     *
     * @param key public key
     * @param algorithm algorithm method
     *
     * @return private key or null if failed
     */
    public static PrivateKey buildPrivateKey(String key, String algorithm) {
        return buildPrivateKey(key, ENCODING_UTF8, algorithm);
    }
    /**
     * Build private key of license
     *
     * @param bytes public key
     * @param algorithm algorithm method
     *
     * @return private key or null if failed
     */
    public static PrivateKey buildPrivateKey(byte[] bytes, String algorithm) {
        if (!CollectionUtils.isEmpty(bytes) && StringUtils.hasText(algorithm)) {
            try {
                KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
                EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(bytes);
                return keyFactory.generatePrivate(keySpec);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * Build private key of license
     *
     * @param key public key
     *
     * @return private key or null if failed
     */
    public static PrivateKey buildDsaPrivateKey(String key) {
        return buildPrivateKey(key, ENCODING_UTF8, ENCRYPT_PROVIDER_DSA);
    }
    /**
     * Build private key of license
     *
     * @param bytes public key
     *
     * @return private key or null if failed
     */
    public static PrivateKey buildDsaPrivateKey(byte[] bytes) {
        return buildPrivateKey(bytes, ENCRYPT_PROVIDER_DSA);
    }
    /**
     * Build private key of license
     *
     * @param key public key
     *
     * @return private key or null if failed
     */
    public static PrivateKey buildRsaPrivateKey(String key) {
        return buildPrivateKey(key, ENCODING_UTF8, ENCRYPT_PROVIDER_RSA);
    }
    /**
     * Build private key of license
     *
     * @param bytes public key
     *
     * @return private key or null if failed
     */
    public static PrivateKey buildRsaPrivateKey(byte[] bytes) {
        return buildPrivateKey(bytes, ENCRYPT_PROVIDER_RSA);
    }

    /**
     * Generate randomize security keys pair
     *
     * @param algorithm algorithm method
     * @param keySize the size of key
     *
     * @return keys pair or null if failed
     */
    public static KeyPair buildRandomKeypair(String algorithm, int keySize) {
        SecureRandom sr = null;
        KeyPairGenerator kpGen = null;
        try {
            sr = new SecureRandom();
            kpGen = KeyPairGenerator.getInstance(algorithm);
            kpGen.initialize(keySize, sr);
            return kpGen.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Generate randomize security keys pair with default 1024 key size
     *
     * @param algorithm algorithm method
     *
     * @return keys pair or null if failed
     */
    public static KeyPair buildRandomKeypair(String algorithm) {
        return buildRandomKeypair(algorithm, DEFAULT_SECURITY_KEY_SIZE);
    }
    /**
     * Generate randomize security keys pair with default 1024 key size
     *
     * @return keys pair or null if failed
     */
    public static KeyPair buildDsaRandomKeypair() {
        return buildRandomKeypair(ENCRYPT_PROVIDER_DSA, DEFAULT_SECURITY_KEY_SIZE);
    }
    /**
     * Generate randomize security keys pair with default 1024 key size
     *
     * @return keys pair or null if failed
     */
    public static KeyPair buildRsaRandomKeypair() {
        return buildRandomKeypair(ENCRYPT_PROVIDER_RSA, DEFAULT_SECURITY_KEY_SIZE);
    }

    /**
     * Create signature for data using private key
     *
     * @param key private key
     * @param algorithm algorithm method
     * @param buffer data
     *
     * @return signed data or null if failed
     */
    public static byte[] createSignature(PrivateKey key, String algorithm, byte[] buffer) {
    	if (key != null) {
	    	try {
	    		algorithm = (!StringUtils.hasText(algorithm) ? key.getAlgorithm() : algorithm);
	    		Signature sig = Signature.getInstance(algorithm);
	    		sig.initSign(key);
	    		sig.update(buffer, 0, buffer.length);
	    		return sig.sign();
	    	} catch (SignatureException e) {
	    		e.printStackTrace();
	    	} catch (InvalidKeyException e) {
	    		e.printStackTrace();
	    	} catch (NoSuchAlgorithmException e) {
	    		e.printStackTrace();
	    	}
    	}
    	return null;
    }
    /**
     * Create signature for data using private key
     *
     * @param key private key
     * @param buffer data
     *
     * @return signed data or null if failed
     */
    public static byte[] createSignature(PrivateKey key, byte[] buffer) {
    	return (key == null ? null : createSignature(key, key.getAlgorithm(), buffer));
    }

    /**
     * Verify data by signature
     *
     * @param key public key
     * @param algorithm algorithm method
     * @param buffer license information
     * @param signature data signature, such as license key (serial number)
     *
     * @return true for valid; else false
     */
    public static boolean verifySignature(PublicKey key, String algorithm, byte[] buffer, byte[] signature) {
    	if (key != null && !CollectionUtils.isEmpty(buffer) && !CollectionUtils.isEmpty(signature)) {
	        try {
	        	algorithm = (!StringUtils.hasText(algorithm) ? key.getAlgorithm() : algorithm);
	            Signature sig = Signature.getInstance(algorithm);
	            sig.initVerify(key);
	            sig.update(buffer, 0, buffer.length);
	            return sig.verify(signature);
	        }
	        catch (SignatureException e) {
	            e.printStackTrace();
	        }
	        catch (InvalidKeyException e) {
	            e.printStackTrace();
	        }
	        catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        }
    	}
        return false;
    }
    /**
     * Verify data by signature
     *
     * @param key public key
     * @param buffer license information
     * @param signature data signature, such as license key (serial number)
     *
     * @return true for valid; else false
     */
    public static boolean verifySignature(PublicKey key, byte[] buffer, byte[] signature) {
        return ((key != null && !CollectionUtils.isEmpty(buffer) && !CollectionUtils.isEmpty(signature))
        		? verifySignature(key, key.getAlgorithm(), buffer, signature) : false);
    }

    /**
     * Reads a Java {@link KeyStore} from a file.
     *
     * @param keystoreFile
     *          {@link KeyStore} file to read
     * @param password
     *          password for the {@link KeyStore} file
     * @param keyStoreType
     *          type of {@link KeyStore}, e.g., JKS or PKCS12
     * @return the {@link KeyStore} object or null if failed
     */
    private static KeyStore loadKeyStore(final File keystoreFile, final String password, final String keyStoreType) {
    	Assert.notNull(keystoreFile, "Keystore could not be null");
    	Assert.isTrue(keystoreFile.exists(), "Keystore not exist");
    	KeyStore keystore = null;
    	InputStream is = null;
    	try {
    		is = new FileInputStream(keystoreFile);
    		keystore = KeyStore.getInstance(keyStoreType);
    		keystore.load(is, !StringUtils.hasText(password) ? null : password.toCharArray());
    	} catch (Exception e) {
    		e.printStackTrace();
    		keystore = null;
    	} finally {
    		StreamUtils.closeQuitely(is);
    	}
    	return keystore;
    }
    /**
     * Get {@link KeyPair} (both {@link PrivateKey}, {@link PublicKey}) from the specified {@link KeyStore}
     *
     * @param keyStoreFile
     * 		{@link KeyStore} file
     * @param storePassword
     * 		password for the {@link KeyStore} file
     * @param keyStoreType
     * 		type of {@link KeyStore}, e.g., JKS or PKCS12
     * @param keyPassword
     * 		password for the {@link KeyPair}
     * @param alias
     * 		certificate alias
     *
     * @return the {@link KeyPair} object or null if failed
     */
    public static KeyPair getKeyPairFrom(
    		File keyStoreFile, String keyStoreType, String storePassword, String keyPassword, String alias) {
    	KeyStore keyStore = loadKeyStore(keyStoreFile, storePassword, keyStoreType);
    	Assert.notNull(keyStore, "Could not load keystore file");
    	KeyPair keyPair = null;
    	Key secKey = null;
    	PrivateKey privateKey = null;
    	PublicKey publicKey = null;
    	Certificate certificate = null;
    	try {
    		secKey = keyStore.getKey(alias, keyPassword.toCharArray());
    		privateKey = BeanUtils.safeType(secKey, PrivateKey.class);
    		if (privateKey != null) {
    			certificate = keyStore.getCertificate(alias);
    			publicKey = certificate.getPublicKey();
    			keyPair = new KeyPair(publicKey, privateKey);
    		}
		} catch (Exception e) {
			e.printStackTrace();
			keyPair = null;
		}
    	return keyPair;
    }
    /**
     * Get {@link KeyPair} (both {@link PrivateKey}, {@link PublicKey}) from the specified {@link KeyStore}
     *
     * @param keyStoreFile
     * 		{@link KeyStore} file
     * @param storePassword
     * 		password for the {@link KeyStore} file
     * @param keyStoreType
     * 		type of {@link KeyStore}, e.g., JKS or PKCS12
     * @param keyPassword
     * 		password for the {@link KeyPair}
     * @param alias
     * 		certificate alias
     *
     * @return the {@link KeyPair} object or null if failed
     */
    public static KeyPair getKeyPairFrom(
    		String keyStoreFile, String keyStoreType, String storePassword, String keyPassword, String alias) {
    	return getKeyPairFrom(new File(keyStoreFile), keyStoreType, storePassword, keyPassword, alias);
    }

    /**
     * Convert the specified string value to {@link SecretKey}
     *
     * @param base64SecretKey to convert
     * @param algorithm algorithm method
     *
     * @return {@link SecretKey} or NULL if failed
     */
    public static SecretKey createSecretKey(String base64SecretKey, String algorithm) {
        byte[] decodedSecretKey = base64decodeBytesArray(base64SecretKey);
        SecretKey secretKey = null;
        if (!CollectionUtils.isEmpty(decodedSecretKey)) {
            try {
                secretKey = new SecretKeySpec(
                        decodedSecretKey, 0, decodedSecretKey.length, algorithm);
            } catch (Exception e) {
                LogUtils.logError(EncryptUtils.class, e.getMessage());
                secretKey = null;
            }
        }
        return secretKey;
    }
    /**
     * Convert the specified string value to AES {@link SecretKey}
     *
     * @param secretKey to convert (non-Base64)
     *
     * @return {@link SecretKey} or NULL if failed
     */
    public static String createAesSecretKey(String secretKey) {
        secretKey = base64encode(secretKey);
        SecretKey specSecretKey = createSecretKey(
                secretKey, ENCRYPT_PROVIDER_AES);
        return (specSecretKey == null ? null : base64encode(specSecretKey.getEncoded()));
    }
    /**
     * Convert the specified string value to DSA {@link SecretKey}
     *
     * @param secretKey to convert (non-Base64)
     *
     * @return {@link SecretKey} or NULL if failed
     */
    public static String createDsaSecretKey(String secretKey) {
        secretKey = base64encode(secretKey);
        SecretKey specSecretKey = createSecretKey(
                secretKey, ENCRYPT_PROVIDER_DSA);
        return (specSecretKey == null ? null : base64encode(specSecretKey.getEncoded()));
    }
    /**
     * Convert the specified string value to RSA {@link SecretKey}
     *
     * @param secretKey to convert (non-Base64)
     *
     * @return {@link SecretKey} or NULL if failed
     */
    public static String createRasSecretKey(String secretKey) {
        secretKey = base64encode(secretKey);
        SecretKey specSecretKey = createSecretKey(
                secretKey, ENCRYPT_PROVIDER_RSA);
        return (specSecretKey == null ? null : base64encode(specSecretKey.getEncoded()));
    }
}
