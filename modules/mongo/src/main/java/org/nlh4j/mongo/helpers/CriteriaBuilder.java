/*
 * @(#)CriteriaBuilder.java Aug 22, 2019
 * Copyright 2019 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.helpers;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.MapUtils;
import org.bson.types.ObjectId;
import org.nlh4j.mongo.helpers.MongoConstants.MongoFields;
import org.nlh4j.mongo.helpers.MongoConstants.MongoKeywords;
import org.nlh4j.util.CollectionUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * {@link Criteria} Builder
 * @author Hai Nguyen
 */
public final class CriteriaBuilder implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;
	private static final String EMPTY = org.apache.commons.lang3.StringUtils.EMPTY;

	/**
	 * Initialize a new instance of {@link CriteriaBuilder} class
	 */
	private CriteriaBuilder() {}

	/**
	 * Create {@link Criteria} condition
	 *
	 * @param field field name condition
	 * @param value field value to filter
	 *
	 * @return {@link Criteria}
	 */
	public static Criteria createCriteria(String field, Object value) {
		Criteria condition = null;
		field = (StringUtils.hasText(field) ? field.trim() : field);
		Assert.hasText(field, "field");
		if (field.startsWith(MongoKeywords.MONGODB_IN)
				&& CollectionUtils.isCollection(value)) {
			condition = createCriteriaIn(field, value);

		} else if (field.startsWith(MongoKeywords.MONGODB_NIN_EXP)
				&& CollectionUtils.isCollection(value)) {
			condition = createCriteriaNinExp(field, value);

		} else if (field.startsWith(MongoKeywords.MONGODB_REGEX)) {
			condition = Criteria.where(field.replace(MongoKeywords.MONGODB_REGEX, EMPTY))
					.regex(".*" + value.toString().trim() + ".*", "i");

		} else if (field.startsWith(MongoKeywords.MONGODB_REGEX_START)) {
			condition = Criteria.where(field.replace(MongoKeywords.MONGODB_REGEX_START, EMPTY))
					.regex(MongoKeywords.MONGODB_REGEX_START_EXP + value.toString().trim(), "i");

		} else if (field.startsWith(MongoKeywords.MONGODB_REGEX_END)) {
			condition = Criteria.where(field.replace(MongoKeywords.MONGODB_REGEX_END, EMPTY))
					.regex(value.toString().trim() + MongoKeywords.MONGODB_REGEX_END_EXP, "i");

		} else if (field.startsWith(MongoKeywords.MONGODB_GT)) {
			condition = Criteria.where(field.replace(MongoKeywords.MONGODB_GT, EMPTY)).gt(value);

		} else if (field.startsWith(MongoKeywords.MONGODB_LT)) {
			condition = Criteria.where(field.replace(MongoKeywords.MONGODB_LT, EMPTY)).lt(value);

		} else if (field.startsWith(MongoKeywords.MONGODB_GTE_EXP)) {
			condition = Criteria.where(field.replace(MongoKeywords.MONGODB_GTE_EXP, EMPTY)).gte(value);

		}  else if (field.startsWith(MongoKeywords.MONGODB_LTE_EXP)) {
			condition = Criteria.where(field.replace(MongoKeywords.MONGODB_LTE_EXP, EMPTY)).lte(value);

		}  else if (field.startsWith(MongoKeywords.MONGODB_EXISTS)) {
			// not equals NULL and exists TRUE
			boolean exists = Boolean.TRUE.equals(value);
			if (exists) {
				condition = (new Criteria()).andOperator(
						Criteria.where(field.replace(MongoKeywords.MONGODB_EXISTS, EMPTY)).ne(null),
						Criteria.where(field.replace(MongoKeywords.MONGODB_EXISTS, EMPTY)).exists(Boolean.TRUE));

			} else {
				condition = (new Criteria()).orOperator(
						Criteria.where(field.replace(MongoKeywords.MONGODB_EXISTS, EMPTY)).is(null),
						Criteria.where(field.replace(MongoKeywords.MONGODB_EXISTS, EMPTY)).exists(Boolean.FALSE));
			}

		} else if (field.startsWith(MongoKeywords.MONGODB_NE)) {
			condition = createCriteriaNe(field, value);

		} else if (field.startsWith(MongoKeywords.MONGODB_OR)) {
			condition = createCriteriaOr(field, value, condition);

		} else if (field.startsWith(MongoKeywords.MONGODB_AND)) {
			condition = createCriteriaAnd(field, value);

		} else if (field.startsWith(MongoKeywords.MONGODB_ELEM_MATCH)) {
			condition = createCriteriaElemMatch(field, value);

		} else if (field.startsWith(MongoKeywords.MONGODB_ALL)) {
			condition = createCriteriaEqualsAll(field, value);

		} else if (value != null) {
			condition = createCriteriaDefault(field, value);

		} else { // value is NULL, same case with $exists false
			condition = createCriteria(MongoKeywords.MONGODB_EXISTS + field, Boolean.FALSE);
		}
		return condition;
	}

	/**
	 * Create {@link Criteria} for default case
	 * @param field to create
	 * @param value to create
	 * @return {@link Criteria}
	 */
	private static Criteria createCriteriaDefault(String field, Object value) {
		Criteria condition;
		if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(field, MongoFields.MONGODB_ID)
				|| org.apache.commons.lang3.StringUtils.equalsIgnoreCase(field, MongoFields.MONGODB_OBJECT_ID)
				|| org.apache.commons.lang3.StringUtils.equalsIgnoreCase(field, MongoFields.MONGODB_ID_VARIABLE)) {
			ObjectId id = null;
			if (ObjectId.class.isAssignableFrom(value.getClass())) {
				id = ObjectId.class.cast(value);
			} else {
				id = (ObjectId.isValid(String.valueOf(value))
						? new ObjectId(String.valueOf(value)) : new ObjectId());
			}
			condition = Criteria.where(MongoFields.MONGODB_OBJECT_ID).is(id);
		} else {
			condition = Criteria.where(field).is(value);
		}
		return condition;
	}

	/**
	 * Create {@link Criteria} for $all
	 * @param field to create
	 * @param value to create
	 * @return {@link Criteria}
	 */
	private static Criteria createCriteriaEqualsAll(String field, Object value) {
		List<Object> values = new LinkedList<>();
		boolean eqOneOfAll = field.contains(MongoKeywords.MONGODB_OR);
		boolean eqAll = (!eqOneOfAll && field.contains(MongoKeywords.MONGODB_AND));
		String fieldName = field.replace(MongoKeywords.MONGODB_ALL, EMPTY)
				.replace(MongoKeywords.MONGODB_OR, EMPTY)
				.replace(MongoKeywords.MONGODB_AND, EMPTY);
		final String checkedFieldName = (!StringUtils.hasText(fieldName) ? field : fieldName);
		if (CollectionUtils.isCollection(value)) {
			values = CollectionUtils.toList(value, Object.class);

		} else if (CollectionUtils.isArray(value)) {
			values.addAll(CollectionUtils.toList(value, Object.class));

		} else {
			values.add(value);
		}
		if (eqOneOfAll) {
			return Criteria.where(checkedFieldName).in(values);

		} else if (eqAll) {
			final List<Criteria> valueEqCriterias = new LinkedList<>();
			values.stream().forEach(val -> valueEqCriterias.add(createCriteria(checkedFieldName, val)));
			if (CollectionUtils.isElementsNumberGreaterThan(valueEqCriterias, 1, Boolean.FALSE)) {
				return new Criteria().andOperator(valueEqCriterias.toArray(new Criteria[valueEqCriterias.size()]));
			} else {
				return valueEqCriterias.get(0);
			}

		} else if (CollectionUtils.isElementsNumberGreaterThan(values, 1, Boolean.FALSE)) {
			return Criteria.where(checkedFieldName).in(values);

		} else {
			return Criteria.where(checkedFieldName).is(values);
		}
	}

	/**
	 * Create {@link Criteria} for $elemMatch
	 * @param field to create
	 * @param value to create
	 * @return {@link Criteria}
	 */
	private static Criteria createCriteriaElemMatch(String field, Object value) {
		Criteria condition;
		String fieldName = field.replace(MongoKeywords.MONGODB_ELEM_MATCH, EMPTY);
		if (CollectionUtils.isMap(value)) {
			Map<String, Object> filtersMap = CollectionUtils.safeMap(value, String.class, Object.class);
			if (!CollectionUtils.isEmpty(filtersMap)) {
				List<Criteria> tmpConditions = new LinkedList<>();
				for(final Iterator<String> it = filtersMap.keySet().iterator(); it.hasNext();) {
					String keyFilter = it.next();
					if (!StringUtils.hasText(keyFilter)) continue;
					tmpConditions.add(createCriteria(keyFilter, filtersMap.get(keyFilter)));
				}
				if (tmpConditions.size() >= 2) {
					condition = (new Criteria()).elemMatch(
							(new Criteria()).andOperator(CollectionUtils.toArray(tmpConditions, Criteria.class)));
				} else {
					condition = (new Criteria()).elemMatch(tmpConditions.get(0));
				}

			} else {
				condition = createCriteria(fieldName, value);
			}

		} else if (CollectionUtils.isCollection(value) || CollectionUtils.isArray(value)) {
			List<Object> values = new LinkedList<>(CollectionUtils.toList(value, Object.class));
			List<Criteria> tmpConditions = new LinkedList<>();
			for(Object val : values) {
				tmpConditions.add(createCriteria(fieldName, val));
			}
			if (tmpConditions.size() >= 2) {
				condition = (new Criteria()).elemMatch(
						(new Criteria()).andOperator(CollectionUtils.toArray(tmpConditions, Criteria.class)));
			} else {
				condition = (new Criteria()).elemMatch(tmpConditions.get(0));
			}

		} else {
			condition = createCriteria(fieldName, value);
		}
		return condition;
	}

	/**
	 * Create {@link Criteria} for $and
	 * @param field to create
	 * @param value to create
	 * @return {@link Criteria}
	 */
	private static Criteria createCriteriaAnd(String field, Object value) {
		Criteria condition;
		String fieldName = field.replace(MongoKeywords.MONGODB_AND, EMPTY);
		if (CollectionUtils.isMap(value)) {
			Map<String, Object> filtersMap = CollectionUtils.safeMap(value, String.class, Object.class);
			if (!CollectionUtils.isEmpty(filtersMap)) {
				List<Criteria> tmpConditions = new LinkedList<>();
				for(final Iterator<String> it = filtersMap.keySet().iterator(); it.hasNext();) {
					String keyFilter = it.next();
					if (!StringUtils.hasText(keyFilter)) continue;
					tmpConditions.add(createCriteria(keyFilter, filtersMap.get(keyFilter)));
				}
				condition = tmpConditions.get(0);
				if (tmpConditions.size() >= 2) {
					condition = (new Criteria()).andOperator(
							CollectionUtils.toArray(tmpConditions, Criteria.class));
				}

			} else {
				condition = createCriteria(fieldName, value);
			}

		} else if (CollectionUtils.isCollection(value) || CollectionUtils.isArray(value)) {
			List<Object> values = new LinkedList<>(CollectionUtils.toList(value, Object.class));
			List<Criteria> tmpConditions = new LinkedList<>();
			for(Object val : values) {
				tmpConditions.add(createCriteria(fieldName, val));
			}
			condition = tmpConditions.get(0);
			if (tmpConditions.size() >= 2) {
				condition = (new Criteria()).andOperator(
						CollectionUtils.toArray(tmpConditions, Criteria.class));
			}

		} else {
			condition = createCriteria(fieldName, value);
		}
		return condition;
	}

	/**
	 * Create {@link Criteria} for $or
	 * @param field to create
	 * @param value to create
	 * @param condition to recursive
	 * @return {@link Criteria}
	 */
	private static Criteria createCriteriaOr(String field, Object value, Criteria condition) {
		String fieldName = field.replace(MongoKeywords.MONGODB_OR, EMPTY);
		if (CollectionUtils.isMap(value)) {
			Map<String, Object> filtersMap = CollectionUtils.safeMap(value, String.class, Object.class);
			if (!CollectionUtils.isEmpty(filtersMap)) {
				List<Criteria> tmpConditions = new LinkedList<>();
				for(final Iterator<String> it = filtersMap.keySet().iterator(); it.hasNext();) {
					String keyFilter = it.next();
					if (!StringUtils.hasText(keyFilter)) continue;
					tmpConditions.add(createCriteria(keyFilter, filtersMap.get(keyFilter)));
				}
				condition = tmpConditions.get(0);
				if (tmpConditions.size() >= 2) {
					condition = (new Criteria()).orOperator(
							CollectionUtils.toArray(tmpConditions, Criteria.class));
				}

			} else if (StringUtils.hasText(fieldName)) {
				condition = createCriteria(fieldName, value);
			}

		} else if ((CollectionUtils.isCollection(value) || CollectionUtils.isArray(value)) && StringUtils.hasText(fieldName)) {
			List<Object> values = new LinkedList<>(CollectionUtils.toList(value, Object.class));
			condition = Criteria.where(fieldName).in(values);

		} else if (StringUtils.hasText(fieldName)) {
			condition = createCriteria(fieldName, value);
		} else {
			throw new IllegalArgumentException("Invalid condition {" + field + "}!");
		}
		return condition;
	}

	/**
	 * Create {@link Criteria} for $ne
	 * @param field to create
	 * @param value to create
	 * @return {@link Criteria}
	 */
	private static Criteria createCriteriaNe(String field, Object value) {
		Criteria condition;
		if ((org.apache.commons.lang3.StringUtils.equalsIgnoreCase(field, MongoFields.MONGODB_ID)
				|| org.apache.commons.lang3.StringUtils.equalsIgnoreCase(field, MongoFields.MONGODB_OBJECT_ID)
				|| org.apache.commons.lang3.StringUtils.equalsIgnoreCase(field, MongoFields.MONGODB_ID_VARIABLE))
			&& (value == null || ObjectId.class.isAssignableFrom(value.getClass()))) {
			condition = Criteria.where(field.replace(MongoKeywords.MONGODB_NE, EMPTY))
					.ne(ObjectId.class.cast(value));

		} else if ((org.apache.commons.lang3.StringUtils.equalsIgnoreCase(field, MongoFields.MONGODB_ID)
				|| org.apache.commons.lang3.StringUtils.equalsIgnoreCase(field, MongoFields.MONGODB_OBJECT_ID)
				|| org.apache.commons.lang3.StringUtils.equalsIgnoreCase(field, MongoFields.MONGODB_ID_VARIABLE)) && value != null) {
			condition = Criteria.where(field.replace(MongoKeywords.MONGODB_NE, EMPTY))
					.ne(ObjectId.isValid(value.toString().trim())
							? new ObjectId(value.toString().trim()) : new ObjectId());

		} else {
			condition = Criteria.where(field.replace(MongoKeywords.MONGODB_NE, EMPTY)).ne(value);
		}
		return condition;
	}

	/**
	 * Create {@link Criteria} for $expnot
	 * @param field to create
	 * @param value to create
	 * @return {@link Criteria}
	 */
	private static Criteria createCriteriaNinExp(String field, Object value) {
		List<?> valuesList = CollectionUtils.toList(value, Object.class);
		List<Object> filteredList = new LinkedList<>();
		//check in list id
		if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(field, MongoFields.MONGODB_ID)
				|| org.apache.commons.lang3.StringUtils.equalsIgnoreCase(field, MongoFields.MONGODB_OBJECT_ID)
				|| org.apache.commons.lang3.StringUtils.equalsIgnoreCase(field, MongoFields.MONGODB_ID_VARIABLE)) {
			for(Object val : valuesList) {
				filteredList.add(val == null || val instanceof ObjectId
						? val : ObjectId.isValid(val.toString().trim())
								? new ObjectId(val.toString().trim()) : new ObjectId());
			}
		}
		return Criteria.where(field.replace(MongoKeywords.MONGODB_NIN_EXP, EMPTY)).nin(valuesList);
	}

	/**
	 * Create {@link Criteria} for $in
	 * @param field to create
	 * @param value to create
	 * @return {@link Criteria}
	 */
	private static Criteria createCriteriaIn(String field, Object value) {
		List<?> valuesList = CollectionUtils.toList(value, Object.class);
		List<Object> filteredList = new LinkedList<>();
		//check in list id
		if (org.apache.commons.lang3.StringUtils.equalsIgnoreCase(field, MongoFields.MONGODB_ID)
				|| org.apache.commons.lang3.StringUtils.equalsIgnoreCase(field, MongoFields.MONGODB_OBJECT_ID)
				|| org.apache.commons.lang3.StringUtils.equalsIgnoreCase(field, MongoFields.MONGODB_ID_VARIABLE)) {
			for(Object val : valuesList) {
				filteredList.add(val == null || val instanceof ObjectId
						? val : ObjectId.isValid(val.toString().trim())
								? new ObjectId(val.toString().trim()) : new ObjectId());
			}
		}
		return Criteria.where(field.replace(MongoKeywords.MONGODB_IN, EMPTY)).in(valuesList);
	}

	/**
	 * Parse and add condition into the specified {@link Query}
	 *
	 * @param query to add condition
	 * @param field field name condition
	 * @param value field value to filter
	 */
	public static void addQueryCriteria(Query query, String field, Object value) {
		Assert.notNull(query, "query");
		query.addCriteria(createCriteria(field, value));
	}
	/**
	 * Create {@link Criteria} condition
	 *
	 * @param filters filter conditions
	 * @param includedDeleted specify including deleted records
	 * @param andOp AND operation; else for OR
	 *
	 * @return {@link Criteria} condition
	 */
	public static Criteria createCriterias(Map<String, Object> filters, boolean includedDeleted, boolean andOp) {
		Criteria condition = null;
		List<Criteria> conditions = new LinkedList<>();
		if (!includedDeleted) {
			conditions.add(createCriteria(MongoFields.MONGODB_DELETED_DATE, null));
		}
		if (MapUtils.isNotEmpty(filters)) {
			for(final Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
				String field = it.next();
				if (!StringUtils.hasText(field)) continue;
				Object val = filters.get(field);
				conditions.add(createCriteria(field, val));
			}
		}
		if (!CollectionUtils.isEmpty(conditions)) {
			if (conditions.size() >= 2 && andOp) {
				condition = (new Criteria()).andOperator(
						CollectionUtils.toArray(conditions, Criteria.class));
			} else if (conditions.size() >= 2 && !andOp) {
				condition = (new Criteria()).orOperator(
						CollectionUtils.toArray(conditions, Criteria.class));
			} else {
				condition = conditions.get(0);
			}
		}
		return condition;
	}
	/**
	 * Create {@link Criteria} condition (AND OPERATION)
	 *
	 * @param filters filter conditions
	 * @param includedDeleted specify including deleted records
	 *
	 * @return {@link Criteria} condition
	 */
	public static Criteria createCriterias(Map<String, Object> filters, boolean includedDeleted) {
		return createCriterias(filters, includedDeleted, Boolean.TRUE);
	}
	/**
	 * Create {@link Criteria} condition (AND OPERATION, NOT INCLUDE DELETED RECORDS)
	 *
	 * @param filters filter conditions
	 *
	 * @return {@link Criteria} condition
	 */
	public static Criteria createCriterias(Map<String, Object> filters) {
		return createCriterias(filters, Boolean.FALSE, Boolean.TRUE);
	}

	/**
	 * Parse and add conditions into the specified {@link Query}
	 *
	 * @param query to add condition
	 * @param filters filter conditions
	 * @param includedDeleted specify including deleted records
	 * @param andOp AND operation; else for OR
	 */
	public static void addQueryCriterias(Query query, Map<String, Object> filters, boolean includedDeleted, boolean andOp) {
		Assert.notNull(query, "query");
		Criteria conditions = createCriterias(filters, includedDeleted, andOp);
		if (Objects.nonNull(conditions)) {
			query.addCriteria(conditions);
		}
	}
	/**
	 * Parse and add conditions into the specified {@link Query} (AND OPERATION)
	 *
	 * @param query to add condition
	 * @param filters filter conditions
	 * @param includedDeleted specify including deleted records
	 */
	public static void addQueryCriterias(Query query, Map<String, Object> filters, boolean includedDeleted) {
		Assert.notNull(query, "query");
		Criteria conditions = createCriterias(filters, includedDeleted, Boolean.TRUE);
		if (Objects.nonNull(conditions)) {
			query.addCriteria(conditions);
		}
	}
	/**
	 * Parse and add conditions into the specified {@link Query} (exclude deleted records)
	 *
	 * @param query to add condition
	 * @param filters filter conditions
	 */
	public static void addQueryCriterias(Query query, Map<String, Object> filters) {
		addQueryCriterias(query, filters, Boolean.FALSE);
	}

	/**
	 * Parse and add conditions into the returned {@link Query}
	 *
	 * @param filters filter conditions
	 * @param includedDeleted specify including deleted records
	 * @param andOp AND operation; else for OR
	 *
	 * @return {@link Query}
	 */
	public static Query createQueryCriterias(Map<String, Object> filters, boolean includedDeleted, boolean andOp) {
		Query query = new Query();
		Criteria conditions = createCriterias(filters, includedDeleted, andOp);
		if (Objects.nonNull(conditions)) {
			query.addCriteria(conditions);
		}
		return query;
	}
	/**
	 * Parse and add conditions into the returned {@link Query} (AND OPERATION)
	 *
	 * @param filters filter conditions
	 * @param includedDeleted specify including deleted records
	 *
	 * @return {@link Query}
	 */
	public static Query createQueryCriterias(Map<String, Object> filters, boolean includedDeleted) {
		Query query = new Query();
		Criteria conditions = createCriterias(filters, includedDeleted, Boolean.TRUE);
		if (Objects.nonNull(conditions)) {
			query.addCriteria(conditions);
		}
		return query;
	}
	/**
	 * Parse and add conditions into the returned {@link Query} (exclude deleted records)
	 *
	 * @param filters filter conditions
	 *
	 * @return {@link Query}
	 */
	public static Query createQueryCriterias(Map<String, Object> filters) {
		return createQueryCriterias(filters, Boolean.FALSE);
	}
}
