package rest;

import static org.junit.Assert.*;

import main.RestApplication;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;

/**
 * Created by gantz on 13.03.16.
 */
public class UsersTest extends JerseyTest {
    @Override
    protected Application configure() {
        return new RestApplication();
    }

    @Test
    public void testGetAdmin() {
        final String adminJson = target("user").path("1").request().get(String.class);
        assertEquals(adminJson, "{\"id\":1,\"login\":\"guest\",\"email\":\"guest@mail.ru\"}");
    }

    @Test
    public void testCreateUser() throws Exception {
    }
}