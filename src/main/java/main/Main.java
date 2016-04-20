package main;

import mechanics.WebSocketGameServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.hibernate.cfg.Configuration;
import rest.Session;
import rest.Users;
import services.AccountService;
import services.AccountServiceImpl;
import services.config.ConfigFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;


public class Main {
    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void main(String[] args) throws Exception {
        int port = -1;
        String host = "localhost";
        // Читаем параметры.
        try (final FileInputStream fis = new FileInputStream("config/server.properties")) {
            final Properties properties = new Properties();
            properties.load(fis);

            port = Integer.parseInt(properties.getProperty("port"));
            host = properties.getProperty("host");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Property file not found.");
        }


        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        final Server server = new Server(port);
        final ServletContextHandler contextHandler = new ServletContextHandler(server, "/api/", ServletContextHandler.SESSIONS);

        final Context context = new Context();
        final Configuration configuration = ConfigFactory.create(ConfigFactory.TYPE.PRODUCTION);
        context.put(AccountService.class, new AccountServiceImpl(configuration));
        contextHandler.addServlet(new ServletHolder(new WebSocketGameServlet()), "/game");

        final ResourceConfig config = new ResourceConfig(Users.class, Session.class);
        config.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(context);
            }
        });

        final ServletHolder servletHolder = new ServletHolder(new ServletContainer(config));
        servletHolder.setInitParameter("javax.ws.rs.Application", "main.RestApplication");

        contextHandler.addServlet(servletHolder, "/*");
        server.start();
        server.join();
    }
}