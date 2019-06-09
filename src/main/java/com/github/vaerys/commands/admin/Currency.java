package com.github.vaerys.commands.admin;

import com.github.vaerys.commands.characters.CharEditModes;
import com.github.vaerys.enums.ChannelSetting;
import com.github.vaerys.enums.SAILType;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.objects.userlevel.CharacterObject;
import com.github.vaerys.objects.userlevel.CurrencyObject;
import com.github.vaerys.objects.utils.ArgsProcessor;
import com.github.vaerys.objects.utils.SplitFirstObject;
import com.github.vaerys.templates.Command;
import sx.blah.discord.handle.obj.Permissions;

public class Currency extends Command {



    /*  Possible inputs
    0       1       2       3       4
    add     BaseId
    add     BaseId  2
    add     BaseId  Name    *
    add     BaseId  Name    *       2
    remove  BaseId
    edit    BaseId  name    Name    *
    edit    BaseId  symbol  SYM
    addal-  BaseId  aliasId
    addal-  BaseId  aliasId 1.5
    addal-  BaseId  aliasId Name    *
    addal-  BaseId  aliasId Name    *       1.5
    rmval-  BaseId  aliasId
    edtal-  BaseId  aliasId name    Name    *
    edtal-  BaseId  aliasId symbol  SYM
    edtal-  BaseId  aliasId factor  1.5
     */


    @Override
    public String execute(String inp, CommandObject command) {
        ArgsProcessor args = new ArgsProcessor(inp);
        if (args.length() < 2){
            return missingArgs(command);
        }
        String mode = args.get(0).toLowerCase();
        String baseId = args.get(1).toLowerCase();
        switch (mode){
            case "add":
                int decimalPlaces;
                try {
                    decimalPlaces = Integer.parseInt(args.get(-1));
                    args = new ArgsProcessor(args.getRange(0,-2));  // remove the number
                } catch (NumberFormatException e) {
                    decimalPlaces = 2;
                }
                command.guild.currencies.addCurrency(
                        new CurrencyObject(baseId,
                                args.isPresent(2) ?
                                        args.getRange(2,-1) :
                                        baseId.substring(0,1).toUpperCase() + baseId.substring(1),
                                decimalPlaces));
                return "> Added new currency **" + baseId + "**.";
            case "remove":
                return command.guild.currencies.removeCurrency(baseId) ?
                        "> Removed currency." :
                        "> There is no such currency with ID " + baseId + ".";
            case "edit":
                String editMode = args.get(2).toLowerCase();
                if (!args.isPresent(3)) return missingArgs(command);
                try {
                    switch (editMode) {
                        case "name":
                            command.guild.currencies.getCurrency(baseId).setName(args.getRange(3,-1));
                            return "> Modified **" + baseId + "**'s name to **" + args.getRange(3,-1) + "**.";
                        case "symbol":
                            command.guild.currencies.getCurrency(baseId).setSymbol(args.get(3));
                            return "> Modified **" + baseId + "**'s symbol to **" + args.get(3) + "**.";
                        default:
                            return "> Edit mode not valid.";
                    }
                } catch (NullPointerException e) {
                    return "> There is no such currency with ID " + baseId + ".";
                }
            case "addalias":
                if (args.length() < 3) return missingArgs(command);
                double factor;
                try {
                    factor = Double.parseDouble(args.get(-1));
                    args = new ArgsProcessor(args.getRange(0,-2));  // remove the number
                } catch (NumberFormatException e) {
                    factor = 1.0;
                }
                String aliasId = args.get(2).toLowerCase();
                if (command.guild.currencies.getCurrency(baseId) == null)
                    return "> Currency **" + baseId + "** doesn't exist.";
                command.guild.currencies.getCurrency(baseId).addAlias(aliasId,
                        factor,
                        args.getRange(3,-1),null);
                return "> Added new alias **" + aliasId + "** for **" + baseId + "**.";
            case "removealias":
                if (args.length() < 3) return missingArgs(command);
                aliasId = args.get(2).toLowerCase();
                if (command.guild.currencies.getCurrency(baseId) == null)
                    return "> Currency **" + baseId + "** doesn't exist.";
                return command.guild.currencies.getCurrency(baseId).removeAlias(aliasId) ?
                        "> Removed currency alias **" + aliasId + "**." :
                        "> There is no such currency alias with ID " + aliasId + ".";
            case "editalias":
                if (args.length() < 5) return missingArgs(command);
                aliasId = args.get(2).toLowerCase();
                editMode = args.get(3).toLowerCase();
                if (command.guild.currencies.getCurrency(baseId) == null)
                    return "> Currency **" + baseId + "** doesn't exist.";
                if (command.guild.currencies.getCurrency(baseId).getAlias(aliasId) == null){
                    return "> Alias **" + aliasId + "** doesn't exist.";
                }
                switch (editMode){
                    case "name":
                        command.guild.currencies.getCurrency(baseId).getAlias(aliasId).name = args.getRange(4,-1);
                        return "> Modified **" + aliasId + "**'s name to **" + args.getRange(4,-1) + "**.";
                    case "symbol":
                        command.guild.currencies.getCurrency(baseId).getAlias(aliasId).symbol = args.get(4);
                        return "> Modified **" + aliasId + "**'s symbol to **" + args.get(4) + "**.";
                    case "factor":
                        try {
                            command.guild.currencies.getCurrency(baseId).getAlias(aliasId).factor = Double.parseDouble(args.get(4));
                        } catch (NumberFormatException e) {
                            return "> Not a valid number.";
                        }
                        return "> Modified **" + aliasId + "**'s factor to **" + args.get(4) + "**.";
                    default:
                        return "> Alias edit mode not valid.";
                }
            default:
                return "> Mode not valid.";
        }
    }

    /*  OLD CODE
    @Override
    public String executea(String args, CommandObject command) {
        SplitFirstObject mode = new SplitFirstObject(args);
        String rest = mode.getRest();
        if (rest == null || rest.isEmpty()) {
            return missingArgs(command);
        }
        switch (mode.getFirstWord().toLowerCase()) {  // TODO: check id not starting with number
            case "add":
                String[] input = rest.split("\\s+");
                Integer decimalPlaces;
                try {
                    decimalPlaces = Integer.parseInt(input[input.length - 1]);
                } catch (NumberFormatException e) {
                    decimalPlaces = null;
                }
                String id = input[0].toLowerCase();
                if (input.length == 1) {
                    command.guild.currencies.addCurrency(new CurrencyObject(id, rest, 2));
                } else if (decimalPlaces != null) {
                    StringBuilder name = new StringBuilder();
                    for (int i = 1; i < input.length - 1; i++) {
                        name.append(input[i]).append(" ");
                    }
                    command.guild.currencies.addCurrency(new CurrencyObject(id, name.toString().trim(), decimalPlaces));
                } else {
                    command.guild.currencies.addCurrency(new CurrencyObject(id, rest.replace(id + "\\s+", ""), 2));
                }
                return "> Added new currency **" + id + "**.";
            case "remove":
                return command.guild.currencies.removeCurrency(new SplitFirstObject(rest).getFirstWord().toLowerCase()) ?
                        "> Removed currency." :
                        "> There is no such currency with ID " + new SplitFirstObject(rest).getFirstWord().toLowerCase() + ".";
            case "edit":
                SplitFirstObject edit = new SplitFirstObject(rest);
                String editArgs = edit.getRest();
                if (editArgs == null || editArgs.isEmpty()) {
                    return missingArgs(command);
                }
                SplitFirstObject args1 = new SplitFirstObject(editArgs);
                String currencyId = args1.getFirstWord().toLowerCase();
                String value = args1.getRest();
                if (value == null || value.isEmpty()) {
                    return missingArgs(command);
                }
                try {
                    switch (edit.getFirstWord().toLowerCase()) {
                        case "name":
                            command.guild.currencies.getCurrency(currencyId).setName(value);
                            return "> Modified **" + currencyId + "**'s name to **" + value + "**.";
                        case "symbol":
                            command.guild.currencies.getCurrency(currencyId).setSymbol(value);
                            return "> Modified **" + currencyId + "**'s symbol to **" + value + "**.";
                        default:
                            return "> Edit mode not valid.";
                    }
                } catch (NullPointerException e) {
                    return "> There is no such currency with ID " + currencyId.toLowerCase() + ".";
                }
            case "addalias":
                String[] input1 = rest.split("\\s+");
                if (input1.length < 2) return "> Missing arguments. Run help to know more.";
                Double factor;
                try {
                    factor = Double.parseDouble(input1[input1.length - 1]);
                } catch (NumberFormatException e) {
                    factor = null;
                }
                String parentId = input1[0].toLowerCase();
                String aliasId = input1[1].toLowerCase();
                if (command.guild.currencies.getCurrency(parentId) == null)
                    return "> Currency **" + parentId + "** doesn't exist.";
                if (input1.length == 2) {
                    command.guild.currencies.getCurrency(parentId).addAlias(aliasId, 1, null, null);
                } else if (input1.length == 3 && factor != null) {
                    command.guild.currencies.getCurrency(parentId).addAlias(aliasId, factor, null, null);
                } else {
                    StringBuilder name = new StringBuilder();
                    for (int i = 2; i < input1.length - (factor == null ? 0 : 1); i++) {
                        name.append(input1[i]).append(" ");
                    }
                    command.guild.currencies.getCurrency(parentId).addAlias(aliasId, factor == null ? 1 : factor, name.toString().trim(), null);
                }
                return "> Added new alias **" + aliasId + "** for **" + parentId + "**.";
            case "removealias":
                String[] input2 = rest.split("\\s+");
                if (input2.length < 2) return "> Missing arguments. Run help to know more.";
                String parentId1 = input2[0].toLowerCase();
                String aliasId1 = input2[1].toLowerCase();
                if (command.guild.currencies.getCurrency(parentId1) == null)
                    return "> Currency **" + parentId1 + "** doesn't exist.";
                return command.guild.currencies.getCurrency(parentId1).removeAlias(aliasId1) ?
                        "> Removed currency alias." :
                        "> There is no such currency alias with ID " + aliasId1 + ".";
            case "editalias":
                String[] input3 = rest.split("\\s+");
                if (input3.length < 2) return "> Missing arguments. Run help to know more.";
                String parentId2 = input3[0].toLowerCase();
                String aliasId2 = input3[1].toLowerCase();
                if (command.guild.currencies.getCurrency(parentId2) == null)
                    return "> Currency **" + parentId2 + "** doesn't exist.";
                switch (input3[2].toLowerCase()) {
                    case "name":
                        StringBuilder name = new StringBuilder();
                        for (int i = 3; i < input3.length ; i++) {
                            name.append(input3[i]).append(" ");
                        }
                        command.guild.currencies.getCurrency(parentId2).getAlias(aliasId2).name = name.toString().trim();
                        return "> Modified **" + aliasId2 + "**'s name to **" + name.toString() + "**.";
                    case "symbol":
                        command.guild.currencies.getCurrency(parentId2).getAlias(aliasId2).symbol = input3[3];
                        return "> Modified **" + aliasId2 + "**'s symbol to **" + input3[3] + "**.";
                    case "factor":
                        try {
                            command.guild.currencies.getCurrency(parentId2).getAlias(aliasId2).factor = Double.parseDouble(input3[3]);
                        } catch (NumberFormatException e) {
                            return "> Not a valid number.";
                        }
                        return "> Modified **" + aliasId2 + "**'s factor to **" + input3[3] + "**.";
                    default:
                        return "> Edit mode not valid.";
                }
            default:
                return "> Mode not valid.";
        }
    }

     */

    @Override
    protected String[] names() {
        return new String[]{"Currency"};
    }

    @Override
    public String description(CommandObject command) {
        return "Create currencies for the server.";
    }

    @Override
    protected String usage() {
        return "ask lyrthras lol";
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
    protected void init() {

    }
}
