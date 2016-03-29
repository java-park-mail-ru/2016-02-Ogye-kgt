package rest;

import main.Context;
import models.UserLoginRequest;
import models.UserProfile;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.hibernate.cfg.Configuration;
import services.AccountService;
import services.AccountServiceImpl;
import services.AccountServiceImplTest;
import services.config.ConfigFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class RestTest extends JerseyTest {
    public static final UserProfile testUser = new UserProfile("testlogin", "qwerty", "test@mail.ru");
    public static final UserLoginRequest userLoginRequest = new UserLoginRequest(testUser.getLogin(), testUser.getPassword());
    public static final Entity<UserProfile> userEntity = Entity.entity(testUser, MediaType.APPLICATION_JSON_TYPE);
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_UNAUTHORIZED = 401;
    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_OK = 200;


    @Override
    protected Application configure() {
        final Context context = new Context();
        final Configuration configuration = ConfigFactory.create(ConfigFactory.TYPE.DEBUG);
        context.put(AccountService.class, new AccountServiceImpl(configuration));

        final ResourceConfig config = new ResourceConfig(Users.class, Session.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpSession mockSession = mock(HttpSession.class);
        when(request.getSession()).thenReturn(mockSession);
        when(mockSession.getId()).thenReturn(AccountServiceImplTest.TEST_SESSION_ID);
        //noinspection AnonymousInnerClassMayBeStatic
        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(context);
                bind(request).to(HttpServletRequest.class);
            }
        });

        return config;
    }

    public long addUser() {
        return addUser(testUser);
    }

    public long addUser(UserProfile user) {
//        final Entity<UserProfile> userEntity = Entity.entity(user, MediaType.APPLICATION_JSON_TYPE);

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

    public Response login(UserProfile user) {
        final UserLoginRequest loginRequest = new UserLoginRequest(user.getLogin(), user.getPassword());
        final Entity<UserLoginRequest> userLoginReqEntity = Entity.entity(loginRequest, MediaType.APPLICATION_JSON_TYPE);
        return target("session").request().put(userLoginReqEntity);
    }

    public Response checkAuth() {
        return target("session").request().get();
    }

    public Response checkAuth(String session) {
        return target("session").request().cookie("JSESSIONID", session).get();
    }
}
