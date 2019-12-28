package com.exer.calendarfy.controller;

import com.exer.calendarfy.data.Event;
import com.exer.calendarfy.data.UserProfile;
import com.exer.calendarfy.profile.ProfileCrud;
import com.exer.calendarfy.push.FCMPush;
import com.exer.calendarfy.push.PushRequest;
import com.exer.calendarfy.response.BaseResponse;
import com.exer.calendarfy.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;


@RestController
public class EventController {

    @Autowired
    ProfileCrud profileCrud;

    @Autowired
    FCMPush fcmPush;

    @GetMapping("/getEventsForProfile")
    public ArrayList<Event> getEventsForProfile(@RequestHeader(value = "profileEmail") String profileEmail) {
        BaseResponse response = new Response();

        UserProfile profile = profileCrud.getProfileByEmail(profileEmail);

        if (profile == null) {
            System.out.println("No profile found for given email");
            return null;
        }

        return profile.getProfileEvents();
    }

    @PostMapping("/addEventForProfile")
    public HashMap<String, String> addEventForProfile(
            @RequestHeader(value = "profileEmail") String profileEmail,
            @RequestHeader(value = "eventTitle") String eventTitle,
            @RequestHeader(value = "eventDesc") String eventDesc,
            @RequestHeader(value = "sendPush", required = false) Boolean shouldSendPush
    ) {
        BaseResponse response = new Response();
        Event event = new Event(eventTitle, eventDesc);

        profileCrud.addEventToProfile(profileEmail, event);

        if (shouldSendPush != null && shouldSendPush) {
            UserProfile profile = profileCrud.getProfileByEmail(profileEmail);

            if (profile != null && profile.getDeviceToken() != null) {
                PushRequest request = new PushRequest(profile.getDeviceToken(), event);
                fcmPush.sendPushToSender(request);
            } else {
                System.out.println("Unable to send push as profile does not exist or device token is null");
            }
        }

        response.setIsSuccessful(true);
        return response.getResponse();
    }

    @PostMapping("/deleteEventForProfile")
    public HashMap<String, String> deleteEventForProfile(
            @RequestHeader(value = "profileEmail") String profileEmail,
            @RequestHeader(value = "eventTitle") String eventTitle,
            @RequestHeader(value = "eventDesc") String eventDesc
    ) {
        BaseResponse response = new Response();
        Event event = new Event(eventTitle, eventDesc);

        profileCrud.deleteEventForProfile(profileEmail, event);

        response.setIsSuccessful(true);
        return response.getResponse();
    }
}
