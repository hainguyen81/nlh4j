/*
 * @(#)ObjectIdDeserializer.java 1.0 Aug 28, 2015
 * Copyright 2015 by SystemEXE Inc. All rights reserved.
 */
package org.nlh4j.mongo.dto.bindeditor.json.codehaus;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

/**
 * {@link ObjectId} JSON deserializer
 *
 * @author Hai Nguyen
 */
public class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {

	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext)
	 */
	@Override
	public ObjectId deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		JsonToken t = jp.getCurrentToken();
		// String is ok too, can easily convert
        if (t == JsonToken.VALUE_STRING) { // let's do implicit re-parse
            String text = jp.getText().trim();
            if (text.length() == 0 || !ObjectId.isValid(text)) return null;
            try { return new ObjectId(text); }
            catch (IllegalArgumentException iae) {
                throw ctxt.weirdStringException(ObjectId.class, "{" + text + "} not a valid representation");
            }
        }
		return null;
	}
}
