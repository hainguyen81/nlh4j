/*
 * @(#)BaseDaoImpl.java Aug 11, 2019
 * Copyright 2019 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.repositories;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.nlh4j.mongo.annotation.Database;
import org.nlh4j.mongo.dto.BaseEntity;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * The basic implementation of {@link BaseDao}
 * @author Hai Nguyen
 *
 * @param <E> entity type
 */
@SuppressWarnings("unchecked")
@Repository
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Transactional
public class BaseDaoImpl<E extends BaseEntity> extends BaseTemplateDaoImpl implements BaseDao<E> {

	/** */
	private static final long serialVersionUID = 1L;
	private Class<E> entityClass;
	private transient MongoTemplate mongoTemplate;
	@Autowired
	private transient Environment env;
	@Value("${mongo.dbname:#{null}}")
	private String dbName = null;
	private String collection = null;

	/**
	 * By defining this class as abstract, we prevent Spring from creating instance
	 * of this class If not defined as abstract, getClass().getGenericSuperClass()
	 * would return Object. There would be exception because Object class does not
	 * hava constructor with parameters.
	 */
	protected BaseDaoImpl() {
	}

	/**
	 * Initialize a new instance of {@link BaseDaoImpl} class
	 * @param entityClass entity class
	 */
	public BaseDaoImpl(Class<? extends E> entityClass) {
		Assert.notNull(entityClass, "entityClass");
		this.entityClass = (Class<E>) entityClass;
	}

	/**
	 * Get the parameterized entity class
	 * @return the the parameterized entity class
	 */
	@Override
	public final Class<E> getEntityClass() {
		if (this.entityClass == null) {
			try {
				Type t = this.getClass().getGenericSuperclass();
				ParameterizedType pt = BeanUtils.safeType(t, ParameterizedType.class);
				Type[] argTypes = (pt == null ? null : pt.getActualTypeArguments());
				this.entityClass = (CollectionUtils.isEmpty(argTypes) ? null : (Class<E>) argTypes[0]);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return this.entityClass;
	}
	/**
	 * Get the current entity collection name
	 * @return the current entity collection name
	 */
	@Override
	public final String getCollectionName() {
		if (!StringUtils.hasText(this.collection) && Objects.nonNull(getEntityClass())) {
			// detect document annotation of current repository
			org.springframework.data.mongodb.core.mapping.Document docAnn =
					BeanUtils.getClassAnnotation(this.getClass(),
							org.springframework.data.mongodb.core.mapping.Document.class);
			if (Objects.isNull(docAnn)) {
				// detect document annotation of current repository entity class
				docAnn = BeanUtils.getClassAnnotation(
						this.getEntityClass(), org.springframework.data.mongodb.core.mapping.Document.class);
			}
			if (Objects.nonNull(docAnn) && StringUtils.hasText(docAnn.collection())) {
				this.collection = docAnn.collection();
			}

			// by database annotation
			if (!StringUtils.hasText(this.collection)) {
				// detect database annotation of current repository
				Database dbAnn = BeanUtils.getClassAnnotation(this.getClass(), Database.class);
				if (dbAnn == null) {
					// detect database annotation of current repository entity class
					dbAnn = BeanUtils.getClassAnnotation(this.getEntityClass(), Database.class);
				}
				if (Objects.nonNull(dbAnn) && StringUtils.hasText(dbAnn.collection())) {
					this.collection = dbAnn.collection();
				}
			}
		}

		// by mongo template
		if (!StringUtils.hasText(this.collection) && Objects.nonNull(getMongoTemplate()) && Objects.nonNull(getEntityClass())) {
			this.collection = getMongoTemplate().getCollectionName(getEntityClass());
		}

		// by entity class
		if (!StringUtils.hasText(this.collection) && Objects.nonNull(getEntityClass())) {
			this.collection = getEntityClass().getSimpleName().toLowerCase();
		}
		return this.collection;
	}

	/**
	 * Get respository's database name
	 * @return respository's database name
	 */
	@Override
	public final String getDatabaseName() {
		if (!StringUtils.hasText(this.dbName)) {
			// detect database annotation of current repository
			Database dbAnn = BeanUtils.getClassAnnotation(this.getClass(), Database.class);
			if (dbAnn == null) {
				// detect database annotation of current repository entity class
				dbAnn = BeanUtils.getClassAnnotation(this.getEntityClass(), Database.class);
			}
			if (dbAnn != null) {
				if (StringUtils.hasText(dbAnn.value())) {
					this.dbName = dbAnn.value();
				}
				if (this.env != null && !StringUtils.hasText(this.dbName) && StringUtils.hasText(dbAnn.valueKey())) {
					String holderPropertyKey = dbAnn.valueKey();
					String propertyKey = holderPropertyKey.replace("$", "").replace("{", "").replace("}", "");
					this.dbName = this.env.getProperty(holderPropertyKey);
					if (!StringUtils.hasText(this.dbName)) {
						this.dbName = this.env.getProperty(propertyKey);
					}
				}
				if (!StringUtils.hasText(this.dbName) && StringUtils.hasText(dbAnn.valueKey())) {
					String holderPropertyKey = dbAnn.valueKey();
					String propertyKey = holderPropertyKey.replace("$", "").replace("{", "").replace("}", "");
					List<PropertySourcesPlaceholderConfigurer> configurers =
							getContextHelper().searchBeans(PropertySourcesPlaceholderConfigurer.class);
					if (!CollectionUtils.isEmpty(configurers)) {
						for (PropertySourcesPlaceholderConfigurer configurer : configurers) {
							for (final Iterator<PropertySource<?>> it = configurer.getAppliedPropertySources()
									.iterator(); it.hasNext();) {
								PropertySource<?> source = it.next();
								if (source == null
										|| (!source.containsProperty(propertyKey)
												&& !source.containsProperty(holderPropertyKey)))
									continue;
								if (source.containsProperty(propertyKey)) {
									this.dbName = String.valueOf(source.getProperty(propertyKey));
								} else if (source.containsProperty(holderPropertyKey)) {
									this.dbName = String.valueOf(source.getProperty(holderPropertyKey));
								}
								if (StringUtils.hasText(this.dbName))
									break;
							}
							if (StringUtils.hasText(this.dbName))
								break;
						}
					}
				}
			}
		}
		return this.dbName;
	}
	/* (non-Javadoc)
	 * @see com.camhub.api.mongo.base.BaseDao#setDatabaseName(java.lang.String)
	 */
	@Override
	public void setDatabaseName(String dbName) {
		Assert.hasText(dbName, "dbName");
		this.dbName = dbName;
	}

	/**
	 * Gets {@link MongoTemplate} instance by database name
	 * @return {@link MongoTemplate} instance
	 */
	@Override
	public MongoTemplate getMongoTemplate() {
		// detect database
		String databaseName = this.getDatabaseName();
		String tempDbName = null;
		// check for template
		boolean needToParse = (this.mongoTemplate == null);
		if (!needToParse) {
			tempDbName = this.mongoTemplate.getDb().getName();
			needToParse = !org.apache.commons.lang3.StringUtils.equalsIgnoreCase(tempDbName, databaseName);
		}
		// need to parse template
		if (needToParse) {
			this.mongoTemplate = this.getMongoTemplate(null, 0, databaseName, Boolean.TRUE);
			// if not found; then requiring the first template
			if (this.mongoTemplate == null) {
				this.mongoTemplate = super.getContextHelper().searchBean(MongoTemplate.class);
			}
		}
		// return cached template if found
		if (this.mongoTemplate != null) {
			synchronized (this.mongoTemplate) {
				return this.mongoTemplate;
			}
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see com.camhub.api.mongo.base.BaseDao#setMongoTemplate(org.springframework.data.mongodb.core.MongoTemplate)
	 */
	@Override
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		Assert.notNull(mongoTemplate, "mongoTemplate");
		this.mongoTemplate = mongoTemplate;
	}
}
