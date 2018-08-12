package com.github.vaerys.commands.modtools;

import com.github.vaerys.enums.ChannelSetting;
import com.github.vaerys.enums.SAILType;
import com.github.vaerys.handlers.RequestHandler;
import com.github.vaerys.main.Globals;
import com.github.vaerys.main.Utility;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.objects.utils.DualVar;
import com.github.vaerys.objects.utils.SplitFirstObject;
import com.github.vaerys.templates.Command;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Clear extends Command {

    private static final ScheduledExecutorService selfDestruct = Executors.newSingleThreadScheduledExecutor();

    private static HashMap<Long,DualVar> deleteQueue;  // Can be a List<>, probably.

    @Override
    public String execute(String args, CommandObject command){
        SplitFirstObject contents = new SplitFirstObject(args);
        String rest = contents.getRest();

        int n;  // Amount of messages to delete.
        try {
            n = Integer.parseInt(contents.getFirstWord());
            if (n < 1) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            return "> Invalid number value.";
        }
        if (n > 100) return "> Cannot delete more than 100 messages.";
        else if (n > 15) askDelete(rest,n,command);
        else delete(rest, n, command);

        return null;
    }

    private static void delete(String rest, int n, CommandObject command){
        IChannel channel = command.channel.get();

        List<IMessage> messages = new ArrayList<>();
        if (rest == null || rest.isEmpty()) {  // no rules, only delete n messages
            // asList() returns a fixed-length array, hence the `new ArrayList<>`
            messages = new ArrayList<>(
                    Arrays.asList(channel.getMessageHistoryFrom(command.message.longID,n).asArray())
            );
            n = messages.size();
            // command message was included, get another message
            if (n > 0 && command.message.longID == messages.get(0).getLongID()) {
                try {
                    messages.add(
                            channel.getMessageHistoryFrom(
                                    messages.get(messages.size() - 1).getLongID(),2).asArray()[1]
                    );
                } catch (ArrayIndexOutOfBoundsException ignored){}  // when [1] fails, i.e. reached end of channel
                n = messages.size();
                n--;  // not including the command itself in delete count
            }
        } else {  // There are rules to check
            rest = Utility.escapeRegex(rest);
            if (rest.length() > 1 && !rest.equals("--")) {
                if (rest.startsWith("-")) rest = ".*?" + rest.substring(1);
                if (rest.endsWith("-") && !rest.endsWith("\\u005C-")) rest = rest.substring(0,rest.length()-1) + ".*?";
            }
            // (?s): . matches everything including newlines
            // (?i): case insensitivity
            rest = "(?si)" + rest.replace("\\u005C-","-");
            Pattern pattern = Pattern.compile(rest);

            // Iterate over messages
            List<IMessage> toScan = Arrays.asList(channel.getMessageHistoryFrom(command.message.longID,n*2).asArray());
            long lastID;
            boolean endOfChannel = false;
            int deleted = 0;
            while (messages.size() < n && !endOfChannel){
                if (toScan.size() < n*2) endOfChannel = true;
                for (IMessage msg : toScan)
                    if (pattern.matcher(msg.getContent()).matches() &&
                            messages.size() < n &&
                            msg.getLongID() != command.message.longID &&
                            !messages.contains(msg)) {
                        messages.add(msg);
                        deleted++;
                    }
                lastID = toScan.get(toScan.size()-1).getLongID();
                if (messages.size() < n) {
                    toScan = new ArrayList<>(
                            Arrays.asList(channel.getMessageHistoryFrom(lastID, (n * 2) + 1).asArray())
                    );
                    // toScan.remove(0).getContent();
                } else break;
            }
            n = deleted;
        }

        // TODO: Check time from react, add offset for request propagation time
        // Actually, D4J bulkDelete also checks for message age, but we need to count messages here
        long current = command.message.get().getTimestamp().getEpochSecond();
        int olds = 0;  // old message (> 2 weeks) counter

        for (int i = messages.size(); i > 0; i--)
            if (current - messages.get(i-1).getTimestamp().getEpochSecond() > 1209600)  // older than 2 weeks
                olds++;
            else break;  // no more 'old' messages
        n = n - olds;    // deleted messages - two-week-old messages

        if (RequestHandler.deleteBulk(channel,messages).get() && n > 0) {  // bulk delete failed
            RequestHandler.sendMessage(
                    "> Deleting messages failed. Make sure I have enough permissions.", channel);
            return;
        }

        command.message.delete();

        // send notice
        sendSelfDestruct("> Deleted " +
                        n + " message" + (n==1 ? ".":"s.") +
                        (olds>0 ? " " + olds + " old message" + (olds==1 ? "":"s") + " not deleted." : ""),
                channel,5);

    }

    private static void askDelete(String rest, int n, CommandObject command){
        IMessage msg = RequestHandler.sendMessage(
                "> Are you sure you want to delete "+n+" messages?",command).get();
        msg.addReaction(Utility.getReaction("white_check_mark"));
        msg.addReaction(Utility.getReaction("x"));
        command.message.delete();
        deleteQueue.put(msg.getLongID(), new DualVar<>(rest,n));
    }

    public static void checkQueue(CommandObject command){
        if (deleteQueue == null || !deleteQueue.containsKey(command.message.longID)) return;
        DualVar<String,Integer> c = deleteQueue.get(command.message.longID);
        deleteQueue.remove(command.message.longID);
        delete(c.getVar1(),c.getVar2(),command);
    }

    private static void sendSelfDestruct(String message, IChannel channel, long delay) {
        IMessage tmpMsg = RequestHandler.sendMessage(message, channel).get();
        selfDestruct.schedule(() -> RequestHandler.deleteMessage(tmpMsg),delay, TimeUnit.SECONDS);
    }

    @Override
    public String description(CommandObject command) {
        return "Bulk deletes a number of recent messages, optionally deletes messages based on a simple pattern. " +
                "Excludes the message that called this command.\n" +
                "Usage:\n" +
                "`" + Globals.defaultPrefixCommand + "Clear 5` delete 5 recent messages.\n" +
                "`" + Globals.defaultPrefixCommand + "Clear 3 Text-` delete 3 messages starting with \"Text\".\n" +
                "`" + Globals.defaultPrefixCommand + "Clear 3 -Text` delete 3 messages ending with \"Text\".\n" +
                "`" + Globals.defaultPrefixCommand + "Clear 3 -Text-` delete 3 messages containing \"Text\".\n" +
                "`" + Globals.defaultPrefixCommand + "Clear 3 -Text\\-` delete 3 messages ending with \"Text-\".\n";
    }

    @Override
    public void init() {

    }

    @Override
    protected String[] names() {
        return new String[]{"Clear","Clr"};
    }

    @Override
    protected String usage() {
        return "[Number of Messages] (Text/Pattern)";
    }

    @Override
    protected SAILType type() {
        return SAILType.MOD_TOOLS;
    }

    @Override
    protected ChannelSetting channel() {
        return null;
    }

    @Override
    protected Permissions[] perms() {
        return new Permissions[]{Permissions.MANAGE_MESSAGES, Permissions.MENTION_EVERYONE};
    }

    @Override
    protected boolean requiresArgs() {
        return true;
    }

    @Override
    protected boolean doAdminLogging() {
        return false;
    }

}

