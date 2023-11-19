/*
 * @(#)BasicAggregationOperation.java Aug 11, 2019
 * Copyright 2019 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.operations;

import java.io.Serializable;

import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.util.Assert;

import com.mongodb.DBObject;

/**
 * Basic {@link AggregationOperation}
 *
 * @author Hai Nguyen
 *
 */
public class BasicAggregationOperation implements Serializable, AggregationOperation {

	/** */
	private static final long serialVersionUID = 1L;
	/** {@link DBObject} */
	private Document operation;

	/**
	 * Initialize a new instance of {@link BasicAggregationOperation} class
	 */
	protected BasicAggregationOperation() {
	}
	/**
	 * Initialize a new instance of class {@link BasicAggregationOperation}
	 * @param operation {@link Document}
	 */
	public BasicAggregationOperation(Document operation) {
		Assert.notNull(operation, "operation");
		this.operation = operation;
	}

	/**
	 * @return the operation
	 */
	public final Document getOperation() {
		return this.operation;
	}
	/**
	 * Set aggregation operation
	 * @param operation the operation to set
	 */
	protected void setOperation(Document operation) {
		this.operation = operation;
	}

	/* (non-Javadoc)
	 * @see org.springframework.data.mongodb.core.aggregation.AggregationOperation#toDocument(org.springframework.data.mongodb.core.aggregation.AggregationOperationContext)
	 */
	@Override
	public Document toDocument(AggregationOperationContext context) {
		return (context == null ? null : context.getMappedObject(operation));
	}
}
