package org.nlh4j.support;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.nlh4j.util.BeanUtils;
import org.nlh4j.util.LogUtils;

/**
 * Interface to support requesting generic type classes
 */
public interface IGenericTypeSupport extends Serializable {

	/**
     * Get the generic entity class
     * 
     * @param <N> class generic types
     * 
     * @return the generic entity class
     */
	default List<Type> getClassGenericTypes() {
		return Collections.unmodifiableList(Arrays.asList(BeanUtils.getActualTypeArguments(getClass())));
	}
	
	/**
	 * Get the class generic type class at the position index
	 * 
	 * @param genericTypeIdx
	 * 			  the index of generic type. based on 0
	 * 
	 * @return the class generic type class at the position index
	 */
	default Type getClassGeneraicTypeByIndex(int genericTypeIdx) {
		try {
			List<Type> generaicTypes = getClassGenericTypes();
			return (generaicTypes.size() <= genericTypeIdx || genericTypeIdx < 0 ? null : generaicTypes.get(0));
		} catch (Exception e) {
			LogUtils.logError(getClass(), e.getMessage(), e);
			return null;
		}
	}
}