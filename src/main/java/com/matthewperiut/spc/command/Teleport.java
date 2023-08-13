package com.matthewperiut.spc.command;

import com.matthewperiut.spc.api.Command;
import com.matthewperiut.spc.api.PosParse;
import com.matthewperiut.spc.optionaldep.stapi.SwitchDimension;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.entity.player.ServerPlayer;

import static com.matthewperiut.spc.util.SPChatUtil.sendMessage;

public class Teleport implements Command {
    @Override
    public void command(PlayerBase player, String[] parameters) {
        if (parameters.length == 4 || parameters.length == 5) {
            PosParse pos = new PosParse(player, 1, parameters);

            if (!pos.valid) {
                sendMessage("Non-number position");
                return;
            }

            if (parameters.length == 5) {
                if (FabricLoader.getInstance().isModLoaded("station-dimensions-v0")) {
                    SwitchDimension.go(player, parameters[4]);
                } else {
                    sendMessage("Cannot switch dimensions without Station API Dimensions");
                }
            }

            sendMessage("Teleporting to " + pos);
            if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
                player.setPosition(pos.x, pos.y, pos.z);
            } else {
                ServerPlayer sp = (ServerPlayer) player;
                sp.packetHandler.method_832(pos.x, pos.y, pos.z, player.yaw, player.pitch);
            }

            return;
        }

        manual();
    }

    @Override
    public String name() {
        return "tp";
    }

    @Override
    public void manual() {
        sendMessage("Usage: /tp {x} {y} {z} {optional: dimension identifier}");
        sendMessage("Info: moves a player to a desired coordinate and/or dimension");
        sendMessage("dimension identifier: e.g. \"minecraft:overworld\"");
    }
}
