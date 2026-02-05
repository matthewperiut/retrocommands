package com.periut.retrocommands.command.extra;

import com.periut.retrocommands.api.Command;
import com.periut.retrocommands.util.SharedCommandSource;

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
