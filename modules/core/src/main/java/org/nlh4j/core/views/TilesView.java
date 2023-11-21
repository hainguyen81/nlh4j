/*
 * @(#)TilesView.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.io.Serializable;

/**
 * Custom {@link org.springframework.web.servlet.view.tiles3.TilesView} for under construction
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class TilesView extends org.springframework.web.servlet.view.tiles3.TilesView implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;

//    /* (Non-Javadoc)
//     * @see org.springframework.web.servlet.view.AbstractUrlBasedView#checkResource(java.util.Locale)
//     */
//    @Override
//    public boolean checkResource(Locale locale) throws Exception {
//        String url = super.getUrl();
//        if (super.logger.isDebugEnabled()) {
//            super.logger.debug("Check existed view URL: '" + url + "'");
//        }
//        InputStream is = super.getServletContext().getResourceAsStream(url);
//        if (is == null) {
//            throw new ApplicationUnderConstructionException(
//                    "Could not found view from URL '" + url + "'");
//        }
//        return (is == null ? false : super.checkResource(locale));
//    }
}
