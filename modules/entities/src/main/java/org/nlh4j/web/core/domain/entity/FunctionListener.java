/*
 * @(#)RoleListener.java
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
public class FunctionListener implements EntityListener<Function> {

    @Override
    public void preInsert(Function entity, PreInsertContext<Function> context) {
    }

    @Override
    public void preUpdate(Function entity, PreUpdateContext<Function> context) {
    }

    @Override
    public void preDelete(Function entity, PreDeleteContext<Function> context) {
    }

    @Override
    public void postInsert(Function entity, PostInsertContext<Function> context) {
    }

    @Override
    public void postUpdate(Function entity, PostUpdateContext<Function> context) {
    }

    @Override
    public void postDelete(Function entity, PostDeleteContext<Function> context) {
    }
}