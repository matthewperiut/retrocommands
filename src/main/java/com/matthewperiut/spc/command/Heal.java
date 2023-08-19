package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import net.minecraft.entity.player.PlayerBase;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class Heal implements Command {
    @Override
    public void command(PlayerBase player, String[] parameters) {

        if (parameters.length > 1)
        {
            int amount = Integer.parseInt(parameters[1]);
            sendMessage((amount > 0 ? "Healed" : "Damaged") + " " + Math.abs(amount) / 2.f + " hearts!");
            player.health += amount;
            return;
        }

        sendMessage("Healed fully!");
        player.health = 20;
    }

    @Override
    public String name() {
        return "heal";
    }

    @Override
    public void manual() {
        sendMessage("Usage: /heal {optional: amount}");
        sendMessage("Info: Restores health");
    }
}
