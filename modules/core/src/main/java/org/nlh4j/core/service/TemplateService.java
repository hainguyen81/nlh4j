/*
 * @(#)TemplateService.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.service;

import java.io.Serializable;

import freemarker.template.Configuration;

/**
 * Service interface for the freemarker
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public interface TemplateService extends Serializable {
	/**
	 * Gets freemarker template {@link Configuration}
	 *
	 * @return The {@link Configuration}
	 */
	Configuration getConfiguration();
	/**
	 * Assigns data model to the specified template
	 *
	 * @param template
	 * 			The template file name
	 * @param encode
	 * 			The template encode
	 * @param dataModelViewName
	 * 			The data model that is used in template. Maybe NULL or empty
	 * @param dataModel
	 * 			The data model to assign
	 *
	 * @return The template content that had been assigned data or null
	 */
	StringBuffer assignTemplate(String template, String encode, String dataModelViewName, Object dataModel);
	/**
	 * Assigns data model to the specified template
	 * (with default encoding UTF-8)
	 *
	 * @param template
	 * 			The template file name
	 * @param dataModelViewName
	 * 			The data model that is used in template. Maybe NULL or empty
	 * @param dataModel
	 * 			The data model to assign
	 *
	 * @return The template content that had been assigned data or null
	 */
	StringBuffer assignTemplate(String template, String dataModelViewName, Object dataModel);
}
