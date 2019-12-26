package com.exer.calendarfy.profile;

import com.exer.calendarfy.dao.CustomProfileRepo;
import com.exer.calendarfy.dao.ProfileRepository;
import com.exer.calendarfy.data.Event;
import com.exer.calendarfy.data.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfileCrudImpl implements ProfileCrud {

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    CustomProfileRepo customProfileRepo;

    @Override
    public UserProfile getProfileByEmail(String profileEmail) {
        return profileRepository.findFirstByProfileEmail(profileEmail);
    }

    @Override
    public void addEventToProfile(String profileEmail, Event event) {
        UserProfile profile = profileRepository.findFirstByProfileEmail(profileEmail);

        if (profile == null) {
            System.out.println("No profile found for given email, creating new profile");
            createProfileWithEvent(profileEmail, event);
        } else {
            System.out.println("Adding event to profile");
            addEventToProfile(profile, event);
        }
    }

    @Override
    public void updateProfile(String profileEmail, String deviceToken) {
        UserProfile profile = profileRepository.findFirstByProfileEmail(profileEmail);

        if (profile == null) {
            System.out.println("No profile found for given email, creating new profile");
            registerNewProfile(profileEmail, deviceToken);
        } else {
            System.out.println("Updating device token to profile");
            updateDeviceToken(profile, deviceToken);
        }
    }

    @Override
    public List<UserProfile> getAllProfiles() {
        return profileRepository.findAll();
    }

    private void registerNewProfile(String profileEmail, String deviceToken) {
        ArrayList<Event> eventList = new ArrayList<>();
        UserProfile profile = new UserProfile(profileEmail, eventList);
        profile.setDeviceToken(deviceToken);

        profileRepository.insert(profile);
    }

    private void updateDeviceToken(UserProfile profile, String deviceToken) {
        customProfileRepo.updateProfileWithDeviceToken(profile, deviceToken);
    }

    private void createProfileWithEvent(String profileEmail, Event event) {
        ArrayList<Event> eventList = new ArrayList<>();
        eventList.add(event);

        UserProfile profile = new UserProfile(profileEmail, eventList);
        profileRepository.insert(profile);
    }

    private void addEventToProfile(UserProfile profile, Event event) {
        customProfileRepo.updateProfileWithEvent(profile, event);
    }
}
