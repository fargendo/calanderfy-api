package com.exer.calendarfy.dao.profile;

import com.exer.calendarfy.model.Event;
import com.exer.calendarfy.model.UserProfile;
import com.exer.calendarfy.log.Log;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomProfileRepoImpl implements CustomProfileRepo {
    @Autowired
    MongoTemplate template;

    @Override
    public void updateProfileWithEvent(UserProfile profile, Event event) {
        Query query = new Query(Criteria.where("profileEmail").is(profile.getProfileEmail()));
        Update update = new Update();
        ArrayList<Event> updatedEventList = profile.getProfileEvents();
        updatedEventList.add(event);
        update.set("profileEvents", updatedEventList);

        template.updateFirst(query, update, UserProfile.class);
    }

    @Override
    public void deleteEventForProfile(UserProfile profile, Event event) {
        Query query = new Query(Criteria.where("profileEmail").is(profile.getProfileEmail()));
        Update update = new Update();
        ArrayList<Event> updatedEventList = profile.getProfileEvents();

        updatedEventList.removeIf(e -> e.getEventTitle().equals(event.getEventTitle()));
        update.set("profileEvents", updatedEventList);
        UpdateResult result = template.updateFirst(query, update, UserProfile.class);

        Log.d(result.toString());
    }

    @Override
    public void updateProfileWithDeviceToken(UserProfile profile, String deviceToken) {
        Query query = new Query(Criteria.where("profileEmail").is(profile.getProfileEmail()));
        Update update = new Update();
        update.set("deviceToken", deviceToken);

        template.updateFirst(query, update, UserProfile.class);
    }

    @Override
    public void updateProfileWithAuthorizedEmail(UserProfile profile, String authorizedEmail) {
        Query query = new Query(Criteria.where("profileEmail").is(profile.getProfileEmail()));
        Update update = new Update();
        ArrayList<String> updatedEventList = profile.getAuthorizedUsers();
        updatedEventList.add(authorizedEmail);

        update.set("authorizedUsers", updatedEventList);

        template.updateFirst(query, update, UserProfile.class);
    }

    @Override
    public void deleteAuthorizedUser(UserProfile profile, String authorizedEmail) {
        Query query = new Query(Criteria.where("profileEmail").is(profile.getProfileEmail()));
        Update update = new Update();
        ArrayList<String> updatedEventList = profile.getAuthorizedUsers();
        updatedEventList.remove(authorizedEmail);
        update.set("authorizedUsers", updatedEventList);

        template.updateFirst(query, update, UserProfile.class);
    }

    @Override
    public void addUserToGroup(UserProfile profile, String groupName) {
        Query query = new Query(Criteria.where("profileEmail").is(profile.getProfileEmail()));
        Update update = new Update();
        ArrayList<String> groupsList = profile.getGroups();
        groupsList.add(groupName);
        update.set("groups", groupsList);

        template.updateFirst(query, update, UserProfile.class);
    }

    @Override
    public void removeUserFromGroup(UserProfile profile, String groupName) {
        Query query = new Query(Criteria.where("profileEmail").is(profile.getProfileEmail()));
        Update update = new Update();
        ArrayList<String> groupsList = profile.getGroups();
        groupsList.remove(groupName);
        update.set("groups", groupsList);

        template.updateFirst(query, update, UserProfile.class);
    }
}