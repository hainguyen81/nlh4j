/*
 * @(#)AbstractEnum.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.StringUtils;

/**
 * Abstract Enumeration Class
 * TODO Children class maybe extend to define enum constant
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public abstract class AbstractEnum extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;
    protected static final int DEFAULT_CODE_FIXED_LENGTH = 5;
    protected static final char DEFAULT_CODE_FIXED_CHARACTER = '0';

    /** NONE value */
    public static final AbstractEnum NONE = null;
    ;

    /** enumeration code */
    private String code;
    /** children enumeration values array */
    private AbstractEnum[] childs;
    /**
     * Initialize a new instance of {@link AbstractEnum}
     *
     * @param code enumeration code
     */
    public AbstractEnum(String code) {
        this(code, null);
    }
    /**
     * Initialize a new instance of {@link AbstractEnum}
     *
     * @param <T> the children enumeration class type
     * @param code enumeration code
     * @param childs children enumeration values array
     */
    public <T extends AbstractEnum> AbstractEnum(String code, T[] childs) {
        this.setCode(code);
        this.setChilds(childs);
    }

    /**
     * Get the enumeration code
     *
     * @return the enumeration code
     */
    public final String getCode() {
        return this.code;
    }
    /**
     * Set enumeration code
     *
     * @param code enumeration code
     */
    protected void setCode(String code) {
        this.code = code;
    }
    /**
     * Get the children enumeration values array
     *
     * @return the children enumeration values array
     */
    public final AbstractEnum[] getChilds() {
        return this.childs;
    }
    /**
     * Set the children enumeration values array
     *
     * @param childs children enumeration values array
     */
    protected <T extends AbstractEnum> void setChilds(T[] childs) {
        if (!ArrayUtils.isEmpty(childs)) {
            this.childs = (AbstractEnum[]) childs;
        }
        else {
            this.childs = new AbstractEnum[] {};
        }
    }

    /**
     * Get {@link AbstractEnum} enumeration values
     *
     * @param <T> the children enumeration class type
     * @param enumClazz enumeration class (a children class of {@link AbstractEnum})
     *
     * @return {@link AbstractEnum} enumeration values or NULL if failed or not found
     */
    public static final <T extends AbstractEnum> T[] values(Class<T> enumClazz) {
        List<T> values = new LinkedList<T>();
        if (enumClazz != null) {
            List<Field> fields = BeanUtils.getFields(enumClazz);
            for(Field field : fields) {
                int modifiers = field.getModifiers();
                // require static and public field
                if (!Modifier.isStatic(modifiers) && !Modifier.isPublic(modifiers)) continue;
                if (!BeanUtils.isInstanceOf(field.getType(), enumClazz)) continue;
                T fieldVal = null;
                try { fieldVal = BeanUtils.safeType(field.get(null), enumClazz); }
                catch(Exception ex) { fieldVal = null; }
                if (fieldVal != null) { values.add(fieldVal); }
            }
            try { return CollectionUtils.toArray(values, enumClazz); }
            catch (Exception e) { return null; }
        }
        return null;
    }
    /**
     * Get {@link AbstractEnum} from enumeration class and code
     *
     * @param <T> the children enumeration class type
     * @param enumClazz enumeration class (a children class of {@link AbstractEnum})
     * @param code enumeration code
     * @param ignoreCase specify comparing code in-sensitive
     *
     * @return {@link AbstractEnum} from enumeration class and code or NULL
     */
    public static final <T extends AbstractEnum> AbstractEnum fromCode(
            Class<T> enumClazz, String code, boolean ignoreCase) {
        AbstractEnum e = AbstractEnum.NONE;
        if (StringUtils.hasText(code)) {
            T[] values = AbstractEnum.values(enumClazz);
            if (!CollectionUtils.isEmpty(values)) {
                for(T value : values) {
                    if (value != null
                            && (code.equals(value.getCode())
                                    || (ignoreCase && code.equalsIgnoreCase(value.getCode())))) {
                        return value;
                    }
                }
            }
        }
        return e;
    }
    /**
     * Get {@link AbstractEnum} from enumeration class and code (case in-sensitive)
     *
     * @param <T> the children enumeration class type
     * @param enumClazz enumeration class (a children class of {@link AbstractEnum})
     * @param code enumeration code
     *
     * @return {@link AbstractEnum} from enumeration class and code or NULL
     */
    public static final <T extends AbstractEnum> AbstractEnum fromCode(Class<T> enumClazz, String code) {
        return AbstractEnum.fromCode(enumClazz, code, Boolean.TRUE);
    }

    /**
     * Get the fixed enumeration code by fixed length and character.<br>
     * See more at:<br>
     * {@link #getFixedCode(int, Character, boolean)}<br>
     * {@link #getDefaultFixedCode(boolean)}<br>
     * {@link #getDefaultPrefixFixedCode()}<br>
     * {@link #getDefaultSuffixFixedCode()}
     *
     * @param length to fix page number and module code
     * @param c character to fill
     * @param prefix true for filling prefix of new code
     *
     * @return the fixed enumeration code
     */
    protected final String getFixedCode(int length, Character c, boolean prefix) {
        if (StringUtils.hasText(this.getCode())) {
            return StringUtils.fixLength(this.getCode(), length, c, prefix);

        } else logger.warn("Code of enum must be not null!");
        return null;
    }
    /**
     * Get the fixed enumeration code by default fixed length and character.<br>
     * See more at:<br>
     * {@link #getFixedCode(int, Character, boolean)}<br>
     * {@link #getDefaultFixedCode(boolean)}<br>
     * {@link #getDefaultPrefixFixedCode()}<br>
     * {@link #getDefaultSuffixFixedCode()}
     *
     * @param c character to fill
     * @param prefix true for filling prefix of new code
     *
     * @return the default fixed enumeration code
     */
    protected final String getDefaultFixedCode(boolean prefix) {
        if (StringUtils.hasText(this.getCode())) {
            return StringUtils.fixLength(this.getCode(),
                    DEFAULT_CODE_FIXED_LENGTH, DEFAULT_CODE_FIXED_CHARACTER, prefix);

        } else logger.warn("Code of enum must be not null!");
        return null;
    }
    /**
     * Get the fixed enumeration code by default fixed length (at suffix) and character.<br>
     * See more at:<br>
     * {@link #getFixedCode(int, Character, boolean)}<br>
     * {@link #getDefaultFixedCode(boolean)}<br>
     * {@link #getDefaultPrefixFixedCode()}<br>
     * {@link #getDefaultSuffixFixedCode()}
     *
     * @param c character to fill
     *
     * @return the default fixed enumeration code
     */
    protected final String getDefaultSuffixFixedCode() {
        return this.getDefaultFixedCode(Boolean.FALSE);
    }
    /**
     * Get the fixed enumeration code by default fixed length (at prefix) and character.<br>
     * See more at:<br>
     * {@link #getFixedCode(int, Character, boolean)}<br>
     * {@link #getDefaultFixedCode(boolean)}<br>
     * {@link #getDefaultPrefixFixedCode()}<br>
     * {@link #getDefaultSuffixFixedCode()}
     *
     * @param c character to fill
     *
     * @return the default fixed enumeration code
     */
    protected final String getDefaultPrefixFixedCode() {
        return this.getDefaultFixedCode(Boolean.TRUE);
    }
}
