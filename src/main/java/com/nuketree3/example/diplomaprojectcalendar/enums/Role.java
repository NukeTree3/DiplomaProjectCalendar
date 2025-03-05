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
}
