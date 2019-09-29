/*
 * @(#)TimestampReadConverter.java Aug 13, 2019
 * Copyright 2019 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.converters.read;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.nlh4j.util.DateUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Read convert to convert from mongo {@link Date} to {@link Timestamp}
 * @author Hai Nguyen
 */
@Component
public class TimestampReadConverter implements Converter<Date, Timestamp>, Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public Timestamp convert(Date source) {
		return (source == null ? null : DateUtils.toTimestamp(source));
	}

}
