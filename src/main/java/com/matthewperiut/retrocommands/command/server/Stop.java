package com.matthewperiut.retrocommands.command.server;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.util.ServerUtil;
import com.matthewperiut.retrocommands.util.SharedCommandSource;

public class Stop implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Stopping the server..");
        ServerUtil.getServer().stop();
    }

    @Override
    public String name() {
        return "stop";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /stop");
        commandSource.sendFeedback("Info: Gracefully stops the server");
    }

    @Override
    public boolean disableInSingleplayer() {
        return true;
    }
}
