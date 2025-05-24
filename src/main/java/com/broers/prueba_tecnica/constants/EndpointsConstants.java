package com.broers.prueba_tecnica.constants;

public class EndpointsConstants {
    private EndpointsConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ENDPOINT_BASE_API = "/api/v1/school";

    // Authentication endpoints
    public static final String ENDPOINT_LOGIN = ENDPOINT_BASE_API + "/login";
    public static final String ENDPOINT_SIGNUP = ENDPOINT_BASE_API + "/signup";

    // Email verification and password setting
    public static final String ENDPOINT_VERIFY_EMAIL = ENDPOINT_BASE_API + "/verify-email";
    public static final String ENDPOINT_SET_PASSWORD = ENDPOINT_BASE_API + "/set-password";

    // Password recovery
    public static final String ENDPOINT_REQUEST_PASSWORD_RECOVERY = ENDPOINT_BASE_API + "/request-password-recovery";
    public static final String ENDPOINT_RESET_FORM = ENDPOINT_BASE_API +"/password-reset-form";
    public static final String ENDPOINT_RESET_PASSWORD = ENDPOINT_BASE_API + "/reset-password";

    // User management
    public static final String ENDPOINT_USERS = ENDPOINT_BASE_API + "/users";
}
