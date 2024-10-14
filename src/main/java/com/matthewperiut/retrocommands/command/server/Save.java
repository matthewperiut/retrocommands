package com.matthewperiut.retrocommands.command.server;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import net.minecraft.server.PlayerManager;

public class Save implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length < 2) {
            manual(commandSource);
            return;
        }

        PlayerManager scm = ServerUtil.getConnectionManager();

        switch (parameters[1]) {
            case "all":
                ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Forcing save..");
                if (scm != null) {
                    scm.savePlayers();
                }

                for (int i = 0; i < ServerUtil.getServer().worlds.length; ++i) {
                    ServerUtil.getServer().worlds[i].saveWithLoadingDisplay(true, null);
                }

                ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Save complete.");
                break;
            case "off":
                ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Disabling level saving..");

                for (int i = 0; i < ServerUtil.getServer().worlds.length; ++i) {
                    ServerUtil.getServer().worlds[i].savingDisabled = true;
                }
                break;
            case "on":
                ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Enabling level saving..");

                for (int i = 0; i < ServerUtil.getServer().worlds.length; ++i) {
                    ServerUtil.getServer().worlds[i].savingDisabled = false;
                }
                break;
            default:
                break;
        }
    }

    @Override
    public String name() {
        return "save";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /save {all/off/on}");
        commandSource.sendFeedback("Info: save all | forces a server-wide level save");
        commandSource.sendFeedback("      save off | disables terrain saving (useful for backup scripts)");
        commandSource.sendFeedback("      save on  | re-enables terrain saving");
    }

    @Override
    public boolean disableInSingleplayer() {
        return true;
    }
}
