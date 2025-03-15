package com.nuketree3.example.diplomaprojectcalendar.repositories;

import com.nuketree3.example.diplomaprojectcalendar.domain.User;
import jakarta.transaction.Transactional;
//import com.vladmihalcea.hibernate.type.array.StringArrayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    Optional<User> findByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

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

    @Query(value = "SELECT user_friends FROM user_friends WHERE id = (SELECT id FROM users WHERE username = :username)", nativeQuery = true)
    String[] getUserFriends(@Param("username") String username);


    @Query(value = "SELECT COALESCE(friend_offers, '{}') FROM user_friends WHERE id = (SELECT id FROM users WHERE username = :username) ", nativeQuery = true)
    String[] getUserFriendsOffers(@Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_friends SET user_friends = array_append(user_friends, :friend) WHERE id = (SELECT id FROM users WHERE username = :username)", nativeQuery = true)
    void addUserFriends(@Param("username") String username, @Param("friend") String friend);

    @Query(value = "SELECT COALESCE(send_offer, '{}') FROM user_friends WHERE id = (SELECT id FROM users WHERE username = :username)", nativeQuery = true)
    String[] getSendOffer(@Param("username") String username);

    @Query(value = "SELECT id FROM users WHERE username = :username", nativeQuery = true)
    Long getUserIDByUsername(@Param("username") String username);

    Long getUserIDByEmail(String username);

    @Query(value = "SELECT username FROM users WHERE email = :email", nativeQuery = true)
    String getUsernameByEmail(@Param("email") String email);

    @Query(value = "SELECT photourl FROM users_photo WHERE user_id = (SELECT id FROM users WHERE username = :username)", nativeQuery = true)
    String getUserPhotoUrlByUsername(@Param("username") String username);

    @Query(value = "SELECT username FROM users WHERE lower(username) LIKE lower(concat('%', :queryName, '%')) AND NOT username = :username", nativeQuery = true)
    ArrayList<String> getUsersLike(@Param("queryName") String queryName, @Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_friends SET send_offer = array_append(send_offer, :sendOffer) WHERE id = :id", nativeQuery = true)
    void addSendOffer(@Param("id") Long id, @Param("sendOffer") String sendOffer);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_friends SET friend_offers = array_append(friend_offers, :userFriendOffer) WHERE id = (SELECT id FROM users WHERE username = :name)", nativeQuery = true)
    void addFriendOffer(@Param("name") String name, @Param("userFriendOffer") String userFriendOffer);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_friends SET send_offer = array_remove(send_offer, :sendOffer) WHERE id = (SELECT id FROM users WHERE username = :name)", nativeQuery = true)
    void delSendOffer(@Param("name") String name, @Param("sendOffer") String sendOffer);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_friends SET friend_offers = array_remove(friend_offers, :userFriendOffer) WHERE id = (SELECT id FROM users WHERE username = :name)", nativeQuery = true)
    void delFriendOffer(@Param("name") String name, @Param("userFriendOffer") String userFriendOffer);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users_photo SET photourl = :photourl WHERE user_id = (SELECT id FROM users WHERE username = :username)", nativeQuery = true)
    void setUserPhotoUrl(@Param("username") String name, @Param("photourl") String photoUrl);

    User getUserById(Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_friends SET user_friends = array_remove(user_friends, :friend) WHERE id = (SELECT id FROM users WHERE username = :name)", nativeQuery = true)
    void delFriend(@Param("name") String user, @Param("friend") String friend);
}
