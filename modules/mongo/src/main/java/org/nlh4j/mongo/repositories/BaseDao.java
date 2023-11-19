/*
 * @(#)BaseDao.java Aug 11, 2019
 * Copyright 2019 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.nlh4j.mongo.dto.BaseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

/**
 * The basic repository interface
 * @author Hai Nguyen
 *
 * @param <E> entity type
 */
@SuppressWarnings("unchecked")
public interface BaseDao<E extends BaseEntity> extends BaseTemplateDao {

	/**
	 * Get the current repository entity class
	 * @return the current repository entity class
	 */
	Class<E> getEntityClass();
	/**
	 * Get the current repository collection name
	 * @return the current repository collection name
	 */
	String getCollectionName();
	/**
	 * Set the current {@link MongoTemplate} instance
	 *
	 * @param mongoTemplate to store
	 */
	void setMongoTemplate(MongoTemplate mongoTemplate);

	/**
	 * Get the current database name
	 *
	 * @return the current database name
	 */
	String getDatabaseName();
	/**
	 * Set the current database name
	 *
	 * @param databaseName to store
	 */
	void setDatabaseName(String databaseName);

	/**
	 * Insert the specified entities collection
	 * @param <T> extended entity type if necessary
	 * @param entities to insert
	 * @return the inserted entities
	 */
	default <T extends E> Collection<T> insertBatch(Collection<T> entities) {
		return insertBatch(getEntityClass(), entities);
	}
	/**
	 * Save/Update the specified entity
	 * @param <T> extended entity type if necessary
	 * @param entity to save/update
	 * @return saved/updated entity
	 */
	default <T extends E> T saveOrUpdate(T entity) {
		return saveOrUpdate(getEntityClass(), entity);
	}
	/**
	 * Save/Update the specified entities iterator
	 * @param <T> extended entity type if necessary
	 * @param entities to save/update
	 * @return saved/updated entities list
	 */
	default <T extends E> List<T> saveOrUpdate(Iterable<T> entities) {
		return saveOrUpdate(getEntityClass(), entities);
	}

	/**
	 * Find entity by the specified entity identity
	 * @param id to filter
	 * @return entity or NULL
	 */
	default E findById(String id) {
		return findById(getEntityClass(), id);
	}
	/**
	 * Find the first occurred entity by the specified conditions
	 * @param field to filter
	 * @param value to compare
	 * @return the first occurred entity or NULL
	 */
	default E findOne(String field, Object value) {
		return findOne(getEntityClass(), field, value);
	}
	/**
	 * Find the first occurred entity by the specified conditions
	 * @param filters to filter
	 * @return the first occurred entity or NULL
	 */
	default E findOne(Map<String, Object> filters) {
		return findOne(getEntityClass(), filters);
	}
	/**
	 * Find the entities list by the specified conditions, sort and limitation
	 * @param filters to filter
	 * @param sorter to sort. NUll for ignoring sorting
	 * @param offset the included begin records index
	 * @param limit the limitted number of records. &lt;= 0 for all
	 * @return the entities list or empty
	 */
	default List<E> find(Map<String, Object> filters, Sort sorter, long offset, int limit) {
		return find(getEntityClass(), filters, sorter, offset, limit);
	}

	/**
	 * Check the specified values whether exist in collection of the specified entity class
	 * @param all true for existing all; else just need at least one
	 * @param field to detect
	 * @param values to check
	 * @return true for existed; else false
	 */
	default boolean exists(boolean all, String field, Object...values) {
		return exists(getEntityClass(), all, field, values);
	}
	/**
	 * Check the specified {@link ObjectId} whether exist in collection of the specified entity class
	 * @param all true for existing all; else just need at least one
	 * @param ids to check
	 * @return true for existed; else false
	 */
	default boolean exists(boolean all, ObjectId...ids) {
		return exists(getEntityClass(), all, ids);
	}

	/**
	 * Aggregate the entities list by the specified conditions and operations
	 * @param <O> {@link AggregationOperation}
	 * @param matchConditions to filter
	 * @param operations aggregation operations
	 * @return the entities list
	 */
	default <O extends AggregationOperation> List<E> aggregateLookup(Map<String, Object> matchConditions, O...operations) {
		return aggregateLookup(getEntityClass(), matchConditions, operations);
	}
	/**
	 * Aggregate the entities list by the specified conditions and operations
	 * @param <O> {@link AggregationOperation}
	 * @param matchConditions to filter
	 * @param operations aggregation operations
	 * @return {@link AggregationResults}
	 */
	default <O extends AggregationOperation> AggregationResults<E> aggregateLookupResults(
			Map<String, Object> matchConditions, O...operations) {
		return aggregateLookupResults(getEntityClass(), matchConditions, operations);
	}
	/**
	 * Aggregate the entities list by the specified conditions and operations
	 * @param <O> {@link AggregationOperation}
	 * @param operations aggregation operations
	 * @return the entities list
	 */
	default <O extends AggregationOperation> List<E> aggregateLookup(O...operations) {
		return aggregateLookup(getEntityClass(), operations);
	}
	/**
	 * Aggregate the entities list by the specified conditions and operations
	 * @param <O> {@link AggregationOperation}
	 * @param operations aggregation operations
	 * @return {@link AggregationResults}
	 */
	default <O extends AggregationOperation> AggregationResults<E> aggregateLookupResults(O...operations) {
		return aggregateLookupResults(getEntityClass(), operations);
	}

	/**
	 * Find and update the matched entity by the specified conditions and updaters
	 * @param filters to filter
	 * @param updaters to update
	 * @return the previous entity before updating or NULL
	 */
	default E findAndModify(Map<String, Object> filters, Map<String, Object> updaters) {
		return findAndModify(getEntityClass(), filters, updaters);
	}
	/**
	 * Update the first matched entity by the specified conditions and updaters
	 * @param filters to filter
	 * @param updaters to update
	 * @return {@link UpdateResult}
	 */
	default UpdateResult updateFirst(Map<String, Object> filters, Map<String, Object> updaters) {
		return updateFirst(getEntityClass(), filters, updaters);
	}
	/**
	 * Update all matched entities by the specified conditions and updaters
	 * @param filters to filter
	 * @param updaters to update
	 * @return {@link UpdateResult}
	 */
	default UpdateResult updateMulti(Map<String, Object> filters, Map<String, Object> updaters) {
		return updateMulti(getEntityClass(), filters, updaters);
	}

	/**
	 * Push the specified values into the specified field of all document(s) that matched with condition filters
	 * @param filters to filter
	 * @param pushedField to push
	 * @param pushedValues pushed values
	 * @return {@link UpdateResult}
	 */
	default UpdateResult push(Map<String, Object> filters, String pushedField, Object...pushedValues) {
		return push(getEntityClass(), filters, pushedField, pushedValues);
	}
	/**
	 * Pull the specified values out of the specified field of all document(s) that matched with condition filters
	 * @param filters to filter
	 * @param pulledField to pull
	 * @param pulledValues pulled values
	 * @return {@link UpdateResult}
	 */
	default UpdateResult pull(Map<String, Object> filters, String pulledField, Object...pulledValues) {
		return pull(getEntityClass(), filters, pulledField, pulledValues);
	}

	/**
	 * Delete all document(s) that matched with the specified identitites
	 * @param needToDeleteIds to delete
	 * @return {@link DeleteResult}
	 */
	default DeleteResult deleteById(ObjectId...needToDeleteIds) {
		return deleteById(getEntityClass(), needToDeleteIds);
	}
	/**
	 * Delete all document(s) that matched with the specified conditions
	 * @param filters to filter
	 * @return {@link DeleteResult}
	 */
	default DeleteResult delete(Map<String, Object> filters) {
		return delete(getEntityClass(), filters);
	}
	/**
	 * Delete the specified entity
	 * @param entity to delete
	 * @return {@link DeleteResult}
	 */
	default DeleteResult delete(E entity) {
		return delete(getEntityClass(), entity);
	}
}
