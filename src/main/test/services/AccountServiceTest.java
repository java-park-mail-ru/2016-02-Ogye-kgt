package services;

import models.UserLoginRequest;
import models.UserProfile;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccountServiceTest {
    private AccountService accountService;
    private static final String TEST_SESSION_ID = "bjvs7ieafcl8ub8qk0ur9ts0";

    @Before
    public void setUp() throws Exception {
        accountService = new AccountService();

    }

    @Test
    public void testAddUser() throws Exception {
        final boolean result = accountService.addUser(new UserProfile("testlogin", "testpass", "test@mail.ru"));
        assertTrue(result);
    }

    @Test
    public void testAddSameUserFail() throws Exception {
        final UserProfile testUser = new UserProfile("testlogin", "testpass", "test@mail.ru");
        accountService.addUser(testUser);
        final boolean result = accountService.addUser(testUser);
        assertFalse(result);
    }

    @Test
    public void testGetUser() throws Exception {

    }

    @Test
    public void testGetUserByLogin() throws Exception {

    }

    @Test
    public void testGetUserBySession() throws Exception {

    }

    @Test
    public void testRemoveUser() throws Exception {
        final UserProfile testUser = new UserProfile("testlogin", "testpass", "test@mail.ru");
        accountService.addUser(testUser);
        final boolean result = accountService.removeUser(testUser.getId());
        assertTrue(result);
    }

    @Test
    public void testRemoveNoUserFail() throws Exception {
        final UserProfile testUser = new UserProfile("testlogin", "testpass", "test@mail.ru");
        final boolean result = accountService.removeUser(testUser.getId());
        assertFalse(result);
    }

    @Test
    public void testUpdateUser() throws Exception {
        final UserProfile testUser = new UserProfile("testlogin", "testpass", "test@mail.ru");
        accountService.addUser(testUser);
        //todo
    }

    @Test
    public void testIsUserExist() throws Exception {

    }

    @Test
    public void testIsAuthorised() throws Exception {

    }

    @Test
    public void testDoLogin() throws Exception {
        final UserProfile testUser = new UserProfile("testlogin", "testpass", "test@mail.ru");
        accountService.addUser(testUser);

        final UserLoginRequest userLoginRequest = new UserLoginRequest("testlogin", "testpass");

        final UserProfile result = accountService.doLogin(TEST_SESSION_ID, userLoginRequest);
        assertNotNull(result);
    }

    @Test
    public void testDoLoginNoUserFail() throws Exception {
        final UserLoginRequest userLoginRequest = new UserLoginRequest("testlogin", "testpass");

        final UserProfile result = accountService.doLogin(TEST_SESSION_ID, userLoginRequest);
        assertNull(result);
    }

    @Test
    public void testDoLoginInvalidPassFail() throws Exception {
        final UserProfile testUser = new UserProfile("testlogin", "testpass", "test@mail.ru");
        accountService.addUser(testUser);

        final UserLoginRequest userLoginRequest = new UserLoginRequest("testlogin", "invalidPass");

        final UserProfile result = accountService.doLogin(TEST_SESSION_ID, userLoginRequest);
        assertNull(result);
    }

    @Test
    public void testDoLogout() throws Exception {
        final UserProfile testUser = new UserProfile("testlogin", "testpass", "test@mail.ru");
        accountService.addUser(testUser);

        final UserLoginRequest userLoginRequest = new UserLoginRequest("testlogin", "testpass");
        accountService.doLogin(TEST_SESSION_ID, userLoginRequest);

        final boolean result = accountService.doLogout(TEST_SESSION_ID);
        assertTrue(result);
    }

    @Test
    public void testDoLogoutNoSessionFail() throws Exception {
        final String invalidSessionId = "000000000000000000000000";
        final boolean result = accountService.doLogout(invalidSessionId);
        assertFalse(result);
    }

    @Test
    public void testIsUserMatch() throws Exception {

    }
}