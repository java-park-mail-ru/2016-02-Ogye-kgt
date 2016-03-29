package services;

import models.UserLoginRequest;
import models.UserProfile;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;
import services.AccountServiceImpl.InvalidUserException;
import services.AccountServiceImpl.UserExistsException;
import services.config.ConfigFactory;

import static org.junit.Assert.*;

public class AccountServiceImplTest {
    private AccountServiceImpl accountService;
    private UserProfile testUser;
    private UserLoginRequest testLoginRequest;
    public static final String TEST_SESSION_ID = "bjvs7ieafcl8ub8qk0ur9ts0";
    private static final String INVALID_SESSION_ID = "000000000000000000000000";
    private static final String TEST_LOGIN = "testlogin";
    private static final String TEST_PASS = "testpass";

    @Before
    public void setUp() throws Exception {
        final Configuration config = ConfigFactory.create(ConfigFactory.TYPE.DEBUG);
        accountService = new AccountServiceImpl(config);
        testUser = new UserProfile(TEST_LOGIN, TEST_PASS, "test@mail.ru");
        testLoginRequest = new UserLoginRequest(TEST_LOGIN, TEST_PASS);
    }

    @Test
    public void testAddUser() throws Exception {
        assertNotNull(accountService.addUser(testUser));
    }

    @Test(expected = UserExistsException.class)
    public void testAddSameUserFail() throws Exception {
        accountService.addUser(testUser);
        accountService.addUser(testUser);
    }

    @Test(expected = InvalidUserException.class)
    public void testInvalidUserFail() throws Exception {
        accountService.addUser(new UserProfile("a", "a", "a"));
    }

    @Test
    public void testRemoveUser() throws Exception {
        final long userId = accountService.addUser(testUser);
        assertNotNull(accountService.getUser(userId));
        final boolean result = accountService.removeUser(userId);
        assertTrue(result);
        assertNull(accountService.getUser(userId));
    }

    @Test
    public void testRemoveNoUserFail() throws Exception {
        final boolean result = accountService.removeUser(testUser.getId());
        assertFalse(result);
    }

    @Test
    public void testRemoveUserWrongSessionFail() throws Exception {
        accountService.addUser(testUser);
        accountService.doLogin(TEST_SESSION_ID, testLoginRequest);
        final boolean result = accountService.removeUser(INVALID_SESSION_ID, testUser.getId());
        assertFalse(result);
    }

    @Test
    public void testRemoveUserWrongIdFail() throws Exception {
        accountService.addUser(testUser);
        accountService.doLogin(TEST_SESSION_ID, testLoginRequest);
        final long wrondId = testUser.getId() + 1;
        final boolean result = accountService.removeUser(TEST_SESSION_ID, wrondId);
        assertFalse(result);
    }

    @Test
    public void testUpdateUser() throws Exception {
        final long userId = accountService.addUser(testUser);
        accountService.doLogin(TEST_SESSION_ID, testLoginRequest);
        final UserProfile newProfile = new UserProfile("newLogin", "testpass", "test@mail.ru");
        final boolean result = accountService.updateUser(TEST_SESSION_ID, userId, newProfile);
        assertTrue(result);
        final UserProfile updatedUser = accountService.getUser(userId);
        assertEquals(updatedUser, newProfile);
    }

    @Test
    public void testUpdateUserWrongUserIdFail() throws Exception {
        accountService.addUser(testUser);
        accountService.doLogin(TEST_SESSION_ID, testLoginRequest);
        final long wrongId = testUser.getId() + 1;
        final UserProfile newUser = new UserProfile("newLogin", "testpass", "test@mail.ru");
        final boolean result = accountService.updateUser(TEST_SESSION_ID, wrongId, newUser);
        assertFalse(result);
    }

    @Test
    public void testUpdateUserInvalidSessionFail() throws Exception {
        accountService.addUser(testUser);
        accountService.doLogin(TEST_SESSION_ID, testLoginRequest);
        final UserProfile newUser = new UserProfile("newLogin", "testpass", "test@mail.ru");
        final boolean result = accountService.updateUser(INVALID_SESSION_ID, testUser.getId(), newUser);
        assertFalse(result);
    }

    @Test
    public void testUpdateUserInvalidEmailFail() throws Exception {
        accountService.addUser(testUser);
        accountService.doLogin(TEST_SESSION_ID, new UserLoginRequest("testlogin", "testpass"));
        final UserProfile newUser = new UserProfile("newLogin", "testpass", "invalidmail.ru");
        final boolean result = accountService.updateUser(TEST_SESSION_ID, testUser.getId(), newUser);
        assertFalse(result);
    }

    @Test
    public void testDoLogin() throws Exception {
        accountService.addUser(testUser);
        final UserProfile result = accountService.doLogin(TEST_SESSION_ID, testLoginRequest);
        assertNotNull(result);
    }

    @Test
    public void testDoLoginNoUserFail() throws Exception {
        final UserProfile result = accountService.doLogin(TEST_SESSION_ID, testLoginRequest);
        assertNull(result);
    }

    @Test
    public void testDoLoginInvalidPassFail() throws Exception {
        accountService.addUser(testUser);
        final UserLoginRequest userLoginRequest = new UserLoginRequest("testlogin", "invalidPass");
        final UserProfile result = accountService.doLogin(TEST_SESSION_ID, userLoginRequest);
        assertNull(result);
    }

    @Test
    public void testDoLogout() throws Exception {
        accountService.addUser(testUser);
        accountService.doLogin(TEST_SESSION_ID, testLoginRequest);
        final boolean result = accountService.doLogout(TEST_SESSION_ID);
        assertTrue(result);
    }

    @Test
    public void testDoLogoutNoSessionFail() throws Exception {
        final boolean result = accountService.doLogout(INVALID_SESSION_ID);
        assertFalse(result);
    }

    @Test
    public void getLocalStatus() throws Exception {
        final String result = accountService.getLocalStatus();
        assertEquals(result, "ACTIVE");
    }

    @Test
    public void getUser() throws Exception {
        final long userId = accountService.addUser(testUser);
        assertNotNull(userId);
        final UserProfile createdUser = accountService.getUser(userId);
        assertEquals(testUser, createdUser);
    }

    @Test
    public void getUserByLogin() throws Exception {
        accountService.addUser(testUser);
        final UserProfile user = accountService.getUserByLogin(testUser.getLogin());
        assertNotNull(user);
        assertEquals(testUser, user);
    }
}