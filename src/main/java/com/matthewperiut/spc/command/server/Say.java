package com.matthewperiut.spc.command.server;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.packet.play.ChatMessage0x3Packet;

public class Say implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length < 2) {
            manual(commandSource);
            return;
        }
        String joinedString = ServerUtil.appendEnd(1, parameters);
        ServerUtil.LOGGER.info("[" + commandSource.getName() + "] " + joinedString);
        ServerUtil.getConnectionManager().sendToAll(new ChatMessage0x3Packet("§d[Server] " + joinedString));
    }

    @Override
    public String name() {
        return "say";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /say");
        commandSource.sendFeedback("Info: Broadcasts a message to all players");
    }

    @Override
    public boolean isOnlyServer() {
        return true;
    }
}
