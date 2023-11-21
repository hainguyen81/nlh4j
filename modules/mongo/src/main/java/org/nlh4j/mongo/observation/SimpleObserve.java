/*
 * @(#)SimpleObserve.java
 * Copyright 2018 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.mongo.observation;

import static java.util.Arrays.*;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.nlh4j.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.client.model.changestream.UpdateDescription;

/**
 * Simple class to observe changed {@link Document} from mongo
 *
 * @author Hai Nguyen
 */
@Component
@Singleton
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public abstract class SimpleObserve implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /** slf4j */
    protected final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    /** observation actions */
    protected static final String[] FULL_OBSERVE_ACTIONS = IObserveAction.FULL_OBSERVE_ACTIONS;

    // variables
    private String clientURI;
    private String collectionName;
    private transient MongoClient client;
    private transient MongoDatabase database;
    private transient MongoCollection<Document> collection;
    private Boolean observed;

    /**
     * Get the client
     *
     * @return the client
     */
    protected final MongoClient getClient() {
        return this.client;
    }
    /**
     * Get the database
     *
     * @return the database
     */
    protected final MongoDatabase getDatabase() {
        return this.database;
    }
    /**
     * Get the mongoCollection
     *
     * @return the mongoCollection
     */
    protected final MongoCollection<Document> getCollection() {
        return this.collection;
    }
    /**
     * Get the observed perform action
     *
     * @return the esAction
     */
    protected abstract IObserveAction getObservedAction();
    /**
     * Get the clientURI
     *
     * @return the clientURI
     */
    protected final String getClientURI() {
        return this.clientURI;
    }
    /**
     * Get the database
     *
     * @return the database
     */
    public final String getDatabaseName() {
        return (this.getDatabase() != null ? this.getDatabase().getName() : null);
    }
    /**
     * Get the collectionName
     *
     * @return the collectionName
     */
    public final String getCollectionName() {
        return this.collectionName;
    }
    /**
     * Get a boolean value indicating that collection has been observed
     * @return true for observed; else false
     */
    public final boolean isObserved() {
        return Boolean.TRUE.equals(this.observed);
    }

    /**
     * Initialize a new instance of {@link SimpleObserve}
     *
     * @param clientURI {@link MongoClientURI}
     * @param database mongo database name
     * @param collectionName collection name
     */
    public SimpleObserve(String clientURI, String database, String collectionName) {
        this.initialize(clientURI, database, collectionName);
    }

    /**
     * Initialize
     *
     * @param clientURI {@link MongoClientURI}
     * @param database mongo database name
     * @param collectionName collection name
     */
    private void initialize(String clientURI, String database, String collectionName) {
        // check parameters
        if (StringUtils.isBlank(clientURI)) {
            throw new IllegalArgumentException("[ERROR] Invalid observe mongo client URI!");
        }
        if (StringUtils.isBlank(database)) {
            throw new IllegalArgumentException("[ERROR] Invalid observe mongo database name!");
        }
        if (StringUtils.isBlank(collectionName)) {
            throw new IllegalArgumentException("[ERROR] Invalid observe mongo collection name!");
        }

        // start requiring collection
        MongoClientURI mongoClientURI = new MongoClientURI(clientURI);
        logger.info("1. START CONNECTING MONGO SERVER! {" + clientURI + "}");
        try { this.client = new MongoClient(mongoClientURI); }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
            this.client = null;
        } finally {
            if (this.client == null) {
                throw new IllegalArgumentException("Failure while connecting to Mongo Server via {" + clientURI + "}!");
            } else {
                this.clientURI = clientURI;
            }
        }

        // connect mongo database
        logger.info("2. START CONNECTING MONGO DATBASE! {" + database + "}");
        try { this.database = this.client.getDatabase(database); }
        catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.database = null;
        } finally {
            if (this.database == null) {
                try { this.client.close(); }
                catch (Exception e) {}
                throw new IllegalArgumentException("Failure while opening to Mongo Database {" + database + "}!");
            }
        }

        // open mongo collection
        logger.info("3. START OPENING MONGO COLLECTION! {" + collectionName + "}");
        try { this.collection = this.database.getCollection(collectionName); }
        catch (Exception e) {
        	logger.error(e.getMessage(), e);
            this.collection = null;
        } finally {
            if (this.collection == null) {
                try { this.client.close(); }
                catch (Exception e) {}
                throw new IllegalArgumentException("Failure while opening to Mongo Collection {" + collectionName + "} from Database {" + database + "}!");
            } else {
                this.collectionName = collectionName;
            }
        }

        // turn off observing
        this.observed = Boolean.FALSE;
    }

    /**
     * Observe the specified documents for the first time<br>
     * TODO Children classes maybe override this method for observing at the first time
     * @param documents to observed
     */
    public abstract void observeFirst(Document...documents);
    /**
     * Observe the specified documents for the first time
     * @param documents to observed
     */
    public void observeFirst(List<Document> documents) {
        if (!CollectionUtils.isEmpty(documents)) {
            this.observeFirst(CollectionUtils.toArray(documents, Document.class));
        }
    }

    /**
     * Start observe with all actions
     */
    public final void observe() {
        this.observe(FULL_OBSERVE_ACTIONS, Boolean.TRUE);
    }
    /**
     * Start observe
     *
     * @param observeActions actions to observe such as: insert/update/replace/delete/invalidate
     */
    public final void observe(final String[] observeActions) {
        this.observe(observeActions, Boolean.TRUE);
    }
    /**
     * Start observe
     *
     * @param observeActions actions to observe such as: insert/update/replace/delete/invalidate
     * @param observeFirst specify whether applying all present records. Require via
     */
    public final void observe(final String[] observeActions, final Boolean observeFirst) {
        if (this.getCollection() == null) {
            throw new IllegalArgumentException("Could not require mongo collection!");
        } else if (this.getObservedAction() == null) {
            throw new IllegalArgumentException("Not found observed action! Please implement getObservedAction method");
        } else if (Boolean.TRUE.equals(this.observed)) { // watched
            return;
        }

        // require collection/ES transport
        MongoCollection<Document> collection = this.getCollection();

        // insert all document for the fisrt running time
        if (Boolean.TRUE.equals(observeFirst) && collection.estimatedDocumentCount() > 0) {
            logger.info("OBSERVE ALL PRESENT DOCUMENTS FROM MONGO FOR FIRST RUNNING TIME!");
            try { this.observeFirst(collection.find().into(new LinkedList<Document>())); }
            catch (Exception e) { logger.error(e.getMessage(), e); }
        }

        // start observing
        String[] arrObserveActions = (observeActions == null || observeActions.length <= 0
                ? FULL_OBSERVE_ACTIONS : observeActions);
        collection.watch(asList(Aggregates.match(Filters.in("operationType", asList(arrObserveActions)))))
        .fullDocument(FullDocument.UPDATE_LOOKUP).forEach(
            new Block<ChangeStreamDocument<Document>>() {

                /*
                 * (Non-Javadoc)
                 * @see com.mongodb.Block#apply(java.lang.Object)
                 */
                @Override
                public void apply(final ChangeStreamDocument<Document> document) {
                	// perform applying action
                    if (document != null) SimpleObserve.this.internalObserveApply(document);
                }
            });
        // turn on observing
        this.observed = Boolean.TRUE;
    }

    /**
     * Start observe resume token with all actions
     */
    public final void observeResume() {
        this.observeResume(FULL_OBSERVE_ACTIONS, Boolean.TRUE);
    }
    /**
     * Start observe resume token
     *
     * @param observeActions actions to observe such as: insert/update/replace/delete/invalidate
     */
    public final void observeResume(final String[] observeActions) {
        this.observeResume(observeActions, Boolean.TRUE);
    }
    /**
     * Start observe resume token
     *
     * @param observeActions actions to observe such as: insert/update/replace/delete/invalidate
     * @param observeFirst specify whether applying all present records. Require via
     */
    public final void observeResume(final String[] observeActions, final Boolean observeFirst) {
        if (this.getCollection() == null) {
            throw new IllegalArgumentException("Could not require mongo collection!");
        } else if (this.getObservedAction() == null) {
            throw new IllegalArgumentException("Not found observed action! Please implement getObservedAction method");
        } else if (Boolean.TRUE.equals(this.observed)) { // watched
            return;
        }

        // require collection/ES transport
        MongoCollection<Document> collection = this.getCollection();

        // insert all document for the fisrt running time
        if (Boolean.TRUE.equals(observeFirst) && collection.estimatedDocumentCount() > 0) {
            logger.info("OBSERVE ALL PRESENT DOCUMENTS FROM MONGO FOR FIRST RUNNING TIME!");
            try { this.observeFirst(collection.find().into(new LinkedList<Document>())); }
            catch (Exception e) { logger.error(e.getMessage(), e); }
        }

        // start observing
        String[] arrObserveActions = (observeActions == null || observeActions.length <= 0
                ? FULL_OBSERVE_ACTIONS : observeActions);
        MongoCursor<ChangeStreamDocument<Document>> cursor =
        		collection.watch(asList(Aggregates.match(Filters.in("operationType", asList(arrObserveActions)))))
        		.fullDocument(FullDocument.UPDATE_LOOKUP).iterator();
        boolean hasNext = (cursor != null && cursor.hasNext());
        logger.info("OBSERVE RESUME AFTER [" + String.valueOf(hasNext) + "]");
        if (hasNext) {
	        while(hasNext) {
	        	// parse changed stream document
	        	final ChangeStreamDocument<Document> document = cursor.next();
	        	// perform applying changed stream document
	        	this.internalObserveApply(document);
	        	// require resume token to watch
	        	BsonDocument token = document.getResumeToken();
	        	logger.debug("--> RESUME TOKEN [" + token.toJson() + "}");
	        	// continue to observe
	        	MongoCursor<ChangeStreamDocument<Document>> next =
	        			collection.watch().resumeAfter(token).iterator();
	        	hasNext = (next != null && next.hasNext());
	        	logger.debug("--> CONTINUE [" + String.valueOf(hasNext) + "}");
	        	if (hasNext) {
	        		cursor.close();
	        		cursor = next;
	        	}
	        }
        }
        // close cursor
        if (hasNext) cursor.close();
        // turn on observing
        this.observed = Boolean.TRUE;
    }

    /**
     * Perform applying for observing (internal)
     * @param document {@link ChangeStreamDocument}
     */
    private void internalObserveApply(final ChangeStreamDocument<Document> document) {
    	Assert.notNull(document, "document");
    	// debug
    	logger.debug("ChangedStream:");
    	final Document doc = document.getFullDocument();
    	logger.debug("1. DOCUMENT: " + (doc == null ? "NULL" : doc.toJson()));
    	final UpdateDescription description = document.getUpdateDescription();
    	final BsonDocument updFields = (description == null ? null : description.getUpdatedFields());
    	final List<String> remFields = (description == null ? null : description.getRemovedFields());
    	String removedFields = org.springframework.util.StringUtils.collectionToCommaDelimitedString(remFields);
    	logger.debug("2. UPDATED FIELDS: " + (updFields == null ? "NULL" : updFields.toJson()));
    	logger.debug("3. REMOVED FIELDS: " + (removedFields == null ? "NULL" : removedFields));
    	// run asynchronus
        CompletableFuture.runAsync(
                new Runnable() {

                    /*
                     * (非 Javadoc)
                     * @see java.lang.Runnable#run()
                     */
                    @Override
                    public void run() {
                        try {
                        	// require action
                        	final IObserveAction observeAct =
                        			SimpleObserve.this.getObservedAction();
                        	// apply action
                            observeAct.perform(
                                    document.getOperationType(),
                                    document.getFullDocument(),
                                    document.getUpdateDescription());
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                });
    }

    /**
     * Close ES transport and collection (as destroy)
     */
    public void close() {
        // close ES action
        IObserveAction observeAct = this.getObservedAction();
        if (observeAct != null) {
            try { observeAct.close(); }
            catch (Exception e) {}
            observeAct = null;
        }
        // close mongo connection
        if (this.client != null) {
            try { this.client.close(); }
            catch (Exception e) {}
            this.client = null;
        }
    }
}
