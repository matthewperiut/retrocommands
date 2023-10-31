package com.matthewperiut.spc.command.server;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;

public class Pardon implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length < 2) {
            manual(commandSource);
            return;
        }
        ServerUtil.getConnectionManager().removeBan(parameters[1]);
        ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Pardoning " + parameters[1]);
    }

    @Override
    public String name() {
        return "pardon";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /pardon {player}");
        commandSource.sendFeedback("Info: Pardons a banned player so that they can connect again");
    }

    @Override
    public boolean isOnlyServer() {
        return true;
    }
}
