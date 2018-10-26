/*
 * @(#)ConcurrentSessionControlStrategy.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;

/**
 * An alias of {@link ConcurrentSessionControlAuthenticationStrategy}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class ConcurrentSessionControlStrategy
        extends ConcurrentSessionControlAuthenticationStrategy
        implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    /**
     * SLF4J
     */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /** {@link SessionRegistry} */
    private final SessionRegistry sessionRegistry;

    /**
     * Initialize a new instance of {@link ConcurrentSessionControlStrategy}
     *
     * @param sessionRegistry {@link SessionRegistry}
     */
    @Autowired(required = true)
    public ConcurrentSessionControlStrategy(SessionRegistry sessionRegistry) {
        super(sessionRegistry);
        this.sessionRegistry = sessionRegistry;
    }

    /**
	 * Get the sessionRegistry
	 *
	 * @return the sessionRegistry
	 */
	protected final SessionRegistry getSessionRegistry() {
		return this.sessionRegistry;
	}
}
