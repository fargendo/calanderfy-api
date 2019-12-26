package com.exer.calendarfy.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "Profile")
public class UserProfile {
    @Id
    private String id;

    private String profileEmail;
    private ArrayList<Event> profileEvents = new ArrayList<>();
    private String deviceToken;

    public UserProfile(String profileEmail, ArrayList<Event> profileEvents) {
        this.profileEmail = profileEmail;
        this.profileEvents = profileEvents;
    }

    public void addEvent(Event event) {
        profileEvents.add(event);
    }

    public String getProfileEmail() {
        return profileEmail;
    }

    public void setProfileEmail(String profileEmail) {
        this.profileEmail = profileEmail;
    }

    public ArrayList<Event> getProfileEvents() {
        return profileEvents;
    }

    public void setProfileEvents(ArrayList<Event> profileEvents) {
        this.profileEvents = profileEvents;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
