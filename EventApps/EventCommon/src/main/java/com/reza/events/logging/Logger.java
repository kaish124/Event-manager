package com.reza.events.logging;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Objects;

/**
 * SLF4J wrapper that formats log calls as Splunk-friendly key=value pairs.
 *
 * Usage:
 *   LOGGER.info(LogKeys.MSG, "Event created", LogKeys.EVENT_ID, eventId);
 *
 * Produces:
 *   MSG="Event created" EVENT_ID=42
 *
 * Rules:
 *   - Always pass an even number of arguments: (key, value, key, value, ...)
 *   - LogKeys.MSG is the special key whose value becomes the SLF4J message text.
 *   - All other key=value pairs are stored in MDC["LOG_DATA"] and appended by Logback.
 *   - MDC is cleaned up after every log call — no leakage between calls.
 */
public class Logger {

    private static final String NULL_TEXT = "<null>";
    private static final String LOG_DATA_MDC_KEY = "LOG_DATA";
    private static final String LINE_NO_MDC_KEY = "LINE_NO";

    // Read once at startup from system property — not from Spring Environment
    // because this class initializes before Spring context is ready.
    private static final boolean LOG_LINE_NUMBER =
            Boolean.parseBoolean(System.getProperty("log.line.number", "true"));

    private final org.slf4j.Logger delegate;

    Logger(Class<?> clazz) {
        this.delegate = LoggerFactory.getLogger(clazz);
    }

    Logger(String name) {
        this.delegate = LoggerFactory.getLogger(name);
    }

    // ─── Level checks ────────────────────────────────────────────────────────

    public boolean isTraceEnabled() { return delegate.isTraceEnabled(); }
    public boolean isDebugEnabled() { return delegate.isDebugEnabled(); }
    public boolean isInfoEnabled()  { return delegate.isInfoEnabled(); }
    public boolean isWarnEnabled()  { return delegate.isWarnEnabled(); }
    public boolean isErrorEnabled() { return delegate.isErrorEnabled(); }

    // ─── TRACE ───────────────────────────────────────────────────────────────

    /** Key-value pair style: trace(MSG, "text", KEY, value, ...) */
    public void trace(Object... args) {
        if (isTraceEnabled()) {
            setLineNo();
            delegate.trace(buildMessage(args));
            cleanupMDC();
        }
    }

    /** SLF4J placeholder style: traceMsg("Processing {}", value) */
    public void traceMsg(String message, Object... args) {
        if (isTraceEnabled()) {
            setLineNo();
            delegate.trace(message, args);
            cleanupMDC();
        }
    }

    // ─── DEBUG ───────────────────────────────────────────────────────────────

    public void debug(Object... args) {
        if (isDebugEnabled()) {
            setLineNo();
            delegate.debug(buildMessage(args));
            cleanupMDC();
        }
    }

    public void debugMsg(String message, Object... args) {
        if (isDebugEnabled()) {
            setLineNo();
            delegate.debug(message, args);
            cleanupMDC();
        }
    }

    // ─── INFO ────────────────────────────────────────────────────────────────

    public void info(Object... args) {
        if (isInfoEnabled()) {
            setLineNo();
            delegate.info(buildMessage(args));
            cleanupMDC();
        }
    }

    public void infoMsg(String message, Object... args) {
        if (isInfoEnabled()) {
            setLineNo();
            delegate.info(message, args);
            cleanupMDC();
        }
    }

    // ─── WARN ────────────────────────────────────────────────────────────────

    /** warn() does NOT check isWarnEnabled() — WARN is nearly always on. */
    public void warn(Object... args) {
        setLineNo();
        delegate.warn(buildMessage(args));
        cleanupMDC();
    }

    public void warn(Throwable throwable, Object... args) {
        setLineNo();
        prepareExceptionMDC(throwable);
        delegate.warn(buildMessage(args));
        cleanupMDC();
    }

    // ─── ERROR ───────────────────────────────────────────────────────────────

    /** error() does NOT check isErrorEnabled() — ERROR is always on. */
    public void error(Object... args) {
        setLineNo();
        delegate.error(buildMessage(args));
        cleanupMDC();
    }

    public void error(Throwable throwable, Object... args) {
        setLineNo();
        prepareExceptionMDC(throwable);
        delegate.error(buildMessage(args));
        cleanupMDC();
    }

    /** Compatibility with SLF4J two-arg style: error("message", throwable) */
    public void error(String message, Throwable throwable) {
        setLineNo();
        prepareExceptionMDC(throwable);
        delegate.error(buildMessage(message));
        cleanupMDC();
    }

    // ─── Internal helpers ────────────────────────────────────────────────────

    /**
     * Converts the varargs into a Splunk key=value string stored in MDC["LOG_DATA"].
     *
     * Input:  (MSG, "User logged in", USER_ID, 42, STATUS, "active")
     * MDC:    LOG_DATA = "USER_ID=42 STATUS=active"
     * Return: "User logged in"   ← this becomes the SLF4J message
     *
     * If no MSG key is present, returns null (Logback prints "null" for the message).
     * If an odd number of args is passed, logs an error and returns null.
     */
    private String buildMessage(Object... args) {
        if (args == null || args.length == 0) {
            return null;
        }
        if (args.length == 1) {
            // Single-arg shortcut: logger.debug("Done") or logger.error(exception)
            return stringify(args[0]);
        }
        if (args.length % 2 != 0) {
            delegate.error("Logger called with odd number of arguments: {}",
                    ArrayUtils.toString(args));
            return null;
        }

        StringBuilder logData = new StringBuilder(
                Objects.toString(MDC.get(LOG_DATA_MDC_KEY), ""));
        String message = null;

        for (int i = 0; i < args.length; i += 2) {
            String key   = Objects.toString(args[i], "");
            Object value = args[i + 1];

            if (LogKeys.MSG.equals(key)) {
                message = stringify(value);
            } else {
                if (logData.length() > 0) {
                    logData.append(" ");
                }
                logData.append(key).append("=");
                String strValue = stringify(value);
                if (StringUtils.containsWhitespace(strValue)) {
                    logData.append("\"").append(strValue).append("\"");
                } else {
                    logData.append(strValue);
                }
            }
        }
        MDC.put(LOG_DATA_MDC_KEY, logData.toString());
        return message;
    }

    /**
     * Appends the full stack trace to MDC["LOG_DATA"] as TRACE="...".
     * Called before buildMessage() when an exception is passed to the log method.
     */
    private void prepareExceptionMDC(Throwable throwable) {
        StringBuilder logData = new StringBuilder(
                Objects.toString(MDC.get(LOG_DATA_MDC_KEY), ""));
        if (logData.length() > 0) logData.append(" ");
        logData.append(LogKeys.TRACE)
               .append("=\"")
               .append(ExceptionUtils.getStackTrace(throwable))
               .append("\"");
        MDC.put(LOG_DATA_MDC_KEY, logData.toString());
    }

    /**
     * Captures the calling line number into MDC["LINE_NO"].
     * Stack index 3: [0]=Thread.getStackTrace, [1]=setLineNo, [2]=trace/debug/info/..., [3]=caller
     */
    private void setLineNo() {
        if (LOG_LINE_NUMBER) {
            StackTraceElement caller = Thread.currentThread().getStackTrace()[3];
            MDC.put(LINE_NO_MDC_KEY, Integer.toString(caller.getLineNumber()));
        }
    }

    /** Removes the per-call MDC keys. Request-scoped MDC keys are NOT touched here. */
    void cleanupMDC() {
        MDC.remove(LINE_NO_MDC_KEY);
        MDC.remove(LOG_DATA_MDC_KEY);
    }

    private static String stringify(Object obj) {
        if (obj == null) return NULL_TEXT;
        if (obj instanceof String s) return s;
        if (obj instanceof Throwable t) return ExceptionUtils.getStackTrace(t);
        if (obj.getClass().isArray()) return ArrayUtils.toString(obj);
        return obj.toString();
    }
}


