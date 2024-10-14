package com.matthewperiut.retrocommands.command;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.optionaldep.bhcreative.ChangeGamemode;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;


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

    @Override
    public String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput) {
        if (parameterNum == 1)
        {
            String[] options = {"survival", "creative"};
            ArrayList<String> output = new ArrayList<>();
            for (String option : options)
            {
                if (option.startsWith(currentInput))
                {
                    output.add(option.substring(currentInput.length()));
                }
            }
            return output.toArray(new String[0]);
        }
        return new String[0];
    }
}
