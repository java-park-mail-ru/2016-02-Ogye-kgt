package services;

import models.UserLoginRequest;
import models.UserProfile;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AccountServiceImpl implements AccountService {
    private Map<Long, UserProfile> users = new ConcurrentHashMap<>();
    private Map<String, UserProfile> sessions = new ConcurrentHashMap<>();

    private SessionFactory sessionFactory;

    public AccountServiceImpl() {
        // FIXME: убрать, когда будет покрыто тестами.
        addUser(new UserProfile("admin", "admin", "admin@mail.ru"));
        addUser(new UserProfile("guest", "12345", "guest@mail.ru"));

        final Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserProfile.class);

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/tp_app");
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "root");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");

        sessionFactory = createSessionFactory(configuration);
    }

    @Override
    public String getLocalStatus() {
        final String status;
        try (Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            status = transaction.getStatus().toString();
            transaction.commit();
        }
        return status;
    }
    // FIXME: тестовый метод, не выкладывать в прод.
    @Override
    public Collection<UserProfile> getAllUsers() {
        return users.values();
    }

    @Override
    public boolean addUser(UserProfile userProfile) {
        if (isUserExist(userProfile) || !userProfile.isValid()) return false;
        users.put(userProfile.getId(), userProfile);
        return true;
    }

    @Override
    public UserProfile getUser(long userId) {
        return users.get(userId);
    }

    @Nullable
    public UserProfile getUserByLogin(String login) {
        for (UserProfile user : users.values()) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public UserProfile getUserBySession(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public boolean removeUser(long userId) {
        if (!users.containsKey(userId)) return false;
        users.remove(userId);
        return true;
    }

    @Override
    public boolean removeUser(String sessionId, long userId) {
        final UserProfile userProfile = sessions.get(sessionId);
        if (userProfile == null) return false;
        if (userProfile.getId() != userId) return false;
        users.remove(userId);
        return true;
    }

    @Override
    public boolean updateUser(final String sessionId, final long userId, UserProfile newProfile) {
        final UserProfile userProfile = getUserBySession(sessionId);
        if (userProfile == null) return false;
        if (userProfile.getId() != userId) return false;

        if (!UserProfile.isLoginValid(newProfile.getLogin())) return false;
        if (!UserProfile.isPasswordValid(newProfile.getPassword())) return false;
        if (!UserProfile.isEmailValid(newProfile.getEmail())) return false;

        userProfile.setLogin(newProfile.getLogin());
        userProfile.setPassword(newProfile.getPassword());
        userProfile.setEmail(newProfile.getEmail());
        users.replace(userId, userProfile);
        return true;
    }

    public boolean isUserExist(UserProfile userProfile) {
        final String login = userProfile.getLogin();
        for (UserProfile curUserProfile : users.values()) {
            if (curUserProfile.getLogin().equals(login)) return true;
        }
        return false;
    }

    public boolean isUserExist(String login) {
        for (UserProfile curUserProfile : users.values()) {
            if (curUserProfile.getLogin().equals(login)) return true;
        }
        return false;
    }

    private void addSession(String sessionId, UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    private void closeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    @Override
    public boolean isAuthorised(String sessioinId) {
        return sessions.containsKey(sessioinId);
    }

    @Override
    @Nullable
    public UserProfile doLogin(String sessionId, UserLoginRequest user) {
        final String login = user.getLogin();
        final UserProfile userProfile = getUserByLogin(login);
        if (userProfile == null) return null;
        addSession(sessionId, userProfile);
        //noinspection UnnecessaryLocalVariable
        final boolean isPasswordEqual = userProfile.getPassword().equals(user.getPassword());
        if (isPasswordEqual) {
            return userProfile;
        } else {
            return null;
        }
    }

    @Override
    public boolean doLogout(String sessionId) {
        if (getUserBySession(sessionId) == null) return false;
        closeSession(sessionId);
        return true;
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        final ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

}
