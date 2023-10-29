package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.server.command.CommandSource;

public class WhoAmI implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        commandSource.sendFeedback(commandSource.getName());
    }

    @Override
    public String name() {
        return "whoami";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /whoami");
        commandSource.sendFeedback("Tells you your name");
    }
}
