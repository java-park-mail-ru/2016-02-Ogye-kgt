package rest;

import models.UserLoginRequest;
import models.UserProfile;
import org.junit.Test;
import services.AccountServiceImplTest;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;


public class SessionTest extends RestTest {

    @Test
    public void testCheckAuth() throws Exception {
        addUser(testUser);
        login(testUser);
        final Response resp = checkAuth(AccountServiceImplTest.TEST_SESSION_ID);
        assertEquals(STATUS_OK, resp.getStatus());
    }

    @Test
    public void testCheckFail() throws Exception {
        addUser(testUser);
        final Response resp = checkAuth(AccountServiceImplTest.TEST_SESSION_ID);
        assertEquals(STATUS_UNAUTHORIZED, resp.getStatus());
    }

    @Test
    public void testUserLogin() throws Exception {
        final long id = addUser();
        final Entity<UserLoginRequest> userLoginReqEntity = Entity.entity(userLoginRequest, MediaType.APPLICATION_JSON_TYPE);

        final Response response = target("session").request().put(userLoginReqEntity);
        assertEquals(STATUS_OK, response.getStatus());

        final String resp = response.readEntity(String.class);
        final JsonReader jsonReader = Json.createReader(new StringReader(resp));
        final JsonObject jsonResponse = jsonReader.readObject();
        assertEquals(id, jsonResponse.getInt("id"));
    }

    @Test
    public void testLogin() throws Exception {
        addUser(testUser);
        final Response resp = login(testUser);
        assertEquals(STATUS_OK, resp.getStatus());
    }

    @Test
    public void testLoginFail() throws Exception {
        final Response resp = login(testUser);
        assertEquals(STATUS_NOT_FOUND, resp.getStatus());
    }

    @Test
    public void testUserLogout() throws Exception {

    }
}