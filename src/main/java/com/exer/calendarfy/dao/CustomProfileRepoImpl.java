package com.exer.calendarfy.dao;

import com.exer.calendarfy.data.Event;
import com.exer.calendarfy.data.UserProfile;
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

        System.out.println(result);
    }

    @Override
    public void updateProfileWithDeviceToken(UserProfile profile, String deviceToken) {
        Query query = new Query(Criteria.where("profileEmail").is(profile.getProfileEmail()));
        Update update = new Update();
        update.set("deviceToken", deviceToken);

        template.updateFirst(query, update, UserProfile.class);
    }
}
