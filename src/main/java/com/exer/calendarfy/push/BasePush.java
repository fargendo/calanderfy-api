package com.exer.calendarfy.push;

public interface BasePush {
    void sendPushToSender(PushRequest pushRequest);
    boolean validatePush(PushRequest pushRequest);
}
