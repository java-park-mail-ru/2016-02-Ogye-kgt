package main;

import org.eclipse.jetty.server.Authentication;
import rest.UserProfile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author esin88
 */
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
        if (users.containsKey(userId))
            return false;
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
}
