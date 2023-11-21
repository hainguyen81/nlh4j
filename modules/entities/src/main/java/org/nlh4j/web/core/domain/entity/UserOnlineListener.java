/*
 * @(#)UserOnlineListener.java
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
public class UserOnlineListener implements EntityListener<UserOnline> {

    @Override
    public void preInsert(UserOnline entity, PreInsertContext<UserOnline> context) {
    }

    @Override
    public void preUpdate(UserOnline entity, PreUpdateContext<UserOnline> context) {
    }

    @Override
    public void preDelete(UserOnline entity, PreDeleteContext<UserOnline> context) {
    }

    @Override
    public void postInsert(UserOnline entity, PostInsertContext<UserOnline> context) {
    }

    @Override
    public void postUpdate(UserOnline entity, PostUpdateContext<UserOnline> context) {
    }

    @Override
    public void postDelete(UserOnline entity, PostDeleteContext<UserOnline> context) {
    }
}