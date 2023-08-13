package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.player.PlayerBase;

import java.util.Map;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class Mobs implements Command {
    @Override
    public void command(PlayerBase player, String[] parameters) {

        sendMessage("Mobs that can be spawned, Case-Sensitive usage!");
        Map<String, Class> map = EntityRegistry.STRING_ID_TO_CLASS;

        String msg = "";
        for (Map.Entry<String, Class> entry : map.entrySet()) {
            msg += "\"" + entry.getKey() + "\", ";
            if (msg.length() > 50) {
                sendMessage(msg);
                msg = "";
            }
        }
        sendMessage(msg.substring(0, msg.length() - 2)); // removes last comma then sends
    }

    @Override
    public String name() {
        return "mobs";
    }

    @Override
    public void manual() {
        sendMessage("Usage: /mobs");
        sendMessage("Info: gives the list of mobs");
    }
}
