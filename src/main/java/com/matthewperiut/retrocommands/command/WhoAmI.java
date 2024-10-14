package com.matthewperiut.retrocommands.command;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.util.SharedCommandSource;

public class WhoAmI implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        commandSource.sendFeedback(commandSource.getName());
    }

    @Override
    public String name() {
        return "whoami";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /whoami");
        commandSource.sendFeedback("Tells you your name");
    }

    public boolean needsPermissions() {
        return false;
    }
}
