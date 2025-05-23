package com.nus.iss.config;

public class AppConstants {
    private AppConstants() {
    }
    // 8080 for gateway, 8081 for liquibase

    // User service URL
    public final static String USER_ROUTE_SWAGGER = "/user-service/v3/api-docs";
    public final static String USER_ROUTE_REGISTER = "/api/v1/register";
    public final static String USER_ROUTE_ACTIVATE = "/api/v1/activate";
    public final static String USER_ROUTE_LOGIN = "/api/v1/login";
    public final static String USER_ROUTE_FORGOT_PASSWORD = "/api/v1/forgot-password";
    public final static String USER_ROUTE_EMPLOYER_PROFILE = "/api/v1/employer-profiles";
    public final static String USER_ROUTE_EMPLOYER_PROFILE_PICTURE = "/api/v1/employer-profiles-picture";
    public final static String USER_ROUTE_EMPLOYEE_PROFILE = "/api/v1/employee-profiles";
    public final static String USER_ROUTE_EMPLOYEE_PROFILE_PICTURE = "/api/v1/employee-profiles-picture";
    public final static String USER_ROUTE_EMPLOYEE_RESUME = "/api/v1/employee-resume";


    // Admin service URL
    public final static String ADMIN_ROUTE_SWAGGER = "/admin-service/v3/api-docs";
    public final static String ADMIN_ROUTE_GET_ALL_EMPLOYERS = "/api/v1/list-employer-profiles";
    public final static String ADMIN_ROUTE_REVIEW_EMPLOYER_PROFILE = "/api/v1/review-employer-profiles";
    public final static String ADMIN_ROUTE_DELETE_EMPLOYER = "/api/v1/employer-profiles";
    public final static String ADMIN_ROUTE_GET_ALL_EMPLOYEES = "/api/v1/list-employee-profiles";
    public final static String ADMIN_ROUTE_REVIEW_JOB_POSTING = "/api/v1/review-job-posting";

    // Job service URL
    public final static String JOB_ROUTE_SWAGGER = "/job-service/v3/api-docs";

    // Interview prep service URL
    public final static String INTERVIEW_SERVICE_ROUTE_SWAGGER = "/interview-service/v3/api-docs";


    // Constants
    public final static String EMPLOYER = "EMPLOYER";
    public final static String EMPLOYEE = "EMPLOYEE";
    public final static String ADMIN = "ADMIN";

}
