/*
 * @(#)BigDecimalJsonDeserializer.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.dto.bindeditor.json;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;

/**
 * JSON {@link BigDecimal} JSON data type deserializer
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public class BigDecimalJsonDeserializer
	extends com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigDecimalDeserializer
	implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

	/*
	 * (Non-Javadoc)
	 * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext)
	 */
	@Override
	public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonToken t = p.getCurrentToken();
		// String is ok too, can easily convert
        if (t == JsonToken.VALUE_STRING) { // let's do implicit re-parse
            String text = p.getText().trim();
            if (text.length() == 0) return null;
            try {
            	// TODO Always using group separator as comma (,) and decimal separator as dot (.)
            	Locale locale = LocaleContextHolder.getLocale();
				DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(locale);
				dfs.setDecimalSeparator('.');
				dfs.setGroupingSeparator(',');
            	String grpRegex = "[" + dfs.getGroupingSeparator() + "]";
            	text = text.replaceAll(grpRegex, "");
                return new BigDecimal(text);
            }
            catch (IllegalArgumentException iae) {
                throw ctxt.weirdStringException(text, _valueClass, "not a valid representation");
            }
        }
		return super.deserialize(p, ctxt);
	}
}
