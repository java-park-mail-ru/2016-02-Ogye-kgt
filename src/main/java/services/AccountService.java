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

    // FIXME: убрать, когда будет покрыто тестами.
    public AccountService() {
        addUser(new UserProfile("admin", "admin", "admin@mail.ru"));
        addUser(new UserProfile("guest", "12345", "guest@mail.ru"));
    }

    // FIXME: тестовый метод, не выкладывать в прод.
    public Collection<UserProfile> getAllUsers() {
        return users.values();
    }

    public boolean addUser(UserProfile userProfile) {
        if (isUserExist(userProfile) || !userProfile.isValid()) return false;
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
        if (!users.containsKey(userId)) return false;
        users.remove(userId);
        return true;
    }

    public boolean removeUser(String sessionId, long userId) {
        if (!users.containsKey(userId)) return false;
        if (sessions.get(sessionId).getId() != userId) return false;
        users.remove(userId);
        return true;
    }

    public boolean updateUser(final String sessionId, final long userId, UserProfile newProfile) {
        final UserProfile userProfile = getUserBySession(sessionId);
        if (userProfile == null) return false;
        if (userProfile.getId() != userId) return false;

        if (!UserProfile.isLoginValid(newProfile.getLogin())) return false;
        if (!UserProfile.isPasswordValid(newProfile.getPassword())) return false;
        if (!UserProfile.isEmailValid(newProfile.getEmail())) return false;

        userProfile.setLogin(newProfile.getLogin());
        userProfile.setPassword(newProfile.getPassword());
        userProfile.setEmail(newProfile.getEmail());
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

    @Nullable
    public UserProfile doLogin(String sessionId, UserLoginRequest user) {
        final String login = user.getLogin();
        final UserProfile userProfile = getUserByLogin(login);
        if (userProfile == null) return null;
        addSession(sessionId, userProfile);
        //noinspection UnnecessaryLocalVariable
        final boolean isPasswordEqual = userProfile.getPassword().equals(user.getPassword());
        if (isPasswordEqual) {
            return userProfile;
        } else {
            return null;
        }
    }

    public boolean doLogout(String sessionId) {
        if (getUserBySession(sessionId) == null) return false;
        closeSession(sessionId);
        return true;
    }

    public boolean isUserMatch(String sessionId, long id) {
        return sessions.get(sessionId).getId() == id;
    }

}
