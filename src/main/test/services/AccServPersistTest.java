package services;

import models.UserProfile;
import org.eclipse.jetty.server.Authentication;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by gantz on 25.03.16.
 */
public class AccServPersistTest {
    private AccServPersist accServ;

    @Before
    public void setUp() throws Exception {
        accServ = new AccServPersist();
    }

    @Test
    public void testGetLocalStatus() throws Exception {
        final String status = accServ.getLocalStatus();
        System.out.println(status);
    }

    @Test
    public void testSave() throws Exception {
        final UserProfile up1 = new UserProfile("Alexandr", "qwerty", "alex@mail.ru");
        final UserProfile up2 = new UserProfile("Анатолий", "qwerty", "anatilijus@mail.ru");
        accServ.save(up1);
        accServ.save(up2);
        final UserProfile cup1 = accServ.readByName("Alexandr");
        final UserProfile cup2 = accServ.readByName("Анатолий");

    }

    @Test
    public void testRead() throws Exception {
    }

    @Test
    public void testReadByName() throws Exception {
        final UserProfile up1 = accServ.readByName("Alexandr");
        final UserProfile up2 = accServ.readByName("Анатолий");
        assertEquals(up1.getEmail(), "alex@mail.ru");
        assertEquals(up2.getEmail(), "anatilijus@mail.ru");
    }

    @Test
    public void testReadAll() throws Exception {

    }

    @Test
    public void testShutdown() throws Exception {

    }
}