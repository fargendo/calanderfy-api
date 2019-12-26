package com.exer.calendarfy.push;

import com.exer.calendarfy.data.Event;

public class PushRequest {
    private String deviceToken;
    private Event event;

    public PushRequest(String deviceToken, Event event) {
        this.deviceToken = deviceToken;
        this.event = event;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
