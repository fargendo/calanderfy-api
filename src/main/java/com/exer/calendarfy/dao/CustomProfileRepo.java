package com.exer.calendarfy.dao;

import com.exer.calendarfy.data.Event;
import com.exer.calendarfy.data.UserProfile;

public interface CustomProfileRepo {
    void updateProfileWithEvent(UserProfile profile, Event event);
    void updateProfileWithDeviceToken(UserProfile profile, String deviceToken);
}
