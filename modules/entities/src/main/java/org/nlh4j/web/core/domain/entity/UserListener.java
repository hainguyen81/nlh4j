/*
 * @(#)UserListener.java
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.web.core.domain.entity;

import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PostDeleteContext;
import org.seasar.doma.jdbc.entity.PostInsertContext;
import org.seasar.doma.jdbc.entity.PostUpdateContext;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

/**
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 * @version $Revision:  $  $Date:  $
 */
public class UserListener implements EntityListener<User> {

    @Override
    public void preInsert(User entity, PreInsertContext<User> context) {
    }

    @Override
    public void preUpdate(User entity, PreUpdateContext<User> context) {
    }

    @Override
    public void preDelete(User entity, PreDeleteContext<User> context) {
    }

    @Override
    public void postInsert(User entity, PostInsertContext<User> context) {
    }

    @Override
    public void postUpdate(User entity, PostUpdateContext<User> context) {
    }

    @Override
    public void postDelete(User entity, PostDeleteContext<User> context) {
    }
}