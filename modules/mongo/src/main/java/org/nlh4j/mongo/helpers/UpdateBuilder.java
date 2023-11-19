/*
 * @(#)UpdateBuilder.java Sep 26, 2019
 * Copyright 2019 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.helpers;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.nlh4j.mongo.helpers.MongoConstants.MongoKeywords;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Update.Position;
import org.springframework.util.StringUtils;

/**
 * {@link Update} Builder
 * @author Hai Nguyen
 */
public final class UpdateBuilder implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * Create {@link Update} by the specified updater information
	 * @param updaters to build
	 * @return {@link Update}
	 */
	public static Update createUpdater(Map<String, Object> updaters) {
		Update update = new Update();
		addUpdater(update, updaters);
		return update;
	}

	/**
	 * Add update information into the specified {@link Update}
	 * @param update to add
	 * @param updaters update information
	 * @return {@link Update}
	 */
	public static Update addUpdater(Update update, Map<String, Object> updaters) {
		update = (update == null ? new Update() : update);
		if (MapUtils.isNotEmpty(updaters)) {
			for(final Iterator<String> it = updaters.keySet().iterator(); it.hasNext();) {
				String field = it.next();
				if (!StringUtils.hasText(field)) continue;
				Object val = updaters.get(field);
				String fieldName = field;

				// $addToSet
				if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_ADDTOSET)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_ADDTOSET,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;

					if (CollectionUtils.isCollection(val) || CollectionUtils.isArray(val)) {
						Collection<Object> values = new LinkedList<>(CollectionUtils.toList(val, Object.class));
						update.addToSet(fieldName).each(values.toArray(new Object[values.size()]));

					} else {
						update.addToSet(fieldName, val);
					}

					// $pull
				} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_PULL)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_PULL,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;

					if (CollectionUtils.isCollection(val) || CollectionUtils.isArray(val)) {
						Collection<Object> values = new LinkedList<>(CollectionUtils.toList(val, Object.class));
						update.pullAll(fieldName, values.toArray(new Object[values.size()]));

					} else {
						update.pull(fieldName, val);
					}

					// $push
				} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_PUSH)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_PUSH,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;

					if (CollectionUtils.isCollection(val) || CollectionUtils.isArray(val)) {
						Collection<Object> values = new LinkedList<>(CollectionUtils.toList(val, Object.class));
						update.push(fieldName).each(values.toArray(new Object[values.size()]));

					} else {
						update.push(fieldName, val);
					}

					// $unset
				} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_UNSET)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_UNSET,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;
					update.unset(fieldName);

					// $currentDate
				} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_CURRENTDATE)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_CURRENTDATE,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;
					update.currentDate(fieldName);

					// $currentTimestamp
				} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_CURRENTTIMESTAMP)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_CURRENTTIMESTAMP,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;
					update.currentTimestamp(fieldName);

					// $bit (and)
				} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_BIT_AND)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_BIT_AND,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;

					if (BeanUtils.isInstanceOf(val, Number.class)) {
						update.bitwise(fieldName).and(Number.class.cast(val).longValue());

					} else {
						throw new IllegalArgumentException("Bitwise value must be a number!");
					}

					// $bit (or)
				} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_BIT_OR)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_BIT_OR,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;

					if (BeanUtils.isInstanceOf(val, Number.class)) {
						update.bitwise(fieldName).or(Number.class.cast(val).longValue());

					} else {
						throw new IllegalArgumentException("Bitwise value must be a number!");
					}

					// $bit (xor)
				} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_BIT_XOR)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_BIT_XOR,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;

					if (BeanUtils.isInstanceOf(val, Number.class)) {
						update.bitwise(fieldName).xor(Number.class.cast(val).longValue());

					} else {
						throw new IllegalArgumentException("Bitwise value must be a number!");
					}

					// $inc
				} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_INC)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_INC,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;
					if (BeanUtils.isInstanceOf(val, Number.class)) {
						update.inc(fieldName, Number.class.cast(val));

					} else {
						update.inc(fieldName);
					}

					// $max
				} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_MAX)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_MAX,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;
					update.max(fieldName, val);

					// $min
				} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_MIN)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_MIN,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;
					update.min(fieldName, val);

					// $mul (multiply)
				} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_MULTIPLY)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_MULTIPLY,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;
					if (BeanUtils.isInstanceOf(val, Number.class)) {
						update.multiply(fieldName, Number.class.cast(val));

					} else {
						throw new IllegalArgumentException("Multiply value must be a number!");
					}

					// $pop
				} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_POP)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_POP,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;
					if (BeanUtils.isInstanceOf(val, Position.class)) {
						update.pop(fieldName, Position.class.cast(val));

					} else {
						Position pos = Position.valueOf(String.valueOf(val));
						if (pos == null) {
							throw new IllegalArgumentException(
									"Pop position value must be a {" + Position.class.getCanonicalName() + "} type!");
						}
						update.pop(fieldName, pos);
					}

					// $rename
				} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_RENAME)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_RENAME,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;
					update.rename(fieldName, String.valueOf(val));

					// $setOnInsert
				} else if (org.apache.commons.lang3.StringUtils.containsIgnoreCase(fieldName, MongoKeywords.MONGODB_SETONINSERT)) {
					fieldName = StringUtils.trimWhitespace(
							StringUtils.replace(fieldName, MongoKeywords.MONGODB_SETONINSERT,
									org.apache.commons.lang3.StringUtils.EMPTY));
					if (update.modifies(fieldName)) continue;
					update.setOnInsert(fieldName, val);

				} else if (!update.modifies(fieldName)) {
					update.set(fieldName, val);
				}
			}
		}
		return update;
	}
}
