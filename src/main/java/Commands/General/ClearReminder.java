package Commands.General;

import Commands.CommandObject;
import Interfaces.Command;
import Main.Globals;
import Objects.ReminderObject;
import sx.blah.discord.handle.obj.Permissions;

/**
 * Created by Vaerys on 05/04/2017.
 */
public class ClearReminder implements Command{
    @Override
    public String execute(String args, CommandObject command) {
        boolean wasfound = false;
        for (ReminderObject r: Globals.getGlobalData().getReminders()){
            if (r.getUserID().equals(command.authorSID)){
                wasfound = true;
                if (r.isSent()){
                    return "> I could not clear your reminder as it is about to be sent.";
                }
            }
        }
        if (wasfound) {
            Globals.getGlobalData().removeReminder(command.authorSID);
            return "> Reminder cleared";
        }else {
            return "> You have no reminders set";
        }
    }

    @Override
    public String[] names() {
        return new String[]{"ClearReminder"};
    }

    @Override
    public String description() {
        return "Removes your global reminder";
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public String type() {
        return TYPE_GENERAL;
    }

    @Override
    public String channel() {
        return null;
    }

    @Override
    public Permissions[] perms() {
        return new Permissions[0];
    }

    @Override
    public boolean requiresArgs() {
        return false;
    }

    @Override
    public boolean doAdminLogging() {
        return false;
    }

    @Override
    public String dualDescription() {
        return null;
    }

    @Override
    public String dualUsage() {
        return null;
    }

    @Override
    public String dualType() {
        return null;
    }

    @Override
    public Permissions[] dualPerms() {
        return new Permissions[0];
    }
}