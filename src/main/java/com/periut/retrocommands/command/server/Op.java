package com.periut.retrocommands.command.server;

import com.periut.retrocommands.api.Command;
import com.periut.retrocommands.util.ServerUtil;
import com.periut.retrocommands.util.SharedCommandSource;

public class Op implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length < 2) {
            manual(commandSource);
            return;
        }
        String playerName = parameters[1];
        ServerUtil.getConnectionManager().addToOperators(playerName);
        ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Opping " + playerName);
        ServerUtil.getConnectionManager().messagePlayer(playerName, "§eYou are now op!");

        if (ServerUtil.getConnectionManager().getPlayer(playerName) != null) {
            ServerUtil.informPlayerOpStatus(playerName);
        }
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
    public boolean disableInSingleplayer() {
        return true;
    }
}
