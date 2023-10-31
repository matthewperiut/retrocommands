package com.matthewperiut.spc.command.server;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;

public class Op implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length < 2) {
            manual(commandSource);
            return;
        }
        String playerName = parameters[1];
        ServerUtil.getConnectionManager().addOp(playerName);
        ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Opping " + playerName);
        ServerUtil.getConnectionManager().sendMessage(commandSource.getName(), "§eYou are now op!");
    }

    @Override
    public String name() {
        return "op";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /op {player}");
        commandSource.sendFeedback("Info: Turns a player into an op");
    }

    @Override
    public boolean isOnlyServer() {
        return true;
    }
}
