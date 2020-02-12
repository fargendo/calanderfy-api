package com.exer.calendarfy.profile;

import com.exer.calendarfy.model.Event;
import com.exer.calendarfy.model.UserProfile;

import java.util.List;

public interface ProfileCrud {
    boolean createProfile(String profileEmail);
    UserProfile getProfileByEmail(String profileEmail);
    boolean addEventToProfile(String requestingUser, String profileEmail, Event event);
    void updateProfile(String profileEmail, String deviceToken);
    List<UserProfile> getAllProfiles();
    boolean deleteEventForProfile(String requestingUser, String profileEmail, Event event);
    void addAuthorizedUserForProfile(String profileEmail, String authorizedEmail);
    void deleteAuthorizedUser(String profileEmail, String authorizedEmail);
    boolean addUserToGroup(String requestingUser, String profileEmail, String groupName);
    boolean removeUserFromGroup(String requestingUser, String profileEmail, String groupName);
}
