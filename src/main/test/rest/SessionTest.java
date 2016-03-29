package rest;

import models.UserLoginRequest;
import models.UserProfile;
import org.junit.Test;

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
        final long id = addUser();
        final Response resp = target("sessioin").request().get();

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
    public void testUserLogout() throws Exception {

    }
}