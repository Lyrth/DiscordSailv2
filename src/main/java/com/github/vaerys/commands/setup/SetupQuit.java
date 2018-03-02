package com.github.vaerys.commands.setup;

import com.github.vaerys.commands.CommandObject;
import com.github.vaerys.handlers.SetupHandler;
import com.github.vaerys.templates.SetupCommand;

public class SetupQuit extends SetupCommand {

    @Override
    public String execute(String args, CommandObject command) {
        SetupHandler.setSetupStage(command, (short) SetupHandler.SETUP_UNSET);
        return "> Setup Cancelled.";
    }

    @Override
    public String[] names() {
        return new String[]{"quit", "stop"};
    }

    @Override
    public String description(CommandObject command) {
        return "Stops setup and resets any progress on the setup.";
    }
}