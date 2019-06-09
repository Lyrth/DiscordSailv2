package com.github.vaerys.commands.characters;

import com.github.vaerys.enums.ChannelSetting;
import com.github.vaerys.enums.SAILType;
import com.github.vaerys.handlers.GuildHandler;
import com.github.vaerys.handlers.RequestHandler;
import com.github.vaerys.main.Utility;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.objects.userlevel.CharacterObject;
import com.github.vaerys.templates.Command;
import com.github.vaerys.utilobjects.XEmbedBuilder;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

import java.util.ArrayList;

/**
 * Created by Vaerys on 26/02/2017.
 */
public class CharStats extends Command {

    @Override
    public String execute(String args, CommandObject command) {
        for (CharacterObject object : command.guild.characters.getCharacters(command.guild.get())) {
            if (object.getName().equalsIgnoreCase(args)) {
                XEmbedBuilder builder = new XEmbedBuilder(command);
                builder.withTitle(object.getNickname());

                IUser user = command.guild.getUserByID(object.getUserID());
                if (user == null) {
                    builder.withFooterText("Author: No longer on this server | Character ID: " + object.getName());
                } else {
                    builder.withFooterText("Author: " + user.getDisplayName(command.guild.get()) + " | Character ID: " + object.getName());
                }

                builder.withColor(GuildHandler.getUsersColour(user, command.guild.get()));

                StringBuilder description = new StringBuilder();
                description
                        .append("**HP**: ")
                        .append(object.getStat("hp"))
                        .append("/")
                        .append(object.getStat("hptotal"))
                        .append("\n\n")
                        .append(object.printStats());

                builder.withDesc(description.toString());
                if (object.getAvatarURL() != null && !object.getAvatarURL().isEmpty()) {
                    if (object.getAvatarURL().contains("\n") || object.getAvatarURL().contains(" ")) {
                        return "> An Error Occurred. Avatar url needs to be reset.";
                    }
                    builder.withThumbnail(object.getAvatarURL());
                }
                RequestHandler.sendEmbedMessage("", builder, command.channel.get());
                return null;
            }
        }
        return "> Character with that name not found.";
    }

    @Override
    protected String[] names() {
        return new String[]{"CharStats","CharStat","StatsChar"};
    }

    @Override
    public String description(CommandObject command) {
        return "Gives stats about a certain character.";
    }

    @Override
    protected String usage() {
        return "[Character ID]";
    }

    @Override
    protected SAILType type() {
        return SAILType.CHARACTER;
    }

    @Override
    protected ChannelSetting channel() {
        return ChannelSetting.CHARACTER;
    }

    @Override
    protected Permissions[] perms() {
        return new Permissions[0];
    }

    @Override
    protected boolean requiresArgs() {
        return true;
    }

    @Override
    protected boolean doAdminLogging() {
        return false;
    }

    @Override
    public void init() {

    }
}
