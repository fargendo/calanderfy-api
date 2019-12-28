package com.exer.calendarfy.controller;

import com.exer.calendarfy.data.UserProfile;
import com.exer.calendarfy.profile.ProfileCrud;
import com.exer.calendarfy.response.BaseResponse;
import com.exer.calendarfy.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class ProfileController {

    @Autowired
    ProfileCrud profileCrud;

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
