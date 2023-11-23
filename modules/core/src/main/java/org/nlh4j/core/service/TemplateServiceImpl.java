/*
 * @(#)TemplateServiceImpl.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.nlh4j.core.context.profiles.SpringProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.Getter;
import lombok.Setter;

/**
 * The implement of the {@link TemplateService} interface.<br>
 * FIXME Create instance using dynamic by disabling/enabling template module
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@Service
@Profile(value = { SpringProfiles.PROFILE_TEMPLATE, SpringProfiles.PROFILE_FULL })
public class TemplateServiceImpl extends AbstractService implements TemplateService {

	/**
	 * Default serial UID
	 */
	private static final long serialVersionUID = 1L;
	private static final String ENCODING_UTF8 = "UTF-8";

	/**
	 * @see Configuration
	 * @see FreeMarkerConfig
	 */
	@Getter
	@Setter
	@Resource
	private transient Configuration configuration;

	/**
	 * Initializes a new instance of the {@link TemplateServiceImpl} class.
	 *
	 */
	public TemplateServiceImpl() {}
    /**
     * Initializes a new instance of the {@link TemplateServiceImpl} class.
     *
     * @param freemarkerConfiguration the configuration
     */
	@Autowired
    public TemplateServiceImpl(Configuration freemarkerConfiguration) {
        this.configuration = freemarkerConfiguration;
    }

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.service.TemplateService#assignTemplate(java.lang.String, java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public StringBuffer assignTemplate(String template, String encode, String dataModelViewName, Object dataModel) {
		if (dataModel != null) {
			StringBuffer templateContent = new StringBuffer();
			try {
				String fExt = FilenameUtils.getExtension(template);
				if (!"ftl".equalsIgnoreCase(fExt)) template += ".ftl";
				if (StringUtils.hasText(dataModelViewName)) {
					Map<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put(dataModelViewName, dataModel);
					templateContent.append(FreeMarkerTemplateUtils.processTemplateIntoString(
				    		this.getConfiguration().getTemplate(template, encode), dataMap));
				} else {
					templateContent.append(FreeMarkerTemplateUtils.processTemplateIntoString(
				    		this.getConfiguration().getTemplate(template, encode), dataModel));
				}
			}
			catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			catch (TemplateException e) {
			    logger.error(e.getMessage(), e);
			}
			// Returns template content that had been assigned data
			return templateContent;
		}
		return null;
	}

	/*
	 * (Non-Javadoc)
	 * @see org.nlh4j.core.service.TemplateService#assignTemplate(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public StringBuffer assignTemplate(String template, String dataModelViewName, Object dataModel) {
		return this.assignTemplate(template, ENCODING_UTF8, dataModelViewName, dataModel);
	}
}
