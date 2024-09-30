package com.matthewperiut.spc;

import com.matthewperiut.spc.util.SPChatUtil;
import com.matthewperiut.spc.util.VanillaMobs;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class SPC implements ModInitializer {
    public static boolean mjf = false;
    public static boolean mp_spc = false;
    public static boolean mp_op = false;
    @Override
    public void onInitialize() {
        SPChatUtil.addDefaultCommands();
        VanillaMobs.setupSummons();
        mjf = FabricLoader.getInstance().isModLoaded("mojangfixstationapi");
    }
}
