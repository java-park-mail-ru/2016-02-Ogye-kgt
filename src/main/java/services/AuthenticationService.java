package services;

import models.UserProfile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by gantz on 01.03.16.
 */
public class AuthenticationService {
    private Map<String, UserProfile> sessions = new ConcurrentHashMap<>();

}
