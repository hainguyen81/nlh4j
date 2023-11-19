/*
 * @(#)BaseTemplateDao.java Aug 11, 2019
 * Copyright 2019 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.repositories;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

/**
 * The basic template repository interface
 * @author Hai Nguyen
 */
@SuppressWarnings("unchecked")
public interface BaseTemplateDao extends Serializable {

	/**
	 * Get the current {@link MongoTemplate} instance
	 *
	 * @return the current {@link MongoTemplate} instance
	 */
	MongoTemplate getMongoTemplate();

	/**
	 * Insert the specified entities collection
	 * @param <E> entity type
	 * @param <T> extended entity type if necessary
	 * @param entities to insert
	 * @param entityClass to detect collection
	 * @return the inserted entities
	 */
	<E, T extends E> Collection<T> insertBatch(Class<E> entityClass, Collection<T> entities);
	/**
	 * Save/Update the specified entity
	 * @param <E> entity type
	 * @param <T> extended entity type if necessary
	 * @param entity to save/update
	 * @param entityClass to detect collection
	 * @return saved/updated entity
	 */
	<E, T extends E> T saveOrUpdate(Class<E> entityClass, T entity);
	/**
	 * Save/Update the specified entities iterator
	 * @param <E> entity type
	 * @param <T> extended entity type if necessary
	 * @param entities to save/update
	 * @param entityClass to detect collection
	 * @return saved/updated entities list
	 */
	<E, T extends E> List<T> saveOrUpdate(Class<E> entityClass, Iterable<T> entities);

	/**
	 * Find entity by the specified entity identity
	 * @param <E> entity type
	 * @param id to filter
	 * @param entityClass to detect collection
	 * @return entity or NULL
	 */
	<E> E findById(Class<E> entityClass, String id);
	/**
	 * Find the first occurred entity by the specified conditions
	 * @param <E> entity type
	 * @param field to filter
	 * @param value to compare
	 * @param entityClass to detect collection
	 * @return the first occurred entity or NULL
	 */
	<E> E findOne(Class<E> entityClass, String field, Object value);
	/**
	 * Find the first occurred entity by the specified conditions
	 * @param <E> entity type
	 * @param filters to filter
	 * @param entityClass to detect collection
	 * @return the first occurred entity or NULL
	 */
	<E> E findOne(Class<E> entityClass, Map<String, Object> filters);
	/**
	 * Find the entities list by the specified conditions, sort and limitation
	 * @param <E> entity type
	 * @param filters to filter
	 * @param sorter to sort. NUll for ignoring sorting
	 * @param offset the included begin records index
	 * @param limit the limitted number of records. &lt;= 0 for all
	 * @param entityClass to detect collection
	 * @return the entities list or empty
	 */
	<E> List<E> find(Class<E> entityClass, Map<String, Object> filters, Sort sorter, long offset, int limit);

	/**
	 * Check the specified values whether exist in collection of the specified entity class
	 * @param <E> entity type
	 * @param all true for existing all; else just need at least one
	 * @param entityClass to detect collection
	 * @param field to detect
	 * @param values to check
	 * @return true for existed; else false
	 */
	<E> boolean exists(Class<E> entityClass, boolean all, String field, Object...values);
	/**
	 * Check the specified {@link ObjectId} whether exist in collection of the specified entity class
	 * @param <E> entity type
	 * @param all true for existing all; else just need at least one
	 * @param entityClass to detect collection
	 * @param ids to check
	 * @return true for existed; else false
	 */
	<E> boolean exists(Class<E> entityClass, boolean all, ObjectId...ids);

	/**
	 * Aggregate the entities list by the specified conditions and operations
	 * @param <E> entity type
	 * @param <O> {@link AggregationOperation}
	 * @param entityClass entity class result
	 * @param matchConditions to filter
	 * @param operations aggregation operations
	 * @return the entities list
	 */
	<E, O extends AggregationOperation> List<E> aggregateLookup(
			Class<E> entityClass, Map<String, Object> matchConditions, O...operations);
	/**
	 * Aggregate the entities list by the specified conditions and operations
	 * @param <E> entity type
	 * @param <O> {@link AggregationOperation}
	 * @param entityClass entity class result
	 * @param matchConditions to filter
	 * @param operations aggregation operations
	 * @return {@link AggregationResults}
	 */
	<E, O extends AggregationOperation> AggregationResults<E> aggregateLookupResults(
			Class<E> entityClass, Map<String, Object> matchConditions, O...operations);
	/**
	 * Aggregate the entities list by the specified conditions and operations
	 * @param <E> entity type
	 * @param <O> {@link AggregationOperation}
	 * @param entityClass entity class result
	 * @param operations aggregation operations
	 * @return the entities list
	 */
	<E, O extends AggregationOperation> List<E> aggregateLookup(Class<E> entityClass, O...operations);
	/**
	 * Aggregate the entities list by the specified conditions and operations
	 * @param <E> entity type
	 * @param <O> {@link AggregationOperation}
	 * @param entityClass entity class result
	 * @param operations aggregation operations
	 * @return {@link AggregationResults}
	 */
	<E, O extends AggregationOperation> AggregationResults<E> aggregateLookupResults(Class<E> entityClass, O...operations);

	/**
	 * Find and update the matched entity by the specified conditions and updaters
	 * @param <E> entity type
	 * @param entityClass entity class result
	 * @param filters to filter
	 * @param updaters to update
	 * @return the previous entity before updating or NULL
	 */
	<E> E findAndModify(Class<E> entityClass, Map<String, Object> filters, Map<String, Object> updaters);
	/**
	 * Update the first matched entity by the specified conditions and updaters
	 * @param <E> entity type
	 * @param entityClass to detect collection
	 * @param filters to filter
	 * @param updaters to update
	 * @return {@link UpdateResult}
	 */
	<E> UpdateResult updateFirst(Class<E> entityClass, Map<String, Object> filters, Map<String, Object> updaters);
	/**
	 * Update all matched entities by the specified conditions and updaters
	 * @param <E> entity type
	 * @param entityClass to detect collection
	 * @param filters to filter
	 * @param updaters to update
	 * @return {@link UpdateResult}
	 */
	<E> UpdateResult updateMulti(Class<E> entityClass, Map<String, Object> filters, Map<String, Object> updaters);

	/**
	 * Push the specified values into the specified field of all document(s) that matched with condition filters
	 * @param <E> entity type
	 * @param entityClass to detect collection
	 * @param filters to filter
	 * @param pushedField to push
	 * @param pushedValues pushed values
	 * @return {@link UpdateResult}
	 */
	<E> UpdateResult push(Class<E> entityClass, Map<String, Object> filters, String pushedField, Object...pushedValues);
	/**
	 * Pull the specified values out of the specified field of all document(s) that matched with condition filters
	 * @param <E> entity type
	 * @param entityClass to detect collection
	 * @param filters to filter
	 * @param pulledField to pull
	 * @param pulledValues pulled values
	 * @return {@link UpdateResult}
	 */
	<E> UpdateResult pull(Class<E> entityClass, Map<String, Object> filters, String pulledField, Object...pulledValues);

	/**
	 * Delete all document(s) that matched with the specified identitites
	 * @param <E> entity type
	 * @param entityClass to detect collection
	 * @param needToDeleteIds to delete
	 * @return {@link DeleteResult}
	 */
	<E> DeleteResult deleteById(Class<E> entityClass, ObjectId...needToDeleteIds);
	/**
	 * Delete all document(s) that matched with the specified conditions
	 * @param <E> entity type
	 * @param entityClass to detect collection
	 * @param filters to filter
	 * @return {@link DeleteResult}
	 */
	<E> DeleteResult delete(Class<E> entityClass, Map<String, Object> filters);
	/**
	 * Delete the specified entity
	 * @param <E> entity type
	 * @param entityClass to detect collection
	 * @param entity to delete
	 * @return {@link DeleteResult}
	 */
	<E> DeleteResult delete(Class<E> entityClass, E entity);

	/**
	 * Create/Access the specified database name
	 * @param dbName to create/access
	 * @return {@link MongoDatabase}
	 */
	MongoDatabase createDatabase(String dbName);
	/**
	 * Create database user for the specified {@link MongoDatabase}
	 * @param db to create user
	 * @param user user name
	 * @param password user password
	 * @param roles user roles
	 * @return {@link Document}
	 */
	Document createDatabaseUser(MongoDatabase db, String user, String password, String... roles);
	/**
	 * Create the specified collections for the specified {@link MongoDatabase}
	 * @param db to create collections
	 * @param collections to create
	 */
	void createCollections(MongoDatabase db, String... collections);
	/**
	 * Create the specified collections by entity classes for the specified {@link MongoDatabase}
	 * @param db to create collections
	 * @param entityClasses to create
	 */
	void createCollections(MongoDatabase db, Class<?>... entityClasses);
}
