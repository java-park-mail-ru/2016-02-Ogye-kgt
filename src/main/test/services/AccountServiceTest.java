package services;

import models.UserProfile;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gantz on 12.03.16.
 */
public class AccountServiceTest {
    private AccountService accountService;

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
}