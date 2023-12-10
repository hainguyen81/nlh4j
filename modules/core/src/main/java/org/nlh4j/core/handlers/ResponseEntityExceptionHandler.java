/*
 * @(#)ResponseEntityExceptionHandler.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.handlers;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.nlh4j.core.service.MessageService;
import org.nlh4j.core.servlet.ApplicationContextProvider;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.core.validation.errors.FieldErrorResource;
import org.nlh4j.exceptions.ApplicationException;
import org.nlh4j.exceptions.ApplicationLicenseException;
import org.nlh4j.exceptions.ApplicationNotSupportedException;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.exceptions.ApplicationUnderConstructionException;
import org.nlh4j.exceptions.ApplicationValidationException;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.ExceptionUtils;
import org.nlh4j.util.JsonUtils;
import org.nlh4j.util.RequestUtils;
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
import org.springframework.web.util.WebUtils;

import lombok.Getter;
import lombok.Setter;

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
    @Nullable
    public final ResponseEntity<Object> handleValidationException(ApplicationValidationException ex, WebRequest request) {
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
    
    /**
	 * Handle under-construction exception
     *
     * @param ex under-construction exception
     * @param request request
     * @return
     */
    @ExceptionHandler(value = { ApplicationUnderConstructionException.class })
    @Nullable
    public final ResponseEntity<Object> handleUnderConstructionException(ApplicationUnderConstructionException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<Object>(
        		JsonUtils.serialize(String.format("[%s] %s: %s",
        				String.valueOf(HttpStatus.NOT_IMPLEMENTED.value()), ex.getErrorCode(), ex.getMessage())),
        		headers, HttpStatus.NOT_IMPLEMENTED);
    }
    
    /**
	 * Handle license exception
     *
     * @param ex license exception
     * @param request request
     * 
     * @return
     */
    @ExceptionHandler(value = { ApplicationLicenseException.class })
    @Nullable
    public final ResponseEntity<Object> handleLicenseException(ApplicationLicenseException ex, WebRequest request) {
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<Object>(
        		JsonUtils.serialize(String.format("[%s] %s: %s",
        				String.valueOf(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value()), ex.getErrorCode(), ex.getMessage())),
        		headers, HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }
    
    /**
	 * Handle un-supported exception
     *
     * @param ex un-supported exception
     * @param request request
     * 
     * @return
     */
    @ExceptionHandler(value = { ApplicationNotSupportedException.class })
    @Nullable
    public final ResponseEntity<Object> handleNotSupportedException(ApplicationNotSupportedException ex, WebRequest request) {
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<Object>(
        		JsonUtils.serialize(String.format("[%s] %s: %s",
        				String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()), ex.getErrorCode(), ex.getMessage())),
        		headers, HttpStatus.SERVICE_UNAVAILABLE);
    }

    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler#handleExceptionInternal(java.lang.Exception, java.lang.Object, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)
     */
    @Override
    protected final ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
    	// validation exception
    	if (ExceptionUtils.isKindOfValidationException(ex)) {
    		return this.handleValidationException(
    				ExceptionUtils.getExceptionCause(ex, ApplicationValidationException.class), request);
    	}
    	
    	// under-construction exception
    	if (BeanUtils.isInstanceOf(ex, ApplicationUnderConstructionException.class)) {
    		return this.handleUnderConstructionException(
    				ExceptionUtils.getExceptionCause(ex, ApplicationUnderConstructionException.class), request);
    	}
    	
    	// license exception
    	if (BeanUtils.isInstanceOf(ex, ApplicationLicenseException.class)) {
    		return this.handleLicenseException(
    				ExceptionUtils.getExceptionCause(ex, ApplicationLicenseException.class), request);
    	}
    	
    	// un-supported exception
    	if (BeanUtils.isInstanceOf(ex, ApplicationNotSupportedException.class)) {
    		return this.handleNotSupportedException(
    				ExceptionUtils.getExceptionCause(ex, ApplicationNotSupportedException.class), request);
    	}

    	// common exception
        return handleCustomException(ex, body, headers, status, request);
    }
    
    /**
     * Child class could override this method for handling custom exception
     * 
     * @param ex to handle
     * @param body response body
     * @param headers {@link HttpHeaders}
     * @param status {@link HttpStatus}
     * @param request {@link WebRequest}
     * 
     * @return {@link ResponseEntity}
     */
    protected ResponseEntity<Object> handleCustomException(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
    	// common exception
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
        	// !ApplicationException && !ApplicationRuntimeException
        	if (!ExceptionUtils.isKindOfException(ex, ApplicationException.class)
        			&& !ExceptionUtils.isKindOfException(ex, ApplicationRuntimeException.class)) {
	    		ex = new ApplicationRuntimeException(this.getInternalServerErrorReason(), ex);

	    		// ApplicationException
        	} else if (ExceptionUtils.isKindOfException(ex, ApplicationException.class)) {
        		ex = new ApplicationException(this.getInternalServerErrorReason(), ex.getCause() != null ? ex.getCause() : ex);

	    		// ApplicationRuntimeException
        	} else if (ExceptionUtils.isKindOfException(ex, ApplicationRuntimeException.class)) {
        		ex = new ApplicationRuntimeException(this.getInternalServerErrorReason(), ex.getCause() != null ? ex.getCause() : ex);
        	}
        	RequestUtils.setRequestAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex);
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
