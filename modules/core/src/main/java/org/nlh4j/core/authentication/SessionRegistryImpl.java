/*
 * @(#)SessionRegistryImpl.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An extended class of {@link org.springframework.security.core.session.SessionRegistryImpl}
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class SessionRegistryImpl
        extends org.springframework.security.core.session.SessionRegistryImpl
        implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;
    /** SLF4J */
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
}
