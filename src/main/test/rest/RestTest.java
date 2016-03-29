package rest;

import main.Context;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.hibernate.cfg.Configuration;
import services.AccountService;
import services.AccountServiceImpl;
import services.config.ConfigFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Application;

import static org.mockito.Mockito.mock;

/**
 * Created by gantz on 29.03.16.
 */
public class RestTest extends JerseyTest{
    @Override
    protected Application configure() {
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
}
