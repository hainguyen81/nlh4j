package org.nlh4j.util;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

import org.nlh4j.exceptions.ApplicationException;
import org.nlh4j.exceptions.ApplicationLicenseException;
import org.nlh4j.exceptions.ApplicationNotSupportedException;
import org.nlh4j.exceptions.ApplicationUnderConstructionException;
import org.nlh4j.exceptions.ApplicationValidationException;
import org.nlh4j.exceptions.ExceptionHandler;
import org.slf4j.Logger;

import lombok.extern.slf4j.Slf4j;

/**
 * Handle {@link Exception}
 */
@Slf4j
public final class ExceptionUtils implements Serializable {

    /**
     * default serial version id
     */
    private static final long serialVersionUID = 1L;

    /**
     * Wrap {@link Exception}
     * 
     * @param exceptionCallback callback when exception occurs
     * 
     * @return {@link ExceptionHandler}
     */
    public static ExceptionHandler wrap(Function<Throwable, Boolean> exceptionCallback) {
    	return new ExceptionHandler(null, exceptionCallback);
    }
    
    /**
     * Wrap {@link Exception}
     * 
     * @param logger {@link Logger}
     * @param exceptionCallback callback when exception occurs
     * 
     * @return {@link ExceptionHandler}
     */
    public static ExceptionHandler wrap(Logger logger, Function<Throwable, Boolean> exceptionCallback) {
    	return new ExceptionHandler(logger, exceptionCallback);
    }
    
    /**
     * Wrap {@link Exception}
     * 
     * @param logger {@link Logger}
	 * @param silent true for just logging when not handling exception; else throwing {@link ApplicationException}
	 * @param exceptionCallback callback for handling exception
     * 
     * @return {@link ExceptionHandler}
     */
    public static ExceptionHandler wrap(Logger logger, boolean silent, Function<Throwable, Boolean> exceptionCallback) {
    	return new ExceptionHandler(logger, silent, exceptionCallback);
    }
    
    /**
     * Wrap {@link Exception}
     * 
     * @param logger {@link Logger}
     * 
     * @return {@link ExceptionHandler}
     */
    public static ExceptionHandler wrap(Logger logger) {
    	return new ExceptionHandler(logger);
    }
    
    /**
     * Wrap {@link Exception}
     * 
     * @return {@link ExceptionHandler}
     */
    public static ExceptionHandler wrap() {
    	return new ExceptionHandler(log);
    }
    
    /**
     * Trace {@link Throwable}
     * 
     * @param logger
     * 		  {@link Logger}
     * @param e
     * 		  {@link Throwable}
     */
    public static void traceException(Logger logger, Throwable e) {
    	if (logger == null || e == null) {
    		return;
    	}

    	if (logger.isDebugEnabled() || logger.isTraceEnabled()) {
    		logger.error(e.getMessage(), e);

    	} else {
    		logger.warn(e.getMessage());
    	}
    }
    /**
     * Trace {@link Throwable}
     * 
     * @param e
     * 		  {@link Throwable}
     */
    public static void traceException(Throwable e) {
    	traceException(log, e);
    }
    
    /**
     * Get a boolean value indicating the specified exception whether is a kind of the specified exception class
     * 
     * @param <T> exception type
     * @param ex to check
     * @param exceptionClass exception class
     * 
     * @return true for exactly kind of the specified exception class; else false
     */
    public static <T extends Throwable> boolean isKindOfException(Throwable ex, Class<T> exceptionClass) {
    	return (ex != null && exceptionClass != null && Objects.nonNull(getExceptionCause(ex, exceptionClass)));
    }
    
    /**
     * Get the cause of the specified {@link Exception} by the specified exception class
     * 
     * @param <T> exception type
     * @param ex to parse
     * @param exceptionClass exception class
     * 
     * @return the cause of the specified {@link Exception} by the specified exception class or NULL
     */
    public static <T extends Throwable> T getExceptionCause(Throwable ex, Class<T> exceptionClass) {
    	if (ex == null && exceptionClass == null
    			|| BeanUtils.isInstanceOf(ex, exceptionClass)) {
    		return BeanUtils.safeType(ex, exceptionClass);

    	}
    	return BeanUtils.safeType(ex.getCause(), exceptionClass);
    }

    /**
     * Get a boolean value indicating the specified exception whether is {@link ApplicationValidationException}
     * 
     * @param ex to check
     * 
     * @return true for exactly {@link ApplicationValidationException}; else false
     */
    public static boolean isKindOfValidationException(Throwable ex) {
    	return isKindOfException(ex, ApplicationValidationException.class);
    }

    /**
     * Get a boolean value indicating the specified exception whether is {@link ApplicationUnderConstructionException}
     * 
     * @param ex to check
     * 
     * @return true for exactly {@link ApplicationUnderConstructionException}; else false
     */
    public static boolean isKindOfUnderConstructionException(Throwable ex) {
    	return isKindOfException(ex, ApplicationUnderConstructionException.class);
    }

    /**
     * Get a boolean value indicating the specified exception whether is {@link ApplicationLicenseException}
     * 
     * @param ex to check
     * 
     * @return true for exactly {@link ApplicationLicenseException}; else false
     */
    public static boolean isKindOfLicenseException(Throwable ex) {
    	return isKindOfException(ex, ApplicationLicenseException.class);
    }

    /**
     * Get a boolean value indicating the specified exception whether is {@link ApplicationNotSupportedException}
     * 
     * @param ex to check
     * 
     * @return true for exactly {@link ApplicationNotSupportedException}; else false
     */
    public static boolean isKindOfNotSupportedException(Throwable ex) {
    	return isKindOfException(ex, ApplicationNotSupportedException.class);
    }
}
