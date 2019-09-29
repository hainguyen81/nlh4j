/*
 * @(#)TimestampWriteConverter.java Aug 13, 2019
 * Copyright 2019 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.converters.write;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Read convert to convert from {@link Timestamp} to mongo {@link Date}
 * @author Hai Nguyen
 */
@Component
public class TimestampWriteConverter implements Converter<Timestamp, Date>, Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
	 */
	@Override
	public Date convert(Timestamp source) {
		return (source == null ? null : new Date(source.getTime()));
	}
}
