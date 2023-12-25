/*
 * @(#)EntityListener.java
 * Copyright 2016 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.core.domain.entity;

import java.util.Objects;

import org.nlh4j.support.IGenericTypeSupport;
import org.nlh4j.util.LogUtils;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

/**
 * Extend of {@link org.seasar.doma.jdbc.entity.EntityListener}
 * 
 * @param <T>
 * 			  entity type
 */
public interface EntityListener<T> extends org.seasar.doma.jdbc.entity.EntityListener<T>, IGenericTypeSupport {

	@Override
    default void preInsert(T entity, PreInsertContext<T> context) {
		LogUtils.logDebug(getClass(), "[EntityListener] preInsert [" + Objects.toString(entity) + "]");
    }

    @Override
    default void preUpdate(T entity, PreUpdateContext<T> context) {
		LogUtils.logDebug(getClass(), "[EntityListener] preUpdate [" + Objects.toString(entity) + "]");
    }

    @Override
    default void preDelete(T entity, PreDeleteContext<T> context) {
		LogUtils.logDebug(getClass(), "[EntityListener] preDelete [" + Objects.toString(entity) + "]");
    }

    @Override
    default void postInsert(T entity, PostInsertContext<T> context) {
		LogUtils.logDebug(getClass(), "[EntityListener] postInsert [" + Objects.toString(entity) + "]");
    }

    @Override
    default void postUpdate(T entity, PostUpdateContext<T> context) {
		LogUtils.logDebug(getClass(), "[EntityListener] postUpdate [" + Objects.toString(entity) + "]");
    }

    @Override
    default void postDelete(T entity, PostDeleteContext<T> context) {
		LogUtils.logDebug(getClass(), "[EntityListener] postDelete [" + Objects.toString(entity) + "]");
    }
}
