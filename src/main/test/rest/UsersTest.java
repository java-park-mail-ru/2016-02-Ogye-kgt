package rest;

import static org.junit.Assert.*;

import main.RestApplication;
import models.UserProfile;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class UsersTest extends JerseyTest {
    @Override
    protected Application configure() {
        return new RestApplication();
    }

    @Test
    public void testCreateUser() throws Exception {
        final UserProfile testUser = new UserProfile("testlogin", "qwerty", "test@mail.ru");
        final Entity<UserProfile> userEntity = Entity.entity(testUser, MediaType.APPLICATION_JSON_TYPE);
        final Response response = target("user").request().post(userEntity);

        final Long createdUserId = response.readEntity(UserProfile.class).getId();
        final Response getUserResponse = target("user").path(createdUserId.toString()).request().get();
        final UserProfile createdUser = getUserResponse.readEntity(UserProfile.class);

        assertEquals(testUser.getLogin(), createdUser.getLogin());
        assertEquals(testUser.getEmail(), createdUser.getEmail());
    }
}