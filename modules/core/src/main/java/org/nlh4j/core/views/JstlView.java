/*
 * @(#)JstlView.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.machinezoo.noexception.Exceptions;

import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.exceptions.ApplicationUnderConstructionException;
import org.nlh4j.util.ExceptionUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;

import lombok.extern.slf4j.Slf4j;

/**
 * Custom {@link org.springframework.web.servlet.view.JstlView} for under construction
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Slf4j
public class JstlView extends org.springframework.web.servlet.view.JstlView implements DisposableBean, Serializable {

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

    /*
     * (non-Javadoc)
     * @see org.springframework.web.servlet.view.AbstractView#render(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	// wrap for throwing under-construction exception for global exception resolver
    	Exceptions.wrap(ex -> new ApplicationUnderConstructionException(
    			"[" + JstlView.class.getSimpleName() + "]" + ex.getMessage(), ex))
    	.run(() -> JstlView.super.render(model, request, response));
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.web.context.support.WebApplicationObjectSupport#initApplicationContext(org.springframework.context.ApplicationContext)
     */
    @Override
	protected void initApplicationContext(ApplicationContext context) {
		Optional.ofNullable(context).ifPresent(c -> getContextHelper().setApplicationContext(c));
		super.initApplicationContext(context);
	}

    /*
     * (non-Javadoc)
     * @see org.springframework.web.servlet.view.JstlView#initServletContext(javax.servlet.ServletContext)
     */
	@Override
	protected void initServletContext(ServletContext servletContext) {
		Optional.ofNullable(servletContext).ifPresent(c -> getContextHelper().setServletContext(c));
		super.initServletContext(servletContext);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public final void destroy() throws Exception {
		// release memory if necessary
		doDestroy();
	}
	
	/**
	 * Child class override this for releasing memory if necessary when destroying view
	 */
	protected void doDestroy() {
		// do nothing
	}
}
