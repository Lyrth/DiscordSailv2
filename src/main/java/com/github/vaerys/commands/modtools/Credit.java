package com.github.vaerys.commands.modtools;

import com.github.vaerys.commands.characters.CharEditModes;
import com.github.vaerys.enums.ChannelSetting;
import com.github.vaerys.enums.SAILType;
import com.github.vaerys.handlers.GuildHandler;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.objects.userlevel.CharacterObject;
import com.github.vaerys.objects.userlevel.CurrencyObject;
import com.github.vaerys.objects.utils.ArgsProcessor;
import com.github.vaerys.objects.utils.DualVar;
import com.github.vaerys.objects.utils.SplitFirstObject;
import com.github.vaerys.templates.Command;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;

public class Credit extends Command {

    /*  Possible inputs
    0       1       2       3
    charId  10
    charId  10CRD
    charId  10      CRD
    charId  +-=     10
    charId  +-=     10CRD
    charId  +-=     10      CRD
    charId  +-=     CRD     10

     */

    @Override
    public String execute(String inp, CommandObject command){
        ArgsProcessor args = new ArgsProcessor(inp);
        String charName = args.get(0);
        if (!args.get(1).matches("[+\\-=]")){  // args now from 1
            args = new ArgsProcessor("+ " + args.getRange(1,-1));
        } else {
            args = new ArgsProcessor(args.getRange(1,-1));
        }
        if (args.length() < 2)  // mode, amount
            return "> Missing args.";
        for (CharacterObject ch : command.guild.characters.getCharacters(command.guild.get())) {
            if (ch.getName().equalsIgnoreCase(charName)) {
                if (args.get(1).matches("[0-9.]+[^0-9.]+")){
                    try {
                        args = new ArgsProcessor(String.join(" ",
                                args.get(0),
                                "" + Double.parseDouble(args.get(1).replaceAll("([0-9.]+)[^0-9.]+", "$1")),
                                args.get(1).replaceAll("[0-9.]+([^0-9.]+)", "$1")
                        ));
                    } catch (NumberFormatException e) {
                        return "> Amount not valid.";
                    }
                }
                String mode = args.get(0);

                CurrencyObject currency;
                String alias = null;
                double amount;
                if (args.length() == 2) {  // mode and amount: default currency
                    try {
                        amount = Double.parseDouble(args.get(1));
                    } catch (NumberFormatException e) {
                        return "> Amount not valid.";
                    }
                    try {
                        currency = command.guild.currencies.getCurrencies().entrySet().iterator().next().getValue();
                    } catch (NullPointerException e){
                        return "> There is no currency for the server.";
                    }
                } else {
                    String currencyName = args.get(1).matches("^[^0-9.].*") ? args.get(1) : args.get(2);
                    try {
                        amount = Double.parseDouble(args.get(1).matches("^[0-9.].*") ? args.get(1) : args.get(2));
                    } catch (NumberFormatException e) {
                        return "> Amount not valid.";
                    }
                    DualVar<String,String> type = command.guild.currencies.findCurrencyType(currencyName);
                    if (type == null) return "> Currency identifier is invalid.";
                    currency = command.guild.currencies.getCurrency(type.getVar1());
                    alias = type.getVar2();
                }
                switch (mode) {
                    case "+":
                        if (alias == null){
                            ch.modifyCredits(currency.getId(),currency.getLongValue(amount));
                        } else {
                            ch.modifyCredits(currency.getId(),currency.getLongAliasValue(amount,alias));
                        }
                        return String.format("> Added %.1f %s to **%s**. Current %s: **%.1f**".replace("1","" +currency.getDecimalDigits()),
                                alias == null ? amount : currency.getAliasAsBase(amount,alias),
                                currency.getName(),
                                ch.getName(),
                                currency.getName(),
                                currency.getRealValue(ch.getCredits(currency.getId())));
                    case "-":
                        if (alias == null){
                            ch.modifyCredits(currency.getId(),-currency.getLongValue(amount));
                        } else {
                            ch.modifyCredits(currency.getId(),-currency.getLongAliasValue(amount,alias));
                        }
                        return String.format("> Subtracted %.1f %s from **%s**. Current %s: **%.1f**".replace("1","" +currency.getDecimalDigits()),
                                alias == null ? amount : currency.getAliasAsBase(amount,alias),
                                currency.getName(),
                                ch.getName(),
                                currency.getName(),
                                currency.getRealValue(ch.getCredits(currency.getId())));
                    case "=":
                        if (alias == null){
                            ch.setCredits(currency.getId(),currency.getLongValue(amount));
                        } else {
                            ch.setCredits(currency.getId(),currency.getLongAliasValue(amount,alias));
                        }
                        return String.format("> Set **%s**'s current %s to **%." + currency.getDecimalDigits() + "f**",
                                ch.getName(),
                                currency.getName(),
                                currency.getRealValue(ch.getCredits(currency.getId())));
                    default:
                        return "> Mode not Valid.";
                }
            }
        }
        return "> Character **" + charName + "** not found.";
    }

    /* //OLD CODE
    public String executea(String args, CommandObject command) {
        SplitFirstObject a = new SplitFirstObject(args);
        if (a.getFirstWord() == null || a.getFirstWord().isEmpty()) {
            return "> Character not specified.";
        }
        if (a.getRest() == null || a.getRest().isEmpty()) {
            return "> Mode not specified.";
        }
        SplitFirstObject mode;
        if (!a.getRest().split("\\s+")[0].matches("[+\\-=]"))  // default to + if mode not there
            mode = new SplitFirstObject("+ " + a.getRest());
        else
            mode = new SplitFirstObject(a.getRest());
        System.out.println(mode.getAll());
        for (CharacterObject c : command.guild.characters.getCharacters(command.guild.get())) {
            if (c.getName().equalsIgnoreCase(a.getFirstWord())) {
                if (mode.getRest() == null || mode.getRest().isEmpty()) {
                    return "> Amount not specified.";
                }
                String[] rest = mode.getRest().split("\\s+");
                System.out.println(mode.getRest());

                if (rest[0].matches("[0-9.]+\\w+")){
                    try {
                        rest = new String[]{
                                "" + Double.parseDouble(rest[0].replaceAll("([0-9.]+)\\w+", "$1")),
                                rest[0].replaceAll("[0-9.]+(\\w+)", "$1")
                        };
                    } catch (NumberFormatException e) {
                        return "> Amount not valid.";
                    }
                }

                CurrencyObject currency;
                String alias = null;

                double amount;
                if (rest.length == 1) {  //default to first currency
                    try {
                        amount = Double.parseDouble(rest[0]);
                    } catch (NumberFormatException e) {
                        return "> Amount not valid.";
                    }
                    try {
                        currency = command.guild.currencies.getCharCurrencies().entrySet().iterator().next().getValue();
                    } catch (NullPointerException e){
                        return "> There is no currency for the server.";
                    }
                } else {
                    String currencyname = rest[0].matches("\\w+") ? rest[0] : rest[1];
                    try {
                        amount = Double.parseDouble(rest[0].matches("[0-9.]+") ? rest[0] : rest[1]);
                    } catch (NumberFormatException e) {
                        return "> Amount not valid.";
                    }
                    DualVar<String,String> type = command.guild.currencies.findCurrencyType(currencyname);
                    if (type == null) return "> Currency identifier is invalid.";
                    currency = command.guild.currencies.getCurrency(type.getVar1());
                    alias = type.getVar2();
                }
                //command.setAuthor(command.guild.getUserByID(c.getUserID()));
                switch (mode.getFirstWord()) {
                    case "+":
                        if (alias == null){
                            c.modifyCredits(currency.getId(),currency.getLongValue(amount));
                        } else {
                            c.modifyCredits(currency.getId(),currency.getLongAliasValue(amount,alias));
                        }
                        return String.format("> Added %.1f to %s. Current %s: %.1f",
                                currency.getAliasAsBase(amount,alias),
                                c.getName(),
                                currency.getName(),
                                currency.getRealValue(c.getCredits(currency.getId())));
                    case "-":
                        if (alias == null){
                            c.modifyCredits(currency.getId(),-currency.getLongValue(amount));
                        } else {
                            c.modifyCredits(currency.getId(),-currency.getLongAliasValue(amount,alias));
                        }
                        return String.format("> Subtracted %.1f from %s. Current %s: %.1f",
                                currency.getAliasAsBase(amount,alias),
                                c.getName(),
                                currency.getName(),
                                currency.getRealValue(c.getCredits(currency.getId())));
                    case "=":
                        if (alias == null){
                            c.setCredits(currency.getId(),currency.getLongValue(amount));
                        } else {
                            c.setCredits(currency.getId(),currency.getLongAliasValue(amount,alias));
                        }
                        return String.format("> Set %s's current %s to %.1f",
                                c.getName(),
                                currency.getName(),
                                currency.getRealValue(c.getCredits(currency.getId())));
                    default:
                        return "> Mode not Valid.";
                }
            }
        }
        return "> Character **" + a.getFirstWord() + "** not found.";
    }*/

    @Override
    protected String[] names() {
        return new String[]{"Credit"};
    }

    @Override
    public String description(CommandObject command) {
        return "Edit the credits for a character.";
    }

    @Override
    protected String usage() {
        return "[Character ID] (+/-/=) [Amount] (Currency)";
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
        return new Permissions[]{Permissions.MANAGE_MESSAGES};
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
    protected void init() {

    }
}
