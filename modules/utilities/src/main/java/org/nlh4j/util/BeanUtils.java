/*
 * @(#)BeanUtils.java 1.0 Aug 28, 2015
 * Copyright 2015 by GNU Lesser General Public License (LGPL). All rights reserved.
 */
package org.nlh4j.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimeConverter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.googlecode.openbeans.PropertyDescriptor;

/**
 * Bean utilities
 *
 * @author Hai Nguyen (hainguyenjc@gmail.com)
 */
@SuppressWarnings("unchecked")
public final class BeanUtils implements Serializable {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;
	private static final String PREFIX_IS_GETTER_PROPERTY = "is";
	private static final String PREFIX_GET_GETTER_PROPERTY = "get";
	private static final String PREFIX_SET_SETTER_PROPERTY = "set";

    /**
     * The JAVA primitive types array<br>
     * <strong><u>NOTE:</u></strong> Not initializing non-primitive variable or initializing variable by any another method.<br>
     * Because while obfuscating code, it'll generate _clinit@12345677() method for referencing.<br>
     * This method will be failed on android (not support) or referencing from another class.<br>
     * So, solution is creating a static method for initializing this variable and synchronizing it.
     */
    private transient static Class<?>[][] primitiveTypes = null;
    static {
        try {
            if (primitiveTypes == null) {
                primitiveTypes = new Class<?>[][] {
                        { Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Boolean.TYPE,
                                Character.TYPE },
                        { java.lang.Byte.class, java.lang.Short.class, java.lang.Integer.class,
                                java.lang.Long.class, java.lang.Float.class, java.lang.Double.class,
                                java.lang.Boolean.class, java.lang.Character.class }, };
            }
        } catch (Exception e) {
            LogUtils.logError(BeanUtils.class, e.getMessage(), e);
            throw e;
        }
    }
    /**
     * Get the JAVA primitive types array
     * @return the JAVA primitive types array
     */
    private static Class<?>[][] getPrimitiveTypes() {
        if (primitiveTypes == null) return new Class<?>[][] {};
        synchronized (primitiveTypes) {
            return primitiveTypes;
        }
    }

	/**
	 * 新規BeanUtilsBeanインスタンス<br>
     * <strong><u>NOTE:</u></strong> Not initializing non-primitive variable or initializing variable by any another method.<br>
     * Because while obfuscating code, it'll generate _clinit@12345677() method for referencing.<br>
     * This method will be failed on android (not support) or referencing from another class.<br>
     * So, solution is creating a static method for initializing this variable and synchronizing it.
     */
	private static transient BeanUtilsBean beanUtilsBean = null;
	static {
	    try {
	        if (beanUtilsBean == null) {
	            beanUtilsBean = new BeanUtilsBean(
	                    new ConvertUtilsBean(),
	                    BeanUtilsBean.getInstance().getPropertyUtils());

	            // 新規BeanUtilsBeanインスタンス
	            beanUtilsBean.getConvertUtils().register(
	                    new DateConverter(null), java.util.Date.class);
	            beanUtilsBean.getConvertUtils().register(
	                    new SqlDateConverter(null), java.sql.Date.class);
	            beanUtilsBean.getConvertUtils().register(
	                    new SqlTimeConverter(null), java.sql.Timestamp.class);
	            beanUtilsBean.getConvertUtils().register(
	                    new BigDecimalConverter(null), BigDecimal.class);
	        }
	    } catch (Exception e) {
	        LogUtils.logError(BeanUtils.class, e.getMessage(), e);
            throw e;
	    }
	}
	/**
     * Get {@link BeanUtilsBean} for converting properties while mapping bean
     * @return {@link BeanUtilsBean}
     */
    private static BeanUtilsBean getBeanUtils() {
        if (beanUtilsBean == null) return null;
        synchronized (beanUtilsBean) {
            return beanUtilsBean;
        }
    }

	/**
	 * Gets a boolean value indicating the specified {@link Type} whether is a
	 * primitive type
	 *
	 * @param type
	 *            the type to check
	 *
	 * @return true for primitive type; else otherwise
	 */
	public static boolean isPrimitive(Type type) {
		if (type != null) {
		    Class<?>[][] primitiveTypes = getPrimitiveTypes();
		    if (primitiveTypes != null && primitiveTypes.length > 0) {
    			for (int i = 0; i < primitiveTypes[0].length; i++) {
    				if (primitiveTypes[0][i].equals(type)) {
    					return true;
    				}
    			}
		    }
		}
		return false;
	}
	/**
	 * Gets java.lang type of the specified primitive type
	 *
	 * @param primitiveType
	 *            the primitive type
	 *
	 * @return the java.lang type
	 */
	public static Class<?> getPrimitiveType(Class<?> primitiveType) {
		if (primitiveType != null && primitiveType.isPrimitive()) {
		    Class<?>[][] primitiveTypes = getPrimitiveTypes();
		    if (primitiveTypes != null && primitiveTypes.length > 0) {
    			for (int i = 0; i < primitiveTypes[0].length; i++) {
    				if (primitiveTypes[0][i].equals(primitiveType)) {
    					return primitiveTypes[1][i];
    				}
    			}
		    }
			return String.class;
		}
		return primitiveType;
	}
	/**
	 * Gets java.lang type of the specified primitive type
	 *
	 * @param primitiveClassName
	 *            the primitive type class name
	 *
	 * @return the java.lang type
	 */
	public static Class<?> getPrimitiveType(String primitiveClassName) {
		return getPrimitiveType(safeClass(primitiveClassName));
	}

	/**
	 * Clone the specified source beans list to destination beans list
	 *
	 * @param <T> destination bean type
	 * @param <K> source bean type
	 * @param srcList source beans list
	 * @param clazz source bean class
	 *
	 * @return destination beans list
	 * @throws Exception thrown if fail
	 */
	public static <T, K> List<T> copyBeansList(List<K> srcList, Class<T> clazz) throws Exception {
		return copyBeansList(srcList, clazz, (String[]) null);
	}
	/**
     * Clone the specified source beans list to destination beans list
     *
     * @param <T> destination bean type
     * @param <K> source bean type
     * @param srcList source beans list
     * @param clazz source bean class
     * @param ignorePoperties the ignored property names that has not been cloned value
     *
     * @return destination beans list
     * @throws Exception thrown if fail
     */
    public static <T, K> List<T> copyBeansList(List<K> srcList, Class<T> clazz, String...ignorePoperties) throws Exception {
		  List<T> destDtos = new LinkedList<T>();
		  if (!CollectionUtils.isEmpty(srcList)) {
			  for(K src : srcList) {
				  destDtos.add(copyBean(src, clazz, ignorePoperties));
			  }
		  }
		  return destDtos;
	}
    /**
     * Clone the specified source beans list to destination beans list
     *
     * @param <T> destination bean type
     * @param <K> source bean type
     * @param srcList source beans list
     * @param clazz source bean class
     *
     * @return destination beans list
     * @throws Exception thrown if fail
     */
    public static <T, K> List<T> copyBeansListByFields(List<K> srcList, Class<T> clazz) throws Exception {
        return copyBeansListByFields(srcList, clazz, (String[]) null);
    }
    /**
     * Clone the specified source beans list to destination beans list
     *
     * @param <T> destination bean type
     * @param <K> source bean type
     * @param srcList source beans list
     * @param clazz source bean class
     * @param ignoreFields the ignored field names that has not been cloned value
     *
     * @return destination beans list
     * @throws Exception thrown if fail
     */
    public static <T, K> List<T> copyBeansListByFields(List<K> srcList, Class<T> clazz, String...ignoreFields) throws Exception {
          List<T> destDtos = new LinkedList<T>();
          if (!CollectionUtils.isEmpty(srcList)) {
              for(K src : srcList) {
                  destDtos.add(copyBeanByFields(src, clazz, ignoreFields));
              }
          }
          return destDtos;
    }

	/**
	 * Clone destination bean from the specified source bean
	 *
	 * @param <T> destination bean type
	 * @param <K> source bean type
	 * @param src source bean
	 * @param clazz destination bean class
	 *
	 * @return destination bean
     * @throws Exception thrown if fail
	 */
	public static <T, K> T copyBean(K src, Class<T> clazz) throws Exception {
		return copyBean(src, clazz, (String[]) null);
	}
	/**
     * Clone destination bean from the specified source bean
     *
     * @param <T> destination bean type
     * @param <K> source bean type
     * @param src source bean
     * @param clazz destination bean class
     * @param ignorePoperties the ignored property names that has not been cloned value
     *
     * @return destination bean
     * @throws Exception thrown if fail
     */
    public static <T, K> T copyBean(K src, Class<T> clazz, String...ignorePoperties) throws Exception {
		T dest = null;
		if (src != null) {
			try {
				dest = clazz.newInstance();
				org.springframework.beans.BeanUtils.copyProperties(src, dest, ignorePoperties);
			}
			catch (Exception e) {
			    LogUtils.logError(BeanUtils.class, e.getMessage(), e);
				throw e;
			}
		}
		return dest;
	}

    /**
     * Clone destination bean from the specified source bean
     *
     * @param <T> destination bean type
     * @param <K> source bean type
     * @param src source bean
     * @param clazz destination bean class
     *
     * @return destination bean
     * @throws Exception thrown if fail
     */
    public static <T, K> T copyBeanByFields(K src, Class<T> clazz) throws Exception {
        return copyBeanByFields(src, clazz, (String[]) null);
    }
    /**
     * Clone destination bean from the specified source bean
     *
     * @param <T> destination bean type
     * @param <K> source bean type
     * @param src source bean
     * @param clazz destination bean class
     * @param ignoreFields the ignored field names that has not been cloned value
     *
     * @return destination bean
     * @throws Exception thrown if fail
     */
    public static <T, K> T copyBeanByFields(K src, Class<T> clazz, String...ignoreFields) throws Exception {
        T dest = null;
        if (src != null) {
            try {
                dest = clazz.newInstance();
                copyFields(src, dest, ignoreFields);
            }
            catch (Exception e) {
                LogUtils.logError(BeanUtils.class, e.getMessage(), e);
                throw e;
            }
        }
        return dest;
    }
    /**
     * Clone destination bean from the specified source bean
     *
     * @param <T> destination bean type
     * @param <K> source bean type
     * @param src source bean
     * @param dest destination bean
     * @param ignoreFields the ignored field names that has not been cloned value
     * @throws Exception thrown if fail
     */
    public static <T, K> void copyFields(K src, T dest, String...ignoreFields) throws Exception {
        Assert.notNull(src, "source");
        Assert.notNull(dest, "destination");
        try {
            Field[] srcFds = src.getClass().getFields();
            List<String> ignoreList = (ignoreFields != null) ? Arrays.asList(ignoreFields) : null;
            for (Field srcFd : srcFds) {
                if (ignoreFields != null && ignoreList.contains(srcFd.getName())) continue;

                int srcModifiers = srcFd.getModifiers();
                if (Modifier.isFinal(srcModifiers)) continue;
                boolean srcAccessible = srcFd.isAccessible();
                if (!Modifier.isPublic(srcModifiers) && !Modifier.isStatic(srcModifiers)) srcFd.setAccessible(true);

                Field destFd = getField(dest, srcFd.getName());
                if (destFd == null) continue;
                int destModifiers = destFd.getModifiers();
                boolean destAccessible = destFd.isAccessible();
                if (!Modifier.isPublic(destModifiers) && !Modifier.isStatic(destModifiers)) destFd.setAccessible(true);

                destFd.set(dest, srcFd.get(src));
                srcFd.setAccessible(srcAccessible);
                destFd.setAccessible(destAccessible);
            }
        }
        catch (Exception e) {
            LogUtils.logError(BeanUtils.class, e.getMessage(), e);
            throw e;
        }
    }

	/**
	 * Gets a boolean value indicating that the specified value whether is
	 * assignable from the specified class or is an instance of this class
	 *
	 * @param <T> the instance type
	 * @param value the value to check
	 * @param checkedCls the class to check
	 *
	 * @return true for being an instance of the specified class; else otherwise
	 */
	public static <T> boolean isInstanceOf(Object value, Class<? extends T> checkedCls) {
		Class<?> valueClass = null;
		if (value != null) {
			if (isPrimitive(value.getClass()))
				valueClass = getPrimitiveType(valueClass);
			else
				valueClass = value.getClass();
		}
		return ((checkedCls != null && checkedCls.isInstance(value))
		        || isInstanceOf(valueClass, checkedCls));
	}
	/**
	 * Gets a boolean value indicating that the specified value whether is
	 * assignable from the specified class or is an instance of this class
	 *
	 * @param <T> the instance type
	 * @param value the value to check
	 * @param checkedClss the class to check
	 *
	 * @return true for being an instance of the specified class; else otherwise
	 */
	public static <T> boolean isInstanceOf(Object value, Class<? extends T>...checkedClss) {
		if (CollectionUtils.isEmpty(checkedClss)) return false;
		for(Class<? extends T> checkedCls : checkedClss) {
			if (isInstanceOf(value, checkedCls)) return true;
		}
		return false;
	}
	/**
	 * Gets a boolean value indicating that the specified value whether is
	 * assignable from the specified class or is an instance of this class
	 *
	 * @param <T> the instance type
	 * @param valueClass the value class to check
	 * @param checkedClss the class to check
	 *
	 * @return true for being an instance of the specified class; else otherwise
	 */
	public static <T> boolean isInstanceOf(Class<?> valueClass, Class<? extends T>...checkedClss) {
		if (valueClass == null || CollectionUtils.isEmpty(checkedClss)) return false;
		for(Class<? extends T> checkedCls : checkedClss) {
			if (isInstanceOf(valueClass, checkedCls)) return true;
		}
		return false;
	}
	/**
	 * Gets a class in the specified classes array that is super or instance of the specified value
	 *
	 * @param <T> the instance type
	 * @param value the value to check
	 * @param checkedClss the class to check
	 *
	 * @return the class in the specified classes array that is super or instance of the specified value.
	 * NULL for not found
	 */
	public static <T> Class<? extends T> findInstanceOf(Object value, Class<? extends T>...checkedClss) {
		if (CollectionUtils.isEmpty(checkedClss)) return null;
		for(Class<? extends T> checkedCls : checkedClss) {
			if (isInstanceOf(value, checkedCls)) return checkedCls;
		}
		return null;
	}
	/**
	 * Gets a class in the specified classes array that is super or instance of the specified value
	 *
	 * @param <T> the instance type
	 * @param valueClass the value class to check
	 * @param checkedClss the class to check
	 *
	 * @return the class in the specified classes array that is super or instance of the specified value.
	 * NULL for not found
	 */
	public static <T> Class<? extends T> findInstanceOf(Class<?> valueClass, Class<? extends T>...checkedClss) {
		if (valueClass == null || CollectionUtils.isEmpty(checkedClss)) return null;
		for(Class<? extends T> checkedCls : checkedClss) {
			if (isInstanceOf(valueClass, checkedCls)) return checkedCls;
		}
		return null;
	}
	/**
	 * Gets a boolean value indicating that the specified value whether is
	 * assignable from the specified class or is an instance of this class
	 *
	 * @param <T> the instance type
	 * @param valueClass the value to check
	 * @param checkedCls the class to check
	 *
	 * @return true for being an instance of the specified class; else otherwise
	 */
	public static <T> boolean isInstanceOf(Class<?> valueClass, Class<? extends T> checkedCls) {
	    // check by simple (same interface or same class)
	    if ((checkedCls != null && valueClass != null)
	            && (checkedCls.isAssignableFrom(valueClass)
	                    || checkedCls == valueClass
	                    || checkedCls.equals(valueClass))) {
            return true;

            // if checked class is an interface, and value class is not an interface
        } else if ((checkedCls != null && checkedCls.isInterface())
                && (valueClass != null && !valueClass.isInterface())) {
            // if checked class interface is one of interfaces of value class
            if (CollectionUtils.indexOf(valueClass.getInterfaces(), checkedCls) != -1) {
                return true;

                // check whether one of interfaces of value class extends from checked class interface
            } else {
                for(Class<?> valueInterface : valueClass.getInterfaces()) {
                    if (isInstanceOf(valueInterface, checkedCls)) {
                        return true;
                    }
                }
                return false;
            }

            // if checked class is not an interface, and value class is an interface
        } else if ((checkedCls != null && !checkedCls.isInterface())
                && (valueClass != null && valueClass.isInterface())) {
	        return false;

	        // if checked class is not an interface, and value class is not an interface
	    } else if ((checkedCls != null && !checkedCls.isInterface())
                && (valueClass != null && !valueClass.isInterface())
                && !Object.class.equals(valueClass.getSuperclass())) {
            return isInstanceOf(valueClass.getSuperclass(), checkedCls);

            // false for another cases
        } else return false;
	}

	/**
	 * Convert the specified class name to the {@link Class} in safety
	 *
	 * @param className to convert
	 *
	 * @return the corrected class or NULL if fail
	 */
	public static Class<?> safeClass(String className) {
		Class<?> clazz = null;
		try { clazz = Class.forName(className); }
		catch (Exception e) {
			LogUtils.logError(BeanUtils.class, e.getMessage());
			clazz = null;
		}
		return clazz;
	}
	/**
	 * Convert the specified value to the specified class in safety
	 *
	 * @param <T> the expected type
	 * @param value value to convert
	 * @param clazz class to convert
	 * @return the corrected instance type or NULL if fail
	 */
	public static <T> T safeType(Object value, Class<? extends T> clazz) {
	    T safe = null;
	    if (value != null && clazz != null && isInstanceOf(value, clazz)) {
    	    try {
    	        safe = (T) value;
    	    } catch (Exception e) {
    	    	LogUtils.logError(BeanUtils.class, e.getMessage());
    			safe = null;
    	    }
	    }
	    return safe;
	}

	/**
     * Clone the specified source beans list to destination beans list
     *
     * @param <T> destination bean type
     * @param <K> source bean type
     * @param dest destination beans list
     * @param orig source beans list
     * @param destClzz destination bean class
     * @throws Exception thrown if fail
     */
    public static <T, K> void copyBeans(List<T> dest, List<K> orig, Class<T> destClzz) throws Exception {
		if (dest != null && orig != null) {
    		for(K beanOrig : orig) {
    			T beanDest = destClzz.newInstance();
    			copyProperties(beanDest, beanOrig);
    			dest.add(beanDest);
    		}
		}
	}

	/**
	 * Clone the specified source bean to destination bean
	 *
	 * @param dest destination bean
	 * @param orig source bean
	 */
	public static void copyProperties(Object dest, Object orig) {
	    BeanUtilsBean utilsBean = getBeanUtils();
		if (dest != null && orig != null && utilsBean != null) {
    		try { utilsBean.copyProperties(dest, orig); }
    		catch (Exception e) {
    			LogUtils.logError(BeanUtils.class, e.getMessage());
		    }
		}
	}
	/**
     * Clone the specified source bean to destination bean
     *
     * @param dest destination bean
     * @param orig source bean
     * @param ignoreProperties the ignored property names that has not been cloned value
     */
    public static void copyProperties(Object dest, Object orig, String...ignoreProperties) {
        if (dest != null && orig != null) {
            try { org.springframework.beans.BeanUtils.copyProperties(orig, dest, ignoreProperties); }
            catch (Exception e) {
            	LogUtils.logError(BeanUtils.class, e.getMessage());
            }
        }
    }

	/**
	 * Gets the annotation of the specified entity class and annotation class
	 *
	 * @param <T> annotation type
	 * @param targetClass the instance class to check
	 * @param annotationClass the annotation class to check
	 *
	 * @return the annotation of the specified entity class and annotation class
	 */
	public static <T extends Annotation> T getClassAnnotation(Class<?> targetClass, Class<? extends T> annotationClass) {
		T annotation = null;
		if (targetClass != null && annotationClass != null) {
			for (Class<?> clazz = targetClass; !Object.class.equals(clazz); clazz = clazz.getSuperclass()) {
				for (Annotation anno : clazz.getAnnotations()) {
					if (isInstanceOf(anno.annotationType(), annotationClass)) {
						annotation = safeType(anno, annotationClass);
						break;
					}
				}
			}
		}
		return annotation;
	}
	/**
	 * Gets the annotation of the specified entity class and annotation class
	 *
	 * @param <T> annotation type
	 * @param targetClassName the instance class name to check
	 * @param annotationClass the annotation class to check
	 *
	 * @return the annotation of the specified entity class and annotation class
	 */
	public static <T extends Annotation> T getClassAnnotation(String targetClassName, Class<? extends T> annotationClass) {
		return getClassAnnotation(safeClass(targetClassName), annotationClass);
	}

	/**
     * Create a new instance of specified class
     *
     * @param beanClassName class name to check
     *
     * @return new instance of specified class
     */
    public static Object newInstance(String beanClassName) {
        return newInstance(safeClass(beanClassName));
    }
	/**
     * Create a new instance of specified class
     *
     * @param beanClass class to check
     *
     * @return new instance of specified class
     */
    public static Object newInstance(Class<?> beanClass) {
        return newInstance(beanClass, (Object[]) null);
    }
    /**
     * Create a new instance of specified class
     *
     * @param beanClassName class name to check
     * @param args contructor's arguments if necessary
     *
     * @return new instance of specified class
     */
    public static Object newInstance(String beanClassName, Object...args) {
    	return newInstance(safeClass(beanClassName), args);
    }
    /**
     * Create a new instance of specified class
     *
     * @param beanClass class to check
     * @param args contructor's arguments if necessary
     *
     * @return new instance of specified class
     */
    public static Object newInstance(Class<?> beanClass, Object...args) {
        Object inst = null;
        if (beanClass != null) {
            Class<?>[][] primitiveTypes = getPrimitiveTypes();
            for (int i = 0; i < primitiveTypes[1].length; i++) {
                if (primitiveTypes[1][i].equals(beanClass)
                        || isInstanceOf(beanClass, primitiveTypes[1][i])) {
                    beanClass = ClassUtils.resolvePrimitiveIfNecessary(primitiveTypes[1][i]);
                    break;
                }
            }
        }
        if (beanClass != null) {
            // try creating default instance
            try { inst = beanClass.newInstance(); }
            catch (Exception e) { inst = null; }
            // try creating new instance using arguments
            if (inst == null) {
                try {
                    Constructor<?>[] arrCstr = beanClass.getConstructors();
                    if (!CollectionUtils.isEmpty(arrCstr)) {
                        int argsSize = CollectionUtils.getSize(args);
                        for(Constructor<?> cstr : arrCstr) {
                            if (inst != null) break;

                            // check for creating new instance
                            Class<?>[] paramTypes = cstr.getParameterTypes();
                            int cstrArgsSize = CollectionUtils.getSize(paramTypes);
                            if (cstrArgsSize != argsSize) continue;
                            // apply accessible flag
                            boolean accessible = cstr.isAccessible();
                            cstr.setAccessible(true);
                            // try creating instance
                            try { inst = cstr.newInstance(args); }
                            catch(Exception e) { inst = null; }
                            // restore accessible flag
                            cstr.setAccessible(accessible);
                        }
                    }
                }
                catch(Exception e) {
                	LogUtils.logError(BeanUtils.class, e.getMessage());
                    inst = null;
                }
            }
        }
        return inst;
    }
    /**
     * Create a new instance of specified class safety
     *
     * @param <T> the returned object type
     * @param beanClass class to check
     *
     * @return new instance of specified class
     */
    public static <T> T safeNewInstance(Class<T> beanClass) {
        return safeNewInstance(beanClass, (Object[]) null);
    }
    /**
     * Create a new instance of specified class safety
     *
     * @param <T> the returned object type
     * @param beanClass class to check
     * @param args contructor's arguments if necessary
     *
     * @return new instance of specified class
     */
    public static <T> T safeNewInstance(Class<T> beanClass, Object...args) {
        return safeType(newInstance(beanClass, args), beanClass);
    }

    /**
     * Get the method by the specified entity class and method name
     *
     * @param beanClassName entity class name to check
     * @param name method name
     * @param parameterTypes the method parameter types
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return {@link Method} or NULL if failed
     */
    public static Method getMethod(String beanClassName, String name, boolean ignoreCase, Class<?>...parameterTypes) {
    	return getMethod(safeClass(beanClassName), name, ignoreCase, parameterTypes);
    }
    /**
     * Get the method by the specified entity class and method name
     *
     * @param beanClass entity class to check
     * @param name method name
     * @param parameterTypes the method parameter types
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return {@link Method} or NULL if failed
     */
    public static Method getMethod(Class<?> beanClass, String name, boolean ignoreCase, Class<?>...parameterTypes) {
        Method method = null;
        if (beanClass != null && StringUtils.hasText(name)) {
            // detect fast method
            try { method = beanClass.getDeclaredMethod(name, parameterTypes); }
            catch(Exception e) { method = null; }
            if (method == null) {
                try { method = beanClass.getMethod(name, parameterTypes); }
                catch(Exception e) { method = null; }
            }

            // if ignoring case-sensitive
            if (ignoreCase && method == null) {
                List<Method> methods = new LinkedList<Method>();
                methods.addAll(Arrays.asList(beanClass.getDeclaredMethods()));
                methods.addAll(Arrays.asList(beanClass.getMethods()));
                if (!CollectionUtils.isEmpty(methods)) {
                    for(Method tmpMed : methods) {
                        Class<?>[] paramTypes = tmpMed.getParameterTypes();
                        if (name.equalsIgnoreCase(tmpMed.getName())) {
                            boolean valid = (CollectionUtils.getSize(paramTypes) == CollectionUtils.getSize(parameterTypes));
                            if (valid && !CollectionUtils.isEmpty(paramTypes)) {
                                // check every parameter type
                                List<Class<?>> medParamTypes = Arrays.asList(paramTypes);
                                List<Class<?>> argParamTypes = Arrays.asList(parameterTypes);
                                while (!medParamTypes.isEmpty() && !argParamTypes.isEmpty()) {
                                    Class<?> medParamType = medParamTypes.get(0);
                                    boolean found = false;
                                    for(int i = 0; i < argParamTypes.size(); i++) {
                                        Class<?> argParamType = argParamTypes.get(i);
                                        found = isInstanceOf(argParamType, medParamType);
                                        if (found) {
                                            argParamTypes.remove(i);
                                            break;
                                        }
                                    }
                                    if (found) {
                                        medParamTypes.remove(medParamType);
                                    }
                                }

                                // re-check valid parameter types
                                valid = (CollectionUtils.getSize(paramTypes) == CollectionUtils.getSize(parameterTypes));
                                if (valid) {
                                    method = tmpMed;
                                    break;
                                }
                            } else if (valid && CollectionUtils.isEmpty(paramTypes)) {
                                method = tmpMed;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return method;
    }
    /**
     * Get the method by the specified entity class and method name (ignore case-sensitive)
     *
     * @param beanClassName entity class name to check
     * @param name method name
     * @param parameterTypes the method parameter types
     *
     * @return {@link Method} or NULL if failed
     */
    public static Method getMethod(String beanClassName, String name, Class<?>...parameterTypes) {
    	return getMethod(safeClass(beanClassName), name, parameterTypes);
    }
    /**
     * Get the method by the specified entity class and method name (ignore case-sensitive)
     *
     * @param beanClass entity class to check
     * @param name method name
     * @param parameterTypes the method parameter types
     *
     * @return {@link Method} or NULL if failed
     */
    public static Method getMethod(Class<?> beanClass, String name, Class<?>...parameterTypes) {
        return getMethod(beanClass, name, Boolean.TRUE, parameterTypes);
    }
    /**
     * Get the method by the specified entity instance and method name
     *
     * @param bean entity instance to check
     * @param name method name
     * @param parameterTypes the method parameter types
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return {@link Method} or null
     */
    public static Method getMethod(Object bean, String name, boolean ignoreCase, Class<?>...parameterTypes) {
        return (bean != null ? getMethod(bean.getClass(), name, parameterTypes) : null);
    }
    /**
     * Get the method by the specified entity instance and method name (ignore case-sensitive)
     *
     * @param bean entity instance to check
     * @param name method name
     * @param parameterTypes the method parameter types
     *
     * @return {@link Method} or null
     */
    public static Method getMethod(Object bean, String name, Class<?>...parameterTypes) {
        return (bean != null ? getMethod(bean.getClass(), name, Boolean.TRUE, parameterTypes) : null);
    }
    /**
     * Gets the annotations of the methods in the specified entity class and annotation class
     *
     * @param targetClassName the instance class name to check
     * @param methodName the method name to parse.
     * @param annotationClasses the annotation class to check
     * @param parameterTypes the method parameter types
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return the annotations list of the specified method
     */
    public static List<? extends Annotation> getMethodAnnotations(
            String targetClassName, String methodName, boolean ignoreCase,
            Class<?>[] parameterTypes, Class<?>...annotationClasses) {
    	return getMethodAnnotations(safeClass(targetClassName), methodName, ignoreCase, annotationClasses);
    }
    /**
     * Gets the annotations of the methods in the specified entity class and annotation class
     *
     * @param targetClass the instance class to check
     * @param methodName the method name to parse.
     * @param annotationClasses the annotation class to check
     * @param parameterTypes the method parameter types
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return the annotations list of the specified method
     */
    public static List<? extends Annotation> getMethodAnnotations(
            Class<?> targetClass, String methodName, boolean ignoreCase,
            Class<?>[] parameterTypes, Class<?>...annotationClasses) {
        List<Annotation> annotations = new LinkedList<Annotation>();
        if (targetClass != null && !CollectionUtils.isEmpty(annotationClasses)) {
            Method method = getMethod(targetClass, methodName, ignoreCase, parameterTypes);
            if (method != null) {
                for (Annotation annotation : method.getAnnotations()) {
                    if (isInstanceOf(annotation.annotationType(), annotationClasses)) {
                        annotations.add(annotation);
                    }
                }
            }
        }
        return annotations;
    }
    /**
     * Gets the annotations of the methods in the specified entity class and annotation class
     *
     * @param targetClassName the instance class name to check
     * @param methodName the method name to parse.
     * @param annotationClasses the annotation class to check
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return the annotations list of the specified method
     */
    public static List<? extends Annotation> getMethodAnnotations(
            String targetClassName, String methodName, boolean ignoreCase,
            Class<?>...annotationClasses) {
    	return getMethodAnnotations(safeClass(targetClassName), methodName, ignoreCase, annotationClasses);
    }
    /**
     * Gets the annotations of the methods in the specified entity class and annotation class
     *
     * @param targetClass the instance class to check
     * @param methodName the method name to parse.
     * @param annotationClasses the annotation class to check
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return the annotations list of the specified method
     */
    public static List<? extends Annotation> getMethodAnnotations(
            Class<?> targetClass, String methodName, boolean ignoreCase,
            Class<?>...annotationClasses) {
        return getMethodAnnotations(
                targetClass, methodName, ignoreCase,
                (Class<?>[]) null, annotationClasses);
    }
    /**
     * Gets the annotations of the methods in the specified entity class and annotation class (ignore case-sensitive)
     *
     * @param targetClassName the instance class name to check
     * @param methodName the method name to parse.
     * @param annotationClasses the annotation class to check
     * @param parameterTypes the method parameter types
     *
     * @return the annotations list of the specified method
     */
    public static List<? extends Annotation> getMethodAnnotations(
            String targetClassName, String methodName,
            Class<?>[] parameterTypes, Class<?>...annotationClasses) {
    	return getMethodAnnotations(safeClass(targetClassName), methodName, parameterTypes, annotationClasses);
    }
    /**
     * Gets the annotations of the methods in the specified entity class and annotation class (ignore case-sensitive)
     *
     * @param targetClass the instance class to check
     * @param methodName the method name to parse.
     * @param annotationClasses the annotation class to check
     * @param parameterTypes the method parameter types
     *
     * @return the annotations list of the specified method
     */
    public static List<? extends Annotation> getMethodAnnotations(
            Class<?> targetClass, String methodName,
            Class<?>[] parameterTypes, Class<?>...annotationClasses) {
        return getMethodAnnotations(
                targetClass, methodName, Boolean.TRUE,
                parameterTypes, annotationClasses);
    }
    /**
     * Gets the annotations of the methods in the specified entity class and annotation class (ignore case-sensitive)
     *
     * @param targetClassName the instance class name to check
     * @param methodName the method name to parse.
     * @param annotationClasses the annotation class to check
     *
     * @return the annotations list of the specified method
     */
    public static List<? extends Annotation> getMethodAnnotations(
            String targetClassName, String methodName, Class<?>...annotationClasses) {
    	return getMethodAnnotations(safeClass(targetClassName), methodName, annotationClasses);
    }
    /**
     * Gets the annotations of the methods in the specified entity class and annotation class (ignore case-sensitive)
     *
     * @param targetClass the instance class to check
     * @param methodName the method name to parse.
     * @param annotationClasses the annotation class to check
     *
     * @return the annotations list of the specified method
     */
    public static List<? extends Annotation> getMethodAnnotations(
            Class<?> targetClass, String methodName, Class<?>...annotationClasses) {
        return getMethodAnnotations(
                targetClass, methodName, Boolean.TRUE,
                (Class<?>[]) null, annotationClasses);
    }
    /**
     * Gets the annotation of the methods in the specified entity class and annotation class
     *
     * @param <T> annotation type
     * @param targetClassName the instance class name to check
     * @param methodName the method name to parse.
     * @param annotationClass the annotation class to check
     * @param parameterTypes the method parameter types
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return the annotations list of the specified method
     */
    public static <T extends Annotation> T getMethodAnnotation(
            String targetClassName, String methodName, boolean ignoreCase,
            Class<?>[] parameterTypes, Class<T> annotationClass) {
    	return getMethodAnnotation(safeClass(targetClassName), methodName, ignoreCase, parameterTypes, annotationClass);
    }
    /**
     * Gets the annotation of the methods in the specified entity class and annotation class
     *
     * @param <T> annotation type
     * @param targetClass the instance class to check
     * @param methodName the method name to parse.
     * @param annotationClass the annotation class to check
     * @param parameterTypes the method parameter types
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return the annotations list of the specified method
     */
    public static <T extends Annotation> T getMethodAnnotation(
            Class<?> targetClass, String methodName, boolean ignoreCase,
            Class<?>[] parameterTypes, Class<T> annotationClass) {
        return getMethodAnnotation(getMethod(targetClass, methodName, ignoreCase, parameterTypes), annotationClass);
    }
    /**
     * Gets the annotation of the methods in the specified entity class and annotation class
     *
     * @param <T> annotation type
     * @param targetClassName the instance class name to check
     * @param methodName the method name to parse.
     * @param annotationClass the annotation class to check
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return the annotations list of the specified method
     */
    public static <T extends Annotation> T getMethodAnnotation(
            String targetClassName, String methodName, boolean ignoreCase, Class<T> annotationClass) {
    	return getMethodAnnotation(safeClass(targetClassName), methodName, ignoreCase, annotationClass);
    }
    /**
     * Gets the annotation of the methods in the specified entity class and annotation class
     *
     * @param <T> annotation type
     * @param targetClass the instance class to check
     * @param methodName the method name to parse.
     * @param annotationClass the annotation class to check
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return the annotations list of the specified method
     */
    public static <T extends Annotation> T getMethodAnnotation(
            Class<?> targetClass, String methodName, boolean ignoreCase, Class<T> annotationClass) {
        return getMethodAnnotation(targetClass, methodName, ignoreCase, (Class<?>[]) null, annotationClass);
    }
    /**
     * Gets the annotation of the methods in the specified entity class and annotation class (ignore case-sensitive)
     *
     * @param <T> annotation type
     * @param targetClassName the instance class name to check
     * @param methodName the method name to parse.
     * @param annotationClass the annotation class to check
     * @param parameterTypes the method parameter types
     *
     * @return the annotations list of the specified method
     */
    public static <T extends Annotation> T getMethodAnnotation(
            String targetClassName, String methodName,
            Class<?>[] parameterTypes, Class<T> annotationClass) {
    	return getMethodAnnotation(safeClass(targetClassName), methodName, parameterTypes, annotationClass);
    }
    /**
     * Gets the annotation of the methods in the specified entity class and annotation class (ignore case-sensitive)
     *
     * @param <T> annotation type
     * @param targetClass the instance class to check
     * @param methodName the method name to parse.
     * @param annotationClass the annotation class to check
     * @param parameterTypes the method parameter types
     *
     * @return the annotations list of the specified method
     */
    public static <T extends Annotation> T getMethodAnnotation(
            Class<?> targetClass, String methodName,
            Class<?>[] parameterTypes, Class<T> annotationClass) {
        return getMethodAnnotation(targetClass, methodName, Boolean.TRUE, parameterTypes, annotationClass);
    }
    /**
     * Gets the annotation of the methods in the specified entity class and annotation class (ignore case-sensitive)
     *
     * @param <T> annotation type
     * @param targetClassName the instance class name to check
     * @param methodName the method name to parse.
     * @param annotationClass the annotation class to check
     *
     * @return the annotations list of the specified method
     */
    public static <T extends Annotation> T getMethodAnnotation(
            String targetClassName, String methodName, Class<T> annotationClass) {
    	return getMethodAnnotation(safeClass(targetClassName), methodName, annotationClass);
    }
    /**
     * Gets the annotation of the methods in the specified entity class and annotation class (ignore case-sensitive)
     *
     * @param <T> annotation type
     * @param targetClass the instance class to check
     * @param methodName the method name to parse.
     * @param annotationClass the annotation class to check
     *
     * @return the annotations list of the specified method
     */
    public static <T extends Annotation> T getMethodAnnotation(
            Class<?> targetClass, String methodName, Class<T> annotationClass) {
        return getMethodAnnotation(targetClass, methodName, Boolean.TRUE, (Class<?>[]) null, annotationClass);
    }
    /**
     * Gets the annotation of the methods in the specified entity class and annotation class
     *
     * @param <T> annotation type
     * @param method the method to check
     * @param annotationClass the annotation class to check
     *
     * @return the annotations list of the specified method
     */
    public static <T extends Annotation> T getMethodAnnotation(Method method, Class<T> annotationClass) {
        if (method != null && annotationClass != null) {
            for (Annotation anno : method.getAnnotations()) {
                if (isInstanceOf(anno.annotationType(), annotationClass)) {
                    return safeType(anno, annotationClass);
                }
            }
        }
        return null;
    }

    /**
     * Invoke the specified method by name
     *
     * @param target target to invoke
     * @param method method to invoke
     * @param args method arguments
     *
     * @return the returned method value if necessary
     */
    public static Object invokeMethod(Object target, Method method, Object...args) {
        Object value = null;
        if (method != null) {
            boolean accessible = method.isAccessible();
            try {
                method.setAccessible(true);
                value = method.invoke(target, args);
            }
            catch (Exception e) {
                LogUtils.logError(BeanUtils.class, e.getMessage());
                value = null;
            }
            finally {
                try { method.setAccessible(accessible); }
                catch (Exception e) {}
            }
        }
        return value;
    }
    /**
     * Invoke the specified method by name
     *
     * @param target target to invoke
     * @param name method name
     * @param ignoreClass specify whether ignoring case-sensitive when comparing
     * @param parameterTypes the method parameter types
     * @param args method arguments
     *
     * @return the returned method value if necessary
     */
    public static Object invokeMethod(
            Object target, String name, boolean ignoreClass,
            Class<?>[] parameterTypes, Object[] args) {
        return invokeMethod(target, getMethod(target, name, parameterTypes), args);
    }

    /**
     * Get fields list of the specified bean class
     *
     * @param beanClass to parse
     * @param recursively specify searching recursive to parent class
     * @param readable specify searching readable fields. NULL for not filtering
     * @param writable specify searching writable fields. NULL for not filtering
     *
     * @return fields list or empty if failed
     */
    public static List<Field> getFields(
            Class<?> beanClass, Boolean recursively,
            Boolean readable, Boolean writable) {
        List<Field> fields = new LinkedList<Field>();
        if (beanClass != null) {
            Map<String, Field> fieldsMap = getRecursiveFields(
                    beanClass, recursively, null, readable, writable);
            if (!CollectionUtils.isEmpty(fieldsMap)) {
                fields = new LinkedList<Field>(fieldsMap.values());
            }
        }
        return fields;
    }
    /**
     * Get field names list of the specified bean class
     *
     * @param beanClass to parse
     * @param recursively specify searching recursive to parent class
     * @param readable specify searching readable fields. NULL for not filtering
     * @param writable specify searching writable fields. NULL for not filtering
     *
     * @return field names list or empty if failed
     */
    public static List<String> getFieldNames(
            Class<?> beanClass, Boolean recursively,
            Boolean readable, Boolean writable) {
        List<String> fields = new LinkedList<String>();
        if (beanClass != null) {
            Map<String, Field> fieldsMap = getRecursiveFields(
                    beanClass, recursively, null, readable, writable);
            if (!CollectionUtils.isEmpty(fieldsMap)) {
                fields = new LinkedList<String>(fieldsMap.keySet());
            }
        }
        return fields;
    }
    /**
     * Get fields list of the specified bean class (including all fields)
     *
     * @param beanClass to parse
     * @param recursively specify searching recursive to parent class
     *
     * @return fields list or empty if failed
     */
    public static List<Field> getFields(
            Class<?> beanClass, Boolean recursively) {
        return getFields(beanClass, Boolean.TRUE, null, null);
    }
    /**
     * Get field names list of the specified bean class (including all fields)
     *
     * @param beanClass to parse
     * @param recursively specify searching recursive to parent class
     *
     * @return field names list or empty if failed
     */
    public static List<String> getFieldNames(
            Class<?> beanClass, Boolean recursively) {
        return getFieldNames(beanClass, Boolean.TRUE, null, null);
    }
    /**
     * Get fields list of the specified bean class (not recursive)
     *
     * @param beanClass to parse
     *
     * @return fields list or empty if failed
     */
    public static List<Field> getFields(Class<?> beanClass) {
        return getFields(beanClass, Boolean.TRUE);
    }
    /**
     * Get field names list of the specified bean class (not recursive)
     *
     * @param beanClass to parse
     *
     * @return field names list or empty if failed
     */
    public static List<String> getFieldNames(Class<?> beanClass) {
        return getFieldNames(beanClass, Boolean.TRUE);
    }
    /**
     * Get fields list of the specified target
     *
     * @param target to parse
     *
     * @return fields list or empty if failed
     */
    public static List<Field> getFields(Object target) {
        return getFields(target == null ? null : target.getClass());
    }
    /**
     * Get field names list of the specified target
     *
     * @param target to parse
     *
     * @return field names list or empty if failed
     */
    public static List<String> getFieldNames(Object target) {
        return getFieldNames(target == null ? null : target.getClass());
    }
    /**
     * Get fields list of the specified bean class
     *
     * @param beanClassName to parse
     *
     * @return fields list or empty if failed
     */
    public static List<Field> getFields(String beanClassName) {
        return getFields(safeClass(beanClassName));
    }
    /**
     * Get field names list of the specified bean class
     *
     * @param beanClassName to parse
     *
     * @return field names list or empty if failed
     */
    public static List<String> getFieldNames(String beanClassName) {
        return getFieldNames(safeClass(beanClassName));
    }
    /**
     * Get fields list of the specified bean class
     *
     * @param beanClass to parse
     * @param recursive specify searching recursively
     * @param fieldsMap returned fields list for recursive
     * @param readable specify searching readable fields. NULL for not filtering
     * @param writable specify searching writable fields. NULL for not filtering
     *
     * @return fields list or empty if failed
     */
    private static Map<String, Field> getRecursiveFields(
            Class<?> beanClass, Boolean recursive, Map<String, Field> fieldsMap,
            Boolean readable, Boolean writable) {
        fieldsMap = (fieldsMap == null ? new LinkedHashMap<String, Field>() : fieldsMap);
        if (beanClass != null) {
            // declare fields
            Field[] arrDeclFields = beanClass.getDeclaredFields();
            Field[] arrFields = beanClass.getFields();
            List<Field> fields = new LinkedList<Field>();
            fields.addAll(Arrays.asList(arrDeclFields));
            fields.addAll(Arrays.asList(arrFields));
            if (!CollectionUtils.isEmpty(fields)) {
                for(Field field : fields) {
                    // if not require checking
                    if (!fieldsMap.containsKey(field.getName())
                            && readable == null && writable == null) {
                        fieldsMap.put(field.getName(), field);
                    }
                    // field is public
                    else if (!fieldsMap.containsKey(field.getName())
                            && (Boolean.TRUE.equals(readable) || Boolean.TRUE.equals(writable))
                            && Modifier.isPublic(field.getModifiers())) {
                        fieldsMap.put(field.getName(), field);

                        // field is not public, checking getter/setter
                    } else if (!fieldsMap.containsKey(field.getName())
                            && (readable != null || writable != null)) {
                        PropertyDescriptor pd = null;
                        try {
                            pd = new PropertyDescriptor(field.getName(), beanClass);
                            // readable
                            if (!fieldsMap.containsKey(field.getName())
                                    && Boolean.TRUE.equals(readable)
                                    && pd.getReadMethod() != null
                                    && Modifier.isPublic(pd.getReadMethod().getModifiers())) {
                                fieldsMap.put(field.getName(), field);
                            }
                            // writable
                            if (!fieldsMap.containsKey(field.getName())
                                    && Boolean.TRUE.equals(writable)
                                    && pd.getWriteMethod() != null
                                    && Modifier.isPublic(pd.getWriteMethod().getModifiers())) {
                                fieldsMap.put(field.getName(), field);
                            }
                            // readable && writable
                            if (!fieldsMap.containsKey(field.getName())
                                    && Boolean.TRUE.equals(readable)
                                    && Boolean.TRUE.equals(writable)
                                    && pd.getReadMethod() != null
                                    && Modifier.isPublic(pd.getReadMethod().getModifiers())
                                    && pd.getWriteMethod() != null
                                    && Modifier.isPublic(pd.getWriteMethod().getModifiers())) {
                                fieldsMap.put(field.getName(), field);
                            }
                        } catch (Exception e) {
                            LogUtils.logWarn(BeanUtils.class, e.getMessage());
                        }
                    }
                }
            }
            // recusive
            if (Boolean.TRUE.equals(recursive) && !Object.class.equals(beanClass)) {
                fieldsMap = getRecursiveFields(
                        beanClass.getSuperclass(), recursive, fieldsMap,
                        readable, writable);
            }
        }
        return fieldsMap;
    }

    /**
     * Get the field by the specified entity class and field name
     *
     * @param beanClassName entity class name to parse
     * @param name declared field name to parse
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return {@link Field} or NULL if failed
     */
    public static Field getField(String beanClassName, String name, boolean ignoreCase) {
    	return getField(safeClass(beanClassName), name, ignoreCase);
    }
    /**
     * Get the field by the specified entity class and field name
     *
     * @param beanClass entity class to parse
     * @param name declared field name to parse
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return {@link Field} or NULL if failed
     */
    public static Field getField(Class<?> beanClass, String name, boolean ignoreCase) {
        Field field = null;
        if (beanClass != null && StringUtils.hasText(name)) {
            // detect fast field
            try { field = beanClass.getDeclaredField(name); }
            catch(Exception e) { field = null; }
            if (field == null) {
                try { field = beanClass.getField(name); }
                catch(Exception e) { field = null; }
            }

            // if ignoring case-sensitive
            if (ignoreCase && field == null) {
                List<Field> fields = getFields(beanClass);
                if (!CollectionUtils.isEmpty(fields)) {
                    for(Field tmpFld : fields) {
                        if (name.equalsIgnoreCase(tmpFld.getName())) {
                            field = tmpFld;
                            break;
                        }
                    }
                }
            }
        }
        return field;
    }
    /**
     * Get the field by the specified entity class and field name (ignore case-sensitive)
     *
     * @param beanClassName entity class name to parse
     * @param name declared field name to parse
     *
     * @return {@link Field} or NULL if failed
     */
    public static Field getField(String beanClassName, String name) {
    	return getField(safeClass(beanClassName), name);
    }
    /**
     * Get the field by the specified entity class and field name (ignore case-sensitive)
     *
     * @param beanClass entity class to parse
     * @param name declared field name to parse
     *
     * @return {@link Field} or NULL if failed
     */
    public static Field getField(Class<?> beanClass, String name) {
        return getField(beanClass, name, Boolean.TRUE);
    }
    /**
     * Get the field by the specified entity instance and field name
     *
     * @param bean entity instance to check
     * @param name declared field name to parse
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return {@link Field} or NULL if failed
     */
    public static Field getField(Object bean, String name, boolean ignoreCase) {
        return (bean != null ? getField(bean.getClass(), name, ignoreCase) : null);
    }
    /**
     * Get the field by the specified entity instance and field name (ignore case-sensitive)
     *
     * @param bean entity instance to check
     * @param name declared field name to parse
     *
     * @return {@link Field} or NULL if failed
     */
    public static Field getField(Object bean, String name) {
        return (bean != null ? getField(bean.getClass(), name, Boolean.TRUE) : null);
    }
    /**
     * Gets the annotation of the field in the specified entity class, field name and annotation class
     *
     * @param <T> annotation type
     * @param targetClassName the instance class name to check
     * @param fieldName the field name to parse.
     * @param annotationClass the annotation class to check
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return the annotations list of the specified field
     */
    public static <T extends Annotation> T getFieldAnnotation(String targetClassName, String fieldName, Class<T> annotationClass, boolean ignoreCase) {
    	return getFieldAnnotation(safeClass(targetClassName), fieldName, annotationClass, ignoreCase);
    }
    /**
     * Gets the annotation of the field in the specified entity class, field name and annotation class
     *
     * @param <T> annotation type
     * @param targetClass the instance class to check
     * @param fieldName the field name to parse.
     * @param annotationClass the annotation class to check
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return the annotations list of the specified field
     */
    public static <T extends Annotation> T getFieldAnnotation(Class<?> targetClass, String fieldName, Class<T> annotationClass, boolean ignoreCase) {
        return getFieldAnnotation(getField(targetClass, fieldName, ignoreCase), annotationClass);
    }
    /**
     * Gets the annotation of the field in the specified entity class, field name and annotation class (ignore case-sensitive)
     *
     * @param <T> annotation type
     * @param targetClassName the instance class name to check
     * @param fieldName the field name to parse.
     * @param annotationClass the annotation class to check
     *
     * @return the annotations list of the specified field
     */
    public static <T extends Annotation> T getFieldAnnotation(String targetClassName, String fieldName, Class<T> annotationClass) {
    	return getFieldAnnotation(safeClass(targetClassName), fieldName, annotationClass);
    }
    /**
     * Gets the annotation of the field in the specified entity class, field name and annotation class (ignore case-sensitive)
     *
     * @param <T> annotation type
     * @param targetClass the instance class to check
     * @param fieldName the field name to parse.
     * @param annotationClass the annotation class to check
     *
     * @return the annotations list of the specified field
     */
    public static <T extends Annotation> T getFieldAnnotation(Class<?> targetClass, String fieldName, Class<T> annotationClass) {
        return getFieldAnnotation(getField(targetClass, fieldName, Boolean.TRUE), annotationClass);
    }
    /**
     * Gets the annotation of the field in the specified entity, field name and annotation class
     *
     * @param <T> annotation type
     * @param bean the instance to check
     * @param fieldName the field name to parse.
     * @param annotationClass the annotation class to check
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return the annotations list of the specified field
     */
    public static <T extends Annotation> T getFieldAnnotation(Object bean, String fieldName, Class<T> annotationClass, boolean ignoreCase) {
        return (bean != null ? getFieldAnnotation(bean.getClass(), fieldName, annotationClass, ignoreCase) : null);
    }
    /**
     * Gets the annotation of the field in the specified entity, field name and annotation class (ignore case-sensitive)
     *
     * @param <T> annotation type
     * @param bean the instance to check
     * @param fieldName the field name to parse.
     * @param annotationClass the annotation class to check
     *
     * @return the annotations list of the specified field
     */
    public static <T extends Annotation> T getFieldAnnotation(Object bean, String fieldName, Class<T> annotationClass) {
        return (bean != null ? getFieldAnnotation(bean.getClass(), fieldName, annotationClass, Boolean.TRUE) : null);
    }
    /**
     * Gets the annotation of the field in the specified entity class, field name and annotation class
     *
     * @param <T> annotation type
     * @param field the field to check
     * @param annotationClass the annotation class to check
     *
     * @return the annotations list of the specified field
     */
    public static <T extends Annotation> T getFieldAnnotation(Field field, Class<T> annotationClass) {
        if (field != null && annotationClass != null) {
            for (Annotation anno : field.getAnnotations()) {
                if (isInstanceOf(anno.annotationType(), annotationClass)) {
                    return safeType(anno, annotationClass);
                }
            }
        }
        return null;
    }

    /**
     * Get the field value of the specified instance
     *
     * @param target instance to parse field value
     * @param field field reflection information
     *
     * @return field value or NULL if failed
     */
    public static Object getFieldValue(Object target, Field field) {
        Object value = null;
        if (target != null && field != null) {
            boolean accessible = field.isAccessible();
            // parse field value
            boolean valid = false;
            if (Modifier.isPublic(field.getModifiers())
                    || Modifier.isStatic(field.getModifiers())) {
                try {
                    field.setAccessible(true);
                    value = field.get(target);
                    valid = true;
                } catch (Exception e) {
                    value = null;
                    valid = false;
                } finally {
                    try { field.setAccessible(accessible); }
                    catch (Exception e) {}
                }

                // try access field non-public and non-static
            } else {
                try {
                    field.setAccessible(true);
                    value = field.get(target);
                    valid = true;
                } catch (Exception e) {
                    value = null;
                    valid = false;
                } finally {
                    try { field.setAccessible(accessible); }
                    catch (Exception e) {}
                }
            }

            // try via property descriptor
            if (!valid) {
                value = getPropertyValue(target, field.getName(), (Object[]) null);
            }
        }
        return value;
    }
    /**
     * Get the field value of the specified instance
     *
     * @param target instance to parse field value
     * @param fieldName field name
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     *
     * @return field value or NULL if failed
     */
    public static Object getFieldValue(Object target, String fieldName, boolean ignoreCase) {
    	Field field = getField(target, fieldName, ignoreCase);
        return (field == null ? getPropertyValue(target, fieldName, (Object[]) null)
        		: getFieldValue(target, field));
    }
    /**
     * Get the field value of the specified instance
     *
     * @param target instance to parse field value
     * @param fieldName field name
     *
     * @return field value or NULL if failed
     */
    public static Object getFieldValue(Object target, String fieldName) {
        return getFieldValue(target, fieldName, Boolean.TRUE);
    }

    /**
     * Set the field value of the specified instance.<br>
     * If this field is static/public, value will be set via {@link Field#set(Object, Object)},
     * otherwise value will be set via {@link PropertyDescriptor#getWriteMethod()}
     *
     * @param target instance to set field value
     * @param field field reflection information
     * @param value to set
     *
     * @return true for successful; else false
     */
    public static boolean setFieldValue(Object target, Field field, Object value) {
        boolean valid = false;
        if (target != null && field != null) {
            boolean accessible = field.isAccessible();
            Class<?> fClazz = field.getDeclaringClass();
            boolean applied = true;
            // parse field value
            if (!Modifier.isFinal(field.getModifiers())
                    && (Modifier.isPublic(field.getModifiers())
                            || Modifier.isStatic(field.getModifiers()))) {
                try {
                    field.setAccessible(true);
                    // try with number
                    if (isInstanceOf(fClazz, Number.class)
                            || isInstanceOf(fClazz, Boolean.class)) {
                        Number numVal = null;
                        if (!isInstanceOf(value, Number.class)) {
                            numVal = NumberUtils.toDouble(value);

                        } else {
                            numVal = (Number) value;
                        }
                        if (numVal != null && isInstanceOf(fClazz, BigDecimal.class)) {
                            field.set(target, new BigDecimal(numVal.doubleValue(), MathContext.DECIMAL32));
                        } else if (numVal != null && isInstanceOf(fClazz, BigInteger.class)) {
                            field.set(target, BigInteger.valueOf(numVal.longValue()));
                        } else if (numVal != null && isInstanceOf(fClazz, Byte.class)) {
                            field.set(target, numVal.byteValue());
                        } else if (numVal != null && isInstanceOf(fClazz, Short.class)) {
                            field.set(target, numVal.shortValue());
                        } else if (numVal != null && isInstanceOf(fClazz, Integer.class)) {
                            field.set(target, numVal.intValue());
                        } else if (numVal != null && isInstanceOf(fClazz, Long.class)) {
                            field.set(target, numVal.longValue());
                        } else if (numVal != null && isInstanceOf(fClazz, Float.class)) {
                            field.set(target, numVal.floatValue());
                        } else if (numVal != null && isInstanceOf(fClazz, Double.class)) {
                            field.set(target, numVal.doubleValue());
                        } else if (numVal != null && isInstanceOf(fClazz, Boolean.class)) {
                            field.set(target, numVal.intValue() == 1
                                    ? Boolean.TRUE : Boolean.FALSE);
                        } else if (numVal != null && isInstanceOf(fClazz, Boolean.class)) {
                            field.set(target, numVal.intValue() == 1
                                    ? Boolean.TRUE : Boolean.FALSE);
                        } else applied = false;

                        // try with date
                    } else if (isInstanceOf(fClazz, Date.class)) {
                        if (isInstanceOf(value, Number.class)) {
                            field.set(target, new Date(((Number) value).longValue()));

                        } else if (isInstanceOf(value, Date.class)
                                && isInstanceOf(fClazz, Timestamp.class)) {
                            field.set(target, DateUtils.toTimestamp((Date) value));
                        } else if (isInstanceOf(value, Date.class)
                                && isInstanceOf(fClazz, Time.class)) {
                            field.set(target, DateUtils.toSqlTime((Date) value));
                        } else applied = false;

                    } else applied = false;
                    // if not apply value yet
                    if (!applied) {
                        field.set(target, value);
                        applied = true;
                    }
                    valid = applied;
                } catch (Exception e) {
                    value = null;
                    valid = false;
                } finally {
                    try { field.setAccessible(accessible); }
                    catch (Exception e) {}
                }

                // try access field non-public and non-static
            } else {
                try {
                    field.setAccessible(true);
                    // try with number
                    if (isInstanceOf(fClazz, Number.class)
                            || isInstanceOf(fClazz, Boolean.class)) {
                        Number numVal = null;
                        if (!isInstanceOf(value, Number.class)) {
                            numVal = NumberUtils.toDouble(value);

                        } else {
                            numVal = (Number) value;
                        }
                        if (numVal != null && isInstanceOf(fClazz, BigDecimal.class)) {
                            field.set(target, new BigDecimal(numVal.doubleValue(), MathContext.DECIMAL32));
                        } else if (numVal != null && isInstanceOf(fClazz, BigInteger.class)) {
                            field.set(target, BigInteger.valueOf(numVal.longValue()));
                        } else if (numVal != null && isInstanceOf(fClazz, Byte.class)) {
                            field.set(target, numVal.byteValue());
                        } else if (numVal != null && isInstanceOf(fClazz, Short.class)) {
                            field.set(target, numVal.shortValue());
                        } else if (numVal != null && isInstanceOf(fClazz, Integer.class)) {
                            field.set(target, numVal.intValue());
                        } else if (numVal != null && isInstanceOf(fClazz, Long.class)) {
                            field.set(target, numVal.longValue());
                        } else if (numVal != null && isInstanceOf(fClazz, Float.class)) {
                            field.set(target, numVal.floatValue());
                        } else if (numVal != null && isInstanceOf(fClazz, Double.class)) {
                            field.set(target, numVal.doubleValue());
                        } else if (numVal != null && isInstanceOf(fClazz, Boolean.class)) {
                            field.set(target, numVal.intValue() == 1
                                    ? Boolean.TRUE : Boolean.FALSE);
                        } else if (numVal != null && isInstanceOf(fClazz, Boolean.class)) {
                            field.set(target, numVal.intValue() == 1
                                    ? Boolean.TRUE : Boolean.FALSE);
                        } else applied = false;

                        // try with date
                    } else if (isInstanceOf(fClazz, Date.class)) {
                        if (isInstanceOf(value, Number.class)) {
                            field.set(target, new Date(((Number) value).longValue()));

                        } else if (isInstanceOf(value, Date.class)
                                && isInstanceOf(fClazz, Timestamp.class)) {
                            field.set(target, DateUtils.toTimestamp((Date) value));
                        } else if (isInstanceOf(value, Date.class)
                                && isInstanceOf(fClazz, Time.class)) {
                            field.set(target, DateUtils.toSqlTime((Date) value));
                        } else applied = false;

                    } else applied = false;
                    // if not apply value yet
                    if (!applied) {
                        field.set(target, value);
                        applied = true;
                    }
                    valid = applied;
                } catch (Exception e) {
                    value = null;
                    valid = false;
                } finally {
                    try { field.setAccessible(accessible); }
                    catch (Exception e) {}
                }
            }

            // try via property descriptor
            if (!valid) {
                valid = setPropertyValue(target, field.getName(), value);
            }
        }
        return valid;
    }
    /**
     * Set the field value of the specified instance.<br>
     * If this field is static/public, value will be set via {@link Field#set(Object, Object)},
     * otherwise value will be set via {@link PropertyDescriptor#getWriteMethod()}
     *
     * @param target instance to set field value
     * @param fieldName field name
     * @param ignoreCase specify whether ignoring case-sensitive when comparing
     * @param value to set
     *
     * @return true for successful; else false
     */
    public static boolean setFieldValue(Object target, String fieldName, Object value, boolean ignoreCase) {
        return setFieldValue(target, getField(target, fieldName, ignoreCase), value);
    }
    /**
     * Set the field value of the specified instance.<br>
     * If this field is static/public, value will be set via {@link Field#set(Object, Object)},
     * otherwise value will be set via {@link PropertyDescriptor#getWriteMethod()}
     *
     * @param target instance to set field value
     * @param fieldName field name
     * @param value to set
     *
     * @return true for successful; else false
     */
    public static boolean setFieldValue(Object target, String fieldName, Object value) {
        return setFieldValue(target, getField(target, fieldName, Boolean.TRUE), value);
    }

    /**
     * Get all fields list that every field is also one of the specified annotations array
     *
     * @param <T> annotation type
     * @param beanClassName the instance class name to parse
     * @param annotationClasses the annotation classes array to check
     *
     * @return all fields list that every field is also one of the specified annotations array or empty if failed
     */
    public static <T extends Annotation> List<Field> getFieldsBy(String beanClassName, Class<T>...annotationClasses) {
    	return getFieldsBy(safeClass(beanClassName), annotationClasses);
    }
    /**
     * Get all fields list that every field is also one of the specified annotations array
     *
     * @param <T> annotation type
     * @param beanClass the instance class to parse
     * @param annotationClasses the annotation classes array to check
     *
     * @return all fields list that every field is also one of the specified annotations array or empty if failed
     */
    public static <T extends Annotation> List<Field> getFieldsBy(Class<?> beanClass, Class<T>...annotationClasses) {
        List<Field> fields = new LinkedList<Field>();
        if (beanClass != null && !CollectionUtils.isEmpty(annotationClasses)) {
            List<Field> beanFlds = getFields(beanClass);
            if (!CollectionUtils.isEmpty(beanFlds)) {
                for(Field field : beanFlds) {
                    for(Annotation anno : field.getAnnotations()) {
                        if (isInstanceOf(anno.annotationType(), annotationClasses)) {
                            fields.add(field);
                            break;
                        }
                    }
                }
            }
        }
        return fields;
    }
    /**
     * Get all fields list that every field is also one of the specified annotations array
     *
     * @param <T> annotation type
     * @param target the instance to parse
     * @param annotationClasses the annotation classes array to check
     *
     * @return all fields list that every field is also one of the specified annotations array or empty if failed
     */
    public static <T extends Annotation> List<Field> getFieldsBy(Object target, Class<T>...annotationClasses) {
        return (target == null ? new LinkedList<Field>() : getFieldsBy(target.getClass(), annotationClasses));
    }
    /**
     * Get all fields list that every field is also one of the specified annotations array
     *
     * @param <T> annotation type
     * @param beanClassName the instance class name to parse
     * @param annotationClass the annotation class to check
     *
     * @return all fields list that every field is also one of the specified annotations array or empty if failed
     */
    public static <T extends Annotation> List<Field> getFieldsBy(String beanClassName, Class<T> annotationClass) {
    	return getFieldsBy(safeClass(beanClassName), annotationClass);
    }
    /**
     * Get all fields list that every field is also one of the specified annotations array
     *
     * @param <T> annotation type
     * @param beanClass the instance class to parse
     * @param annotationClass the annotation class to check
     *
     * @return all fields list that every field is also one of the specified annotations array or empty if failed
     */
    public static <T extends Annotation> List<Field> getFieldsBy(Class<?> beanClass, Class<T> annotationClass) {
        List<Field> fields = new LinkedList<Field>();
        if (beanClass != null && annotationClass != null) {
            List<Field> beanFlds = getFields(beanClass);
            if (!CollectionUtils.isEmpty(beanFlds)) {
                for(Field field : beanFlds) {
                    for(Annotation anno : field.getAnnotations()) {
                        if (isInstanceOf(anno.annotationType(), annotationClass)) {
                            fields.add(field);
                            break;
                        }
                    }
                }
            }
        }
        return fields;
    }
    /**
     * Get all fields list that every field is also one of the specified annotations array
     *
     * @param <T> annotation type
     * @param target the instance to parse
     * @param annotationClass the annotation class to check
     *
     * @return all fields list that every field is also one of the specified annotations array or empty if failed
     */
    public static <T extends Annotation> List<Field> getFieldsBy(Object target, Class<T> annotationClass) {
        return (target == null ? new LinkedList<Field>() : getFieldsBy(target.getClass(), annotationClass));
    }

    /**
     * Get a map of fields and annotations list of every field
     *
     * @param <T> annotation type
     * @param beanClassName the instance class name to parse
     * @param annotationClasses the annotation classes array to check
     *
     * @return a map of fields and annotations list of every field or empty if failed
     */
    public static <T extends Annotation> Map<Field, List<Annotation>> getMapFieldsBy(String beanClassName, Class<T>...annotationClasses) {
    	return getMapFieldsBy(safeClass(beanClassName), annotationClasses);
    }
    /**
     * Get a map of fields and annotations list of every field
     *
     * @param <T> annotation type
     * @param beanClass the instance class to parse
     * @param annotationClasses the annotation classes array to check
     *
     * @return a map of fields and annotations list of every field or empty if failed
     */
    public static <T extends Annotation> Map<Field, List<Annotation>> getMapFieldsBy(Class<?> beanClass, Class<T>...annotationClasses) {
        Map<Field, List<Annotation>> fields = new LinkedHashMap<Field, List<Annotation>>();
        if (beanClass != null && !CollectionUtils.isEmpty(annotationClasses)) {
            List<Field> beanFlds = getFields(beanClass);
            if (!CollectionUtils.isEmpty(beanFlds)) {
                for(Field field : beanFlds) {
                    for(Annotation anno : field.getAnnotations()) {
                        if (isInstanceOf(anno.annotationType(), annotationClasses)) {
                            if (!fields.containsKey(field)) {
                                fields.put(field, new LinkedList<Annotation>());
                            }
                            fields.get(field).add(anno);
                        }
                    }
                }
            }
        }
        return fields;
    }
    /**
     * Get a map of fields and annotations list of every field
     *
     * @param <T> annotation type
     * @param target the instance to parse
     * @param annotationClasses the annotation classes array to check
     *
     * @return a map of fields and annotations list of every field or empty if failed
     */
    public static <T extends Annotation> Map<Field, List<Annotation>> getMapFieldsBy(Object target, Class<T>...annotationClasses) {
        return (target == null
                ? new LinkedHashMap<Field, List<Annotation>>()
                        : getMapFieldsBy(target.getClass(), annotationClasses));
    }
    /**
     * Get a map of fields and annotation of every field
     *
     * @param <T> annotation type
     * @param beanClassName the instance class to parse
     * @param annotationClass the annotation class array to check
     *
     * @return a map of fields and annotation of every field or empty if failed
     */
    public static <T extends Annotation> Map<Field, T> getMapFieldsBy(String beanClassName, Class<T> annotationClass) {
    	return getMapFieldsBy(safeClass(beanClassName), annotationClass);
    }
    /**
     * Get a map of fields and annotation of every field
     *
     * @param <T> annotation type
     * @param beanClass the instance class to parse
     * @param annotationClass the annotation class array to check
     *
     * @return a map of fields and annotation of every field or empty if failed
     */
    public static <T extends Annotation> Map<Field, T> getMapFieldsBy(Class<?> beanClass, Class<T> annotationClass) {
        Map<Field, T> fields = new LinkedHashMap<Field, T>();
        if (beanClass != null && annotationClass != null) {
            List<Field> beanFlds = getFields(beanClass);
            if (!CollectionUtils.isEmpty(beanFlds)) {
                for(Field field : beanFlds) {
                    for(Annotation anno : field.getAnnotations()) {
                        if (isInstanceOf(anno.annotationType(), annotationClass)) {
                            fields.put(field, safeType(anno, annotationClass));
                            break;
                        }
                    }
                }
            }
        }
        return fields;
    }
    /**
     * Get a map of fields and annotation of every field
     *
     * @param <T> annotation type
     * @param target the instance to parse
     * @param annotationClass the annotation class array to check
     *
     * @return a map of fields and annotation of every field or empty if failed
     */
    public static <T extends Annotation> Map<Field, T> getMapFieldsBy(Object target, Class<T> annotationClass) {
        return (target == null
                ? new LinkedHashMap<Field, T>()
                        : getMapFieldsBy(target.getClass(), annotationClass));
    }

    /**
     * Get property descriptor by entity class and property name
     *
     * @param beanClassName bean class name to check
     * @param name property name
     *
     * @return {@link PropertyDescriptor} or NULL if failed
     */
    public static PropertyDescriptor getProperty(String beanClassName, String name) {
    	return getProperty(safeClass(beanClassName), name);
    }
    /**
     * Get property descriptor by entity class and property name
     *
     * @param beanClass bean class to check
     * @param name property name
     *
     * @return {@link PropertyDescriptor} or NULL if failed
     */
    public static PropertyDescriptor getProperty(Class<?> beanClass, String name) {
        if (beanClass != null && StringUtils.hasText(name)) {
            try {
            	String getterName = (PREFIX_IS_GETTER_PROPERTY + StringUtils.toUpperCaseFirstChar(name));
            	Method getter = getMethod(beanClass, getterName, (Class<?>[]) null);
            	if (getter == null) {
            		getterName = (PREFIX_GET_GETTER_PROPERTY + StringUtils.toUpperCaseFirstChar(name));
            		getter = getMethod(beanClass, getterName, (Class<?>[]) null);
            	}
            	String setterName = (PREFIX_SET_SETTER_PROPERTY + StringUtils.toUpperCaseFirstChar(name));
            	Method setter = getMethod(beanClass, setterName, (Class<?>[]) null);
            	return new PropertyDescriptor(name, getter, setter);
        	}
            catch(Exception e) {
            	LogUtils.logError(BeanUtils.class, e.getMessage());
            }
        }
        return null;
    }
    /**
     * Get property descriptor by entity instance and property name
     *
     * @param bean bean instance to check
     * @param name property name
     *
     * @return {@link PropertyDescriptor} or NULL if failed
     */
    public static PropertyDescriptor getProperty(Object bean, String name) {
        return (bean != null ? getProperty(bean.getClass(), name) : null);
    }
    /**
     * Get property value by entity instance and property name
     *
     * @param bean bean instance to invoke
     * @param name property name
     * @param args the property read method arguments if necessary
     *
     * @return property value by entity instance and property name
     */
    public static Object getPropertyValue(Object bean, String name, Object...args) {
        PropertyDescriptor property = getProperty(bean, name);
        Object value = null;
        if (property != null) {
            value = invokeMethod(bean, property.getReadMethod(), args);
        }
        return value;
    }
    /**
     * Set property value by entity instance and property name
     *
     * @param bean bean instance to invoke
     * @param name property name
     * @param values the property values to apply
     *
     * @return true for applying successful; else false if failed
     */
    public static boolean setPropertyValue(Object bean, String name, Object...values) {
        PropertyDescriptor property = getProperty(bean, name);
        if (property != null) {
            try {
                Method med = property.getWriteMethod();
                if (med != null) {
                    boolean accessible = med.isAccessible();
                    med.setAccessible(true);
                    med.invoke(bean, values);
                    med.setAccessible(accessible);
                    return true;
                }
            }
            catch(Exception e) {
            	LogUtils.logError(BeanUtils.class, e.getMessage());
            }
        }
        return false;
    }
    /**
     * Gets the annotation of the property in the specified entity class, property name and annotation class
     *
     * @param <T> annotation type
     * @param property the property to check
     * @param annotationClass the annotation class to check
     *
     * @return the annotations list of the specified property
     */
    public static <T extends Annotation> T getPropertyAnnotation(PropertyDescriptor property, Class<T> annotationClass) {
        T annotation = null;
        if (property != null && annotationClass != null) {
            annotation = getMethodAnnotation(property.getReadMethod(), annotationClass);
            if (annotation == null) {
                annotation = getMethodAnnotation(property.getWriteMethod(), annotationClass);
            }
        }
        return null;
    }
    /**
     * Gets the annotation of the property in the specified entity class, property name and annotation class
     *
     * @param <T> annotation type
     * @param targetClassName the instance class name to check
     * @param propName the property name to parse.
     * @param annotationClass the annotation class to check
     *
     * @return the annotations list of the specified property
     */
    public static <T extends Annotation> T getPropertyAnnotation(String targetClassName, String propName, Class<T> annotationClass) {
    	return getPropertyAnnotation(safeClass(targetClassName), propName, annotationClass);
    }
    /**
     * Gets the annotation of the property in the specified entity class, property name and annotation class
     *
     * @param <T> annotation type
     * @param targetClass the instance class to check
     * @param propName the property name to parse.
     * @param annotationClass the annotation class to check
     *
     * @return the annotations list of the specified property
     */
    public static <T extends Annotation> T getPropertyAnnotation(Class<?> targetClass, String propName, Class<T> annotationClass) {
        return getPropertyAnnotation(getProperty(targetClass, propName), annotationClass);
    }
    /**
     * Gets the annotation of the property in the specified entity instance, property name and annotation class
     *
     * @param <T> annotation type
     * @param target the instance class to check
     * @param propName the property name to parse.
     * @param annotationClass the annotation class to check
     *
     * @return the annotations list of the specified property
     */
    public static <T extends Annotation> T getPropertyAnnotation(Object target, String propName, Class<T> annotationClass) {
        return (target != null ? getPropertyAnnotation(target.getClass(), propName, annotationClass) : null);
    }

    /**
     * Get the actual type arguments of the specified bean field (if field is generic type)
     *
     * @param beanClassName 豆クラス名
     * @param name フィールド名
     *
     * @return the actual type arguments or empty array
     */
    public static Type[] getActualTypeArguments(String beanClassName, String name) {
    	return getActualTypeArguments(safeClass(beanClassName), name);
    }
    /**
     * Get the actual type arguments of the specified bean field (if field is generic type)
     *
     * @param beanClass 豆クラス
     * @param name フィールド名
     *
     * @return the actual type arguments or empty array
     */
    public static Type[] getActualTypeArguments(Class<?> beanClass, String name) {
        Field fd = getField(beanClass, name);
        if (fd != null && isInstanceOf(fd.getGenericType(), ParameterizedType.class)) {
            ParameterizedType pt = (ParameterizedType) fd.getGenericType();
            if (pt != null) return pt.getActualTypeArguments();
        }
        return new Type[] {};
    }
    /**
     * Get the first occurred actual type argument of the specified bean field (if field is generic type)
     *
     * @param beanClassName bean class name to parse
     * @param name field name
     *
     * @return the actual type arguments or NULL
     */
    public static Class<?> getFirstActualTypeArgument(String beanClassName, String name) {
    	return getFirstActualTypeArgument(safeClass(beanClassName), name);
    }
    /**
     * Get the first occurred actual type argument of the specified bean field (if field is generic type)
     *
     * @param beanClass bean class to parse
     * @param name field name
     *
     * @return the actual type arguments or NULL
     */
    public static Class<?> getFirstActualTypeArgument(Class<?> beanClass, String name) {
        Type[] types = getActualTypeArguments(beanClass, name);
        if (!CollectionUtils.isEmpty(types)) return (Class<?>) types[0];
        return null;
    }
    /**
     * Get the actual type arguments of the specified bean field (if field is generic type)
     *
     * @param bean instance to parse
     * @param name field name
     *
     * @return the actual type arguments or empty array
     */
    public static Type[] getActualTypeArguments(Object bean, String name) {
        return (bean != null ? getActualTypeArguments(bean.getClass(), name) : new Type[] {});
    }
    /**
     * Get the first occurred actual type argument of the specified bean field (if field is generic type)
     *
     * @param bean instance to parse
     * @param name field name
     *
     * @return the actual type arguments or NULL
     */
    public static Class<?> getFirstActualTypeArgument(Object bean, String name) {
        return (bean != null ? getFirstActualTypeArgument(bean.getClass(), name) : null);
    }

    /**
     * Convert the specified target to string (for DEBUG)
     *
     * @param target to convert
     *
     * @return the string target
     */
    public static String toString(Object target) {
        return toString(target, (String[]) null);
    }
    /**
     * Convert the specified target to string (for DEBUG)
     *
     * @param target to convert
     * @param properties property/field names to parse value
     *
     * @return the string target
     */
    public static String toString(Object target, String...properties) {
        String value = "[]";
        if (target != null) {
        	// parse field/property names list
            List<String> propsLst = new LinkedList<String>();
            if (!CollectionUtils.isEmpty(properties)) {
                propsLst.addAll(CollectionUtils.toList(properties));
            } else {
            	List<Field> fields = getFields(target);
                if (!CollectionUtils.isEmpty(fields)) {
                    for(Field field : fields) {
                        propsLst.add(field.getName());
                    }
                }
            }
            // apply values
            StringBuilder buff = new StringBuilder();
            if (!CollectionUtils.isEmpty(propsLst)) {
            	for(String property : propsLst) {
            		Object fval = getFieldValue(target, property);
            		if (buff.length() > 0) buff.append(", ");
            		String sval = (fval == null ? "NULL" : String.valueOf(fval));
            		String spair = MessageFormat.format("{0} = {1}", property, sval);
                    buff.append(spair);
            	}
            } else {
            	buff.append("[" + target.toString() + "]");
            }
            value = buff.toString();
        }
        return value;
    }

    /**
     * Convert object to bytes array
     *
     * @param obj to convert
     *
     * @return bytes array or empty if failed
     */
    public static byte[] toByteArray(Object obj) {
        // writes object
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        byte[] bytes = null;
        try {
            // creates output streams
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            // writes object
            oos.writeObject(obj);
            baos.flush();
            oos.flush();
            // gets binaries array of object
            bytes = baos.toByteArray();
        } catch (Exception e) {
        	LogUtils.logError(BeanUtils.class, e.getMessage());
            bytes = new byte[] {};
        }
        finally {
            // closes streams
            StreamUtils.closeQuitely(baos);
            StreamUtils.closeQuitely(oos);
        }
        return bytes;
    }
    /**
     * Convert object to bytes array
     *
     * @param obj to convert
     *
     * @return bytes array or empty if failed
     */
    public static ByteArrayInputStream toByteArrayInputStream(Object obj) {
        // writes object
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        byte[] bytes = null;
        try {
            // creates output streams
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            // writes object
            oos.writeObject(obj);
            baos.flush();
            oos.flush();
            // gets binaries array of object
            bytes = baos.toByteArray();
        } catch (Exception e) {
        	LogUtils.logError(BeanUtils.class, e.getMessage());
            bytes = new byte[] {};
        }
        finally {
            // closes streams
            StreamUtils.closeQuitely(baos);
            StreamUtils.closeQuitely(oos);
        }
        return (!CollectionUtils.isEmpty(bytes)
        		? new ByteArrayInputStream(bytes) : null);
    }

    /**
     * Convert bytes to object
     *
     * @param <T> the converted object type
     * @param bytes to convert
     * @param objClazz object class to return
     *
     * @return safe-type object or NULL if failed
     */
    public static <T> T fromByteArray(byte[] bytes, Class<T> objClazz) {
        T converted = null;
        if (!CollectionUtils.isEmpty(bytes)) {
            // reads object
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                // creates input streams
                bais = new ByteArrayInputStream(bytes);
                ois = new ObjectInputStream(bais);

                // reads object
                converted = safeType(ois.readObject(), objClazz);
            }
            catch (Exception e) {
            	LogUtils.logError(BeanUtils.class, e.getMessage());
                converted = null;
            }
            finally {
                // closes streams
                StreamUtils.closeQuitely(bais);
                StreamUtils.closeQuitely(ois);
            }
        }
        return converted;
    }

    /**
     * Map the specified data list to a simple list such as matrix (non recursive fields/properties)
     *
     * @param <T> data class type
     * @param dataClass data class
     * @param dataLst data list to map
     * @param properties property names array to export values. NULL for exporting all
     *
     * @return property values list such as matrix or empty/null if failed
     */
    public static <T> List<List<Object>> mapPropertyValuesList(Class<T> dataClass, List<T> dataLst, String...properties) {
        return mapPropertyValuesList(dataClass, Boolean.FALSE, dataLst, properties);
    }
    /**
     * Map the specified data list to a simple list such as matrix
     *
     * @param <T> data class type
     * @param dataClass data class
     * @param inherited specify searching recursive fields/properties
     * @param dataLst data list to map
     * @param properties property names array to export values. NULL for exporting all
     *
     * @return property values list such as matrix or empty/null if failed
     */
    public static <T> List<List<Object>> mapPropertyValuesList(
            Class<T> dataClass, Boolean inherited, List<T> dataLst, String...properties) {
        List<List<Object>> propValues = new LinkedList<List<Object>>();
        if (dataClass != null && !CollectionUtils.isEmpty(dataLst)) {
            // parse field/property names list
            List<String> propsLst = new LinkedList<String>();
            if (!CollectionUtils.isEmpty(properties)) {
                propsLst.addAll(CollectionUtils.toList(properties));
            } else {
                List<Field> fields = getFields(dataClass, inherited);
                if (!CollectionUtils.isEmpty(fields)) {
                    for(Field field : fields) {
                        propsLst.add(field.getName());
                    }
                }
            }
            // export property values
            if (!CollectionUtils.isEmpty(propsLst)) {
                for(T data : dataLst) {
                    List<Object> propValue = new LinkedList<Object>();
                    for(String property : propsLst) {
                        if (!StringUtils.hasText(property)) continue;
                        propValue.add(getFieldValue(data, property));
                    }
                    propValues.add(propValue);
                }
            }
        }
        return propValues;
    }

    /**
     * Map the specified data list to a simple list such as matrix (non recursive fields/properties)
     *
     * @param <T> data class type
     * @param dataClass data class
     * @param dataLst data list to map
     * @param properties property names array to export values. NULL for exporting all
     *
     * @return property values list such as matrix or empty/null if failed
     */
    public static <T> List<Map<String, Object>> mapPropertyValuesMap(
            Class<T> dataClass, List<T> dataLst, String...properties) {
        return mapPropertyValuesMap(dataClass, Boolean.FALSE, dataLst, properties);
    }
    /**
     * Map the specified data list to a simple list such as matrix
     *
     * @param <T> data class type
     * @param dataClass data class
     * @param inherited specify searching recursive fields/properties
     * @param dataLst data list to map
     * @param properties property names array to export values. NULL for exporting all
     *
     * @return property values list such as matrix or empty/null if failed
     */
    public static <T> List<Map<String, Object>> mapPropertyValuesMap(
            Class<T> dataClass, Boolean inherited, List<T> dataLst, String...properties) {
        List<Map<String, Object>> propValues = new LinkedList<Map<String, Object>>();
        if (dataClass != null && !CollectionUtils.isEmpty(dataLst)) {
            // parse field/property names list
            List<String> propsLst = new LinkedList<String>();
            if (!CollectionUtils.isEmpty(properties)) {
                propsLst.addAll(CollectionUtils.toList(properties));
            } else {
                List<Field> fields = getFields(dataClass, inherited);
                if (!CollectionUtils.isEmpty(fields)) {
                    for(Field field : fields) {
                        propsLst.add(field.getName());
                    }
                }
            }
            // export property values
            if (!CollectionUtils.isEmpty(propsLst)) {
                for(T data : dataLst) {
                    Map<String, Object> propValue = new LinkedHashMap<String, Object>();
                    for(String property : propsLst) {
                        if (!StringUtils.hasText(property)) continue;
                        propValue.put(property, getFieldValue(data, property));
                    }
                    propValues.add(propValue);
                }
            }
        }
        return propValues;
    }

    /**
     * Map the specified data matrix to the object list by the properties array order
     *
     * @param <T> data class type
     * @param dataClass data class
     * @param dataLst data matrix to map
     * @param properties property names array to apply values. NULL for applying all
     *
     * @return the object list of the specified data class
     */
    public static <T> List<T> asDataList(Class<T> dataClass, List<List<Object>> dataLst, String...properties) {
        List<T> objLst = new LinkedList<T>();
        if (dataClass != null && !CollectionUtils.isEmpty(dataLst)) {
            // parse field/property names list
            List<String> propsLst = new LinkedList<String>();
            if (!CollectionUtils.isEmpty(properties)) {
                propsLst.addAll(CollectionUtils.toList(properties));
            } else {
                List<Field> fields = getFields(dataClass);
                if (!CollectionUtils.isEmpty(fields)) {
                    for(Field field : fields) {
                        propsLst.add(field.getName());
                    }
                }
            }
            // apply values
            if (!CollectionUtils.isEmpty(propsLst)) {
                for(List<Object> data : dataLst) {
                    // create new instance of data class
                    T target = safeNewInstance(dataClass);
                    // if could not creating new instance of data class;
                    // then breaking here
                    if (target == null) return objLst;

                    // apply property values
                    int[] propertieSizes = new int[] { propsLst.size(), data.size() };
                    int maxProperties = NumberUtils.min(propertieSizes);
                    for(int i = 0; i < maxProperties; i++) {
                        String property = propsLst.get(i);
                        Object value = data.get(i);
                        setFieldValue(target, property, value);
                    }
                    objLst.add(target);
                }
            }
        }
        return objLst;
    }

    /**
     * Get a not null value from the specified arguments
     * @param args to check
     * @return not null value or null
     */
    public static Object coalesce(Object...args) {
        Object val = null;
        if (!CollectionUtils.isEmpty(args)) {
            for(Object arg : args) {
                val = (val == null ? arg : val);
                if (val != null) break;
            }
        }
        return val;
    }
}
