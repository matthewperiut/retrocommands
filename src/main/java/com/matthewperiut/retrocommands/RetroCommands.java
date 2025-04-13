package com.matthewperiut.retrocommands;

import com.matthewperiut.retrocommands.util.RetroChatUtil;
import com.matthewperiut.retrocommands.util.VanillaMobs;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class RetroCommands implements ModInitializer {
    public static boolean mjf = false;
    public static boolean mp_spc = false;
    public static boolean mp_op = false;
    public static String[] player_names = null;
    public static boolean cc = false;
    public static String[] disabled_commands;
    public static final String MOD_ID = "retrocommands";
    @Override
    public void onInitialize() {
        mjf = FabricLoader.getInstance().isModLoaded("mojangfixstationapi");
        cc = FabricLoader.getInstance().isModLoaded("cryonicconfig");
        RetroChatUtil.addDefaultCommands();
        VanillaMobs.setupSummons();
    }
}
