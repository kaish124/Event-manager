package com.reza.events.http;

import jakarta.servlet.http.HttpServletRequest;

public class RequestStateHolder {
    private static final ThreadLocal<RequestState> HOLDER = new ThreadLocal<RequestState>();

    private RequestStateHolder() {}

    public static void setupRequest(HttpServletRequest request) {
        RequestState state = new RequestState();
        state.requestUri = request.getRequestURI();
        state.requestMethod = request.getMethod();
        HOLDER.set(state);
    }

    public static void teardownRequest() {
        HOLDER.remove();
    }

    public static void setUserId(Long userId) {
        RequestState state = HOLDER.get();
        if(state != null)
            state.userId = userId;
    }

    public static Long getUserId() {
        RequestState state = HOLDER.get();
        return state != null ? state.userId : null;
    }

    public static String getRequestUri() {
        RequestState state = HOLDER.get();
        return state != null ? state.requestUri : null;
    }

    public static String getRequestMethod() {
        RequestState state = HOLDER.get();
        return state != null ? state.requestMethod : null;
    }

    private static class RequestState{
        Long userId;
        String requestUri;
        String requestMethod;
    }
}
