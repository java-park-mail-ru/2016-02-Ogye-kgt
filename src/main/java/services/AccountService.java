package services;

import models.UserLoginRequest;
import models.UserProfile;

import java.util.Collection;

public interface AccountService {
    String getLocalStatus();

    long addUser(UserProfile userProfile) throws AccountServiceImpl.UserExistsException, AccountServiceImpl.InvalidUserException;

    UserProfile getUser(long userId);

    Collection<UserProfile> getAllUsers();

    boolean removeUser(long userId);

    boolean removeUser(String sessionId, long userId);

    boolean updateUser(String sessionId, long userId, UserProfile userProfile);

    boolean isAuthorised(String sessionId);

    UserProfile getUserBySession(String sessionId);

    UserProfile doLogin(String sessionId, UserLoginRequest userLoginRequest);

    boolean doLogout(String sessionId);

    boolean drop();

}
