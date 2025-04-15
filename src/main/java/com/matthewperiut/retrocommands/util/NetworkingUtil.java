package com.matthewperiut.retrocommands.util;

import com.matthewperiut.retrocommands.RetroCommands;
import com.matthewperiut.retrocommands.command.server.ServerUtil;
import net.glasslauncher.mods.networking.GlassPacketListener;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class NetworkingUtil implements GlassPacketListener {
    @Override
    public void registerGlassPackets() {
        registerGlassPacket("retrocommands:op", ((glassPacket, networkHandler) -> {
            if (!networkHandler.isServerSide()) {
                ClientNetworkHandler clientNetworkHandler = (ClientNetworkHandler) networkHandler;
                RetroCommands.mp_op = glassPacket.getNbt().getBoolean("op");
                RetroCommands.mp_spc = true;
            } else {
                ServerPlayNetworkHandler serverPlayNetworkHandler = (ServerPlayNetworkHandler) networkHandler;
                ServerUtil.informPlayerOpStatus(serverPlayNetworkHandler.getName());
            }
        }), true, true);

        registerGlassPacket("retrocommands:players", ((glassPacket, networkHandler) -> {
            if (!networkHandler.isServerSide()) {
                ClientNetworkHandler clientNetworkHandler = (ClientNetworkHandler) networkHandler;
                RetroCommands.player_names = glassPacket.getNbt().getString("players").split(",");
            }
        }), true, false);
    }
}
