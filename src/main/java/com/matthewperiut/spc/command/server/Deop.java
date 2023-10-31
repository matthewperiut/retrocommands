package com.matthewperiut.spc.command.server;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;

public class Deop implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length < 2) {
            manual(commandSource);
            return;
        }
        String playerName = parameters[1];
        ServerUtil.getConnectionManager().removeOp(playerName);
        ServerUtil.sendFeedbackAndLog(commandSource.getName(), "De-opping " + playerName);
        ServerUtil.getConnectionManager().sendMessage(commandSource.getName(), "§eYou are no longer op!");
    }

    @Override
    public String name() {
        return "deop";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /deop {player}");
        commandSource.sendFeedback("Info: Removes op status from a player");
    }

    @Override
    public boolean isOnlyServer() {
        return true;
    }
}
