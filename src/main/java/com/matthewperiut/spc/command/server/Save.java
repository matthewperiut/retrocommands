package com.matthewperiut.spc.command.server;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.minecraft.server.ServerPlayerConnectionManager;

public class Save implements Command {
    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (parameters.length < 2) {
            manual(commandSource);
            return;
        }

        ServerPlayerConnectionManager scm = ServerUtil.getConnectionManager();

        switch (parameters[1]) {
            case "all":
                ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Forcing save..");
                if (scm != null) {
                    scm.updateAllPlayers();
                }

                for (int i = 0; i < ServerUtil.getServer().levels.length; ++i) {
                    ServerUtil.getServer().levels[i].saveLevel(true, null);
                }

                ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Save complete.");
                break;
            case "off":
                ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Disabling level saving..");

                for (int i = 0; i < ServerUtil.getServer().levels.length; ++i) {
                    ServerUtil.getServer().levels[i].field_275 = true;
                }
                break;
            case "on":
                ServerUtil.sendFeedbackAndLog(commandSource.getName(), "Enabling level saving..");

                for (int i = 0; i < ServerUtil.getServer().levels.length; ++i) {
                    ServerUtil.getServer().levels[i].field_275 = false;
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
    public boolean isOnlyServer() {
        return true;
    }
}
