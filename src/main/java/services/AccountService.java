package services;

import models.UserLoginRequest;
import models.UserProfile;

import java.util.Collection;

public interface AccountService {
    String getLocalStatus();

    long addUser(UserProfile userProfile) throws AccountServiceImpl.DatabaseException;

    UserProfile getUser(long userId) throws AccountServiceImpl.DatabaseException;

    UserProfile getUserByLogin(String login) throws AccountServiceImpl.DatabaseException;

    Collection<UserProfile> getAllUsers() throws AccountServiceImpl.DatabaseException;

    boolean removeUser(long userId) throws AccountServiceImpl.DatabaseException;

    boolean removeUser(String sessionId, long userId); // throws AccountServiceImpl.DatabaseException;

    boolean updateUser(String sessionId, long userId, UserProfile userProfile); //throws AccountServiceImpl.DatabaseException;

    boolean isAuthorised(String sessionId);

    UserProfile getUserBySession(String sessionId);

    UserProfile doLogin(String sessionId, UserLoginRequest userLoginRequest) throws AccountServiceImpl.DatabaseException;

    boolean doLogout(String sessionId);

    boolean drop();

}
