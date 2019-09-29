/*
 * @(#)ObjectIdDeserializer.java 1.0 Aug 28, 2015
 * Copyright 2015 by SystemEXE Inc. All rights reserved.
 */
package org.nlh4j.mongo.dto.bindeditor.json.fasterxml;

import java.io.IOException;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * {@link ObjectId} JSON deserializer
 *
 * @author Hai Nguyen
 */
public class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext)
	 */
	@Override
	public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonToken t = p.getCurrentToken();
		// String is ok too, can easily convert
        if (t == JsonToken.VALUE_STRING) { // let's do implicit re-parse
            String text = p.getText().trim();
            if (text.length() == 0 || !ObjectId.isValid(text)) return null;
            try { return new ObjectId(text); }
            catch (IllegalArgumentException iae) {
                throw ctxt.weirdStringException("{" + text + "}", ObjectId.class, " not a valid representation");
            }
        }
		return null;
	}

}
