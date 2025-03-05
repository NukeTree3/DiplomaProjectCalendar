package com.nuketree3.example.diplomaprojectcalendar.userdetail;

import com.nuketree3.example.diplomaprojectcalendar.domain.User;
import com.nuketree3.example.diplomaprojectcalendar.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails{

    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthdate;
    private String photoUrl;
    private String role;

    public static UserDetails buildUserDetails(User user, String role, String photoUrl) {
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBirthday(),
                photoUrl,
                role
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return role.equals(String.valueOf(Role.ROLE_USER)) || role.equals(String.valueOf(Role.ROLE_ADMIN));
    }
}
