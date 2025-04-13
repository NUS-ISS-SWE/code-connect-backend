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
    private final CdcntProperties cdcntProperties;

    @Autowired
    public GatewayConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CdcntProperties cdcntProperties) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.cdcntProperties = cdcntProperties;
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Swagger API doc routes (just for aggregation)
                .route("user-docs", r -> r.path(USER_ROUTE_SWAGGER)
                        .uri(cdcntProperties.getUserService()))
                .route("admin-docs", r -> r.path(ADMIN_ROUTE_SWAGGER)
                        .uri(cdcntProperties.getAdminService()))
                .route("job-docs", r -> r.path(JOB_ROUTE_SWAGGER)
                        .uri(cdcntProperties.getJobService()))
                .route("interview-docs", r -> r.path(INTERVIEW_SERVICE_ROUTE_SWAGGER)
                        .uri(cdcntProperties.getInterviewPrepService()))

                // ==== USER SERVICE ROUTES (localhost:8081) ====
                .route("user_register", r -> r.path(USER_ROUTE_REGISTER)
                        .uri(cdcntProperties.getUserService()))
                .route("user_activate", r -> r.path(USER_ROUTE_ACTIVATE)
                        .uri(cdcntProperties.getUserService()))
                .route("user_login", r -> r.path(USER_ROUTE_LOGIN)
                        .uri(cdcntProperties.getUserService()))
                .route("user_employer_profile", r -> r.path(USER_ROUTE_EMPLOYER_PROFILE)
                        .filters(f -> f.filter(jwtAuthenticationFilter.jwtRoleFilter(List.of(EMPLOYER))))
                        .uri(cdcntProperties.getUserService()))
                .route("user_employer_profile_picture", r -> r.path(USER_ROUTE_EMPLOYER_PROFILE_PICTURE)
                        .filters(f -> f.filter(jwtAuthenticationFilter.jwtRoleFilter(List.of(EMPLOYER))))
                        .uri(cdcntProperties.getUserService()))

                // ==== ADMIN SERVICE ROUTES (localhost:8082) ====
                .route("admin_get_employers", r -> r.path(ADMIN_ROUTE_GET_ALL_EMPLOYERS)
                        .filters(f -> f.filter(jwtAuthenticationFilter.jwtRoleFilter(List.of(ADMIN))))
                        .uri(cdcntProperties.getAdminService()))
                .route("admin_review_employer", r -> r.path(ADMIN_ROUTE_REVIEW_EMPLOYER_PROFILE)
                        .filters(f -> f.filter(jwtAuthenticationFilter.jwtRoleFilter(List.of(ADMIN))))
                        .uri(cdcntProperties.getAdminService()))
                .route("admin_delete_employees", r -> r.path(ADMIN_ROUTE_DELETE_EMPLOYERS)
                        .filters(f -> f.filter(jwtAuthenticationFilter.jwtRoleFilter(List.of(ADMIN))))
                        .uri(cdcntProperties.getAdminService()))

                // ==== JOB SERVICE ROUTES (localhost:8083) ====

                .build();
    }
}
