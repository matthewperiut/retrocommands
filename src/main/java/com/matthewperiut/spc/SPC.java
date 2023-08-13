package com.matthewperiut.spc;

import com.matthewperiut.spc.util.SPChatUtil;
import net.fabricmc.api.ModInitializer;

public class SPC implements ModInitializer {
    @Override
    public void onInitialize() {
        SPChatUtil.addDefaultCommands();
    }
}
