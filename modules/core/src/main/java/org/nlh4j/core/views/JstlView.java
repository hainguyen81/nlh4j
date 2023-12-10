/*
 * @(#)JstlView.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Locale;

import org.nlh4j.exceptions.ApplicationUnderConstructionException;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom {@link org.springframework.web.servlet.view.JstlView} for under construction
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Slf4j
public class JstlView extends org.springframework.web.servlet.view.JstlView implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;

    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.view.AbstractUrlBasedView#checkResource(java.util.Locale)
     */
    @Override
    public boolean checkResource(Locale locale) throws Exception {
        String url = super.getUrl();
        if (log.isDebugEnabled()) {
            log.debug("Check existed view URL: [{}]", url);
        }
        InputStream is = super.getServletContext().getResourceAsStream(url);
        if (is == null) {
            throw new ApplicationUnderConstructionException(
                    "Could not found view from URL [" + url + "]");
        }
        return (is == null ? false : super.checkResource(locale));
    }
}
