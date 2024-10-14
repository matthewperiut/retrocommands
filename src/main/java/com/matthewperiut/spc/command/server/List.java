package com.matthewperiut.spc.command.server;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;

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
}
