package com.periut.retrocommands.command.extra;

import com.periut.retrocommands.api.Command;
import com.periut.retrocommands.util.SharedCommandSource;
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

        player.health = 20 + getExtraHP(player);
    }

    private static int getExtraHP(PlayerEntity player) {
        if (!FabricLoader.getInstance().isModLoaded("accessoryapi")) {
            return 0;
        }
        try {
            Class<?> extraHPClass = Class.forName("com.periut.accessoryapi.api.PlayerExtraHP");
            if (extraHPClass.isInstance(player)) {
                Object result = extraHPClass.getMethod("getExtraHP").invoke(player);
                return ((Number) result).intValue();
            }
        } catch (Throwable ignored) {
        }
        return 0;
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
