package com.exer.calendarfy.profile;

import com.exer.calendarfy.data.Event;
import com.exer.calendarfy.data.UserProfile;

import java.util.List;

public interface ProfileCrud {
    UserProfile getProfileByEmail(String profileEmail);
    void addEventToProfile(String profileEmail, Event event);
    void updateProfile(String profileEmail, String deviceToken);
    List<UserProfile> getAllProfiles();
    void deleteEventForProfile(String profileEmail, Event event);
}
