package com.matthewperiut.retrocommands;

import com.matthewperiut.retrocommands.util.ConfigUtil;
import com.matthewperiut.retrocommands.util.RetroChatUtil;
import com.matthewperiut.retrocommands.util.VanillaMobs;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

public class RetroCommands implements ModInitializer {
    public static final String MOD_ID = "retrocommands";

    // other mods located
    public static boolean mojangFix = false;
    public static boolean cryConfig = false;
    public static boolean bhCreative = false;

    // Multiplayer
    public static boolean mp_spc = false;
    public static boolean mp_op = false;

    // String arrays for completions help
    public static String[] player_names = null;
    public static List<String> disabled_commands = List.of(new String[]{});

    @Override
    public void onInitialize() {
        mojangFix = FabricLoader.getInstance().isModLoaded("mojangfixstationapi");
        cryConfig = FabricLoader.getInstance().isModLoaded("cryonicconfig");
        bhCreative = FabricLoader.getInstance().isModLoaded("bhcreative");

        RetroChatUtil.addDefaultCommands();
        VanillaMobs.setupSummons();
    }
}
