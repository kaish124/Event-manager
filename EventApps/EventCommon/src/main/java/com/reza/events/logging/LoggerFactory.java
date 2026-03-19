package com.reza.events.logging;

/**
 * Factory for the custom Logger wrapper.
 *
 * Usage:
 *   private static final Logger LOGGER = LoggerFactory.getLogger(MyClass.class);
 *   private static final Logger PERF   = LoggerFactory.getLogger("PERFORMANCE");
 */
public final class LoggerFactory {

    private LoggerFactory() {}

    /** Creates a Logger named after the given class. */
    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz);
    }

    /**
     * Creates a Logger with the given string name.
     * Use this for named loggers that route to separate files (e.g., "PERFORMANCE").
     */
    public static Logger getLogger(String name) {
        return new Logger(name);
    }
}


