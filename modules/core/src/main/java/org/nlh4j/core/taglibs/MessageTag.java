/*
 * @(#)MessageTag.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.taglibs;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.math.NumberUtils;
import org.nlh4j.util.RequestUtils;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.util.WebUtils;

/**
 * Extended message JSP tag for application.
 * Some format message key:
 * . System:		SYS_MSG_ID_ERR_0000# - system message
 *                  SYS_MSG_ID_INF_0000# - info message
 *                  SYS_MSG_ID_WARN_0000# - warning message
 *                  SYS_MSG_ID_CONF_0000# - confirm message
 * . Common:        COM_MSG_ID_ERR_0000# - error message
 *                  COM_MSG_ID_INF_0000# - info message
 *                  COM_MSG_ID_WARN_0000# - warning message
 *                  COM_MSG_ID_CONF_0000# - confirm message
 * . Application:   APP_MSG_ID_ERR_0000# - error message
 *                  APP_MSG_ID_INF_0000# - info message
 *                  APP_MSG_ID_WARN_0000# - warning message
 *                  APP_MSG_ID_CONF_0000# - confirm message
 * . Screen:        &lt;Screen ID&gt;_MSG_ID_ERR_0000# - error message
 *                  &lt;Screen ID&gt;_MSG_ID_INF_0000# - info message
 *                  &lt;Screen ID&gt;_MSG_ID_WARN_0000# - warning message
 *                  &lt;Screen ID&gt;_MSG_ID_CONF_0000# - confirm message
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class MessageTag extends org.springframework.web.servlet.tags.MessageTag {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Message category enumeration
     */
    public enum MSG_CATEGORY {
        NONE
        , SYSTEM
        , COMMON
        , APPLICATION
        , SCREEN
    }
    /**
     * Message type enumeration
     */
    public enum MSG_TYPE {
        INFO("INF")
        , ERROR("ERR")
        , WARN("WARN")
        , CONFIRM("CONF")
        ;

        private final String value;
        private MSG_TYPE(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }
    
    /*************************************************
     * MESSAGE CODE
     *************************************************/
    private String code;
    @Override
    public void setCode(String code) {
    	this.code = code; // keep the message code for system error case
    	super.setCode(code);
    }
    
    /*************************************************
     * SYSTEM ERROR
     *************************************************/
    private boolean systemError = false;
    /**
     * Set default message category as string: SYSTEM.
     * If true, it will turn the default system error message: SYS_MSG_ID_ERR_99999
     *
     * @param systemError true for system error
     */
    public void setSystemError(boolean systemError) {
		this.systemError = systemError;
	}

    /*************************************************
     * MESSAGE CATEGORY
     *************************************************/
    private MSG_CATEGORY msgCategory = MSG_CATEGORY.NONE;
    /**
     * Set message category as string: SYSTEM, COMMON, APPLICATION, SCREEN.
     * If message category is invalid; then just using message key as &lt;code&gt; property (non-format)
     *
     * @param msgCategory message category
     */
    public void setCategory(String msgCategory) {
        msgCategory = (StringUtils.hasText(msgCategory) ? msgCategory.trim().toUpperCase() : "");
        MSG_CATEGORY category = MSG_CATEGORY.valueOf(msgCategory);
        category = (category == null ? MSG_CATEGORY.NONE : category);
        this.msgCategory = category;
    }
    /**
     * Set message category as integer: 0 - SYSTEM, 1 - COMMON, 2 - APPLICATION, 3 - SCREEN.
     * If message category is invalid; then just using message key as &lt;code&gt; property (non-format)
     *
     * @param msgCategory message category
     */
    public void setCategoryNumber(Integer msgCategory) {
        MSG_CATEGORY category = MSG_CATEGORY.NONE;
        if (msgCategory != null && 0 <= msgCategory && msgCategory < 4) {
            switch(msgCategory) {
                case 0: {
                    category = MSG_CATEGORY.SYSTEM;
                    break;
                }
                case 1: {
                    category = MSG_CATEGORY.COMMON;
                    break;
                }
                case 2: {
                    category = MSG_CATEGORY.APPLICATION;
                    break;
                }
                case 3: {
                    category = MSG_CATEGORY.SCREEN;
                    break;
                }
            }
        }
        this.msgCategory = category;
    }

    /*************************************************
     * MESSAGE TYPE
     *************************************************/
    private MSG_TYPE msgType = MSG_TYPE.INFO;
    /**
     * Set message type as string: INFO, ERROR, WARN, CONFIRM.
     * If message type is invalid; then just using INFO message type as default
     *
     * @param msgType message category
     */
    public void setType(String msgType) {
        msgType = (StringUtils.hasText(msgType) ? msgType.trim().toUpperCase() : "");
        MSG_TYPE type = MSG_TYPE.valueOf(msgType);
        type = (type == null ? MSG_TYPE.INFO : type);
        this.msgType = type;
    }
    /**
     * Set message category as integer: 1 - ERROR, 2 - WARN, 3 - CONFIRM.
     * If message category is invalid; then just using message key as &lt;code&gt; property (non-format)
     *
     * @param msgType message category
     */
    public void setTypeNumber(Integer msgType) {
        MSG_TYPE type = MSG_TYPE.INFO;
        if (msgType != null && 0 < msgType && msgType < 4) {
            switch(msgType) {
                case 1: {
                    type = MSG_TYPE.ERROR;
                    break;
                }
                case 2: {
                    type = MSG_TYPE.WARN;
                    break;
                }
                case 3: {
                    type = MSG_TYPE.CONFIRM;
                    break;
                }
            }
        }
        this.msgType = type;
    }

    /*************************************************
     * MESSAGE SCREEN
     *************************************************/
    private String screenId = "SCR";
    /**
     * Set screen identity for SCREEN message type.
     * if null or empty for not specify; then default is "SCR"
     *
     * @param screenId screen identity
     */
    public void setScreen(String screenId) {
        this.screenId = (StringUtils.hasText(screenId) ? screenId : "SCR");
    }

    /*************************************************
     * MESSAGE NUMBER
     *************************************************/
    private int msgNum = 0;
    /**
     * Set message number. default is 0.
     *
     * @param msgNum message number
     */
    public void setNumber(int msgNum) {
        this.msgNum = Math.max(msgNum, 0);
    }

    /*************************************************
     * MESSAGE NUMBER LENGTH
     *************************************************/
    private int msgNumLength = 5;
    /**
     * Set message number length. default is 5.
     * if number &lt;= 0; then default is 5.
     *
     * @param msgNumLen message number length
     */
    public void setNumLen(int msgNumLen) {
        msgNumLen = (msgNumLen <= 0 ? 5 : msgNumLen);
        this.msgNumLength = msgNumLen;
    }

    /*************************************************
     * RESOLVE MESSAGE
     *************************************************/
    /* (Non-Javadoc)
     * @see org.springframework.web.servlet.tags.MessageTag#resolveMessage()
     */
    @Override
    protected String resolveMessage() throws JspException, NoSuchMessageException {
    	// detect whether is default system error
    	if (this.systemError) {
			setCategoryNumber(0);
			setTypeNumber(1);
			// try to detect message number
			if (this.msgNum <= 0 && StringUtils.hasText(this.code)) {
				setNumber(NumberUtils.toInt(this.code, this.msgNum));
			}
			if (this.msgNum <= 0) {
				setNumber(99999);
			}

			// try to resolve by Exception/HttpStatus to parse reason
			String arguments = this.code;
			Exception exception = tryToDetectPageException();
			// by page exception
			if (exception != null) {
				arguments = String.format("%s - %s", arguments, exception.getMessage());

				// by HttpStatus
			} else {
				HttpStatus status = HttpStatus.resolve(msgNum);
				status =Optional.ofNullable(status).orElseGet(() -> HttpStatus.resolve(NumberUtils.toInt(this.code, -1)));
				arguments = Optional.ofNullable(status).map(s -> String.format("%s - %s", String.valueOf(s.value()), s.getReasonPhrase())).orElse(arguments);
			}
			setArguments(arguments);
		}

        // detect for overriding message code
        if (this.msgCategory != null && !MSG_CATEGORY.NONE.equals(this.msgCategory)) {
            String code = null;
            String numFmt = ("%0" + String.valueOf(this.msgNumLength) + "d");
            switch(this.msgCategory) {
                case SYSTEM: {
                    this.screenId = "SYS";
                    break;
                }
                case COMMON: {
                    this.screenId = "COM";
                    break;
                }
                case APPLICATION: {
                    this.screenId = "APP";
                    break;
                }
                default: {
                    break;
                }
            }
            // formatted message code
            code = MessageFormat.format(
                    "{2}_MSG_ID_{0}_{1}",
                    this.msgType.value(),
                    String.format(numFmt, this.msgNum),
                    this.screenId);
            super.setCode(code);
        }
        // resolve message as default
        return super.resolveMessage();
    }
    
    /**
     * Try to detect {@link Exception} from page/request context attributes such as:<br>
     * - {@link WebUtils#ERROR_EXCEPTION_ATTRIBUTE}<br>
     * - `exception` key from page model
     * 
     * @return {@link Exception} or NULL
     */
    protected final Exception tryToDetectPageException() {
    	Exception exception = RequestUtils.getRequestAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, Exception.class);
    	exception = Optional.ofNullable(exception).orElseGet(
    			() -> Optional.ofNullable(super.pageContext.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, WebRequest.SCOPE_REQUEST))
    			.filter(Exception.class::isInstance).map(Exception.class::cast).orElse(null));
    	exception = Optional.ofNullable(exception).orElseGet(
    			() -> {
    				Map<String, Object> requestModel = Optional.ofNullable(super.getRequestContext())
    						.map(RequestContext::getModel).orElseGet(LinkedHashMap::new);
    				return Optional.ofNullable(requestModel.getOrDefault("exception", null))
    						.filter(Exception.class::isInstance).map(Exception.class::cast)
    						.orElseGet(() -> requestModel.values().parallelStream()
    								.filter(Exception.class::isInstance).map(Exception.class::cast)
    								.findFirst().orElse(null));
    			});
    	return exception;
    }
}
