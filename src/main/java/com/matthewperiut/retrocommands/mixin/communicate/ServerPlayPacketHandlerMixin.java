package com.matthewperiut.retrocommands.mixin.communicate;

import com.matthewperiut.retrocommands.RetroCommands;
import com.matthewperiut.retrocommands.command.server.ServerUtil;
import net.glasslauncher.mods.networking.GlassPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Connection;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayPacketHandlerMixin {
    @Shadow public abstract void sendPacket(Packet arg);

    @Inject(method = "<init>", at = @At("TAIL"))
    void tellOp(MinecraftServer server, Connection connection, ServerPlayerEntity player, CallbackInfo ci) {

    }
}
