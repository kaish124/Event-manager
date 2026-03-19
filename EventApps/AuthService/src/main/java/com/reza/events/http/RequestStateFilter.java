package com.reza.events.http;

import com.reza.events.logging.LogKeys;
import com.reza.events.logging.Logger;
import com.reza.events.logging.LoggerFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Servlet filter that initializes per-request logging context.
 * <p>
 * This MUST run first (lowest order) among all filters so that every
 * subsequent filter, provider, and controller finds MDC already populated.
 * <p>
 * It is registered in WebConfig with order = Integer.MIN_VALUE.
 *
 */
public class RequestStateFilter extends OncePerRequestFilter {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(RequestStateFilter.class);

    private static final Logger PERFORMANCE_LOGGER =
            LoggerFactory.getLogger("PERFORMANCE");

    // Header key constants — define these in a HeaderKeys class or inline here
    private static final String HEADER_TXN_ID = "X-TXN-ID";
    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_APP_ID = "X-App-Id";
    private static final String HEADER_MARKET_CODE = "X-Market-Code";
    private static final String HEADER_SOURCE_TYPE = "X-Source-Type";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        StopWatch watch = new StopWatch();
        watch.start();

        String uri = request.getRequestURI();
        String method = request.getMethod();

        try {
            setupRequestState(request, uri, method);

            LOGGER.debug(LogKeys.MSG, "STARTED",
                    LogKeys.URI, uri,
                    LogKeys.METHOD, method);

            filterChain.doFilter(request, response);

        } finally {
            watch.stop();

            LOGGER.debug(LogKeys.MSG, "FINISHED");

            // Performance log — goes to separate file via named logger "PERFORMANCE"
            PERFORMANCE_LOGGER.info(LogKeys.URI, uri,
                    LogKeys.METHOD, method,
                    LogKeys.TIME, watch.getTime());

            RequestStateHolder.teardownRequest();
        }
    }

    /**
     * Reads request headers and populates RequestStateHolder + MDC.
     * <p>
     * TXN_ID: if the caller provides one (for distributed tracing across services),
     * use it. Otherwise generate a new UUID. This ensures every request has a
     * correlation ID whether or not the caller supplied one.
     */
    private void setupRequestState(HttpServletRequest request,
                                   String uri, String method) {
        String txnId = request.getHeader(HEADER_TXN_ID);
        if (StringUtils.isBlank(txnId)) {
            txnId = UUID.randomUUID().toString();
        }

        String userIdHeader = request.getHeader(HEADER_USER_ID);
        Long userId = parseOptionalLong(userIdHeader);

        String marketCode = request.getHeader(HEADER_MARKET_CODE);
        String sourceType = request.getHeader(HEADER_SOURCE_TYPE);

        RequestStateHolder.setupRequest(txnId, userId, uri, method, marketCode, sourceType);

        String appIdHeader = request.getHeader(HEADER_APP_ID);
        Long appId = parseOptionalLong(appIdHeader);
        if (appId != null) {
            RequestStateHolder.setAppId(appId);
        }
    }

    private Long parseOptionalLong(String value) {
        if (StringUtils.isBlank(value)) return null;
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

