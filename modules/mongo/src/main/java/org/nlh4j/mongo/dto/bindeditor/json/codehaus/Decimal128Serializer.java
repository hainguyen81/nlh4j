/*
 * @(#)Decimal128Serializer.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.dto.bindeditor.json.codehaus;

import java.io.IOException;

import org.bson.types.Decimal128;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * {@link Decimal128} JSON serializer
 *
 * @author Hai Nguyen
 */
public class Decimal128Serializer extends JsonSerializer<Decimal128> {

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
	 */
	@Override
	public void serialize(Decimal128 value, JsonGenerator gen, SerializerProvider arg2) throws IOException {
		if (value != null) {
			gen.writeNumber(value.toString());
		}
	}
}
