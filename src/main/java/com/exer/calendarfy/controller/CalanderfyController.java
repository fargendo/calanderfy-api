package com.exer.calendarfy.controller;

import com.exer.calendarfy.data.Event;
import com.exer.calendarfy.data.UserProfile;
import com.exer.calendarfy.profile.ProfileCrud;
import com.exer.calendarfy.push.FCMPush;
import com.exer.calendarfy.push.PushRequest;
import com.exer.calendarfy.response.BaseResponse;
import com.exer.calendarfy.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class CalanderfyController {

    @Autowired
    ProfileCrud profileCrud;

    @Autowired
    FCMPush fcmPush;

    @RequestMapping("/healthcheck")
    public HashMap<String, String> healthcheck() {
        BaseResponse response = new Response();
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();

        response.addResponseHeader("uptime", String.valueOf(mxBean.getUptime()));
        response.setIsSuccessful(true);

        return response.getResponse();
    }

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

    @PostMapping("/updateProfile")
    public HashMap<String, String> registerProfile (
            @RequestHeader(value = "profileEmail") String profileEmail,
            @RequestHeader(value = "deviceToken") String deviceToken
    ) {
        BaseResponse response = new Response();

        profileCrud.updateProfile(profileEmail, deviceToken);

        response.setIsSuccessful(true);
        return response.getResponse();
    }

    @GetMapping("/getAllProfiles")
    public List<UserProfile> getAllProfile() {
        return profileCrud.getAllProfiles();
    }
}
