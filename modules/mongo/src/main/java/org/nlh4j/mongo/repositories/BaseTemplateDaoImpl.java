/*
 * @(#)BaseTemplateDaoImpl.java Aug 11, 2019
 * Copyright 2019 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.repositories;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Singleton;

import org.apache.commons.lang3.ArrayUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.nlh4j.core.servlet.SpringContextHelper;
import org.nlh4j.mongo.helpers.CriteriaBuilder;
import org.nlh4j.mongo.helpers.MongoConstants.MongoFields;
import org.nlh4j.mongo.helpers.MongoConstants.MongoKeywords;
import org.nlh4j.mongo.helpers.UpdateBuilder;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The basic template implementation of {@link BaseDao}
 * @author Hai Nguyen
 *
 * @param <E> entity type
 */
@SuppressWarnings("unchecked")
@Repository
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Singleton
@Transactional
public abstract class BaseTemplateDaoImpl implements BaseTemplateDao, ApplicationListener<ContextRefreshedEvent> {

	/** */
	private static final long serialVersionUID = 1L;
	/** slf4j */
    protected final transient Logger logger = LoggerFactory.getLogger(this.getClass());
    /** {@link SpringContextHelper} */
    private SpringContextHelper contextHelper;

	/**
	 * {@link MongoTemplate} information
	 * @author Hai Nguyen
	 */
	@Data
	@EqualsAndHashCode(callSuper = false)
	protected static class MongoTemplateInfo {
		/** {@link MongoTemplate} */
		private MongoTemplate template;

		/**
		 * Get {@link MongoClient}
		 * @return {@link MongoClient}
		 */
		public MongoClient getMongoClient() {
			MongoTemplate mongoTempl = this.getTemplate();
			return (mongoTempl == null ? null
					: BeanUtils.safeType(BeanUtils.getFieldValue(mongoTempl, "mongoClient"), MongoClient.class));
		}

		/** {@link BeanDefinition} */
		private BeanDefinition definition;

		/**
		 * Get bean resource description from {@link BeanDefinition}
		 * @return bean resource description
		 */
		public String getBeanResource() {
			BeanDefinition beanDefinition = this.getDefinition();
			return (beanDefinition != null ? beanDefinition.getResourceDescription() : null);
		}

		/**
		 * Get database host from {@link MongoClient}
		 * @return database host
		 */
		public String getDbHost() {
			MongoClient client = this.getMongoClient();
			ServerAddress dbHost = (client == null ? null : client.getAddress());
			return (dbHost != null ? dbHost.getHost() : null);
		}

		/**
		 * Get database port from {@link MongoClient}
		 * @return database port
		 */
		public int getDbPort() {
			MongoClient client = this.getMongoClient();
			ServerAddress dbHost = (client == null ? null : client.getAddress());
			return (dbHost != null ? dbHost.getPort() : 0);
		}

		/**
		 * Get database port from {@link MongoClient}
		 * @return database port
		 */
		public String getDbName() {
			MongoTemplate mongoTempl = this.getTemplate();
			MongoDatabase db = (mongoTempl == null ? null : mongoTempl.getDb());
			return (db != null ? db.getName() : null);
		}
	}
	/** cached {@link MongoTemplate} from application context */
	private static final transient List<MongoTemplateInfo> templates = new LinkedList<MongoTemplateInfo>();
	/**
	 * Get all {@link MongoTemplate} definition from application context
	 * @return all {@link MongoTemplate} definition from application context
	 */
	protected final List<MongoTemplateInfo> getTemplates() {
		synchronized (templates) {
			ensureMongoTemplates();
			return templates;
		}
	}
	/**
	 * Ensure {@link MongoTemplate} from context configuration
	 */
	protected final void ensureMongoTemplates() {
		synchronized (templates) {
			if (CollectionUtils.isEmpty(templates)) {
				SpringContextHelper ctxHelper = getContexthelper();
				List<MongoTemplateInfo> parsedTemplates = this.parseMongoTemplates(ctxHelper.getContext());
				if (!CollectionUtils.isEmpty(parsedTemplates)) {
					templates.addAll(parsedTemplates);
				}
			}
		}
	}
	/**
	 * Parse all {@link MongoTemplate} from the specified {@link ApplicationContext}
	 * @param ctx to parse
	 */
	private LinkedList<MongoTemplateInfo> parseMongoTemplates(ApplicationContext ctx) {
		LinkedList<MongoTemplateInfo> templatesList = new LinkedList<MongoTemplateInfo>();
		if (ctx != null) {
			templatesList = new LinkedList<MongoTemplateInfo>();
			BeanFactory beanFactory = BeanUtils.safeType(
					BeanUtils.getFieldValue(ctx, "beanFactory"), BeanFactory.class);
			if (beanFactory != null) {
				Object beanNames = BeanUtils.invokeMethod(
						beanFactory, "getBeanNamesForType", Boolean.TRUE,
						new Class<?>[] { Class.class },
						new Object[] { MongoTemplate.class });
				if (CollectionUtils.isArray(beanNames)
						|| CollectionUtils.isCollection(beanNames)) {
					int size = CollectionUtils.getSize(beanNames);
					for (int i = 0; i < size; i++) {
						String beanName = String.valueOf(CollectionUtils.get(beanNames, i));
						logger.info("Defined MongoTemplate with bean name {" + beanName + "}");
						BeanDefinition definition = BeanUtils.safeType(
								BeanUtils.invokeMethod(
										beanFactory, "getBeanDefinition", Boolean.TRUE,
										new Class<?>[] { String.class }, new Object[] { beanName }),
								BeanDefinition.class);
						Class<?> mongoTempClass = (definition == null ? null
								: BeanUtils.safeClass(definition.getBeanClassName()));
						if (MongoTemplate.class.equals(mongoTempClass)) {
							MongoTemplateInfo template = new MongoTemplateInfo();
							template.setDefinition(definition);
							template.setTemplate(ctx.getBean(beanName, MongoTemplate.class));
							templatesList.add(template);
						}
					}
				}
			}
		}
		return templatesList;
	}

	/**
	 * Listen {@link ContextRefreshedEvent}
	 * @param event {@link ContextRefreshedEvent} data
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// apply context
		SpringContextHelper ctxHelper = getContexthelper();
		ctxHelper.setApplicationContext(event == null ? null : event.getApplicationContext());
		// detect to try parsing mongo templates
		ensureMongoTemplates();
	}

	/**
	 * @return the contexthelper
	 */
	protected final SpringContextHelper getContexthelper() {
		if (contextHelper == null) {
			contextHelper = new SpringContextHelper();
		}
		return contextHelper;
	}

	/**
	 * Ensure {@link MongoTemplate} for performing actions
	 */
	private void ensureMongoTemplate() {
		MongoTemplate mongoTemplate = getMongoTemplate();
		Assert.notNull(mongoTemplate, "Please implement getMongoTemplate method to perform actions!");
	}

	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#insertBatch(java.lang.Class, java.util.Collection)
	 */
	@Override
	public <E, T extends E> Collection<T> insertBatch(Class<E> entityClass, Collection<T> entities) {
		ensureMongoTemplate();
		return getMongoTemplate().insert(entities, entityClass);
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#saveOrUpdate(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <E, T extends E> T saveOrUpdate(Class<E> entityClass, T entity) {
		ensureMongoTemplate();
		return getMongoTemplate().save(entity, getMongoTemplate().getCollectionName(entityClass));
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#saveOrUpdate(java.lang.Class, java.lang.Iterable)
	 */
	@Override
	public <E, T extends E> List<T> saveOrUpdate(Class<E> entityClass, Iterable<T> entities) {
		ensureMongoTemplate();
		List<T> savedEntities = new LinkedList<>();
		if (!CollectionUtils.isEmpty(entities)) {
			for(final Iterator<T> it = entities.iterator(); it.hasNext();) {
				savedEntities.add(this.saveOrUpdate(entityClass, it.next()));
			}
		}
		return savedEntities;
	}

	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#findById(java.lang.Class, java.lang.String)
	 */
	@Override
	public <E> E findById(Class<E> entityClass, String id) {
		ensureMongoTemplate();
		Assert.isTrue(StringUtils.hasText(id) && ObjectId.isValid(id), "Invalid identity {" + id + "}!");
		return getMongoTemplate().findById(new ObjectId(id), entityClass);
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#find(java.lang.Class, java.util.Map, org.springframework.data.domain.Sort, long, int)
	 */
	@Override
	public <E> List<E> find(Class<E> entityClass, Map<String, Object> filters, Sort sorter, long offset, int limit) {
		ensureMongoTemplate();
		Query query = CriteriaBuilder.createQueryCriterias(filters);
		if (sorter != null) query.with(sorter);
		offset = Math.max(0, offset);
		query.skip(offset);
		if (limit > 0) query.limit(limit);
		logger.debug(query.toString());
		return getMongoTemplate().find(query, entityClass);
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#findOne(java.lang.Class, java.util.Map)
	 */
	@Override
	public <E> E findOne(Class<E> entityClass, Map<String, Object> filters) {
		ensureMongoTemplate();
		Query query = CriteriaBuilder.createQueryCriterias(filters);
		logger.debug(query.toString());
		return getMongoTemplate().findOne(query, entityClass);
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#findOne(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	@Override
	public <E> E findOne(Class<E> entityClass, String field, Object value) {
		Map<String, Object> filters = new LinkedHashMap<>();
		filters.put(field, value);
		return this.findOne(entityClass, filters);
	}

	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#exists(java.lang.Class, boolean, org.bson.types.ObjectId[])
	 */
	@Override
	public <E> boolean exists(Class<E> entityClass, boolean all, ObjectId...ids) {
		return exists(entityClass, all, MongoFields.MONGODB_OBJECT_ID, (Object[]) ids);
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#exists(java.lang.Class, boolean, java.lang.String, java.lang.Object[])
	 */
	@Override
	public <E> boolean exists(Class<E> entityClass, boolean all, String field, Object...values) {
		ensureMongoTemplate();
		Map<String, Object> filters = new LinkedHashMap<>();
		filters.put(MongoKeywords.MONGODB_OR + field, values);
		Query query = CriteriaBuilder.createQueryCriterias(filters);
		logger.debug(query.toString());
		long count = getMongoTemplate().count(query, entityClass);
		logger.debug("==> Found {" + count + "} matched records!");
		return (count > 0 && (!all || CollectionUtils.getSize(values) == count));
	}

	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#aggregateLookup(java.lang.Class, java.util.Map, org.springframework.data.mongodb.core.aggregation.AggregationOperation[])
	 */
	@Override
	public <E, O extends AggregationOperation> List<E> aggregateLookup(
			Class<E> entityClass, Map<String, Object> matchConditions, O...operations) {
		return aggregateLookupResults(entityClass, matchConditions, operations).getMappedResults();
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#aggregateLookupResults(java.lang.Class, java.util.Map, org.springframework.data.mongodb.core.aggregation.AggregationOperation[])
	 */
	@Override
	public <E, O extends AggregationOperation> AggregationResults<E> aggregateLookupResults(
			Class<E> entityClass, Map<String, Object> matchConditions, O...operations) {
		ensureMongoTemplate();
		MatchOperation matchOperation = new MatchOperation(CriteriaBuilder.createCriterias(matchConditions));
		List<AggregationOperation> operationsList = new LinkedList<>();
		operationsList.add(matchOperation);
		if (ArrayUtils.isNotEmpty(operations)) {
			operationsList.addAll(Arrays.asList(operations));
		}
		return aggregateLookupResults(entityClass, operationsList.toArray(new AggregationOperation[operationsList.size()]));
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#aggregateLookup(java.lang.Class, org.springframework.data.mongodb.core.aggregation.AggregationOperation[])
	 */
	@Override
	public <E, O extends AggregationOperation> List<E> aggregateLookup(Class<E> entityClass, O...operations) {
		return aggregateLookupResults(entityClass, operations).getMappedResults();
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#aggregateLookupResults(java.lang.Class, org.springframework.data.mongodb.core.aggregation.AggregationOperation[])
	 */
	@Override
	public <E, O extends AggregationOperation> AggregationResults<E> aggregateLookupResults(Class<E> entityClass, O...operations) {
		ensureMongoTemplate();
		Aggregation aggregation = Aggregation.newAggregation(operations);
		logger.debug(aggregation.toString());
		return getMongoTemplate().aggregate(aggregation, entityClass, entityClass);
	}

	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#findAndModify(java.lang.Class, java.util.Map, java.util.Map)
	 */
	@Override
	public <E> E findAndModify(Class<E> entityClass, Map<String, Object> filters, Map<String, Object> updaters) {
		ensureMongoTemplate();
		Query query = CriteriaBuilder.createQueryCriterias(filters);
		Update update = UpdateBuilder.createUpdater(updaters);
		logger.debug(query.toString());
		logger.debug("==> Update: " + update.toString());
		return getMongoTemplate().findAndModify(query, update, entityClass);
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#updateFirst(java.lang.Class, java.util.Map, java.util.Map)
	 */
	@Override
	public <E> UpdateResult updateFirst(Class<E> entityClass, Map<String, Object> filters, Map<String, Object> updaters) {
		ensureMongoTemplate();
		Query query = CriteriaBuilder.createQueryCriterias(filters);
		Update update = UpdateBuilder.createUpdater(updaters);
		logger.debug(query.toString());
		logger.debug("==> Update: " + update.toString());
		return getMongoTemplate().updateFirst(query, update, entityClass);
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#updateMulti(java.lang.Class, java.util.Map, java.util.Map)
	 */
	@Override
	public <E> UpdateResult updateMulti(Class<E> entityClass, Map<String, Object> filters, Map<String, Object> updaters) {
		ensureMongoTemplate();
		Query query = CriteriaBuilder.createQueryCriterias(filters);
		Update update = UpdateBuilder.createUpdater(updaters);
		logger.debug(query.toString());
		logger.debug("==> Update: " + update.toString());
		return getMongoTemplate().updateMulti(query, update, entityClass);
	}

	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#push(java.lang.Class, java.util.Map, java.lang.String, java.lang.Object[])
	 */
	@Override
	public <E> UpdateResult push(Class<E> entityClass, Map<String, Object> filters, String pushedField, Object...pushedValues) {
		Map<String, Object> updaters = new LinkedHashMap<>();
		updaters.put(MongoKeywords.MONGODB_PUSH + pushedField, pushedValues);
		return updateMulti(entityClass, filters, updaters);
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#pull(java.lang.Class, java.util.Map, java.lang.String, java.lang.Object[])
	 */
	@Override
	public <E> UpdateResult pull(Class<E> entityClass, Map<String, Object> filters, String pulledField, Object...pulledValues) {
		Map<String, Object> updaters = new LinkedHashMap<>();
		updaters.put(MongoKeywords.MONGODB_PULL + pulledField, pulledValues);
		return updateMulti(entityClass, filters, updaters);
	}

	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#deleteById(java.lang.Class, org.bson.types.ObjectId[])
	 */
	@Override
	public <E> DeleteResult deleteById(Class<E> entityClass, ObjectId...needToDeleteIds) {
		Map<String, Object> filters = new LinkedHashMap<>();
		filters.put(MongoKeywords.MONGODB_IN + MongoFields.MONGODB_OBJECT_ID, needToDeleteIds);
		return delete(entityClass, filters);
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#delete(java.lang.Class, java.util.Map)
	 */
	@Override
	public <E> DeleteResult delete(Class<E> entityClass, Map<String, Object> filters) {
		ensureMongoTemplate();
		Query query = CriteriaBuilder.createQueryCriterias(filters);
		logger.debug(query.toString());
		return getMongoTemplate().remove(query, entityClass);
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#delete(java.lang.Class, java.lang.Object)
	 */
	@Override
	public <E> DeleteResult delete(Class<E> entityClass, E entity) {
		ensureMongoTemplate();
		return getMongoTemplate().remove(entity, getMongoTemplate().getCollectionName(entityClass));
	}

	/* (non-Javadoc)
	 * @see com.camhub.api.mongo.base.BaseDao#createDatabase(java.lang.String)
	 */
	@Override
	public MongoDatabase createDatabase(String dbName) {
		MongoDbFactory dbFactory = Optional.ofNullable(getMongoTemplate())
				.map(MongoTemplate::getMongoDbFactory).orElse(null);
		if (Objects.isNull(dbFactory)) {
			logger.warn("Could not found MongoDbFactory to create database {" + dbName + "}!");
			return null;
		}

		// access data of company. if it's not exist, database has been created
		MongoDatabase mongoDb = dbFactory.getDb(dbName);
		if (Objects.isNull(mongoDb)) {
			logger.warn("Could not access/create database {" + dbName + "}!");
			return null;
		}
		return mongoDb;
	}
	/* (non-Javadoc)
	 * @see com.camhub.api.mongo.base.BaseDao#createDatabaseUser(com.mongodb.client.MongoDatabase, java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Override
	public Document createDatabaseUser(MongoDatabase db, String user, String password, String... roles) {
		// create user admin for company's database
	    Map<String, Object> createUserCmd = new LinkedHashMap<>();
	    createUserCmd.put("createUser", user);
	    createUserCmd.put("pwd", password);
	    createUserCmd.put("roles", roles);
	    return db.runCommand(new BasicDBObject(createUserCmd));
	}
	/* (non-Javadoc)
	 * @see com.camhub.api.mongo.base.BaseDao#createCollections(com.mongodb.client.MongoDatabase, java.lang.String[])
	 */
	@Override
	public void createCollections(MongoDatabase db, String... collections) {
		if (db != null && ArrayUtils.isNotEmpty(collections)) {
			for(String collection : collections) {
				if (!StringUtils.hasText(collection)) continue;
				db.createCollection(collection);
			}
		}
	}
	/* (non-Javadoc)
	 * @see org.nlh4j.mongo.repositories.BaseTemplateDao#createCollections(com.mongodb.client.MongoDatabase, java.lang.Class[])
	 */
	@Override
	public void createCollections(MongoDatabase db, Class<?>... entityClasses) {
		if (db != null && ArrayUtils.isNotEmpty(entityClasses)) {
			for(Class<?> entityClass : entityClasses) {
				if (entityClass == null) continue;
				String collectionName = org.apache.commons.lang3.StringUtils.EMPTY;
				org.springframework.data.mongodb.core.mapping.Document annotation =
						BeanUtils.getClassAnnotation(entityClass, org.springframework.data.mongodb.core.mapping.Document.class);
				if (annotation != null && StringUtils.hasText(annotation.collection())) {
					collectionName = annotation.collection();
				}
				if (!StringUtils.hasText(collectionName) && getMongoTemplate() != null) {
					collectionName = getMongoTemplate().getCollectionName(entityClass);
				}
				if (!StringUtils.hasText(collectionName)) {
					logger.warn("Could not detect collection name of {" + entityClass.getCanonicalName() + "} class!");
					continue;
				}
				db.createCollection(collectionName);
			}
		}
	}
}
