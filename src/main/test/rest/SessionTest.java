package rest;

import main.RestApplication;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;

import static org.junit.Assert.*;

/**
 * Created by gantz on 13.03.16.
 */
public class SessionTest extends JerseyTest{
    @Override
    protected Application configure() {
        return new RestApplication();
    }

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