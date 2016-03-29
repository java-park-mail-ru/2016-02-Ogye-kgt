package rest;

import main.Context;
import models.UserProfile;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;
import services.AccountService;
import services.AccountServiceImpl;
import services.config.ConfigFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class UsersTest extends RestTest {
    private UserProfile testUser = new UserProfile("testlogin", "qwerty", "test@mail.ru");


    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_OK = 200;


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

    public long addUser() {
        final Entity<UserProfile> userEntity = Entity.entity(testUser, MediaType.APPLICATION_JSON_TYPE);

        final Response response = target("user").request().post(userEntity);
        assertEquals(STATUS_OK, response.getStatus());

        final String resp = response.readEntity(String.class);
        final JsonReader jsonReader = Json.createReader(new StringReader(resp));
        final JsonObject jsonResponse = jsonReader.readObject();
        return jsonResponse.getInt("id");
    }

    public void createAndGetUser() {
        final long id = addUser();

        final Response getUserResponse = target("user").path(Long.toString(id)).request().get();
        final UserProfile createdUser = getUserResponse.readEntity(UserProfile.class);

        assertEquals(testUser.getLogin(), createdUser.getLogin());
        assertEquals(testUser.getEmail(), createdUser.getEmail());
    }

    public UserProfile getUser(long id) {
        final Response getUserResponse = target("user").path(Long.toString(id)).request().get();
        return getUserResponse.readEntity(UserProfile.class);
    }
}