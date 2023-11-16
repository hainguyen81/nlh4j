/*
 * @(#)SqlLiteEntityDao.java 1.0 Oct 5, 2016
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.android.db;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.nlh4j.android.util.ContentValuesUtils;
import org.nlh4j.android.util.LogUtils;
import org.nlh4j.exceptions.ApplicationRuntimeException;
import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.CollectionUtils;
import org.nlh4j.util.StreamUtils;
import org.nlh4j.util.StringUtils;
import org.seasar.doma.Column;
import org.seasar.doma.Id;
import org.seasar.doma.Table;
import org.springframework.util.Assert;

import com.googlecode.openbeans.PropertyDescriptor;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity Data Access Object (DAO) with {@link Table}/{@link org.nlh4j.android.annotation.Table}, {@link Column}/{@link org.nlh4j.android.annotation.Column} SQLite supporter
 *
 * @param <T> the entity class
 * @author Hai Nguyen
 *
 */
@SuppressWarnings("unchecked")
public abstract class SqlLiteEntityDao<T> implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;
    protected final String TAG = this.getClass().getSimpleName();

    /**
     * Interface to map {@link Cursor} to entity
     *
     * @param <T> the entity class
     */
    public static interface EntityMapper<T> extends Serializable {
        /**
         * Map data from {@link Cursor} to entity
         *
         * @param cursor the database {@link Cursor}
         *
         * @return the mapped entity
         */
        T map(Cursor cursor);
    }

    /** SQLite database */
    private SQLiteDatabase database;
    /** entity class */
    private Class<T> entityClass;
    /** entity database catalog */
    private String catalog;
    /** entity database schema */
    private String schema;
    /** entity table name */
    private String tableName;
    /**
     * entity table column names with
     * key: entity field name, value: {@link Column}/{@link org.nlh4j.android.annotation.Column} annotation
     */
    private Map<String, Object> columns;
    /**
     * entity table primary keys with
     * key: entity field name, value: {@link Column}/{@link org.nlh4j.android.annotation.Column} annotation
     */
    private Map<String, Object> primaryKeys;
    /** entity mapper */
    @Getter
    @Setter
    private EntityMapper<T> mapper;

    /**
     * Initialize a new instance of {@link SqlLiteEntityDao}
     *
     * @param database to query
     * @param entityClass the entity class
     * @exception IllegalArgumentException thrown if database/entity class is NULL
     */
    protected SqlLiteEntityDao(SQLiteDatabase database, Class<T> entityClass) {
        this.initialize(database, entityClass);
    }

    /**
     * Initialization
     */
    private void initialize(SQLiteDatabase database, Class<T> entityClass) {
        Assert.notNull(database, "database");
        Assert.notNull(entityClass, "entityClass");
        this.database = database;
        // parse entity meta-data information
        Table tblAnn = BeanUtils.getClassAnnotation(entityClass, Table.class);
        if (tblAnn == null) {
            org.nlh4j.android.annotation.Table tblPerAnn = BeanUtils.getClassAnnotation(
                    entityClass, org.nlh4j.android.annotation.Table.class);
            Assert.notNull(tblPerAnn,
                    "Bean is not a persistence entity! "
                    + "Please declare org.seasar.doma.Table or org.nlh4j.android.annotations.Table annotation");
            this.entityClass = entityClass;
            this.catalog = tblPerAnn.catalog();
            this.schema = tblPerAnn.schema();
            this.tableName = tblPerAnn.name();
        } else {
            this.entityClass = entityClass;
            this.catalog = tblAnn.catalog();
            this.schema = tblAnn.schema();
            this.tableName = tblAnn.name();
        }
        Assert.hasText(this.tableName, "Table");

        // parse column meta-data information
        List<Field> fields = new LinkedList<Field>();
        fields.addAll(Arrays.asList(this.entityClass.getFields()));
        fields.addAll(Arrays.asList(this.entityClass.getDeclaredFields()));
        if (!CollectionUtils.isEmpty(fields)) {
            for(Field field : fields) {
                // column meta-data
                Object colAnn = BeanUtils.getFieldAnnotation(field, Column.class);
                String columnName = null;
                if (colAnn == null) {
                    colAnn = BeanUtils.getPropertyAnnotation(this.entityClass, field.getName(), Column.class);
                }
                if (colAnn == null) {
                    colAnn = BeanUtils.getFieldAnnotation(field, org.nlh4j.android.annotation.Column.class);
                    if (colAnn == null) {
                        colAnn = BeanUtils.getPropertyAnnotation(
                                this.entityClass, field.getName(), org.nlh4j.android.annotation.Column.class);
                    }
                    if (colAnn != null) {
                        org.nlh4j.android.annotation.Column col =
                                BeanUtils.safeType(colAnn, org.nlh4j.android.annotation.Column.class);
                        if (col != null) columnName = col.name();
                    }
                } else {
                    Column col = BeanUtils.safeType(colAnn, Column.class);
                    if (col != null) columnName = col.name();
                }
                if (colAnn == null) continue;
                LogUtils.d("--- Map field {0} with {1} column", new Object[] { field.getName(), columnName });
                this.getColumns().put(field.getName(), colAnn);

                // primary key meta-data
                Object idAnn = BeanUtils.getFieldAnnotation(field, Id.class);
                if (idAnn == null) {
                    idAnn = BeanUtils.getPropertyAnnotation(this.entityClass, field.getName(), Id.class);
                }
                if (idAnn == null) {
                    idAnn = field.getAnnotation(org.nlh4j.android.annotation.Id.class);
                    if (idAnn == null) {
                        idAnn = BeanUtils.getPropertyAnnotation(
                                this.entityClass, field.getName(), org.nlh4j.android.annotation.Id.class);
                    }
                }
                if (idAnn != null) {
                    LogUtils.d("--- Map field {0} with {1} column as primary key", new Object[] { field.getName(), columnName });
                    this.getPrimaryKeys().put(field.getName(), colAnn);
                }
            }
        }
        // check valid any columns
        Assert.notEmpty(this.getColumns(), "Columns");
    }

    /**
     * Get the SQLite database
     *
     * @return the SQLite database
     */
    protected final SQLiteDatabase getDatabase() {
        return this.database;
    }
    /**
     * Get the entity class
     * @return the entity class
     */
    public final Class<T> getEntityClass() {
        return this.entityClass;
    }
    /**
     * Get the entity database catalog
     *
     * @return the entity database catalog
     */
    public final String getCatalog() {
        return catalog;
    }
    /**
     * Get the entity database schema
     *
     * @return the entity database schema
     */
    public final String getSchema() {
        return schema;
    }
    /**
     * Get the entity table name
     *
     * @return the entity table name
     */
    public final String getTableName() {
        return this.tableName;
    }
    /**
     * Get the entity table column names list
     *
     * @return the entity table column names list
     */
    public final List<String> getColumnNames() {
        List<String> columnNames = new LinkedList<String>();
        if (!CollectionUtils.isEmpty(this.columns)) {
            for(final Iterator<String> it = this.columns.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                Object column = this.columns.get(key);
                String columnName = null;
                if (BeanUtils.isInstanceOf(column, Column.class)) {
                    Column col = BeanUtils.safeType(column, Column.class);
                    if (col != null) columnName = col.name();
                } else if (BeanUtils.isInstanceOf(column, org.nlh4j.android.annotation.Column.class)) {
                    org.nlh4j.android.annotation.Column col =
                            BeanUtils.safeType(column, org.nlh4j.android.annotation.Column.class);
                    if (col != null) columnName = col.name();
                }
                if (!StringUtils.hasText(columnName)) columnName = key;
                if (!columnNames.contains(columnName)) {
                    columnNames.add(columnName);
                }
            }
        }
        return columnNames;
    }
    /**
     * Get the entity table column names map with key: entity property name, value: entity table column name
     *
     * @return the entity table column names map with key: entity property name, value: entity table column name
     */
    protected final Map<String, Object> getColumns() {
        if (this.columns == null) {
            this.columns = new LinkedHashMap<String, Object>();
        }
        return this.columns;
    }
    /**
     * Get the entity table primary keys map with key: entity property name, value: entity table column name
     *
     * @return the entity table primary keys map with key: entity property name, value: entity table column name
     */
    protected final Map<String, Object> getPrimaryKeys() {
        if (this.primaryKeys == null) {
            this.primaryKeys = new LinkedHashMap<String, Object>();
        }
        return this.primaryKeys;
    }

    /**
     * Insert the specified entity
     *
     * @param entity entity to insert
     *
     * @return the effected records
     * @exception ApplicationRuntimeException thrown if database is read-only, or inserting fail
     */
    public long insert(T entity) throws ApplicationRuntimeException {
        long[] effected = this.insert((T[]) new Object[] { entity });
        return effected[0];
    }
    /**
     * Insert the specified entity
     *
     * @param entities entities to insert
     *
     * @return the effected records
     * @exception ApplicationRuntimeException thrown if database is read-only, or inserting fail
     */
    public long[] insert(T...entities) throws ApplicationRuntimeException {
        Assert.isTrue(!this.getDatabase().isReadOnly(), "Could not insert while database is read-only!");
        Assert.notEmpty(entities, "entities");
        long[] effectedRecs = new long[entities.length];

        // parse values to insert
        List<ContentValues> valuesLst = new LinkedList<ContentValues>();
        Map<String, Object> columns = this.getColumns();
        for(T entity : entities) {
            ContentValues values = new ContentValues();
            for(final Iterator<String> it = columns.keySet().iterator(); it.hasNext();) {
                String fieldName = it.next();
                Object value = BeanUtils.getFieldValue(entity, fieldName);

                // apply column's value
                Object column = columns.get(fieldName);
                if (BeanUtils.isInstanceOf(column, Column.class)) {
                    Column col = BeanUtils.safeType(column, Column.class);
                    if (col != null) ContentValuesUtils.put(values, col.name(), value);
                } else if (BeanUtils.isInstanceOf(column, org.nlh4j.android.annotation.Column.class)) {
                    org.nlh4j.android.annotation.Column col =
                            BeanUtils.safeType(column, org.nlh4j.android.annotation.Column.class);
                    if (col != null) ContentValuesUtils.put(values, col.name(), value);
                }
            }
            if (values.size() > 0) {
                valuesLst.add(values);
            }
        }

        // check values to insert
        Assert.notEmpty(valuesLst, "values");

        // start transaction
        try {
            this.getDatabase().beginTransaction();
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            throw new ApplicationRuntimeException(e);
        }

        try {
            // insert entity
            int i = 0;
            for(ContentValues values : valuesLst) {
                effectedRecs[i] = this.getDatabase().insert(this.getTableName(), null, values);
                i++;
            }
            // commit transaction
            if (this.getDatabase().inTransaction()) {
                this.getDatabase().setTransactionSuccessful();
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            throw new ApplicationRuntimeException(e);
        }

        // end transaction
        try {
            if (this.getDatabase().inTransaction()) {
                this.getDatabase().endTransaction();
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            throw new ApplicationRuntimeException(e);
        }

        // return effected records
        return effectedRecs;
    }

    /**
     * Update the specified entity
     *
     * @param entity entity to update
     *
     * @return the effected records
     * @exception ApplicationRuntimeException thrown if database is read-only, or updating fail
     */
    public long update(T entity) throws ApplicationRuntimeException {
        long[] effected = update((T[]) new Object[] { entity });
        return effected[0];
    }
    /**
     * Update the specified entity
     *
     * @param entities entities to update
     *
     * @return the effected records
     * @exception ApplicationRuntimeException thrown if database is read-only, or updating fail
     */
    public long[] update(T...entities) throws ApplicationRuntimeException {
        Assert.isTrue(!this.getDatabase().isReadOnly(), "Could not update while database is read-only!");
        Assert.notEmpty(entities, "entities");
        long[] effectedRecs = new long[entities.length];

        // parse values to update
        Map<ContentValues, ContentValues> valuesLst = new LinkedHashMap<ContentValues, ContentValues>();
        Map<String, Object> columns = this.getColumns();
        Map<String, Object> pkcolumns = this.getPrimaryKeys();
        for(T entity : entities) {
            ContentValues values = new ContentValues();
            ContentValues args = new ContentValues();
            for(final Iterator<String> it = columns.keySet().iterator(); it.hasNext();) {
                String fieldName = it.next();
                Object value = BeanUtils.getFieldValue(entity, fieldName);

                // check primary key
                if (pkcolumns.containsKey(fieldName)) {
                    // apply primary key's value
                    Object column = pkcolumns.get(fieldName);
                    if (BeanUtils.isInstanceOf(column, Column.class)) {
                        Column col = BeanUtils.safeType(column, Column.class);
                        if (col != null) ContentValuesUtils.put(args, col.name(), value);
                    } else if (BeanUtils.isInstanceOf(column, org.nlh4j.android.annotation.Column.class)) {
                        org.nlh4j.android.annotation.Column col =
                                BeanUtils.safeType(column, org.nlh4j.android.annotation.Column.class);
                        if (col != null) ContentValuesUtils.put(args, col.name(), value);
                    }
                } else {
                    // apply column's value
                    Object column = columns.get(fieldName);
                    if (BeanUtils.isInstanceOf(column, Column.class)) {
                        Column col = BeanUtils.safeType(column, Column.class);
                        if (col != null) ContentValuesUtils.put(values, col.name(), value);
                    } else if (BeanUtils.isInstanceOf(column, org.nlh4j.android.annotation.Column.class)) {
                        org.nlh4j.android.annotation.Column col =
                                BeanUtils.safeType(column, org.nlh4j.android.annotation.Column.class);
                        if (col != null) ContentValuesUtils.put(values, col.name(), value);
                    }
                }
            }
            if (args.size() > 0 && values.size() > 0) {
                valuesLst.put(args, values);
            }
        }

        // check values to update
        Assert.notEmpty(valuesLst, "values");

        // start transaction
        try {
            this.getDatabase().beginTransaction();
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            throw new ApplicationRuntimeException(e);
        }

        try {
            // update entity
            int i = 0;
            for(final Iterator<ContentValues> it = valuesLst.keySet().iterator(); it.hasNext();) {
                ContentValues args = it.next();
                ContentValues values = valuesLst.get(args);
                StringBuffer conds = new StringBuffer();
                List<String> condValues = new LinkedList<String>();
                for(String key : args.keySet()) {
                    if (conds.length() > 0) {
                        conds.append(" AND ");
                    }
                    conds.append(key);
                    conds.append(" = ?");
                    condValues.add(args.getAsString(key));
                }
                effectedRecs[i] = this.getDatabase().update(
                        this.getTableName(),
                        values,
                        conds.toString(),
                        condValues.toArray(new String[condValues.size()]));
                i++;
            }
            // commit transaction
            if (this.getDatabase().inTransaction()) {
                this.getDatabase().setTransactionSuccessful();
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            throw new ApplicationRuntimeException(e);
        }

        // end transaction
        try {
            if (this.getDatabase().inTransaction()) {
                this.getDatabase().endTransaction();
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            throw new ApplicationRuntimeException(e);
        }

        // return effected records
        return effectedRecs;
    }

    /**
     * Delete the specified entity
     *
     * @param entity entity to delete
     *
     * @return the effected records
     * @exception ApplicationRuntimeException thrown if database is read-only, or deleting fail
     */
    public long delete(T entity) throws ApplicationRuntimeException {
        long[] effected = delete((T[]) new Object[] { entity });
        return effected[0];
    }
    /**
     * Delete the specified entity
     *
     * @param entities entities to delete
     *
     * @return the effected records
     * @exception ApplicationRuntimeException thrown if database is read-only, or deleting fail
     */
    public long[] delete(T...entities) throws ApplicationRuntimeException {
        Assert.isTrue(!this.getDatabase().isReadOnly(), "Could not delete while database is read-only!");
        Assert.notEmpty(entities, "entities");
        long[] effectedRecs = new long[entities.length];

        // parse values to delete
        List<ContentValues> valuesLst = new LinkedList<ContentValues>();
        Map<String, Object> columns = this.getPrimaryKeys();
        for(T entity : entities) {
            ContentValues values = new ContentValues();
            for(final Iterator<String> it = columns.keySet().iterator(); it.hasNext();) {
                String fieldName = it.next();
                Object value = BeanUtils.getFieldValue(entity, fieldName);

                // apply primary key's value
                Object column = columns.get(fieldName);
                if (BeanUtils.isInstanceOf(column, Column.class)) {
                    Column col = BeanUtils.safeType(column, Column.class);
                    if (col != null) ContentValuesUtils.put(values, col.name(), value);
                } else if (BeanUtils.isInstanceOf(column, org.nlh4j.android.annotation.Column.class)) {
                    org.nlh4j.android.annotation.Column col =
                            BeanUtils.safeType(column, org.nlh4j.android.annotation.Column.class);
                    if (col != null) ContentValuesUtils.put(values, col.name(), value);
                }
            }
            if (values.size() > 0) {
                valuesLst.add(values);
            }
        }

        // check values to delete
        Assert.notEmpty(valuesLst, "values");

        // start transaction
        try {
            this.getDatabase().beginTransaction();
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            throw new ApplicationRuntimeException(e);
        }

        try {
            // delete entity
            int i = 0;
            for(ContentValues values : valuesLst) {
                StringBuffer conds = new StringBuffer();
                List<String> condValues = new LinkedList<String>();
                for(String key : values.keySet()) {
                    if (conds.length() > 0) {
                        conds.append(" AND ");
                    }
                    conds.append(key);
                    conds.append(" = ?");
                    condValues.add(values.getAsString(key));
                }
                effectedRecs[i] = this.getDatabase().delete(
                        this.getTableName(),
                        conds.toString(),
                        condValues.toArray(new String[condValues.size()]));
                i++;
            }
            // commit transaction
            if (this.getDatabase().inTransaction()) {
                this.getDatabase().setTransactionSuccessful();
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            throw new ApplicationRuntimeException(e);
        }

        // end transaction
        try {
            if (this.getDatabase().inTransaction()) {
                this.getDatabase().endTransaction();
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            throw new ApplicationRuntimeException(e);
        }

        // return effected records
        return effectedRecs;
    }

    /**
     * Get a boolean value indicating that the specified values of the primary keys is existed in database
     *
     * @param entity the entity that has been applied values for primary keys to check
     *
     * @return true for existed; else false
     * @exception ApplicationRuntimeException thrown if processing fail
     */
    public boolean exist(T entity) throws ApplicationRuntimeException {
        Assert.notNull(entity, "entity");
        boolean existed = false;

        // parse values to update
        Map<String, Object> columns = this.getPrimaryKeys();
        ContentValues values = new ContentValues();
        for(final Iterator<String> it = columns.keySet().iterator(); it.hasNext();) {
            String fieldName = it.next();
            Object value = BeanUtils.getFieldValue(entity, fieldName);
            // if valid primary key values
            if (value != null) {
                // apply primary key's value
                Object column = columns.get(fieldName);
                if (BeanUtils.isInstanceOf(column, Column.class)) {
                    Column col = BeanUtils.safeType(column, Column.class);
                    if (col != null) ContentValuesUtils.put(values, col.name(), value);
                } else if (BeanUtils.isInstanceOf(column, org.nlh4j.android.annotation.Column.class)) {
                    org.nlh4j.android.annotation.Column col =
                            BeanUtils.safeType(column, org.nlh4j.android.annotation.Column.class);
                    if (col != null) ContentValuesUtils.put(values, col.name(), value);
                }
            }
        }

        // if has primary key values
        if (values.size() > 0) {
            // prepare WHERE statement
            StringBuffer conds = new StringBuffer();
            List<String> condValues = new LinkedList<String>();
            for(String key : values.keySet()) {
                if (conds.length() > 0) {
                    conds.append(" AND ");
                }
                conds.append(key);
                conds.append(" = ?");
                condValues.add(values.getAsString(key));
            }

            // execute query
            try {
                Cursor cursor = this.getDatabase().query(
                        this.getTableName(),
                        new String[] { "COUNT(1)" },
                        conds.toString(),
                        condValues.toArray(new String[condValues.size()]),
                        null, null, null);
                long count = 0;
                if (cursor != null && cursor.getCount() > 0) {
                    if (!cursor.isFirst()) cursor.moveToFirst();
                    count = cursor.getLong(0);
                }
                existed = (count > 0);
            } catch (Exception e) {
                LogUtils.e(e.getMessage(), e);
                throw new ApplicationRuntimeException(e);
            }
        }

        // return existed value
        return existed;
    }

    /**
     * Get a entities list by the specified conditions
     *
     * @param conditions the conditions to query with key: entity field name, value: comparison value
     * @param orderBy order by clause, such as: [ABC asc, DEF desc]
     *
     * @return entities list or empty/NULL
     * @exception ApplicationRuntimeException thrown if processing fail
     */
    public List<T> find(Map<String, Object> conditions, String orderBy) throws ApplicationRuntimeException {
        // parse values to query
        List<T> entities = new LinkedList<T>();
        ContentValues values = new ContentValues();
        if (!CollectionUtils.isEmpty(conditions)) {
            Map<String, Object> columns = this.getColumns();
            for(final Iterator<String> it = conditions.keySet().iterator(); it.hasNext();) {
                String key = it.next();
                Object value = conditions.get(key);
                if (!StringUtils.hasText(key)) continue;
                Assert.isTrue(
                        columns.containsKey(key),
                        MessageFormat.format("The specified property {0} has not been mapped with table column", key));
                // apply column's value
                Object column = columns.get(key);
                if (BeanUtils.isInstanceOf(column, Column.class)) {
                    Column col = BeanUtils.safeType(column, Column.class);
                    if (col != null) ContentValuesUtils.put(values, col.name(), value);
                } else if (BeanUtils.isInstanceOf(column, org.nlh4j.android.annotation.Column.class)) {
                    org.nlh4j.android.annotation.Column col =
                            BeanUtils.safeType(column, org.nlh4j.android.annotation.Column.class);
                    if (col != null) ContentValuesUtils.put(values, col.name(), value);
                }
            }
        }

        // prepare SELECT statement
        List<String> dbColumns = new LinkedList<String>();
        Map<String, Object> columns = this.getColumns();
        for(final Iterator<String> it = columns.keySet().iterator(); it.hasNext();) {
            String key = it.next();
            Object column = columns.get(key);
            if (BeanUtils.isInstanceOf(column, Column.class)) {
                Column col = BeanUtils.safeType(column, Column.class);
                if (col != null) dbColumns.add(col.name());
            } else if (BeanUtils.isInstanceOf(column, org.nlh4j.android.annotation.Column.class)) {
                org.nlh4j.android.annotation.Column col =
                        BeanUtils.safeType(column, org.nlh4j.android.annotation.Column.class);
                if (col != null) dbColumns.add(col.name());
            }
        }

        // prepare WHERE statement
        StringBuffer conds = new StringBuffer();
        List<String> condValues = new LinkedList<String>();
        if (values.size() > 0) {
            for(String key : values.keySet()) {
                if (conds.length() > 0) {
                    conds.append(" AND ");
                }
                conds.append(key);
                conds.append(" = ?");
                condValues.add(values.getAsString(key));
            }
        }

        // mapping data
        Cursor cursor = null;
        try {
            cursor = this.getDatabase().query(
                    this.getTableName(),
                    dbColumns.toArray(new String[dbColumns.size()]),
                    (values.size() > 0 ? conds.toString() : null),
                    (values.size() > 0 ? condValues.toArray(new String[condValues.size()]) : null),
                    null, null, orderBy);
            // if valid data
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    T entity = null;
                    if (this.getMapper() != null) {
                        entity = this.getMapper().map(cursor);
                    } else {
                        entity = this.getEntityClass().newInstance();
                        for(final Iterator<String> it = columns.keySet().iterator(); it.hasNext();) {
                            String key = it.next();
                            String colName = null;
                            Object column = columns.get(key);
                            if (BeanUtils.isInstanceOf(column, Column.class)) {
                                Column col = BeanUtils.safeType(column, Column.class);
                                if (col != null) colName = col.name();
                            } else if (BeanUtils.isInstanceOf(column, org.nlh4j.android.annotation.Column.class)) {
                                org.nlh4j.android.annotation.Column col =
                                        BeanUtils.safeType(column, org.nlh4j.android.annotation.Column.class);
                                if (col != null) colName = col.name();
                            }
                            // check field/property
                            if (!StringUtils.hasText(key) || !StringUtils.hasText(colName)) continue;

                            // parse field/property to apply value
                            int colIdx = cursor.getColumnIndex(colName);
                            Field field = BeanUtils.getField(this.getEntityClass(), key);
                            PropertyDescriptor pd = BeanUtils.getProperty(this.getEntityClass(), key);
                            boolean useField = (Modifier.isPublic(field.getModifiers())
                                    || (Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())));
                            Class<?> propertyType = null;
                            if (useField) {
                                propertyType = field.getType();
                            } else {
                                propertyType = pd.getPropertyType();
                            }
                            // check if primitive type
                            propertyType = BeanUtils.getPrimitiveType(propertyType);
                            LogUtils.d("--- Detect field type {0} - {1}", new Object[] { key, propertyType.getName() });

                            // apply value by type
                            Object dbValue = null;
                            LogUtils.d("--- Detect database column index {0} - {1}", new Object[] { colName, String.valueOf(colIdx) });
                            if (!cursor.isNull(colIdx)) {
                                if (BeanUtils.isInstanceOf(propertyType, String.class)) {
                                    dbValue = cursor.getString(colIdx);
                                } else if (BeanUtils.isInstanceOf(propertyType, Integer.class)) {
                                    dbValue = cursor.getInt(colIdx);
                                } else if (BeanUtils.isInstanceOf(propertyType, Boolean.class)) {
                                    Integer intVal = cursor.getInt(colIdx);
                                    String sVal = cursor.getString(colIdx);
                                    if ((new Integer(1)).equals(intVal)) {
                                        dbValue = Boolean.TRUE;
                                    } else if ((new Integer(1)).equals(intVal)) {
                                        dbValue = Boolean.FALSE;
                                    } else {
                                        dbValue = Boolean.parseBoolean(sVal);
                                    }
                                } else if (BeanUtils.isInstanceOf(propertyType, Byte.class)) {
                                    dbValue = Byte.parseByte(cursor.getString(colIdx));
                                } else if (BeanUtils.isInstanceOf(propertyType, byte[].class)) {
                                    dbValue = cursor.getBlob(colIdx);
                                } else if (BeanUtils.isInstanceOf(propertyType, Double.class)) {
                                    dbValue = cursor.getDouble(colIdx);
                                } else if (BeanUtils.isInstanceOf(propertyType, BigDecimal.class)) {
                                    dbValue = new BigDecimal(cursor.getDouble(colIdx));
                                } else if (BeanUtils.isInstanceOf(propertyType, Float.class)) {
                                    dbValue = cursor.getFloat(colIdx);
                                } else if (BeanUtils.isInstanceOf(propertyType, Long.class)) {
                                    dbValue = cursor.getLong(colIdx);
                                } else if (BeanUtils.isInstanceOf(propertyType, BigInteger.class)) {
                                    dbValue = new BigInteger(cursor.getBlob(colIdx));
                                } else if (BeanUtils.isInstanceOf(propertyType, Short.class)) {
                                    dbValue = cursor.getShort(colIdx);
                                } else if (BeanUtils.isInstanceOf(propertyType, Date.class)) {
                                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                    String sVal = cursor.getString(colIdx);
                                    dbValue = df.parse(sVal);
                                } else {
                                    // try value to bytes array
                                    ByteArrayInputStream bis = null;
                                    ObjectInputStream ois = null;
                                    try {
                                        bis = new ByteArrayInputStream(cursor.getBlob(colIdx));
                                        ois = new ObjectInputStream(bis);
                                        dbValue = ois.readObject();
                                    } catch (Exception e) {
                                        LogUtils.e(e.getMessage(), e);
                                        throw new ApplicationRuntimeException(
                                                new IllegalArgumentException("Unmapped"));
                                    } finally {
                                        StreamUtils.closeQuitely(bis);
                                        StreamUtils.closeQuitely(ois);
                                    }
                                }
                            }

                            // apply entity field/property value
                            if (!BeanUtils.setFieldValue(entity, key, dbValue)) {
                                throw new ApplicationRuntimeException(
                                        new IllegalArgumentException(MessageFormat.format(
                                                "Could not apply database value to field {0}", key)));
                            }
                        }
                    }
                    if (entity != null) {
                        entities.add(entity);
                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            LogUtils.e(e.getMessage(), e);
            if (!CollectionUtils.isEmpty(entities)) entities.clear();
            throw new ApplicationRuntimeException(e);
        } finally {
            StreamUtils.closeQuitely(cursor);
        }

        // return existed value
        return entities;
    }
    /**
     * Get the first occurred entity by the specified conditions
     *
     * @param conditions the conditions to query with key: entity field name, value: comparison value
     *
     * @return the first occurred entity by the specified conditions
     * @exception ApplicationRuntimeException thrown if processing fail
     */
    public T findOne(Map<String, Object> conditions) throws ApplicationRuntimeException {
        List<T> entities = this.find(conditions, null);
        return (!CollectionUtils.isEmpty(entities) ? entities.get(0) : null);
    }
}
