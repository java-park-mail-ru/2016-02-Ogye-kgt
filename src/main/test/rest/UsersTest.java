package rest;

import models.UserProfile;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class UsersTest extends RestTest {

    @Test
    public void testCreateUser() throws Exception {
        createAndGetUser();
    }

    @Test
    public void testCreateExistUserFail() throws Exception {
        final Entity<UserProfile> userEntity = Entity.entity(testUser, MediaType.APPLICATION_JSON_TYPE);
        target("user").request().post(userEntity);
        final Response response = target("user").request().post(userEntity);

        assertEquals(STATUS_FORBIDDEN, response.getStatus());
    }


    @Test
    public void getUserById() throws Exception {
        createAndGetUser();
    }

    @Test
    public void deleteUserById() throws Exception {
        final long id = addUser();
        final Response response = target("user").path(Long.toString(id)).request().delete();
        assertEquals(STATUS_FORBIDDEN, response.getStatus());

//        final Response getUserResponse = target("user").path(Long.toString(id)).request().get();
//        assertEquals(STATUS_FORBIDDEN, getUserResponse.getStatus());
    }

    @Test
    public void updateUser() throws Exception {

    }


}