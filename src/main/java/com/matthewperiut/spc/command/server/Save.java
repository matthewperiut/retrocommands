package com.matthewperiut.spc.command.server;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.util.ProgressListener;

public class Save implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        switch (parameters[1])
        {
            case "all":
                break;
            case "off":
                break;
            case "on":
                break;
            default:
                break;
        }

    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /save {all/off/on}");
        commandSource.sendFeedback("Info: save all | forces a server-wide level save");
        commandSource.sendFeedback("      save off | disables terrain saving (useful for backup scripts)");
        commandSource.sendFeedback("      save on  | re-enables terrain saving");
    }
}
