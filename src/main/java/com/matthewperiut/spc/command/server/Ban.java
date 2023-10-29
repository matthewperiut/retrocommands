package com.matthewperiut.spc.command.server;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.entity.player.ServerPlayer;

public class Ban implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length < 2)
        {
            manual(commandSource);
            return;
        }
        String var13 = parameters[1];
        ServerUtil.getConnectionManager().addBan(var13);
        ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Banning " + var13);
        ServerPlayer player = ServerUtil.getConnectionManager().getServerPlayer(var13);
        if (player != null) {
            player.packetHandler.kick("Banned by admin");
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
}
