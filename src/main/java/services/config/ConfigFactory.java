package services.config;

import models.UserProfile;
import org.hibernate.cfg.Configuration;

public class ConfigFactory {
    public enum TYPE {DEBUG, PRODUCTION}

    ;

    public static Configuration create(TYPE type) {
        Configuration configuration = new Configuration();
        configuration = configuration.addAnnotatedClass(UserProfile.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.username", "ogye");
        configuration.setProperty("hibernate.connection.password", "ogye_kgt");
        configuration.setProperty("hibernate.show_sql", "true");
        if (type == TYPE.DEBUG) {
            configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/tp_app_debug");
            configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        } else if (type == TYPE.PRODUCTION) {
            configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/tp_app");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        }
        return configuration;
    }
}
