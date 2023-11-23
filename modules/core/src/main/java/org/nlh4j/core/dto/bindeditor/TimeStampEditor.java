/*
 * @(#)TimeStampEditor.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto.bindeditor;

import java.beans.PropertyEditorSupport;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.DateUtils;

/**
 * @see java.sql.Timestamp データ型エディタ
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public class TimeStampEditor extends PropertyEditorSupport implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /** date/time format pattern */
	@Getter
	@Setter
	private String pattern;

	/** specify date/time whether allows empty/null */
	@Getter
	@Setter
	private boolean allowEmpty;

	/**
	 * 初期化
	 * @param pattern date/time format pattern
	 */
	public TimeStampEditor(String pattern) {
		this(pattern, true);
	}
	public TimeStampEditor(String pattern, boolean allowEmpty) {
		this.setAllowEmpty(allowEmpty);
		this.setPattern(pattern);
	}

	/*
	 * (Non-Javadoc)
	 * @see java.beans.PropertyEditorSupport#getAsText()
	 */
	@Override
	public String getAsText() {
		return (!BeanUtils.isInstanceOf(super.getValue(), Date.class)
				? null : DateUtils.formatDateTime(this.getPattern(), (Date) super.getValue()));
	}

	/*
	 * (Non-Javadoc)
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.isAllowEmpty() && !StringUtils.hasText(text)) {
			setValue(null);
		}
		else {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(this.getPattern());
				Date parsedDate = dateFormat.parse(text);
				super.setValue(new java.sql.Timestamp(parsedDate.getTime()));
			}
			catch (Exception e) {
				throw new IllegalArgumentException(
						"Could not parse date: " + e.getMessage() + " by the specified pattern [" + this.getPattern() + "]", e);
			}
		}
	}
}
