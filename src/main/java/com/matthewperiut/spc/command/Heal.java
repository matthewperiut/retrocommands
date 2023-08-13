package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import net.minecraft.entity.player.PlayerBase;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class Heal implements Command {
    @Override
    public void command(PlayerBase player, String[] parameters) {
        sendMessage("Healed!");
        player.health = 20;
    }

    @Override
    public String name() {
        return "heal";
    }

    @Override
    public void manual() {
        sendMessage("Usage: /heal");
        sendMessage("Info: Restores full health");
    }
}
