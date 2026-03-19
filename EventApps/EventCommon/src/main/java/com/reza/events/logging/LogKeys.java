package com.reza.events.logging;


public final class LogKeys {

    // ─── Special key — always use first ──────────────────────────────────────
    /** The human-readable message. Logger treats this key specially. */
    public static final String MSG = "MSG";

    // ─── Request / HTTP context ───────────────────────────────────────────────
    public static final String URI        = "URI";
    public static final String METHOD     = "METHOD";
    public static final String PATH       = "PATH";
    public static final String STATUS     = "STATUS";
    public static final String TXN_ID     = "TXN_ID";      // correlation ID per request
    public static final String APP_ID     = "APP_ID";      // client application ID
    public static final String MARKET     = "MARKET";      // market/region code
    public static final String SOURCE     = "SOURCE";      // WEB, MOBILE, API, etc.

    // ─── User / auth ──────────────────────────────────────────────────────────
    public static final String USER_ID    = "USER_ID";
    public static final String EMAIL      = "EMAIL";
    public static final String ROLE       = "ROLE";
    public static final String TOKEN      = "TOKEN";

    // ─── Errors ───────────────────────────────────────────────────────────────
    public static final String EXCEPTION      = "EXCEPTION";    // the Throwable object
    public static final String ERROR_MESSAGE  = "ERROR_MESSAGE"; // exception.getMessage()
    public static final String TRACE          = "TRACE";        // full stack trace string

    // ─── Performance ──────────────────────────────────────────────────────────
    public static final String API   = "API";   // name of the operation being timed
    public static final String TIME  = "TIME";  // duration in milliseconds

    // ─── Business entities ────────────────────────────────────────────────────
    public static final String EVENT_ID        = "EVENT_ID";
    public static final String REGISTRATION_ID = "REGISTRATION_ID";
    public static final String SESSION_ID      = "SESSION_ID";

    private LogKeys() {}
}


