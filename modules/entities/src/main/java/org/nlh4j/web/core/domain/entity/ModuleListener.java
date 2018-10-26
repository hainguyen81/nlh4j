/*
 * @(#)ModuleListener.java 1.0 Jun 1, 2015
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
public class ModuleListener implements EntityListener<Module> {

    @Override
    public void preInsert(Module entity, PreInsertContext<Module> context) {
    }

    @Override
    public void preUpdate(Module entity, PreUpdateContext<Module> context) {
    }

    @Override
    public void preDelete(Module entity, PreDeleteContext<Module> context) {
    }

    @Override
    public void postInsert(Module entity, PostInsertContext<Module> context) {
    }

    @Override
    public void postUpdate(Module entity, PostUpdateContext<Module> context) {
    }

    @Override
    public void postDelete(Module entity, PostDeleteContext<Module> context) {
    }
}