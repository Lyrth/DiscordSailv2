package com.github.vaerys.tags.admintags;

import com.github.vaerys.enums.TagType;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.objects.adminlevel.AdminCCObject;
import com.github.vaerys.templates.TagAdminSubTagObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagVarGet extends TagAdminSubTagObject {

    public TagVarGet(int priority, TagType... types) {
        super(priority, types);
    }

    protected static String regex = "(<varStore>;.*?)(/v>)"; // [A-Za-z0-9_\\;=]

    @Override
    public String execute(String from, CommandObject command, String args, AdminCCObject cc) {
        String vname = getSubTag(from);
        String contents = cc.getContents(false);

        String value = "";

        try {
            // make vname valid
            vname = vname.replace("(","<u0028>")
                    .replace(")","<u0029>")
                    .replace(";","<u003B>")
                    .replace("=","<u003D>")
                    .replaceAll("<(.*?)>","<u003C>$1<u003E>");

            // find the var
            Matcher matcher = Pattern.compile(regex).matcher(contents);
            if (matcher.find()) {   // has varstore?
                String varStore = matcher.group(1);
                if (varStore.contains(";" + vname + "=")) {   // has var?
                    Matcher v = Pattern.compile(";\\Q" + vname + "\\E=(.*?);").matcher(varStore);
                    if(v.find()) value = v.group(1);
                }
            }

            // then revert value
            value = value.replace("<u0028>", "(")
                    .replace("<u0029>", ")")
                    .replace("<u003B>", ";")
                    .replace("<u003D>", "=")
                    .replaceAll("<u003C>(.*?)<u003E>","<$1>");


        } catch (Exception e) {
            return replaceFirstTag(from, error);
        }

        return replaceFirstTag(from, value);
    }

    @Override
    protected String subTagUsage() {
        return "VarName";
    }

    @Override
    public String tagName() {
        return "varGet";
    }

    @Override
    protected int argsRequired() {
        return 0;
    }

    @Override
    protected String usage() {
        return null;
    }

    @Override
    protected String desc() {
        return "Fetches the value of VarName from variable storage.";
    }
}