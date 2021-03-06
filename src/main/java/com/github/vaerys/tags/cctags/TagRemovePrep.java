package com.github.vaerys.tags.cctags;

import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.enums.TagType;
import com.github.vaerys.main.Utility;
import com.github.vaerys.templates.TagObject;

public class TagRemovePrep extends TagObject {

    public TagRemovePrep(int priority, TagType... types) {
        super(priority, types);
    }

    @Override
    public String execute(String from, CommandObject command, String args) {
        return Utility.removePrep(from);
    }

    @Override
    public String tagName() {
        return "<dontSanitize>";
    }

    @Override
    public int argsRequired() {
        return 0;
    }

    @Override
    public boolean isPassive() {
        return true;
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public String desc() {
        return "This tag is used to tell the <args> tag not to sanitize the input.";
    }

    @Override
    public String handleTag(String from, CommandObject command, String args) {
        return execute(from, command, args);
    }
}
