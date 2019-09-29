/*
 * @(#)BaseEntity.java Aug 24, 2019
 * Copyright 2019 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.dto;

import java.sql.Timestamp;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.codehaus.jackson.map.annotate.JsonCachable;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.nlh4j.core.dto.AbstractDto;
import org.nlh4j.mongo.dto.bindeditor.json.codehaus.ObjectIdDeserializer;
import org.nlh4j.mongo.dto.bindeditor.json.codehaus.ObjectIdSerializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The base entity for database
 * @author nlhai
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@org.codehaus.jackson.annotate.JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_EMPTY)
@JsonCachable(value = true)
@ReadingConverter
@WritingConverter
@Document
public abstract class BaseEntity extends AbstractDto {

	/** */
	private static final long serialVersionUID = 1L;

	@Id
	@org.codehaus.jackson.annotate.JsonProperty("id")
	@com.fasterxml.jackson.annotation.JsonProperty("id")
	@JsonSerialize(using = ObjectIdSerializer.class)
	@JsonDeserialize(using = ObjectIdDeserializer.class)
	@com.fasterxml.jackson.databind.annotation.JsonSerialize(
			using = org.nlh4j.mongo.dto.bindeditor.json.fasterxml.ObjectIdSerializer.class)
	@com.fasterxml.jackson.databind.annotation.JsonDeserialize(
			using = org.nlh4j.mongo.dto.bindeditor.json.fasterxml.ObjectIdDeserializer.class)
	private ObjectId _id; // primary key

	private Timestamp createdDate;
	private Timestamp updatedDate;
	@com.fasterxml.jackson.annotation.JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	private Timestamp deletedDate;

	private String createdUser;
	private String updatedUser;
	@com.fasterxml.jackson.annotation.JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	private String deletedUser;

	/** Current login user */
	@Transient
	@com.fasterxml.jackson.annotation.JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	private String currentUser;

	@Transient
	@com.fasterxml.jackson.annotation.JsonIgnore
	@org.codehaus.jackson.annotate.JsonIgnore
	public boolean isNew() {
		return (Objects.isNull(get_id())
				|| !StringUtils.hasText(get_id().toHexString()));
	}
}