package com.matthewperiut.retrocommands.command.optional;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import com.periut.cryonicconfig.UtilityCryonicConfig;

public class ReloadCryonicConfig implements Command {

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        UtilityCryonicConfig.init(System.getProperty("user.dir"));
        commandSource.sendFeedback("Cryonic Config has been refreshed!");
    }

    @Override
    public String name() {
        return "reloadcryonicconfig";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /reloadcryonicconfig");
        commandSource.sendFeedback("Info: Refreshes the config cache for Cryonic Config");
    }
}
