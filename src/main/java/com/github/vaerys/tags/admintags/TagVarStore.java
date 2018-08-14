package com.github.vaerys.tags.admintags;

import com.github.vaerys.enums.TagType;
import com.github.vaerys.masterobjects.CommandObject;
import com.github.vaerys.templates.TagObject;

//  Actually an AdminCC but for prefix/suffix simplicity it's a plain tag.
public class TagVarStore extends TagObject {

    public TagVarStore(int priority, TagType... types) {
        super(priority, types);
    }

    @Override
    public String execute(String from, CommandObject command, String args) {
        return removeAllTag(from);
    }

    @Override
    public String tagName() {
        return "<varStore>";
    }

    @Override
    public String prefix() {
        return name + ";";
    }

    @Override
    public String suffix() {
        return "/v>";
    }

    @Override
    public int argsRequired() {
        return 1;
    }   // meh of course :3

    @Override
    public boolean isPassive() {
        return true;
    }

    @Override
    public String usage() {
        return "VarContents";
    }

    @Override
    public String desc() {
        return "Variable storage. Part of the CC contents, returns nothing to the output.";
    }

    @Override
    public String handleTag(String from, CommandObject command, String args) {
        return removeAllTag(from);
    }
}