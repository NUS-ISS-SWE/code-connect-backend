package com.nus.iss.config;

import com.nus.iss.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.nus.iss.config.AppConstants.*;

@Configuration
public class GatewayConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public GatewayConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Swagger API doc routes (just for aggregation)
                .route("user-docs", r -> r.path(USER_ROUTE_SWAGGER)
                        .uri(USER_SERVICE_URL))
                .route("admin-docs", r -> r.path(ADMIN_ROUTE_SWAGGER)
                        .uri(ADMIN_SERVICE_URL))
                .route("job-docs", r -> r.path(JOB_ROUTE_SWAGGER)
                        .uri(JOB_SERVICE_URL))
                .route("interview-docs", r -> r.path(INTERVIEW_SERVICE_ROUTE_SWAGGER)
                        .uri(INTERVIEW_SERVICE_URL))

                // ==== USER SERVICE ROUTES (localhost:8081) ====
                .route("user_register", r -> r.path(USER_ROUTE_REGISTER)
                        .uri(USER_SERVICE_URL))
                .route("user_activate", r -> r.path(USER_ROUTE_ACTIVATE)
                        .uri(USER_SERVICE_URL))
                .route("user_login", r -> r.path(USER_ROUTE_LOGIN)
                        .uri(USER_SERVICE_URL))
                .route("compliance_data", r -> r.path(USER_ROUTE_COMPLIANCE_DATA)
                        .uri(USER_SERVICE_URL))

                // ==== ADMIN SERVICE ROUTES (localhost:8082) ====
                .route("admin_get_employers", r -> r.path(ADMIN_ROUTE_GET_ALL_EMPLOYERS)
                        .filters(f -> f.filter(jwtAuthenticationFilter.jwtRoleFilter(List.of(ADMIN))))
                        .uri(ADMIN_SERVICE_URL))
                .route("admin_review_employer", r -> r.path(ADMIN_ROUTE_REVIEW_EMPLOYER_PROFILE)
                        .filters(f -> f.filter(jwtAuthenticationFilter.jwtRoleFilter(List.of(ADMIN))))
                        .uri(ADMIN_SERVICE_URL))
                .route("admin_delete_employees", r -> r.path(ADMIN_ROUTE_DELETE_EMPLOYERS)
                        .filters(f -> f.filter(jwtAuthenticationFilter.jwtRoleFilter(List.of(ADMIN))))
                        .uri(ADMIN_SERVICE_URL))

                // ==== JOB SERVICE ROUTES (localhost:8083) ====

                .build();
    }
}
