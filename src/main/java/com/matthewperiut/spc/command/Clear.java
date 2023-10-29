package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.util.SharedCommandSource;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;


public class Clear implements Command {

    @Override
    public void command(SharedCommandSource commandSource, String[] parameters) {
        if (commandSource.isClient())
            ((Minecraft) FabricLoader.getInstance().getGameInstance()).overlay.clearChat();
        else
            commandSource.sendFeedback("Intended to clear chat for client only");
    }

    @Override
    public String name() {
        return "clear";
    }

    @Override
    public void manual(SharedCommandSource commandSource) {
        commandSource.sendFeedback("Usage: /clear");
        commandSource.sendFeedback("Info: Clears chat history");
    }
}
