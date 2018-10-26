/*
 * @(#)FunctionDto.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.dto;

import java.text.MessageFormat;

import org.seasar.doma.Entity;
import org.seasar.doma.jdbc.entity.NamingType;
import org.springframework.util.StringUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.nlh4j.core.dto.AbstractDto;

/**
 * Function DTO information
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@EqualsAndHashCode(callSuper = false)
@Entity(naming = NamingType.SNAKE_UPPER_CASE)
public class FunctionDto extends AbstractDto {

    /** */
    private static final long serialVersionUID = 1L;
    /** FUNCTION identity */
    @Getter
    @Setter
    private Long id;
    /** FUNCTION code */
    @Getter
    @Setter
    private String code;
    /** FUNCTION name */
    @Getter
    @Setter
    private String name;
    /** FUNCTION name key from language resource file */
    @Getter
    @Setter
    private String langKey;
    /** FUNCTION display order */
    @Getter
    @Setter
    private Integer funcOrder;
    /** Specify this function whether is enabled */
    @Getter
    @Setter
    private Boolean enabled;

    public String getDisplay() {
        if (!StringUtils.hasText(this.getLangKey())) return this.getName();
        return super.getMessageService().getMessage(this.getLangKey());
    }
    public String getCodeNameDisplay() {
        if (!StringUtils.hasText(this.getCode())) return null;
        String name = this.getName();
        if (StringUtils.hasText(this.getLangKey())) {
            name = super.getMessageService().getMessage(this.getLangKey());
        }
        return MessageFormat.format("{0} - {1}", this.getCode(), name);
    }
}
