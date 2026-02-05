package com.periut.retrocommands.util;

import com.periut.retrocommands.RetroCommands;
import com.periut.cryonicconfig.CryonicConfig;

public class ConfigUtil {
    public static void refreshDisabledCommands() {
        RetroCommands.disabled_commands = java.util.List.of(CryonicConfig.getConfig(RetroCommands.MOD_ID).getString("disabledCommands", "").split(","));
    }
}
