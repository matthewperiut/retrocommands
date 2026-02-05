package com.periut.retrocommands.command.server;

import com.periut.retrocommands.api.Command;
import com.periut.retrocommands.util.ServerUtil;
import com.periut.retrocommands.util.SharedCommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.PlayerManager;

public class Kick implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length < 2) {
            manual(commandSource);
            return;
        }
        String message = ServerUtil.appendEnd(2, parameters);
        ServerPlayerEntity player = null;
        PlayerManager scm = ServerUtil.getConnectionManager();

        for (int i = 0; i < scm.players.size(); ++i) {
            ServerPlayerEntity var9 = (ServerPlayerEntity) scm.players.get(i);
            if (var9.name.equalsIgnoreCase(parameters[1])) {
                player = var9;
            }
        }

        if (player != null) {
            player.networkHandler.disconnect(parameters.length > 2 ? message : "Kicked by admin");
            ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Kicking " + player.name);
        } else {
            commandSource.sendFeedback("Can't find user " + message + ". No kick.");
        }
    }

    @Override
    public String name() {
        return "kick";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /kick {player}");
        commandSource.sendFeedback("Info: Removes a player from the server");
    }

    @Override
    public boolean disableInSingleplayer() {
        return true;
    }
}
