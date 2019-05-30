package com.github.vaerys.objects.adminlevel;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Vaerys on 27/08/2016.
 */
public class MutedUserObject {
    long userID;
    long remainderSecs;
    List<Long> roleIDs; // set in requesthandler when this information is already available

    public MutedUserObject(long userID, long remainderSecs, List<IRole> roles) {
        this.userID = userID;
        this.remainderSecs = remainderSecs;
        this.roleIDs = roles.stream().map(r -> r.getLongID()).collect(Collectors.toList());
    }

    public long getID() {
        return userID;
    }

    public long getRemainderSecs() {
        return remainderSecs;
    }

    public List<IRole> getRoles(IGuild guild) {
        if (roleIDs == null || roleIDs.isEmpty()) return new LinkedList<>();
        return roleIDs.stream().map(guild::getRoleByID).collect(Collectors.toList());
    }

    public void setRemainderSecs(long remainderSecs) {
        this.remainderSecs = remainderSecs;
    }

    public void tickDown(int i) {
        if (remainderSecs != -1) {
            remainderSecs -= i;
            if (remainderSecs < 0) {
                remainderSecs = 0;
            }
        }
    }

}
