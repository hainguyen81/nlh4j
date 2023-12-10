/*
 * @(#)JstlView.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Locale;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.exceptions.ApplicationUnderConstructionException;
import org.springframework.context.ApplicationContext;

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
    
    /** {@link SpringContextHelper} */
    @Inject
    private SpringContextHelper contextHelper;
    
    /**
     * Get the {@link SpringContextHelper} instance
     * 
     * @return the {@link SpringContextHelper} instance
     */
    protected final SpringContextHelper getContextHelper() {
    	if (contextHelper == null) {
    		contextHelper = new SpringContextHelper();
    	}
		return contextHelper;
	}

    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.view.AbstractUrlBasedView#checkResource(java.util.Locale)
     */
    @Override
    public boolean checkResource(Locale locale) throws Exception {
        String url = super.getUrl();
        if (log.isDebugEnabled()) {
            log.debug("Check existed view URL: [{}]", url);
        }
        InputStream is = Optional.ofNullable(super.getServletContext().getResourceAsStream(url))
        		.orElseGet(() -> Optional.ofNullable(contextHelper).orElseGet(() -> new SpringContextHelper(getServletContext()))
        				.searchFirstResourceAsStream(url));
        if (is == null) {
            throw new ApplicationUnderConstructionException(
                    "Could not found view from URL [" + url + "]");
        }
        return (is == null ? false : super.checkResource(locale));
    }
    
    @Override
	protected void initApplicationContext(ApplicationContext context) {
		Optional.ofNullable(context).ifPresent(c -> getContextHelper().setApplicationContext(c));
		super.initApplicationContext(context);
	}

	@Override
	protected void initServletContext(ServletContext servletContext) {
		Optional.ofNullable(servletContext).ifPresent(c -> getContextHelper().setServletContext(c));
		super.initServletContext(servletContext);
	}
}
