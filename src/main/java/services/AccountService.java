package services;

import models.UserLoginRequest;
import models.UserProfile;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AccountService {
    private Map<Long, UserProfile> users = new ConcurrentHashMap<>();
    private Map<String, UserProfile> sessions = new ConcurrentHashMap<>();

    public AccountService() {
        addUser(new UserProfile("admin", "admin", "admin@mail.ru"));
        addUser(new UserProfile("guest", "12345", "guest@mail.ru"));
    }

    public Collection<UserProfile> getAllUsers() {
        return users.values();
    }

    public boolean addUser(UserProfile userProfile) {
        final long userId = userProfile.getId();

        if (isUserExist(userProfile)) return false;
        users.put(userProfile.getId(), userProfile);
        return true;
    }

    public UserProfile getUser(long userId) {
        return users.get(userId);
    }

    @Nullable
    public UserProfile getUserByLogin(String login) {
        for (UserProfile user : users.values()) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }

    public UserProfile getUserBySession(String sessionId) {
        return sessions.get(sessionId);
    }

    public boolean removeUser(long userId) {
        if (!users.containsKey(userId))
            return false;
        users.remove(userId);
        return true;
    }

    public boolean updateUser(long userId, UserProfile newProfile) {
        if (!users.containsKey(userId))
            return false;
        final UserProfile userProfile = users.get(userId);
        userProfile.setLogin(newProfile.getLogin());
        userProfile.setPassword(newProfile.getPassword());
        users.replace(userId, userProfile);
        return true;
    }

    public boolean isUserExist(UserProfile userProfile) {
        final String login = userProfile.getLogin();
        for (UserProfile curUserProfile : users.values()) {
            if (curUserProfile.getLogin().equals(login)) return true;
        }
        return false;
    }

    public boolean isUserExist(String login) {
        for (UserProfile curUserProfile : users.values()) {
            if (curUserProfile.getLogin().equals(login)) return true;
        }
        return false;
    }

    private void addSession(String sessionId, UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    private void closeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public boolean isAuthorised(String sessioinId) {
        return sessions.containsKey(sessioinId);
    }

    public boolean doLogin(String sessionId, UserLoginRequest user) {
        final String login = user.getLogin();
        final UserProfile userProfile = getUserByLogin(login);
        if (userProfile == null) return false;
        addSession(sessionId, userProfile);
        //noinspection UnnecessaryLocalVariable
        final boolean isPasswordEqual = userProfile.getPassword().equals(user.getPassword());
        return isPasswordEqual;
    }

    public void doLogout(String sessionId) {
        closeSession(sessionId);
    }

    public boolean isUserMatch(String sessionId, long id) {
        return sessions.get(sessionId).getId() == id;
    }

}
