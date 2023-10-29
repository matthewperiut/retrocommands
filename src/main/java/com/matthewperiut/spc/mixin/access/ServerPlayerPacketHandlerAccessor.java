package com.matthewperiut.spc.mixin.access;

import net.minecraft.entity.player.ServerPlayer;
import net.minecraft.server.network.ServerPlayerPacketHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayerPacketHandler.class)
public interface ServerPlayerPacketHandlerAccessor {
    @Accessor("serverPlayer")
    ServerPlayer getServerPlayer();
}
