/*
 * @(#)ResponseEntityExceptionHandler.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.handlers;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import lombok.Getter;
import lombok.Setter;
import org.nlh4j.core.service.MessageService;
import org.nlh4j.core.servlet.ApplicationContextProvider;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.core.validation.errors.FieldErrorResource;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.exceptions.ApplicationValidationException;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.JsonUtils;

/**
 * Handle REST API response entity exception (request AJAX and response JSON)<br>
 * An extended class of {@link org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler}
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Component
public class ResponseEntityExceptionHandler
		extends org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
		implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * slf4j
	 */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * {@link MessageService}
	 */
	@Inject
	protected MessageService messageService;

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
     * The {@link HttpStatus#INTERNAL_SERVER_ERROR} reason phase key from resource bundle
     */
    @Getter
    @Setter
    private String internalServerErrorReasonKey;

    /**
     * The {@link HttpStatus#INTERNAL_SERVER_ERROR} reason phase.
     * TODO children classes maybe override this method for customizing reason phase
     */
    @Setter
    private String internalServerErrorReason;

    /**
     * Get the {@link HttpStatus#INTERNAL_SERVER_ERROR} reason phase
     * @return the {@link HttpStatus#INTERNAL_SERVER_ERROR} reason phase
     */
    public String getInternalServerErrorReason() {
        if (!StringUtils.hasText(this.internalServerErrorReason)) {
            MessageService msgSrv = this.messageService;
            msgSrv = (msgSrv == null ? this.findBean(MessageService.class) : msgSrv);
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
    public final String getHttpStatusReason(HttpStatus status, Object... args) {
        if (status == null) return null;
        // require message from bundle with status code as message key
        MessageService msgSrv = this.messageService;
        msgSrv = (msgSrv == null ? this.findBean(MessageService.class) : msgSrv);
        String message = msgSrv.getMessage(String.valueOf(status.value()), args, null);
        if (!StringUtils.hasText(message)) {
            message = status.getReasonPhrase();
        }
        return message;
    }

	/**
	 * Handle validation exception
     *
     * @param ex validation exception
     * @param request request
     * @return
     */
    @ExceptionHandler(value = { ApplicationValidationException.class })
    protected final ResponseEntity<Object> handleValidationException(ApplicationValidationException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Object body = null;
        List<FieldErrorResource> fieldErrors = new LinkedList<FieldErrorResource>();
        for(FieldError error : ex.getErrors().getFieldErrors()) {
            try {
                FieldErrorResource fieldError = BeanUtils.copyBean(error, FieldErrorResource.class);
                fieldError.setResource(error.getObjectName());
                fieldError.setMessage(error.getDefaultMessage());
                fieldErrors.add(fieldError);
            } catch (Exception e) {
                break;
            }
        }
        if (body == null && !CollectionUtils.isEmpty(fieldErrors)) {
            body = JsonUtils.serialize(fieldErrors);
        } else if (body == null) {
            body = JsonUtils.serialize(ex.getErrors().getAllErrors());
        }
        return new ResponseEntity<Object>(body, headers, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler#handleExceptionInternal(java.lang.Exception, java.lang.Object, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
    	// validation exception
    	if (BeanUtils.isInstanceOf(ex, ApplicationValidationException.class)) {
    		return this.handleValidationException(
    				BeanUtils.safeType(ex, ApplicationValidationException.class), request);
    	}

    	// common exception
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            String msg = this.getInternalServerErrorReason();
            ex = new ApplicationRuntimeException(msg, ex);
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
