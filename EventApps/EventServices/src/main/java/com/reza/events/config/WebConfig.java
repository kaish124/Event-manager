package com.reza.events.config;

import com.reza.events.http.RequestStateFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    /**
     * Registers RequestStateFilter as a servlet filter.
     *
     * URL pattern: /services/*
     *   - Covers all your API endpoints (e.g., /services/events/v1, /services/users/v1)
     *   - Excludes /health, /actuator, /auth/login, /public/* — no MDC needed there
     *
     * Order: Integer.MIN_VALUE (-2147483648)
     *   - Ensures this runs before EVERY other filter, including Spring Security filters.
     *   - By the time JwtAuthenticationFilter runs, MDC is already populated with
     *     TXN_ID, URI, METHOD — so auth errors carry full request context automatically.
     *
     * Why not @Component on RequestStateFilter?
     *   @Component auto-registers with defaults: ALL URLs, default order.
     *   FilterRegistrationBean gives explicit URL pattern + order control.
     *   Using both at once would register the filter TWICE.
     */
    @Bean
    public FilterRegistrationBean<RequestStateFilter> requestStateFilterRegistration() {
        FilterRegistrationBean<RequestStateFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(new RequestStateFilter());

        // Only fire on API paths — not health checks, actuator, static resources
        registration.addUrlPatterns("/services/*");

        // Integer.MIN_VALUE = run before everything else in the filter chain
        registration.setOrder(Integer.MIN_VALUE);

        return registration;
    }
}

