/*
 * @(#)Decimal128Derializer.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.dto.bindeditor.json.fasterxml;

import java.io.IOException;
import java.math.BigDecimal;

import org.bson.types.Decimal128;
import org.nlh4j.util.BeanUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.POJONode;

/**
 * {@link Decimal128} JSON deserializer
 *
 * @author Hai Nguyen
 */
public class Decimal128Derializer extends JsonDeserializer<Decimal128> {

	/* (non-Javadoc)
	 * @see org.codehaus.jackson.map.JsonDeserializer#deserialize(org.codehaus.jackson.JsonParser, org.codehaus.jackson.map.DeserializationContext)
	 */
	@Override
	public Decimal128 deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		JsonToken t = jp.getCurrentToken();
		// String is ok too, can easily convert
        if (t == JsonToken.VALUE_STRING
        		|| t == JsonToken.VALUE_NUMBER_FLOAT
        		|| t == JsonToken.VALUE_NUMBER_INT) { // let's do implicit re-parse
            String text = jp.getText().trim();
            if (text.length() == 0) return null;
            try { return new Decimal128(new BigDecimal(text)); }
            catch (IllegalArgumentException iae) {
            	throw ctxt.weirdStringException("{" + text + "}", Decimal128.class, " not a valid representation");
            }
        } else {
        	try {
	        	JsonNode treeNode = jp.getCodec().readTree(jp);
	        	if (treeNode.isObject()) {
	        		String text = (treeNode.has("$numberDecimal")
	        				? treeNode.get("$numberDecimal").asText() : null);
	        		return new Decimal128(new BigDecimal(text));

	        	} else if (BeanUtils.isInstanceOf(treeNode, POJONode.class)) {
	        		POJONode node = BeanUtils.safeType(treeNode, POJONode.class);
	        		if (node != null && node.getPojo() != null) {
	        			Object value = node.getPojo();
	        			if (BeanUtils.isInstanceOf(value, Decimal128.class)) {
	        				return BeanUtils.safeType(value, Decimal128.class);
	        			} else if (BeanUtils.isInstanceOf(value, Number.class)) {
	        				Number valNumber = BeanUtils.safeType(value, Number.class);
	        				if (valNumber != null) {
	        					return new Decimal128(new BigDecimal(valNumber.toString()));
	        				}
	        			} else {
	        				return new Decimal128(new BigDecimal(value.toString()));
	        			}
	        		}
	        	}
        		throw new IllegalArgumentException("Decimal128 not a valid representation");
        	} catch (Exception e) {
        		throw ctxt.weirdStringException("{" + t.asString() + "}", Decimal128.class, " not a valid representation");
            }
        }
	}
}
