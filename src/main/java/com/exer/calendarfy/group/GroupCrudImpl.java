package com.exer.calendarfy.group;

import com.exer.calendarfy.dao.group.CustomGroupRepo;
import com.exer.calendarfy.dao.group.GroupRepository;
import com.exer.calendarfy.log.Log;
import com.exer.calendarfy.model.Event;
import com.exer.calendarfy.model.Group;
import com.exer.calendarfy.profile.ProfileCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GroupCrudImpl implements GroupCrud {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    CustomGroupRepo customGroupRepo;

    @Autowired
    ProfileCrud profileCrud;

    @Override
    public boolean createNewGroup(String groupName, String creator) {
        Group group = groupRepository.findFirstByGroupName(groupName);

        if (group == null) {
            group = new Group();

            ArrayList<String> users = new ArrayList<>();
            users.add(creator);

            group.setGroupName(groupName);
            group.setGroupUsers(users);

            groupRepository.insert(group);

            profileCrud.addUserToGroup(creator, creator, groupName, true);

            return true;
        }

        return false;
    }

    @Override
    public Group getGroupByGroupName(String groupName) {
        return groupRepository.findFirstByGroupName(groupName);
    }

    @Override
    public ArrayList<Event> getEventsForGroup(String groupName) {
        return groupRepository.findFirstByGroupName(groupName).getGroupEvents();
    }

    @Override
    public boolean addEventToGroup(String groupName, Event event) {
        Group group = groupRepository.findFirstByGroupName(groupName);

        if (group == null) {
            Log.d("Group does not exist");
            return false;
        }

        customGroupRepo.updateGroupWithEvent(group, event);
        return true;
    }

    @Override
    public boolean removeEventFromGroup(String groupName, Event event) {
        Group group = groupRepository.findFirstByGroupName(groupName);

        if (group == null) {
            Log.d("Group does not exist");
            return false;
        }

        customGroupRepo.removeEventFromGroup(group, event);
        return true;
    }

    @Override
    public boolean addUserToGroup(String profileEmail, String groupName) {
        Group group = groupRepository.findFirstByGroupName(groupName);

        if (group == null) {
            Log.d("Group does not exist");
            return false;
        }

        if (!checkIfUserAlreadyInGroup(group, profileEmail)) {
            Log.d("Adding user " + profileEmail + "to group " + groupName);

            customGroupRepo.addUserToGroup(group, profileEmail);
            profileCrud.addUserToGroup(profileEmail, profileEmail, groupName, true);
        } else {
            Log.d("User is already a part of group");
        }

        return true;
    }

    private boolean checkIfUserAlreadyInGroup(Group group, String requestingUser) {
        for (String user : group.getGroupUsers()) {
            if (user.equals(requestingUser)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean removeUserFromGroup(String profileEmail, String groupName) {
        Group group = groupRepository.findFirstByGroupName(groupName);

        if (group == null) {
            Log.d("Group does not exist");
            return false;
        }

        customGroupRepo.removeUserFromGroup(group, profileEmail);
        profileCrud.removeUserFromGroup(profileEmail, profileEmail, groupName);

        return true;
    }

    @Override
    public boolean deleteGroup(String profileEmail, String groupName) {
        Group group = groupRepository.findFirstByGroupName(groupName);

        if (group == null || !checkIfUserAuthorized(group.getGroupUsers(), profileEmail)) {
            return false;
        }

        for (String user : group.getGroupUsers()) {
            profileCrud.removeUserFromGroup(user, user, groupName);
        }

        groupRepository.delete(group);

        return true;
    }

    private boolean checkIfUserAuthorized(List<String> users, String requestingUser) {
        for (String user : users) {
            if (user.equals(requestingUser)) {
                return true;
            }
        }

        return false;
    }
}