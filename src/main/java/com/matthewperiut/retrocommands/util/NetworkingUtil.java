package com.matthewperiut.retrocommands.util;

import com.matthewperiut.retrocommands.RetroCommands;
import net.glasslauncher.mods.networking.GlassPacketListener;
import net.minecraft.server.network.ServerPlayNetworkHandler;

import java.util.List;

public class NetworkingUtil implements GlassPacketListener {
    @Override
    public void registerGlassPackets() {
        registerGlassPacket("retrocommands:op", ((glassPacket, networkHandler) -> {
            RetroCommands.mp_op = glassPacket.getNbt().getBoolean("op");
            RetroCommands.mp_spc = true;
        }), true, false);

        registerGlassPacket("retrocommands:players", ((glassPacket, networkHandler) -> {
            RetroCommands.player_names = glassPacket.getNbt().getString("players").split(",");
        }), true, false);

        registerGlassPacket("retrocommands:disabled", ((glassPacket, networkHandler) -> {
            RetroCommands.disabled_commands = List.of(glassPacket.getNbt().getString("disabled").split(","));
        }), true, false);
    }
}
