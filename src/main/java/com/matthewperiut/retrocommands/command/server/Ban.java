package com.matthewperiut.retrocommands.command.server;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.util.ServerUtil;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

public class Ban implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length < 2) {
            manual(commandSource);
            return;
        }
        String var13 = parameters[1];
        ServerUtil.getConnectionManager().banPlayer(var13);
        ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Banning " + var13);
        ServerPlayerEntity player = ServerUtil.getConnectionManager().getPlayer(var13);
        if (player != null) {
            player.networkHandler.disconnect("Banned by admin");
        }
    }

    @Override
    public String name() {
        return "ban";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /ban {player}");
        commandSource.sendFeedback("Info: Bans a player from the server");
    }

    @Override
    public boolean disableInSingleplayer() {
        return true;
    }
}
