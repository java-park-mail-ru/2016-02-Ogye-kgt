package services;


import models.UserProfile;

import java.util.List;

public interface DBService {
    String getLocalStatus();

    void save(UserProfile userProfile);

    UserProfile read(long id);

    UserProfile readByName(String name);

    List<UserProfile> readAll();

    void shutdown();
}
