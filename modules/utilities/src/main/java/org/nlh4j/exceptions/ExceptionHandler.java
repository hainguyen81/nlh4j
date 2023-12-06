package org.nlh4j.exceptions;

import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;

import lombok.extern.slf4j.Slf4j;

/**
 * Handle {@link Exception}
 */
@Slf4j
public class ExceptionHandler extends com.machinezoo.noexception.ExceptionHandler {

	/** for logging */
	private final Logger logger;
	private final boolean silent;
	/** to custom handling exception */
	private final Function<Throwable, Boolean> exceptionCallback;

	/**
	 * Initialize a new instance of {@link ExceptionHandler}
	 * 
	 * @param logger {@link Logger}
	 * @param silent true for just logging when not handling exception; else throwing {@link ApplicationException}
	 * @param exceptionCallback callback for handling exception
	 */
	public ExceptionHandler(Logger logger, boolean silent, Function<Throwable, Boolean> exceptionCallback) {
		this.logger = logger;
		this.silent = silent;
		this.exceptionCallback = exceptionCallback;
	}
	/**
	 * Initialize a new instance of {@link ExceptionHandler}
	 * 
	 * @param logger {@link Logger}
	 * @param exceptionCallback callback for handling exception
	 */
	public ExceptionHandler(Logger logger, Function<Throwable, Boolean> exceptionCallback) {
		this(logger, true, exceptionCallback);
	}
	/**
	 * Initialize a new instance of {@link ExceptionHandler}
	 * 
	 * @param logger {@link Logger}
	 */
	public ExceptionHandler(Logger logger) {
		this(logger, true, null);
	}

	@Override
	public boolean handle(Throwable e) {
		Logger exLogger = Optional.ofNullable(this.logger).orElse(log);
		boolean handled = false;
		try {
			handled = Optional.ofNullable(exceptionCallback).map(c -> c.apply(e)).orElse(Boolean.FALSE);
		} catch (Exception e1) {
			exLogger.warn(e1.getMessage());
		}

		// silent
		if (!handled && silent) {
			if (exLogger.isDebugEnabled() || exLogger.isTraceEnabled()) {
				exLogger.error(e.getMessage(), e);

			} else {
				exLogger.warn(e.getMessage());
			}
			handled = true;
		}
		return handled;
	}
}
