/*
 * @(#)ExpressionHelper.java Aug 18, 2019
 * Copyright 2019 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.helpers;

import java.io.Serializable;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.bson.Document;
import org.nlh4j.mongo.helpers.MongoConstants.MongoFields;
import org.nlh4j.mongo.helpers.MongoConstants.MongoKeywords;
import org.nlh4j.util.CollectionUtils;
import org.springframework.util.Assert;

/**
 * Query/Expression Builder
 * @author Hai Nguyen
 */
public final class ExpressionBuilder implements Serializable {

	/** */
	private static final long serialVersionUID = 1L;
	private static final String REGEX_PATTERN = ".*%s.*";
	private static final String REGEX_START_PATTERN = "^%s.*";
	private static final String REGEX_END_PATTERN = ".*%s$";

	/**
	 * Initialize a new instance of {@link ExpressionBuilder} class
	 */
	private ExpressionBuilder() {}

	/**
	 * Create $regex expression filter as {@link Document} with the specified pattern<br>
	 * Ex: <code>{ exprKey: { $regex: /.*exprValue.*\/i } }</code>
	 * @param field field name
	 * @param regexPattern regular expression pattern
	 * @return the expression filter as {@link Document}
	 */
	public static Document createRegexPatternExpression(String field, String regexPattern) {
		Document expression = new Document();
		expression.put(field, Pattern.compile(regexPattern,
				Pattern.DOTALL | Pattern.LITERAL | Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE));
		return expression;
	}
	/**
	 * Create $regex expression filter as {@link Document}<br>
	 * Ex: <code>{ exprKey: { $regex: /.*exprValue.*\/i } }</code>
	 * @param field field name
	 * @param exprValue expression value
	 * @return the expression filter as {@link Document}
	 */
	public static Document createRegexExpression(String field, Object exprValue) {
		return createRegexPatternExpression(
				field,
				String.format(REGEX_PATTERN,
						Pattern.quote(exprValue != null ? String.valueOf(exprValue)
								: org.apache.commons.lang3.StringUtils.EMPTY)));
	}
	/**
	 * Create $regex expression filter as {@link Document}<br>
	 * Ex: <code>{ exprKey: { $regex: /.*exprValue.*\/i } }</code>
	 * @param field field name
	 * @param exprValue expression value
	 * @return the expression filter as {@link Document}
	 */
	public static Document createStartsWithExpression(String field, Object exprValue) {
		return createRegexPatternExpression(
				field,
				String.format(REGEX_START_PATTERN,
						Pattern.quote(exprValue != null ? String.valueOf(exprValue)
								: org.apache.commons.lang3.StringUtils.EMPTY)));
	}
	/**
	 * Create $regex expression filter as {@link Document}<br>
	 * Ex: <code>{ exprKey: { $regex: /.*exprValue.*\/i } }</code>
	 * @param field field name
	 * @param exprValue expression value
	 * @return the expression filter as {@link Document}
	 */
	public static Document createEndsWithExpression(String field, Object exprValue) {
		return createRegexPatternExpression(
				field,
				String.format(REGEX_END_PATTERN,
						Pattern.quote(exprValue != null ? String.valueOf(exprValue)
								: org.apache.commons.lang3.StringUtils.EMPTY)));
	}

	/**
	 * Create expression filter as {@link Document}<br>
	 * Ex: <code>{ exprKey: exprValue }</code>
	 * @param exprKey expression key like field name or operation
	 * @param exprValue expression value
	 * @return the expression filter as {@link Document}
	 */
	public static Document createExpression(String exprKey, Object exprValue) {
		Document expression = new Document();
		expression.put(exprKey, exprValue);
		return expression;
	}
	/**
	 * Create expression filter as {@link Document}<br>
	 * Ex: <code>{ $or: [ expression1, expression2, ...] }</code>
	 * @param andOp specify AND/OR operator
	 * @param exprValues expression values
	 * @return the expression filter as {@link Document}
	 */
	public static Document createExpressions(boolean andOp, Object...exprValues) {
		return (andOp ? createExpression(MongoKeywords.MONGODB_AND, CollectionUtils.toList(exprValues))
				: createExpression(MongoKeywords.MONGODB_OR, CollectionUtils.toList(exprValues)));
	}
	/**
	 * Create expression filter as {@link Document} with pair expression key and value<br>
	 * Ex: <code>{ $or: [ expression1, expression2, ...] }</code>
	 * @param exprKeyValuePairs key1, value1, key2, value2, ...
	 * @return the expression filter as {@link Document} with pair expression key and value<br>
	 */
	public static Document createPairExpressions(Object...exprKeyValuePairs) {
		Assert.isTrue(ArrayUtils.isNotEmpty(exprKeyValuePairs) && (exprKeyValuePairs.length % 2 == 0),
				"Expressions must are provied as pairs of key and value!");
		Document expression = new Document();
		for(int i = 0; i < exprKeyValuePairs.length; i += 2) {
			expression.put(String.valueOf(exprKeyValuePairs[i]), exprKeyValuePairs[i + 1]);
		}
		return expression;
	}

	/**
	 * Create deleted expression.<br>
	 * Ex: <code>{ $or: [{ deletedDate: { $exists: false } }, { deletedDate: { $eq: null } }] }</code>
	 * @param deleted specify comparing deleted or non-deleted. true for deleted
	 * @return the deleted expression
	 */
	public static Document createDeletedFilter(boolean deleted) {
		return createExistedFilter(MongoFields.MONGODB_DELETED_DATE,
				(deleted ? Boolean.TRUE : Boolean.FALSE));
	}
	/**
	 * Create deleted date expression.<br>
	 * Ex: <code>{ $and: [{ deletedDate: { $exists: true } }, { deletedDate: { $ne: null } }] }</code>
	 * @return the deleted date expression
	 */
	public static Document createDeletedDateFilter() {
		return createDeletedFilter(Boolean.TRUE);
	}
	/**
	 * Create non-deleted date expression.<br>
	 * Ex: <code>{ $or: [{ deletedDate: { $exists: false } }, { deletedDate: { $eq: null } }] }</code>
	 * @return the non-deleted date expression
	 */
	public static Document createNonDeletedDateFilter() {
		return createDeletedFilter(Boolean.FALSE);
	}
	/**
	 * Create existed expression.<br>
	 * Ex: <code>{ $and: [{ fieldName: { $exists: true } }, { fieldName: { $ne: null } }] }</code>
	 * @param existed specify comparing existed (existed and not equals NULL) or non-existed. true for existed
	 * @param fieldName to check
	 * @return the existed expression
	 */
	public static Document createExistedFilter(String fieldName, boolean existed) {
		// { $exists: false }
		Document existsExprValue = createExpression(MongoKeywords.MONGODB_EXISTS, existed);
		// { field: { $exists: false } }
		Document existsExpr = createExpression(fieldName, existsExprValue);

		// { $eq: null }
		Document nullEqExprValue = createExpression(
				existed ? MongoKeywords.MONGODB_NE : MongoKeywords.MONGODB_EQ, null);
		// { field: { $eq: null } }
		Document nullEqExpr = createExpression(fieldName, nullEqExprValue);

		// { $and/$or: [{ field: { $exists: false } }, { field: { $eq: null } }] }
		return createExpressions((existed ? Boolean.TRUE : Boolean.FALSE), existsExpr, nullEqExpr);
	}
}
