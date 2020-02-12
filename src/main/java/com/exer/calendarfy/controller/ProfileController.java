package com.exer.calendarfy.controller;

import com.exer.calendarfy.model.UserProfile;
import com.exer.calendarfy.log.Log;
import com.exer.calendarfy.profile.ProfileCrud;
import com.exer.calendarfy.response.BaseResponse;
import com.exer.calendarfy.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ProfileController {

    @Autowired
    ProfileCrud profileCrud;


    @PostMapping("/createProfile")
    public ResponseEntity<HashMap<String, String>> createProfile(
            @RequestHeader(value = "profileEmail") String profileEmail
    ) {
        BaseResponse response = new Response();

        boolean success = profileCrud.createProfile(profileEmail);

        if (success) {
            response.setIsSuccessful(true);
            response.addResponseHeader("success", "profile was created");

            return ResponseEntity.status(HttpStatus.OK).body(response.getResponse());
        }

        response.setIsSuccessful(false);
        response.addResponseHeader("error", "profile already exist");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getResponse());
    }


    @PostMapping("/updateProfile")
    public ResponseEntity<HashMap<String, String>> registerProfile (
            @RequestHeader(value = "profileEmail") String profileEmail,
            @RequestHeader(value = "deviceToken") String deviceToken
    ) {
        Log.d("Updating profile: " + profileEmail + " with token: " + deviceToken);

        BaseResponse response = new Response();

        profileCrud.updateProfile(profileEmail, deviceToken);

        response.setIsSuccessful(true);
        return ResponseEntity.status(HttpStatus.OK).body(response.getResponse());
    }

    @GetMapping("/getAllProfiles")
    public ResponseEntity<List<UserProfile>> getAllProfile() {
        return ResponseEntity.status(HttpStatus.OK).body(profileCrud.getAllProfiles());
    }

    @GetMapping("/getGroupsForProfile")
    public ResponseEntity<ArrayList<String>> getGroupsForProfile (
            @RequestHeader(value = "profileEmail") String profileEmail
    ) {
        Log.d("Getting groups for profile: " + profileEmail);

        UserProfile profile = profileCrud.getProfileByEmail(profileEmail);

        return ResponseEntity.status(HttpStatus.OK).body(profile.getGroups());
    }
}
