/*
 * @(#)AbstractFaviconController.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.controller;

import java.io.InputStream;
import java.text.MessageFormat;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.EncryptUtils;
import org.nlh4j.util.StreamUtils;
import org.nlh4j.util.StringUtils;

/**
 * favicon (website browser icon) controller
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@RestController
public abstract class AbstractFaviconController extends AbstractController {

    /** */
    private static final long serialVersionUID = 1L;
    /**  default favicon resource name */
    protected static final String DEFAULT_FAVICON_RESOURCE = "favicon.ico";

    /**
     * Require favicon data
     *
     * @return the favicon data or page or link
     */
    @RequestMapping(value = "/favicon", method = {
    		RequestMethod.OPTIONS,
    		RequestMethod.PATCH,
    		RequestMethod.HEAD,
    		RequestMethod.GET,
    		RequestMethod.POST
    })
    public final String favicon() {
        String path = this.getFaviconResourcePath();
        String data = (!StringUtils.hasText(path) ? DEFAULT_FAVICON_RESOURCE : path);
        if (super.getContextHelper() != null) {
            // search resource stream
            InputStream is = super.getContextHelper().searchResourceAsStream(path);
            if (is != null) {
                // convert resource stream to base64
                byte[] dataBytes = StreamUtils.toByteArray(is);
                if (!CollectionUtils.isEmpty(dataBytes)) {
                    data = EncryptUtils.base64encode(dataBytes);
                    if (StringUtils.hasText(data)) {
                        data = MessageFormat.format("data:image/x-icon;base64,{1}",data);
                    }
                }
            }
        }
        return data;
    }

    /**
     * Get the favicon resource path
     *
     * @return the favicon resource path
     */
    protected String getFaviconResourcePath() { return null; }
}
