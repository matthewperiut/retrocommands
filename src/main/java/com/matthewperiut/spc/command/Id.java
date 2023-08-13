package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import net.minecraft.entity.player.PlayerBase;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class Id implements Command {
    @Override
    public void command(PlayerBase player, String[] parameters) {
        if (parameters.length > 1) {
            int i = Give.nameToItemId(parameters[1]);
            if (i > -1) {
                sendMessage(parameters[1] + " has an id of " + i);
            }
            return;
        }
        manual();
    }

    @Override
    public String name() {
        return "id";
    }

    @Override
    public void manual() {
        sendMessage("Usage: /id {item name}");
        sendMessage("Info: get the ID number of a given item");
        sendMessage("item name: use the translated item name without spaces, or with '_' instead");
    }
}
