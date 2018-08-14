package com.github.vaerys.tags.admintags;

import com.github.vaerys.enums.TagType;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.objects.adminlevel.AdminCCObject;
import com.github.vaerys.templates.TagAdminSubTagObject;
import org.apache.commons.lang3.StringUtils;

public class TagRepeat extends TagAdminSubTagObject {

    public TagRepeat(int priority, TagType... types) {
        super(priority, types);
    }

    @Override
    public String execute(String from, CommandObject command, String args, AdminCCObject cc) {
        try {
            String contents = getContents(from);
            int n = Integer.parseInt(getSubTag(from));
            if (n < 0 || n > 50) throw new NumberFormatException();

            String result = StringUtils.repeat(contents,n);

            return replaceFirstTag(from, result);
        } catch (NumberFormatException e) {
            return replaceFirstTag(from, error);
        }
    }

    @Override
    protected String subTagUsage() {
        return "Amount";
    }

    @Override
    public String tagName() {
        return "repeat";
    }

    @Override
    public String prefix() {
        prefix = "<" + name + ":([\\w| ]+?)>\\(";   // h
        return prefix;
    }

    @Override
    public String suffix() {
        suffix = "\\)";     // hh
        return suffix;
    }

    @Override
    protected int argsRequired() {
        return -1;
    }

    @Override
    protected String usage() {
        return "Text";
    }

    @Override
    protected String desc() {
        return "Repeats Text a specified Amount of times. Will not repeat text more than 50 times.";
    }
}
