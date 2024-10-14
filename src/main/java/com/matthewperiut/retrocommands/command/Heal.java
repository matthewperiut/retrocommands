package com.matthewperiut.retrocommands.command;

import com.matthewperiut.accessoryapi.api.PlayerExtraHP;
import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;


public class Heal implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {

        PlayerEntity player = commandSource.getPlayer();
        if (player == null) {
            return;
        }

        if (parameters.length > 1) {
            int amount = Integer.parseInt(parameters[1]);
            commandSource.sendFeedback((amount > 0 ? "Healed" : "Damaged") + " " + Math.abs(amount) / 2.f + " hearts!");
            player.health += amount;
            return;
        }

        commandSource.sendFeedback("Healed fully!");

        if (FabricLoader.getInstance().isModLoaded("accessoryapi")) {
            player.health = 20 + ((PlayerExtraHP)player).getExtraHP();
        }
        else
        {
            player.health = 20;
        }
    }

    @Override
    public String name() {
        return "heal";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /heal {optional: amount}");
        commandSource.sendFeedback("Info: Restores health");
    }
}
