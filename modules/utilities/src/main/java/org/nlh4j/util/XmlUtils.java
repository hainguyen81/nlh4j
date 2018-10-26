/*
 * @(#)XmlUtils.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;

/**
 * The Jersey XML utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
public final class XmlUtils implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The XML mapper for serializing/de-serializing
	 */
	private transient static XmlMapper xmlMapper = null;
	/**
	 * Gets the XML mapper for serializing/de-serializing
	 *
	 * @return the XML mapper for serializing/de-serializing
	 */
	protected static final XmlMapper getMapper() {
		if (xmlMapper == null) xmlMapper = new XmlMapper();
		synchronized(xmlMapper) {
			return xmlMapper;
		}
	}

	// -------------------------------------------------
	// Configuration
	// -------------------------------------------------

	/**
	 * Configure mapper features.
	 *
	 * @param features the {@link ObjectMapper} features for configuring
	 */
	public static void configure(Map<ToXmlGenerator.Feature, Boolean> features) {
		for(final Iterator<ToXmlGenerator.Feature> it = features.keySet().iterator(); it.hasNext();) {
			ToXmlGenerator.Feature f = it.next();
			Boolean state = features.get(f);
			state = (Boolean.TRUE.equals(state) ? Boolean.TRUE : Boolean.FALSE);
			// disables all automatic property detection features
			getMapper().configure(f, state);
		}
	}
	/**
	 * Configure mapper features.
	 *
	 * @param features the {@link ObjectMapper} features for configuring
	 */
	public static void configure2(Map<SerializationFeature, Boolean> features) {
		for(final Iterator<SerializationFeature> it = features.keySet().iterator(); it.hasNext();) {
			SerializationFeature f = it.next();
			Boolean state = features.get(f);
			state = (Boolean.TRUE.equals(state) ? Boolean.TRUE : Boolean.FALSE);
			// disables all automatic property detection features
			getMapper().configure(f, state);
		}
	}
	/**
	 * Configure mapper features.
	 *
	 * @param features the {@link ObjectMapper} features for configuring
	 */
	public static void configure3(Map<MapperFeature, Boolean> features) {
		for(final Iterator<MapperFeature> it = features.keySet().iterator(); it.hasNext();) {
			MapperFeature f = it.next();
			Boolean state = features.get(f);
			state = (Boolean.TRUE.equals(state) ? Boolean.TRUE : Boolean.FALSE);
			// disables all automatic property detection features
			getMapper().configure(f, state);
		}
	}

	// -------------------------------------------------
	// Serialization
	// -------------------------------------------------

	/**
	 * Serializes the specified object to XML.
	 *
	 * @param <T> entity type
	 * @param object the object for serializing
	 *
	 * @return the serialized XML data.
     * @throws WebApplicationException thrown if fail
	 */
	public static <T> String serialize(T object)
			throws WebApplicationException {
		try {
			String json = getMapper().writeValueAsString(object);
			return json;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
	/**
	 * Serializes the specified object to XML.
	 *
     * @param <T> entity type
	 * @param object the object for serializing
	 *
	 * @return the serialized XML data.
	 * @throws WebApplicationException thrown if fail
	 */
    public static <T> String manualSerialize(T object)
			throws WebApplicationException {
		// configures
		Map<ToXmlGenerator.Feature, Boolean> features1 = new LinkedHashMap<ToXmlGenerator.Feature, Boolean>();
		features1.put(ToXmlGenerator.Feature.WRITE_XML_1_1, Boolean.TRUE);
		configure(features1);
		// configures
		Map<SerializationFeature, Boolean> features2 = new LinkedHashMap<SerializationFeature, Boolean>();
		features2.put(SerializationFeature.FAIL_ON_EMPTY_BEANS, Boolean.FALSE);
		features2.put(SerializationFeature.WRAP_EXCEPTIONS, Boolean.FALSE);
		features2.put(SerializationFeature.WRITE_NULL_MAP_VALUES, Boolean.FALSE);
		configure2(features2);
		// configures
		Map<MapperFeature, Boolean> features3 = new LinkedHashMap<MapperFeature, Boolean>();
		features3.put(MapperFeature.AUTO_DETECT_FIELDS, Boolean.FALSE);
		features3.put(MapperFeature.AUTO_DETECT_GETTERS, Boolean.FALSE);
		features3.put(MapperFeature.AUTO_DETECT_IS_GETTERS, Boolean.FALSE);
		configure3(features3);
		// serializes
		return serialize(object);
	}

	// -------------------------------------------------
	// De-serialization
	// -------------------------------------------------

	/**
	 * De-serializes the specified XML data to the specified type
	 *
     * @param <T> entity type
	 * @param xml the serialized XML data
	 * @param valueType the type to de-serialize
	 *
	 * @return the de-serialized object by the specified type
     * @throws WebApplicationException thrown if fail
	 */
    public static <T> T deserialize(String xml, Class<T> valueType)
    		throws WebApplicationException {
		try {
			T object = getMapper().readValue(xml, valueType);
			return object;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}
    }
    /**
	 * De-serializes the specified XML data to the specified type reference
	 *
     * @param <T> entity type
	 * @param xml the serialized XML data
	 * @param valueTypeRef the type reference to de-serialize
	 *
	 * @return the de-serialized object by the specified type reference
     * @throws WebApplicationException thrown if fail
	 */
    public static <T> T deserialize(String xml, TypeReference<T> valueTypeRef)
    		throws WebApplicationException {
		try {
			T object = getMapper().readValue(xml, valueTypeRef);
			return object;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}
    }
    /**
	 * De-serializes the specified XML data to the specified {@link JavaType}
	 *
     * @param <T> entity type
	 * @param xml the serialized XML data
	 * @param valueType the {@link JavaType} to de-serialize
	 *
	 * @return the de-serialized object by the specified {@link JavaType}
     * @throws WebApplicationException thrown if fail
	 */
    public static <T> T deserialize(String xml, JavaType valueType)
    		throws WebApplicationException {
		try {
			T object = getMapper().readValue(xml, valueType);
			return object;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
		}
    }
}
