/*
 * @(#)JWTUtils.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.security.PrivateKey;
import java.text.MessageFormat;
import java.util.Date;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.nimbusds.jwt.SignedJWT;

/**
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class JWTUtils implements Serializable {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create JWT Token
	 *
	 * @param algorithm {@link JWSAlgorithm}
	 * @param privateKey {@link PrivateKey} to sign
	 * @param authorizationSchema JWT authorization schema
	 * @param iss The iss (issuer) claim identifies the principal that issued the JWT.
	 * @param sub The sub (subject) claim identifies the principal that is the subject of the JWT.
	 * @param aud The aud (audience) claim identifies the audiences that the JWT is
	 * intended for according to draft 18 of the JWT spec,
	 * the aud claim is option and may be present in singular or as a list.
	 * @param expire The exp (expiration time) claim identifies the expiration time on
	 * or after which the JWT MUST NOT be accepted for processing.
	 * @param nbf The nbf (not before) claim identifies the time before
	 * which the JWT MUST NOT be accepted for processing.
	 * @param iat The iat (issued at) claim identifies the time at which the JWT was issued.
	 * @param jti The jti (JWT ID) claim provides a unique identifier for the JWT.
	 *
	 * @return JWT token string value. NULL if failed
	 */
	public static String createSignedJWT(
			JWSAlgorithm algorithm, PrivateKey privateKey, String authorizationSchema,
			String iss, String sub, String aud,
			Date expire, Date nbf, Date iat, String jti) {
		String token = null;
		try {
			// Let's set the JWT Claims
			JWTClaimsSet claimsSet = createJwtClaimsSet(iss, sub, aud, expire, nbf, iat, jti);

	        // sign JWT
	        token = signJWT(algorithm, privateKey, claimsSet);
	        if (StringUtils.hasText(token) && StringUtils.hasText(authorizationSchema)) {
	        	token = MessageFormat.format("{0} {1}", authorizationSchema, token);
	        }
		} catch (Exception e) {
			LogUtils.logError(JWTUtils.class, e.getMessage());
			token = null;
		}
		return token;
	}
	/**
     * Create JWT Token
     *
     * @param algorithm {@link JWSAlgorithm}
     * @param secretKey {@link MACSigner} secret key to sign
     * @param authorizationSchema JWT authorization schema
     * @param iss The iss (issuer) claim identifies the principal that issued the JWT.
     * @param sub The sub (subject) claim identifies the principal that is the subject of the JWT.
     * @param aud The aud (audience) claim identifies the audiences that the JWT is
     * intended for according to draft 18 of the JWT spec,
     * the aud claim is option and may be present in singular or as a list.
     * @param expire The exp (expiration time) claim identifies the expiration time on
     * or after which the JWT MUST NOT be accepted for processing.
     * @param nbf The nbf (not before) claim identifies the time before
     * which the JWT MUST NOT be accepted for processing.
     * @param iat The iat (issued at) claim identifies the time at which the JWT was issued.
     * @param jti The jti (JWT ID) claim provides a unique identifier for the JWT.
     *
     * @return JWT token string value. NULL if failed
     */
    public static String createSignedJWT(
            JWSAlgorithm algorithm, String secretKey, String authorizationSchema,
            String iss, String sub, String aud,
            Date expire, Date nbf, Date iat, String jti) {
        String token = null;
        try {
            // Let's set the JWT Claims
            JWTClaimsSet claimsSet = createJwtClaimsSet(iss, sub, aud, expire, nbf, iat, jti);

            // sign JWT
            token = signJWT(algorithm, secretKey, claimsSet);
            if (StringUtils.hasText(token) && StringUtils.hasText(authorizationSchema)) {
                token = MessageFormat.format("{0} {1}", authorizationSchema, token);
            }
        } catch (Exception e) {
            LogUtils.logError(JWTUtils.class, e.getMessage());
            token = null;
        }
        return token;
    }
	/**
	 * Sign JWT claims set
	 *
	 * @param algorithm {@link JWSAlgorithm}
	 * @param privateKey {@link PrivateKey} to sign as secret key
	 * @param claims to sign
	 *
	 * @return signed JWT token
	 */
	public static String signJWT(JWSAlgorithm algorithm, PrivateKey privateKey, JWTClaimsSet claims) {
		String signed = null;
		try {
			// sign JWT
	        String secretKey = EncryptUtils.base64encode(privateKey.getEncoded());
	        JWSSigner signer = new MACSigner(secretKey);
	        JWSHeader header = new JWSHeader(algorithm);
	        SignedJWT signedJWT = new SignedJWT(header, claims);
	        signedJWT.sign(signer);
	        signed = signedJWT.serialize();
		} catch (Exception e) {
			LogUtils.logError(JWTUtils.class, e.getMessage());
			signed = null;
		}
		return signed;
	}
    /**
     * Sign JWT claims set
     *
     * @param algorithm {@link JWSAlgorithm}
     * @param secretKey {@link MACSigner} secret key to sign (un-encoded base64 key)
     * @param claims to sign
     *
     * @return signed JWT token
     */
    public static String signJWT(JWSAlgorithm algorithm, String secretKey, JWTClaimsSet claims) {
        String signed = null;
        try {
            // sign JWT
            secretKey = EncryptUtils.base64encode(secretKey);
            JWSSigner signer = new MACSigner(secretKey);
            JWSHeader header = new JWSHeader(algorithm);
            SignedJWT signedJWT = new SignedJWT(header, claims);
            signedJWT.sign(signer);
            signed = signedJWT.serialize();
        } catch (Exception e) {
            LogUtils.logError(JWTUtils.class, e.getMessage());
            signed = null;
        }
        return signed;
    }
    
    /**
	 * Create a new instance of {@link JWTClaimsSet}
	 * 
	 * @param iss The iss (issuer) claim identifies the principal that issued the JWT.
     * @param sub The sub (subject) claim identifies the principal that is the subject of the JWT.
     * @param aud The aud (audience) claim identifies the audiences that the JWT is
     * intended for according to draft 18 of the JWT spec,
     * the aud claim is option and may be present in singular or as a list.
     * @param expire The exp (expiration time) claim identifies the expiration time on
     * or after which the JWT MUST NOT be accepted for processing.
     * @param nbf The nbf (not before) claim identifies the time before
     * which the JWT MUST NOT be accepted for processing.
     * @param iat The iat (issued at) claim identifies the time at which the JWT was issued.
     * @param jti The jti (JWT ID) claim provides a unique identifier for the JWT.
	 * 
	 * @return {@link JWTClaimsSet}
     */
    public static JWTClaimsSet createJwtClaimsSet(
    		String iss, String sub, String aud,
            Date expire, Date nbf, Date iat, String jti) {
    	return createJwtClaimsSetBuilder(iss, sub, aud, expire, nbf, iat, jti).build();
    }
    
    /**
	 * Create a new instance of {@link Builder} for building {@link JWTClaimsSet}
	 * 
	 * @param iss The iss (issuer) claim identifies the principal that issued the JWT.
     * @param sub The sub (subject) claim identifies the principal that is the subject of the JWT.
     * @param aud The aud (audience) claim identifies the audiences that the JWT is
     * intended for according to draft 18 of the JWT spec,
     * the aud claim is option and may be present in singular or as a list.
     * @param expire The exp (expiration time) claim identifies the expiration time on
     * or after which the JWT MUST NOT be accepted for processing.
     * @param nbf The nbf (not before) claim identifies the time before
     * which the JWT MUST NOT be accepted for processing.
     * @param iat The iat (issued at) claim identifies the time at which the JWT was issued.
     * @param jti The jti (JWT ID) claim provides a unique identifier for the JWT.
	 * 
	 * @return {@link Builder} for building {@link JWTClaimsSet}
     */
    public static Builder createJwtClaimsSetBuilder(
    		String iss, String sub, String aud,
            Date expire, Date nbf, Date iat, String jti) {
    	return new Builder()
				.issuer(iss)
				.issueTime(iat)
				.subject(sub)
				.audience(aud)
				.expirationTime(expire)
				.notBeforeTime(nbf)
				.jwtID(jti);
    }
}
