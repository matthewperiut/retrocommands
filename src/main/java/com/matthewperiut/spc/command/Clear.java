package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class Clear implements Command {

    @Override
    public void command(PlayerBase player, String[] parameters) {
        ((Minecraft) FabricLoader.getInstance().getGameInstance()).overlay.clearChat();
    }

    @Override
    public String name() {
        return "clear";
    }

    @Override
    public void manual() {
        sendMessage("Usage: /clear");
        sendMessage("Info: Clears chat history");
    }
}
