package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.optionaldep.bhcreative.ChangeGamemode;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.fabricmc.loader.api.FabricLoader;


public class Gamemode implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (!FabricLoader.getInstance().isModLoaded("bhcreative")) {
            commandSource.sendFeedback("Install BHCreative mod to use /gamemode");
            return;
        }

        if (commandSource.getPlayer() == null) {
            return;
        }

        if (parameters.length > 1) {
            if (parameters[1].charAt(0) == 's' || parameters[1].charAt(0) == '0') {
                commandSource.sendFeedback("Set game mode to Survival Mode");
                ChangeGamemode.set(commandSource.getPlayer(), false);
            } else if (parameters[1].charAt(0) == 'c' || parameters[1].charAt(0) == '1') {
                commandSource.sendFeedback("Set game mode to Creative Mode");
                ChangeGamemode.set(commandSource.getPlayer(), true);
            }
            return;
        }

        manual(commandSource);
    }

    @Override
    public String name() {
        return "gamemode";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Requires: BHCreative");
        commandSource.sendFeedback("Usage: /gamemode {mode}");
        commandSource.sendFeedback("Info: changes player's gamemode");
        commandSource.sendFeedback("mode can be 0/s/survival for survival mode");
        commandSource.sendFeedback("mode can be 1/c/creative for creative mode");
    }
}
