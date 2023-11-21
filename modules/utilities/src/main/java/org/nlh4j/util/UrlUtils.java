/*
 * @(#)UrlUtils.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.util.Assert;

/**
 * {@link URL}/{@link URI} utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class UrlUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Build {@link URI}
     *
     * @param sUrl the {@link URL} path
     * @param params the {@link URL} parameters
     * @return {@link URI}
     * @throws MalformedURLException thrown if the specified {@link URL} is invalid
     * @throws URISyntaxException thrown if could not build URI
     */
    public static URI buildUri(String sUrl, List<NameValuePair> params)
            throws MalformedURLException, URISyntaxException {
        Assert.hasText(sUrl, "URL");
        URL url = new URL(sUrl);
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setScheme(url.getProtocol());
        uriBuilder.setHost(url.getHost());
        uriBuilder.setPort(url.getPort());
        uriBuilder.setPath(url.getPath());
        if (!CollectionUtils.isEmpty(params)) {
            uriBuilder.addParameters(params);
        }
        uriBuilder.setFragment(url.getRef());
        return uriBuilder.build();
    }

    /**
     * Check the specified URL whether is valid
     *
     * @param sUrl the URL to check
     *
     * @return true for valid; else false
     */
    public static boolean isUrlVaid(String sUrl) {
        return (StringUtils.hasText(sUrl)
                && UrlValidator.getInstance().isValid(sUrl));
    }
}
