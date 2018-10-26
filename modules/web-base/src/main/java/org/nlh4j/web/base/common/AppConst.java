/*
 * @(#)AppConst.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.common;

import java.io.Serializable;
import java.text.MessageFormat;

import org.nlh4j.core.context.properties.PropertyPlaceholderConfigurer;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.util.ApplicationPropUtils;
import org.nlh4j.util.ApplicationPropUtils.BUILTIN_PROPERTY;
import org.nlh4j.util.FileUtils;
import org.nlh4j.util.LogUtils;
import org.nlh4j.util.StringUtils;

/**
 * The common application constants
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class AppConst implements Serializable {

    /** */
	private static final long serialVersionUID = 1L;
	/** {@link PropertyPlaceholderConfigurer} */
	private static PropertyPlaceholderConfigurer properties = null;
	/**
	 * Detect {@link PropertyPlaceholderConfigurer} from context
	 * @return {@link PropertyPlaceholderConfigurer} or NULL
	 */
	private static PropertyPlaceholderConfigurer findProperties() {
		if (properties == null) {
			properties = SpringContextHelper.findBean(
					PropertyPlaceholderConfigurer.class);
		}
		if (properties == null) return null;
		synchronized(properties) {
			return properties;
		}
	}

	/**
	 * SQL LIKE operator
	 */
	public static enum LIKE_OPERATOR {
		NONE(null)
	    , PREFIX("prefix")
	    , SUFFIX("suffix")
	    , CONTAIN("contain")
	    ;

	    private final String code;
	    private LIKE_OPERATOR(String code) {
	        this.code = code;
	    }

	    /**
	     * Get the operator code
	     *
         * @return the operator code
         */
        public String getCode() {
            return this.code;
        }
        public boolean isPrefix() {
            return "prefix".equalsIgnoreCase(this.getCode());
        }
        public boolean isSuffix() {
            return "suffix".equalsIgnoreCase(this.getCode());
        }
        public boolean isContain() {
            return "contain".equalsIgnoreCase(this.getCode());
        }
        public static LIKE_OPERATOR fromCode(String code) {
            LIKE_OPERATOR lop = LIKE_OPERATOR.NONE;
            if (StringUtils.hasText(code)) {
                for(LIKE_OPERATOR op : LIKE_OPERATOR.values()) {
                    if (code.equalsIgnoreCase(op.getCode())) {
                        lop = op;
                        break;
                    }
                }
            }
            return lop;
        }
	}

	/** Request JSON header */
	public static final String REQUEST_ACCEPT_JSON_HEADER = "Accept=application/json";
	/** Request JSON produce */
    public static final String REQUEST_JSON_PRODUCE = "application/json";

	/** the records number per page **/
	private static final int RECORDS_PER_PAGE =
	        ApplicationPropUtils.getInt(BUILTIN_PROPERTY.APP, AppPropKeys.MAX_ITEMS_PER_PAGE, 15);
	/** the minimum password length **/
	private static final int MIN_PASSWORD_LENGTH =
	        ApplicationPropUtils.getInt(BUILTIN_PROPERTY.APP, AppPropKeys.MIN_PASSWORD_LENGTH, 8);
	/** the application repository **/
	private static final String APPLICATION_REPOSITORY =
	        ApplicationPropUtils.getString(BUILTIN_PROPERTY.APP, AppPropKeys.APPLICATION_REPOSITORY);
	/** the LIKE operator */
	private static final LIKE_OPERATOR SQL_LIKE_OPERATOR =
	        LIKE_OPERATOR.fromCode(ApplicationPropUtils.getString(BUILTIN_PROPERTY.APP, AppPropKeys.LIKE_OPERATOR));

	/**
     * Detect LIKE operator from properties configuration or built-in configuration
     * @return LIKE operator
     */
    public static LIKE_OPERATOR findLikeOperator() {
    	// default built-in operator
    	LIKE_OPERATOR likeOp = AppConst.SQL_LIKE_OPERATOR;
    	// search for override properties
    	PropertyPlaceholderConfigurer properties = findProperties();
    	if (properties != null) {
    		String operator = properties.getPropertyString(AppPropKeys.LIKE_OPERATOR);
    		LIKE_OPERATOR eLikeOp = LIKE_OPERATOR.fromCode(operator);
    		if (!LIKE_OPERATOR.NONE.equals(eLikeOp)) {
    			likeOp = eLikeOp;
    		}
    	}
    	return (likeOp == null || LIKE_OPERATOR.NONE.equals(likeOp)
    			? LIKE_OPERATOR.PREFIX : likeOp);
    }
	/**
	 * Get the number records per page from configuration
	 * @return the number records per page from configuration
	 */
	public static int findRecordsPerPage() {
		int recordsPerPage = RECORDS_PER_PAGE;
		// search for override properties
    	PropertyPlaceholderConfigurer properties = findProperties();
    	if (properties != null) {
    		recordsPerPage = properties.getPropertyInt(
    				AppPropKeys.MAX_ITEMS_PER_PAGE, recordsPerPage);
    	}
    	return recordsPerPage;
	}
	/**
	 * Get the number records per page from configuration
	 * @return the number records per page from configuration
	 */
	public static int findMinPasswordLength() {
		int minLength = MIN_PASSWORD_LENGTH;
		// search for override properties
    	PropertyPlaceholderConfigurer properties = findProperties();
    	if (properties != null) {
    		minLength = properties.getPropertyInt(
    				AppPropKeys.MIN_PASSWORD_LENGTH, minLength);
    	}
    	return minLength;
	}

	/**
	 * Ensure application repository directory
	 * @return true for ensuring; else false
	 */
	private static boolean ensureRepository(String repository) {
		boolean ensured = false;
		try {
			ensured = (StringUtils.hasText(repository)
					&& FileUtils.ensureDirectory(repository));
		} catch (Exception e) {
			LogUtils.logError(AppConst.class, e);
		}
		if (!ensured) {
			String msg = null;
			if (!StringUtils.hasText(repository)) {
				msg = "Application has not configure repository for storing uploaded file!!!";
			} else {
				msg = "Invalid application repository [" + repository + "] configuration!!!";
			}
			LogUtils.logWarn(AppConst.class, msg);
		}
		return ensured;
	}
	/**
	 * Get the application repository directory from configuration
	 * @return the application repository directory from configuration
	 */
	public static String findAppRepository() {
		String repository = APPLICATION_REPOSITORY;
		// search for override properties
    	PropertyPlaceholderConfigurer properties = findProperties();
    	if (properties != null) {
    		repository = properties.getPropertyString(
    				AppPropKeys.APPLICATION_REPOSITORY, repository);
    	}
    	// ensure repository
    	return (ensureRepository(repository) ? repository.trim() : null);
	}
	/**
     * Ensure application sub repository directory in the main application repository
     * @return the application sub repository directory from configuration
     */
    public static String ensureAppSubRepository(String subdir) {
        String appRepo = findAppRepository();
        String subRepo = appRepo;
        if (StringUtils.hasText(appRepo) && StringUtils.hasText(subdir)) {
            appRepo = appRepo.replace("\\", "/").replace("//", "/");
            if (appRepo.endsWith("/")) {
                appRepo = appRepo.substring(0, appRepo.length() - 1);
            }
            subRepo = MessageFormat.format("{0}/{1}", appRepo, subdir);
            if (!ensureRepository(subRepo)) subRepo = null;
        }
        return subRepo;
    }
}
