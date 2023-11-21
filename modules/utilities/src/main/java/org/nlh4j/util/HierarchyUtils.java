/*
 * @(#)HierarchyUtils.java
 * Copyright 2017 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.Assert;

/**
 * Hierarchy utilities
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
@SuppressWarnings("unchecked")
public final class HierarchyUtils implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * Build hierarchical entities list
     *
     * @param <T> entity type
     * @param <K> data type
     * @param dataLst to build
     * @param dataClass entity class
     * @param fieldName field name to find childs data base on {@code parentFieldName}
     * @param parentFieldName field name to find parent data base on {@code fieldName}
     * @param childsListFieldName field name to add childs data (must be collection type)
     * @param rootValue root value to find root nodes
     *
     * @return the hierarchical entities list or NULL if failed/empty data
     * @throws Exception thrown if failed
     */
    public static final <T, K> List<T> buildHierarchicalList(
            List<K> dataLst, Class<T> dataClass,
            String fieldName, String parentFieldName, String childsListFieldName,
            Object rootValue) throws Exception {
        // check parameters
        Assert.notNull(dataClass, "data-class");
        Assert.hasText(fieldName, "field-name");
        Assert.hasText(parentFieldName, "parent-field-name");
        Assert.hasText(childsListFieldName, "childs-list-field-name");

        // build hierarchy
        List<T> recursiveLst = null;
        if (!CollectionUtils.isEmpty(dataLst)) {
            // find root nodes
            recursiveLst = new LinkedList<T>();
            List<K> exDataLst = new LinkedList<K>();
            for(K data : dataLst) {
                Object parentFieldVal = BeanUtils.getFieldValue(data, parentFieldName);
                if ((rootValue == null && parentFieldVal == null)
                        || (rootValue != null && rootValue == parentFieldVal)
                        || (rootValue != null && rootValue.equals(parentFieldVal))) {
                    T nodeInst = BeanUtils.copyBean(data, dataClass);
                    Object childsList = BeanUtils.getFieldValue(nodeInst, childsListFieldName);
                    if (childsList == null) childsList = new LinkedList<T>();
                    BeanUtils.setFieldValue(nodeInst, childsListFieldName, childsList);
                    recursiveLst.add(nodeInst);
                    exDataLst.add(data);
                }
            }

            // find children nodes
            if (!CollectionUtils.isEmpty(recursiveLst)) {
                for(T root : recursiveLst) {
                    internalBuildHierarchicalList(
                            root, dataLst, dataClass,
                            fieldName, parentFieldName, childsListFieldName,
                            exDataLst);
                }
            } else LogUtils.logDebug("Not found any root nodes!!!");
        }
        return recursiveLst;
    }
    /**
     * Build hierarchical entities list
     *
     * @param <T> entity type
     * @param <K> data type
     * @param parent parent data
     * @param dataLst to build
     * @param dataClass entity class
     * @param fieldName field name to find childs data base on {@code parentFieldName}
     * @param parentFieldName field name to find parent data base on {@code fieldName}
     * @param childsListFieldName field name to add childs data (must be collection type)
     * @param exDataLst excluded entities list
     *
     * @return the hierarchical entities list or NULL if failed/empty data
     * @throws Exception thrown if failed
     */
    private static final <T, K> void internalBuildHierarchicalList(
            T parent, List<K> dataLst, Class<T> dataClass,
            String fieldName, String parentFieldName, String childsListFieldName,
            List<K> exDataLst) throws Exception {
        // check parameters
        Assert.notNull(parent, "parent-data");
        Assert.notNull(dataClass, "data-class");
        Assert.hasText(fieldName, "field-name");
        Assert.hasText(parentFieldName, "parent-field-name");
        Assert.hasText(childsListFieldName, "childs-list-field-name");
        if (!CollectionUtils.isEmpty(dataLst)
                && CollectionUtils.getSize(exDataLst) < CollectionUtils.getSize(dataLst)) {
            if (exDataLst == null) exDataLst = new LinkedList<K>();
            for(K data : dataLst) {
                if (exDataLst.contains(data)) continue;
                Object fieldVal = BeanUtils.getFieldValue(parent, fieldName);
                Object parentFieldVal = BeanUtils.getFieldValue(data, parentFieldName);
                // root nodes
                if ((fieldVal == null && parentFieldVal == null)
                        || (fieldVal != null && fieldVal == parentFieldVal)
                        || (fieldVal != null && fieldVal.equals(parentFieldVal))) {
                    Object childsList = BeanUtils.getFieldValue(parent, childsListFieldName);
                    if (childsList == null) childsList = new LinkedList<T>();
                    T nodeInst = BeanUtils.copyBean(data, dataClass);
                    ((Collection<T>) childsList).add(nodeInst);
                    BeanUtils.setFieldValue(parent, childsListFieldName, childsList);
                    exDataLst.add(data);

                    // call recursively
                    internalBuildHierarchicalList(
                            nodeInst, dataLst, dataClass,
                            fieldName, parentFieldName, childsListFieldName,
                            exDataLst);
                }
            }
        }
    }
}
