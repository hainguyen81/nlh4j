/*
 * @(#)ConditionalBeanDefinitionParser.java 1.0 Jan 5, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.context.conditions;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.NumberUtils;
import org.nlh4j.util.StringUtils;

/**
 * Support for conditional bean in XML spring context configuration
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class ConditionalBeanDefinitionParser implements Serializable, BeanDefinitionParser {

    /** */
    private static final long serialVersionUID = 1L;

    /** Default placeholder prefix: "${" */
    private static final String DEFAULT_PLACEHOLDER_PREFIX = "$" + "{";
    /** Default placeholder suffix: "}" */
    private static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
    /** Default placeholder suffix: "}" */
    private static final String DEFAULT_CHILD_ELEMENT_TAG = "bean";

    /**
     * Parse the "if" ({@link ConditionalBeanNamespaceHandler}) element and check the mandatory "test" attribute. If
     * the system property named by test is null or empty (i.e. not defined)
     * then return null, which is the same as not defining the bean.
     *
     * @param element configuration XML element
     * @param parserContext context parser
     *
     * @return definition bean or null
     */
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        if (DomUtils.nodeNameEquals(element, ConditionalBeanNamespaceHandler.CONTEXT_BEAN_TAG)) {
            String test = element.getAttribute("test");
            if (getProperty(test, parserContext)) {
                Element bean = null;
                try {
                    bean = DomUtils.getChildElementByTagName(element, DEFAULT_CHILD_ELEMENT_TAG);
                } catch (Exception e) {
                    bean = null;
                } finally {
                    // custom element if necessary
                    if (bean == null) {
                        List<Element> beans = DomUtils.getChildElements(element);
                        bean = (!CollectionUtils.isEmpty(beans) ? beans.get(0) : null);
                    }
                }
                if (bean != null) {
                    return parseAndRegisterBean(bean, parserContext);
                }
            }
        }
        return null;
    }

    /**
     * Get the value of a named system property (it may not be defined).
     *
     * @param propName The name of a system property. The property may
     * optionally be surrounded in Ant/EL-style brackets. e.g. "${propertyname}"
     * @param parserContext context parser
     *
     * @return the value of a named system property
     */
    private boolean getProperty(String propName, ParserContext parserContext) {
        if (!StringUtils.hasText(propName)) return Boolean.FALSE;
        String propVal = null;
        // get from enviroment
        if (!StringUtils.hasText(propVal)) {
            propVal = System.getenv(propName);
            if (propName.startsWith(DEFAULT_PLACEHOLDER_PREFIX)) {
                if (propName.endsWith(DEFAULT_PLACEHOLDER_SUFFIX)) {
                    propVal = System.getenv(
                            propName.substring(DEFAULT_PLACEHOLDER_PREFIX.length(),
                                    propName.length() - DEFAULT_PLACEHOLDER_SUFFIX.length()));
                }
            }
        }
        // get from system
        if (!StringUtils.hasText(propVal)) {
            propVal = System.getProperty(propName);
            if (propName.startsWith(DEFAULT_PLACEHOLDER_PREFIX)) {
                if (propName.endsWith(DEFAULT_PLACEHOLDER_SUFFIX)) {
                    propVal = System.getProperty(
                            propName.substring(DEFAULT_PLACEHOLDER_PREFIX.length(),
                                    propName.length() - DEFAULT_PLACEHOLDER_SUFFIX.length()));
                }
            }
        }
        return NumberUtils.toBool(propVal);
    }

    /**
     * Parse and register bean.
     *
     * @param element element configuration XML element
     * @param parserContext context parser
     *
     * @return definition bean or null
     */
    private BeanDefinition parseAndRegisterBean(Element element, ParserContext parserContext) {
        BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
        BeanDefinitionHolder holder = delegate.parseBeanDefinitionElement(element);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, parserContext.getRegistry());
        return holder.getBeanDefinition();
    }
}
