package com.matthewperiut.spc.util;

import com.matthewperiut.spc.mixin.access.ServerPlayerPacketHandlerAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.network.ServerPlayerPacketHandler;

public class SharedCommandSource {
    private final Object actualSource;

    public SharedCommandSource(Object source) {
        this.actualSource = source;
    }

    public void sendFeedback(String feedback) {
        EnvType side = FabricLoader.getInstance().getEnvironmentType();
        switch (side) {
            case CLIENT -> sendClientFeedback(feedback);
            case SERVER -> sendServerFeedback(feedback);
            default -> throw new IllegalArgumentException("Unknown side " + side + "!");
        }
    }

    public String getName() {
        if (actualSource instanceof PlayerBase player) {
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

    public PlayerBase getPlayer() {
        if (actualSource instanceof PlayerBase player) {
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
    private PlayerBase getPlayerClient() {
        return ((Minecraft) FabricLoader.getInstance().getGameInstance()).player;
    }

    @Environment(EnvType.SERVER)
    private PlayerBase getPlayerServer() {
        if (actualSource instanceof ServerPlayerPacketHandler playerPacketHandler) {
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
        if (actualSource instanceof CommandSource cmdSrc) {
            return cmdSrc.getName();
        }
        return "unknown";
    }

    @Environment(EnvType.CLIENT)
    private void sendClientFeedback(String feedback) {
        ((Minecraft) FabricLoader.getInstance().getGameInstance()).overlay.addChatMessage(feedback);
    }

    @Environment(EnvType.SERVER)
    private void sendServerFeedback(String feedback) {
        ((CommandSource) actualSource).sendFeedback(feedback);
    }

    public boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
}
