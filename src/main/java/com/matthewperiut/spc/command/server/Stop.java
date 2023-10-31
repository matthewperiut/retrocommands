package com.matthewperiut.spc.command.server;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;

public class Stop implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Stopping the server..");
        ServerUtil.getServer().stopRunning();
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
    public boolean isOnlyServer() {
        return true;
    }
}
