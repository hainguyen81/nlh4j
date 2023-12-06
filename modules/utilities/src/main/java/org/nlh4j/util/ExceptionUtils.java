package org.nlh4j.util;

import java.io.Serializable;
import java.util.function.Function;

import org.nlh4j.exceptions.ApplicationException;
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
}
