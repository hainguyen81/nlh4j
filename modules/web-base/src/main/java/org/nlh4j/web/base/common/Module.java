/*
 * @(#)Module.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.base.common;

import java.text.MessageFormat;

import org.nlh4j.core.dto.AbstractEnum;
import org.nlh4j.util.NumberUtils;
import org.nlh4j.util.StringUtils;

/**
 * Constant Module Class
 * TODO CHildren class maybe extend to define module constant
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class Module extends AbstractEnum {

    /** */
    private static final long serialVersionUID = 1L;

    /**
     * Common LOGIN modules
     */
    public static final Module CMN_LOGIN = new Module(ModuleConst.CMN_LOGIN);

    /**
     * Common SOCKET modules
     */
    public static final Module CMN_SPRING_SEC = new Module(ModuleConst.CMN_SPRING_SEC);
    public static final Module CMN_SOCK = new Module(ModuleConst.CMN_SOCK);
    public static final Module CMN_SOCK_WS = new Module(ModuleConst.CMN_SOCK_WS);
    public static final Module CMN_SOCK_CHAT = new Module(ModuleConst.CMN_SOCK_CHAT);
    public static final Module CMN_SOCK_USERS_ONLINE = new Module(ModuleConst.CMN_SOCK_USERS_ONLINE);
    /**
     * Common security modules
     */
    public static final Module CMN_SEC_TOKEN = new Module(ModuleConst.CMN_SEC_TOKEN);
    public static final Module CMN_SEC_USERNAME = new Module(ModuleConst.CMN_SEC_USERNAME);

    /**
     * Common PERSONAL HOME modules
     */
    public static final Module CMN_HOME = new Module(ModuleConst.CMN_HOME);

    /**
     * Common PERSONAL TASK TODAY/NEXT modules (current user info)
     */
    public static final Module CMN_TASK_TODAY = new Module(ModuleConst.CMN_TASK_TODAY);
    public static final Module CMN_TASK_NEXT = new Module(ModuleConst.CMN_TASK_NEXT);

    /**
     * Common DASHBOARD modules
     */
    public static final Module CMN_DOWNLOAD_SERVICE = new Module(ModuleConst.CMN_DOWNLOAD_SERVICE);

    /**
     * Common CHANGE PASSWORD modules
     */
    public static final Module CMN_CHANGE_PASSWORD = new Module(ModuleConst.CMN_CHANGE_PASSWORD);

    /**
     * Common LOGOUT modules
     */
    public static final Module CMN_LOGOUT = new Module(ModuleConst.CMN_LOGOUT);

    /**
     * Common DASHBOARD modules
     */
    public static final Module CMN_DASHBOARD = new Module(ModuleConst.CMN_DASHBOARD);
    public static final Module CMN_DASHBOARD_MASTER = new Module(ModuleConst.CMN_DASHBOARD_MASTER);
    public static final Module CMN_DASHBOARD_SYSTEM = new Module(ModuleConst.CMN_DASHBOARD_SYSTEM);

    /**
     * System modules
     */
    public static final Module SYSTEM_ROLE = new Module(ModuleConst.SYSTEM_ROLE);
    public static final Module SYSTEM_USER = new Module(ModuleConst.SYSTEM_USER);
    public static final Module SYSTEM_MODULE = new Module(ModuleConst.SYSTEM_MODULE);
    public static final Module SYSTEM_FUNCTION = new Module(ModuleConst.SYSTEM_FUNCTION);
    ;

    /**
     * Initialize a new instance of {@link Module}
     *
     * @param code module code
     */
    public Module(String code) {
        this(code, null);
    }
    /**
     * Initialize a new instance of {@link Module}
     *
     * @param <T> the child module type
     * @param code module code
     * @param childs children modules array
     */
    public <T extends Module> Module(String code, T[] childs) {
        super(code, childs);
    }

    /**
     * Get the fixed module code by fixed length and character.<br>
     * See more at:<br>
     * {@link #getFixedCode(int, Character, boolean)}<br>
     * {@link #getDefaultFixedCode(boolean)}<br>
     * {@link #getDefaultPrefixFixedCode()}<br>
     * {@link #getDefaultSuffixFixedCode()}
     *
     * @return the fixed module code
     */
    public final String getFixedCode() {
        return super.getDefaultSuffixFixedCode();
    }

    /**
     * Get the page view code.<br>
     * The page number will filled, at prefix, with the specified character;
     * and attach after module code.<br>
     * See more at:<br>
     * {@link #getFixedPageCode(int, int, Character, boolean)}<br>
     * {@link #getDefaultFixedPageCode(int, boolean)}<br>
     * {@link #getDefaultPrefixFixedPageCode(int)}<br>
     * {@link #getDefaultSuffixFixedPageCode(int)}
     *
     * @param pageNum page number: 1 - index page; 2 - detail page; another for custom page
     *
     * @return the page view code
     */
    public final String getFixedPageCode(int pageNum) {
        return this.getDefaultPrefixFixedPageCode(pageNum);
    }
    /**
     * Get the page view code.<br>
     * The page number will filled, at prefix, with the specified character;
     * and attach after module code.<br>
     * See more at:<br>
     * {@link #getFixedPageCode(int, int, Character, boolean)}<br>
     * {@link #getDefaultFixedPageCode(int, boolean)}<br>
     * {@link #getDefaultPrefixFixedPageCode(int)}<br>
     * {@link #getDefaultSuffixFixedPageCode(int)}
     *
     * @param pageNum page number: 1 - index page; 2 - detail page; another for custom page
     * @param length to fix page view code.
     * this length will be compared with (the length of module code + the length of page number)
     * @param c character to fill
     * @param prefix true for filling prefix of new code
     *
     * @return the page view code
     */
    protected final String getFixedPageCode(int pageNum, int length, Character c, boolean prefix) {
        if (StringUtils.hasText(this.getCode())) {
            String moduleCode = this.getCode();
            pageNum = NumberUtils.max(new int[] { pageNum, 1 });
            String sPageNumber = String.valueOf(pageNum);
            if (sPageNumber.length() + moduleCode.length() < length) {
                sPageNumber = StringUtils.fixLength(
                        sPageNumber, length - moduleCode.length(), c, prefix);
            }
            return moduleCode + sPageNumber;

        } else logger.warn("Code of module must be not null!");
        return null;
    }
    /**
     * Get the page view code.<br>
     * The page number will filled, at prefix, with the default character;
     * and attach after module code.<br>
     * See more at:<br>
     * {@link #getFixedPageCode(int, int, Character, boolean)}<br>
     * {@link #getDefaultFixedPageCode(int, boolean)}<br>
     * {@link #getDefaultPrefixFixedPageCode(int)}<br>
     * {@link #getDefaultSuffixFixedPageCode(int)}
     *
     * @param pageNum page number: 1 - index page; 2 - detail page; another for custom page
     * @param prefix true for filling prefix of new code
     *
     * @return the page view code
     */
    protected final String getDefaultFixedPageCode(int pageNum, boolean prefix) {
        return getFixedPageCode(pageNum, DEFAULT_CODE_FIXED_LENGTH, DEFAULT_CODE_FIXED_CHARACTER, prefix);
    }
    /**
     * Get the page view code.<br>
     * The page number will filled with the default character;
     * and attach after module code.<br>
     * See more at:<br>
     * {@link #getFixedPageCode(int, int, Character, boolean)}<br>
     * {@link #getDefaultFixedPageCode(int, boolean)}<br>
     * {@link #getDefaultPrefixFixedPageCode(int)}<br>
     * {@link #getDefaultSuffixFixedPageCode(int)}
     *
     * @param pageNum page number: 1 - index page; 2 - detail page; another for custom page
     *
     * @return the page view code
     */
    protected final String getDefaultPrefixFixedPageCode(int pageNum) {
        return getDefaultFixedPageCode(pageNum, Boolean.TRUE);
    }
    /**
     * Get the page view code.<br>
     * The page number will filled with the default character;
     * and attach after module code.<br>
     * See more at:<br>
     * {@link #getFixedPageCode(int, int, Character, boolean)}<br>
     * {@link #getDefaultFixedPageCode(int, boolean)}<br>
     * {@link #getDefaultPrefixFixedPageCode(int)}<br>
     * {@link #getDefaultSuffixFixedPageCode(int)}
     *
     * @param pageNum page number: 1 - index page; 2 - detail page; another for custom page
     *
     * @return the page view code
     */
    protected final String getDefaultSuffixFixedPageCode(int pageNum) {
        return getDefaultFixedPageCode(pageNum, Boolean.FALSE);
    }

    /**
     * Get the index page view
     *
     * @return the index page view
     */
    public final String getIndexPage() {
        return this.getPage(1);
    }
    /**
     * Get the detail page view
     *
     * @return the detail page view
     */
    public final String getDetailPage() {
        return this.getPage(2);
    }
    /**
     * Get the path to the page view with the default filling length is 5;
     * and the filling character is '0'.
     *
     * @param pageNum page number: 1 - index page; 2 - detail page; another for custom page
     *
     * @return the path to the page view
     */
    public final String getPage(int pageNum) {
        return getPage(pageNum, DEFAULT_CODE_FIXED_LENGTH, DEFAULT_CODE_FIXED_CHARACTER);
    }
    /**
     * Get the path to the page view.<br>
     * The module code will be filled, at suffix, with the specified character;<br>
     * and the page number will filled, at prefix, with the specified character.
     *
     * @param pageNum page number: 1 - index page; 2 - detail page; another for custom page
     * @param length to fix page number and module code
     * @param c character to fill
     *
     * @return the path to the page view
     */
    public final String getPage(int pageNum, int length, Character c) {
        if (StringUtils.hasText(this.getCode())) {
            String fixedModCode = this.getFixedCode();
            String fixedPageCode = this.getFixedPageCode(pageNum);
            return MessageFormat.format("{0}/{1}", fixedModCode, fixedPageCode);

        } else logger.warn("Code of module must be not null!");
        return null;
    }
}
