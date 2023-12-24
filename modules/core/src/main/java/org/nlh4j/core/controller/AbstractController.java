/*
 * @(#)AbstractController.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nlh4j.core.context.profiles.SpringProfiles;
import org.nlh4j.core.dto.bindeditor.CustomNumberEditor;
import org.nlh4j.core.dto.bindeditor.TimeStampEditor;
import org.nlh4j.core.handlers.RequestMappingHandlerMapping;
import org.nlh4j.core.service.MessageService;
import org.nlh4j.core.service.TemplateService;
import org.nlh4j.core.service.TemplateServiceImpl;
import org.nlh4j.core.service.mail.MailService;
import org.nlh4j.core.service.mail.MailServiceImpl;
import org.nlh4j.core.servlet.ApplicationContextProvider;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.exceptions.ApplicationValidationException;
import org.nlh4j.support.IGenericTypeSupport;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.RequestUtils;
import org.nlh4j.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestController;

import freemarker.template.Configuration;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract rest service controller
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@RestController
public abstract class AbstractController implements IGenericTypeSupport {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * slf4j
	 */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * {@link MailService}
	 */
	private MailService mailService;
	/**
     * Get the mail service {@link MailService}
     *
     * @return the mail service {@link MailService}
     */
	@Profile(value = { SpringProfiles.PROFILE_MAIL, SpringProfiles.PROFILE_FULL })
    protected final MailService getMailService() {
        if (this.mailService == null) {
            this.mailService = this.findBean(MailService.class);
            if (this.mailService == null) {
                JavaMailSender sender = this.findBean(JavaMailSender.class);
                if (sender != null) {
                    this.mailService = new MailServiceImpl(sender);
                }
            }
        }
        if (this.mailService == null) return null;
        synchronized (this.mailService) {
            return this.mailService;
        }
    }

	/**
	 * {@link TemplateService}
	 */
	private TemplateService templateService;
	/**
     * Get the template service {@link TemplateService}
     *
     * @return the template service {@link TemplateService}
     */
	@Profile(value = { SpringProfiles.PROFILE_TEMPLATE, SpringProfiles.PROFILE_FULL })
    protected final TemplateService getTemplateService() {
        if (this.templateService == null) {
            this.templateService = this.findBean(TemplateService.class);
            if (this.templateService == null) {
                Configuration fmCfg = this.findBean(Configuration.class);
                if (fmCfg != null) {
                    this.templateService = new TemplateServiceImpl(fmCfg);
                }
            }
        }
        if (this.templateService == null) return null;
        synchronized (this.templateService) {
            return this.templateService;
        }
    }

	/**
	 * {@link MessageService}
	 */
	@Inject
	protected MessageService messageService;
	/**
     * {@link RequestMappingHandlerMapping}
     */
	@Inject
    protected RequestMappingHandlerMapping handlerMapping;
    //	/**
    //	 * {@link RequestMappingHandlerMapping}
    //	 */
    //	private RequestMappingHandlerMapping handlerMapping;
    //	/**
    //     * Get the handlerMapping
    //     *
    //     * @return the handlerMapping
    //     */
    //    protected final RequestMappingHandlerMapping getHandlerMapping() {
    //        if (this.handlerMapping == null) {
    //            this.handlerMapping = this.findBean(RequestMappingHandlerMapping.class);
    //        }
    //        if (this.handlerMapping == null) return null;
    //        synchronized (this.handlerMapping) {
    //            return this.handlerMapping;
    //        }
    //    }
    /**
     * {@link HttpServletRequest}
     */
	@Inject
    private HttpServletRequest request;
	/**
	 * Get the request
	 *
	 * @return the request
	 */
	protected final HttpServletRequest getRequest() {
		if (this.request == null) {
			this.request = RequestUtils.getHttpRequest();
		}
		return this.request;
	}
    /**
     * {@link HttpSession}
     */
	@Resource
    private HttpSession session;
	/**
	 * Get the session
	 *
	 * @return the session
	 */
	protected final HttpSession getSession() {
		if (this.session == null) {
			this.session = SessionUtils.getHttpSession();
		}
		return this.session;
	}

	/**
     * {@link ApplicationContextProvider}
     */
	@Inject
    protected ApplicationContextProvider contextProvider;
    /**
     * {@link SpringContextHelper}
     */
	@Inject
    private SpringContextHelper contextHelper;
    /**
     * Get the SPRING context helper
     * @return the SPRING context helper
     */
    protected final SpringContextHelper getContextHelper() {
    	if (this.contextHelper == null && this.contextProvider != null
    			&& this.contextProvider.getApplicationContext() != null) {
    		this.contextHelper = new SpringContextHelper(this.contextProvider.getApplicationContext());
        }
    	else if (contextHelper == null && this.getRequest() != null
    			&& this.getRequest().getServletContext() != null) {
    		this.contextHelper = new SpringContextHelper(this.getRequest().getServletContext());
        }
    	if (this.contextHelper == null) return null;
        synchronized (contextHelper) {
            return contextHelper;
        }
    }
    /**
     * Get bean by the specified bean class
     *
     * @param beanClass the bean class to parse from context
     *
     * @return bean or NULL
     */
    protected final <K> K findBean(Class<K> beanClass) {
        return (this.getContextHelper() == null ? null : this.getContextHelper().searchBean(beanClass));
    }
    /**
     * Get bean by the specified bean class
     *
     * @param beanClass the bean class to parse from context
     *
     * @return bean or NULL
     */
    protected final <K> List<K> findBeans(Class<K> beanClass) {
        return (this.getContextHelper() == null ? null : this.getContextHelper().searchBeans(beanClass));
    }

    /**
     * The {@link HttpStatus}.INTERNAL_SERVER_ERROR reason phase key from resource bundle
     */
    @Getter
    @Setter
    private String internalServerErrorReasonKey;

    /**
     * The {@link HttpStatus}.INTERNAL_SERVER_ERROR reason phase.
     * TODO children classes maybe override this method for customizing reason phase
     */
    @Setter
    private String internalServerErrorReason;
    /**
     * Get the {@link HttpStatus}.INTERNAL_SERVER_ERROR reason phase
     * @return the {@link HttpStatus}.INTERNAL_SERVER_ERROR reason phase
     */
    public String getInternalServerErrorReason() {
        if (!StringUtils.hasText(this.internalServerErrorReason)) {
            MessageService msgSrv = this.messageService;
            SpringContextHelper helper = this.getContextHelper();
            msgSrv = (msgSrv == null && helper != null ? helper.searchBean(MessageService.class) : msgSrv);
            String msgKey = this.getInternalServerErrorReasonKey();
            if (!StringUtils.hasText(msgKey)) msgKey = String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
            if (StringUtils.hasText(msgKey)) {
                this.internalServerErrorReason = msgSrv.getMessage(
                        this.getInternalServerErrorReasonKey(), (Object[]) null, null);
            }
        }
        if (!StringUtils.hasText(this.internalServerErrorReason)) {
            this.internalServerErrorReason = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return this.internalServerErrorReason;
    }

    /**
     * Get the {@link HttpStatus} reason phase
     *
     * @param status {@link HttpStatus} to parse reason
     * @param args message arguments if necessary
     *
     * @return the {@link HttpStatus} reason phase
     */
    public String getHttpStatusReason(HttpStatus status, Object... args) {
        if (status == null) return null;
        // require message from bundle with status code as message key
        MessageService msgSrv = this.messageService;
        SpringContextHelper helper = this.getContextHelper();
        msgSrv = (msgSrv == null && helper != null ? helper.searchBean(MessageService.class) : msgSrv);
        String message = msgSrv.getMessage(String.valueOf(status.value()), args, null);
        if (!StringUtils.hasText(message)) {
            message = status.getReasonPhrase();
        }
        return message;
    }

    /**
	 * Support for checking the binding result
	 *
	 * @param result to check
	 */
	protected final void checkBinding(BindingResult result) {
		// check by bean validation
		if (result != null && result.hasErrors()) {
			//	return new ResponseEntity<String>(
			//			JsonUtils.serialize(result.getAllErrors()),
			//			HttpStatus.EXPECTATION_FAILED);
			throw new ApplicationValidationException(result);
		}
	}

    /*****************************************************************************
     * パラメータコンバータ
     *****************************************************************************/

    /**
     * Initializes data binder
     *
     * @param binder data binder
     */
    @InitBinder
    @SuppressWarnings("unchecked")
    protected void initBinder(WebDataBinder binder) {
    	// 一般的な日付·時刻パターン
    	binder.registerCustomEditor(
    			Timestamp.class,
    			new TimeStampEditor("yyyy/MM/dd HH:mm"));
    	// 一般的な日付·時刻パターン
    	binder.registerCustomEditor(
    			Time.class,
    			new TimeStampEditor("HH:mm"));
    	// 一般的な日付·時刻パターン
    	binder.registerCustomEditor(
    			Date.class,
    			new TimeStampEditor("yyyy/MM/dd"));
    	// 一般数パターン
    	binder.registerCustomEditor(
    			BigDecimal.class,
    			new CustomNumberEditor(
    					BigDecimal.class, NumberFormat.getInstance(LocaleContextHolder.getLocale()), Boolean.TRUE));
    	// カスタム定義された日付と時刻のパターン
    	Map<String, Map<Class<?>, String>> dtPatterns = this.bindTimestampPattern();
    	if (!CollectionUtils.isEmpty(dtPatterns)) {
			for(final Iterator<String> it = dtPatterns.keySet().iterator(); it.hasNext();) {
				String field = it.next();
				if (StringUtils.hasText(field)) {
					for(final Iterator<Class<?>> it2 = dtPatterns.get(field).keySet().iterator(); it2.hasNext();) {
						Class<?> registType = it2.next();
						String pattern = dtPatterns.get(field).get(registType);
						if (registType != null && StringUtils.hasText(pattern)
								&& BeanUtils.isInstanceOf(registType, java.util.Date.class)) {
				    		binder.registerCustomEditor(registType, field, new TimeStampEditor(pattern));
		    			}
					}
				}
			}
    	}
    	// カスタム定義された数のパターン
    	Map<String, Map<Class<?>, String>> numPatterns = this.bindNumberPattern();
    	if (!CollectionUtils.isEmpty(numPatterns)) {
			for(final Iterator<String> it = numPatterns.keySet().iterator(); it.hasNext();) {
				String field = it.next();
				if (StringUtils.hasText(field)) {
					for(final Iterator<Class<?>> it2 = numPatterns.get(field).keySet().iterator(); it2.hasNext();) {
						Class<?> registType = it2.next();
						String pattern = numPatterns.get(field).get(registType);
						if (registType != null && StringUtils.hasText(pattern)
								&& BeanUtils.isInstanceOf(registType, Number.class)) {
				    		binder.registerCustomEditor(
				    				registType, field,
				    				new CustomNumberEditor((Class<? extends Number>) registType, pattern, Boolean.TRUE));
		    			}
					}
				}
			}
    	}
    }

    /**
     * カスタム定義された日付と時刻のパターン
     * @return キー：プロパティ名 － 値：日付と時刻のパターン
     */
    protected Map<String, Map<Class<?>, String>> bindTimestampPattern() {
    	return null;
    }
    /**
     * カスタム定義された日付と時刻のパターン
     * @return キー：プロパティ名 － 値：日付と時刻のパターン
     */
    protected Map<String, Map<Class<?>, String>> bindNumberPattern() {
    	return null;
    }
}
