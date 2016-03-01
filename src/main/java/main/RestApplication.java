package main;

import rest.Session;
import rest.Users;
import services.AccountService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by e.shubin on 25.02.2016.
 */
@ApplicationPath("api")
public class RestApplication extends Application {
    @Override
    public Set<Object> getSingletons() {
        final HashSet<Object> objects = new HashSet<>();
        final AccountService accountService = new AccountService();
        objects.add(new Users(accountService));
        objects.add(new Session(accountService));
        return objects;
    }
}
