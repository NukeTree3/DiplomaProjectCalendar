package com.nuketree3.example.diplomaprojectcalendar.services;

import com.nuketree3.example.diplomaprojectcalendar.domain.User;
import com.nuketree3.example.diplomaprojectcalendar.enums.Role;
import com.nuketree3.example.diplomaprojectcalendar.userdetail.UserDetailsImpl;
import com.nuketree3.example.diplomaprojectcalendar.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.ArrayList;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    @Autowired
    private final TemplateEngine templateEngine;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        if(userRepository.getUserRole(user.getId()).equals(String.valueOf(Role.ROLE_NOT_ACTIVATED))) {
            throw new DisabledException("User account is not activated");
        }
//        System.out.println(user.getEmail() + " " + userRepository.getUserRole(user.getId()));
        String photoPath = userRepository.getUserPhotoPath(user.getId());
        return UserDetailsImpl.buildUserDetails(user, userRepository.getUserRole(user.getId()), photoPath);
    }

    public boolean createUser(User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent() && userRepository.findByUsername(user.getUsername()).isPresent()) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        userRepository.setRole(user.getId(), String.valueOf(Role.ROLE_NOT_ACTIVATED), UUID.randomUUID().toString());

        if(!user.getEmail().isEmpty()){
            Context context = new Context();
            context.setVariable("userName", user.getUsername());
            context.setVariable("activationLink", "http://localhost:8080/activationcode/" + userRepository.getActivationCode(user.getId()));
            String message = templateEngine.process("mail", context);
            mailSender.sendSimpleMail(user.getEmail(), "Activation", message);
        }
        return true;
    }

    public boolean activateUser(String code) {
        Long id = userRepository.getUserIDByActivationCode(code);
        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(code));

        if(user == null || !userRepository.getUserRole(id).equals(String.valueOf(Role.ROLE_NOT_ACTIVATED))) {
            return false;
        }

        userRepository.setUserActivationCode(id, "null");
        userRepository.setUserRole(id, String.valueOf(Role.ROLE_USER));

        return true;
    }

    public ArrayList<User> findAllUsersByUsername(ArrayList<String> usernames) {
        return userRepository.findByUsernameIn(usernames);
    }

    public User getUserByUserName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public ArrayList<String> getUsersLike(String username) {
        return userRepository.getUsersLike(username);
    }

    public String getUserPhotoUrlByUserID(Long userId) {
        return userRepository.getUserPhotoUrlByUserId(userId);
    }

    public ArrayList<String> getUserFriendUserID(Long userId) {
        return userRepository.getUserFriends(userId);
    }

    public Long getUserIDByUsername(String username) {
        return userRepository.getUserUIIDByUsername(username);
    }

    public Long getUserIDByEmail(String username) {
        return userRepository.getUserIDByEmail(username);
    }

    public String getUsernameByEmail(String username) {
        return userRepository.getUsernameByEmail(username);
    }
}
