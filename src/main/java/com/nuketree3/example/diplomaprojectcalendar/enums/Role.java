package com.nuketree3.example.diplomaprojectcalendar.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_NOT_ACTIVATED;

    @Override
    public String getAuthority() {
        return name();
    }

    public static class SecurityConstants {
        public static final String ROLE_ADMIN_STRING = "ROLE_ADMIN";
        public static final String ROLE_USER_STRING = "ROLE_USER";
        public static final String ROLE_NOT_ACTIVATED_STRING = "ROLE_NOT_ACTIVATED";
    }
}
