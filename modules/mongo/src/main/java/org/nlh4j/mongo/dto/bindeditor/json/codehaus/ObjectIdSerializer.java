/*
 * @(#)ObjectIdSerializer.java 1.0 Aug 28, 2015
 * Copyright 2015 by SystemEXE Inc. All rights reserved.
 */
package org.nlh4j.mongo.dto.bindeditor.json.codehaus;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

/**
 * {@link ObjectId} JSON serializer
 *
 * @author Hai Nguyen
 */
public class ObjectIdSerializer extends JsonSerializer<ObjectId> {

	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object, org.codehaus.jackson.JsonGenerator, org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(ObjectId value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		if (value != null) jgen.writeString(value.toHexString());
	}
}
