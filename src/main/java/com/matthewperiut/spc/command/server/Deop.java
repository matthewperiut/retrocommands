package com.matthewperiut.spc.command.server;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.network.packet.play.UpdateSignPacket;

public class Deop implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length < 2) {
            manual(commandSource);
            return;
        }
        String playerName = parameters[1];
        ServerUtil.getConnectionManager().removeFromOperators(playerName);
        ServerUtil.sendFeedbackAndLog(commandSource.getName(), "De-opping " + playerName);
        ServerUtil.getConnectionManager().messagePlayer(playerName, "§eYou are no longer op!");

        if (ServerUtil.getConnectionManager().getPlayer(playerName) != null) {
            // I would put this in a method... I would, but I'm informing SPC that you can autofill OP commands now
            String[] contents = new String[]{"", "", "", ""};
            contents[0] = "0";
            ServerUtil.getConnectionManager().sendPacket(playerName, new UpdateSignPacket(0, -1, 0, contents));
        }
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
    public boolean disableInSingleplayer() {
        return true;
    }
}
