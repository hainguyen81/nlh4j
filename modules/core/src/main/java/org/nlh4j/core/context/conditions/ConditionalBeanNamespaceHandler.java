/*
 * @(#)ConditionalBeanNamespaceHandler.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.context.conditions;

import java.io.Serializable;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Support for conditional bean in XML spring context configuration
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class ConditionalBeanNamespaceHandler
        extends NamespaceHandlerSupport
        implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * XML conditional bean tag name.<br>
     * See from resource file: org/nlh4j/core/context/conditionalbean.xsd
     */
    public static final String CONTEXT_BEAN_TAG = "if";

    /* (Non-Javadoc)
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    @Override
    public void init() {
        super.registerBeanDefinitionParser(CONTEXT_BEAN_TAG, new ConditionalBeanDefinitionParser());
    }
}
