/*
 * @(#)TestHttpUtils.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.apache.http.ParseException;
import org.junit.Test;

/**
 * Test class of {@link HttpUtils}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class TestHttpUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Test HTTP DELETE
     */
    @Test
    public void testDelete() {
        Integer status = null;
        StringBuffer response = new StringBuffer();
        try {
            status = HttpUtils.deleteStatus("https://httpbin.org/get", null, null, null, response);
            System.out.println("Response: " + String.valueOf(status) + "::" + response.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /**
     * Test HTTP GET
     */
    @Test
    public void testGet() {
        Integer status = null;
        StringBuffer response = new StringBuffer();
        try {
            status = HttpUtils.getStatus("https://httpbin.org/get", null, null, null, response);
            System.out.println("Response: " + String.valueOf(status) + "::" + response.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /**
     * Test HTTP HEAD
     */
    @Test
    public void testHead() {
        Integer status = null;
        StringBuffer response = new StringBuffer();
        try {
            status = HttpUtils.headStatus("https://httpbin.org/get", null, null, null, response);
            System.out.println("Response: " + String.valueOf(status) + "::" + response.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /**
     * Test HTTP OPTIONS
     */
    @Test
    public void testOptions() {
        Integer status = null;
        StringBuffer response = new StringBuffer();
        try {
            status = HttpUtils.optionsStatus("https://httpbin.org/get", null, null, null, response);
            System.out.println("Response: " + String.valueOf(status) + "::" + response.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /**
     * Test HTTP PATCH
     */
    @Test
    public void testPatch() {
        Integer status = null;
        StringBuffer response = new StringBuffer();
        try {
            status = HttpUtils.patchStatus("https://httpbin.org/get", null, null, null, response);
            System.out.println("Response: " + String.valueOf(status) + "::" + response.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /**
     * Test HTTP POST
     */
    @Test
    public void testPost() {
        Integer status = null;
        StringBuffer response = new StringBuffer();
        try {
            status = HttpUtils.postStatus("https://httpbin.org/get", null, null, null, response);
            System.out.println("Response: " + String.valueOf(status) + "::" + response.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /**
     * Test HTTP PUT
     */
    @Test
    public void testPut() {
        Integer status = null;
        StringBuffer response = new StringBuffer();
        try {
            status = HttpUtils.putStatus("https://httpbin.org/get", null, null, null, response);
            System.out.println("Response: " + String.valueOf(status) + "::" + response.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /**
     * Test HTTP TRACE
     */
    @Test
    public void testTrace() {
        Integer status = null;
        StringBuffer response = new StringBuffer();
        try {
            status = HttpUtils.traceStatus("https://httpbin.org/get", null, null, null, response);
            System.out.println("Response: " + String.valueOf(status) + "::" + response.toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
