package com.periut.retrocommands.util;

import com.periut.retrocommands.mixin.access.ServerPlayerPacketHandlerAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class SharedCommandSource {
    private final Object actualSource;

    public SharedCommandSource(Object source) {
        this.actualSource = source;
    }

    public void sendFeedback(String feedback) {
        EnvType side = FabricLoader.getInstance().getEnvironmentType();
        switch (side) {
            case CLIENT -> sendClientFeedback("§7" + feedback);
            case SERVER -> sendServerFeedback(feedback);
            default -> throw new IllegalArgumentException("Unknown side " + side + "!");
        }
    }

    public String getName() {
        if (actualSource instanceof PlayerEntity player) {
            return player.name;
        } else {
            EnvType side = FabricLoader.getInstance().getEnvironmentType();
            if (side == EnvType.CLIENT) {
                return getNameClient();
            }
            if (side == EnvType.SERVER) {
                return getNameServer();
            }
        }
        return "unknown";
    }

    public PlayerEntity getPlayer() {
        if (actualSource instanceof PlayerEntity player) {
            return player;
        } else {
            EnvType side = FabricLoader.getInstance().getEnvironmentType();
            if (side == EnvType.CLIENT) {
                return getPlayerClient();
            }
            if (side == EnvType.SERVER) {
                return getPlayerServer();
            }
        }
        return null;
    }

    @Environment(EnvType.CLIENT)
    private PlayerEntity getPlayerClient() {
        return ((Minecraft) FabricLoader.getInstance().getGameInstance()).player;
    }

    @Environment(EnvType.SERVER)
    private PlayerEntity getPlayerServer() {
        if (actualSource instanceof ServerPlayNetworkHandler playerPacketHandler) {
            return ((ServerPlayerPacketHandlerAccessor) playerPacketHandler).getServerPlayer();
        } else {
            return null;
        }
    }

    @Environment(EnvType.CLIENT)
    private String getNameClient() {
        return "client";
    }

    @Environment(EnvType.SERVER)
    private String getNameServer() {
        if (actualSource instanceof CommandOutput cmdSrc) {
            return cmdSrc.getName();
        }
        return "unknown";
    }

    @Environment(EnvType.CLIENT)
    private void sendClientFeedback(String feedback) {
        ((Minecraft) FabricLoader.getInstance().getGameInstance()).inGameHud.addChatMessage(feedback);
    }

    @Environment(EnvType.SERVER)
    private void sendServerFeedback(String feedback) {
        ((CommandOutput) actualSource).sendMessage(feedback);
    }

    public boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
}
