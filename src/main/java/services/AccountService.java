package services;

import models.UserProfile;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AccountService {
    private Map<Long, UserProfile> users = new ConcurrentHashMap<>();

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
}
