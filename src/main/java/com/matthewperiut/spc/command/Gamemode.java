package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.optionaldep.bhcreative.ChangeGamemode;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerBase;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;


public class Gamemode implements Command {
    @Override
    public void command(PlayerBase player, String[] parameters) {
        if (!FabricLoader.getInstance().isModLoaded("bhcreative")) {
            sendMessage("Install BHCreative mod to use /gamemode");
            return;
        }

        if (parameters.length > 1) {
            if (parameters[1].charAt(0) == 's' || parameters[1].charAt(0) == '0') {
                sendMessage("Set game mode to Survival Mode");
                ChangeGamemode.set(player, false);
            } else if (parameters[1].charAt(0) == 'c' || parameters[1].charAt(0) == '1') {
                sendMessage("Set game mode to Creative Mode");
                ChangeGamemode.set(player, true);
            }
            return;
        }

        manual();
    }

    @Override
    public String name() {
        return "gamemode";
    }

    @Override
    public void manual() {
        sendMessage("Requires: BHCreative");
        sendMessage("Usage: /gamemode {mode}");
        sendMessage("Info: changes player's gamemode");
        sendMessage("mode can be 0/s/survival for survival mode");
        sendMessage("mode can be 1/c/creative for creative mode");
    }
}
