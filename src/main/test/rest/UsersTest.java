package rest;

import models.UserProfile;
import org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UsersTest extends RestTest {

    @Test
    public void testCreateUser() throws Exception {
        createAndGetUser();
    }

    @Test
    public void testCreateInvalidUser() throws Exception {
        final Response resp = addUserRequest(new UserProfile("a", "b"));
        assertEquals(STATUS_FORBIDDEN, resp.getStatus());
    }

    @Test
    public void testCreateExistUserFail() throws Exception {
        addUser(testUser);
        final Entity<UserProfile> userEntity = Entity.entity(testUser, MediaType.APPLICATION_JSON_TYPE);
        final Response response = target("user").request().post(userEntity);

        assertEquals(STATUS_FORBIDDEN, response.getStatus());
    }


    @Test
    public void getUserById() throws Exception {
        createAndGetUser();
    }

    @Test
    public void deleteUserForbidden() throws Exception {
        final long id = addUser(testUser);
        final Response response = target("user").path(Long.toString(id)).request().delete();
        assertEquals(STATUS_FORBIDDEN, response.getStatus());
    }

    @Test(expected = MessageBodyProviderNotFoundException.class)
    public void deleteUser() throws Exception {
        final long id = addUser(testUser);
        login(testUser);
        final Response resp = target("user").path(Long.toString(id)).request().delete();
        assertEquals(STATUS_OK, resp.getStatus());
        getUser(id);
    }

    @Test
    public void updateUser() throws Exception {
        final long id = addUser(testUser);
        login(testUser);
        final UserProfile newProfile = new UserProfile("newLogin", "newPassword");
        final Entity<UserProfile> newProfileEntity = Entity.entity(newProfile, MediaType.APPLICATION_JSON_TYPE);
        final Response resp = target("user").path(Long.toString(id)).request().put(newProfileEntity);
        assertEquals(STATUS_OK, resp.getStatus());
        final UserProfile updatedProfile = getUser(id);
        assertEquals(newProfile.getLogin(), updatedProfile.getLogin());
    }

    @Test
    public void updateUserForbidden() throws Exception {
        final long id = addUser(testUser);
        final UserProfile newProfile = new UserProfile("newLogin", "newPassword");
        final Entity<UserProfile> newProfileEntity = Entity.entity(newProfile, MediaType.APPLICATION_JSON_TYPE);
        final Response resp = target("user").path(Long.toString(id)).request().put(newProfileEntity);
        assertEquals(STATUS_FORBIDDEN, resp.getStatus());
    }


}