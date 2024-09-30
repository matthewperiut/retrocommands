package com.matthewperiut.spc.command.server;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.ParameterSuggestUtil;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.packet.play.ChatMessage0x3Packet;

import java.util.Arrays;

public class Tell implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length > 3) {
            String a = parameters[1];
            String b = "";
            for (int i = 2; i < parameters.length; i++) {
                b += parameters[i];
            }
            String message = "§7" + commandSource.getName() + " whispers " + b;
            System.out.println(message + " to " + a);
            if (!ServerUtil.getConnectionManager().trySendPacket(a, new ChatMessage0x3Packet(message))) {
                commandSource.sendFeedback("§cThere's no player by that name online.");
            }
        }
    }

    @Override
    public String name() {
        return "tell";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /tell {player} <message>");
        commandSource.sendFeedback("Whisper to another player");
    }

    @Override
    public String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput) {
        if (parameterNum == 1) {
            System.out.println(currentInput);
            System.out.println(Arrays.toString(ParameterSuggestUtil.suggestPlayerName(currentInput)));
        }
        return new String[0];
    }
}
