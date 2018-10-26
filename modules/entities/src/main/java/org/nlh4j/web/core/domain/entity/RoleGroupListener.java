/*
 * @(#)RoleGroupListener.java 1.0 Jun 1, 2015
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
public class RoleGroupListener implements EntityListener<RoleGroup> {

    @Override
    public void preInsert(RoleGroup entity, PreInsertContext<RoleGroup> context) {
    }

    @Override
    public void preUpdate(RoleGroup entity, PreUpdateContext<RoleGroup> context) {
    }

    @Override
    public void preDelete(RoleGroup entity, PreDeleteContext<RoleGroup> context) {
    }

    @Override
    public void postInsert(RoleGroup entity, PostInsertContext<RoleGroup> context) {
    }

    @Override
    public void postUpdate(RoleGroup entity, PostUpdateContext<RoleGroup> context) {
    }

    @Override
    public void postDelete(RoleGroup entity, PostDeleteContext<RoleGroup> context) {
    }
}