package com.matthewperiut.spc.command.server;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.server.ServerPlayerConnectionManager;

public class Kick implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length < 2) {
            manual(commandSource);
            return;
        }
        String message = ServerUtil.appendEnd(2, parameters);
        ServerPlayer player = null;
        ServerPlayerConnectionManager scm = ServerUtil.getConnectionManager();

        for (int i = 0; i < scm.players.size(); ++i) {
            ServerPlayer var9 = (ServerPlayer) scm.players.get(i);
            if (var9.name.equalsIgnoreCase(parameters[1])) {
                player = var9;
            }
        }

        if (player != null) {
            player.packetHandler.kick(parameters.length > 2 ? message : "Kicked by admin");
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
}
