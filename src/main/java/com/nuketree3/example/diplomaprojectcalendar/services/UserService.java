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

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private final DropboxService dropboxService;

    @Autowired
    private final TemplateEngine templateEngine;

    /**
     * loadUserByUsername - загружает UserDetails по пользовательскому username'у
     * @param username - username пользователя
     * @return - UserDetails
     * @throws UsernameNotFoundException - если пользователь есть в базе данный, но его аккаунт не активирован
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        if(userRepository.getUserRole(user.getId()).equals(String.valueOf(Role.ROLE_NOT_ACTIVATED))) {
            throw new DisabledException("User account is not activated");
        }
        String photoPath = userRepository.getUserPhotoPath(user.getId());
        return UserDetailsImpl.buildUserDetails(user, userRepository.getUserRole(user.getId()), photoPath);
    }

    /**
     * createUser - метод, который добавляет пользователя в базу данных, присваивает ему не активированную роль и отправляет на почту пользователю код активации
     * @param user - экземпляр класса пользователя
     * @return - возвращает статус, успешно ли создан пользователь
     */
    public boolean createUser(User user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent() || userRepository.findByUsername(user.getUsername()).isPresent()) {
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
            mailSender.sendMail(user.getEmail(), "Activation", message);
        }
        return true;
    }

    /**
     * activateUser - метод, который по уникальному активационному коду находит пользователя и активирует его
     * @param code - код активации
     * @return - возвращает boolean значение - получилось ли активировать пользователя или нет
     */
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

    /**
     * getUserByUserName - метод, который возвращает экземпляр класса User по username'у из базы данных
     * @param username - username пользователя, которого нужно найти
     * @return - экземпляр класса User
     */
    public User getUserByUserName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    /**
     * getUsersLike - метод, который возвращает HashMap имен пользователей и статус их относительно текущего пользователя(друг/вам отправлено приглашение/пользователю отправлено приглашение/другой)
     * @param queryName - строка, относительно которого и идет поиск пользователя
     * @param username - username текущего пользователя
     * @return HashMap имен пользователей и статус их относительно текущего пользователя
     */
    public HashMap<String, String> getUsersLike(String queryName, String username) {
        HashMap<String, String> usersStatus = new HashMap<>();

        String[] tempFriendsArray = userRepository.getUserFriends(username);
        List<String> friends;
        if(tempFriendsArray.length != 0 && tempFriendsArray[0] != null) {
            friends = new ArrayList<>(Arrays.asList(tempFriendsArray[0].split(",")));
        }else {
            friends = new ArrayList<>();
        }

        String[] tempSendOffer = userRepository.getUserFriendsOffers(username);
        List<String> sendOffer;
        if(tempSendOffer.length != 0 && tempSendOffer[0] != null) {
            sendOffer = new ArrayList<>(Arrays.asList(tempSendOffer[0].split(",")));
        }else {
            sendOffer = new ArrayList<>();
        }

        String[] tempFriendOffer = userRepository.getSendOffer(username);
        List<String> friendOffer;
        if(tempFriendOffer.length != 0 && tempFriendOffer[0] != null) {
            friendOffer = new ArrayList<>(Arrays.asList(tempFriendOffer[0].split(",")));
        }else {
            friendOffer = new ArrayList<>();
        }

        for(String otherUsers : userRepository.getUsersLike(queryName, username)){
            if(friends.contains(otherUsers)) {
                usersStatus.put(otherUsers, "friend");
            }else if(sendOffer.contains(otherUsers)) {
                usersStatus.put(otherUsers, "offer");
            }else if(friendOffer.contains(otherUsers)) {
                usersStatus.put(otherUsers, "invite");
            }else{
                usersStatus.put(otherUsers, "other");
            }
        }

        return usersStatus;
    }

    /**
     * getUserPhotoUrlByUsername - метод, который возвращает путь к фотографии в облаке пользователя из базы данных
     * @param username - username пользователя, путь фотографии которого нужно найти
     * @return путь к фотографии
     */
    public String getUserPhotoUrlByUsername(String username) {
        String photoUrl = userRepository.getUserPhotoUrlByUsername(username);
        byte[] photo = dropboxService.downloadFile(photoUrl);
        if(photo != null && photo.length > 0) {
            return photoUrl;
        }
        return null;
    }

    /**
     * getUserFriendsWithPhotoUrl - метод, который возвращает HashMap username'ов и путь к их фотографиям в облаке из базы данных
     * @param username - пользовательский username
     * @return - HashMap username'ов и путь к их фотографиям в облаке из базы данных
     */
    public HashMap<String, String> getUserFriendsWithPhotoUrl(String username) {
        HashMap<String, String> friendsWithPhotoUrl = new HashMap<>();
        List<String> friendsUser = getUserFriendsUser(username);
        for (String user : friendsUser) {
            friendsWithPhotoUrl.put(user, getUserPhotoUrlByUsername(user));
        }
        return friendsWithPhotoUrl;
    }

    /**
     * getUserFriendsUser - метод, который по пользовательскому username'у возвращает список username'ов друзей пользователя
     * @param username - пользовательский username
     * @return - список username'ов друзей пользователя
     */
    public List<String> getUserFriendsUser(String username) {
        String[] result = userRepository.getUserFriends(username);
        if (result.length != 0 && result[0] != null) {
            return new ArrayList<String>(Arrays.asList(result[0].split(",")));
        }
        return new ArrayList<>();
    }

    /**
     * sendFriendOffer - метод, который добавляет другу предложение "дружить", а текущему пользователю добавляет в список "отправленных приглашений"
     * @param username - username пользователя, которому нужно отправить предложение
     * @param name - username текущего пользователя
     */
    public void sendFriendOffer(String username, String name) {
        addSendOffer(username, name);
        addFriendOffer(name, username);
    }

    /**
     * addSendOffer - метод, который username'у пользователя добавляет предложение
     * @param username - кому нужно добавить
     * @param name - username текущего пользователя
     */
    private void addSendOffer(String username, String name) {
        userRepository.addSendOffer(userRepository.getUserIDByUsername(username), name);
    }

    /**
     * addFriendOffer - метод, который текущему пользователю добавляет username пользователя в список "отправленных приглашений"
     * @param name - username текущего пользователя
     * @param username - username пользователя, которого добавляют в список
     */
    private void addFriendOffer(String name, String username) {
        userRepository.addFriendOffer(name, username);

    }

    /**
     * acceptFriend - метод, который удаляет из запроса и предложения username'ы пользователей и добавляет каждого в друзья
     * @param friendUserName - username друга
     * @param username - username текущего пользователя
     */
    public void acceptFriend(String friendUserName, String username) {
        delSendOffer(username, friendUserName);
        delFriendOffer(friendUserName, username);
        userRepository.addUserFriends(username, friendUserName);
        userRepository.addUserFriends(friendUserName, username);
    }

    /**
     * delSendOffer - метод, который удаляет у username friendUserName из списка "отправленных приглашений"
     * @param username - текущего пользователя, у которого надо удалить
     * @param friendUserName - username пользователя, который надо удалить
     */
    private void delSendOffer(String username, String friendUserName) {
        userRepository.delSendOffer(username, friendUserName);
    }

    /**
     * delFriendOffer - метод, который удаляет у friendUserName username из списка "приглашений в друзья"
     * @param friendUserName -  username пользователя, у которого надо удалить
     * @param username - текущего пользователя, которого надо удалить
     */
    private void delFriendOffer(String friendUserName, String username) {
        userRepository.delFriendOffer(friendUserName, username);
    }

    /**
     * delFriend - метод, который удаляет друг друга из друзей
     * @param username - пользовательский username
     * @param friendUserName - username друга
     */
    public void delFriend(String username, String friendUserName) {
        userRepository.delFriend(username, friendUserName);
        userRepository.delFriend(friendUserName, username);
    }

    /**
     * getInvitations - метод, который возвращает HashMap пользователей, которые отправили текущему пользователю приглашение, и путь на их фотографии в облаке
     * @param username - текущего пользователя
     * @return HashMap username'ов и путей их фотографий на облаке
     */
    public HashMap<String, String> getInvitations(String username) {
        HashMap<String, String> invitationsWithPhotoUrl = new HashMap<>();
        String[] tempArray = userRepository.getSendOffer(username);
        if(tempArray.length == 0 || tempArray[0] == null) {
            return null;
        }
        tempArray = tempArray[0].split(",");
        for(String invitationUerName : new ArrayList<>(Arrays.asList(tempArray))){
            invitationsWithPhotoUrl.put(invitationUerName, getUserPhotoUrlByUsername(username));
        }
        return invitationsWithPhotoUrl;
    }

    /**
     * rejectFriend - метод "отказа от дружбы", который удаляет у текущего пользователя пользователя с friendUserName из предложений,
     * а у пользователя с friendUserName текущего пользователя из запроса на дружбу
     * @param friendUserName - username пользователя, который отправил запрос на дружбу
     * @param username - текущий пользователь
     */
    public void rejectFriend(String friendUserName, String username) {
        delFriendOffer(friendUserName, username);
        delSendOffer(username, friendUserName);
    }

    /**
     * addUserPhotoUrl - метод, который добавляет пользователю путь к фотографии на облаке, а в случае, если до этого у пользователя был файл в облаке - удаляет его
     * @param name - текущего пользователя
     * @param photoUrl - путь на облаке к фотографии
     */
    public void addUserPhotoUrl(String name, String photoUrl) {
        if(getUserPhotoUrlByUsername(name) != null){
            dropboxService.deleteFile(getUserPhotoUrlByUsername(name));
        }
        userRepository.setUserPhotoUrl(name, photoUrl);
    }
}
