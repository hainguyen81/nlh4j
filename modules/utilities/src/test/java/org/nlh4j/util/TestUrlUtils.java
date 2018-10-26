/*
 * @(#)TestUrlUtils.java 1.0 Nov 2, 2016
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;

import org.junit.Test;

/**
 * Test class {@link UrlUtils}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class TestUrlUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Test {@link UrlUtils#isUrlVaid(String)}
     */
    @Test
    public void testUrl1() {
        String url = "http://localhost:8080/isev";
        if (UrlUtils.isUrlVaid(url)) {
            System.out.println("VALID: " + url);
        } else {
            System.out.println("INVALID: " + url);
        }
        url = "http://127.0.0.1:8080/isev";
        if (UrlUtils.isUrlVaid(url)) {
            System.out.println("VALID: " + url);
        } else {
            System.out.println("INVALID: " + url);
        }
    }
}
