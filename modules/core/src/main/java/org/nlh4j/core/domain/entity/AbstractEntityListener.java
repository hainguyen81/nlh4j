/*
 * @(#)AbstractEntityListener.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.domain.entity;

import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

import lombok.extern.slf4j.Slf4j;

/**
 * Abstract {@link EntityListener}
 * 
 * @param <T>
 * 			  entity type
 */
@Slf4j
public abstract class AbstractEntityListener<T> implements EntityListener<T> {

	/** */
	private static final long serialVersionUID = 1L;
	
	@Override
    public void preInsert(T entity, PreInsertContext<T> context) {
		log.debug("[EntityListener] preInsert [{}] context [{}]", entity, context);
    }

    @Override
    public void preUpdate(T entity, PreUpdateContext<T> context) {
		log.debug("[EntityListener] preInsert [{}] context [{}]", entity, context);
    }

    @Override
    public void preDelete(T entity, PreDeleteContext<T> context) {
		log.debug("[EntityListener] preInsert [{}] context [{}]", entity, context);
    }

    @Override
    public void postInsert(T entity, PostInsertContext<T> context) {
		log.debug("[EntityListener] preInsert [{}] context [{}]", entity, context);
    }

    @Override
    public void postUpdate(T entity, PostUpdateContext<T> context) {
		log.debug("[EntityListener] preInsert [{}] context [{}]", entity, context);
    }

    @Override
    public void postDelete(T entity, PostDeleteContext<T> context) {
		log.debug("[EntityListener] preInsert [{}] context [{}]", entity, context);
    }
}
