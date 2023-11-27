/*
 * @(#)Nlh4jAuthenticationEntryPoint.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;
import org.nlh4j.core.service.MessageService;
import org.nlh4j.util.RequestUtils;

/**
 * SessionExpiredAuthenticationEntryPoint 認証エントリポイントクラス
 *
 * <p>認証エントリポイントクラスです。</p>
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public class Nlh4jAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint implements Serializable {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * SLF4J
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * {@link MessageService}
     */
    @Inject
    protected MessageService messageService;

    /**
     * Initialize a new instance of {@link Nlh4jAuthenticationEntryPoint}
     *
     * @param loginFormUrl login form URL
     */
    public Nlh4jAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    /**
     * commence 実行判定処理
     *
     * <p>処理開始時の処理を行います。</p>
     *
     * @param request httpリクエスト情報
     * @param response httpレスポンス情報
     * @param authException 認証エラー情報
     * @throws IOException thrown if could not found request
     * @throws ServletException thrown if processing fail
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // セッションのタイムアウト判定を行う。
        if (RequestUtils.isAjaxRequest(request)) {
        	int status = HttpStatus.UNAUTHORIZED.value();
        	String msg = this.getUnAuthorizedReason();
        	logger.warn(status + " - " + msg);

        	response.setStatus(status);
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            out.println(msg);
            out.flush();

            return;
        }

        super.commence(request, response, authException);
    }

    /**
     * The {@link HttpStatus#UNAUTHORIZED} reason phase key from resource bundle
     */
    @Getter
    @Setter
    private String unAuthorizedReasonKey;

    /**
     * Get the {@link HttpStatus#UNAUTHORIZED} reason phase
     * TODO children classes maybe override this method for customizing reason phase
     */
    @Setter
    private String unAuthorizedReason;
    /**
     * Get the {@link HttpStatus#UNAUTHORIZED} reason phase
     * @return the {@link HttpStatus#UNAUTHORIZED} reason phase
     */
    public String getUnAuthorizedReason() {
        if (!StringUtils.hasText(this.unAuthorizedReason)
                && StringUtils.hasText(this.getUnAuthorizedReasonKey())) {
            this.unAuthorizedReason = this.messageService.getMessage(
                    this.getUnAuthorizedReasonKey());
        }
        if (!StringUtils.hasText(this.unAuthorizedReason)) {
            this.unAuthorizedReason = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        }
        return this.unAuthorizedReason;
    }
}
