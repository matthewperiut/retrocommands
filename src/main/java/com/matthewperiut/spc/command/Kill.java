package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.ParameterSuggestUtil;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.entity.player.PlayerBase;


public class Kill implements Command {

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        PlayerBase player = commandSource.getPlayer();
        if (player == null) {
            return;
        }

        player.damage(null, 1000);
        commandSource.sendFeedback("Killed player");
    }

    @Override
    public String name() {
        return "kill";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /kill");
        commandSource.sendFeedback("Info: Kills the player");
    }

    @Override
    public String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput) {
        return new String[0];
    }

    @Override
    public boolean needsPermissions() {
        return false;
    }
}
