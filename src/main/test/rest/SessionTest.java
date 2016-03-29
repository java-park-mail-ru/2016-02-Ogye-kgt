package rest;

import main.Context;
import main.RestApplication;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import services.AccountService;
import services.AccountServiceImpl;
import services.config.ConfigFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Application;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by gantz on 13.03.16.
 */
public class SessionTest extends RestTest{

    @Test
    public void testCheckAuth() throws Exception {

    }

    @Test
    public void testUserLogin() throws Exception {

    }

    @Test
    public void testUserLogout() throws Exception {

    }
}