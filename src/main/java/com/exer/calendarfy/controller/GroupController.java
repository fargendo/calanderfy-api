package com.exer.calendarfy.controller;

import com.exer.calendarfy.group.GroupCrud;
import com.exer.calendarfy.log.Log;
import com.exer.calendarfy.model.Event;
import com.exer.calendarfy.response.BaseResponse;
import com.exer.calendarfy.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class GroupController {

    @Autowired
    GroupCrud groupCrud;

    @GetMapping("/getEventsForGroup")
    public ResponseEntity<ArrayList<Event>> getEventsForGroup(
            @RequestHeader(value = "groupName") String groupName
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(groupCrud.getEventsForGroup(groupName));
    }

    @PostMapping("/createNewGroup")
    public ResponseEntity<HashMap<String, String>> createNewGroup(
            @RequestHeader(value = "groupName") String groupName,
            @RequestHeader(value = "profileEmail") String profileEmail

    ) {
        BaseResponse response = new Response();

        boolean success = groupCrud.createNewGroup(groupName, profileEmail);

        if (!success) {
            response.setIsSuccessful(false);
            response.addResponseHeader("error", "group already exists");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getResponse());
        }

        response.setIsSuccessful(true);
        response.addResponseHeader("success", "group created with name " + groupName);

        return ResponseEntity.status(HttpStatus.OK).body(response.getResponse());
    }

    @PostMapping("/deleteGroup")
    public ResponseEntity<HashMap<String, String>> deleteGroup (
            @RequestHeader(value = "groupName") String groupName,
            @RequestHeader(value = "profileEmail") String profileEmail
    ) {
        BaseResponse response = new Response();

        boolean success = groupCrud.deleteGroup(profileEmail, groupName);

        if (!success) {
            response.setIsSuccessful(false);
            response.addResponseHeader("error", "you do not have permission to edit this group");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.getResponse());
        }

        response.setIsSuccessful(true);
        response.addResponseHeader("success", "group deleted with name " + groupName);

        return ResponseEntity.status(HttpStatus.OK).body(response.getResponse());
    }

    @PostMapping("/addUserToGroup")
    public ResponseEntity<HashMap<String, String>> addUsersToGroup(
            @RequestHeader(value = "groupName") String groupName,
            @RequestHeader(value = "profileEmail") String profileEmail
    ) {
        BaseResponse response = new Response();

        boolean success = groupCrud.addUserToGroup(profileEmail, groupName);

        if (!success) {
            response.setIsSuccessful(false);
            response.addResponseHeader("error", "group does not exist");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getResponse());
        }

        response.setIsSuccessful(true);
        response.addResponseHeader("success", "user added to group");

        return ResponseEntity.status(HttpStatus.OK).body(response.getResponse());
    }

    @PostMapping("/removeUserFromGroup")
    public ResponseEntity<HashMap<String, String>> removeUserFromGroup(
            @RequestHeader(value = "groupName") String groupName,
            @RequestHeader(value = "profileEmail") String profileEmail
    ) {
        BaseResponse response = new Response();

        boolean success = groupCrud.removeUserFromGroup(profileEmail, groupName);

        if (!success) {
            response.setIsSuccessful(false);
            response.addResponseHeader("error", "group does not exist");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getResponse());
        }

        response.setIsSuccessful(true);
        response.addResponseHeader("success", "user removed from group");

        return ResponseEntity.status(HttpStatus.OK).body(response.getResponse());
    }

    @PostMapping("/addEventToGroup")
    public ResponseEntity<HashMap<String, String>> addEventToGroup(
            @RequestHeader(value = "groupName") String groupName,
            @RequestHeader(value = "eventTitle") String eventTitle,
            @RequestHeader(value = "eventDesc") String eventDesc

    ) {
        BaseResponse response = new Response();

        boolean success = groupCrud.addEventToGroup(groupName, new Event(eventTitle, eventDesc));

        if (!success) {
            response.setIsSuccessful(false);
            response.addResponseHeader("error", "group does not exists");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getResponse());
        }

        response.setIsSuccessful(true);
        response.addResponseHeader("success", "event added to group");

        return ResponseEntity.status(HttpStatus.OK).body(response.getResponse());
    }

    @PostMapping("/removeEventFromGroup")
    public ResponseEntity<HashMap<String, String>> removeEventFromGroup(
            @RequestHeader(value = "groupName") String groupName,
            @RequestHeader(value = "eventTitle") String eventTitle,
            @RequestHeader(value = "eventDesc") String eventDesc

    ) {
        BaseResponse response = new Response();

        boolean success = groupCrud.removeEventFromGroup(groupName, new Event(eventTitle, eventDesc));

        if (!success) {
            response.setIsSuccessful(false);
            response.addResponseHeader("error", "group does not exist");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getResponse());
        }

        response.setIsSuccessful(true);
        response.addResponseHeader("success", "event removed from group");

        return ResponseEntity.status(HttpStatus.OK).body(response.getResponse());
    }

    @GetMapping("/getUsersInGroup")
    public ResponseEntity<ArrayList<String>> getUsersInGroup(
            @RequestHeader(value = "groupName") String groupName
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(groupCrud.getGroupByGroupName(groupName).getGroupUsers());
    }
}
