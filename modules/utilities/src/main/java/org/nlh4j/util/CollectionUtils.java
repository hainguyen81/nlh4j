/*
 * @(#)CollectionUtils.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.util.Assert;

/**
 * {@link Collection}, {@link List}, {@link Map}, {@link Set}, {@link Object}[], {@link Array} utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 *
 */
public final class CollectionUtils implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1L;

    // -------------------------------------------------
    // COLLECTION
    // -------------------------------------------------

    /**
     * Find the common element type of the given Collection, if any.
     * Alias of {@link org.springframework.util.CollectionUtils#findCommonElementType(Collection)} function
     *
     * @param collection to check
     *
     * @return the common element type, or null if no clear common type has been found (or the collection was empty)
     */
    public static Class<?> findCommonElementType(Collection<?> collection) {
        return (collection == null || isEmpty(collection)
                ? null : org.springframework.util.CollectionUtils.findCommonElementType(collection));
    }
    /**
     * Return true if the supplied collection is null or empty. Otherwise, return false.
     * Alias of {@link org.springframework.util.CollectionUtils#isEmpty(Collection)} function
     *
     * @param collection to check
     *
     * @return whether the given collection is empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return org.springframework.util.CollectionUtils.isEmpty(collection);
    }
    /**
     * Get the size of specified collection
     *
     * @param collection to parse
     *
     * @return size of collection or 0 if empty
     */
    public static int getSize(Collection<?> collection) {
        return (collection == null || collection.size() <= 0 ? 0 : collection.size());
    }
    /**
     * Get a boolean value indicating the specified collection size whether is equals with the specified size
     *
     * @param collection the collection to check
     * @param size the size to compare with collection's size
     *
     * @return true for equals; else false
     */
    public static boolean isElementsNumber(Collection<?> collection, int size) {
        return (!isEmpty(collection) && collection.size() == size);
    }
    /**
     * Get a boolean value indicating the specified collection size whether is greater than with the specified size
     *
     * @param collection the collection to check
     * @param size the size to compare with collection's size
     * @param equals specify whether checking collection's size equals with the specified size
     *
     * @return true for equals or greater than the specified size; else false
     */
    public static boolean isElementsNumberGreaterThan(Collection<?> collection, int size, boolean equals) {
        return (!isEmpty(collection)
                && ((equals && collection.size() >= size) || (!equals && collection.size() > size)));
    }
    /**
     * Get a boolean value indicating the specified collection size whether is less than with the specified size
     *
     * @param collection the collection to check
     * @param size the size to compare with collection's size
     * @param equals specify whether checking collection's size equals with the specified size
     *
     * @return true for equals or less than the specified size; else false
     */
    public static boolean isElementsNumberLessThan(Collection<?> collection, int size, boolean equals) {
        return (!isEmpty(collection)
                && ((equals && collection.size() <= size) || (!equals && collection.size() < size)));
    }

    /**
     * Gets a boolean value indicating that specified object whether is collection
     *
     * @param collection
     *      the object to check
     *
     * @return true for collection; else false
     */
    public static boolean isCollection(Object collection) {
        return BeanUtils.isInstanceOf(collection, Collection.class);
    }
    /**
     * Gets a boolean value indicating that specified object whether is collection
     * of the item class.
     * <b><i>(* NOTE: In empty collection, this function not accepts the empty collection)</i></b>
     *
     * @param collection
     *      the object to check
     * @param itemClass
     *      the item class to check
     *
     * @return true for collection; else false
     */
    public static boolean isCollectionOf(Object collection, Class<?> itemClass) {
        return (itemClass != null
                // is not a empty collection (is a collection and not empty)
                && !isEmptyCollection(collection)
                // element type is the specified class
                && BeanUtils.isInstanceOf(findCommonElementType((Collection<?>) collection), itemClass));
    }
    /**
     * Gets a boolean value indicating that specified object whether is collection and empty (or null) or not a collection
     *
     * @param collection
     *      the object to check
     *
     * @return true for empty/null collection or not a collection; else false
     */
    public static boolean isEmptyCollection(Object collection) {
        return (!isCollection(collection) || isEmpty((Collection<?>) collection));
    }

    /**
     * Get the first occurred index of the specified value in collection
     *
     * @param collection to check
     * @param value to find by {@link Object#equals(Object)}
     *
     * @return index of value in collection (base on 0) or -1 if not found
     */
    public static int indexOf(Collection<?> collection, Object value) {
        if (!isEmpty(collection) && collection.contains(value)) {
        	int idx = -1;
            for(final Iterator<?> it = collection.iterator(); it.hasNext();) {
                Object item = it.next();
                idx++;
                if (item == null && value == null) break;
                if (item != null && value != null && (item == value || item.equals(value))) {
                	return idx;
                }
            }
        }
        return -1;
    }
    /**
     * Get the first occurred index of the specified value in collection/array
     *
     * @param collOrArray to check
     * @param value to find by {@link Object#equals(Object)}
     *
     * @return index of value in collection/array (base on 0) or -1 if not found
     */
    public static int indexOf(Object collOrArray, Object value) {
        // if collection
        if (isCollection(collOrArray) && !isEmpty(collOrArray)) {
            Collection<?> objColl = (Collection<?>) collOrArray;
            return indexOf(objColl, value);

            // if array
        } else if (isArray(collOrArray) && !isEmptyArray(collOrArray)) {
            int size = getSize(collOrArray);
            for(int i = 0; i < size; i++) {
                Object item = Array.get(collOrArray, i);
                if ((item == null && value == null)
                        || (item != null && value != null && (item == value || item.equals(value)))) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Get the item at the specified index in collection
     *
     * @param collection to get
     * @param idx of item. base on 0
     *
     * @return the item at the specified index of NULL if not found
     */
    public static Object get(Collection<?> collection, int idx) {
    	int size = getSize(collection);
        if (!isEmpty(collection) && 0 <= idx && idx < size) {
            for(final Iterator<?> it = collection.iterator(); it.hasNext();) {
                Object item = it.next();
                if (idx <= 0) return item;
                idx--;
            }
        }
        return null;
    }
    /**
     * Get the item at the specified index in collection
     *
     * @param <T> item class type
     * @param collection to get
     * @param idx index of item. base on 0
     * @param itemClass to convert
     *
     * @return the item at the specified index of NULL if not found
     */
    public static <T> T get(Collection<?> collection, int idx, Class<T> itemClass) {
    	return BeanUtils.safeType(get(collection, idx), itemClass);
    }
    /**
     * Get the item at the specified index in collection/array
     *
     * @param collOrArray to check
     * @param idx index of item. base on 0
     *
     * @return the item at the specified index of NULL if not found
     */
    public static Object get(Object collOrArray, int idx) {
        // if collection
        if (isCollection(collOrArray) && !isEmpty(collOrArray)) {
        	Collection<?> objColl = (Collection<?>) collOrArray;
        	return get(objColl, idx);

            // if array
        } else if (isArray(collOrArray) && !isEmptyArray(collOrArray)) {
            int size = getSize(collOrArray);
            return (0 <= idx && idx < size ? Array.get(collOrArray, idx) : null);
        }
        return null;
    }

    /**
     * Get the item at the specified index in collection/array
     *
     * @param <T> item class type
     * @param collOrArray to check
     * @param idx index of item. base on 0
     * @param itemClass to convert
     *
     * @return the item at the specified index of NULL if not found
     */
    public static <T> T get(Object collOrArray, int idx, Class<T> itemClass) {
    	return BeanUtils.safeType(get(collOrArray, idx), itemClass);
    }

    // -------------------------------------------------
    // LIST
    // -------------------------------------------------

    /**
     * Return true if the supplied list is null or empty. Otherwise, return false.
     * Alias of {@link org.springframework.util.CollectionUtils#isEmpty(Collection)} function
     *
     * @param list to check
     *
     * @return whether the given list is empty
     */
    public static boolean isEmpty(List<?> list) {
        return org.springframework.util.CollectionUtils.isEmpty(list);
    }
    /**
     * Get the size of specified list
     *
     * @param list to parse
     *
     * @return size of list or 0 if empty
     */
    public static int getSize(List<?> list) {
        return (list == null || list.size() <= 0 ? 0 : list.size());
    }
    /**
     * Get a boolean value indicating the specified list size whether is equals with the specified size
     *
     * @param list the list to check
     * @param size the size to compare with list's size
     *
     * @return true for equals; else false
     */
    public static boolean isElementsNumber(List<?> list, int size) {
        return (!isEmpty(list) && list.size() == size);
    }
    /**
     * Get a boolean value indicating the specified list size whether is greater than with the specified size
     *
     * @param list the list to check
     * @param size the size to compare with list's size
     * @param equals specify whether checking list's size equals with the specified size
     *
     * @return true for equals or greater than the specified size; else false
     */
    public static boolean isElementsNumberGreaterThan(List<?> list, int size, boolean equals) {
        return (!isEmpty(list)
                && ((equals && list.size() >= size) || (!equals && list.size() > size)));
    }
    /**
     * Get a boolean value indicating the specified list size whether is less than with the specified size
     *
     * @param list the list to check
     * @param size the size to compare with list's size
     * @param equals specify whether checking list's size equals with the specified size
     *
     * @return true for equals or less than the specified size; else false
     */
    public static boolean isElementsNumberLessThan(List<?> list, int size, boolean equals) {
        return (!isEmpty(list)
                && ((equals && list.size() <= size) || (!equals && list.size() < size)));
    }

    /**
     * Get the first occurred index of the specified value in list
     *
     * @param list to check
     * @param value to find by {@link List#indexOf(Object)}
     *
     * @return index of value in list (base on 0) or -1 if not found
     */
    public static int indexOf(List<?> list, Object value) {
        return (isEmpty(list) ? -1 : list.indexOf(value));
    }

    /**
     * Get the item at the specified index in list
     *
     * @param list to check
     * @param idx of item. base on 0
     *
     * @return the item at the specified index or NULL if not found
     */
    public static Object get(List<?> list, int idx) {
    	int size = getSize(list);
        return (0 <= idx && idx < size ? list.get(idx) : null);
    }
    /**
     * Get the item at the specified index in list
     *
     * @param <T> item class type
     * @param list to check
     * @param idx of item. base on 0
     * @param itemClass to convert
     *
     * @return the item at the specified index or NULL if not found
     */
    public static <T> T get(List<?> list, int idx, Class<T> itemClass) {
        return BeanUtils.safeType(get(list, idx), itemClass);
    }

    // -------------------------------------------------
    // MAP
    // -------------------------------------------------

    /**
     * Return true if the supplied map is null or empty. Otherwise, return false.
     * Alias of {@link org.springframework.util.CollectionUtils#isEmpty(Map)} function
     *
     * @param map to check
     *
     * @return whether the given map is empty
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return org.springframework.util.CollectionUtils.isEmpty(map);
    }
    /**
     * Get the size of specified map
     *
     * @param map to parse
     *
     * @return size of map or 0 if empty
     */
    public static int getSize(Map<?, ?> map) {
        return (map == null || map.size() <= 0 ? 0 : map.size());
    }
    /**
     * Get a boolean value indicating the specified map size whether is equals with the specified size
     *
     * @param map the map to check
     * @param size the size to compare with map's size
     *
     * @return true for equals; else false
     */
    public static boolean isElementsNumber(Map<?, ?> map, int size) {
        return (!isEmpty(map) && map.size() == size);
    }
    /**
     * Get a boolean value indicating the specified map size whether is greater than with the specified size
     *
     * @param map the map to check
     * @param size the size to compare with map's size
     * @param equals specify whether checking map's size equals with the specified size
     *
     * @return true for equals or greater than the specified size; else false
     */
    public static boolean isElementsNumberGreaterThan(Map<?, ?> map, int size, boolean equals) {
        return (!isEmpty(map)
                && ((equals && map.size() >= size) || (!equals && map.size() > size)));
    }
    /**
     * Get a boolean value indicating the specified map size whether is less than with the specified size
     *
     * @param map the map to check
     * @param size the size to compare with map's size
     * @param equals specify whether checking map's size equals with the specified size
     *
     * @return true for equals or less than the specified size; else false
     */
    public static boolean isElementsNumberLessThan(Map<?, ?> map, int size, boolean equals) {
        return (!isEmpty(map)
                && ((equals && map.size() <= size) || (!equals && map.size() < size)));
    }

    /**
     * Gets a boolean value indicating that specified object whether is map
     *
     * @param map
     *      the object to check
     *
     * @return true for map; else false
     */
    public static boolean isMap(Object map) {
        return BeanUtils.isInstanceOf(map, Map.class);
    }
    /**
     * Gets a boolean value indicating that specified object whether is map
     * of the key and value classes.
     * <b><i>(* NOTE: In empty map, this function not accepts the empty map)</i></b>
     *
     * @param map
     *      the object to check
     * @param keyClass
     *      the key class to check
     * @param valueClass
     *      the value class to check
     *
     * @return true for map; else false
     */
    public static boolean isMapOf(Object map, Class<?> keyClass, Class<?> valueClass) {
        return (keyClass != null && valueClass != null
                // is not a empty map (is a map and not empty)
                && !isEmptyMap(map)
                // element key is the specified key class
                && BeanUtils.isInstanceOf(findCommonElementType(((Map<?, ?>) map).keySet()), keyClass)
                // element value is the specified value class
                && BeanUtils.isInstanceOf(findCommonElementType(((Map<?, ?>) map).values()), valueClass)
        );
    }
    /**
     * Gets a boolean value indicating that specified object whether is map
     * of the key class.
     * <b><i>(* NOTE: In empty map, this function not accepts the empty map)</i></b>
     *
     * @param map
     *      the object to check
     * @param keyClass
     *      the key class to check
     *
     * @return true for map; else false
     */
    public static boolean isMapOfKey(Object map, Class<?> keyClass) {
        return (keyClass != null
                // is not a empty map (is a map and not empty)
                && !isEmptyMap(map)
                // element key is the specified key class
                && BeanUtils.isInstanceOf(findCommonElementType(((Map<?, ?>) map).keySet()), keyClass)
        );
    }
    /**
     * Gets a boolean value indicating that specified object whether is map and empty (or null) or not a map
     *
     * @param map
     *      the object to check
     *
     * @return true for empty/null map or not a map; else false
     */
    public static boolean isEmptyMap(Object map) {
        return (!isMap(map) || isEmpty((Map<?, ?>) map));
    }

    /**
     * Get the item entry at the specified index in map
     *
     * @param map to check
     * @param idx of item. base on 0
     *
     * @return the item entry at the specified index or NULL if not found
     */
    public static Entry<?, ?> get(Map<?, ?> map, int idx) {
    	int size = getSize(map);
    	if (!isEmpty(map) && 0 <= idx && idx < size) {
    		for(final Iterator<?> it = map.entrySet().iterator(); it.hasNext();) {
    			Object item = it.next();
    			if (idx <= 0) return BeanUtils.safeType(item, Entry.class);
    			idx--;
    		}
    	}
        return null;
    }
    /**
     * Get the item entry value at the specified index in map
     *
     * @param map to check
     * @param idx of item. base on 0
     *
     * @return the item entry value at the specified index or NULL if not found
     */
    public static Object getEntryValue(Map<?, ?> map, int idx) {
    	Entry<?, ?> item = get(map, idx);
        return (item == null ? null : item.getValue());
    }
    /**
     * Get the item entry value at the specified index in map
     *
     * @param <T> value class type
     * @param map to check
     * @param idx of item. base on 0
     * @param valueClass to convert
     *
     * @return the item entry value at the specified index or NULL if not found
     */
    public static <T> T getEntryValue(Map<Object, Object> map, int idx, Class<T> valueClass) {
        return BeanUtils.safeType(getEntryValue(map, idx), valueClass);
    }
    /**
     * Get the item entry key at the specified index in map
     *
     * @param map to check
     * @param idx of item. base on 0
     *
     * @return the item entry key at the specified index or NULL if not found
     */
    public static Object getEntryKey(Map<?, ?> map, int idx) {
    	Entry<?, ?> item = get(map, idx);
        return (item == null ? null : item.getKey());
    }
    /**
     * Get the item entry key at the specified index in map
     *
     * @param <T> key class type
     * @param map to check
     * @param idx of item. base on 0
     * @param keyClass to convert
     *
     * @return the item entry key at the specified index or NULL if not found
     */
    public static <T> T getEntryKey(Map<?, ?> map, int idx, Class<T> keyClass) {
        return BeanUtils.safeType(getEntryKey(map, idx), keyClass);
    }

    // -------------------------------------------------
    // SET
    // -------------------------------------------------

    /**
     * Return true if the supplied set is null or empty. Otherwise, return false.
     * Alias of {@link org.springframework.util.CollectionUtils#isEmpty(Collection)} function
     *
     * @param set to check
     *
     * @return whether the given set is empty
     */
    public static boolean isEmpty(Set<?> set) {
        return (set == null || set.size() <= 0);
    }
    /**
     * Get the size of specified set
     *
     * @param set to parse
     *
     * @return size of set or 0 if empty
     */
    public static int getSize(Set<?> set) {
        return (set == null || set.size() <= 0 ? 0 : set.size());
    }
    /**
     * Get a boolean value indicating the specified set size whether is equals with the specified size
     *
     * @param set the set to check
     * @param size the size to compare with set's size
     *
     * @return true for equals; else false
     */
    public static boolean isElementsNumber(Set<?> set, int size) {
        return (!isEmpty(set) && set.size() == size);
    }
    /**
     * Get a boolean value indicating the specified set size whether is greater than with the specified size
     *
     * @param set the set to check
     * @param size the size to compare with set's size
     * @param equals specify whether checking set's size equals with the specified size
     *
     * @return true for equals or greater than the specified size; else false
     */
    public static boolean isElementsNumberGreaterThan(Set<?> set, int size, boolean equals) {
        return (!isEmpty(set)
                && ((equals && set.size() >= size) || (!equals && set.size() > size)));
    }
    /**
     * Get a boolean value indicating the specified set size whether is less than with the specified size
     *
     * @param set the set to check
     * @param size the size to compare with set's size
     * @param equals specify whether checking set's size equals with the specified size
     *
     * @return true for equals or less than the specified size; else false
     */
    public static boolean isElementsNumberLessThan(Set<?> set, int size, boolean equals) {
        return (!isEmpty(set)
                && ((equals && set.size() <= size) || (!equals && set.size() < size)));
    }

    /**
     * Get the first occurred index of the specified value in set
     *
     * @param set to check
     * @param value to find by {@link Object#equals(Object)}
     *
     * @return index of value in set (base on 0) or -1 if not found
     */
    public static int indexOf(Set<?> set, Object value) {
        if (!isEmpty(set) && set.contains(value)) {
        	int idx = -1;
            for(final Iterator<?> it = set.iterator(); it.hasNext();) {
                Object item = it.next();
                idx++;
                if (item == null && value == null) break;
                if (item != null && value != null && (item == value || item.equals(value))) {
                	return idx;
                }
            }
        }
        return -1;
    }

    /**
     * Get the item at the specified index in set
     *
     * @param set to check
     * @param idx of item. base on 0
     *
     * @return the item at the specified index or NULL if not found
     */
    public static Object get(Set<?> set, int idx) {
    	int size = getSize(set);
    	if (!isEmpty(set) && 0 <= idx && idx < size) {
    		for(final Iterator<?> it = set.iterator(); it.hasNext();) {
    			Object item = it.next();
    			if (idx <= 0) return item;
    			idx--;
    		}
    	}
        return null;
    }
    /**
     * Get the item at the specified index in set
     *
     * @param <T> item class type
     * @param set to check
     * @param idx of item. base on 0
     * @param itemClass to convert
     *
     * @return the item at the specified index or NULL if not found
     */
    public static <T> T get(Set<?> set, int idx, Class<T> itemClass) {
        return BeanUtils.safeType(get(set, idx), itemClass);
    }

    // -------------------------------------------------
    // ARRAY
    // -------------------------------------------------

    /**
     * Return true if the supplied array is null or empty. Otherwise, return false.
     *
     * @param array to check
     *
     * @return whether the given array is empty
     */
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length <= 0);
    }
    /**
     * Get the size of specified array
     *
     * @param array to parse
     *
     * @return size of array or 0 if empty
     */
    public static int getSize(Object[] array) {
        return (array == null || array.length <= 0 ? 0 : array.length);
    }
    /**
     * Get a boolean value indicating the specified array length whether is equals with the specified size
     *
     * @param array the array to check
     * @param size the size to compare with array's length
     *
     * @return true for equals; else false
     */
    public static boolean isElementsNumber(Object[] array, int size) {
        return (!isEmpty(array) && array.length == size);
    }
    /**
     * Get a boolean value indicating the specified array length whether is greater than with the specified size
     *
     * @param array the array to check
     * @param size the size to compare with array's length
     * @param equals specify whether checking array's length equals with the specified size
     *
     * @return true for equals or greater than the specified size; else false
     */
    public static boolean isElementsNumberGreaterThan(Object[] array, int size, boolean equals) {
        return (!isEmpty(array)
                && ((equals && array.length >= size) || (!equals && array.length > size)));
    }
    /**
     * Get a boolean value indicating the specified array length whether is less than with the specified size
     *
     * @param array the array to check
     * @param size the size to compare with array's length
     * @param equals specify whether checking array's length equals with the specified size
     *
     * @return true for equals or less than the specified size; else false
     */
    public static boolean isElementsNumberLessThan(Object[] array, int size, boolean equals) {
        return (!isEmpty(array)
                && ((equals && array.length <= size) || (!equals && array.length < size)));
    }

    /**
     * Get the first occurred index of the specified value in array
     *
     * @param array to check
     * @param value to find by {@link Object#equals(Object)}
     *
     * @return index of value in array (base on 0) or -1 if not found
     */
    public static int indexOf(Object[] array, Object value) {
        if (!isEmpty(array)) {
        	int idx = -1;
            for(Object item : array) {
                idx++;
                if (item == null && value == null) break;
                if (item != null && value != null && (item == value || item.equals(value))) {
                	return idx;
                }
            }
        }
        return -1;
    }

    /**
     * Get the item at the specified index in array
     *
     * @param array to check
     * @param idx of item. base on 0
     *
     * @return the item at the specified index or NULL if not found
     */
    public static Object get(Object[] array, int idx) {
    	int size = getSize(array);
        return (0 <= idx && idx < size ? array[idx] : null);
    }
    /**
     * Get the item at the specified index in array
     *
     * @param <T> item class type
     * @param array to check
     * @param idx of item. base on 0
     * @param itemClass to convert
     *
     * @return the item at the specified index or NULL if not found
     */
    public static <T> T get(Object[] array, int idx, Class<T> itemClass) {
        return BeanUtils.safeType(get(array, idx), itemClass);
    }

    // -------------------------------------------------
    // OBJECT AS ARRAY
    // -------------------------------------------------

    /**
     * Return true if the supplied array/collection/map is null or empty. Otherwise, return false.
     *
     * @param obj to check
     *
     * @return whether the given array/collection/map is empty
     */
    public static boolean isEmpty(Object obj) {
        return (obj == null
                || (obj.getClass().isArray() && Array.getLength(obj) <= 0)
                || (isCollection(obj) && isEmpty((Collection<?>) obj))
                || (isMap(obj) && isEmpty((Map<?, ?>) obj)));
    }
    /**
     * Get the size of specified array/collection/map
     *
     * @param obj to parse
     *
     * @return size of array/collection/map or 0 if empty
     */
    public static int getSize(Object obj) {
        if (obj == null) return 0;
        else if (isArray(obj)) return Array.getLength(obj);
        else if (isCollection(obj)) return getSize((Collection<?>) obj);
        else if (isMap(obj)) return getSize((Map<?, ?>) obj);
        else return 0;
    }
    /**
     * Get a boolean value indicating the specified array/collection/map length whether is equals with the specified size
     *
     * @param obj the array/collection/map to check
     * @param size the size to compare with array's length
     *
     * @return true for equals; else false
     */
    public static boolean isElementsNumber(Object obj, int size) {
        return (!isEmpty(obj) && getSize(obj) == size);
    }
    /**
     * Get a boolean value indicating the specified array/collection/map length whether is greater than with the specified size
     *
     * @param obj the array/collection/map to check
     * @param size the size to compare with array's length
     * @param equals specify whether checking (array/collection/map)'s length equals with the specified size
     *
     * @return true for equals or greater than the specified size; else false
     */
    public static boolean isElementsNumberGreaterThan(Object obj, int size, boolean equals) {
        return (!isEmpty(obj) && ((equals && getSize(obj) >= size) || (!equals && getSize(obj) > size)));
    }
    /**
     * Get a boolean value indicating the specified array/collection/map length whether is less than with the specified size
     *
     * @param obj the array/collection/map to check
     * @param size the size to compare with array's length
     * @param equals specify whether checking (array/collection/map)'s length equals with the specified size
     *
     * @return true for equals or less than the specified size; else false
     */
    public static boolean isElementsNumberLessThan(Object obj, int size, boolean equals) {
        return (!isEmpty(obj) && ((equals && getSize(obj) <= size) || (!equals && getSize(obj) < size)));
    }

    /**
     * Gets a boolean value indicating that specified object whether is array
     *
     * @param array
     *      the object to check
     *
     * @return true for array; else false
     */
    public static boolean isArray(Object array) {
        return (array != null && array.getClass().isArray());
    }
    /**
     * Gets a boolean value indicating that specified object whether is array
     * of the item classes.
     * <b><i>(* NOTE: In empty array, this function not accepts the empty array)</i></b>
     *
     * @param array
     *      the object to check
     * @param itemClass
     *      the item class to check
     *
     * @return true for array; else false
     */
    public static boolean isArrayOf(Object array, Class<?> itemClass) {
        return (itemClass != null
                // is not a empty array (is a array and not empty)
                && !isEmptyArray(array)
                // element is the specified item class
                && BeanUtils.isInstanceOf(array.getClass().getComponentType(), itemClass)
        );
    }
    /**
     * Gets a boolean value indicating that specified object whether is array and empty (or null) or not a array
     *
     * @param array
     *      the object to check
     *
     * @return true for empty/null array or not a array; else false
     */
    public static boolean isEmptyArray(Object array) {
        return (!isArray(array) || isEmpty(array));
    }
    /**
     * Merge 2 array that elements is same type
     *
     * @param o1 array 1
     * @param o2 array 2
     *
     * @return merged array
     * @throws IllegalArgumentException
     *      thrown if argument 1,2 is null;
     *      or one of them is not an array;
     *      or elements of them are not same type
     */
    public static Object joinArrays(Object o1, Object o2) throws IllegalArgumentException {
        Assert.isTrue(isArray(o1), "Argument 1 is not an array!");
        Assert.isTrue(isArray(o2), "Argument 2 is not an array!");
        Class<?> o1Type = o1.getClass().getComponentType();
        Class<?> o2Type = o2.getClass().getComponentType();
        if (!BeanUtils.isInstanceOf(o1Type, o2Type)
                && BeanUtils.isInstanceOf(o2Type, o1Type)) {
            throw new IllegalArgumentException("Arrays is not same element type!");
        }
        int o1Size = Array.getLength(o1);
        int o2Size = Array.getLength(o2);
        Object array = Array.newInstance(o1Type, o1Size + o2Size);
        int offset = 0, i;
        for(i = 0; i < o1Size; i++, offset++) {
            Array.set(array, offset, Array.get(o1, i));
        }
        for(i = 0; i < o2Size; i++, offset++) {
            Array.set(array, offset, Array.get(o2, i));
        }
        return array;
    }

    /**
     * Convert object value/array to objects collection
     *
     * @param <T> item class type
     * @param value to convert
     * @param entityClass class to check
     *
     * @return objects collection
     */
    public static <T> List<T> toList(Object value, Class<T> entityClass) {
        List<T> list = new LinkedList<T>();
        if (value != null) {
        	// try as collection
        	Collection<?> valsCollection = BeanUtils.safeType(value, Collection.class);
    		int size = getSize(value);
        	if (valsCollection != null && size > 0 && isCollectionOf(value, entityClass)) {
    			for(final Iterator<?> it = valsCollection.iterator(); it.hasNext();) {
    				Object itVal = it.next();
    				T val = BeanUtils.safeType(itVal, entityClass);
    				if (val == null) continue;
    				list.add(val);
    			}
        	}
        	// try as array
        	if (size > 0 && list.size() <= 0 && isArrayOf(value, entityClass)) {
        		for(int i = 0; i < size; i++) {
        			Object itVal = Array.get(value, i);
    				T val = BeanUtils.safeType(itVal, entityClass);
    				if (val == null) continue;
    				list.add(val);
        		}
        	}
        	// try as simple object
        	if (list.size() <= 0 && BeanUtils.isInstanceOf(value, entityClass)) {
        		T val = BeanUtils.safeType(value, entityClass);
        		if (val != null) list.add(val);
        	}
        }
        return list;
    }
    /**
     * Convert object values array to objects collection
     *
     * @param <T> item class type
     * @param values to convert
     *
     * @return objects collection
     */
    public static <T> List<T> toList(T[] values) {
        List<T> list = new LinkedList<T>();
        if (!isEmpty(values)) {
            list.addAll(Arrays.asList(values));
        }
        return list;
    }
    /**
     * Convert object value/array to objects collection
     *
     * @param <T> item class type
     * @param value to convert
     * @param entityClass class to check
     *
     * @return objects collection
     */
    public static <T> Set<T> toSet(Object value, Class<T> entityClass) {
    	Set<T> set = new LinkedHashSet<T>();
        if (value != null) {
        	// try as collection
        	Collection<?> valsCollection = BeanUtils.safeType(value, Collection.class);
    		int size = getSize(value);
        	if (valsCollection != null && size > 0 && isCollectionOf(value, entityClass)) {
    			for(final Iterator<?> it = valsCollection.iterator(); it.hasNext();) {
    				Object itVal = it.next();
    				T val = BeanUtils.safeType(itVal, entityClass);
    				if (val == null) continue;
    				set.add(val);
    			}
        	}
        	// try as array
        	if (size > 0 && set.size() <= 0 && isArrayOf(value, entityClass)) {
        		for(int i = 0; i < size; i++) {
        			Object itVal = Array.get(value, i);
    				T val = BeanUtils.safeType(itVal, entityClass);
    				if (val == null) continue;
    				set.add(val);
        		}
        	}
        	// try as simple object
        	if (set.size() <= 0 && BeanUtils.isInstanceOf(value, entityClass)) {
        		T val = BeanUtils.safeType(value, entityClass);
        		if (val != null) set.add(val);
        	}
        }
        return set;
    }
    /**
     * Convert object values array to objects collection
     *
     * @param <T> item class type
     * @param values to convert
     *
     * @return objects collection
     */
    public static <T> Set<T> toSet(T[] values) {
        Set<T> set = new LinkedHashSet<T>();
        if (!isEmpty(values)) {
            set.addAll(Arrays.asList(values));
        }
        return set;
    }

    /**
     * Convert object values list to objects array
     *
     * @param <T> item class type
     * @param values to convert
     * @param arrayItemClass the item class of array
     *
     * @return objects array
     */
    @SuppressWarnings("unchecked")
	public static <T> T[] toArray(List<T> values, Class<T> arrayItemClass) {
    	Assert.notNull(arrayItemClass, "arrayItemClass");
        T[] array = null;
        try {
        	if (!isEmpty(values) && isCollectionOf(values, arrayItemClass)) {
        		array = (T[]) Array.newInstance(arrayItemClass, values.size());
        		for(int i = 0; i < values.size(); i++) {
        			array[i] = values.get(i);
        		}

        	} else if (!isEmpty(values)) {
        		throw new IllegalArgumentException(
        				"the item of values list is not "
        				+ "an instance of the specified class [" + arrayItemClass.getName() + "]");
        	} else {
        		array = (T[]) Array.newInstance(arrayItemClass, 0);
        	}
        } catch (Exception e) {
        	LogUtils.logError(CollectionUtils.class,
        			"Could not create array of ["
        			+ arrayItemClass.getName() + "] - " + e.getMessage());
        	array = null;
        }
        return array;
    }
    /**
     * Convert object values set to objects array
     *
     * @param <T> item class type
     * @param values to convert
     * @param arrayItemClass the item class of array
     *
     * @return objects array
     */
    @SuppressWarnings("unchecked")
	public static <T> T[] toArray(Set<T> values, Class<T> arrayItemClass) {
    	Assert.notNull(arrayItemClass, "arrayItemClass");
        T[] array = null;
        try {
        	if (!isEmpty(values) && isCollectionOf(values, arrayItemClass)) {
        		array = (T[]) Array.newInstance(arrayItemClass, values.size());
        		int i = 0;
        		for(final Iterator<T> it = values.iterator(); it.hasNext();) {
        			array[i] = it.next();
        			i++;
        		}

        	} else if (!isEmpty(values)) {
        		throw new IllegalArgumentException(
        				"the item of values list is not "
        				+ "an instance of the specified class [" + arrayItemClass.getName() + "]");
        	} else {
        		array = (T[]) Array.newInstance(arrayItemClass, 0);
        	}
        } catch (Exception e) {
        	LogUtils.logError(CollectionUtils.class,
        			"Could not create array of ["
        			+ arrayItemClass.getName() + "] - " + e.getMessage());
        	array = null;
        }
        return array;
    }
    /**
     * Convert object values collection to objects array
     *
     * @param <T> item class type
     * @param values to convert
     * @param arrayItemClass the item class of array
     *
     * @return objects array
     */
    @SuppressWarnings("unchecked")
	public static <T> T[] toArray(Collection<T> values, Class<T> arrayItemClass) {
    	Assert.notNull(arrayItemClass, "arrayItemClass");
        T[] array = null;
        try {
        	if (!isEmpty(values) && isCollectionOf(values, arrayItemClass)) {
        		array = (T[]) Array.newInstance(arrayItemClass, values.size());
        		int i = 0;
        		for(final Iterator<T> it = values.iterator(); it.hasNext();) {
        			array[i] = it.next();
        			i++;
        		}

        	} else if (!isEmpty(values)) {
        		throw new IllegalArgumentException(
        				"the item of values list is not "
        				+ "an instance of the specified class [" + arrayItemClass.getName() + "]");
        	} else {
        		array = (T[]) Array.newInstance(arrayItemClass, 0);
        	}
        } catch (Exception e) {
        	LogUtils.logError(CollectionUtils.class,
        			"Could not create array of ["
        			+ arrayItemClass.getName() + "] - " + e.getMessage());
        	array = null;
        }
        return array;
    }

    /**
     * Convert the specified value to the map of the specified key, value classes in safety.<br>
     * Exception will be thrown if key is not inherited from the specified key class.<br>
     * And all value(s), that has not been inherited from the specified value class, will be NULL.
     *
     * @param <T> the expected map entry key type
     * @param <K> the expected map entry value type
     * @param value value to convert
     * @param keyClazz class of map entry key to convert
     * @param valueClazz class of map entry value to convert
     * @return the corrected instance map or NULL if failed
     */
    @SuppressWarnings("unchecked")
    public static <T, K> Map<T, K> safeMap(Object value, Class<? extends T> keyClazz, Class<? extends K> valueClazz) {
        Map<T, K> safe = null;
        if (value != null && keyClazz != null
                && CollectionUtils.isMapOfKey(value, keyClazz)) {
            try {
                Map<T, ?> tempMap = (Map<T, ?>) value;
                safe = new LinkedHashMap<T, K>();
                for(final Iterator<T> it = tempMap.keySet().iterator(); it.hasNext();) {
                    T key = it.next();
                    K val = BeanUtils.safeType(tempMap.get(key), valueClazz);
                    safe.put(key, val);
                }
            } catch (Exception e) {
                e.printStackTrace();
                safe = null;
            }
        }
        return safe;
    }
    /**
     * Convert the specified value to the collection of the specified item class in safety
     *
     * @param <T> the expected collection item type
     * @param value value to convert
     * @param itemClazz class of collection item to convert
     * @return the corrected instance collection or NULL if failed
     */
    @SuppressWarnings("unchecked")
	public static <T> Collection<T> safeCollection(Object value, Class<? extends T> itemClazz) {
        Collection<T> safe = null;
        if (value != null && itemClazz != null
                && CollectionUtils.isCollectionOf(value, itemClazz)) {
            try {
                safe = (Collection<T>) value;
            } catch (Exception e) {
            	LogUtils.logError(CollectionUtils.class, e.getMessage());
                safe = null;
            }
        }
        return safe;
    }

    /**
     * Remove duplicate item from the specified collection and return a new uniqued collection.<br>
     * Items should be calculated {@link Object#hashCode()} for comparing each other.
     * @param collection to remove duplicate items
     * @return a new uniqued collection
     */
    public static <T> Collection<T> uniqueCollection(Collection<T> collection) {
        if (!isEmpty(collection)) {
            // create uniqued set from the specified collection
            Set<T> uqSet = new LinkedHashSet<T>(collection);
            return new LinkedList<T>(uqSet);
        }
        return null;
    }
}
