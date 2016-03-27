package services.dao;

import models.UserProfile;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class UserProfileDAO {
    private Session session;

    public UserProfileDAO(Session session) {
        this.session = session;
    }

    public long save(UserProfile dataSet) {
        return (long) session.save(dataSet);
    }

    public UserProfile read(long id) {
        return session.get(UserProfile.class, id);
    }

    public UserProfile readByName(String name) {
        final Criteria criteria = session.createCriteria(UserProfile.class);
        return (UserProfile) criteria.add(Restrictions.eq("login", name)).uniqueResult();
    }

    public void delete(long id) {
        UserProfile userProfile = session.load(UserProfile.class, id);
        session.delete(userProfile);
    }

    @SuppressWarnings("unchecked")
    public List<UserProfile> readAll() {
        final Criteria criteria = session.createCriteria(UserProfile.class);
        return (List<UserProfile>) criteria.list();
    }
}
