package com.github.vaerys.commands.admin;

import com.github.vaerys.enums.ChannelSetting;
import com.github.vaerys.enums.SAILType;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.templates.Command;
import sx.blah.discord.handle.obj.Permissions;

public class SetLikeArtXp extends Command {

    @Override
    public String execute(String args, CommandObject command) {
        try {
            int xp = Integer.parseInt(args);
            if (xp < 1) {
                return "> Must be a positive number.";
            }
            command.guild.config.likeArtXp = xp;
            return "> Art liking XP set to " + xp;
        } catch (NumberFormatException e) {
            return "> Not a valid number.";
        }
    }

    @Override
    protected String[] names() {
        return new String[]{"SetLikeArtXp"};
    }

    @Override
    public String description(CommandObject command) {
        return "Allows for editing the pixel reward for having art likes.";
    }

    @Override
    protected String usage() {
        return "[Pixels]";
    }

    @Override
    protected SAILType type() {
        return SAILType.ADMIN;
    }

    @Override
    protected ChannelSetting channel() {
        return null;
    }

    @Override
    protected Permissions[] perms() {
        return new Permissions[]{Permissions.MANAGE_SERVER};
    }

    @Override
    protected boolean requiresArgs() {
        return true;
    }

    @Override
    protected boolean doAdminLogging() {
        return true;
    }

    @Override
    public void init() {

    }
}
