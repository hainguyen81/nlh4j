/*
 * @(#)JsonUtils.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.http.HttpStatus;

/**
 * The Jersey JSON utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public final class JsonUtils implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The JSON mapper for serializing/de-serializing
	 */
	private static transient ObjectMapper jsonMapper = null;

	/**
	 * Gets the JSON mapper for serializing/de-serializing
	 *
	 * @return the JSON mapper for serializing/de-serializing
	 */
	protected static final ObjectMapper getMapper() {
		if (jsonMapper == null) jsonMapper = new ObjectMapper();
		synchronized(jsonMapper) {
			return jsonMapper;
		}
	}

	// -------------------------------------------------
	// Serialization
	// -------------------------------------------------

	/**
	 * Serializes the specified object to JSON.
	 *
	 * @param <T> entity type
	 * @param object the object for serializing
	 *
	 * @return the serialized JSON data.
	 * @throws WebApplicationException thrown if fail
	 */
	public static <T> String serialize(T object) throws WebApplicationException {
		try {
			String json = getMapper().writeValueAsString(object);
			return json;
		}
		catch (JsonGenerationException e) {
			e.printStackTrace();
			throw new WebApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		catch (JsonMappingException e) {
			e.printStackTrace();
			throw new WebApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new WebApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}
	/**
	 * Serializes the specified object to JSON.
	 *
     * @param <T> entity type
	 * @param object the object for serializing
	 * @param features the {@link ObjectMapper} features for configuring
	 *
	 * @return the serialized JSON data.
     * @throws WebApplicationException thrown if fail
	 */
	public static <T> String serialize(T object, Map<Feature, Boolean> features) throws WebApplicationException {
		if (!CollectionUtils.isEmpty(features)) {
			for(final Iterator<Feature> it = features.keySet().iterator(); it.hasNext();) {
				Feature f = it.next();
				Boolean state = features.get(f);
				state = (Boolean.TRUE.equals(state) ? Boolean.TRUE : Boolean.FALSE);
				// disables all automatic property detection features
				getMapper().configure(f, state);
			}
		}
		// serializes result
		return serialize(object);
	}
	/**
	 * Serializes the specified object to JSON.
	 *
     * @param <T> entity type
	 * @param object the object for serializing
	 * @param features the {@link ObjectMapper} features for configuring
	 *
	 * @return the serialized JSON data.
     * @throws WebApplicationException thrown if fail
	 */
	public static <T> String serialize2(T object, Map<SerializationConfig.Feature, Boolean> features) throws WebApplicationException {
		if (!CollectionUtils.isEmpty(features)) {
			for(final Iterator<SerializationConfig.Feature> it = features.keySet().iterator(); it.hasNext();) {
				SerializationConfig.Feature f = it.next();
				Boolean state = features.get(f);
				state = (Boolean.TRUE.equals(state) ? Boolean.TRUE : Boolean.FALSE);
				// disables all automatic property detection features
				getMapper().configure(f, state);
			}
		}
		// serializes result
		return serialize(object);
	}
	/**
	 * Serializes the specified object to JSON.
	 *
     * @param <T> entity type
	 * @param object the object for serializing
	 * @param features the {@link ObjectMapper} features for configuring
	 *
	 * @return the serialized JSON data.
     * @throws WebApplicationException thrown if fail
	 */
	public static <T> String serialize3(T object, Map<JsonParser.Feature, Boolean> features) throws WebApplicationException {
		if (!CollectionUtils.isEmpty(features)) {
			for(final Iterator<JsonParser.Feature> it = features.keySet().iterator(); it.hasNext();) {
				JsonParser.Feature f = it.next();
				Boolean state = features.get(f);
				state = (Boolean.TRUE.equals(state) ? Boolean.TRUE : Boolean.FALSE);
				// disables all automatic property detection features
				getMapper().configure(f, state);
			}
		}
		// serializes result
		return serialize(object);
	}
	/**
	 * Serializes the specified object to JSON.
	 *
     * @param <T> entity type
	 * @param object the object for serializing
	 *
	 * @return the serialized JSON data.
     * @throws WebApplicationException thrown if fail
	 */
	public static <T> String manualSerialize(T object) throws WebApplicationException {
		Map<SerializationConfig.Feature, Boolean> features = new LinkedHashMap<SerializationConfig.Feature, Boolean>();
		features.put(SerializationConfig.Feature.AUTO_DETECT_FIELDS, Boolean.FALSE);
		features.put(SerializationConfig.Feature.AUTO_DETECT_GETTERS, Boolean.FALSE);
		features.put(SerializationConfig.Feature.AUTO_DETECT_IS_GETTERS, Boolean.FALSE);
		return serialize2(object, features);
	}

	// -------------------------------------------------
	// De-serialization
	// -------------------------------------------------

	/**
	 * De-serializes the specified JSON data to the specified type
	 *
     * @param <T> entity type
	 * @param json the serialized JSON data
	 * @param valueType the type to de-serialize
	 *
	 * @return the de-serialized object by the specified type
     * @throws WebApplicationException thrown if fail
	 */
    public static <T> T deserialize(String json, Class<T> valueType) throws WebApplicationException {
		try {
			T object = getMapper().readValue(json, valueType);
			return object;
		}
		catch (JsonGenerationException e) {
			e.printStackTrace();
			throw new WebApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		catch (JsonMappingException e) {
			e.printStackTrace();
			throw new WebApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new WebApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
    }
    /**
	 * De-serializes the specified JSON data to the specified type reference
	 *
     * @param <T> entity type
	 * @param json the serialized JSON data
	 * @param valueTypeRef the type reference to de-serialize
	 *
	 * @return the de-serialized object by the specified type reference
     * @throws WebApplicationException thrown if fail
	 */
    public static <T> T deserialize(String json, TypeReference<T> valueTypeRef) throws WebApplicationException {
		try {
			T object = getMapper().readValue(json, valueTypeRef);
			return object;
		}
		catch (JsonGenerationException e) {
			e.printStackTrace();
			throw new WebApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		catch (JsonMappingException e) {
			e.printStackTrace();
			throw new WebApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new WebApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
    }
    /**
	 * De-serializes the specified JSON data to the specified {@link JavaType}
	 *
     * @param <T> entity type
	 * @param json the serialized JSON data
	 * @param valueType the {@link JavaType} to de-serialize
	 *
	 * @return the de-serialized object by the specified {@link JavaType}
     * @throws WebApplicationException thrown if fail
	 */
    public static <T> T deserialize(String json, JavaType valueType) throws WebApplicationException {
		try {
			T object = getMapper().readValue(json, valueType);
			return object;
		}
		catch (JsonGenerationException e) {
			e.printStackTrace();
			throw new WebApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		catch (JsonMappingException e) {
			e.printStackTrace();
			throw new WebApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new WebApplicationException(e, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
    }
}
