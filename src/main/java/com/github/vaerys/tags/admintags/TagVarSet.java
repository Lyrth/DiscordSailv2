package com.github.vaerys.tags.admintags;

import com.github.vaerys.enums.TagType;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.objects.adminlevel.AdminCCObject;
import com.github.vaerys.templates.TagAdminSubTagObject;
import com.github.vaerys.commands.adminccs.EditAdminCC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagVarSet extends TagAdminSubTagObject {

    public TagVarSet(int priority, TagType... types) {
        super(priority, types);
    }

    protected static String regex = "(<varStore>;.*?)(/v>)"; // [A-Za-z0-9_\\;=]

    @Override
    public String execute(String from, CommandObject command, String args, AdminCCObject cc) {
        String value = getContents(from);
        String vname = getSubTag(from);
        String contents = cc.getContents(false);
        String result;

        try {
            // check vname empty
            if ((vname == null || vname.isEmpty() )) throw new Exception(); // This might be unreachable.

            // make vname/value valid
            vname = vname.replace("(","<u0028>")
                    .replace(")","<u0029>")
                    .replace(";","<u003B>")
                    .replace("=","<u003D>")
                    .replaceAll("<(.*?)>","<u003C>$1<u003E>");

            value = value.replace("(","<u0028>")
                    .replace(")","<u0029>")
                    .replace(";","<u003B>")
                    .replace("=","<u003D>")
                    .replaceAll("<(.*?)>","<u003C>$1<u003E>");

            // add/update the variable storage text
            Matcher matcher = Pattern.compile(regex).matcher(contents);
            if (matcher.find()) {   // already has varstore
                String varStore = matcher.group(1);
                if (varStore.contains(";" + vname + "=")) {   // has var
                    result = varStore.replaceFirst("(;\\Q"+vname+"\\E=).*?;","$1"+value+";");
                    result = contents.replaceFirst(regex,result + "$2");
                } else {   // no var
                    result = contents.replaceFirst(regex,"$1" + vname + "=" + value + ";$2");
                }
            } else {   // no varstore
                result = contents + "<varStore>;" + vname + "=" + value + ";/v>";
            }

            // check cc length
            if (result.length() > 10000) throw new Exception();

            // push the update to the cc
            EditAdminCC editor = new EditAdminCC();
            editor.execute(cc.getName() + " " + result, command);

        } catch (Exception e) {
            return replaceFirstTag(from, error);
        }

        return replaceFirstTag(from, "");
    }

    @Override
    protected String subTagUsage() {
        return "VarName";
    }

    @Override
    public String tagName() {
        return "varSet";
    }

    @Override
    protected int argsRequired() {
        return 1;
    }

    @Override
    protected String usage() {
        return "Value";
    }

    @Override
    protected String desc() {
        return "Assigns Value to VarName in variable storage.";
    }
}