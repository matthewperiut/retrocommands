package com.periut.retrocommands.command.server;

import com.periut.retrocommands.api.Command;
import com.periut.retrocommands.util.ServerUtil;
import com.periut.retrocommands.util.SharedCommandSource;

public class List implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        commandSource.sendFeedback("Connected players: " + ServerUtil.getConnectionManager().getPlayerList());
    }

    @Override
    public String name() {
        return "list";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /list");
        commandSource.sendFeedback("Info: Lists all currently connected players");
    }

    @Override
    public boolean disableInSingleplayer() {
        return true;
    }

    @Override
    public boolean needsPermissions() {
        return false;
    }
}
