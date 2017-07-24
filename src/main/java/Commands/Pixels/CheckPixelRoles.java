package Commands.Pixels;

import Commands.CommandObject;
import Handlers.XpHandler;
import Interfaces.Command;
import Main.Utility;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

/**
 * Created by Vaerys on 12/07/2017.
 */
public class CheckPixelRoles implements Command {
    @Override
    public String execute(String args, CommandObject command) {
        IMessage working = Utility.sendMessage("`Working...`", command.channel).get();
        for (IUser user : command.guild.getUsers()) {
            XpHandler.checkUsersRoles(user.getStringID(), command.guildContent);
        }
        Utility.deleteMessage(working);
        return "> Done.";
    }

    @Override
    public String[] names() {
        return new String[]{"CheckPixelRoles"};
    }

    @Override
    public String description() {
        return "checks all user's Roles and allocates the correct roles based on their Pixel stats.";
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public String type() {
        return TYPE_PIXEL;
    }

    @Override
    public String channel() {
        return null;
    }

    @Override
    public Permissions[] perms() {
        return new Permissions[]{Permissions.MANAGE_SERVER};
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