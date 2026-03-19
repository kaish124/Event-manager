package com.reza.events.http;

import com.reza.events.logging.LogKeys;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.MDC;
import org.apache.commons.lang3.StringUtils;

/**
 * ThreadLocal store for per-request context.
 */
public final class RequestStateHolder {

    // The ThreadLocal container. One RequestInfo instance per active thread.
    private static final ThreadLocal<RequestInfo> HOLDER = new ThreadLocal<>();

    private RequestStateHolder() {}


    /**
     * Initialize request state at the start of a request.
     * Called by RequestStateFilter before the filter chain proceeds.
     *
     * @param txnId     unique ID for this request (from header or generated)
     * @param userId    authenticated user's ID (null if not yet authenticated)
     * @param uri       request URI, e.g. /services/events/v1
     * @param method    HTTP method, e.g. GET
     * @param marketCode market/region code from request header
     * @param source    source type: WEB, MOBILE, API
     */
    public static void setupRequest(String txnId, Long userId, String uri,
                                    String method, String marketCode, String source) {
        RequestInfo info = new RequestInfo();
        info.setTxnId(txnId);
        info.setUserId(userId);
        info.setUri(uri);
        info.setMethod(method);
        info.setMarketCode(marketCode);
        info.setSource(source);
        HOLDER.set(info);

        // Populate MDC — Logback pattern reads these via %X{KEY}
        putMDC(LogKeys.TXN_ID, txnId);
        putMDC(LogKeys.URI,    uri);
        putMDC(LogKeys.METHOD, method);
        putMDC(LogKeys.MARKET, StringUtils.defaultString(marketCode));
        putMDC(LogKeys.SOURCE, StringUtils.defaultString(source));
        if (userId != null) {
            MDC.put(LogKeys.USER_ID, String.valueOf(userId));
        }
    }

    /**
     * Clear all ThreadLocal and MDC state at the end of a request.
     * MUST be called in a finally block in RequestStateFilter.
     * Forgetting this leaks state to the next request that reuses this thread.
     */
    public static void teardownRequest() {
        HOLDER.remove();
        MDC.remove(LogKeys.TXN_ID);
        MDC.remove(LogKeys.USER_ID);
        MDC.remove(LogKeys.URI);
        MDC.remove(LogKeys.METHOD);
        MDC.remove(LogKeys.MARKET);
        MDC.remove(LogKeys.SOURCE);
        MDC.remove(LogKeys.APP_ID);
    }

    /**
     * Set the authenticated user's ID.
     * Called from JwtAuthenticationProvider after the JWT is validated — the user
     * is not known at filter entry, only after authentication succeeds.
     */
    public static void setUserId(Long userId) {
        RequestInfo info = getRequestInfo();
        if (info != null) {
            info.setUserId(userId);
            putMDC(LogKeys.USER_ID, String.valueOf(userId));
        }
    }

    public static void setAppId(Long appId) {
        RequestInfo info = getRequestInfo();
        if (info != null) {
            info.setAppId(appId);
            putMDC(LogKeys.APP_ID, appId == null ? "" : String.valueOf(appId));
        }
    }

    /** Returns the RequestInfo for the current thread. Null if setupRequest was not called. */
    public static RequestInfo getRequestInfo() {
        return HOLDER.get();
    }

    public static String getTxnId() {
        RequestInfo info = getRequestInfo();
        return info != null ? info.getTxnId() : null;
    }

    public static Long getUserId() {
        RequestInfo info = getRequestInfo();
        return info != null ? info.getUserId() : null;
    }

    public static String getUri() {
        RequestInfo info = getRequestInfo();
        return info != null ? info.getUri() : null;
    }

    private static void putMDC(String key, String value) {
        if (value != null) {
            MDC.put(key, value);
        }
    }

    /**
     * Plain data holder. No Spring dependencies — just getters and setters.
     * Stored in the ThreadLocal.
     */
    @Getter
    @Setter
    public static class RequestInfo {
        private String txnId;
        private Long   userId;
        private Long   appId;
        private String uri;
        private String method;
        private String marketCode;
        private String source;

    }
}


