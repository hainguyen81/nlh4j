/*
 * @(#)TilesView.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.views;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.tiles.Definition;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.renderer.DefinitionRenderer;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.Request;
import org.apache.tiles.request.render.Renderer;
import org.apache.tiles.request.servlet.ServletRequest;
import org.apache.tiles.request.servlet.ServletUtil;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.exceptions.ApplicationUnderConstructionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom {@link org.springframework.web.servlet.view.tiles3.TilesView} for under construction
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Slf4j
public class TilesView extends org.springframework.web.servlet.view.tiles3.TilesView implements Serializable {

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

    /** {@link TilesContainer} */
    @Setter
    private TilesContainer tilesContainer = null;

    /**
     * Get the {@link TilesContainer} instance
     * 
     * @return the {@link TilesContainer} instance
     */
    protected final TilesContainer getTilesContainer() {
    	return tilesContainer;
    }
    
    /** {@link Renderer} */
    @Getter(value = AccessLevel.PROTECTED)
    private Renderer renderer;
    
    @Override
    public void setRenderer(Renderer renderer) {
    	this.renderer = renderer;
    	super.setRenderer(renderer);
    }

    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.view.AbstractUrlBasedView#checkResource(java.util.Locale)
     */
    @Override
    public boolean checkResource(Locale locale) throws Exception {
    	Renderer render = Objects.requireNonNull(getRenderer(), "No Renderer set");
    	TilesContainer container = Objects.requireNonNull(getTilesContainer(), "No TilesContainer set");
    	
    	HttpServletRequest servletRequest = null;
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes) {
			servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
		}

		ApplicationContext ctx = Optional.ofNullable(super.getServletContext())
				.map(sc -> ServletUtil.getApplicationContext(sc))
				.orElseGet(() -> Optional.ofNullable(getContextHelper().getServletContext())
						.map(sc -> ServletUtil.getApplicationContext(sc)).orElse(null));
		Request request = new ServletRequest(Objects.requireNonNull(ctx, "No ApplicationContext set"), servletRequest, null) {
			@Override
			public Locale getRequestLocale() {
				return locale;
			}
		};

		// detect view definition for under-construction
        String url = super.getUrl();
        Definition tilesDef = container.getDefinition(url, request);
        if (log.isDebugEnabled()) {
            log.debug("Check existed view URL same as tiles definition name: [{}]", url);
            log.debug("- Definition: [{}]", tilesDef);
        }

        // under-construction if not found definition
        if (tilesDef == null) {
        	throw new ApplicationUnderConstructionException(
                    "Could not found tiles defintion for view [" + url + "]");
        }

		/**
		 * TODO Tile views has been loaded based on the tiles definition.<br>
		 * So no need to check the resource whether is an existing physical resource.<br>
		 * It also doesn't need prefix/suffix, it's just based on the definition name as view URL
		 */
		return render.isRenderable(url, request);
    }
    
    @Override
	protected void initApplicationContext(org.springframework.context.ApplicationContext context) {
		Optional.ofNullable(context).ifPresent(c -> {
			getContextHelper().setApplicationContext(c);
			Optional.ofNullable(c).filter(WebApplicationContext.class::isInstance)
			.map(WebApplicationContext.class::cast)
			.ifPresent(wac -> ensureTilesRenderAndContainer(wac.getServletContext()));
		});
		super.initApplicationContext(context);
	}

	@Override
	protected void initServletContext(ServletContext servletContext) {
		ensureTilesRenderAndContainer(servletContext);
		super.initServletContext(servletContext);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		ensureTilesRenderAndContainer(getServletContext());
	}

	/**
	 * Ensure for initializing context
	 * 
	 * @param sc {@link ServletContext}
	 */
	private void ensureTilesRenderAndContainer(ServletContext sc) {
		Optional.ofNullable(sc).ifPresent(c -> {
			getContextHelper().setServletContext(c);
			if (getTilesContainer() == null) {
				setTilesContainer(TilesAccess.getContainer(ServletUtil.getApplicationContext(c)));
			}
			if (getRenderer() == null) {
				setRenderer(new DefinitionRenderer(getTilesContainer()));
			}
		});
	}
}
