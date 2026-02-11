package com.periut.retrocommands.util;

import com.periut.retrocommands.RetroCommands;
import com.periut.retrocommands.RetroCommandsNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.ornithemc.osl.networking.api.client.ClientPlayNetworking;

import java.util.List;

public class NetworkingUtil implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerListener(RetroCommandsNetworking.OP_CHANNEL, (ctx, buffer) -> {
            ctx.ensureOnMainThread();
            RetroCommands.mp_op = buffer.readBoolean();
            RetroCommands.mp_rc = true;
        });

        ClientPlayNetworking.registerListener(RetroCommandsNetworking.PLAYERS_CHANNEL, (ctx, buffer) -> {
            ctx.ensureOnMainThread();
            RetroCommands.player_names = buffer.readString().split(",");
        });

        ClientPlayNetworking.registerListener(RetroCommandsNetworking.DISABLED_CHANNEL, (ctx, buffer) -> {
            ctx.ensureOnMainThread();
            RetroCommands.disabled_commands = List.of(buffer.readString().split(","));
        });
    }
}
