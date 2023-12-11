/*
 * @(#)RequestUtils.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.BufferedReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleContextResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContext;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * Ultilities about HTTP request
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class RequestUtils implements Serializable {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;

	// referer header key
	private static final String REQUEST_REFERER_HEADER_KEY = "Referer";
	private static final String REQUEST_UA_HEADER_KEY = "user-agent";
	public static final String AJAX_REQUEST_IDENTIFIER = "XMLHttpRequest";
	public static final String AJAX_REQUEST_HEADER = "X-Requested-With";
    public static final String LOOP_BACK_IPV4_ADDRESS = "127.0.0.1";
    public static final String LOOP_BACK_IPV6_ADDRESS = "0:0:0:0:0:0:0:1";
    public static final String LOOP_BACK_LOCALHOST_ADDRESS = "localhost";
	/** The request header key for parsing client IP address */
	private static transient String[] requestClientIpHeaders = null;
	/**
	 * Get the request header key for parsing client IP address
	 *
	 * @return the request header key for parsing client IP address
	 */
	private static String[] getRequestClientIpHeaders() {
	    if (requestClientIpHeaders == null) {
	        requestClientIpHeaders = new String[] {
	                "X-Forwarded-For",
	                "Proxy-Client-IP",
	                "WL-Proxy-Client-IP",
	                "HTTP_X_FORWARDED_FOR",
	                "HTTP_X_FORWARDED",
	                "HTTP_X_CLUSTER_CLIENT_IP",
	                "HTTP_CLIENT_IP",
	                "HTTP_FORWARDED_FOR",
	                "HTTP_FORWARDED",
	                "HTTP_VIA",
	                "REMOTE_ADDR"
	        };
	    }
	    synchronized (requestClientIpHeaders) {
            return requestClientIpHeaders;
        }
	}

	/**
	 * Get the current {@link HttpServletRequest} from {@link RequestContextHolder}
	 *
	 * @return the current {@link HttpServletRequest} from {@link RequestContextHolder}
	 */
	public static final HttpServletRequest getHttpRequest() {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (BeanUtils.isInstanceOf(ra, ServletRequestAttributes.class)) {
            ServletRequestAttributes sra = BeanUtils.safeType(ra, ServletRequestAttributes.class);
            return sra.getRequest();
        }
        return null;
	}
	/**
	 * Get the current {@link HttpServletRequest} from {@link ServletRequestAttributes}
	 *
	 * @param requestAttributes to parse {@link HttpServletRequest}
	 *
	 * @return the current {@link HttpServletRequest} from {@link ServletRequestAttributes}
	 */
	public static final HttpServletRequest getHttpRequest(ServletRequestAttributes requestAttributes) {
		return (requestAttributes != null ? requestAttributes.getRequest() : null);
	}

    /**
     * Get full context path (including host, port, protocol and context path)
     *
     * @param request the HTTP request to parse
     *
     * @return the full context path (including host, port, protocol and context path)
     */
    public static final String getFullContextPath(HttpServletRequest request) {
    	String value = null;
    	if (request != null) {
    		String url = request.getRequestURL().toString();
    		String uri = request.getRequestURI();
    		String ctx = request.getContextPath();
    		value = url.substring(0, url.length() - uri.length()) + ctx;
    	}
    	return value;
    }
    /**
     * Get current full context path (including host, port, protocol and context path)
     *
     * @return the current full context path (including host, port, protocol and context path)
     */
    public static final String getFullContextPath() {
    	return getFullContextPath(getHttpRequest());
    }

    /**
     * Get compress referer of the specified request (excluding context path)
     *
     * @param request the HTTP request to parse
     *
     * @return the compress referer of the specified request (excluding context path)
     */
    public static final String getRefererURI(HttpServletRequest request) {
    	String value = getFullContextPath(request);
    	if (request != null && StringUtils.hasText(value)) {
    		String refReqURL = request.getHeader(REQUEST_REFERER_HEADER_KEY);
    		if (StringUtils.hasText(refReqURL) && refReqURL.indexOf("?") > 0) {
    		    refReqURL = refReqURL.substring(0, refReqURL.indexOf("?") - 1);
    		}
    		if (StringUtils.hasText(refReqURL) && refReqURL.startsWith(value)) {
    			value = refReqURL.substring(value.length());
    		}
    		else {
    			value = refReqURL;
    		}
    	}
    	return value;
    }
    /**
     * Get compress referer of the current request (excluding context path)
     *
     * @return the compress referer of the current request (excluding context path)
     */
    public static final String getRefererURI() {
    	return getRefererURI(getHttpRequest());
    }

    /**
     * Get URL from {@link HttpServletRequest}
     * @param request {@link HttpServletRequest}
     * @return URL from {@link HttpServletRequest}
     */
    public static final String getRequestURL(HttpServletRequest request) {
        return (request != null ? request.getRequestURL().toString() : null);
    }
    /**
     * Get URL from current {@link HttpServletRequest}
     * @return URL from current {@link HttpServletRequest}
     */
    public static final String getRequestURL() {
        return getRequestURL(getHttpRequest());
    }
    /**
     * Get full URL from {@link HttpServletRequest}
     * @param request {@link HttpServletRequest}
     * @return full URL from {@link HttpServletRequest}
     */
    public static final String getFullRequestURL(HttpServletRequest request) {
        String reqUrl = getRequestURL(request);
        if (StringUtils.hasText(reqUrl)) {
            String queryString = request.getQueryString();
            reqUrl += (StringUtils.hasText(queryString) ? ("?" + queryString) : "");
        }
        return reqUrl;
    }
    /**
     * Get full URL from current {@link HttpServletRequest}
     * @return full URL from current {@link HttpServletRequest}
     */
    public static final String getFullRequestURL() {
        return getFullRequestURL(getHttpRequest());
    }

    /**
     * Detect the specified request whether is ajax request
     *
     * @param request the request to detect
     *
     * @return true for ajax; else false
     */
    public static final boolean isAjaxRequest(HttpServletRequest request) {
    	return (request != null
    			&& AJAX_REQUEST_IDENTIFIER.equals(request.getHeader(AJAX_REQUEST_HEADER)));
    }
    /**
     * Detect the current request whether is ajax request
     *
     * @return true for ajax; else false
     */
    public static final boolean isAjaxRequest() {
    	return isAjaxRequest(getHttpRequest());
    }

    /**
     * Get a boolean value indicating the specified parameter
     * whether existed in the specified request
     *
     * @param request {@link HttpServletRequest}
     * @param paramName parameter name
     *
     * @return true for existed; else false
     */
    public static final boolean hasParameter(HttpServletRequest request, String paramName) {
        return StringUtils.hasText(getParameter(request, paramName));
    }
    /**
     * Get a boolean value indicating the specified parameter
     * whether existed in the current request
     *
     * @param paramName parameter name
     *
     * @return true for existed; else false
     */
    public static final boolean hasParameter(String paramName) {
        return hasParameter(getHttpRequest(), paramName);
    }

    /**
     * Get parameter value from the specified request by name
     *
     * @param request {@link HttpServletRequest}
     * @param paramName parameter name
     *
     * @return parameter value
     */
    public static final String getParameter(HttpServletRequest request, String paramName) {
        if (request == null) return null;
        // require POST parameter
        String value = request.getParameter(paramName);
        // check GET parameter
        if (!StringUtils.hasText(value)) {
            Map<String, List<String>> queryParams = null;
            try {
                queryParams = splitQuery(request.getQueryString());
            } catch (UnsupportedEncodingException e) {
                LogUtils.logError(RequestUtils.class, e.getMessage());
            } finally {
                if (queryParams != null && !CollectionUtils.isEmpty(queryParams)) {
                    Set<String> paramNames = queryParams.keySet();
                    if (!CollectionUtils.isEmpty(paramNames)) {
                        for(final Iterator<String> it = paramNames.iterator(); it.hasNext();) {
                            String key = it.next();
                            if (StringUtils.hasText(key) && key.equalsIgnoreCase(paramName)) {
                                List<String> paramVals = queryParams.get(key);
                                value = (CollectionUtils.isEmpty(paramVals) ? null : paramVals.get(0));
                                break;
                            }
                        }
                    }
                }
            }
        }
        return value;
    }
    /**
     * Get parameter value from the current request by name
     *
     * @param paramName parameter name
     *
     * @return parameter value
     */
    public static final String getParameter(String paramName) {
        return getParameter(getHttpRequest(), paramName);
    }

    /**
     * Get parameter value(s) array from the specified request by name
     *
     * @param request {@link HttpServletRequest}
     * @param paramName parameter name
     *
     * @return parameter value(s) array
     */
    public static final String[] getParameters(HttpServletRequest request, String paramName) {
        if (request == null) return new String[] {};
        // require POST parameters
        String[] values = request.getParameterValues(paramName);
        // check GET parameters
        if (CollectionUtils.isEmpty(values)) {
            Map<String, List<String>> queryParams = null;
            try {
                queryParams = splitQuery(request.getQueryString());
            } catch (UnsupportedEncodingException e) {
                LogUtils.logError(RequestUtils.class, e.getMessage());
            } finally {
                if (!CollectionUtils.isEmpty(queryParams)) {
                    Set<String> paramNames = queryParams.keySet();
                    if (!CollectionUtils.isEmpty(paramNames)) {
                        for(final Iterator<String> it = paramNames.iterator(); it.hasNext();) {
                            String key = it.next();
                            if (StringUtils.hasText(key) && key.equalsIgnoreCase(paramName)) {
                                List<String> paramVals = queryParams.get(key);
                                values = (CollectionUtils.isEmpty(paramVals)
                                        ? new String[] {}
                                        : paramVals.toArray(new String[paramVals.size()]));
                                break;
                            }
                        }
                    }
                }
            }
        }
        return values;
    }
    /**
     * Get parameter value(s) array from the current request by name
     *
     * @param paramName parameter name
     *
     * @return parameter value(s) array
     */
    public static final String[] getParameters(String paramName) {
        return getParameters(getHttpRequest(), paramName);
    }

    /**
     * Get parameter integer value(s) array from the specified request by name
     *
     * @param request {@link HttpServletRequest}
     * @param paramName parameter name
     * @param defVal default value of every invalid integer value
     *
     * @return parameter integer value(s) array
     */
    public static final Integer[] getIntParameters(HttpServletRequest request, String paramName, int defVal) {
        List<Integer> params = new LinkedList<Integer>();
        String[] paramVals = getParameters(request, paramName);
        if (!CollectionUtils.isEmpty(paramVals)) {
            for(String paramVal : paramVals) {
                params.add(NumberUtils.toInt(paramVal, defVal));
            }
        }
        return (!CollectionUtils.isEmpty(params)
                ? params.toArray(new Integer[params.size()]) : new Integer[] {});
    }
    /**
     * Get parameter integer value(s) array from the current request by name
     *
     * @param paramName parameter name
     * @param defVal default value of every invalid integer value
     *
     * @return parameter integer value(s) array
     */
    public static final Integer[] getIntParameters(String paramName, int defVal) {
        return getIntParameters(getHttpRequest(), paramName, defVal);
    }

    /**
     * Get parameter integer value from the specified request by name
     *
     * @param request {@link HttpServletRequest}
     * @param paramName parameter name
     * @param defVal default value of invalid integer value
     *
     * @return parameter integer value
     */
    public static final int getIntParameter(HttpServletRequest request, String paramName, int defVal) {
        return NumberUtils.toInt(getParameter(request, paramName), defVal);
    }
    /**
     * Get parameter integer value from the current request by name
     *
     * @param paramName parameter name
     * @param defVal default value of invalid integer value
     *
     * @return parameter integer value
     */
    public static final int getIntParameter(String paramName, int defVal) {
        return getIntParameter(getHttpRequest(), paramName, defVal);
    }

    /**
     * Get parameter {@link Long} value(s) array from the specified request by name
     *
     * @param request {@link HttpServletRequest}
     * @param paramName parameter name
     * @param defVal default value of every invalid {@link Long} value
     *
     * @return parameter {@link Long} value(s) array
     */
    public static final Long[] getLongParameters(HttpServletRequest request, String paramName, long defVal) {
        List<Long> params = new LinkedList<Long>();
        String[] paramVals = getParameters(request, paramName);
        if (!CollectionUtils.isEmpty(paramVals)) {
            for(String paramVal : paramVals) {
                params.add(NumberUtils.toLong(paramVal, defVal));
            }
        }
        return (!CollectionUtils.isEmpty(params)
                ? params.toArray(new Long[params.size()]) : new Long[] {});
    }
    /**
     * Get parameter {@link Long} value(s) array from the current request by name
     *
     * @param paramName parameter name
     * @param defVal default value of every invalid {@link Long} value
     *
     * @return parameter {@link Long} value(s) array
     */
    public static final Long[] getLongParameters(String paramName, long defVal) {
        return getLongParameters(getHttpRequest(), paramName, defVal);
    }

    /**
     * Get parameter {@link Long} value from the specified request by name
     *
     * @param request {@link HttpServletRequest}
     * @param paramName parameter name
     * @param defVal default value of invalid {@link Long} value
     *
     * @return parameter {@link Long} value
     */
    public static final long getLongParameter(HttpServletRequest request, String paramName, long defVal) {
        return NumberUtils.toLong(getParameter(request, paramName), defVal);
    }
    /**
     * Get parameter {@link Long} value from the current request by name
     *
     * @param paramName parameter name
     * @param defVal default value of invalid {@link Long} value
     *
     * @return parameter {@link Long} value
     */
    public static final long getLongParameter(String paramName, long defVal) {
        return getLongParameter(getHttpRequest(), paramName, defVal);
    }

    /**
     * Get parameter {@link Boolean} value from the specified request by name
     *
     * @param request {@link HttpServletRequest}
     * @param paramName parameter name
     * @param trueVal integer value to check true/false
     *
     * @return parameter {@link Boolean} value
     */
    public static final boolean getBooleanParameter(HttpServletRequest request, String paramName, int trueVal) {
        try {
            return (Integer.parseInt(getParameter(request, paramName)) == trueVal);
        }
        catch(Exception e1) {
            try {
                return Boolean.parseBoolean(getParameter(request, paramName));
            } catch(Exception e2) {
                return Boolean.FALSE;
            }
        }
    }
    /**
     * Get parameter {@link Boolean} value from the current request by name
     *
     * @param paramName parameter name
     * @param trueVal integer value to check true/false
     *
     * @return parameter {@link Boolean} value
     */
    public static final boolean getBooleanParameter(String paramName, int trueVal) {
        return getBooleanParameter(getHttpRequest(), paramName, trueVal);
    }

    /**
     * Get parameter {@link Boolean} value from the specified request by name
     *
     * @param request {@link HttpServletRequest}
     * @param paramName parameter name
     *
     * @return parameter {@link Boolean} value
     */
    public static final boolean getBooleanParameter(HttpServletRequest request, String paramName) {
        return getBooleanParameter(request, paramName, 1);
    }
    /**
     * Get parameter {@link Boolean} value from the current request by name
     *
     * @param paramName parameter name
     *
     * @return parameter {@link Boolean} value
     */
    public static final boolean getBooleanParameter(String paramName) {
        return getBooleanParameter(getHttpRequest(), paramName);
    }

    /**
     * Get HTTP headers from the specified request
     * @param request {@link HttpServletRequest}
     * @return HTTP headers
     */
    public static final Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new LinkedHashMap<String, String>();
        if (request != null) {
            Enumeration<String> paramNames = null;
            try {
                paramNames = request.getHeaderNames();
                if (paramNames != null) {
                    do {
                        String paramName = paramNames.nextElement();
                        headers.put(paramName, request.getHeader(paramName));
                    } while(paramNames.hasMoreElements());
                }
            } catch (Exception e) {
                LogUtils.logWarn(RequestUtils.class, e.getMessage());
                headers.clear();
            }
        }
        return headers;
    }
    /**
     * Get HTTP headers from the current request
     * @return HTTP headers
     */
    public static final Map<String, String> getHeaders() {
        return getHeaders(getHttpRequest());
    }

    /**
     * HTTPリクエストのヘッダパラメータをチェック
     *
     * @param request HTTPリクエスト
     * @param paramName パラメータ名
     *
     * @return true - 存在するヘッダパラメータ
     */
    public static final boolean hasHeader(HttpServletRequest request, String paramName) {
        return StringUtils.hasText(getHeader(request, paramName));
    }
    /**
     * 現在HTTPリクエストのヘッダパラメータをチェック
     *
     * @param paramName パラメータ名
     *
     * @return true - 存在するヘッダパラメータ
     */
    public static final boolean hasHeader(String paramName) {
        return hasHeader(getHttpRequest(), paramName);
    }

    /**
     * HTTPリクエストのヘッダパラメータを取得
     *
     * @param request HTTPリクエスト
     * @param paramName パラメータ名
     *
     * @return HTTPリクエストのヘッダパラメータ値
     */
    public static final String getHeader(HttpServletRequest request, String paramName) {
        if (request == null) return null;
        return request.getHeader(paramName);
    }
    /**
     * 現在HTTPリクエストのヘッダパラメータを取得
     *
     * @param paramName パラメータ名
     *
     * @return HTTPリクエストのヘッダパラメータ値
     */
    public static final String getHeader(String paramName) {
        return getHeader(getHttpRequest(), paramName);
    }

    /**
     * HTTPリクエストのヘッダパラメータを取得
     *
     * @param request HTTPリクエスト
     * @param paramName パラメータ名
     *
     * @return HTTPリクエストのヘッダパラメータ値
     */
    public static final Integer getIntHeader(HttpServletRequest request, String paramName) {
        return NumberUtils.toInt(getHeader(request, paramName));
    }
    /**
     * 現在HTTPリクエストのヘッダパラメータを取得
     *
     * @param paramName パラメータ名
     *
     * @return HTTPリクエストのヘッダパラメータ値
     */
    public static final Integer getIntHeader(String paramName) {
        return getIntHeader(getHttpRequest(), paramName);
    }

    /**
     * HTTPリクエストのヘッダパラメータを取得
     *
     * @param request HTTPリクエスト
     * @param paramName パラメータ名
     * @param defVal デフォルト値
     *
     * @return HTTPリクエストのヘッダパラメータ値
     */
    public static final int getIntHeader(HttpServletRequest request, String paramName, int defVal) {
        return NumberUtils.toInt(getHeader(request, paramName), defVal);
    }
    /**
     * 現在HTTPリクエストのヘッダパラメータを取得
     *
     * @param paramName パラメータ名
     * @param defVal デフォルト値
     *
     * @return HTTPリクエストのヘッダパラメータ値
     */
    public static final int getIntHeader(String paramName, int defVal) {
        return getIntHeader(getHttpRequest(), paramName, defVal);
    }

    /**
     * HTTPリクエストのヘッダパラメータを取得
     *
     * @param request HTTPリクエスト
     * @param paramName パラメータ名
     *
     * @return HTTPリクエストのヘッダパラメータ値
     */
    public static final Long getLongHeader(HttpServletRequest request, String paramName) {
        return NumberUtils.toLong(getHeader(request, paramName));
    }
    /**
     * 現在HTTPリクエストのヘッダパラメータを取得
     *
     * @param paramName パラメータ名
     *
     * @return HTTPリクエストのヘッダパラメータ値
     */
    public static final Long getLongHeader(String paramName) {
        return getLongHeader(getHttpRequest(), paramName);
    }

    /**
     * HTTPリクエストのヘッダパラメータを取得
     *
     * @param request HTTPリクエスト
     * @param paramName パラメータ名
     * @param defVal デフォルト値
     *
     * @return HTTPリクエストのヘッダパラメータ値
     */
    public static final long getLongHeader(HttpServletRequest request, String paramName, long defVal) {
        return NumberUtils.toLong(getHeader(request, paramName), defVal);
    }
    /**
     * 現在HTTPリクエストのヘッダパラメータを取得
     *
     * @param paramName パラメータ名
     * @param defVal デフォルト値
     *
     * @return HTTPリクエストのヘッダパラメータ値
     */
    public static final long getLongHeader(String paramName, long defVal) {
        return getLongHeader(getHttpRequest(), paramName, defVal);
    }

    /**
     * HTTPリクエストのヘッダパラメータを取得
     *
     * @param request HTTPリクエスト
     * @param paramName パラメータ名
     *
     * @return HTTPリクエストのヘッダパラメータ値
     */
    public static final Boolean getBooleanHeader(HttpServletRequest request, String paramName) {
        return NumberUtils.toBool(getHeader(request, paramName));
    }
    /**
     * 現在HTTPリクエストのヘッダパラメータを取得
     *
     * @param paramName パラメータ名
     *
     * @return HTTPリクエストのヘッダパラメータ値
     */
    public static final Boolean getBooleanHeader(String paramName) {
        return getBooleanHeader(getHttpRequest(), paramName);
    }

    /**
     * HTTPリクエストのヘッダパラメータを取得
     *
     * @param request HTTPリクエスト
     * @param paramName パラメータ名
     * @param trueVal TRUE値
     *
     * @return HTTPリクエストのヘッダパラメータ値
     */
    public static final boolean getBooleanHeader(HttpServletRequest request, String paramName, int trueVal) {
        try {
            return (Integer.parseInt(getHeader(request, paramName)) == trueVal);
        }
        catch(Exception e1) {
            try {
                return Boolean.parseBoolean(getHeader(request, paramName));
            } catch(Exception e2) {
                return Boolean.FALSE;
            }
        }
    }
    /**
     * 現在HTTPリクエストのヘッダパラメータを取得
     *
     * @param paramName パラメータ名
     * @param trueVal TRUE値
     *
     * @return HTTPリクエストのヘッダパラメータ値
     */
    public static final boolean getBooleanHeader(String paramName, int trueVal) {
        return getBooleanHeader(getHttpRequest(), paramName, trueVal);
    }

    /**
     * サーバの実ファイルパスを取得
     *
     * @param request HTTPリクエスト
     * @param path ファイルパス
     *
     * @return サーバの実ファイルパス
     */
    public static final String getServletRealPath(HttpServletRequest request, String path) {
        if (request == null) return null;
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        ServletContext sc = session.getServletContext();
        if (sc == null) return null;
        return sc.getRealPath(path);
    }
    /**
     * サーバの実ファイルパスを取得
     *
     * @param path ファイルパス
     *
     * @return サーバの実ファイルパス
     */
    public static final String getServletRealPath(String path) {
        return getServletRealPath(getHttpRequest(), path);
    }

    /**
     * リクエスト属性を取得します
     *
     * @param <T> 属性クラス
     * @param request HTTPリクエスト
     * @param attrName 属性名
     * @param attrClazz 属性クラス
     *
     * @return 値のリクエスト属性
     */
    public static <T> T getRequestAttribute(HttpServletRequest request, String attrName, Class<T> attrClazz) {
        if (request == null || !StringUtils.hasText(attrName) || attrClazz == null) return null;
        Object reqAttr = request.getAttribute(attrName);
        return BeanUtils.safeType(reqAttr, attrClazz);
    }
    /**
     * リクエスト属性を取得します
     *
     * @param <T> 属性クラス
     * @param attrName 属性名
     * @param attrClazz 属性クラス
     *
     * @return 値のリクエスト属性
     */
    public static <T> T getRequestAttribute(String attrName, Class<T> attrClazz) {
        return getRequestAttribute(getHttpRequest(), attrName, attrClazz);
    }

    /**
     * Set request attribute value
     *
     * @param request {@link HttpServletRequest}
     * @param attrName attribute name
     * @param attrValue attribute value
     */
    public static void setRequestAttribute(HttpServletRequest request, String attrName, Object attrValue) {
        if (request == null || !StringUtils.hasText(attrName) || attrValue == null) return;
        request.setAttribute(attrName, attrValue);
    }
    /**
     * Set request attribute value
     *
     * @param attrName attribute name
     * @param attrValue attribute value
     */
    public static void setRequestAttribute(String attrName, Object attrValue) {
        setRequestAttribute(getHttpRequest(), attrName, attrValue);
    }

    /**
     * Remove request attribute by name
     *
     * @param request {@link HttpServletRequest}
     * @param attrName attribute name
     */
    public static void removeRequestAttribute(HttpServletRequest request, String attrName) {
        if (request == null || !StringUtils.hasText(attrName)) return;
        request.removeAttribute(attrName);
    }
    /**
     * Remove request attribute by name
     * @param attrName to remove
     */
    public static void removeRequestAttribute(String attrName) {
        removeRequestAttribute(getHttpRequest(), attrName);
    }

    /**
     * Get the browser type of specified request
     *
     * @param request {@link HttpServletRequest}
     *
     * @return the browser type of specified request
     */
    public static Browser getBrowser(HttpServletRequest request) {
        Browser bt = Browser.UNKNOWN;
        try {
            if (request != null) {
                String userAgent = request.getHeader(REQUEST_UA_HEADER_KEY);
                UserAgent ua = UserAgent.parseUserAgentString(userAgent);
                bt = ua.getBrowser();
            }
        }
        catch(Exception e) {
            LogUtils.logWarn(RequestUtils.class, e.getMessage());
            bt = Browser.UNKNOWN;
        }
        return bt;
    }
    /**
     * Get the browser type of current request
     *
     * @return the browser type of current request
     */
    public static Browser getBrowser() {
        return getBrowser(getHttpRequest());
    }

    /**
     * Get the client OS of the specified request
     *
     * @param request {@link HttpServletRequest}
     *
     * @return the client OS of specified request
     */
    public static OperatingSystem getClientOS(HttpServletRequest request) {
        OperatingSystem ost = OperatingSystem.UNKNOWN;
        try {
            if (request != null) {
                String userAgent = request.getHeader(REQUEST_UA_HEADER_KEY);
                UserAgent ua = UserAgent.parseUserAgentString(userAgent);
                ost = ua.getOperatingSystem();
            }
        }
        catch(Exception e) {
            LogUtils.logWarn(RequestUtils.class, e.getMessage());
            ost = OperatingSystem.UNKNOWN;
        }
        return ost;
    }
    /**
     * Get the client OS of the current request
     *
     * @return the client OS of the current request
     */
    public static OperatingSystem getClientOS() {
        return getClientOS(getHttpRequest());
    }

    /**
     * Get the client device type of specified request
     *
     * @param request {@link HttpServletRequest}
     *
     * @return the client device type of specified request
     */
    public static DeviceType getClientDeviceType(HttpServletRequest request) {
        OperatingSystem ost = getClientOS(request);
        return ost.getDeviceType();
    }
    /**
     * Get the client device type of current request
     *
     * @return the client device type of current request
     */
    public static DeviceType getClientDeviceType() {
        return getClientDeviceType(getHttpRequest());
    }

    /**
     * Create content-disposition vaue to apply for downloaded request
     *
     * @param request {@link HttpServletRequest}
     * @param fileName file name
     *
     * @return content-disposition value
     */
    public static String getContentDisposition(HttpServletRequest request, String fileName) {
        Browser bt = getBrowser(request);
        String encFileName = fileName;
        try { encFileName = URLEncoder.encode(fileName, EncryptUtils.ENCODING_UTF8); }
        catch (UnsupportedEncodingException e) { encFileName = fileName; }
        String contentDisposition = MessageFormat.format("attachment; filename=\"{0}\"", encFileName);
        if (Browser.FIREFOX.equals(bt.getGroup())) {
            contentDisposition = MessageFormat.format(
                    "attachment; filename*={0}''\"{1}\"",
                    EncryptUtils.ENCODING_UTF8, encFileName);
        }
        return contentDisposition;
    }
    /**
     * Create content-disposition vaue to apply for downloaded request
     *
     * @param fileName file name
     *
     * @return content-disposition value
     */
    public static String getContentDisposition(String fileName) {
        return getContentDisposition(getHttpRequest(), fileName);
    }

    /**
     * Get the client remote address
     *
     * @param request {@link HttpServletRequest}
     *
     * @return the client remote address
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String ip = null;
        if (request != null) {
            String[] requestClientIpHeaders = getRequestClientIpHeaders();
            for (String header : requestClientIpHeaders) {
                ip = request.getHeader(header);
                if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                    break;
                }
            }
            if (!StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            // check for loopback
            if (!LOOP_BACK_IPV4_ADDRESS.equalsIgnoreCase(ip)
                    && LOOP_BACK_IPV6_ADDRESS.equalsIgnoreCase(ip)
                    && LOOP_BACK_LOCALHOST_ADDRESS.equalsIgnoreCase(ip)) {
                InetAddress address = null;
                try {
                    address = InetAddress.getByName(ip);
                    ip = (address.isAnyLocalAddress() ? LOOP_BACK_LOCALHOST_ADDRESS : ip);
                } catch (Exception e) {
                    LogUtils.logWarn(RequestUtils.class, e.getMessage());
                }
            } else {
                ip = LOOP_BACK_LOCALHOST_ADDRESS;
            }
        }
        return ip;
    }
    /**
     * Get the client remote address
     *
     * @return the client remote address
     */
    public static String getClientIpAddress() {
        return getClientIpAddress(getHttpRequest());
    }

    /**
     * Get a boolean value indicating that remote address came from localhost/loopback
     *
     * @param request {@link HttpServletRequest}
     *
     * @return true for loopback; else false
     */
    public static boolean isClientLoopback(HttpServletRequest request) {
        return LOOP_BACK_LOCALHOST_ADDRESS.equalsIgnoreCase(getClientIpAddress(request));
    }
    /**
     * Get a boolean value indicating that remote address came from localhost/loopback
     *
     * @return true for loopback; else false
     */
    public static boolean isClientLoopback() {
        return isClientLoopback(getHttpRequest());
    }

    /**
     * Get the {@link RequestContext} from the specified {@link HttpServletRequest}
     *
     * @param request to parse context
     *
     * @return the {@link RequestContext} from the specified {@link HttpServletRequest}
     */
    public static RequestContext getRequestContext(HttpServletRequest request) {
		try {
    		return (request == null ? null : new RequestContext(request));
		} catch (Exception e) {
			LogUtils.logWarn(RequestUtils.class, e.getMessage());
			return null;
		}
    }
    /**
     * Get the {@link RequestContext} from the current {@link HttpServletRequest}
     *
     * @return the {@link RequestContext} from the current {@link HttpServletRequest}
     */
    public static RequestContext getRequestContext() {
		return getRequestContext(getHttpRequest());
    }

    /**
     * Get the configuration enviroment from the specified {@link HttpServletRequest}
     *
     * @param requestContext to parse enviroment
     *
     * @return the configuration enviroment from the specified {@link HttpServletRequest}
     */
    public static Environment getEnviroment(RequestContext requestContext) {
    	if (requestContext != null) {
			WebApplicationContext wac = null;
			try {
	    		wac = (requestContext == null ? null : requestContext.getWebApplicationContext());
	    		return (wac == null ? null : wac.getEnvironment());
			} catch (Exception e) {
				LogUtils.logWarn(RequestUtils.class, e.getMessage());
			}
    	}
    	return null;
    }
    /**
     * Get the configuration enviroment from the specified {@link HttpServletRequest}
     *
     * @param request to parse context
     *
     * @return the configuration enviroment from the specified {@link HttpServletRequest}
     */
    public static Environment getEnviroment(HttpServletRequest request) {
		return getEnviroment(getRequestContext(request));
    }
    /**
     * Get the configuration enviroment from the current {@link HttpServletRequest}
     *
     * @return the configuration enviroment from the current {@link HttpServletRequest}
     */
    public static Environment getEnviroment() {
    	return getEnviroment(getHttpRequest());
    }

    /**
     * Split {@link URL} for parameters (query-string)
     *
     * @param queryString to split
     *
     * @return parameters mapping
     *
     * @throws UnsupportedEncodingException thrown if failed
     */
    public static Map<String, List<String>> splitQuery(final String queryString) throws UnsupportedEncodingException {
        final Map<String, List<String>> queryPairs = new LinkedHashMap<String, List<String>>();
        if (StringUtils.hasText(queryString)) {
            final String[] pairs = queryString.trim().split("&");
            for (String pair : pairs) {
                final int idx = pair.indexOf("=");
                final String key = (idx > 0 ? URLDecoder.decode(
                        pair.substring(0, idx), EncryptUtils.ENCODING_UTF8) : pair);
                if (!queryPairs.containsKey(key)) {
                    queryPairs.put(key, new LinkedList<String>());
                }
                final String value = ((idx > 0 && pair.length() > idx + 1)
                        ? URLDecoder.decode(pair.substring(idx + 1), EncryptUtils.ENCODING_UTF8) : null);
                queryPairs.get(key).add(value);
            }
        }
        return queryPairs;
    }

    /**
     * Get request body from the specified {@link HttpServletRequest}
     * @param request {@link HttpServletRequest}
     * @return request body from the specified {@link HttpServletRequest}
     */
    public static final StringBuilder getRequestBody(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        if (request != null) {
            BufferedReader reader = null;
            String line = null;
            try {
                reader = request.getReader();
                while(reader != null && StringUtils.hasText(line = reader.readLine())) {
                    builder.append(line);
                }
            } catch (Exception e) {
                LogUtils.logWarn(RequestUtils.class, e.getMessage());
                builder = new StringBuilder();
            }
        }
        return builder;
    }
    /**
     * Get request body from the current {@link HttpServletRequest}
     * @return request body from the current {@link HttpServletRequest}
     */
    public static final StringBuilder getRequestBody() {
        return getRequestBody(getHttpRequest());
    }

    /**
     * Detect the {@link LocaleResolver} from the specified {@link HttpServletRequest}
     * 
     * @param request to detect
     * 
     * @return {@link LocaleResolver} or NULL
     */
    public static LocaleResolver getRequestLocaleResolver(final HttpServletRequest request) {
    	return Optional.ofNullable(request).map(req -> req.getAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE))
    			.filter(LocaleResolver.class::isInstance).map(LocaleResolver.class::cast).orElse(null);
    }
    /**
     * Detect the {@link LocaleResolver} from the present {@link HttpServletRequest}
     * 
     * @return {@link LocaleResolver} or NULL
     */
    public static LocaleResolver getRequestLocaleResolver() {
    	return getRequestLocaleResolver(getHttpRequest());
    }

    /**
     * Build the {@link LocaleContext}
     * 
     * @param localeResolver {@link LocaleResolver}
     * @param request {@link HttpServletRequest}
     * @param defaultLocale specify whether should build based on default locale
     * 
     * @return {@link LocaleContext}/NULL
     */
    private static LocaleContext buildLocaleContext(final LocaleResolver localeResolver, final HttpServletRequest request, boolean defaultLocale) {
    	return () -> localeResolver == null && request == null && !defaultLocale
    			? null : localeResolver == null && request == null && defaultLocale
    			? Locale.getDefault() : localeResolver == null && request != null && !defaultLocale
    			? request.getLocale() : localeResolver == null && request != null && defaultLocale
    			? Optional.ofNullable(request.getLocale()).orElseGet(Locale::getDefault)
    					: localeResolver != null && request == null && !defaultLocale
    					? null : localeResolver != null && request == null && defaultLocale
    					? Locale.getDefault() : localeResolver != null && request != null && !defaultLocale
    					? localeResolver.resolveLocale(request) : localeResolver != null && request != null && defaultLocale
    					? Optional.ofNullable(localeResolver.resolveLocale(request)).orElseGet(Locale::getDefault) : null;
    }
    /**
     * Detect the {@link LocaleResolver} from the specified {@link HttpServletRequest}
     * 
     * @param request to detect
     * @param defaultLocale specify whether should build with default locale
     * 
     * @return {@link LocaleResolver}/NULL
     */
    public static LocaleContext buildRequestLocaleContext(final HttpServletRequest request, final boolean defaultLocale) {
    	return Optional.ofNullable(getRequestLocaleResolver(request))
    			.filter(LocaleContextResolver.class::isInstance).map(LocaleContextResolver.class::cast)
    			.map(lcr -> lcr.resolveLocaleContext(request))
    			.orElseGet(() -> buildLocaleContext(getRequestLocaleResolver(request), request, defaultLocale));
    }
    /**
     * Detect the {@link LocaleResolver} from the present {@link HttpServletRequest}
     * 
     * @param defaultLocale specify whether should build with default locale
     * 
     * @return {@link LocaleResolver}/NULL
     */
    public static LocaleContext buildRequestLocaleContext(boolean defaultLocale) {
    	return buildRequestLocaleContext(getHttpRequest(), defaultLocale);
    }
    /**
     * Detect the {@link LocaleResolver} from the present {@link HttpServletRequest}
     * 
     * @return {@link LocaleResolver}/NULL
     */
    public static LocaleContext buildRequestLocaleContext() {
    	return buildRequestLocaleContext(true);
    }
    
    /**
     * Detect {@link Locale} from the specified {@link HttpServletRequest}
     * 
     * @param request {@link HttpServletRequest}
     * @param defaultLocale specify whether should resolve as default locale if un-possible
     * 
     * @return {@link Locale}
     */
    public static Locale getRequestLocale(final HttpServletRequest request, final boolean defaultLocale) {
    	return Optional.ofNullable(buildRequestLocaleContext(request, defaultLocale))
    			.map(LocaleContext::getLocale).orElseGet(() -> defaultLocale ? Locale.getDefault() : null);
    }
    /**
     * Detect {@link Locale} from the present {@link HttpServletRequest}
     * 
     * @param defaultLocale specify whether should resolve as default locale if un-possible
     * 
     * @return {@link Locale}
     */
    public static Locale getRequestLocale(boolean defaultLocale) {
    	return getRequestLocale(getHttpRequest(), defaultLocale);
    }
    /**
     * Detect {@link Locale} from the present {@link HttpServletRequest}
     * 
     * @return {@link Locale} or default
     */
    public static Locale getRequestLocale() {
    	return getRequestLocale(true);
    }
}
