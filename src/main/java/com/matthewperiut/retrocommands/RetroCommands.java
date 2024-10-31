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
    @Override
    public void onInitialize() {
        RetroChatUtil.addDefaultCommands();
        VanillaMobs.setupSummons();
        mjf = FabricLoader.getInstance().isModLoaded("mojangfixstationapi");
    }
}
