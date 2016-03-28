package rest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import main.Context;
import main.RestApplication;
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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class UsersTest extends JerseyTest {
    private UserProfile testUser;

    @Override
    protected Application configure() {
        testUser = new UserProfile("testlogin", "qwerty", "test@mail.ru");

        final Context context = new Context();
        final Configuration configuration = ConfigFactory.create(ConfigFactory.TYPE.DEBUG);
        context.put(AccountService.class, new AccountServiceImpl(configuration));

        final ResourceConfig config = new ResourceConfig(Users.class);
        final HttpServletRequest request = mock(HttpServletRequest.class);
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


    @Test
    public void testCreateUser() throws Exception {
        final Entity<UserProfile> userEntity = Entity.entity(testUser, MediaType.APPLICATION_JSON_TYPE);
        final Response response = target("user").request().post(userEntity);

        final Long createdUserId = response.readEntity(UserProfile.class).getId();
        final Response getUserResponse = target("user").path(createdUserId.toString()).request().get();
        final UserProfile createdUser = getUserResponse.readEntity(UserProfile.class);

        assertEquals(testUser.getLogin(), createdUser.getLogin());
        assertEquals(testUser.getEmail(), createdUser.getEmail());
    }

    @Test
    public void testCreateExistUserFail() throws Exception {
        final Entity<UserProfile> userEntity = Entity.entity(testUser, MediaType.APPLICATION_JSON_TYPE);
        final Response response = target("user").request().post(userEntity);
    }
}