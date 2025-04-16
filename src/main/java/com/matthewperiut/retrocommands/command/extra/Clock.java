package com.matthewperiut.retrocommands.command.extra;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.minecraft.entity.player.PlayerEntity;


public class Clock implements Command {
    public void command(SharedCommandSource commandSource, String[] parameters) {
        PlayerEntity player = commandSource.getPlayer();
        if (player == null) {
            return;
        }

        commandSource.sendFeedback("Time is " + String.valueOf(player.world.getTime()));
        commandSource.sendFeedback("Days: " + String.valueOf((int) (player.world.getTime() / 24000)));
    }

    @Override
    public String name() {
        return "clock";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /clock");
        commandSource.sendFeedback("Info: tells you the time in-game");
    }

    @Override
    public boolean needsPermissions() {
        return false;
    }
}
