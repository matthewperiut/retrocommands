package com.matthewperiut.spc.command.server;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.packet.play.UpdateSign0x82C2SPacket;

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
        ServerUtil.getConnectionManager().sendMessage(playerName, "§eYou are now op!");

        if (ServerUtil.getConnectionManager().getServerPlayer(playerName) != null) {
            // I would put this in a method... I would, but I'm informing SPC that you can autofill OP commands now
            String[] contents = new String[]{"", "", "", ""};
            contents[0] = "1";
            ServerUtil.getConnectionManager().trySendPacket(playerName, new UpdateSign0x82C2SPacket(0, -1, 0, contents));
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
