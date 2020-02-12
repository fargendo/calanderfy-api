package com.exer.calendarfy.profile;

import com.exer.calendarfy.dao.profile.CustomProfileRepo;
import com.exer.calendarfy.dao.profile.ProfileRepository;
import com.exer.calendarfy.model.Event;
import com.exer.calendarfy.model.UserProfile;
import com.exer.calendarfy.log.Log;
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
    public boolean createProfile(String profileEmail) {
        UserProfile profile = profileRepository.findFirstByProfileEmail(profileEmail);

        if (profile == null) {
            Log.d("Creating new profile with profile email: " + profileEmail);
            UserProfile newProfile = new UserProfile(profileEmail);

            profileRepository.insert(newProfile);
            return true;
        }

        return false;
    }

    @Override
    public UserProfile getProfileByEmail(String profileEmail) {
        return profileRepository.findFirstByProfileEmail(profileEmail);
    }

    @Override
    public boolean addEventToProfile(String requestingUser, String profileEmail, Event event) {
        UserProfile profile = profileRepository.findFirstByProfileEmail(profileEmail);

        if (profile == null) {
            Log.d("No profile found for given email, creating new profile");
            createProfileWithEvent(profileEmail, event);
        } else if (checkIfUserCanEditProfile(requestingUser, profile)){
            Log.d("Adding event to profile");
            addEventToProfile(profile, event);
        } else {
            Log.d("User is not authorized to edit profile");
            return false;
        }

        return true;
    }

    @Override
    public void updateProfile(String profileEmail, String deviceToken) {
        UserProfile profile = profileRepository.findFirstByProfileEmail(profileEmail);

        if (profile == null) {
            Log.d("No profile found for given email, creating new profile");
            registerNewProfile(profileEmail, deviceToken);
        } else {
            Log.d("Updating device token to profile");
            updateDeviceToken(profile, deviceToken);
        }
    }

    @Override
    public List<UserProfile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @Override
    public boolean deleteEventForProfile(String requestingUser, String profileEmail, Event event) {
        UserProfile profile = profileRepository.findFirstByProfileEmail(profileEmail);

        if (profile == null) {
            Log.d("No profile found for given email");
        } else if (checkIfUserCanEditProfile(requestingUser, profile)) {
            Log.d("Removing event for profile");
            deleteEvent(profile, event);
        } else {
            Log.d("User is not authorized to edit profile");
            return false;
        }

        return true;
    }

    @Override
    public void addAuthorizedUserForProfile(String profileEmail, String authorizedEmail) {
        UserProfile profile = profileRepository.findFirstByProfileEmail(profileEmail);

        if (profile == null) {
            Log.d("No profile found for given email");
        } else {
            Log.d("Adding authorized user");
            addAuthorizedUser(profile, authorizedEmail);
        }
    }

    @Override
    public void deleteAuthorizedUser(String profileEmail, String authorizedEmail) {
        UserProfile profile = profileRepository.findFirstByProfileEmail(profileEmail);

        if (profile == null) {
            Log.d("No profile found for given email");
        } else {
            Log.d("Delete authorized user");
            deleteAuthorizedUser(profile, authorizedEmail);
        }
    }

    @Override
    public boolean addUserToGroup(String requestingUser, String profileEmail, String groupName) {
        UserProfile profile = profileRepository.findFirstByProfileEmail(profileEmail);

        if (checkIfUserCanEditProfile(requestingUser, profile)) {
            customProfileRepo.addUserToGroup(profile, groupName);
        } else {
            Log.d("User is not authorized to edit profile");
            return false;
        }

        return true;
    }

    @Override
    public boolean removeUserFromGroup(String requestingUser, String profileEmail, String groupName) {
        UserProfile profile = profileRepository.findFirstByProfileEmail(profileEmail);

        if (checkIfUserCanEditProfile(requestingUser, profile)) {
            customProfileRepo.removeUserFromGroup(profile, groupName);
        } else {
            Log.d("User is not authorized to edit profile");
            return false;
        }

        return true;
    }

    private boolean checkIfUserCanEditProfile(String requestingUser, UserProfile profile) {
        ArrayList<String> authorizedUsers = profile.getAuthorizedUsers();

        if (profile.getProfileEmail().equals(requestingUser))
            return true;

        for (String user : authorizedUsers) {
            if (user.equals(requestingUser))
                return true;
        }

        return false;
    }


    private void deleteEvent(UserProfile profile, Event eventTitle) {
        customProfileRepo.deleteEventForProfile(profile, eventTitle);
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

    private void addAuthorizedUser(UserProfile profile, String authorizedUser) {
        customProfileRepo.updateProfileWithAuthorizedEmail(profile, authorizedUser);
    }

    private void deleteAuthorizedUser(UserProfile profile, String authorizedUser) {
        customProfileRepo.deleteAuthorizedUser(profile, authorizedUser);
    }
}