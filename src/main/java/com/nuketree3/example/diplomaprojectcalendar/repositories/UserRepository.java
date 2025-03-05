package com.nuketree3.example.diplomaprojectcalendar.repositories;

import com.nuketree3.example.diplomaprojectcalendar.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_roles (user_id, user_role, activation_code) VALUES (:id, :role, :code)", nativeQuery = true)
    void setRole(@Param("id") Long id, @Param("role") String role, @Param("code") String code);

    @Query(value = "SELECT activation_code FROM user_roles WHERE user_id = :id", nativeQuery = true)
    String getActivationCode(@Param("id") Long id);

    @Query(value = "SELECT photourl FROM users_photo WHERE user_id = :id", nativeQuery = true)
    String getUserPhotoPath(@Param("id") Long id);

    @Query(value = "SELECT user_role FROM user_roles WHERE user_id = :id", nativeQuery = true)
    String getUserRole(Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_roles SET user_role = :role WHERE user_id = :id", nativeQuery = true)
    void setUserRole(Long id, String role);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_roles SET activation_code = :code WHERE user_id = :id", nativeQuery = true)
    void setUserActivationCode(Long id, String code);

    @Query(value = "SELECT user_id FROM user_roles WHERE activation_code = :code", nativeQuery = true)
    Long getUserIDByActivationCode(String code);

    @Query(value = "SELECT * FROM users WHERE id = :id", nativeQuery = true)
    Optional<User> findById(Long id);

    ArrayList<User> findByUsernameIn(ArrayList<String> usernames);

    @Query(value = "SELECT user_friends FROM user_friends WHERE id = :id", nativeQuery = true)
    ArrayList<String> getUserFriends(@Param("id") Long id);

    Long getUserUIIDByUsername(String username);

    Long getUserIDByEmail(String username);

    @Query(value = "SELECT username FROM users WHERE email = :email", nativeQuery = true)
    String getUsernameByEmail(@Param("email") String email);

//    @Query(value = "SELECT photourl FROM users_photo WHERE user_id = (SELECT id FROM users WHERE username = :username)", nativeQuery = true)
//    String getUserPhotoUrlByUsername(@Param("username") String username);
//
//    @Query(value = "SELECT user_friends FROM user_friends WHERE id = (SELECT id FROM users WHERE username = :username)", nativeQuery = true)
//    ArrayList<String> getUserFriendsByUsername(String username);

    @Query(value = "SELECT photourl FROM users_photo WHERE user_id = :id", nativeQuery = true)
    String getUserPhotoUrlByUserId(@Param("id") Long userId);

    @Query(value = "SELECT username FROM users WHERE username LIKE :username", nativeQuery = true)
    ArrayList<String> getUsersLike(@Param("username") String username);
}
