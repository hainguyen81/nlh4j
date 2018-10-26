/*
 * @(#)GlobalExceptionResolver.java 1.0 Feb 18, 2017
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.handlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import lombok.Getter;
import lombok.Setter;
import org.nlh4j.core.service.MessageService;
import org.nlh4j.core.servlet.ApplicationContextProvider;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.exceptions.ApplicationException;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.EncryptUtils;
import org.nlh4j.util.JsonUtils;
import org.nlh4j.util.RequestUtils;

/**
 * Global exception resolver
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@Component
@ControllerAdvice
@Singleton
public class GlobalExceptionResolver implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * slf4j
	 */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * {@link ResponseEntityExceptionHandler}
	 */
	@Inject
	protected ResponseEntityExceptionHandler entityExceptionHandler;

	/**
	 * {@link HandlerExceptionResolver}
	 */
	@Inject
	protected HandlerExceptionResolver handlerExceptionResover;

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
	 * Handle exception
	 *
	 * @param webRequest {@link WebRequest}. NULL for creating new {@link ServletWebRequest} from the specified
	 * @param request HTTP request
	 * @param response HTTP response
	 * @param handler object handler
	 * @param ex exception
	 */
	@ExceptionHandler(value = { Exception.class })
	public final void handleException(
			WebRequest webRequest, HttpServletRequest request, HttpServletResponse response,
			Exception ex, Object handler) {
		// check for real exception ?
		Exception innerEx = null;
		if (BeanUtils.isInstanceOf(ex, ApplicationException.class)
				|| BeanUtils.isInstanceOf(ex, ApplicationRuntimeException.class)) {
			innerEx = BeanUtils.safeType(ex.getCause(), Exception.class);
		}
		innerEx = (innerEx == null ? ex : innerEx);

        // check response has been commited
        if (response == null || response.isCommitted()) {
            logger.warn("Exception has been occured, but response has been committed. "
                    + "So exception will be logged by your logback configuration!!!");
            logger.error(innerEx.getMessage(), innerEx);
            return;
        }

		// check for context helper
		if (this.getContextHelper() == null && request != null
				&& request.getServletContext() != null) {
			this.contextHelper = new SpringContextHelper(request.getServletContext());
		}

		// debug if necessary
		if (logger.isDebugEnabled()) logger.debug(innerEx.getMessage(), innerEx);
		// check for web resquest
		webRequest = (webRequest == null ? new ServletWebRequest(request, response) : webRequest);
		// check for response HTML page or JSON
		boolean responsed = false;
		if (response != null && RequestUtils.isAjaxRequest(request)) {
			// handle by response entity
			ResponseEntity<Object> responseEntity = (this.entityExceptionHandler != null
					? this.entityExceptionHandler.handleException(innerEx, webRequest)
							: new ResponseEntity<Object>(this.getInternalServerErrorReason(),
									HttpStatus.INTERNAL_SERVER_ERROR));
			// status
			HttpStatus status = responseEntity.getStatusCode();
			response.setStatus(status.value());
			response.setCharacterEncoding(EncryptUtils.ENCODING_UTF8);
			// headers
			HttpHeaders headers = responseEntity.getHeaders();
			if (!CollectionUtils.isEmpty(headers)) {
				for(final Iterator<String> it = headers.keySet().iterator(); it.hasNext();) {
					String key = it.next();
					response.addHeader(key, StringUtils.collectionToCommaDelimitedString(
							headers.get(key)));
				}
			}
			// content
			Object body = responseEntity.getBody();
			body = (body == null ? this.getHttpStatusReason(status, (Object[]) null) : body);
			body = (body == null ? this.getInternalServerErrorReason() : body);
			if (body != null) {
				// check for response entity
				String responseBody = null;
				if (BeanUtils.isInstanceOf(body, String.class)) {
					responseBody = body.toString();

					// if entity is object; then serializing
				} else {
					responseBody = JsonUtils.serialize(body);
				}
				// write to response
				PrintWriter out = null;
				try {
					out = response.getWriter();
					out.println(responseBody);
					responsed = true;
				}
				catch (IOException e) {}
				finally {
					if (out != null) out.flush();
				}
			}
		}

		// response as default
		if (!responsed && ex != null) {
			if (this.handlerExceptionResover == null) {
				throw new ApplicationRuntimeException(innerEx);
			} else {
				ModelAndView mav = this.handlerExceptionResover.resolveException(
						request, response, handler, innerEx);
				if (mav != null && !mav.isEmpty()) {
					try {
					    response.setCharacterEncoding(EncryptUtils.ENCODING_UTF8);
                        mav.getView().render(mav.getModel(), request, response);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						throw new ApplicationRuntimeException(innerEx);
					}
				} else throw new ApplicationRuntimeException(innerEx);
			}
		}
	}
}
