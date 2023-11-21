/*
 * @(#)SpringProfileTag.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.taglibs;

import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.tags.RequestContextAwareTag;

import lombok.Getter;
import lombok.Setter;
import org.nlh4j.util.CollectionUtils;

/**
 * Superclass for all tags that require a {@link RequestContext}.
 *
 * <p>The {@code RequestContext} instance provides easy access
 * to current state like the
 * {@link org.springframework.web.context.WebApplicationContext},
 * the {@link java.util.Locale}, the
 * {@link org.springframework.ui.context.Theme}, etc.
 *
 * <p>Mainly intended for
 * {@link org.springframework.web.servlet.DispatcherServlet} requests;
 * will use fallbacks when used outside {@code DispatcherServlet}.
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @see org.springframework.web.servlet.support.RequestContext
 * @see org.springframework.web.servlet.DispatcherServlet
 */
public class SpringProfileTag extends RequestContextAwareTag {

    /** */
    private static final long serialVersionUID = 1L;

	/** default profile delimited */
	private static final String DEFAULT_PROFILE_DELIMITED = ",";

	/** profile delimiter */
	@Setter
	private String delimiter;
	/**
	 * Get the delimiter
	 *
	 * @return the delimiter
	 */
	public String getDelimiter() {
		this.delimiter = (!StringUtils.hasText(this.delimiter)
				? DEFAULT_PROFILE_DELIMITED : this.delimiter);
		return this.delimiter;
	}

	/** require spring profile to check */
	@Getter
	@Setter
	private String profile;

	/* (Non-Javadoc)
	 * @see org.springframework.web.servlet.tags.RequestContextAwareTag#doStartTagInternal()
	 */
	@Override
	protected int doStartTagInternal() throws Exception {
		final RequestContext reqCtx = super.getRequestContext();
		final WebApplicationContext wac = (reqCtx == null ? null : reqCtx.getWebApplicationContext());
		final Environment env = (wac == null ? null : wac.getEnvironment());
        if (env != null) {
            final String[] profiles = env.getActiveProfiles();
            final String[] needProfiles = StringUtils.delimitedListToStringArray(
            		this.getProfile(), this.getDelimiter());
            if (!CollectionUtils.isEmpty(needProfiles)) {
            	for(String needProfile : needProfiles) {
            		if (CollectionUtils.indexOf(profiles, needProfile) >= 0) {
                        return EVAL_BODY_INCLUDE;
                    }
            	}
            }
        }
        return SKIP_BODY;
	}

}
