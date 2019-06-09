package com.github.vaerys.commands.characters;

import com.github.vaerys.enums.ChannelSetting;
import com.github.vaerys.enums.SAILType;
import com.github.vaerys.handlers.GuildHandler;
import com.github.vaerys.handlers.RequestHandler;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.objects.userlevel.CharacterObject;
import com.github.vaerys.templates.Command;
import com.github.vaerys.utilobjects.XEmbedBuilder;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

/**
 * Created by Vaerys on 26/02/2017.
 */
public class CharCredits extends Command {

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

                builder.appendField("Currencies: ",object.getCharCurrencies(command.guild),false);
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
        return new String[]{"CharCredits","Credits", "CharCredit"};
    }

    @Override
    public String description(CommandObject command) {
        return "Gives credits information about a certain character.";
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
