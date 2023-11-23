/*
 * @(#)IObserveAction.java
 * Copyright 2018 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.observation;

import java.io.Closeable;
import java.io.Serializable;

import org.bson.Document;

import com.mongodb.client.model.changestream.OperationType;
import com.mongodb.client.model.changestream.UpdateDescription;

/**
 * Observe Action Interface to perform an action with changed {@link Document}
 *
 * @author Hai Nguyen
 *
 */
public interface IObserveAction extends Closeable, Serializable {
    /**
     * Observe Actions String Values List
     */
    static final String[] FULL_OBSERVE_ACTIONS =
            new String[] {
                    OperationType.INSERT.getValue()
                    , OperationType.UPDATE.getValue()
                    , OperationType.REPLACE.getValue()
                    , OperationType.DELETE.getValue()
                    , OperationType.INVALIDATE.getValue()
            };

    /**
     * Perform action with the specified changed {@link Document} and operation that had made that chaging
     * @param op {@link OperationType}
     * @param document {@link Document}
     * @param description {@link UpdateDescription}
     */
    void perform(final OperationType op, final Document document, final UpdateDescription description);
}
