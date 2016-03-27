package services;

import models.UserLoginRequest;
import models.UserProfile;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.jetbrains.annotations.Nullable;
import services.dao.UserProfileDAO;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class AccountServiceImpl implements AccountService {
//    private Map<Long, UserProfile> users = new ConcurrentHashMap<>();
    private Map<String, UserProfile> sessions = new ConcurrentHashMap<>();

    private SessionFactory sessionFactory;

    public AccountServiceImpl() {
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
    @Nullable
    public Collection<UserProfile> getAllUsers() {
        final Collection<UserProfile> users;
        try (Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            final UserProfileDAO dao = new UserProfileDAO(session);
            users = dao.readAll();
            transaction.commit();
        }
        return users;
    }

    @Override
    public long addUser(UserProfile userProfile) throws UserExistsException, InvalidUserException {
        if (isUserExist(userProfile) || !userProfile.isValid()) {
            throw new InvalidUserException("Invalid User");
        }
        final long userId;
        try (Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            final UserProfileDAO dao = new UserProfileDAO(session);
            userId = dao.save(userProfile);
            transaction.commit();
        } catch (HibernateException e) {
            // todo: добавить проверку на тип исключения.
            throw new UserExistsException("User Already Exists");
        }
        return userId;
    }

    @Override
    @Nullable
    public UserProfile getUser(long userId) {
        final UserProfile userProfile;
        try (Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            final UserProfileDAO dao = new UserProfileDAO(session);
            userProfile = dao.read(userId);
            transaction.commit();
        }
        return userProfile;
    }

    @Nullable
    public UserProfile getUserByLogin(String login) {
        final UserProfile userProfile;
        try (Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            final UserProfileDAO dao = new UserProfileDAO(session);
            userProfile = dao.readByName(login);
            transaction.commit();
        }
        return userProfile;
    }

    @Override
    public UserProfile getUserBySession(String sessionId) {
        return sessions.get(sessionId);
    }

    @Override
    public boolean removeUser(long userId) {
        try (Session session = sessionFactory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            final UserProfileDAO dao = new UserProfileDAO(session);
            dao.delete(userId);
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean removeUser(String sessionId, long userId) {
        final UserProfile userProfile = sessions.get(sessionId);
        if (userProfile == null) return false;
        if (userProfile.getId() != userId) return false;
        return removeUser(userId);
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
//        users.replace(userId, userProfile);
        return true;
    }

    public boolean isUserExist(UserProfile userProfile) {
        final String login = userProfile.getLogin();
//        for (UserProfile curUserProfile : users.values()) {
//            if (curUserProfile.getLogin().equals(login)) return true;
//        }
        return false;
    }

    public boolean isUserExist(String login) {
//        for (UserProfile curUserProfile : users.values()) {
//            if (curUserProfile.getLogin().equals(login)) return true;
//        }
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

    public static class UserExistsException extends Exception {
        public UserExistsException(String message) {
            super(message);
        }
    }

    public static class InvalidUserException extends Exception {
        public InvalidUserException(String message) {
            super(message);
        }
    }

}
